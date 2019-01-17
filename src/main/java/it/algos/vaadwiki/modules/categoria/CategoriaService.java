package it.algos.vaadwiki.modules.categoria;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwiki.application.WikiCost;
import it.algos.vaadwiki.modules.attnazprofcat.AttNazProfCatService;
import it.algos.wiki.WrapCat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.MONGO_PAGE_LIMIT;
import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 5-ott-2018 12.05.13 <br>
 * <br>
 * Estende la classe astratta AService. Layer di collegamento per la Repository. <br>
 * <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Service (ridondante) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_CAT)
@Slf4j
@AIScript(sovrascrivibile = false)
public class CategoriaService extends AttNazProfCatService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    //        private final static String NOME_CATEGORIA = "Nati nel 1812"; //circa 269
//    private final static String NOME_CATEGORIA = "Nati nel 1946"; //circa 2.550
    private final static String NOME_CATEGORIA = "BioBot"; //circa 356.000


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public CategoriaRepository repository;

    @Autowired
    protected ADateService date;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public CategoriaService(@Qualifier(TAG_CAT) MongoRepository repository) {
        super(repository);
        super.entityClass = Categoria.class;
        this.repository = (CategoriaRepository) repository;
        super.codeLastDownload = LAST_DOWNLOAD_CATEGORIA;
        super.durataLastDownload = DURATA_DOWNLOAD_CATEGORIA;
    }// end of Spring constructor


    /**
     * Crea una entity e la registra <br>
     *
     * @param pageid della pagina wiki (obbligatorio, unico)
     * @param title  della pagina wiki (obbligatorio, unico)
     *
     * @return la entity appena creata
     */
    public Categoria crea(long pageid, String title) {
        return (Categoria) save(newEntity(pageid, title));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Categoria newEntity() {
        return newEntity(0, "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param pageid della pagina wiki (obbligatorio, unico)
     * @param title  della pagina wiki (obbligatorio, unico)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Categoria newEntity(long pageid, String title) {
        Categoria entity = null;

        entity = findByKeyUnica(pageid);
        if (entity != null) {
            return findByKeyUnica(pageid);
        }// end of if cycle

        entity = Categoria.builderCategoria()
                .pageid(pageid)
                .title(title.equals("") ? null : title)
                .build();

        return (Categoria) creaIdKeySpecifica(entity);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param wrap wrapper con pageid e title (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Categoria newEntity(WrapCat wrap) {
        Categoria entity = null;

        entity = findByKeyUnica(wrap.getPageid());
        if (entity != null) {
            return findByKeyUnica(wrap.getPageid());
        }// end of if cycle

        entity = Categoria.builderCategoria()
                .pageid(wrap.getPageid())
                .title(wrap.getTitle())
                .build();

        return (Categoria) creaIdKeySpecifica(entity);
    }// end of method


    /**
     * Property unica (se esiste).
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Categoria) entityBean).getPageid() + "";
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param pageid della pagina wiki (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Categoria findByKeyUnica(long pageid) {
        return repository.findByPageid(pageid);
    }// end of method

    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param wikititle della pagina wiki (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Categoria findByTitle(String wikititle) {
        return repository.findByTitle(wikititle);
    }// end of method


    /**
     * Returns all entities of the type <br>
     * <p>
     * Ordinate secondo l'ordinamento previsto
     *
     * @param sort ordinamento previsto
     *
     * @return all ordered entities
     */
    protected ArrayList<? extends AEntity> findAll(Sort sort) {
        ArrayList<? extends AEntity> lista = null;

        try { // prova ad eseguire il codice
            if (sort != null) {
                lista = new ArrayList(repository.findAll(sort));
            } else {
                lista = new ArrayList(repository.findTop100ByOrderByTitleAsc());
            }// end of if/else cycle
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return lista;
    }// end of method


    /**
     * Returns all entities of the type <br>
     * <p>
     *
     * @return all ordered entities
     */
    public ArrayList<Long> findPageids() {
        ArrayList<Long> listaLong = new ArrayList<>();
        List<? extends AEntity> listaPagine = null;
        long inizio = System.currentTimeMillis();
        int size = pref.getInt(MONGO_PAGE_LIMIT);
        Sort sort = new Sort(Sort.Direction.ASC, "pageid");

        for (int k = 0; k < array.numCicli(count(), size); k++) {
            try { // prova ad eseguire il codice
                listaPagine = mongo.mongoOp.find(new Query().with(PageRequest.of(k, size, sort)), Categoria.class);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch

            for (AEntity entity : listaPagine) {
                listaLong.add(((Categoria) entity).pageid);
            }// end of for cycle

            if (pref.isBool(FlowCost.USA_DEBUG)) {
                log.info("Debug. Recuperate " + text.format(listaLong.size()) + " pagine da categoriaService.findPageids()");
            }// end of if cycle
        }// end of for cycle

        if (pref.isBool(FlowCost.USA_DEBUG)) {
            logger.debug("Recuperate " + text.format(listaLong.size()) + " pagine da categoriaService.findPageids() in " + date.deltaText(inizio));
        }// end of if cycle
        return listaLong;
    }// end of method


    /**
     * Download completo da Wiki di tutte le pagine della categoria preselezionata<br>
     * Cancella tutte le precedenti entities <br>
     * Recupera il solo pageid oppure pageid e title <br>
     * Registra su Mongo DB tutte le occorrenze <br>
     * Log del tempo impiegato con e senza registrazione su Mongo <br>
     * singola pagina - 48 sec per 266 pagine - troppo, circa 17 ore
     * categoria, 10.000 voci in 2 sec, circa 1 minuto per tutte le biografie
     * categoria, 350.000 voci in 2 minuti per tutte le biografie
     * mongo registrazione di 350.000 Categoria in 10 secondi (con insert)
     * gruppi di 500 pages - 6 (lettura) sec per 500 pagine = totale 1,2H
     * gruppi di 500 pages - 7 (scrittura) sec per 500 pagine = totale 1,3H
     * gruppi di 500 timestamp - 1 sec per 500 pagine = totale 11 minuti
     */
    public void download() {
        long inizio;
        ArrayList<Categoria> listaCat;
        String nomeCategoria = pref.getStr(WikiCost.CAT_BIO);
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            log.info("CAT - Inizio a caricare le pagine di wiki.categoria. Circa due minuti");
        }// end of if cycle

        inizio = System.currentTimeMillis();
        listaCat = api.leggeCatCat(nomeCategoria);
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            logger.debug("CAT - Lette le pagine di wiki.categoria." + nomeCategoria + " (" + text.format(listaCat.size()) + " elementi) in " + date.deltaText(inizio));
        }// end of if cycle

        inizio = System.currentTimeMillis();
        insert(listaCat);
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            logger.debug("Insert in mongoDB.Categoria (pageid e title) (" + text.format(listaCat.size()) + " elementi) in " + date.deltaText(inizio));
        }// end of if cycle

        setLastDownload(inizio);
    }// end of method


    /**
     * Returns pageid list of the requested page.
     * <p>
     * Senza filtri
     * Ordinati per sort
     *
     * @param offset numero di pagine da saltare, parte da zero
     * @param size   numero di elementi per ogni pagina
     * @param sort   ordinamento degli elementi
     *
     * @return all entities
     */
    public ArrayList<Long> findAllPageid(int offset, int size, Sort sort) {
        ArrayList<Long> lista = null;
        ArrayList<? extends AEntity> listaEntities = findAll(offset, size, sort);

        if (array.isValid(listaEntities)) {
            lista = new ArrayList<>();
            for (AEntity entity : listaEntities) {
                lista.add(((Categoria) entity).pageid);
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Inserts multiple documents into a collection.
     *
     * @param listaCat lista di elementi di categoria
     */
    private void insert(ArrayList<Categoria> listaCat) {
        ArrayList<Categoria> listaBlocco;
        int pageLimit = pref.getInt(MONGO_PAGE_LIMIT);
        int numCicliInsert = array.numCicli(listaCat.size(), pageLimit);

        mongo.drop(Categoria.class);
        for (int k = 0; k < numCicliInsert; k++) {
            listaBlocco = array.estraeSublista(listaCat, pageLimit, k + 1);
            mongo.insert(listaBlocco, Categoria.class);
            if (pref.isBool(FlowCost.USA_DEBUG)) {
                log.info("Inserite " + text.format(count()) + " entities nella collezione Categoria");
            }// end of if cycle
        }// end of for cycle

    }// end of method


    /**
     * Costruisce una lista di nomi delle properties del Search nell'ordine:
     * 1) Sovrascrive la lista nella sottoclasse specifica di xxxService
     *
     * @param context legato alla sessione
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getSearchPropertyNamesList(AContext context) {
        return Arrays.asList("title");
    }// end of method

}// end of class