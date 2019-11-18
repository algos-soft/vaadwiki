package it.algos.vaadwiki.modules.attivita;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadwiki.modules.attnazprofcat.AttNazProfCatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 6-ott-2019 18.00.03 <br>
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
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_ATT)
@Slf4j
@AIScript(sovrascrivibile = false)
public class AttivitaService extends AttNazProfCatService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public AttivitaRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public AttivitaService(@Qualifier(TAG_ATT) MongoRepository repository) {
        super(repository);
        super.entityClass = Attivita.class;
        this.repository = (AttivitaRepository) repository;
        super.titoloModulo = titoloModuloAttivita;
        super.codeLastDownload = LAST_DOWNLOAD_ATTIVITA;
        super.durataLastDownload = DURATA_DOWNLOAD_ATTIVITA;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     * @param plurale   neutro (obbligatorio NON unico)
     *
     * @return la entity trovata o appena creata
     */
    public Attivita findOrCrea(String singolare, String plurale) {
        Attivita entity = findByKeyUnica(singolare);

        if (entity == null) {
            entity = crea(singolare, plurale);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     * @param plurale   neutro (obbligatorio NON unico)
     *
     * @return la entity appena creata
     */
    public Attivita crea(String singolare, String plurale) {
        return (Attivita) save(newEntity(singolare, plurale));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Attivita newEntity() {
        return newEntity("", "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     * @param plurale   neutro (obbligatorio NON unico)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Attivita newEntity(String singolare, String plurale) {
        Attivita entity = null;

        entity = findByKeyUnica(singolare);
        if (entity != null) {
            return findByKeyUnica(singolare);
        }// end of if cycle

        entity = Attivita.builderAttivita()
                .singolare(singolare.equals("") ? null : singolare)
                .plurale(plurale.equals("") ? null : plurale)
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
    public Attivita findByKeyUnica(String singolare) {
        return repository.findBySingolare(singolare);
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
    public List<Attivita> findAll() {
        return (List<Attivita>) super.findAll();
    }// end of method


    public List<String> findAllPlurali() {
        List<String> listaAttivitaPlurali = null;

        listaAttivitaPlurali = mongo.mongoOp.findDistinct("plurale", Attivita.class, String.class);
        listaAttivitaPlurali = array.sort(listaAttivitaPlurali);

        return listaAttivitaPlurali;
    }// end of method


    public List<Attivita> findAllByPlurale(String plurale) {
        return repository.findAllByPlurale(plurale);
    }// end of method


    /**
     * Controlla l'esistenza di una Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     *
     * @return true se trovata
     */
    public boolean isEsiste(String singolare) {
        return findByKeyUnica(singolare) != null;
    }// end of method


    /**
     * Property unica (se esiste).
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Attivita) entityBean).getSingolare();
    }// end of method


}// end of class