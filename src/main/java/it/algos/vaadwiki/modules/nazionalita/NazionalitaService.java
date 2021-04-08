package it.algos.vaadwiki.modules.nazionalita;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
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

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwiki.application.WikiCost.DURATA_DOWNLOAD_NAZIONALITA;
import static it.algos.vaadwiki.application.WikiCost.LAST_DOWNLOAD_NAZIONALITA;
import static it.algos.vaadwiki.application.WikiCost.TAG_NAZ;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 5-ott-2018 12.02.34 <br>
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
@Qualifier(TAG_NAZ)
@Slf4j
@AIScript(sovrascrivibile = false)
public class NazionalitaService extends WikiService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public NazionalitaRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public NazionalitaService(@Qualifier(TAG_NAZ) MongoRepository repository) {
        super(repository);
        super.entityClass = Nazionalita.class;
        this.repository = (NazionalitaRepository) repository;
        super.titoloModulo = titoloModuloNazionalita;
        super.codeLastDownload = LAST_DOWNLOAD_NAZIONALITA;
        super.durataLastDownload = DURATA_DOWNLOAD_NAZIONALITA;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     * @param plurale   neutro (obbligatorio NON unico)
     *
     * @return la entity trovata o appena creata
     */
    public Nazionalita findOrCrea(String singolare, String plurale) {
        Nazionalita entity = findByKeyUnica(singolare);

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
    public Nazionalita crea(String singolare, String plurale) {
        return (Nazionalita) save(newEntity(singolare, plurale));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Nazionalita newEntity() {
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
    public Nazionalita newEntity(String singolare, String plurale) {
        Nazionalita entity = null;

        entity = findByKeyUnica(singolare);
        if (entity != null) {
            return findByKeyUnica(singolare);
        }// end of if cycle

        entity = new Nazionalita();
        entity.singolare=singolare.equals("") ? null : singolare;
        entity.plurale=plurale.equals("") ? null : plurale;

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
    public Nazionalita findById(String id) {
        return (Nazionalita) super.findById(id);
    }// end of method

    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Nazionalita findByKeyUnica(String singolare) {
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
    public List<Nazionalita> findAll() {
        return (List<Nazionalita>) super.findAll();
    }// end of method


    public List<String> findAllPlurali() {
        List<String> listaNazionalitaPlurali = null;

        listaNazionalitaPlurali = mongo.mongoOp.findDistinct("plurale", Nazionalita.class, String.class);
        listaNazionalitaPlurali = array.sort(listaNazionalitaPlurali);

        return listaNazionalitaPlurali;
    }// end of method


    public List<Nazionalita> findAllByPlurale(String plurale) {
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
        return ((Nazionalita) entityBean).singolare;
    }// end of method

    public int countDistinctPlurale() {
        List<String> lista = new ArrayList<>();
        List<Nazionalita> listaNazionalita = repository.findAllByOrderByPluraleAsc();

        if (array.isValid(listaNazionalita)) {
            for (Nazionalita nazionalita : listaNazionalita) {
                if (!lista.contains(nazionalita.plurale)) {
                    lista.add(nazionalita.plurale);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return lista.size();
    }// end of method

}// end of class