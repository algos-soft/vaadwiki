package it.algos.vaadflow.modules.preferenza;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAPrefType;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.service.AService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_PRE;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 30-set-2018 16.14.56 <br>
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
@Qualifier(TAG_PRE)
@Slf4j
@AIScript(sovrascrivibile = false)
public class PreferenzaService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private PreferenzaRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public PreferenzaService(@Qualifier(TAG_PRE) MongoRepository repository) {
        super(repository);
        super.entityClass = Preferenza.class;
        this.repository = (PreferenzaRepository) repository;
    }// end of Spring constructor

    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param code        codice di riferimento (obbligatorio)
     * @param descrizione (facoltativa)
     * @param type        (obbligatorio) per convertire in byte[] i valori
     * @param value       (obbligatorio) memorizza tutto in byte[]
     *
     * @return la entity trovata o appena creata
     */
    public Preferenza findOrCrea(String code, String descrizione, EAPrefType type, Object value) {
        Preferenza entity = findByKeyUnica(code);

        if (entity == null) {
            entity = crea(code, descrizione, type, value);
        }// end of if cycle

        return entity;
    }// end of method

    /**
     * Crea una entity e la registra <br>
     *
     * @param code        codice di riferimento (obbligatorio)
     * @param descrizione (facoltativa)
     * @param type        (obbligatorio) per convertire in byte[] i valori
     * @param value       (obbligatorio) memorizza tutto in byte[]
     *
     * @return la entity appena creata
     */
    public Preferenza crea(String code, String descrizione, EAPrefType type, Object value) {
        Preferenza entity = newEntity((Company) null, 0, code, descrizione, type, value);
        save(entity);
        return entity;
    }// end of method

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Preferenza newEntity() {
        return newEntity(null, 0, "", "", null, null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok) <br>
     *
     * @param code        codice di riferimento (obbligatorio)
     * @param descrizione (facoltativa)
     * @param type        (obbligatorio) per convertire in byte[] i valori
     * @param value       (obbligatorio) memorizza tutto in byte[]
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Preferenza newEntity(String code, String descrizione, EAPrefType type, Object value) {
        return newEntity((Company) null, 0, code, descrizione, type, value);
    }// end of method

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok) <br>
     *
     * @param company     di appartenenza (obbligatoria, se manca viene recuperata dal login)
     * @param ordine      di presentazione (obbligatorio con inserimento automatico se è zero)
     * @param code        codice di riferimento (obbligatorio)
     * @param descrizione (facoltativa)
     * @param type        (obbligatorio) per convertire in byte[] i valori
     * @param value       (obbligatorio) memorizza tutto in byte[]
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Preferenza newEntity(Company company, int ordine, String code, String descrizione, EAPrefType type, Object value) {
        Preferenza entity = null;

        entity = findByKeyUnica(code);
        if (entity != null) {
            return findByKeyUnica(code);
        }// end of if cycle

        entity = Preferenza.builderPreferenza()
                .ordine(ordine != 0 ? ordine : this.getNewOrdine())
                .code(text.isValid(code) ? code : null)
                .descrizione(text.isValid(descrizione) ? descrizione : null)
                .type(type != null ? type : EAPrefType.string)
                .value(type != null ? type.objectToBytes(value) : (byte[]) null)
                .build();

        return (Preferenza) addCompany(entity, company);
    }// end of method

    /**
     * Fetches the entities whose 'main text property' matches the given filter text.
     * <p>
     * Se esiste la company, filtrate secondo la company <br>
     * The matching is case insensitive. When passed an empty filter text,
     * the method returns all categories. The returned list is ordered by name.
     * The 'main text property' is different in each entity class and chosen in the specific subclass
     *
     * @param filter the filter text
     *
     * @return the list of matching entities
     */
    @Override
    public List<? extends AEntity> findFilter(String filter) {
        return findAll(); //@todo PROVVISORIO
    }

    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param code di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Preferenza findByKeyUnica(String code) {
        return repository.findByCode(code);
    }// end of method


    /**
     * Returns all instances of the type <br>
     * La Entity è EACompanyRequired.nonUsata. Non usa Company. <br>
     * Lista ordinata <br>
     *
     * @return lista ordinata di tutte le entities
     */
    @Override
    public List<Preferenza> findAll() {
        List<Preferenza> lista = null;

        lista = repository.findAllByOrderByOrdineAsc();

        return lista;
    }// end of method


//    /**
//     * Fetches the entities whose 'main text property' matches the given filter text.
//     * <p>
//     * The matching is case insensitive. When passed an empty filter text,
//     * the method returns all categories. The returned list is ordered by name.
//     * The 'main text property' is different in each entity class and chosen in the specific subclass
//     *
//     * @param filter the filter text
//     *
//     * @return the list of matching entities
//     */
//    @Override
//    public List<Preferenza> findFilter(String filter) {
//        String normalizedFilter = filter.toLowerCase();
//        List<Preferenza> lista = findAll();
//
//        return lista.stream()
//                .filter(entity -> entity.getCode().toLowerCase().contains(normalizedFilter))
//                .collect(Collectors.toList());
//    }// end of method


//    /**
//     * Property unica (se esiste).
//     */
//    @Override
//    public String getPropertyUnica(AEntity entityBean) {
//        return ((Preferenza) entityBean).getCode();
//    }// end of method

//    /**
//     * Opportunità di controllare (per le nuove schede) che la key unica non esista già. <br>
//     * Invocato appena prima del save(), solo per una nuova entity <br>
//     *
//     * @param entityBean nuova da creare
//     */
//    @Override
//    public boolean isEsisteEntityKeyUnica(AEntity entityBean) {
//        return findByKeyUnica(((Preferenza) entityBean).getCode()) != null;
//    }// end of method

//    /**
//     * Opportunità di usare una idKey specifica. <br>
//     * Invocato appena prima del save(), solo per una nuova entity <br>
//     *
//     * @param entityBean da salvare
//     */
//    protected void creaIdKeySpecifica(AEntity entityBean) {
//        entityBean.id = ((Preferenza) entityBean).getCode();
//    }// end of method

    /**
     * Ordine di presentazione (obbligatorio, unico per tutte le eventuali company), <br>
     * Viene calcolato in automatico alla creazione della entity <br>
     * Recupera dal DB il valore massimo pre-esistente della property <br>
     * Incrementa di uno il risultato <br>
     */
    public int getNewOrdine() {
        int ordine = 0;

        List<Preferenza> lista = repository.findTop1AllByOrderByOrdineDesc();
        if (lista != null && lista.size() == 1) {
            ordine = lista.get(0).getOrdine();
        }// end of if cycle

        return ordine + 1;
    }// end of method


    public Object getValue(String keyCode) {
        Object value = null;
        Preferenza pref = findByKeyUnica(keyCode);

        if (pref != null) {
            value = pref.getType().bytesToObject(pref.value);
        }// end of if cycle

        return value;
    } // end of method


    public LocalDateTime getDate(String code) {
        LocalDateTime value = null;
        Object genericValue = getValue(code);

        if (genericValue instanceof LocalDateTime) {
            value = (LocalDateTime) genericValue;
        }// end of if cycle

        return value;
    } // end of method

    public Boolean isBool(String keyCode) {
        boolean status = false;
        Object value = getValue(keyCode);

        if (value != null && value instanceof Boolean) {
            status = (boolean) value;
        }// end of if cycle

        return status;
    } // end of method


    public Preferenza setValue(String keyCode, Object value) {
        Preferenza pref = findByKeyUnica(keyCode);

        if (pref != null) {
            pref.setValue(pref.getType().objectToBytes(value));
        }// end of if cycle

        return pref;
    } // end of method


    public void saveValue(String keyCode, Object value) {
        Preferenza pref = setValue(keyCode, value);

        if (pref != null) {
            this.save(pref);
        }// end of if cycle

    } // end of method

//    public  Boolean getBool(String code, Object defaultValue) {
//        return getBool(code, CompanySessionLib.getCompany(), defaultValue);
//    } // end of method
//
//    public  Boolean getBool(String code, BaseCompany company) {
//        return getBool(code, company, "");
//    } // end of static method
//
//    public  Boolean getBool(String code, BaseCompany company, Object defaultValue) {
//        Pref pref = Pref.findByCode(code, company);
//
//        if (pref != null) {
//            return (boolean) pref.getValore();
//        }// end of if cycle
//
//        if (defaultValue != null && defaultValue instanceof Boolean) {
//            return (boolean) defaultValue;
//        }// end of if cycle
//
//        return false;
//    } // end of method

}// end of class