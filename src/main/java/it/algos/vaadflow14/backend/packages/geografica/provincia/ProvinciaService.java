package it.algos.vaadflow14.backend.packages.geografica.provincia;

import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.geografica.regione.*;
import it.algos.vaadflow14.backend.packages.geografica.stato.*;
import it.algos.vaadflow14.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 25-dic-2020
 * Time: 20:29
 * <p>
 * Classe (facoltativa) di un package con personalizzazioni <br>
 * Se manca, si usa la classe EntityService <br>
 * Layer di collegamento tra il 'backend' e mongoDB <br>
 * Mantiene lo 'stato' della classe AEntity ma non mantiene lo stato di un'istanza entityBean <br>
 * L' istanza (SINGLETON) viene creata alla partenza del programma <br>
 * <p>
 * Annotated with @Service (obbligatorio) <br>
 * Annotated with @Scope (obbligatorio con SCOPE_SINGLETON) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@Service
@Qualifier("provinciaService")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovraScrivibile = false)
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
     * Crea e registra una entity solo se non esisteva <br>
     *
     * @param ordine  (obbligatorio, unico)
     * @param nome    (obbligatorio, unico)
     * @param sigla   (consuetudinaria, obbligatoria)
     * @param regione (obbligatorio)
     * @param stato   (obbligatorio)
     * @param iso     di riferimento (obbligatorio, unico)
     * @param status  (obbligatorio)
     *
     * @return la nuova entity appena creata e salvata
     */
    public Provincia creaIfNotExist(final int ordine, final String nome, final String sigla, final Regione regione, final Stato stato, final String iso, final AETypeProvincia status) {
        return (Provincia) checkAndSave(newEntity(ordine, nome, sigla, regione, stato, iso, status));
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
        return newEntity(0, VUOTA, VUOTA, (Regione) null, (Stato) null, VUOTA, (AETypeProvincia) null);
    }


    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param ordine  (obbligatorio, unico)
     * @param nome    (obbligatorio, unico)
     * @param sigla   (consuetudinaria, obbligatoria)
     * @param regione (obbligatorio)
     * @param stato   (obbligatorio)
     * @param iso     di riferimento (obbligatorio, unico)
     * @param status  (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Provincia newEntity(final int ordine, final String nome, final String sigla, final Regione regione, final Stato stato, final String iso, final AETypeProvincia status) {
        Provincia newEntityBean = Provincia.builderProvincia()
                .ordine(ordine > 0 ? ordine : getNewOrdine())
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
    public Provincia findById(final String keyID) {
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
    @Override
    public Provincia findByKey(final String keyValue) {
        return (Provincia) super.findByKey(keyValue);
    }

    /**
     * Creazione o ricreazione di alcuni dati iniziali standard <br>
     * Invocato in fase di 'startup' e dal bottone Reset di alcune liste <br>
     * <p>
     * 1) deve esistere lo specifico metodo sovrascritto
     * 2) deve essere valida la entityClazz
     * 3) deve esistere la collezione su mongoDB
     * 4) la collezione non deve essere vuota
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
    public AIResult resetEmptyOnly() {
        AIResult result = super.resetEmptyOnly();
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

        return creaProvinceItaliane();
    }


    private AIResult checkRegione() {
        String collection = "regione";

        if (regioneService == null) {
            regioneService = appContext.getBean(RegioneService.class);
        }

        if (mongo.isValid(collection)) {
            return AResult.valido("La collezione " + collection + " esiste già e non è stata modificata");
        }
        else {
            if (regioneService == null) {
                return AResult.errato("Manca la classe RegioneService");
            }
            else {
                return regioneService.resetEmptyOnly();
            }
        }
    }


    private AIResult creaProvinceItaliane() {
        AIResult result = AResult.errato();
        List<WrapTreStringhe> listaWrap;
        Provincia provincia;
        int ordine = 0;
        String nome;
        String sigla;
        String regioneTxt;
        Regione regione;
        Stato stato = AEStato.italia.getStato();
        String iso;
        AETypeProvincia status = AETypeProvincia.provincia;

        listaWrap = wiki.getProvince();
        if (listaWrap != null && listaWrap.size() > 0) {
            for (WrapTreStringhe wrap : listaWrap) {
                nome = wrap.getSeconda();
                sigla = wrap.getPrima();
                iso = sigla;
                regioneTxt = wrap.getTerza().toLowerCase();
                regione = regioneService.findById(regioneTxt);

                if (text.isValid(nome) && stato != null && text.isValid(iso) && text.isValid(sigla)) {
                    provincia = creaIfNotExist(ordine, nome, sigla, regione, stato, iso, status);
                    if (provincia != null) {
                        ordine++;
                    }
                }
                else {
                    logger.warn("Mancano dati essenziali", this.getClass(), "creaProvinceItaliane");
                }
            }
            result = AResult.valido("Province italiane: ", ordine);
        }
        else {
            result = AResult.errato("Non sono riuscito a trovare la table nella pagina di wikipedia 'Province d'Italia'");
        }

        return result;
    }


}