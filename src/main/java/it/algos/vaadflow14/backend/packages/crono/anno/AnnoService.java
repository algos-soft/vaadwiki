package it.algos.vaadflow14.backend.packages.crono.anno;

import com.google.gson.*;
import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.crono.secolo.*;
import it.algos.vaadflow14.backend.wrapper.*;
import org.bson.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 25-dic-2020
 * Time: 06:54
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
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovraScrivibile = false)
public class AnnoService extends AService {

    /**
     * Costanti usate nell' ordinamento delle categorie
     */
    private static final int ANNO_INIZIALE = 2000;

    private static final int ANTE_CRISTO = 1000;

    private static final int DOPO_CRISTO = 2030;

    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin nel costruttore <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    SecoloService secoloService;

    /**
     * Costruttore @Autowired. <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore <br>
     * Se ci sono DUE o più costruttori, va in errore <br>
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public AnnoService(final SecoloService secoloService) {
        super(Anno.class);
        this.secoloService = secoloService;
    }


    /**
     * Crea e registra una entity solo se non esisteva <br>
     *
     * @param ordine    (obbligatorio, unico)
     * @param anno      (obbligatorio, unico)
     * @param bisestile (obbligatorio)
     * @param secolo    di riferimento (obbligatorio)
     *
     * @return la nuova entityBean appena creata e salvata
     */
    public Anno creaIfNotExist(final int ordine,final String anno,final boolean bisestile,final Secolo secolo) {
        return (Anno) checkAndSave(newEntity(ordine, anno, bisestile, secolo));
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
    public Anno newEntity() {
        return newEntity(0, VUOTA, false, (Secolo) null);
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param ordine    (obbligatorio, unico)
     * @param anno      (obbligatorio, unico)
     * @param bisestile (obbligatorio)
     * @param secolo    di riferimento (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Anno newEntity(final int ordine,final String anno,final boolean bisestile,final Secolo secolo) {
        Anno newEntityBean = Anno.builderAnno()
                .ordine(ordine > 0 ? ordine : getNewOrdine())
                .anno(text.isValid(anno) ? anno : null)
                .bisestile(bisestile)
                .secolo(secolo)
                .build();

        return (Anno) fixKey(newEntityBean);
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
    public Anno findById(final String keyID) {
        return (Anno) super.findById(keyID);
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
    public Anno findByKey(final String keyValue) {
        return (Anno) super.findByKey(keyValue);
    }


    public List<Anno> fetchAnni(final int offset, final int limit) {
        List<Anno> lista = new ArrayList<>();
        Gson gson = new Gson();
        Anno anno;

        List<Document> products = mongo.mongoOp.getCollection("anno").find().skip(offset).limit(limit).into(new ArrayList<>());

        for (Document doc : products) {

            // 1. JSON file to Java object
            anno = gson.fromJson(doc.toJson(), Anno.class);
            lista.add(anno);
        }

        return lista;
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
        int ordine;
        String nome;
        AESecolo secoloEnum;
        Secolo secolo;
        String titoloSecolo;
        boolean bisestile = false;

        if (result.isErrato()) {
            return result;
        }

        resultCollectionPropedeutica = checkSecolo();
        if (resultCollectionPropedeutica.isValido()) {
            logger.log(AETypeLog.checkData, resultCollectionPropedeutica.getMessage());
        }
        else {
            return resultCollectionPropedeutica;
        }

        //--costruisce gli anni prima di cristo dal 1000
        for (int k = ANTE_CRISTO; k > 0; k--) {
            ordine = ANNO_INIZIALE - k;
            nome = k + AESecolo.TAG_AC;
            secoloEnum = AESecolo.getSecoloAC(k);
            titoloSecolo = secoloEnum.getNome();
            titoloSecolo = titoloSecolo.toLowerCase();
            titoloSecolo = text.levaSpazi(titoloSecolo);
            secolo = (Secolo) mongo.findById(Secolo.class, titoloSecolo);
            bisestile = false; //non ci sono anni bisestili prima di Cristo
            if (ordine != ANNO_INIZIALE && secolo != null && text.isValid(nome)) {
                if (creaIfNotExist(ordine, nome, bisestile, secolo) != null) {
                    numRec++;
                }
            }
        }

        //--costruisce gli anni dopo cristo fino al 2030
        for (int k = 1; k <= DOPO_CRISTO; k++) {
            ordine = k + ANNO_INIZIALE;
            nome = k + VUOTA;
            secoloEnum = AESecolo.getSecoloDC(k);
            titoloSecolo = secoloEnum.getNome();
            titoloSecolo = titoloSecolo.toLowerCase();
            titoloSecolo = text.levaSpazi(titoloSecolo);
            secolo = (Secolo) mongo.findById(Secolo.class, titoloSecolo);
            bisestile = date.bisestile(k);
            if (ordine != ANNO_INIZIALE && secolo != null && text.isValid(nome)) {
                if (creaIfNotExist(ordine, nome, bisestile, secolo) != null) {
                    numRec++;
                }
            }
        }

        return super.fixPostReset(AETypeReset.hardCoded, numRec);
    }


    private AIResult checkSecolo() {
        String collection = "secolo";

        if (secoloService == null) {
            secoloService = appContext.getBean(SecoloService.class);
        }

        if (mongo.isValid(collection)) {
            return AResult.valido("La collezione " + collection + " esiste già e non è stata modificata");
        }
        else {
            if (secoloService == null) {
                return AResult.errato("Manca la classe SecoloLogic");
            }
            else {
                return secoloService.resetEmptyOnly();
            }
        }
    }

}