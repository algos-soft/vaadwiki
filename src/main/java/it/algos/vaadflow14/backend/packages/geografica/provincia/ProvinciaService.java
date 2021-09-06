package it.algos.vaadflow14.backend.packages.geografica.provincia;

import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.geografica.regione.*;
import it.algos.vaadflow14.backend.packages.geografica.stato.*;
import it.algos.vaadflow14.backend.service.*;
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
@Qualifier("geografica/provinciaService")
//Spring
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//Algos
@AIScript(sovraScrivibile = false, doc = AEWizDoc.inizioRevisione)
public class ProvinciaService extends AService {


    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin nel costruttore <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    RegioneService regioneService;

    /**
     * Costruttore @Autowired. <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore <br>
     * Se ci sono DUE o più costruttori, va in errore <br>
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public ProvinciaService(final RegioneService regioneService) {
        super(Provincia.class);
        this.regioneService = regioneService;
    }


    /**
     * Crea e registra una entityBean col flag reset=true <br>
     *
     * @param nome    (obbligatorio, unico)
     * @param sigla   (consuetudinaria, obbligatoria)
     * @param regione (obbligatorio)
     * @param stato   (obbligatorio)
     * @param iso     di riferimento (obbligatorio, unico)
     * @param status  (obbligatorio)
     *
     * @return true se la entity è stata creata e salvata
     */
    private boolean creaReset(final String nome, final String sigla, final Regione regione, final Stato stato, final String iso, final AETypeProvincia status) {
        return super.creaReset(newEntity(nome, sigla, regione, stato, iso, status));
    }


    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    @Override
    public Provincia newEntity() {
        return newEntity(VUOTA, VUOTA, (Regione) null, (Stato) null, VUOTA, (AETypeProvincia) null);
    }


    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param nome    (obbligatorio, unico)
     * @param sigla   (consuetudinaria, obbligatoria)
     * @param regione (obbligatorio)
     * @param stato   (obbligatorio)
     * @param iso     di riferimento (obbligatorio, unico)
     * @param status  (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Provincia newEntity(final String nome, final String sigla, final Regione regione, final Stato stato, final String iso, final AETypeProvincia status) {
        Provincia newEntityBean = Provincia.builderProvincia()
                .nome(text.isValid(nome) ? nome : null)
                .sigla(text.isValid(sigla) ? sigla : null)
                .regione(regione)
                .stato(stato)
                .iso(text.isValid(iso) ? iso : null)
                .status(status != null ? status : AETypeProvincia.provincia)
                .build();

        return (Provincia) fixKey(newEntityBean);
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
    public Provincia findById(final String keyID) throws AMongoException {
        return (Provincia) super.findById(keyID);
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
    public Provincia findByKey(final String keyValue) throws AMongoException {
        return (Provincia) super.findByKey(keyValue);
    }


    private AIResult checkRegione() {
        String packageName = Provincia.class.getSimpleName().toLowerCase();
        String collection = "regione";

        if (((MongoService) mongo).isValidCollection(collection)) {//@todo da controllare
            return AResult.valido(String.format("Nel package %s la collezione %s esiste già e non è stata modificata", packageName, collection));
        }
        else {
            if (regioneService == null) {
                return AResult.errato("Manca la classe RegioneService");
            }
            else {
                return regioneService.reset();
            }
        }
    }


    private AIResult creaProvinceItaliane() {
        AIResult result = AResult.errato();
        List<WrapQuattro> listaWrap;
        Provincia provincia;
        String nome;
        String sigla;
        String regioneTxt;
        Regione regione = null;
        Stato stato = AEStato.italia.getStato();
        String iso;
        AETypeProvincia status = null;

        listaWrap = geografic.getProvince();
        if (listaWrap != null && listaWrap.size() > 0) {
            for (WrapQuattro wrap : listaWrap) {
                nome = wrap.getSeconda();
                sigla = wrap.getPrima();
                iso = sigla;
                regioneTxt = wrap.getTerza().toLowerCase();

                try {
                    regione = regioneService.findById(regioneTxt);
                    status = AETypeProvincia.findByIso(Integer.parseInt(regione.sigla));
                    status = wrap.isValido() ? AETypeProvincia.metropolitana : status;
                } catch (AMongoException unErrore) {
                    logger.warn(unErrore, this.getClass(), "creaProvinceItaliane");
                }

                if (text.isValid(nome) && stato != null && regione != null && text.isValid(iso) && text.isValid(sigla)) {
                    if (creaReset(nome, sigla, regione, stato, iso, status)) {
                    }
                    else {
                        logger.log(String.format("Provincia non registrata. Nome:%s, Sigla:%s, Regione:%s", nome, sigla, regioneTxt));
                    }
                }
                else {
                    logger.log(String.format("Mancano dati essenziali. Nome:%s, Sigla:%s, Regione:%s", nome, sigla, regioneTxt));
                }
            }
            result = AResult.valido("Province italiane: ");
        }
        else {
            result = AResult.errato("Non sono riuscito a trovare la table nella pagina di wikipedia 'Province d'Italia'");
        }

        return result;
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

        if (result.isErrato()) {
            return result;
        }
        resultCollectionPropedeutica = checkRegione();
        if (resultCollectionPropedeutica.isValido()) {
            logger.log(AETypeLog.checkData, resultCollectionPropedeutica.getMessage());
        }
        else {
            return resultCollectionPropedeutica;
        }

        result = creaProvinceItaliane();
        return AResult.valido(AETypeReset.wikipedia.get(), numRec);
    }

}// end of Singleton class