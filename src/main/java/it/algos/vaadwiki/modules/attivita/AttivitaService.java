package it.algos.vaadwiki.modules.attivita;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadwiki.modules.genere.Genere;
import it.algos.vaadwiki.modules.wiki.WikiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
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
public class AttivitaService extends WikiService {

    public static String EX = "ex ";
    public static String EX2 = "ex-";

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
        return findOrCrea(singolare, plurale, false);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     * @param plurale   neutro (obbligatorio NON unico)
     * @param aggiunta  oltre alle voci presenti nel modulo wiki
     *
     * @return la entity trovata o appena creata
     */
    public Attivita findOrCrea(String singolare, String plurale, boolean aggiunta) {
        Attivita entity = findByKeyUnica(singolare);

        if (entity == null) {
            entity = crea(singolare, plurale, aggiunta);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     * @param plurale   neutro (obbligatorio NON unico)
     * @param aggiunta  oltre alle voci presenti nel modulo wiki
     *
     * @return la entity appena creata
     */
    public Attivita crea(String singolare, String plurale, boolean aggiunta) {
        return (Attivita) save(newEntity(singolare, plurale, aggiunta));
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
        return newEntity("", "", false);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     * @param plurale   neutro (obbligatorio NON unico)
     * @param aggiunta  oltre alle voci presenti nel modulo wiki
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Attivita newEntity(String singolare, String plurale, boolean aggiunta) {
        Attivita entity = null;

        entity = findByKeyUnica(singolare);
        if (entity != null) {
            return findByKeyUnica(singolare);
        }// end of if cycle

        entity = Attivita.builderAttivita()
                .singolare(singolare.equals("") ? null : singolare)
                .plurale(plurale.equals("") ? null : plurale)
                .aggiunta(aggiunta)
                .build();

        return entity;
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
    public Attivita findById(String id) {
        return (Attivita) super.findById(id);
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


    public List<Attivita> findAllAggiunte() {
        return repository.findAllByAggiuntaIsTrue();
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


    public int countDistinctPlurale() {
        List<String> lista = new ArrayList<>();
        List<Attivita> listaAttivita = repository.findAllByOrderByPluraleAsc();

        if (array.isValid(listaAttivita)) {
            for (Attivita attivita : listaAttivita) {
                if (!lista.contains(attivita.plurale)) {
                    lista.add(attivita.plurale);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return lista.size();
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
     * Aggiunge le ex-attività NON presenti nel modulo 'Modulo:Bio/Plurale attività' <br>
     * Le recupera dal modulo 'Modulo:Bio/Plurale attività genere' <br>
     * Le aggiunge se trova la corrispondenza tra il nome con e senza EX <br>
     */
    private void aggiunge() {
        List<Genere> listaGenere = genereService.findAll();
        String attivitaSingolare;
        String genereSingolare;
        Attivita entity;

        if (array.isValid(listaGenere)) {
            for (Genere genere : listaGenere) {
                entity = null;
                attivitaSingolare = VUOTA;
                genereSingolare = genere.singolare;

                if (genereSingolare.startsWith(EX)) {
                    attivitaSingolare = genereSingolare.substring(EX.length());
                }// end of if cycle
                if (genereSingolare.startsWith(EX2)) {
                    attivitaSingolare = genereSingolare.substring(EX2.length());
                }// end of if cycle

                if (text.isValid(attivitaSingolare)) {
                    entity = findByKeyUnica(attivitaSingolare);
                }// end of if cycle

                if (entity != null) {
                    crea(genereSingolare, entity.plurale, true);
                }// end of if cycle

            }// end of for cycle
        }// end of if cycle

    }// end of method

}// end of class