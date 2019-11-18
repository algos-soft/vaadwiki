package it.algos.vaadwiki.modules.bio;

import com.mongodb.client.result.DeleteResult;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.download.ElaboraService;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.attnazprofcat.AttNazProfCatService;
import it.algos.vaadwiki.modules.cognome.Cognome;
import it.algos.vaadwiki.modules.cognome.CognomeService;
import it.algos.vaadwiki.modules.nome.Nome;
import it.algos.vaadwiki.modules.nome.NomeService;
import it.algos.wiki.Api;
import it.algos.wiki.Page;
import it.algos.wiki.WrapTime;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static it.algos.vaadflow.application.FlowCost.MONGO_PAGE_LIMIT;
import static it.algos.vaadwiki.application.WikiCost.TAG_BIO;


/**
 * Project vaadbio2 <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Date: 11-ago-2018 17.19.29 <br>
 * <br>
 * Estende la classe astratta AService. Layer di collegamento per la Repository. <br>
 * <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Service (ridondante) <br>
 * Annotated with @Scope (obbligatorio = 'singleton') <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_BIO)
@Slf4j
@AIScript(sovrascrivibile = false)
public class BioService extends AttNazProfCatService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private Api api;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private ElaboraService elabora;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private GiornoService giornoService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private AnnoService annoService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private NomeService nomeService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private CognomeService cognomeService;

    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (come previsto dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private BioRepository repository;


    /**
     * Costruttore <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public BioService(@Qualifier(TAG_BIO) MongoRepository repository) {
        super(repository);
        super.entityClass = Bio.class;
        this.repository = (BioRepository) repository;
    }// end of Spring constructor


    /**
     * Crea una entity e la registra <br>
     *
     * @param pageid        della pagina wiki (obbligatorio, unico)
     * @param wikiTitle     della pagina wiki (obbligatorio, unico)
     * @param tmplBioServer template effettivamente presente sul server (obligatorio, unico)
     *
     * @return la entity appena creata
     */
    public Bio crea(long pageid, String wikiTitle, String tmplBioServer) {
        return (Bio) save(newEntity(pageid, wikiTitle, tmplBioServer));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Bio newEntity() {
        return newEntity(0, "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Properties <br>
     *
     * @param pageid    della pagina wiki (obbligatorio, unico)
     * @param wikiTitle della pagina wiki (obbligatorio, unico)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Bio newEntity(long pageid, String wikiTitle) {
        return newEntity(pageid, wikiTitle, "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Properties <br>
     *
     * @param pageid        della pagina wiki (obbligatorio, unico)
     * @param wikiTitle     della pagina wiki (obbligatorio, unico)
     * @param tmplBioServer template effettivamente presente sul server (obligatorio, unico)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Bio newEntity(long pageid, String wikiTitle, String tmplBioServer) {
        return newEntity(pageid, wikiTitle, tmplBioServer, (LocalDateTime) null, (LocalDateTime) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param pageid           della pagina wiki (obbligatorio, unico)
     * @param wikiTitle        della pagina wiki (obbligatorio, unico)
     * @param tmplBioServer    template effettivamente presente sul server (obligatorio, unico)
     * @param lastWikiModifica della pagina effettuata sul servere wiki
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Bio newEntity(long pageid, String wikiTitle, String tmplBioServer, LocalDateTime lastWikiModifica) {
        return newEntity(pageid, wikiTitle, tmplBioServer, lastWikiModifica, (LocalDateTime) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param pageid           della pagina wiki (obbligatorio, unico)
     * @param wikiTitle        della pagina wiki (obbligatorio, unico)
     * @param tmplBioServer    template effettivamente presente sul server (obligatorio, unico)
     * @param lastWikiModifica della pagina effettuata sul servere wiki
     * @param lastGacLettura   della voce effettuata dal programma
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Bio newEntity(long pageid, String wikiTitle, String tmplBioServer, LocalDateTime lastWikiModifica, LocalDateTime lastGacLettura) {
        Bio entity = null;

        entity = Bio.builderBio()
                .pageid(pageid)
                .wikiTitle(wikiTitle.equals("") ? null : wikiTitle)
                .tmplBioServer(tmplBioServer.equals("") ? null : tmplBioServer)
                .lastModifica(lastWikiModifica != null ? lastWikiModifica : LocalDateTime.of(2000, 1, 1, 0, 0))
                .lastLettura(lastGacLettura != null ? lastGacLettura : LocalDateTime.now())
                .build();

        if (text.isValid(wikiTitle)) {
            entity.id = wikiTitle;
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Operazioni eseguite PRIMA del save <br>
     * Regolazioni automatiche di property <br>
     * Controllo della validità delle properties obbligatorie <br>
     * Può essere sovrascritto - Invocare PRIMA il metodo della superclasse
     *
     * @param entityBean da regolare prima del save
     * @param operation  del dialogo (NEW, Edit)
     *
     * @return the modified entity
     */
    @Override
    public AEntity beforeSave(AEntity entityBean, EAOperation operation) {
        return super.beforeSave(entityBean, operation);
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
                //@todo troppo lento - devo disabilitarlo
//                lista = new ArrayList(repository.findTop50ByOrderByWikiTitleAsc());
//                lista = new ArrayList(repository.findAllByOrderByWikiTitleAsc());
            }// end of if/else cycle
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return lista;
    }// end of method


    /**
     * Seleziona tutte le biografie delle persone nate in un certo giorno <br>
     * La lista viene ordinata per anno di nascita <br>
     * La lista viene ulteriormente ordinata per cognome o per wikiTitle (a seconda del flag di programma) <br>
     *
     * @param giornoNascita per la selezione
     *
     * @return ordered entities
     */
    public List<Bio> findAllByGiornoNascita(Giorno giornoNascita) {
        return orderByGiornoAnnoAndCognome(repository.findAllByGiornoNascita(giornoNascita), EADidascalia.giornoNato);
    }// end of method


    /**
     * Seleziona tutte le biografie delle persone nate in un certo giorno <br>
     * La lista viene ordinata per anno di nascita <br>
     * La lista viene ulteriormente ordinata per cognome o per wikiTitle (a seconda del flag di programma) <br>
     *
     * @param giornoTxt per la selezione
     *
     * @return ordered entities
     */
    public List<Bio> findAllByGiornoNascita(String giornoTxt) {
        return giornoService != null ? findAllByGiornoNascita(giornoService.findByKeyUnica(giornoTxt)) : null;
    }// end of method


    /**
     * Seleziona tutte le biografie delle persone morte in un certo giorno <br>
     * La lista viene ordinata per anno di morte <br>
     * La lista viene ulteriormente ordinata per cognome o per wikiTitle (a seconda del flag di programma) <br>
     *
     * @param giornoMorte per la selezione
     *
     * @return ordered entities
     */
    public List<Bio> findAllByGiornoMorte(Giorno giornoMorte) {
        return orderByGiornoAnnoAndCognome(repository.findAllByGiornoMorte(giornoMorte), EADidascalia.giornoMorto);
    }// end of method


    /**
     * Seleziona tutte le biografie delle persone morte in un certo giorno <br>
     * La lista viene ordinata per anno di morte <br>
     * La lista viene ulteriormente ordinata per cognome o per wikiTitle (a seconda del flag di programma) <br>
     *
     * @param giornoTxt per la selezione
     *
     * @return ordered entities
     */
    public List<Bio> findAllByGiornoMorte(String giornoTxt) {
        return giornoService != null ? findAllByGiornoMorte(giornoService.findByKeyUnica(giornoTxt)) : null;
    }// end of method


    /**
     * Seleziona tutte le biografie delle persone nate in un certo anno <br>
     * La lista viene ordinata per giorno di nascita <br>
     * La lista viene ulteriormente ordinata per cognome o per wikiTitle (a seconda del flag di programma) <br>
     *
     * @param annoNascita per la selezione
     *
     * @return ordered entities
     */
    public List<Bio> findAllByAnnoNascita(Anno annoNascita) {
        return orderByGiornoAnnoAndCognome(repository.findAllByAnnoNascita(annoNascita), EADidascalia.annoNato);
    }// end of method


    /**
     * Seleziona tutte le biografie delle persone nate in un certo anno <br>
     * La lista viene ordinata per giorno di nascita <br>
     * La lista viene ulteriormente ordinata per cognome o per wikiTitle (a seconda del flag di programma) <br>
     *
     * @param annoTxt per la selezione
     *
     * @return ordered entities
     */
    public List<Bio> findAllByAnnoNascita(String annoTxt) {
        return annoService != null ? findAllByAnnoNascita(annoService.findByKeyUnica(annoTxt)) : null;
    }// end of method


    /**
     * Seleziona tutte le biografie delle persone morte in un certo anno <br>
     * La lista viene ordinata per giorno di morte <br>
     * La lista viene ulteriormente ordinata per cognome o per wikiTitle (a seconda del flag di programma) <br>
     *
     * @param annoMorte per la selezione
     *
     * @return ordered entities
     */
    public List<Bio> findAllByAnnoMorte(Anno annoMorte) {
        return orderByGiornoAnnoAndCognome(repository.findAllByAnnoMorte(annoMorte), EADidascalia.annoMorto);
    }// end of method


    /**
     * Seleziona tutte le biografie delle persone morte in un certo anno <br>
     * La lista viene ordinata per giorno di morte <br>
     * La lista viene ulteriormente ordinata per cognome o per wikiTitle (a seconda del flag di programma) <br>
     *
     * @param annoTxt per la selezione
     *
     * @return ordered entities
     */
    public List<Bio> findAllByAnnoMorte(String annoTxt) {
        return annoService != null ? findAllByAnnoMorte(annoService.findByKeyUnica(annoTxt)) : null;
    }// end of method


    /**
     * Seleziona tutte le biografie delle persone di un certo nome <br>
     * La lista viene ordinata per attività <br>
     * La lista viene ulteriormente ordinata per cognome o per wikiTitle (a seconda del flag di programma) <br>
     *
     * @param nome per la selezione
     *
     * @return ordered entities
     */
    public List<Bio> findAllByNome(Nome nome) {
        return orderByAttivitaAndNomeCognome(repository.findAllByNome(nome.nome), EADidascalia.listaNomi);
    }// end of method


    /**
     * Seleziona tutte le biografie delle persone di un certo nome <br>
     * La lista viene ordinata per attività <br>
     * La lista viene ulteriormente ordinata per cognome o per wikiTitle (a seconda del flag di programma) <br>
     *
     * @param nomeTxt per la selezione
     *
     * @return ordered entities
     */
    public List<Bio> findAllByNome(String nomeTxt) {
        return nomeService != null ? findAllByNome(nomeService.findByKeyUnica(nomeTxt)) : null;
    }// end of method


    /**
     * Seleziona tutte le biografie delle persone di un certo cognome <br>
     * La lista viene ordinata per attività <br>
     * La lista viene ulteriormente ordinata per cognome o per wikiTitle (a seconda del flag di programma) <br>
     *
     * @param cognome per la selezione
     *
     * @return ordered entities
     */
    public List<Bio> findAllByCognome(Cognome cognome) {
        return orderByAttivitaAndNomeCognome(repository.findAllByCognome(cognome.cognome), EADidascalia.listaCognomi);
    }// end of method


    /**
     * Seleziona tutte le biografie delle persone di un certo cognome <br>
     * La lista viene ordinata per attività <br>
     * La lista viene ulteriormente ordinata per cognome o per wikiTitle (a seconda del flag di programma) <br>
     *
     * @param cognomeTxt per la selezione
     *
     * @return ordered entities
     */
    public List<Bio> findAllByCognome(String cognomeTxt) {
        return cognomeService != null ? findAllByCognome(cognomeService.findByKeyUnica(cognomeTxt)) : null;
    }// end of method


    /**
     * Ordina la lista <br>
     *
     * @return all ordered entities
     */
    public ArrayList<Bio> orderByGiornoAnnoAndCognome(List<Bio> listaGrezza, EADidascalia type) {
        ArrayList lista = null;
        List<Bio> listaTmp = null;
        LinkedHashMap<Integer, List<Bio>> mappa;
        int ordine;
        List<Bio> listaCognomi;

        if (listaGrezza != null) {
            lista = new ArrayList<>();
            mappa = new LinkedHashMap<>();

            for (Bio bio : listaGrezza) {
                ordine = getOrdine(bio, type);

                if (mappa.containsKey(ordine)) {
                    listaTmp = mappa.get(ordine);
                } else {
                    listaTmp = new ArrayList<>();
                }// end of if/else cycle
                listaTmp.add(bio);

                mappa.put(ordine, listaTmp);
            }// end of for cycle

            Set<Integer> listaChiavi = mappa.keySet();
            Integer[] matrice = listaChiavi.toArray(new Integer[listaChiavi.size()]);
            List<Integer> list = Arrays.asList(matrice);
            Collections.sort(list);

            for (Integer key : list) {
                listaCognomi = mappa.get(key);
                listaCognomi = orderByNomeCognome(listaCognomi, EADidascalia.listaNomi);
                for (Bio bio : listaCognomi) {
                    lista.add(bio);
                }// end of for cycle
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Ordina la lista <br>
     *
     * @return all ordered entities
     */
    public ArrayList<Bio> orderByAttivitaAndNomeCognome(List<Bio> listaGrezza, EADidascalia type) {
        ArrayList lista = null;
        List<Bio> listaTmp = null;
        LinkedHashMap<String, List<Bio>> mappa;
        String attivita;
        List<Bio> listaCognomi;

        if (listaGrezza != null) {
            lista = new ArrayList<>();
            mappa = new LinkedHashMap<>();

            for (Bio bio : listaGrezza) {
                attivita = bio.getAttivita() != null ? bio.getAttivita().singolare : "";

                if (mappa.containsKey(attivita)) {
                    listaTmp = mappa.get(attivita);
                } else {
                    listaTmp = new ArrayList<>();
                }// end of if/else cycle
                listaTmp.add(bio);

                mappa.put(attivita, listaTmp);
            }// end of for cycle

            Set<String> listaChiavi = mappa.keySet();
            String[] matrice = listaChiavi.toArray(new String[listaChiavi.size()]);
            List<String> list = Arrays.asList(matrice);
            Collections.sort(list);

            for (String key : list) {
                listaCognomi = mappa.get(key);
                listaCognomi = orderByNomeCognome(listaCognomi, type);
                for (Bio bio : listaCognomi) {
                    lista.add(bio);
                }// end of for cycle
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Conta tutte le biografie delle persone con una certa attività (primaria) <br>
     *
     * @param attivita per il conteggio
     *
     * @return titale biografie che rispondono al requisito
     */
    public int countByAttivitaPrincipale(Attivita attivita) {
        return repository.countAllByAttivita(attivita);
    }// end of method


    /**
     * Conta tutte le biografie delle persone con una certa attività (secondaria) <br>
     *
     * @param attivita per il conteggio
     *
     * @return titale biografie che rispondono al requisito
     */
    public int countByAttivitaDue(Attivita attivita) {
        return repository.countAllByAttivita2(attivita);
    }// end of method


    /**
     * Conta tutte le biografie delle persone con una certa attività (terziaria) <br>
     *
     * @param attivita per il conteggio
     *
     * @return titale biografie che rispondono al requisito
     */
    public int countByAttivitaTre(Attivita attivita) {
        return repository.countAllByAttivita3(attivita);
    }// end of method


    /**
     * Conta tutte le biografie delle persone con una certa attività (qualsiasi) <br>
     *
     * @param attivita per il conteggio
     *
     * @return titale biografie che rispondono al requisito
     */
    public int countByAttivitaTotali(Attivita attivita) {
        return repository.countByAttivitaOrAttivita2OrAttivita3(attivita, attivita, attivita);
    }// end of method


    public int getOrdine(Bio bio, EADidascalia type) {
        int ordine = 0;

        if (bio != null && type != null) {
            switch (type) {
                case giornoNato:
                    ordine = bio.annoNascita != null ? bio.annoNascita.ordine : 0;
                    break;
                case giornoMorto:
                    ordine = bio.annoMorte != null ? bio.annoMorte.ordine : 0;
                    break;
                case annoNato:
                    ordine = bio.giornoNascita != null ? bio.giornoNascita.ordine : 0;
                    break;
                case annoMorto:
                    ordine = bio.giornoMorte != null ? bio.giornoMorte.ordine : 0;
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

        return ordine;
    }// end of method


    public List<Bio> orderByNomeCognome(List<Bio> listaIn, EADidascalia type) {
        List<Bio> listaOut = listaIn;
        LinkedHashMap<String, List<Bio>> mappa;
        String nomeCognome = "";
        List<Bio> listaTmp = null;

        if (array.isValid(listaIn)) {
            listaOut = new ArrayList<>();
            mappa = new LinkedHashMap<>();

            for (Bio bio : listaIn) {
                nomeCognome = getNomeCognome(bio, type);

                if (mappa.containsKey(nomeCognome)) {
                    listaTmp = mappa.get(nomeCognome);
                } else {
                    listaTmp = new ArrayList<>();
                }// end of if/else cycle
                listaTmp.add(bio);

                mappa.put(nomeCognome, listaTmp);
            }// end of for cycle

            Set<String> listaChiavi = mappa.keySet();
            String[] matrice = listaChiavi.toArray(new String[listaChiavi.size()]);
            List<String> list = Arrays.asList(matrice);
            Collections.sort(list);

            for (String key : list) {
                for (Bio bio : mappa.get(key)) {
                    listaOut.add(bio);
                }// end of for cycle
            }// end of for cycle

        }// end of if cycle

        return listaOut;
    }// end of method


    public String getNomeCognome(Bio bio, EADidascalia type) {
        String nomeCognome = "";

        if (bio != null && type != null) {
            switch (type) {
                case listaNomi:
                    nomeCognome = bio.cognome != null ? bio.cognome : "";
                    break;
                case listaCognomi:
                    nomeCognome = bio.nome != null ? bio.nome : "";
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

        return nomeCognome;
    }// end of method


    /**
     * Returns all entities of the type <br>
     * <p>
     *
     * @return all ordered entities
     */
    public ArrayList<Long> findAllPageids() {
        ArrayList<Long> listaLong = new ArrayList<>();
        List<? extends AEntity> listaPagine = null;
        long inizio = System.currentTimeMillis();
        int size = pref.getInt(MONGO_PAGE_LIMIT);
        Sort sort = new Sort(Sort.Direction.ASC, "pageid");

        for (int k = 0; k < array.numCicli(count(), size); k++) {
            listaPagine = mongo.mongoOp.find(new Query().with(PageRequest.of(k, size, sort)), Bio.class);
            if (array.isValid(listaPagine)) {
                for (AEntity entity : listaPagine) {
                    listaLong.add(((Bio) entity).pageid);
                }// end of for cycle
                if (pref.isBool(FlowCost.USA_DEBUG)) {
                    log.info("Debug. Recuperate " + text.format(listaLong.size()) + " pagine da bioService.findAllPageids()");
                }// end of if cycle
            } else {
                log.warn("Qualcosa non ha funzionato in BioService.findAllPageids()");
            }// end of if/else cycle
        }// end of for cycle

        if (pref.isBool(FlowCost.USA_DEBUG)) {
            logger.debug("Recuperate " + text.format(listaLong.size()) + " pagine da bioService.findAllPageids() in " + date.deltaText(inizio));
            log.info("Recuperata una lista di " + text.format(listaLong.size()) + " pageid da bioService.findAllPageids() in " + date.deltaText(inizio));
        }// end of if cycle

        return listaLong;
    }// end of method


    /**
     * Returns all entities of the type <br>
     * <p>
     *
     * @return all ordered entities
     */
    public ArrayList<String> findAllTitles() {
        ArrayList<String> listaString = new ArrayList<>();
        List<? extends AEntity> listaPagine = null;
        long inizio = System.currentTimeMillis();
        int size = pref.getInt(MONGO_PAGE_LIMIT);
        Sort sort = new Sort(Sort.Direction.ASC, "pageid");

        for (int k = 0; k < array.numCicli(count(), size); k++) {
            listaPagine = mongo.mongoOp.find(new Query().with(PageRequest.of(k, size, sort)), Bio.class);
            if (array.isValid(listaPagine)) {
                for (AEntity entity : listaPagine) {
                    listaString.add(((Bio) entity).wikiTitle);
                }// end of for cycle
                if (pref.isBool(FlowCost.USA_DEBUG)) {
                    log.info("Debug. Recuperate " + text.format(listaString.size()) + " pagine da bioService.findAllTitles()");
                }// end of if cycle
            } else {
                log.warn("Qualcosa non ha funzionato in BioService.findAllTitles()");
            }// end of if/else cycle
        }// end of for cycle

        if (pref.isBool(FlowCost.USA_DEBUG)) {
            logger.debug("Recuperate " + text.format(listaString.size()) + " pagine da bioService.findAllTitles() in " + date.deltaText(inizio));
            log.info("Recuperata una lista di " + text.format(listaString.size()) + " pageid da bioService.findAllTitles() in " + date.deltaText(inizio));
        }// end of if cycle

        return listaString;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param wikiTitle della pagina wiki (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Bio findByKeyUnica(String wikiTitle) {
        return repository.findByWikiTitle(wikiTitle);
    }// end of method


    /**
     * Property unica (se esiste) <br>
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Bio) entityBean).getWikiTitle() + "";
    }// end of method


    public Bio downloadBio(String wikiTitle) {
        Bio bio = null;

        Page pagina = api.leggePage(wikiTitle);
        if (pagina != null) {
            long pageId = pagina.getPageid();
            String templateText = api.estraeTmplBio(pagina);
            bio = crea(pageId, wikiTitle, templateText);
            elabora.esegue(bio, true);
        }// end of if cycle

        return bio;
    }// end of method


    public Bio downloadBio(long pageid) {
        Bio bio = null;

        Page pagina = api.leggePage(pageid);
        if (pagina != null) {
            String wikiTitle = pagina.getTitle();
            String templateText = api.estraeTmplBio(pagina);
            bio = crea(pageid, wikiTitle, templateText);
            elabora.esegue(bio, true);
        }// end of if cycle

        return bio;
    }// end of method


    /**
     * Returns only entities of the requested page.
     * <p>
     * Senza filtri
     * Ordinati per sort
     * <p>
     * Methods of this library return Iterable<T>, while the rest of my code expects Collection<T>
     * L'annotation standard di JPA prevede un ritorno di tipo Iterable, mentre noi usiamo List
     * Eseguo qui la conversione, che rimane trasparente al resto del programma
     *
     * @param offset numero di pagine da saltare, parte da zero
     * @param size   numero di elementi per ogni pagina
     *
     * @return all entities
     */
    public List<? extends AEntity> findAll(int offset, int size) {
        Sort sort = new Sort(Sort.Direction.ASC, "cognome");
        return findAll(offset, size, sort);
    }// end of method


    /**
     * Returns pageid list of all entities of the type.
     * <p>
     * Senza filtri
     * Ordinati per sort
     *
     * @return all entities
     */
    public ArrayList<Long> findAllPageid() {
        ArrayList<Long> listaPageid = null;
        List<Bio> listaBio;

        listaBio = mongo.findAllProperty("pageid", Bio.class);

        if (array.isValid(listaBio)) {
            listaPageid = new ArrayList<>();
            for (Bio bio : listaBio) {
                listaPageid.add(bio.getPageid());
            }// end of for cycle
        }// end of if cycle

        return listaPageid;
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
                lista.add(((Bio) entity).pageid);
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Returns WrapTime list of the requested page.
     * <p>
     * Senza filtri
     * Ordinati per sort
     *
     * @param offset numero di pagine da saltare, parte da zero
     * @param size   numero di elementi per ogni pagina
     * @param sort   ordinamento degli elementi
     *
     * @return list of WrapTime
     */
    public ArrayList<WrapTime> findAllWrapTime(int offset, int size, Sort sort) {
        ArrayList<WrapTime> lista = null;
        ArrayList<? extends AEntity> listaEntities = findAll(offset, size, sort);

        if (array.isValid(listaEntities)) {
            lista = new ArrayList<>();
            for (AEntity entity : listaEntities) {
                lista.add(new WrapTime(((Bio) entity).pageid, ((Bio) entity).wikiTitle, Timestamp.valueOf(((Bio) entity).lastLettura)));
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Returns map of the requested page.
     * <p>
     * Senza filtri
     * Ordinati per sort
     *
     * @param offset numero di pagine da saltare, parte da zero
     * @param size   numero di elementi per ogni pagina
     * @param sort   ordinamento degli elementi
     *
     * @return map (wikititle,timestamp)
     */
    public LinkedHashMap<Long, Timestamp> findTimestampMap(int offset, int size, Sort sort) {
        LinkedHashMap<Long, Timestamp> mappa = null;
        ArrayList<? extends AEntity> listaEntities = findAll(offset, size, sort);

        if (array.isValid(listaEntities)) {
            mappa = new LinkedHashMap<>();
            for (AEntity entity : listaEntities) {
                mappa.put(((Bio) entity).pageid, Timestamp.valueOf(((Bio) entity).lastLettura));
            }// end of for cycle
        }// end of if cycle

        return mappa;
    }// end of method


    /**
     * Delete a list of entities.
     *
     * @param listaId di ObjectId da cancellare
     *
     * @return numero di elementi cancellati
     */
    public int deleteBulk(ArrayList<String> listaId) {
        return super.deleteBulk(listaId, Bio.class);
    }// end of method


    /**
     * Delete a list of entities.
     *
     * @param listaPage di ObjectId da cancellare
     *
     * @return numero di elementi cancellati
     */
    public DeleteResult deleteBulkByPageid(ArrayList<Long> listaPage) {
        return super.deleteBulkByProperty(listaPage, Bio.class, "page");
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
        return Arrays.asList("wikiTitle", "nome", "cognome", "luogoNato", "attivita", "nazionalita");
    }// end of method


    public List<String> findDistinctNome() {
//        return repository.findDistinctNome("nome");
//        return repository.findDistinctByNome();
//        JSONArray lista= repository.listDistinctNome();
        return null;
    }// end of method


//    /**
//     * Fetches the entities whose 'main text property' matches the given filter text.
//     * <p>
//     * Se esiste la company, filtrate secondo la company <br>
//     * The matching is case insensitive. When passed an empty filter text,
//     * the method returns all categories. The returned list is ordered by name.
//     * The 'main text property' is different in each entity class and chosen in the specific subclass
//     *
//     * @param filter the filter text
//     *
//     * @return the list of matching entities
//     */
//    @Override
//    public List<? extends AEntity> findFilter(String filter) {
//        List<? extends AEntity> lista = null;
//        String normalizedFilter = filter.toLowerCase();
//
//        lista = findAll();
//        if (lista != null) {
//            lista = lista.stream()
//                    .filter(entity -> {
//                        return ((Bio)entity).getWikiTitle().toLowerCase().startsWith(normalizedFilter);
//                    })
//                    .collect(Collectors.toList());
//        }// end of if cycle
//
//        return lista;
//    }// end of method

}// end of class