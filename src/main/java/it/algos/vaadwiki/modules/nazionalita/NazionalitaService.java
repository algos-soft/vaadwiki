package it.algos.vaadwiki.modules.nazionalita;

import com.vaadin.flow.spring.annotation.SpringComponent;
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
public class NazionalitaService extends AttNazProfCatService {


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

        entity = Nazionalita.builderNazionalita()
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
    public Nazionalita findByKeyUnica(String singolare) {
        return repository.findBySingolare(singolare);
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
        return ((Nazionalita) entityBean).getSingolare();
    }// end of method

}// end of class