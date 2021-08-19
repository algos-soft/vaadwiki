package it.algos.vaadflow14.backend.packages.crono.giorno;

import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.crono.mese.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * First time: ven, 25-dic-2020
 * Last doc revision: mer, 19-mag-2021 alle 18:38 <br>
 * <p>
 * Classe (facoltativa) di un package con personalizzazioni <br>
 * Se manca, usa la classe EntityService <br>
 * Layer di collegamento tra il 'backend' e mongoDB <br>
 * Mantiene lo 'stato' della classe AEntity ma non mantiene lo stato di un'istanza entityBean <br>
 * L' istanza (SINGLETON) viene creata alla partenza del programma <br>
 * <p>
 * Annotated with @Service (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per iniettare questo singleton nel costruttore di xxxLogicList <br>
 * Annotated with @Scope (obbligatorio con SCOPE_SINGLETON) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
//Spring
@Service
//Spring
@Qualifier("crono/giornoService")
//Spring
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//Algos
@AIScript(sovraScrivibile = false, doc = AEWizDoc.inizioRevisione)
public class GiornoService extends AService {


    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin nel costruttore <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    private MeseService meseService;

    /**
     * Costruttore @Autowired. <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore <br>
     * Se ci sono DUE o più costruttori, va in errore <br>
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public GiornoService(final MeseService meseService) {
        super(Giorno.class);
        this.meseService = meseService;
    }


    /**
     * Crea e registra una entityBean col flag reset=true <br>
     *
     * @param titolo (obbligatorio, unico)
     * @param ordine (obbligatorio, unico)
     * @param mese   di riferimento (obbligatorio)
     *
     * @return true se la entity è stata creata e salvata
     */
    private boolean creaReset(final int ordine, final String titolo, final Mese mese) {
        return super.creaReset(newEntity(ordine, titolo, mese));
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Giorno newEntity() {
        return newEntity(0, VUOTA, (Mese) null);
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param ordine (obbligatorio, unico)
     * @param titolo (obbligatorio, unico)
     * @param mese   di riferimento (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Giorno newEntity(final int ordine,final String titolo,final Mese mese) {
        Giorno newEntityBean = Giorno.builderGiorno()
                .ordine(ordine > 0 ? ordine : getNewOrdine())
                .titolo(text.isValid(titolo) ? titolo : null)
                .mese(mese)
                .build();

        return (Giorno) fixKey(newEntityBean);
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param keyID must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public Giorno findById(final String keyID) {
        return (Giorno) super.findById(keyID);
    }


    /**
     * Retrieves an entity by its keyProperty.
     *
     * @param keyValue must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public Giorno findByKey(final String keyValue) {
        return (Giorno) super.findByKey(keyValue);
    }


    private AIResult checkMese() {
        String packageName = Giorno.class.getSimpleName().toLowerCase();
        String collection = "mese";

        if (mongo.isValid(collection)) {
            return AResult.valido(String.format("Nel package %s la collezione %s esiste già e non è stata modificata", packageName, collection));
        }
        else {
            if (meseService == null) {
                return AResult.errato("Manca la classe MeseService");
            }
            else {
                return meseService.reset();
            }
        }
    }


    /**
     * Creazione o ricreazione di alcuni dati iniziali standard <br>
     * Invocato dal bottone Reset di alcune liste <br>
     * <p>
     * I dati possono essere: <br>
     * 1) recuperati da una Enumeration interna <br>
     * 2) letti da un file CSV esterno <br>
     * 3) letti da Wikipedia <br>
     * 4) creati direttamente <br>
     * DEVE essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @return wrapper col risultato ed eventuale messaggio di errore
     */
    @Override
    public AIResult reset() {
        AIResult result = super.reset();
        int numRec = 0;
        AIResult resultCollectionPropedeutica;
        int ordine;
        String titolo;
        String titoloMese;
        List<HashMap> lista;
        Mese mese;

        if (result.isErrato()) {
            return result;
        }

        resultCollectionPropedeutica = checkMese();
        if (resultCollectionPropedeutica.isValido()) {
            logger.log(AETypeLog.checkData, resultCollectionPropedeutica.getMessage());
        }
        else {
            return resultCollectionPropedeutica;
        }

        //costruisce i 366 records
        lista = date.getAllGiorni();
        for (HashMap mappaGiorno : lista) {
            titolo = (String) mappaGiorno.get(KEY_MAPPA_GIORNI_TITOLO);
            titoloMese = (String) mappaGiorno.get(KEY_MAPPA_GIORNI_MESE_TESTO);
            mese = (Mese) mongo.findById(Mese.class, titoloMese);
            ordine = (int) mappaGiorno.get(KEY_MAPPA_GIORNI_BISESTILE);

            numRec = creaReset(ordine, titolo, mese)  ? numRec + 1 : numRec;
        }

        return AResult.valido(AETypeReset.hardCoded.get(), numRec);
    }

}// end of Singleton class}