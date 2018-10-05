package it.algos.vaadflow.service;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.ui.IAView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 22:11
 * Classe di Libreria
 * NON deve essere astratta, altrimenti Spring non la costruisce
 * Utility per recuperare property e metodi da altre classi
 */
@Slf4j
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AReflectionService {


    /**
     * Private final property
     */
    private static final AReflectionService INSTANCE = new AReflectionService();
    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AArrayService array = AArrayService.getInstance();
    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AAnnotationService annotation = AAnnotationService.getInstance();
    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();


    /**
     * Private constructor to avoid client applications to use constructor
     */
    private AReflectionService() {
    }// end of constructor

    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static AReflectionService getInstance() {
        return INSTANCE;
    }// end of static method


//    /**
//     * Inietta da Spring come 'session'
//     */
//    @Autowired
//    public ALogin login;

    /**
     * Mappa di tutti i valori delle properties di una classe
     *
     * @param entityBean oggetto su cui operare la riflessione
     */
    public Map<String, Object> getPropertyMap(final AEntity entityBean) {
        Map<String, Object> mappa = null;
        List<String> listaNomi = getAllFieldsName(entityBean.getClass());
        Object value;

        if (array.isValid(listaNomi)) {
            mappa = new LinkedHashMap<>();
            for (String publicFieldName : listaNomi) {
                value = getPropertyValue(entityBean, publicFieldName);
                mappa.put(publicFieldName, value);
            }// end of for cycle
        }// end of if cycle

        return mappa;
    }// end of method


    /**
     * Valore della property di una classe
     *
     * @param entityBean oggetto su cui operare la riflessione
     */
    public Object getPropertyValue(final AEntity entityBean, final String publicFieldName) {
        Object value = null;
        Class<?> clazz = entityBean.getClass();
        List<Field> fieldsList = getAllFieldsNoCrono(entityBean.getClass());

        try { // prova ad eseguire il codice
            for (Field field : fieldsList) {
                if (field.getName().equals(publicFieldName)) {
                    field.setAccessible(true);
                    value = field.get(entityBean);
                }// end of if cycle
            }// end of for cycle
        } catch (Exception unErrore) { // intercetta l'errore
            int a = 87;
        }// fine del blocco try-catch

        return value;
    }// end of method


    /**
     * Valore della property statica di una classe
     *
     * @param clazz           classe su cui operare la riflessione
     * @param publicFieldName property statica e pubblica
     */
    @Deprecated
    public Object getPropertyValue(final Class<?> clazz, final String publicFieldName) {
        Object value = null;

        try { // prova ad eseguire il codice
            value = clazz.getDeclaredField(publicFieldName).get(null);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return value;
    }// end of method


    /**
     * Valore della VaadinIcon di una view
     *
     * @param viewClazz classe view su cui operare la riflessione
     */
    public VaadinIcon getIconView(final Class<?> viewClazz) {
        VaadinIcon icon = null;
        String iconNameProperty = "VIEW_ICON";
        Object genericValue = getPropertyValue(viewClazz, iconNameProperty);

        if (genericValue != null && genericValue instanceof VaadinIcon) {
            icon = (VaadinIcon) genericValue;
        }// end of if cycle

        return icon;
    }// end of method


    /**
     * Valore della IronIcon di una view
     *
     * @param viewClazz classe view su cui operare la riflessione
     */
    public String getIronIcon(final Class<?> viewClazz) {
        String icon = null;
        String iconNameProperty = "IRON_ICON";
        Object genericValue = getPropertyValue(viewClazz, iconNameProperty);

        if (genericValue != null && genericValue instanceof String) {
            icon = (String) genericValue;
        }// end of if cycle

        return icon;
    }// end of method


    /**
     * Valore della MENU_NAME di una view
     *
     * @param viewClazz classe view su cui operare la riflessione
     */
    public String getMenuName(final Class<?> viewClazz) {
        String menuName = "";
        String iconNameProperty = "MENU_NAME";
        Object genericValue = getPropertyValue(viewClazz, iconNameProperty);

        if (genericValue != null && genericValue instanceof String) {
            menuName = (String) genericValue;
        }// end of if cycle

        return menuName;
    }// end of method


//    /**
//     * Valore della property statica di una classe
//     *
//     * @param clazz           classe su cui operare la riflessione
//     * @param publicFieldName property statica e pubblica
//     */
//    @Deprecated
//    public Resource getPropertyRes(final Class<?> clazz, final String publicFieldName) {
//        Resource value = null;
//        Object obj = getPropertyValue(clazz, publicFieldName);
//
//        if (obj != null && obj instanceof Resource) {
//            value = (Resource) obj;
//        }// end of if cycle
//
//        return value;
//    }// end of method


    /**
     * Valore della property statica di una classe
     *
     * @param clazz           classe su cui operare la riflessione
     * @param publicFieldName property statica e pubblica
     */
    @Deprecated
    public String getPropertyStr(final Class<?> clazz, final String publicFieldName) {
        String value = "";
        Object obj = getPropertyValue(clazz, publicFieldName);

        if (obj != null && obj instanceof String) {
            value = (String) obj;
        }// end of if cycle

        return value;
    }// end of method


    /**
     * Field dichiarato di una Entity
     *
     * @param entityClazz     classe su cui operare la riflessione
     * @param publicFieldName property statica e pubblica
     */
    public Field getField(final Class<? extends AEntity> entityClazz, final String publicFieldName) {
        Field field = null;
        List<Field> listaFields = getAllFields(entityClazz);

        for (Field fieldTmp : listaFields) {
            if (fieldTmp.getName().equals(publicFieldName)) {
                field = fieldTmp;
                break;
            }// end of if cycle
        }// end of for cycle

        return field;
    }// end of method


    /**
     * Fields dichiarati nella Entity
     * Comprende la entity e tutte le superclassi (fino a ACEntity e AEntity)
     * Esclusa la property: PROPERTY_SERIAL
     * Non ordinati
     *
     * @param entityClazz da cui estrarre i fields
     *
     * @return lista di fields della Entity e di tutte le supeclassi
     */
    public List<Field> getAllFields(Class<? extends AEntity> entityClazz) {
        List<Field> listaFields = new ArrayList<>();
        Class<?> clazz = entityClazz;
        Field[] fieldsArray = null;

        //--recupera tutti i fields della entity e di tutte le superclassi
        while (clazz != Object.class) {
            try { // prova ad eseguire il codice
                fieldsArray = clazz.getDeclaredFields();
                for (Field field : fieldsArray) {
                    if (!field.getName().equals(FlowCost.PROPERTY_SERIAL)) {
                        listaFields.add(field);
                    }// end of if cycle
                }// end of for cycle
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
            clazz = clazz.getSuperclass();
        }// end of while cycle

        return listaFields;
    }// end of method


    /**
     * Fields dichiarati nella View
     * Non ordinati
     *
     * @param viewClazz da cui estrarre i fields
     *
     * @return lista di fields della View
     */
    public List<Field> getAllFieldsView(Class<? extends IAView> viewClazz) {
        List<Field> listaFields = new ArrayList<>();
        Class<?> clazz = viewClazz;
        Field[] fieldsArray = null;

        //--recupera tutti i fields della view
        try { // prova ad eseguire il codice
            fieldsArray = clazz.getDeclaredFields();
            for (Field field : fieldsArray) {
                listaFields.add(field);
            }// end of for cycle
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return listaFields;
    }// end of method


    /**
     * Fields dichiarati nella Entity
     * Comprende la entity e tutte le superclassi (fino a ACEntity e AEntity)
     * Esclusa le properties: PROPERTY_SERIAL, PROPERTY_CREAZIONE, PROPERTY_MODIFICA
     * Non ordinati
     *
     * @param entityClazz da cui estrarre i fields
     *
     * @return lista di fields della Entity e di tutte le supeclassi
     */
    public List<Field> getAllFieldsNoCrono(Class<? extends AEntity> entityClazz) {
        List<Field> listaFields = new ArrayList<>();
        Class<?> clazz = entityClazz;
        Field[] fieldsArray = null;

        //--recupera tutti i fields della entity e di tutte le superclassi
        while (clazz != Object.class) {
            try { // prova ad eseguire il codice
                fieldsArray = clazz.getDeclaredFields();
                for (Field field : fieldsArray) {
                    if (!FlowCost.ESCLUSI_ALL.contains(field.getName())) {
                        listaFields.add(field);
                    }// end of if cycle
                }// end of for cycle
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
            clazz = clazz.getSuperclass();
        }// end of while cycle

        return listaFields;
    }// end of method


    /**
     * Nomi dei fields dichiarati nella Entity
     * Comprende la entity e tutte le superclassi (fino a ACEntity e AEntity)
     * Esclusa la property: PROPERTY_SERIAL
     * Non ordinati
     *
     * @param entityClazz da cui estrarre i fields
     *
     * @return lista di fields della Entity e di tutte le supeclassi
     */
    public List<String> getAllFieldsName(Class<? extends AEntity> entityClazz) {
        List<String> listaNomi = new ArrayList<>();
        Class<?> clazz = entityClazz;
        Field[] fieldsArray = null;

        //--recupera tutti i fields della entity e di tutte le superclassi
        while (clazz != Object.class) {
            try { // prova ad eseguire il codice
                fieldsArray = clazz.getDeclaredFields();
                for (Field field : fieldsArray) {
                    if (!field.getName().equals(FlowCost.PROPERTY_SERIAL)) {
                        listaNomi.add(field.getName());
                    }// end of if cycle
                }// end of for cycle
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
            clazz = clazz.getSuperclass();
        }// end of while cycle

        return listaNomi;
    }// end of method


    /**
     * Nomi dei fields dichiarati nella Entity
     * Comprende la entity e tutte le superclassi (fino a ACEntity e AEntity)
     * Esclusa le properties: PROPERTY_SERIAL, PROPERTY_CREAZIONE, PROPERTY_MODIFICA
     * Non ordinati
     *
     * @param entityClazz da cui estrarre i fields
     *
     * @return lista di fields della Entity e di tutte le supeclassi
     */
    public ArrayList<String> getAllFieldsNameNoCrono(Class<? extends AEntity> entityClazz) {
        ArrayList<String> listaNomi = new ArrayList<>();
        Class<?> clazz = entityClazz;
        Field[] fieldsArray = null;

        //--recupera tutti i fields della entity e di tutte le superclassi
        while (clazz != Object.class) {
            try { // prova ad eseguire il codice
                fieldsArray = clazz.getDeclaredFields();
                for (Field field : fieldsArray) {
                    if (!FlowCost.ESCLUSI_FORM.contains(field.getName())) {
                        listaNomi.add(field.getName());
                    }// end of if cycle
                }// end of for cycle
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
            clazz = clazz.getSuperclass();
        }// end of while cycle

        return listaNomi;
    }// end of method


    /**
     * Fields dichiarati nella Entity, da usare come columns della Grid (List)
     * Se listaNomi è nulla, usa tutti i campi (senza ID, senza note, senza creazione, senza modifica)
     * Comprende la entity e tutte le superclassi (fino a ACEntity e AEntity)
     * Se la company2 è prevista (AlgosApp.USE_MULTI_COMPANY, login.isDeveloper() e entityClazz derivata da ACEntity),
     * la posiziona come prima colonna a sinistra
     *
     * @param entityClazz da cui estrarre i fields
     * @param listaNomi   dei fields da considerare. Tutti, se listaNomi=null
     *
     * @return lista di fields visibili nella Grid
     */
    public List<Field> getListFields(Class<? extends AEntity> entityClazz, List<String> listaNomi) {
        ArrayList<Field> fieldsList = new ArrayList<>();
        Class<?> clazz = entityClazz;
        ArrayList<Field> fieldsTmp = new ArrayList<>();
        Field[] fieldsArray = null;
        Field fieldCompany = null;
        String fieldName;
//        boolean useCompany = displayCompany(entityClazz); //@todo rimettere flag
        boolean useCompany = false;

        //--recupera tutti i fields della entity e di tutte le superclassi
        while (clazz != Object.class) {
            try { // prova ad eseguire il codice
                fieldsArray = clazz.getDeclaredFields();
                for (Field field : fieldsArray) {
                    if (field.getName().equals(FlowCost.PROPERTY_COMPANY)) {
                        fieldCompany = field;
                    } else {
                        fieldsTmp.add(field);
                    }// end of if/else cycle
                }// end of for cycle
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
            clazz = clazz.getSuperclass();
        }// end of while cycle

        //--controlla che i fields siano quelli richiesti
        //--se la lista dei nomi dei fields è nulla, li prende tutti
        if (array.isValid(fieldsTmp)) {
            if (array.isValid(listaNomi)) {
                for (String nome : listaNomi) {
                    for (Field field : fieldsTmp) {
                        fieldName = field.getName();
                        if (text.isValid(fieldName) && fieldName.equals(nome)) {
                            fieldsList.add(field);
                        }// end of if cycle
                    }// end of for cycle
                }// end of for cycle
            } else {
                for (Field field : fieldsTmp) {
                    fieldName = field.getName();
                    if (text.isValid(fieldName) && !FlowCost.ESCLUSI_LIST.contains(fieldName)) {
                        fieldsList.add(field);
                    }// end of if cycle
                }// end of for cycle
            }// end of if/else cycle
        }// end of if cycle


        //--se la entity è di tipo ACEntity, aggiunge (all'inizio) il field di riferimento
        if (useCompany && fieldCompany != null) {
            fieldsList.add(0, fieldCompany);
        }// end of if cycle

        return fieldsList;
    }// end of method


    /**
     * Se esiste il field della Entity
     *
     * @param entityClazz     classe su cui operare la riflessione
     * @param publicFieldName property
     *
     * @return lista di fields della Entity e di tutte le supeclassi
     */
    public boolean isEsiste(Class<? extends AEntity> entityClazz, final String publicFieldName) {
        return getField(entityClazz, publicFieldName) != null;
    }// end of method


//    /**
//     * Fields dichiarati nella Entity, da usare come campi del Form
//     * Se listaNomi è nulla, usa tutti i campi:
//     * user = senza ID, senza note, senza creazione, senza modifica
//     * developer = con ID, con note, con creazione, con modifica
//     * Comprende la entity e tutte le superclassi (fino a ACEntity e AEntity)
//     * Se la company2 è prevista (AlgosApp.USE_MULTI_COMPANY, login.isDeveloper() e entityClazz derivata da ACEntity),
//     * la posiziona come secondo campo in alto, dopo la keyID
//     *
//     * @param entityClazz da cui estrarre i fields
//     * @param listaNomi   dei fields da considerare. Tutti, se listaNomi=null
//     *
//     * @return lista di fields visibili nel Form
//     */
//    public List<Field> getFormFields(Class<? extends AEntity> entityClazz, List<String> listaNomi) {
//        ArrayList<Field> fieldsList = new ArrayList<>();
//        Class<?> clazz = entityClazz;
//        ArrayList<Field> fieldsTmp = new ArrayList<>();
//        Field[] fieldsArray = null;
//        Field fieldKeyId = null;
//        Field fieldCompany = null;
//        String fieldName;
//        boolean isDeveloper = login.isDeveloper();
//        boolean useCompany = displayCompany(entityClazz);
//
//        //--recupera tutti i fields della entity e di tutte le superclassi
//        while (clazz != Object.class) {
//            try { // prova ad eseguire il codice
//                fieldsArray = clazz.getDeclaredFields();
//                for (Field field : fieldsArray) {
//                    if (field.getName().equals(BaseCost.PROPERTY_ID)) {
//                        fieldKeyId = field;
//                    }// end of if cycle
//                    if (field.getName().equals(BaseCost.PROPERTY_COMPANY)) {
//                        fieldCompany = field;
//                    }// end of if cycle
//                    fieldsTmp.add(field);
//                }// end of for cycle
//            } catch (Exception unErrore) { // intercetta l'errore
//                log.error(unErrore.toString());
//            }// fine del blocco try-catch
//            clazz = clazz.getSuperclass();
//        }// end of while cycle
//
//        //--controlla che i fields siano quelli richiesti
//        //--se la lista dei nomi dei fields è nulla, li prende tutti
//        if (array.isValid(fieldsTmp)) {
//            if (array.isValid(listaNomi) || text.isValid(listaNomi) && !isDeveloper) {
//                for (String nome : listaNomi) {
//                    for (Field field : fieldsTmp) {
//                        fieldName = field.getName();
//                        if (text.isValid(fieldName) && fieldName.equals(nome)) {
//                            fieldsList.add(field);
//                        }// end of if cycle
//                    }// end of for cycle
//                }// end of for cycle
//            } else {
//                for (Field field : fieldsTmp) {
//                    fieldName = field.getName();
//                    if (isDeveloper) {
//                        if (text.isValid(fieldName) && !fieldName.equals(BaseCost.PROPERTY_SERIAL)) {
//                            fieldsList.add(field);
//                        }// end of if cycle
//                    } else {
//                        if (text.isValid(fieldName) && !BaseCost.ESCLUSI_FORM.contains(fieldName)) {
//                            fieldsList.add(field);
//                        }// end of if cycle
//                    }// end of if/else cycle
//                }// end of for cycle
//            }// end of if/else cycle
//        }// end of if cycle
//
//        //--se la entity è di tipo ACEntity, aggiunge (all'inizio) il field di riferimento
//        if (useCompany && fieldCompany != null) {
//            if (fieldsList.contains(fieldCompany)) {
//                fieldsList.remove(fieldCompany);
//            }// end of if cycle
//            fieldsList.add(0, fieldCompany);
//        }// end of if cycle
//
//        //--se il flag booleano addKeyID è true, aggiunge (all'inizio) il field keyId
//        if (isDeveloper && fieldKeyId != null) {
//            if (fieldsList.contains(fieldKeyId)) {
//                fieldsList.remove(fieldKeyId);
//            }// end of if cycle
//            fieldsList.add(0, fieldKeyId);
//        }// end of if cycle
//
//        return fieldsList;
//    }// end of method


//    /**
//     * Flag.
//     * Deve essere true il flag useMultiCompany
//     * La Entity deve estendere ACompanyEntity
//     * L'user collegato deve essere developer
//     *
//     * @param entityClazz da cui estrarre i fields
//     */
//    public boolean displayCompany(Class<? extends AEntity> entityClazz) {
//
//        //--Deve essere true il flag useMultiCompany
//        if (!AlgosApp.USE_MULTI_COMPANY) {
//            return false;
//        }// end of if cycle
//
//        //--La Entity deve estendere ACompanyEntity
//        if (!ACEntity.class.isAssignableFrom(entityClazz)) {
//            return false;
//        }// end of if cycle
//
//        //--L'User collegato deve essere developer
//        if (!login.isDeveloper()) {
//            return false;
//        }// end of if cycle
//
//        return true;
//    }// end of method

}// end of service class
