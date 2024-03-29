package it.algos.vaadwiki.modules.professione;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.wiki.WikiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.*;
import static it.algos.vaadwiki.modules.attivita.AttivitaService.EX;
import static it.algos.vaadwiki.modules.attivita.AttivitaService.EX2;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 6-ott-2018 7.29.00 <br>
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
@Qualifier(TAG_PRO)
@Slf4j
@AIScript(sovrascrivibile = false)
public class ProfessioneService extends WikiService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public ProfessioneRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public ProfessioneService(@Qualifier(TAG_PRO) MongoRepository repository) {
        super(repository);
        super.entityClass = Professione.class;
        this.repository = (ProfessioneRepository) repository;
        super.titoloModulo = titoloModuloProfessione;
        super.codeLastDownload = LAST_DOWNLOAD_PROFESSIONE;
        super.durataLastDownload = DURATA_DOWNLOAD_PROFESSIONE;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     * @param pagina    wiki di riferimento per la professione - pipedlink (obbligatorio NON unico)
     *
     * @return la entity trovata o appena creata
     */
    public Professione findOrCrea(String singolare, String pagina) {
        return findOrCrea(singolare, pagina, false);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     * @param pagina    wiki di riferimento per la professione - pipedlink (obbligatorio NON unico)
     * @param aggiunta  oltre alle voci presenti nel modulo wiki
     *
     * @return la entity trovata o appena creata
     */
    public Professione findOrCrea(String singolare, String pagina, boolean aggiunta) {
        Professione entity = findByKeyUnica(singolare);

        if (entity == null) {
            entity = crea(singolare, pagina, aggiunta);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     * @param pagina    wiki di riferimento per la professione - pipedlink (obbligatorio NON unico)
     * @param aggiunta  oltre alle voci presenti nel modulo wiki
     *
     * @return la entity appena creata
     */
    public Professione crea(String singolare, String pagina, boolean aggiunta) {
        return (Professione) save(newEntity(singolare, pagina, aggiunta));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Professione newEntity() {
        return newEntity("", "", false);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     * @param pagina    wiki di riferimento per la professione - pipedlink (obbligatorio NON unico)
     * @param aggiunta  oltre alle voci presenti nel modulo wiki
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Professione newEntity(String singolare, String pagina, boolean aggiunta) {
        Professione entity = null;

        entity = findByKeyUnica(singolare);
        if (entity != null) {
            return findByKeyUnica(singolare);
        }// end of if cycle

        entity = new Professione();
        entity.singolare=singolare.equals("") ? null : singolare;
        entity.pagina=pagina.equals("") ? null : pagina;
        entity.aggiunta=aggiunta;

        return entity;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Professione findByKeyUnica(String singolare) {
        return repository.findBySingolare(singolare);
    }// end of method


    /**
     * Property unica (se esiste) <br>
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Professione) entityBean).singolare;
    }// end of method


    /**
     * Conta tutte le professioni NON aggiunte (quelle originariamente presenti nel modulo) <br>
     */
    public int countAggiunta() {
        return repository.countAllByAggiuntaIsTrue();
    }// end of method


    /**
     * Conta tutte le professioni aggiunte (quelle aggiunte nella collezione mongoDB) <br>
     */
    public int countAggiuntaFalsa() {
        return repository.countAllByAggiuntaIsFalse();
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
    public List<Professione> findAll() {
        return (List<Professione>) super.findAll();
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
        return Arrays.asList("pagina");
    }// end of method


    /**
     * Pagina da linkare (se esiste).
     */
    public String getPagina(String singolare) {
        String pagina = "";
        Professione professione = findByKeyUnica(singolare);

        if (professione != null) {
            pagina = professione.pagina;
        }// end of if cycle

        return pagina;
    }// end of method


    /**
     * Download completo del modulo da Wiki <br>
     * Cancella tutte le precedenti entities <br>
     * Registra su Mongo DB tutte le occorrenze di attività/nazionalità <br>
     */
    @Override
    public void download() {
        super.download();
//        System.out.println("");
//        System.out.println("download - Ci sono " + count() + " professioni");

        this.aggiungeProfessioni();
        this.aggiungeAttivitaEx();
        this.aggiungeProfessioniMancanti();
    }// end of method


    /**
     * Aggiunge le professioni che NON corrispondono al titolo della pagina wiki di destinazione <br>
     * Spazzola tutta la collezione <br>
     * Per ogni professione legge la pagina e crea (se non esiste) una entity con lo stesso nome <br>
     */
    private void aggiungeProfessioni() {
        List<Professione> lista = findAll();
        String pagina;
        int prima;
        int dopo;

        if (array.isValid(lista)) {
            prima = countAggiunta();
            for (Professione professione : lista) {
                pagina = professione.pagina;
                findOrCrea(pagina, pagina, true);
            }// end of for cycle

            dopo = countAggiunta();
//            System.out.println("");
//            System.out.println("aggiungeProfessioni - Sono state aggiunte " + (dopo - prima) + " professioni");
        }// end of if cycle

    }// end of method


    /**
     * Aggiunge le ex-attività (non presenti nel Modulo su Wiki) che sono state aggiunte nella collection Attivita su mongoDSB <br>
     */
    private void aggiungeAttivitaEx() {
        List<Attivita> lista = attivitaService.findAllAggiunte();
        String singolareAttivita;
        String singolareProfessione = VUOTA;
        String pagina = VUOTA;
        Professione professione;
        int prima;
        int dopo;

        if (array.isValid(lista)) {
            prima = countAggiunta();
            for (Attivita attivita : lista) {
                singolareAttivita = attivita.singolare;

                if (singolareAttivita.startsWith(EX)) {
                    singolareProfessione = text.levaTesta(singolareAttivita, EX);
                }// end of if cycle
                if (singolareAttivita.startsWith(EX2)) {
                    singolareProfessione = text.levaTesta(singolareAttivita, EX2);
                }// end of if cycle

                professione = findByKeyUnica(singolareProfessione);
                if (professione != null) {
                    findOrCrea(singolareAttivita, professione.pagina, true);
                }// end of if cycle
            }// end of for cycle

            dopo = countAggiunta();
//            System.out.println("");
//            System.out.println("aggiungeAttivitaEx - Sono state aggiunte " + (dopo - prima) + " professioni");
        }// end of if cycle

    }// end of method


    /**
     * Aggiunge le professioni che mancano recuperando la pagina <br>
     * Aggiunge i plurali delle professioni per una ricerca più rapida <br>
     * es.: poliziotto -> polizia <br>
     * es.: carabiniere -> carabiniere <br>
     * es.: academici -> accademico <br>
     * es.: abati -> abate <br>
     * es.: badesse -> badessa <br>
     * es.: poliziotti -> poliziotto <br>
     * es.: carabinieri -> carabiniere <br>
     * es.:
     */
    private void aggiungeProfessioniMancanti() {
        int prima;
        int dopo;


        prima = countAggiunta();
        dopo = countAggiunta();
//        System.out.println("");
//        System.out.println("aggiungeProfessioniMancanti - Sono state aggiunte " + (dopo - prima) + " professioni");
    }// end of method

}// end of class