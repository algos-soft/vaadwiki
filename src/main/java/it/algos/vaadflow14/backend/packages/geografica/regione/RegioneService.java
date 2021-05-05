package it.algos.vaadflow14.backend.packages.geografica.regione;

import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.geografica.stato.*;
import it.algos.vaadflow14.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 25-dic-2020
 * Time: 19:01
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
@Qualifier("regioneService")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovraScrivibile = false)
public class RegioneService extends AService {


    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin nel costruttore <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    StatoService statoService;

    /**
     * Costruttore @Autowired. <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore <br>
     * Se ci sono DUE o più costruttori, va in errore <br>
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public RegioneService(final StatoService statoService) {
        super(Regione.class);
        this.statoService = statoService;
    }

    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public RegioneService() {
        super(Regione.class);
    }


    /**
     * Crea e registra una entity solo se non esisteva <br>
     *
     * @param divisione (obbligatorio, unico)
     * @param stato     (obbligatorio)
     * @param iso       di riferimento (obbligatorio, unico)
     * @param sigla     (consuetudinaria, obbligatoria)
     * @param status    (obbligatorio)
     *
     * @return true se la nuova entity è stata creata e salvata
     */
    public Regione creaIfNotExist(final String divisione,final Stato stato,final String iso,final String sigla,final AEStatus status) {
        return (Regione) checkAndSave(newEntity(divisione, stato, iso, sigla, status));
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
    public Regione newEntity() {
        return newEntity(VUOTA, (Stato) null, VUOTA, VUOTA, (AEStatus) null);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param divisione (obbligatorio, unico)
     * @param stato     (obbligatorio)
     * @param iso       di riferimento (obbligatorio, unico)
     * @param sigla     (consuetudinaria, obbligatoria)
     * @param status    (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Regione newEntity(final String divisione,final Stato stato,final String iso,final String sigla,final AEStatus status) {
        Regione newEntityBean = Regione.builderRegione()
                .ordine(this.getNewOrdine())
                .divisione(text.isValid(divisione) ? divisione : null)
                .stato(stato)
                .iso(text.isValid(iso) ? iso : null)
                .sigla(text.isValid(sigla) ? sigla : null)
                .status(status)
                .build();

        return (Regione) fixKey(newEntityBean);
    }

    /**
     * Retrieves all entities.
     *
     * @return the entity with the given id or {@literal null} if none found
     */
    public List<Regione> findAllByStato(final Stato stato) {
        return findAllByStato(stato.id);
    }


    /**
     * Retrieves all entities.
     *
     * @return the entity with the given id or {@literal null} if none found
     */
    public List<Regione> findAllByStato(final String statoKeyID) {
        List<Regione> items;
        Query query = new Query();

        query.addCriteria(Criteria.where("stato.id").is(statoKeyID));
        query.with(Sort.by(Sort.Direction.ASC, "ordine"));
        items = mongo.findAll(entityClazz, query);

        return items;
    }

    /**
     * Retrieves an entity by its key.
     *
     * @param key must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     */
    public Regione findByIsoItalian(final String key) {
        return (Regione) mongo.findOneUnique(Regione.class, "iso", "IT-" + key);
    }


    /**
     * Retrieves all entities.
     *
     * @return the entity with the given id or {@literal null} if none found
     */
    public List<Regione> findAllItalian() {
        return findAllByStato("italia");
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
    public Regione findById(final String keyID) {
        return (Regione) super.findById(keyID);
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
    public Regione findByKey(final String keyValue) {
        return (Regione) super.findByKey(keyValue);
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
//    @Override
    public AIResult resetEmptyOnly2() {//@todo rimettere
        AIResult result = super.resetEmptyOnly();
        AIResult resultCollectionPropedeutica;

        if (result.isErrato()) {
            return result;
        }
        resultCollectionPropedeutica = checkStato();
        if (resultCollectionPropedeutica.isValido()) {
            logger.log(AETypeLog.checkData, resultCollectionPropedeutica.getMessage());
        }
        else {
            return resultCollectionPropedeutica;
        }

        return creaRegioniAllStati();
    }

    private AIResult checkStato() {
        String collection = "stato";

        if (statoService == null) {
            statoService = appContext.getBean(StatoService.class);
        }

        if (mongo.isValid(collection)) {
            return AResult.valido("La collezione " + collection + " esiste già e non è stata modificata");
        }
        else {
            if (statoService == null) {
                return AResult.errato("Manca la classe StatoService");
            }
            else {
                return statoService.resetEmptyOnly();
            }
        }
    }


    /**
     * Recupera le suddivisioni di secondo livello per tutti gli stati <br>
     */
    public AIResult creaRegioniAllStati() {
        AIResult result = AResult.errato();
        int numRec = 0;

        List<Stato> listaStati = statoService.findAllStato();
        List<WrapDueStringhe> listaWrap = null;

        if (array.isAllValid(listaStati)) {
            for (Stato stato : listaStati) {
                result = creaRegioniDiUnoStato(stato);
                if (result.isErrato()) {
                    logger.log(AETypeLog.checkData, "Non sono riuscito a creare le regioni di " + stato.stato);
                }
                else {
                    numRec = numRec + result.getValore();
                }
            }
            return AResult.valido("Sono state create " + numRec + " regioni di " + listaStati.size() + " stati " + AETypeReset.wikipedia.get());
        }

        return AResult.errato("Manca la collezione stati");
    }


    /**
     * Recupera le suddivisioni di secondo livello per il singolo stato <br>
     * Le legge (se riesce) dalla pagina wiki 'ISO_3166-2:xx' col codice iso-alfa2 di ogni stato <br>
     * Cancella eventualmente le regioni esistenti per ricrearle correttamente <br>
     */
    public AIResult creaRegioniDiUnoStato(final Stato stato) {
        AIResult result = AResult.errato();
        Regione regione;
        int numRec = 0;
        String tagWiki = "ISO 3166-2:";
        String alfaDue;
        String wikiPagina = VUOTA;
        String nome;
        String sigla;
        String iso;
        String status = VUOTA;
        AEStatus aeStatus = AEStatus.regione;
        List<WrapDueStringhe> listaWrap = null;
        WrapDueStringhe wrapTitoli;
        List<Regione> regioniDaResettare = findAllByStato(stato);
        if (regioniDaResettare != null && regioniDaResettare.size() > 0) {
            mongo.delete(regioniDaResettare, Regione.class);
        }

        alfaDue = stato.alfadue;
        if (text.isValid(alfaDue)) {
            wikiPagina = tagWiki + alfaDue;

            try {
                listaWrap = wiki.getRegioni(wikiPagina);
            } catch (Exception unErrore) {
                return AResult.errato("Non sono riuscito a leggere la pagina di wikipedia");
            }
        }

        if (listaWrap != null) {
            wrapTitoli = listaWrap.get(0);
            status = wrapTitoli != null ? wrapTitoli.getSeconda() : VUOTA;
            aeStatus = getStatus(status);
            for (WrapDueStringhe wrap : listaWrap.subList(1, listaWrap.size())) {
                nome = fixNome(wrap.getSeconda());
                iso = wrap.getPrima();
                sigla = text.levaTestoPrimaDi(iso, TRATTINO);
                aeStatus = stato.id.equals("italia") ? fixStatusItalia(nome) : aeStatus;
                if (text.isValid(nome) && stato != null && text.isValid(iso) && text.isValid(sigla)) {
                    regione = creaIfNotExist(nome, stato, iso, sigla, aeStatus);
                    if (regione != null) {
                        numRec++;
                    }
                }
                else {
                    logger.warn("Mancano dati essenziali", this.getClass(), "creaRegioniDiUnoStato: " + stato.stato);
                }
            }
            result = AResult.valido("Regioni di " + stato.stato, numRec);
        }
        else {
            result = AResult.errato("Non sono riuscito a trovare la table nella pagina di wikipedia " + wikiPagina);
        }

        return result;
    }

    public AEStatus getStatus(final String status) {
        return AEStatus.get(status);
    }

    public String fixNome(final String nomeIn) {
        String nomeOut = nomeIn;

        if (nomeOut.contains(PIPE)) {
            nomeOut = text.levaTestoPrimaDi(nomeOut, PIPE);
        }

        return nomeOut;
    }


    public AEStatus fixStatusItalia(final String nome) {
        Map<String, List<String>> mappa = resourceService.leggeMappaConfig("regioni");
        String status = mappa.get(nome).get(4);
        return AEStatus.get(status);
    }

}