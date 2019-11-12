package it.algos.vaadwiki.modules.professione;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadwiki.modules.attnazprofcat.AttNazProfCatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static it.algos.vaadwiki.application.WikiCost.*;

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
public class ProfessioneService extends AttNazProfCatService {


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
        Professione entity = findByKeyUnica(singolare);

        if (entity == null) {
            entity = crea(singolare, pagina);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     * @param pagina    wiki di riferimento per la professione - pipedlink (obbligatorio NON unico)
     *
     * @return la entity appena creata
     */
    public Professione crea(String singolare, String pagina) {
        return (Professione) save(newEntity(singolare, pagina));
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
        return newEntity("", "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     * @param pagina    wiki di riferimento per la professione - pipedlink (obbligatorio NON unico)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Professione newEntity(String singolare, String pagina) {
        Professione entity = null;

        entity = findByKeyUnica(singolare);
        if (entity != null) {
            return findByKeyUnica(singolare);
        }// end of if cycle

        entity = Professione.builderProfessione()
                .singolare(singolare.equals("") ? null : singolare)
                .pagina(pagina.equals("") ? null : pagina)
                .build();

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
        return ((Professione) entityBean).getSingolare();
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
        this.aggiunge();
    }// end of method


    /**
     * Aggiunge le attività che NON corrispondono al titolo della pagina wiki di destinazione <br>
     * Spazzola tutta la collezione <br>
     * Per ogni professione legge la pagina e crea (se non esiste) una entity con lo stesso nome <br>
     */
    private void aggiunge() {
        List<Professione> lista = findAll();
        String pagina;

        if (array.isValid(lista)) {
            for (Professione professione : lista) {
                pagina = professione.pagina;
                findOrCrea(pagina, pagina);
            }// end of for cycle
        }// end of if cycle

    }// end of method

}// end of class