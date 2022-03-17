package it.algos.vaadwiki.modules.nome;

import com.mongodb.client.DistinctIterable;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.service.AMongoService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.doppinomi.DoppinomiService;
import it.algos.vaadwiki.modules.wiki.NomeCognomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 29-mag-2019 19.55.00 <br>
 * <br>
 * Business class. Layer di collegamento per la Repository. <br>
 * <br>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * NOT annotated with @VaadinSessionScope (sbagliato, perché SpringBoot va in loop iniziale) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_NOM)
@Slf4j
@AIScript(sovrascrivibile = false)
public class NomeService extends NomeCognomeService {

    //--titolo della pagina di statistiche sul server wiki
    public static final String TITOLO_PAGINA_WIKI = "Progetto:Antroponimi/Nomi";

    //--titolo della pagina di statistiche sul server wiki
    public static final String TITOLO_PAGINA_WIKI_2 = "Progetto:Antroponimi/Liste nomi";

    //--titolo del template incipit nomi
    public static final String TITOLO_TEMPLATE_INCIPIT_NOMI = "Template:Incipit lista nomi";

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public NomeRepository repository;

    @Autowired
    private AMongoService mongo;

    @Autowired
    private DoppinomiService doppinomiService;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public NomeService(@Qualifier(TAG_NOM) MongoRepository repository) {
        super(repository);
        super.entityClass = Nome.class;
        this.repository = (NomeRepository) repository;
        super.codeLastElabora = LAST_ELABORA_NOME;
        super.durataLastElabora = DURATA_ELABORA_NOMI;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param nome di riferimento (obbligatorio ed unico)
     *
     * @return la entity trovata o appena creata
     */
    public Nome findOrCrea(String nome) {
        return findOrCrea(nome, 0, false, false);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param nome   di riferimento (obbligatorio ed unico)
     * @param voci   biografiche con questo nome <br>
     * @param valido se supera la soglia minima per creare una pagina sul server wiki <br>
     *
     * @return la entity trovata o appena creata
     */
    public Nome findOrCrea(String nome, int voci, boolean valido) {
        Nome entity = findByKeyUnica(nome);

        if (entity == null) {
            entity = crea(nome, voci, valido);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param nome   di riferimento (obbligatorio ed unico)
     * @param voci   biografiche con questo nome <br>
     * @param valido se supera la soglia minima per creare una pagina sul server wiki <br>
     * @param doppio se proviene dalla lista di nomi doppi <br>
     *
     * @return la entity trovata o appena creata
     */
    public Nome findOrCrea(String nome, int voci, boolean valido, boolean doppio) {
        Nome entity = findByKeyUnica(nome);

        if (entity == null) {
            entity = crea(nome, voci, valido, doppio);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param nome di riferimento (obbligatorio ed unico)
     *
     * @return la entity appena creata
     */
    public Nome crea(String nome) {
        return crea(nome, 0, false, false);
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param nome   di riferimento (obbligatorio ed unico)
     * @param voci   biografiche con questo nome <br>
     * @param valido se supera la soglia minima per creare una pagina sul server wiki <br>
     *
     * @return la entity appena creata
     */
    public Nome crea(String nome, int voci, boolean valido) {
        return (Nome) save(newEntity(nome, voci, valido));
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param nome   di riferimento (obbligatorio ed unico)
     * @param voci   biografiche con questo nome <br>
     * @param valido se supera la soglia minima per creare una pagina sul server wiki <br>
     * @param doppio se proviene dalla lista di nomi doppi <br>
     *
     * @return la entity appena creata
     */
    public Nome crea(String nome, int voci, boolean valido, boolean doppio) {
        return (Nome) save(newEntity(nome, voci, valido, doppio));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Nome newEntity() {
        return newEntity("", 0, false, false);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param nome   di riferimento (obbligatorio ed unico)
     * @param voci   biografiche con questo nome <br>
     * @param valido se supera la soglia minima per creare una pagina sul server wiki <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Nome newEntity(String nome, int voci, boolean valido) {
        return newEntity(nome, voci, valido, false);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param nome   di riferimento (obbligatorio ed unico)
     * @param voci   biografiche con questo nome <br>
     * @param valido se supera la soglia minima per creare una pagina sul server wiki <br>
     * @param doppio se proviene dalla lista di nomi doppi <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Nome newEntity(String nome, int voci, boolean valido, boolean doppio) {
        Nome entity = null;

        entity = findByKeyUnica(nome);
        if (entity != null) {
            return findByKeyUnica(nome);
        }// end of if cycle

        entity = new Nome();
        entity.nome = text.isValid(nome) ? nome : null;
        entity.voci = voci != 0 ? voci : this.getNewOrdine();
        entity.valido = valido;
        entity.doppio = doppio;

        return (Nome) creaIdKeySpecifica(entity);
    }// end of method


    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public Nome findById(String id) {
        return (Nome) super.findById(id);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param nome di riferimento (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Nome findByKeyUnica(String nome) {
        return repository.findByNome(nome);
    }// end of method


    /**
     * Property unica (se esiste) <br>
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Nome) entityBean).nome;
    }// end of method


    /**
     * Returns all entities of the type <br>
     * <p>
     * Se esiste la property 'ordine', ordinate secondo questa property <br>
     * Altrimenti, se esiste la property 'code', ordinate secondo questa property <br>
     * Altrimenti, se esiste la property 'descrizione', ordinate secondo questa property <br>
     * Altrimenti, ordinate secondo il metodo sovrascritto nella sottoclasse concreta <br>
     * Altrimenti, ordinate in ordine di inserimento nel DB mongo <br>
     *
     * @return all ordered entities
     */
    @Override
    public List<Nome> findAll() {
        return (List<Nome>) super.findAll();
    }// end of method


    public List<Nome> findAllDimensioni() {
        return (List<Nome>) super.findAll(new Sort(Sort.Direction.DESC, "voci"));
    }// end of method


    public List<Nome> findAllAlfabetico() {
        return (List<Nome>) super.findAll(new Sort(Sort.Direction.ASC, "nome"));
    }// end of method


    public List<Nome> findAllNomiDoppi() {
        return (List<Nome>) super.findAll(new Sort(Sort.Direction.DESC, "doppio"));
    }// end of method


    public int countValidi() {
        return repository.countAllByValido(true);
    }// end of method


    public List<Nome> findValidi() {
        return repository.findAllByValidoOrderByNomeAsc(true);
    }// end of method


    /**
     * Creazione di tutti i nomi 'singoli' e 'doppi' esistenti come property nelle voci biografiche <br>
     * <p>
     * Ricrea al volo (per sicurezza di aggiornamento) tutta la collezione mongoDb dei 'doppinomi' <br>
     * Cancella tutte le entities della collezione <br>
     * Estrae tutti i nomi 'distinti' (differenti) dalla collezione Bio <br>
     * Elimina tutti i nomi 'doppi' composti da più nomi e che contengono uno 'spazio vuoto' <br>
     * Registra i nomi che hanno più di n ricorrenze nelle voci biografiche <br>
     * Recupera una lista di 'nomi doppi' speciali ed ammessi <br>
     * Aggiunge (se già non ci sono) i nomi doppi speciali <br>
     */
    public void elabora() {
        long inizio = System.currentTimeMillis();
        List<String> listaDoppi = null;
        int tot = 0;
        int cont = 0;
        Nome nome = null;
        String tagSpazio = " ";
        logger.info("Creazione completa nomi delle biografie. Circa 2 minuti.");

        //--Ricrea al volo (per sicurezza di aggiornamento) tutta la collezione mongoDb dei doppinomi
        doppinomiService.download();
        listaDoppi = doppinomiService.findAllCode();

        //--Cancella tutte le entities della collezione
        deleteAll();

        DistinctIterable<String> listaNomiDistinti = mongo.mongoOp.getCollection("bio").distinct("nome", String.class);
        for (String nomeTxt : listaNomiDistinti) {
            tot++;

            //--Nome 'semplici'. Quelli 'doppi' vengono inseriti dopo da apposita lista
            if (text.isValid(nomeTxt) && !nomeTxt.contains(tagSpazio)) {
                if (saveNome(nomeTxt) != null) {
                    cont++;
                }// end of if cycle
            }// end of if cycle

        }// end of for cycle

        //--Nome 'doppi' inseriti da apposita lista
        if (array.isValid(listaDoppi)) {
            for (String nomeTxt : listaDoppi) {
                nome = saveNome(nomeTxt);
                if (nome != null) {
                    nome.doppio = true;
                    save(nome);
                    cont++;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        super.setLastElabora(EATempo.minuti, inizio);
        logger.info("Creazione di " + text.format(cont) + " nomi su un totale di " + text.format(tot) + " nomi distinti. Tempo impiegato: " + date.deltaText(inizio));
    }// end of method


    /**
     * Registra il numero di voci biografiche che hanno il nome indicato <br>
     * Sono validi i nome 'semplici' oppure quelli dell'apposita collection 'doppinomi' <br>
     */
    public Nome saveNome(String nomeTxt) {
        Nome nome = null;
        //--Soglia minima per creare una entity nella collezione Nomi sul mongoDB
        int sogliaMongo = pref.getInt(SOGLIA_NOMI_MONGO, 40);
        //--Soglia minima per creare una pagina sul server wiki
        int sogliaWiki = pref.getInt(SOGLIA_NOMI_PAGINA_WIKI, 50);
        boolean valido;
        long numVoci = 0;
        Query query = new Query();

        query.addCriteria(Criteria.where("nome").is(nomeTxt));
        numVoci = mongo.mongoOp.count(query, Bio.class);
        valido = numVoci > sogliaWiki;

        if (numVoci >= sogliaMongo && text.isValid(nomeTxt)) {
            nome = findOrCrea(nomeTxt, (int) numVoci, valido);
        }// end of if cycle

        return nome;
    }// end of method


}// end of class