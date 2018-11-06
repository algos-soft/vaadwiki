package it.algos.vaadflow.service;

import com.mongodb.client.result.DeleteResult;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.data.FlowData;
import it.algos.vaadflow.backend.entity.ACEntity;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static it.algos.vaadflow.application.FlowCost.KEY_CONTEXT;
import static it.algos.vaadflow.ui.dialog.AViewDialog.DURATA;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 08-dic-2017
 * Time: 07:36
 */
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public abstract class AService extends AbstractService implements IAService {


    public final static String FIELD_NAME_ID = "id";

    public final static String FIELD_NAME_ORDINE = "ordine";

    public final static String FIELD_NAME_CODE = "code";

    public final static String FIELD_NAME_DESCRIZIONE = "descrizione";

    public final static String FIELD_NAME_COMPANY = "company";

//    /**
//     * Inietta da Spring come 'session'
//     */
//    @Autowired
//    public ALogin login2;

    //--il modello-dati specifico viene regolato dalla sottoclasse nel costruttore
    public Class<? extends AEntity> entityClass;

    /**
     * Inietta da Spring
     */
    @Autowired
    public AMongoService mongo;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected FlowData flow;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PreferenzaService pref;

    //--la repository dei dati viene iniettata dal costruttore della sottoclasse concreta
    protected MongoRepository repository;


//    @Autowired
//    private LogService logger;


    /**
     * Default constructor
     */
    public AService() {
    }// end of constructor


    /**
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public AService(MongoRepository repository) {
        this.repository = repository;
    }// end of Spring constructor


    @Override
    public AAnnotationService getAnnotationService() {
        return annotation;
    }// end of method


    //    @Override
    public AFieldService getFieldService() {
        return field;
    }// end of method


    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    @Override
    public int count() {
        return mongo.count(entityClass);
    }// end of method


//    /**
//     * Returns the number of entities available for the current company
//     *
//     * @return the number of selected entities
//     */
//    @Override
//    public int countByCompany() {
////        Company company = null;
////
////        if (login != null) {
////            company = login.getCompany();
////        }// end of if cycle
//
//        return countByCompany(company);
//    }// end of method


//    /**
//     * Returns the number of entities available for the company
//     *
//     * @return the number of selected entities
//     */
//    @Override
//    public int countByCompany(Company company) {
//        Long numRecords = 0L;
//        Query query = null;
//
//        if (company != null) {
//            query = new Query(Criteria.where("id").regex("^" + company.getCode()));
//            numRecords = mongo.count(query, entityClass);
//        }// end of if cycle
//
//        return numRecords.intValue();
//    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param keyUnica (obbligatoria, unica)
     *
     * @return istanza della Entity, null se non trovata
     */
    protected AEntity findByKeyUnica(String keyUnica) {
        return null;
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
    public AEntity findById(String id) {
        AEntity entityBean = null;
        Object genericObj = null;
        Optional optional = repository.findById(id.trim());

        if (optional.isPresent()) {
            genericObj = optional.get();
            if (genericObj instanceof AEntity) {
                entityBean = (AEntity) genericObj;
            }// end of if cycle
        }// end of if cycle

        return entityBean;
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
    public List<? extends AEntity> findAll() {
        List<? extends AEntity> lista = null;
        String sortName = "";
        Sort sort;

        if (entityClass == null) {
            return null;
        }// end of if cycle

        if (reflection.isEsiste(entityClass, FIELD_NAME_ORDINE)) {
            sortName = FIELD_NAME_ORDINE;
        } else {
            if (reflection.isEsiste(entityClass, FIELD_NAME_CODE)) {
                sortName = FIELD_NAME_CODE;
            } else {
                if (reflection.isEsiste(entityClass, FIELD_NAME_DESCRIZIONE)) {
                    sortName = FIELD_NAME_DESCRIZIONE;
                }// end of if cycle
            }// end of if/else cycle
        }// end of if/else cycle

        if (text.isValid(sortName)) {
            sort = new Sort(Sort.Direction.ASC, sortName);
            lista = findAll(sort);
        } else {
            lista = findAll((Sort) null);
        }// end of if/else cycle

        return lista;
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
        return findAll(offset, size, (Sort) null);
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
     * @param sort   ordinamento degli elementi
     *
     * @return all entities
     */
    public List<? extends AEntity> findAll(int offset, int size, Sort sort) {
        Pageable page = PageRequest.of(offset, size);
        return repository.findAll(page).getContent();
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
    protected List<? extends AEntity> findAll(Sort sort) {
        List<? extends AEntity> lista = null;

        try { // prova ad eseguire il codice
            if (sort != null) {
                lista = repository.findAll(sort);
            } else {
                lista = repository.findAll();
            }// end of if/else cycle
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString() + " in AService.findAll(Sort sort)");
        }// fine del blocco try-catch

        return lista;
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
    @Deprecated
    public List<? extends AEntity> findFilter(String filter) {
        List<? extends AEntity> lista = null;
        String normalizedFilter = filter.toLowerCase();
//        boolean appUsaCompany = pref.isBool(EAPreferenza.usaCompany.getCode());
        boolean entityUsaCompanyObbligatoria = annotation.getCompanyRequired(entityClass) == EACompanyRequired.obbligatoria;
        boolean entityUsaCompanyFacoltativa = annotation.getCompanyRequired(entityClass) == EACompanyRequired.facoltativa;
//        boolean entityUsaCompany = ();
        boolean mancaCompany = mancaCompanyNecessaria();
//        Company companyLoggata = login.getCompany();
//        boolean nonEsisteCompany = companyLoggata == null;
//        boolean notDeveloper = !login.isDeveloper();
//        String companyCode = companyLoggata != null ? companyLoggata.getCode() : "";

        if (mancaCompany) {
            return lista;
        }// end of if cycle

        if (entityUsaCompanyObbligatoria || entityUsaCompanyFacoltativa) {
            if (entityUsaCompanyObbligatoria) {
                lista = findAll();
                lista = lista.stream()
//                        .filter(entity -> ((ACEntity) entity).company != null)
//                        .filter(entity -> ((ACEntity) entity).company.getCode().equals(companyCode))
                        .filter(entity -> {
                            if (isEsisteEntityKeyUnica(entity)) {
                                return getKeyUnica(entity).toLowerCase().startsWith(normalizedFilter);
                            } else {
                                if (reflection.isEsiste(entityClass, FIELD_NAME_CODE)) {
                                    return ((String) reflection.getPropertyValue(entity, FIELD_NAME_CODE)).startsWith(normalizedFilter);
                                } else {
                                    if (reflection.isEsiste(entityClass, FIELD_NAME_DESCRIZIONE)) {
                                        return ((String) reflection.getPropertyValue(entity, FIELD_NAME_DESCRIZIONE)).startsWith(normalizedFilter);
                                    } else {
                                        return true;
                                    }// end of if/else cycle
                                }// end of if/else cycle
                            }// end of if/else cycle
                        })
                        .collect(Collectors.toList());
            } else {
                lista = findAll();
                if (lista != null) {
                    lista = lista.stream()
                            .filter(entity -> {
                                boolean status = true;
//                                boolean esisteCompany = ((ACEntity) entity).company != null;
//                                if (esisteCompany) {
//                                    status = ((ACEntity) entity).company.getCode().equals(companyCode);
//                                } else {
//                                    status = companyCode.equals("");
//                                }// end of if/else cycle

                                return status;
//                            return ((ACEntity) entity).company != null && ((ACEntity) entity).company.getCode().equals(company.getCode());
                            })
                            .filter(entity -> {
                                if (isEsisteEntityKeyUnica(entity)) {
                                    return getKeyUnica(entity).toLowerCase().startsWith(normalizedFilter);
                                } else {
                                    if (reflection.isEsiste(entityClass, FIELD_NAME_CODE)) {
                                        return ((String) reflection.getPropertyValue(entity, FIELD_NAME_CODE)).startsWith(normalizedFilter);
                                    } else {
                                        if (reflection.isEsiste(entityClass, FIELD_NAME_DESCRIZIONE)) {
                                            return ((String) reflection.getPropertyValue(entity, FIELD_NAME_DESCRIZIONE)).startsWith(normalizedFilter);
                                        } else {
                                            return true;
                                        }// end of if/else cycle
                                    }// end of if/else cycle
                                }// end of if/else cycle
                            })
                            .collect(Collectors.toList());
                }// end of if cycle
            }// end of if/else cycle
        } else {
            lista = findAll();
            if (lista != null) {
                lista = lista.stream()
                        .filter(entity -> {
                            if (isEsisteEntityKeyUnica(entity)) {
                                return getKeyUnica(entity).toLowerCase().startsWith(normalizedFilter);
                            } else {
                                if (reflection.isEsiste(entityClass, FIELD_NAME_CODE)) {
                                    if (reflection.getPropertyValue(entity, FIELD_NAME_CODE) == null) {
                                        return true;
                                    } else {
                                        return ((String) reflection.getPropertyValue(entity, FIELD_NAME_CODE)).startsWith(normalizedFilter);
                                    }// end of if/else cycle
                                } else {
                                    if (reflection.isEsiste(entityClass, FIELD_NAME_DESCRIZIONE)) {
                                        return ((String) reflection.getPropertyValue(entity, FIELD_NAME_DESCRIZIONE)).startsWith(normalizedFilter);
                                    } else {
                                        return true;
                                    }// end of if/else cycle
                                }// end of if/else cycle
                            }// end of if/else cycle
                        })
                        .collect(Collectors.toList());
            }// end of if cycle
        }// end of if/else cycle

        return lista;
    }// end of method


    private Predicate<? extends AEntity> getPredicate(String normalizedFilter) {
        return entity -> getKeyUnica(entity).toLowerCase().contains(normalizedFilter);
    }


    public boolean mancaCompanyNecessaria() {
        boolean status = false;
//        boolean appUsaCompany = pref.isBool(EAPreferenzxa.usaCompany.getCode());
        boolean entityUsaCompany = annotation.getCompanyRequired(entityClass) == EACompanyRequired.obbligatoria;
//        Company company = login.getCompany();
//        boolean nonEsisteCompany = company == null;
//        boolean notDeveloper = !login.isDeveloper();

//        if (appUsaCompany && entityUsaCompany && nonEsisteCompany) {
//            status = true;
//        }// end of if cycle

        return status;
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties della Grid nell'ordine:
     * 1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica
     * todo ancora da sviluppare
     *
     * @param context legato alla sessione
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getGridPropertyNamesList(AContext context) {
        List<String> lista = annotation.getGridPropertiesName(entityClass);

        if (lista.contains(FIELD_NAME_COMPANY) && !context.getLogin().isDeveloper()) {
            lista.remove(FIELD_NAME_COMPANY);
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties del Form nell'ordine:
     * 1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxService
     * todo ancora da sviluppare
     *
     * @param context legato alla sessione
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getFormPropertyNamesList(AEntity curremtItem, AContext context) {
        ArrayList<String> lista = annotation.getFormPropertiesName(entityClass);

        if (lista.contains(FIELD_NAME_COMPANY) && !context.getLogin().isDeveloper()) {
            lista.remove(FIELD_NAME_COMPANY);
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public AEntity newEntity() {
        return null;
    }// end of method


    /**
     * Se la nuova entity usa la company, la recupera dal login
     * Se la campany manca, lancia l'eccezione
     *
     * @param entityBean da creare
     */
    protected AEntity addCompanySeManca(AEntity entityBean) {
        EACompanyRequired tableCompanyRequired;
        Company company = null;

        //--se la EntityClass non estende ACEntity, non deve fare nulla
        if ((entityBean instanceof ACEntity)) {
            company = ((ACEntity) entityBean).company;
        } else {
            return entityBean;
        }// end of if/else cycle

        if (company == null) {
            company = this.getCompany();
        }// end of if cycle

        //--controlla l'obbligatorietà della Company
        tableCompanyRequired = annotation.getCompanyRequired(entityBean.getClass());
        switch (tableCompanyRequired) {
            case nonUsata:
                log.error("C'è una discrepanza tra 'extends ACEntity' della classe " + entityBean.getClass().getSimpleName() + " e l'annotation @AIEntity della classe stessa");
                break;
            case facoltativa:
                if (company != null) {
                    ((ACEntity) entityBean).company = company;
                } else {
                    log.warn("Algos- Nuova scheda senza company (facoltativa) di " + entityBean.toString() + " della classe " + entityBean.getClass().getSimpleName());
                }// end of if/else cycle
                break;
            case obbligatoria:
                if (company != null) {
                    ((ACEntity) entityBean).company = company;
                } else {
                    log.error("Algos- Manca la company (obbligatoria) di " + entityBean.toString() + " della classe " + entityBean.getClass().getSimpleName());
                }// end of if/else cycle
                break;
            default:
                break;
        } // end of switch statement

        return entityBean;
    }// end of method


    public boolean usaCompany() {
        boolean status = false;
        EACompanyRequired tableCompanyRequired = null;

        //--se la EntityClass non estende ACCompany, non deve fare nulla
        tableCompanyRequired = annotation.getCompanyRequired(entityClass);
        status = tableCompanyRequired == EACompanyRequired.obbligatoria || tableCompanyRequired == EACompanyRequired.facoltativa;

        return status;
    }// end of method


    /**
     * Saves a given entity.
     * Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     * <p>
     * Controlla se l'applicazione usa le company2 - flag  AlgosApp.USE_MULTI_COMPANY=true
     * Controlla se la collection (table) usa la company2: può essere
     * a)EACompanyRequired.nonUsata
     * b)EACompanyRequired.facoltativa
     * c)EACompanyRequired.obbligatoria
     *
     * @param entityBean da salvare
     *
     * @return the saved entity
     */
    @Override
    public AEntity save(AEntity entityBean) {
        AEntity entityValida = beforeSave(entityBean, EAOperation.edit);

        if (entityValida != null) {
            entityValida = save(null, entityValida);
        } else {
            if (UI.getCurrent() != null) {
                Notification.show("La scheda non è completa", DURATA, Notification.Position.BOTTOM_START);
            }// end of if cycle
            log.error("Algos - La scheda " + entityBean.toString() + " di " + entityBean.getClass().getSimpleName() + " non è completa");
        }// end of if/else cycle

        return entityValida;
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
    public AEntity beforeSave(AEntity entityBean, EAOperation operation) {
        return creaIdKeySpecifica(entityBean);
    }// end of method


    /**
     * Saves a given entity.
     * Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     *
     * @param oldBean      previus state
     * @param modifiedBean to be saved
     *
     * @return the saved entity
     */
    @Override
    public AEntity save(AEntity oldBean, AEntity modifiedBean) {
        AEntity savedBean = null;
        Object obj = null;

//        EACompanyRequired tableCompanyRequired = annotation.getCompanyRequired(modifiedBean.getClass());
//        Map mappa = null;
//        boolean nuovaEntity = oldBean == null || text.isEmpty(modifiedBean.id);
//
//        //--opportunità di controllare (per le nuove schede) che la key unica non esista già.
//        if (nuovaEntity) {
//            if (isEsisteEntityKeyUnica(modifiedBean)) {
//                Notification.show("Nuova scheda", DuplicateException.MESSAGE, Notification.Type.ERROR_MESSAGE);
//                log.error("Ha cercato di salvare una entity già esistente, ma unica");
//                return null;
//            }// end of if cycle
//        }// end of if cycle
//
        //--opportunità di usare una idKey specifica
        if (text.isEmpty(modifiedBean.id)) {
            creaIdKeySpecifica(modifiedBean);
        }// end of if cycle
//
//        //--inserisce il valore della data attuale per una nuova scheda
//        if (modifiedBean.creazione == null) {
//            modifiedBean.creazione = LocalDateTime.now();
//        }// end of if cycle
//
//        // --inserisce il valore della data attuale per la modifica di una scheda
//        modifiedBean.modifica = LocalDateTime.now();
//
//        //--Controlla se l'applicazione usa le company2
//        if (AlgosApp.USE_MULTI_COMPANY) {
//            switch (tableCompanyRequired) {
//                case nonUsata:
//                    savedBean = (AEntity) repository.save(modifiedBean);
//                    break;
//                case facoltativa:
//                    savedBean = (AEntity) repository.save(modifiedBean);
//                    break;
//                case obbligatoria:
//                    try { // prova ad eseguire il codice
//                        if (checkCompany(modifiedBean, false)) {
//                            savedBean = (AEntity) repository.save(modifiedBean);
//                        } else {
//                            log.warn("Entity non creata perché manca la Company (obbligatoria)");
//                            savedBean = null;
//                        }// end of if/else cycle
//                    } catch (Exception unErrore) { // intercetta l'errore
//                        log.error(unErrore.toString());
//                    }// fine del blocco try-catch
//                    break;
//                default:
//                    log.warn("Switch - caso non definito");
//                    savedBean = (AEntity) repository.save(modifiedBean);
//                    break;
//            } // end of switch statement
//        } else {
//            savedBean = (AEntity) repository.save(modifiedBean);
//        }// end of if/else cycle
//
//        if (!modifiedBean.getClass().getSimpleName().equals("Log")) {
//            if (nuovaEntity) {
//                logNewBean(modifiedBean);
//            } else {
//                mappa = chekDifferences(oldBean, modifiedBean);
//                logDifferences(mappa, modifiedBean);
//            }// end of if/else cycle
//        }// end of if cycle
//
//        return savedBean;

        //--controlla l'integrità dei dati, elaborati dalle sottoclassi nei metodi beforeSave()
        if (modifiedBean.id == null || !modifiedBean.id.equals(FlowCost.STOP_SAVE)) {
            obj = repository.save(modifiedBean);
        }// end of if cycle

        if (obj != null && obj instanceof AEntity) {
            savedBean = (AEntity) obj;
        }// end of if cycle

        return savedBean;
    }// end of method


    /*
     * Opportunità di usare una idKey specifica. <br>
     * Invocato appena prima del save(), solo per una nuova entity (vuol dire che arriva con entityBean.id=null) <br>
     * Se idKey è vuota, uso la standard random mongo (lasciando entityBean.id = null) <br>
     *
     * @param entityBean da salvare
     */
    public AEntity creaIdKeySpecifica(AEntity entityBean) {
        String idKey = "";

        if (text.isEmpty(entityBean.id)) {
            idKey = getKeyUnica(entityBean);
            if (text.isValid(idKey)) {
                entityBean.id = idKey;
            } else {
                entityBean.id = null;
            }// end of if/else cycle
        }// end of if cycle

        return entityBean;
    }// end of method


    /**
     * Opportunità di controllare (per le nuove schede) che la key unica non esista già <br>
     * Invocato appena prima del save(), solo per una nuova entity <br>
     *
     * @param newEntityBean nuova da creare
     */
    public boolean isEsisteEntityKeyUnica(AEntity newEntityBean) {
//        String keyID = getKeyUnica(newEntityBean);
//        if (text.isValid(keyID)) {
//            return findById(keyID) != null;
//        } else {
//            return false;
//        }// end of if/else cycle
        return isEsisteByKeyUnica(getKeyUnica(newEntityBean));
    }// end of method


    /**
     * Opportunità di controllare (per le nuove schede) che una entity con la keyUnica indicata non esista già <br>
     * Invocato appena prima del save(), solo per una nuova entity <br>
     *
     * @param keyUnica di riferimento (obbligatoria ed unica)
     *
     * @return true se la entity con la keyUnica indicata esiste
     */
    public boolean isEsisteByKeyUnica(String keyUnica) {
        if (text.isValid(keyUnica)) {
            return findById(keyUnica) != null;
        } else {
            return false;
        }// end of if/else cycle
    }// end of method


    /**
     * Opportunità di controllare (per le nuove schede) che una entity con la keyUnica indicata non esista già <br>
     * Invocato appena prima del save(), solo per una nuova entity <br>
     *
     * @param keyUnica di riferimento (obbligatoria ed unica)
     *
     * @return true se la entity con la keyUnica indicata non esiste
     */
    public boolean isMancaByKeyUnica(String keyUnica) {
        return !isEsisteByKeyUnica(keyUnica);
    }// end of method


    /**
     * Formula univoca per costruire una (eventuale) idKey
     * Può essere costruita liberamente
     * La utilizza sia in scrittura che in lettura
     * Se usa la company, questa deve esserci (eventualmente vuota)
     * <p>
     * Se esiste la property 'code', usa questa property come idKey <br>
     * Altrimenti, se esiste la property 'ordine', usa questa property come idKey <br>
     * Altrimenti, se esiste un metodo sovrascritto nella sottoclasse concreta, utilizza quello <br>
     * Altrimenti, restituisce un valore vuoto <br>
     *
     * @param entityBean da regolare
     *
     * @return chiave univoca da usare come idKey nel DB mongo
     */
    public String getKeyUnica(AEntity entityBean) {
        String keyUnica = "";
        String keyCode = getPropertyUnica(entityBean);

        if (text.isEmpty(keyCode)) {
            if (reflection.isEsiste(entityClass, FIELD_NAME_CODE)) {
                keyCode = (String) reflection.getPropertyValue(entityBean, FIELD_NAME_CODE);
            } else {
                if (reflection.isEsiste(entityClass, FIELD_NAME_ORDINE)) {
                    keyCode = reflection.getPropertyValue(entityBean, FIELD_NAME_ORDINE).toString();
                }// end of if cycle
            }// end of if/else cycle
        }// end of if cycle

        if (text.isEmpty(keyCode)) {
            return keyCode;
        }// end of if cycle

        keyUnica = addKeyCompany(entityBean, keyCode);
        return keyUnica;
    }// end of method


    /**
     * Se è prevista la company obbligatoria, antepone company.code a quanto sopra (se non è vuoto)
     * Se manca la company obbligatoria, non registra
     * <p>
     * Se è prevista la company facoltativa, antepone company.code a quanto sopra (se non è vuoto)
     * Se manca la company facoltativa, registra con idKey regolata come sopra
     * <p>
     * Per codifiche diverse, sovrascrivere il metodo
     *
     * @param entityBean da regolare
     *
     * @return chiave univoca da usare come idKey nel DB mongo
     */
    public String addKeyCompany(AEntity entityBean, String keyCode) {
        String keyUnica = "";
        Company company = null;
        String companyCode = "";

        if (usaCompany()) {
            if (entityBean instanceof ACEntity) {
                company = ((ACEntity) entityBean).company;
                if (company != null) {
                    companyCode = company.getCode();
                }// end of if cycle
            }// end of if cycle

            if (text.isEmpty(companyCode)) {
                companyCode = getCompanyCode();
            }// end of if cycle

            if (text.isValid(companyCode)) {
                keyUnica = companyCode + text.primaMaiuscola(keyCode);
            } else {
                if (annotation.getCompanyRequired(entityClass) == EACompanyRequired.obbligatoria) {
                    keyUnica = null;
                } else {
                    keyUnica = keyCode;
                }// end of if/else cycle
            }// end of if/else cycle
        } else {
            keyUnica = keyCode;
        }// end of if/else cycle

        return keyUnica;
    }// end of method


    /**
     * Property unica (se esiste).
     */
    public String getPropertyUnica(AEntity entityBean) {
        return "";
    }// end of method


    public String getCompanyCode(AEntity entityBean) {
        String code = "";
        Company company = getCompany(entityBean);

        if (company != null) {
//            code = company.getCode();
        }// end of if cycle

        return code;
    }// end of method


    public Company getCompany(AEntity entityBean) {
        Company company = null;

        if (entityBean instanceof ACEntity) {
//            company = ((ACEntity) entityBean).getCompany();
        } else {
            company = getCompany();
        }// end of if/else cycle

        return company;
    }// end of method


//    /**
//     * Property unica (se esiste) <br>
//     * <p>
//     * Se esiste la property 'code', utilizza il valore di questa property <br>
//     * Altrimenti, se esiste la property 'descrizione', utilizza il valore di questa property <br>
//     * Altrimenti, se esiste un metodo sovrascritto nella sottoclasse concreta, utilizza quello <br>
//     * Altrimenti, restituisce un valore vuoto <br>
//     */
//    public String getPropertyUnica(AEntity entityBean) {
//        String propertyUnica = "";
//
//        if (reflection.isEsiste(entityClass, FIELD_NAME_CODE)) {
//            propertyUnica = (String) reflection.getPropertyValue(entityBean, FIELD_NAME_CODE);
//        } else {
//            if (reflection.isEsiste(entityClass, FIELD_NAME_DESCRIZIONE)) {
//                propertyUnica = (String) reflection.getPropertyValue(entityBean, FIELD_NAME_DESCRIZIONE);
//            }// end of if cycle
//        }// end of if/else cycle
//
//        return propertyUnica;
//    }// end of method


    /**
     * Ordine di presentazione (obbligatorio, unico per tutte le eventuali company), <br>
     * Viene calcolato in automatico alla creazione della entity <br>
     * Recupera dal DB il valore massimo pre-esistente della property <br>
     * Incrementa di uno il risultato <br>
     */
    public int getNewOrdine() {
//        int ordine = 0;
//        AEntity entityBean = null;
//        Sort sort;
//        List lista = null;
//        Field field;
//        Object value;
//
//        if (!reflection.isEsiste(entityClass, FIELD_NAME_ORDINE)) {
//            return 0;
//        }// end of if/else cycle
//
//        sort = new Sort(Sort.Direction.DESC, FIELD_NAME_ORDINE);
//        if (usaCompany()) {
//            lista = findAllByCompany( sort);
//        } else {
//            lista = findAll( sort);
//        }// end of if/else cycle
//
//        if (array.isValid(lista)) {
//            entityBean = (AEntity) lista.get(0);
//        }// end of if cycle
//
//        if (entityBean != null) {
//            field = reflection.getField(entityClass, FIELD_NAME_ORDINE);
//            try { // prova ad eseguire il codice
//                value = field.get(entityBean);
//                if (value instanceof Integer) {
//                    ordine = (Integer) value;
//                }// end of if cycle
//            } catch (Exception unErrore) { // intercetta l'errore
//                log.error(unErrore.toString());
//            }// fine del blocco try-catch
//        }// end of if cycle
//
//        return ordine + 1;
//
        return mongo.getNewOrdine(entityClass);
    }// end of method


    /**
     * Instances of the current company <br>
     * Lista ordinata <br>
     * Può essere sovrascritta nella sottoclasse <br>
     * Adatta SOLO per collections non troppo lunghe <br>
     * Per colelctions con centinaia o migliaia di entities, usare una chiamata nella repository specifica <br>
     *
     * @return lista ordinata delle entities della company corrente
     */
    private List<? extends AEntity> findAllByCompany(Sort sort) {
        List<AEntity> listByCompany = null;
        List<? extends AEntity> listAllEntities = null;
        Company company = null;
        ACEntity companyEntity;
        String companyCode;

//        if (login != null) {
//            company = login.getCompany();
//        }// end of if cycle

        if (company != null) {
//            companyCode = company.getCode();
            listAllEntities = findAll(sort);
            if (array.isValid(listAllEntities)) {
                listByCompany = new ArrayList<>();
                for (AEntity entity : listAllEntities) {
                    if (entity instanceof ACEntity) {
                        companyEntity = (ACEntity) entity;
//                        if (companyEntity.getCompany().getCode().equals(companyCode)) {
//                            listByCompany.add(entity);
//                        }// end of if cycle
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle
        }// end of if cycle

        return listByCompany;
    }// end of method


//    public void logNewBean(AEntity modifiedBean) {
//        String note;
//        String clazz = text.primaMaiuscola(modifiedBean.getClass().getSimpleName());
//
//        note = "";
//        note += clazz;
//        note += ": ";
//        note += modifiedBean;
//
//        if (AlgosApp.SETUP_TIME) {
//            logger.logSetup(modifiedBean, note);
//        } else {
//            logger.logNew(modifiedBean, note);
//        }// end of if/else cycle
//    }// end of method


//    public void logDifferences(Map<String, String> mappa, AEntity modifiedBean) {
//        String note;
//        String aCapo = "\n";
//        String clazz = text.primaMaiuscola(modifiedBean.getClass().getSimpleName());
//
//        for (String publicFieldName : mappa.keySet()) {
//            note = "";
//            note += clazz;
//            note += ": ";
//            note += modifiedBean;
//            note += aCapo;
//            note += "Property: ";
//            note += text.primaMaiuscola(publicFieldName);
//            note += aCapo;
//            note += mappa.get(publicFieldName);
//
//            if (AlgosApp.SETUP_TIME) {
//                logger.logSetup(modifiedBean, note);
//            } else {
//                logger.logEdit(modifiedBean, note);
//            }// end of if/else cycle
//        }// end of for cycle
//    }// end of method


//    protected Map<String, String> chekDifferences(AEntity oldBean, AEntity modifiedBean) {
//        return chekDifferences(oldBean, modifiedBean, (EAPrefType) null);
//    }// end of method


//    protected Map<String, String> chekDifferences(AEntity oldBean, AEntity modifiedBean, EAPrefType type) {
//        Map<String, String> mappa = new LinkedHashMap();
//        List<String> listaNomi = reflection.getAllFieldsNameNoCrono(oldBean.getClass());
//        Object oldValue;
//        Object newValue;
//        String descrizione = "";
//
//        for (String publicFieldName : listaNomi) {
//            oldValue = reflection.getPropertyValue(oldBean, publicFieldName);
//            newValue = reflection.getPropertyValue(modifiedBean, publicFieldName);
//            descrizione = text.getModifiche(oldValue, newValue, type);
//            if (text.isValid(descrizione)) {
//                mappa.put(publicFieldName, descrizione);
//            }// end of if cycle
//        }// end of for cycle
//
//        return mappa;
//    }// end of method


//    /**
//     * Controlla che la entity estenda ACompanyEntity
//     * Se manca la company, cerca di usare quella del login (se esiste)
//     * Se la campany manca ancora, lancia l'eccezione
//     * //     * Controlla che la gestione della chiave unica sia soddisfatta
//     *
//     * @param entity da salvare
//     */
//    @Deprecated
//    private boolean checkCompany(AEntity entity, boolean usaCodeCompanyUnico) throws Exception {
//        ACEntity companyEntity;
//        Company company2;
//        String codeCompanyUnico;
//
//        if (entity instanceof ACEntity) {
//            companyEntity = (ACEntity) entity;
//            company2 = companyEntity.getCompany();
//        } else {
//            throw new NotCompanyEntityException(entityClass);
//        }// end of if/else cycle
//
//        if (company2 == null) {
////            company2 = LibSession.getCompany();@todo rimettere
////            companyEntity.setCompany(company2);
//        }// end of if cycle
//
//        if (companyEntity.getCompany() == null) {
//            throw new NullCompanyException();
//        }// end of if cycle
//
//        return true;
//    }// end of method


    /**
     * Deletes a given entity.
     *
     * @param entityBean must not be null
     *
     * @return true, se la entity è stata effettivamente cancellata
     *
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    public boolean delete(AEntity entityBean) {
        boolean status = false;
        DeleteResult result;

        result = mongo.delete(entityBean);

        if (result.getDeletedCount() == 1) {
            status = true;
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Delete a list of entities.
     *
     * @param listaEntities di elementi da cancellare
     * @param clazz         della collezione
     *
     * @return numero di elementi cancellati
     */
    public int delete(List<? extends AEntity> listaEntities, Class<? extends AEntity> clazz) {
        List<String> listaId = new ArrayList<String>();

        for (AEntity entity : listaEntities) {
            listaId.add(entity.id);
        }// end of for cycle

        return deleteBulk(listaId, clazz);
    }// end of method


    /**
     * Delete a list of entities.
     *
     * @param listaId di ObjectId da cancellare
     * @param clazz   della collezione
     *
     * @return numero di elementi cancellati
     */
    public int deleteBulk(List<String> listaId, Class<? extends AEntity> clazz) {
        int cancellati = 0;
        DeleteResult result = mongo.deleteBulk(listaId, clazz);

        if (result != null) {
            cancellati = (int) result.getDeletedCount();
        }// end of if cycle

        return cancellati;
    }// end of method


    /**
     * Deletes all entities of the collection.
     */
    @Override
    public boolean deleteAll() {
        mongo.drop(entityClass);
        return count() == 0;
    }// end of method


    //    public void logDeleteBean(AEntity deletedBean) {
//        String note;
//        String clazz = text.primaMaiuscola(deletedBean.getClass().getSimpleName());
//
//        note = "";
//        note += clazz;
//        note += ": ";
//        note += deletedBean;
//
//        logger.logDelete(deletedBean, note);
//    }// end of method

//    /**
//     * Deletes all entities of the collection.
//     */
//    @Override
//    public boolean deleteAll() {
//        repository.deleteAll();
//        return repository.count() == 0;
//    }// end of method


    /**
     * Metodo invocato da ABoot (o da una sua sottoclasse) <br>
     * Viene invocato alla creazione del programma e dal bottone Reset della lista (solo per il developer) <br>
     * Creazione di una collezione - Solo se non ci sono records
     */
    @Override
    public void loadData() {
        int numRec = this.count();
        String collectionName = annotation.getCollectionName(entityClass);

        if (numRec == 0) {
            numRec = reset();
            log.warn("Algos - Data. La collezione " + collectionName + " è stata creata: " + numRec + " schede");
        } else {
            log.info("Algos - Data. La collezione " + collectionName + " è già presente: " + numRec + " schede");
        }// end of if/else cycle

    }// end of method


    /**
     * Creazione di alcuni dati demo iniziali <br>
     * Viene invocato alla creazione del programma e dal bottone Reset della lista (solo per il developer) <br>
     * La collezione viene svuotata <br>
     * I dati possono essere presi da una Enumeration o creati direttamemte <br>
     * Deve essere sovrascritto - Invocare PRIMA il metodo della superclasse
     *
     * @return numero di elementi creato
     */
    @Override
    public int reset() {
        this.deleteAll();
        return 0;
    }// end of method


    /**
     * Importazione di dati <br>
     * Deve essere sovrascritto - Invocare PRIMA il metodo della superclasse
     *
     * @return true se sono stati importati correttamente
     */
    public boolean importa() {
        this.deleteAll();
        return false;
    }// end of method


    /**
     * Importazione di dati <br>
     * Deve essere sovrascritto - Invocare PRIMA il metodo della superclasse
     *
     * @param company di riferimento
     *
     * @return true se sono stati importati correttamente
     */
    @Override
    public boolean importa(Company company) {
        this.deleteAll();
        return false;
    }// end of method


    /**
     * Recupera il context della session <br>
     * Controlla che la session sia attiva <br>
     *
     * @return context della sessione
     */
    public AContext getContext() {
        AContext context = null;
        UI ui;
        VaadinSession vaadSession = null;

        ui = UI.getCurrent();
        if (ui != null) {
            vaadSession = ui.getSession();
        }// end of if cycle

        if (vaadSession != null) {
            context = (AContext) vaadSession.getAttribute(KEY_CONTEXT);
        }// end of if cycle

        return context;
    }// end of method


    /**
     * Recupera il login della session corrente <br>
     * Controlla che la session sia attiva <br>
     *
     * @return context della sessione
     */
    public ALogin getLogin() {
        ALogin login = null;
        AContext context = getContext();

        if (context != null) {
            login = context.getLogin();
        }// end of if cycle

        return login;
    }// end of method


    /**
     * Recupera la company della session corrente <br>
     * Controlla che la session sia attiva <br>
     *
     * @return context della sessione
     */
    public Company getCompany() {
        Company company = null;
        AContext context = getContext();

        if (context != null) {
            company = context.getCompany();
        }// end of if cycle

        return company;
    }// end of method


    /**
     * Recupera la sigla della company della session corrente (se esiste) <br>
     * Controlla che la session sia attiva <br>
     *
     * @return context della sessione
     */
    public String getCompanyCode() {
        String code = "";
        Company company = getCompany();

        if (company != null) {
            code = company.getCode();
        }// end of if cycle

        return code;
    }// end of method


    /**
     * Casting da una superclasse ad una sottoclasse <br>
     * Viene creata una nuova istanza VUOTA della sottoclasse <br>
     * Tutti i valori delle property della superclasse vengono ricopiati nella sottoclasse <br>
     * Le rimanenti property della sottoclasse rimangono vuote e verranno regolate successivamente <br>
     */
    public AEntity cast(AEntity entitySopra, AEntity entitySotto) {
        Object value;
        Field fieldSotto;
        List<Field> lista = reflection.getAllFields(entitySopra.getClass());

        try { // prova ad eseguire il codice
            for (Field fieldSopra : lista) {
                value = reflection.getPropertyValue(entitySopra, fieldSopra.getName());
                fieldSotto = reflection.getField(entitySotto.getClass(), fieldSopra.getName());
                fieldSotto.set(entitySotto, value);
            }// end of for cycle
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return entitySotto;
    }// end of method

}// end of class
