package it.algos.vaadflow14.backend.service;

import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.lang.reflect.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: sab, 30-mag-2020
 * Time: 22:32
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ClassService extends AbstractService {


    /**
     * Istanza della sottoclasse xxxService (singleton) associata alla entity <br>
     *
     * @param entityClazz di riferimento
     *
     * @return istanza di xxxService associata alla Entity
     */
    public AIService getServiceFromEntityClazz(Class<? extends AEntity> entityClazz) {
        return getServiceFromEntityName(entityClazz.getCanonicalName());
    }


    /**
     * Istanza della sottoclasse xxxService (singleton) associata alla entity <br>
     *
     * @param entityClazzCanonicalName the canonical name of entity class
     *
     * @return istanza di xxxService associata alla Entity
     */
    public AIService getServiceFromEntityName(String entityClazzCanonicalName) {
        AIService entityService = null;
        String serviceClazzCanonicalName;
        AEntity entityBean;

        if (text.isValid(entityClazzCanonicalName)) {
            serviceClazzCanonicalName = text.levaCoda(entityClazzCanonicalName, SUFFIX_ENTITY) + SUFFIX_SERVICE;
            try {
                entityService = (AIService) appContext.getBean(Class.forName(serviceClazzCanonicalName));
            } catch (Exception unErrore) {
                try {
                    entityBean = (AEntity) appContext.getBean(Class.forName(entityClazzCanonicalName));
                    entityService = appContext.getBean(EntityService.class, entityBean.getClass());
                } catch (Exception unErrore2) {
                    logger.error(unErrore2.getMessage(), this.getClass(), "getServiceFromEntityName");
                }
            }
        }
        return entityService;
    }


    /**
     * Controlla che esiste una classe xxxLogicList associata alla Entity inviata  <br>
     *
     * @param dovrebbeEssereUnaEntityClazz the entity class
     *
     * @return classe xxxLogicList associata alla Entity
     */
    public boolean isLogicListClassFromEntityClazz(final Class dovrebbeEssereUnaEntityClazz) throws AlgosException {
        return getLogicListClassFromEntityClazz(dovrebbeEssereUnaEntityClazz) != null;
    }

    /**
     * Classe xxxLogicList associata alla Entity inviata  <br>
     *
     * @param dovrebbeEssereUnaEntityClazz the entity class
     *
     * @return classe xxxLogicList associata alla Entity
     */
    public Class getLogicListClassFromEntityClazz(final Class dovrebbeEssereUnaEntityClazz) throws AlgosException {
        Class listClazz = null;
        String canonicalNameEntity;
        String canonicalNameLogicList;
        String message;
        String packageName;
        String simpleNameEntity;
        String simpleNameLogicList = VUOTA;

        if (dovrebbeEssereUnaEntityClazz == null) {
            message = String.format("Manca la EntityClazz");
            logger.error(message, this.getClass(), "getLogicListClassFromEntityClazz");
            return null;
        }

        canonicalNameEntity = dovrebbeEssereUnaEntityClazz.getCanonicalName();
        simpleNameEntity = fileService.estraeClasseFinale(canonicalNameEntity);
        packageName = text.levaCoda(simpleNameEntity, SUFFIX_ENTITY).toLowerCase();

        if (!annotation.isEntityClass(dovrebbeEssereUnaEntityClazz)) {
            message = String.format("La clazz ricevuta %s NON è una EntityClazz", simpleNameEntity);
            logger.info(message, this.getClass(), "getLogicListClassFromEntityClazz");
            return null;
        }

        //--provo a creare la classe specifica xxxLogicList (classe, non istanza)
        try {
            canonicalNameLogicList = text.levaCoda(canonicalNameEntity, SUFFIX_ENTITY) + SUFFIX_LOGIC_LIST;
            simpleNameLogicList = fileService.estraeClasseFinale(canonicalNameLogicList);

            listClazz = Class.forName(canonicalNameLogicList);
        } catch (Exception unErrore) {
            message = String.format("Nel package %s non esiste la classe %s", packageName, simpleNameLogicList);
            logger.info(message, this.getClass(), "getLogicListClassFromEntityClazz");
        }

        return listClazz;
    }


    /**
     * Controlla che esiste una classe xxxLogicForm associata alla Entity inviata  <br>
     *
     * @param dovrebbeEssereUnaEntityClazz the entity class
     *
     * @return classe xxxLogicForm associata alla Entity
     */
    public boolean isLogicFormClassFromEntityClazz(final Class dovrebbeEssereUnaEntityClazz) {
        return getLogicFormClassFromEntityClazz(dovrebbeEssereUnaEntityClazz) != null;
    }

    /**
     * Classe xxxLogicForm associata alla Entity inviata  <br>
     *
     * @param dovrebbeEssereUnaEntityClazz the entity class
     *
     * @return classe xxxLogicForm associata alla Entity
     */
    public Class getLogicFormClassFromEntityClazz(final Class dovrebbeEssereUnaEntityClazz) {
        return getLogicFormClassFromEntityClazz(dovrebbeEssereUnaEntityClazz, false);
    }

    /**
     * Classe xxxLogicForm associata alla Entity inviata  <br>
     *
     * @param dovrebbeEssereUnaEntityClazz the entity class
     * @param printLog                     flag per mostrare l'eccezione (se non trova la classe specifica)
     *
     * @return classe xxxLogicForm associata alla Entity
     */
    public Class getLogicFormClassFromEntityClazz(final Class dovrebbeEssereUnaEntityClazz, boolean printLog) {
        Class listClazz = null;
        String canonicalNameEntity;
        String canonicalNameLogicForm;
        String message;
        String packageName;
        String simpleNameEntity;
        String simpleNameLogicForm = VUOTA;

        if (dovrebbeEssereUnaEntityClazz == null) {
            message = String.format("Manca la EntityClazz");
            logger.error(message, this.getClass(), "getLogicFormClassFromEntityClazz");
            return null;
        }

        canonicalNameEntity = dovrebbeEssereUnaEntityClazz.getCanonicalName();
        simpleNameEntity = fileService.estraeClasseFinale(canonicalNameEntity);
        packageName = text.levaCoda(simpleNameEntity, SUFFIX_ENTITY).toLowerCase();

        if (!annotation.isEntityClass(dovrebbeEssereUnaEntityClazz)) {
            message = String.format("La clazz ricevuta %s NON è una EntityClazz", simpleNameEntity);
            logger.info(message, this.getClass(), "getLogicFormClassFromEntityClazz");
            return null;
        }

        //--provo a creare la classe specifica xxxLogicForm (classe, non istanza)
        try {
            canonicalNameLogicForm = text.levaCoda(canonicalNameEntity, SUFFIX_ENTITY) + SUFFIX_LOGIC_FORM;
            simpleNameLogicForm = fileService.estraeClasseFinale(canonicalNameLogicForm);

            listClazz = Class.forName(canonicalNameLogicForm);
        } catch (Exception unErrore) {
            if (printLog) {
                message = String.format("Nel package %s non esiste la classe %s", packageName, simpleNameLogicForm);
                logger.info(message, this.getClass(), "getLogicFormClassFromEntityClazz");
            }
        }

        return listClazz;
    }

    //    /**
    //     * Istanza della sottoclasse xxxLogic associata alla Entity inviata  <br>
    //     *
    //     * @param entityClazz the entity class
    //     *
    //     * @return istanza de xxxLogic associata alla Entity
    //     */
    //    public AILogic getLogicListFromEntityClazz(Class<? extends AEntity> entityClazz) {
    //        return getLogicListFromEntityClazz(entityClazz, null, AEOperation.listNoForm);
    //    }

    /**
     * Istanza della sottoclasse xxxLogic associata alla Entity inviata  <br>
     *
     * @param entityClazz the entity class
     *
     * @return istanza de xxxLogic associata alla Entity
     */
    @Deprecated
    public AILogic getLogicListFromEntityClazz(Class<? extends AEntity> entityClazz, AIService entityService) {
        return getLogicListFromEntityClazz(entityClazz, entityService, AEOperation.listNoForm);
    }


    /**
     * Istanza della sottoclasse xxxLogicList associata alla Entity inviata  <br>
     *
     * @param entityClazz   the entity class
     * @param operationForm supported by dialog
     *
     * @return istanza de xxxLogic associata alla Entity
     */
    @Deprecated
    public AILogic getLogicListFromEntityClazz(Class<? extends AEntity> entityClazz, AIService entityService, AEOperation operationForm) {
        AILogic entityLogic = null;
        String canonicalName;

        if (entityClazz != null) {
            canonicalName = text.levaCoda(entityClazz.getCanonicalName(), SUFFIX_ENTITY) + SUFFIX_LOGIC_LIST;
            try {
                entityLogic = (AILogic) appContext.getBean(Class.forName(canonicalName), entityService, operationForm);
            } catch (Exception unErrore) {
                try {
                    entityLogic = (AILogic) appContext.getBean(Class.forName(canonicalName), entityService);
                } catch (Exception unErrore2) {
                    try {
                        entityLogic = (AILogic) appContext.getBean(LogicList.class, entityClazz, operationForm);
                    } catch (Exception unErrore3) {
                        logger.error("Non sono riuscito a creare la entityLogic", this.getClass(), "getLogicFromEntity");
                    }
                }
            }
        }
        else {
            logger.error("Manca la entityClazz", this.getClass(), "getLogicFromEntity");
            return null;
        }
        return entityLogic;
    }


    /**
     * Istanza di PreferenzaLogic associata alla Entity inviata  <br>
     *
     * @return istanza di PreferenzaLogic associata alla Entity
     */
    public PreferenzaService getPreferenzaLogic() {
        return (PreferenzaService) getServiceFromEntityClazz(Preferenza.class);
    }

    /**
     *
     */
    public String getRouteNameForm(final Class<? extends AEntity> entityClazz) {
        String routeName = ROUTE_NAME_GENERIC_FORM;
        String entityName;

        if (isLogicFormClassFromEntityClazz(entityClazz)) {
            entityName = entityClazz.getSimpleName();
            entityName = text.primaMinuscola(entityName);
            routeName = entityName + SUFFIX_FORM;
        }

        return routeName;
    }

    /**
     * @param menuClazz inserito da FlowBoot.fixMenuRoutes() e sue sottoclassi
     */
    public String getRouteFormName2(Class<? extends AEntity> entityClazz) {
        //        String packageName;
        //        String message;
        String routeName = VUOTA;
        String simpleName = VUOTA;
        String canonicalName;
        Class listClazz = null;

        //--se è una entity, cerca la classe specifica xxxLogicList altrimenti usa GenericLogicList
        if (annotation.isEntityClass(entityClazz)) {
            canonicalName = entityClazz.getCanonicalName();
            //            packageName = fileService.estraeClasseFinale(canonicalName);
            //            packageName = text.levaCoda(packageName, SUFFIX_ENTITY).toLowerCase();
            canonicalName = text.levaCoda(canonicalName, SUFFIX_ENTITY) + SUFFIX_LOGIC_FORM;

            //--provo a creare la classe specifica xxxLogicList
            try {
                listClazz = Class.forName(canonicalName);
            } catch (Exception unErrore) {
            }

            //--controllo che la classe specifica xxxLogicList esista e che contenga @Route
            if (listClazz != null) {
                simpleName = listClazz.getSimpleName();
                routeName = text.levaCoda(simpleName, SUFFIX_LOGIC_FORM) + SUFFIX_FORM;
                routeName = text.primaMinuscola(routeName);
            }
        }

        return routeName;
    }

    /**
     * Recupera la clazz dal nome canonico <br>
     * Il canonicalName inizia da ...it/algos/... <br>
     * Il canonicalName termina SENZA JAVA_SUFFIX <br>
     *
     * @param canonicalName della classe relativo al path parziale di esecuzione
     *
     * @return classe individuata
     */
    public Class getClazzFromCanonicalName(String canonicalName) throws AlgosException {
        Class clazz;
        String message;

        if (text.isEmpty(canonicalName)) {
            throw AlgosException.stack("Manca il canonicalName in ingresso", getClass(), "getClazzFromCanonicalName");
        }

        if (canonicalName.endsWith(JAVA_SUFFIX)) {
            canonicalName = text.levaCoda(canonicalName, JAVA_SUFFIX);
        }

        try {
            clazz = Class.forName(canonicalName);
        } catch (Exception unErrore) {
            message = String.format("Non esiste la classe [%s] nella directory package", canonicalName);
            throw AlgosException.stack(unErrore, message, getClass(), "getClazzFromCanonicalName");
        }

        return clazz;
    }


    /**
     * Recupera la clazz dal nome Java nel package <br>
     * Il simpleName termina SENZA JAVA_SUFFIX <br>
     *
     * @param simpleName della classe
     *
     * @return classe individuata
     */
    public Class getClazzFromSimpleName(String simpleName) throws AlgosException {
        String canonicalName;
        String message;

        if (simpleName == null) {
            throw AlgosException.stack("Il simpleName in ingresso è nullo", getClass(), "getClazzFromSimpleName");
        }

        if (text.isEmpty(simpleName)) {
            throw AlgosException.stack("Il simpleName in ingresso è vuoto", getClass(), "getClazzFromSimpleName");
        }

        if (simpleName.endsWith(JAVA_SUFFIX)) {
            simpleName = text.levaCoda(simpleName, JAVA_SUFFIX);
        }

        simpleName = text.primaMaiuscola(simpleName);
        canonicalName = fileService.getCanonicalName(simpleName);

        if (text.isEmpty(canonicalName)) {
            message = String.format("Non esiste la classe [%s] nella directory package", simpleName);
            throw AlgosException.stack(message, getClass(), "getClazzFromSimpleName");
        }

        return getClazzFromCanonicalName(canonicalName);
    }

    /**
     * Esistenza della clazz dal nome Java nel package <br>
     * Il simpleName termina SENZA JAVA_SUFFIX <br>
     *
     * @param simpleName della classe
     *
     * @return classe individuata
     */
    public boolean isEsiste(String simpleName) throws AlgosException {
        return getClazzFromSimpleName(simpleName) != null;
    }

    /**
     * Recupera il canonicalName dal path completo <br>
     * Il path completo inizia da /Users/gac/Documents/... <br>
     *
     * @param pathCompleto della classe
     *
     * @return canonicalName
     */
    public String getNameFromPath(String pathCompleto) {
        String canonicalName = VUOTA;
        String tagDirectory = "it/algos";

        canonicalName = fileService.findPathCanonical(pathCompleto, tagDirectory);
        if (canonicalName.endsWith(JAVA_SUFFIX)) {
            canonicalName = text.levaCoda(canonicalName, JAVA_SUFFIX);
        }

        return canonicalName;
    }

    /**
     * Recupera la clazz dal path completo <br>
     * Il path completo inizia da /Users/gac/Documents/... <br>
     *
     * @param pathCompleto della classe
     *
     * @return classe individuata
     */
    public Class getClazzFromPath(final String pathCompleto) throws AlgosException {
        String message;
        if (text.isEmpty(pathCompleto)) {
            throw AlgosException.stack("Manca il pathCompleto in ingresso", getClass(), "getClazzFromPath");
        }

        String canonicalName = getNameFromPath(pathCompleto);
        if (text.isEmpty(canonicalName)) {
            message = String.format("Il path [%s] non esiste", pathCompleto);
            throw AlgosException.stack(message, getClass(), "getClazzFromPath");
        }

        canonicalName = canonicalName.replaceAll(FlowCost.SLASH, FlowCost.PUNTO);
        return getClazzFromCanonicalName(canonicalName);
    }


    /**
     * Check if the class is an entityBean class.
     * 1) Controlla che il parametro in ingresso non sia vuoto <br>
     *
     * @param canonicalName of the class to be checked if is of type AEntity
     *
     * @return true if the class is of type AEntity
     */
    public boolean isEntity(final String canonicalName) {
        Class clazz = null;

        try {
            clazz = Class.forName(canonicalName);
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "isEntityClass");
        }

        if (clazz != null) {
            return isEntity(clazz);
        }
        else {
            return false;
        }
    }


    /**
     * Controlla se la classe è una AEntity. <br>
     *
     * @param genericClazz to be checked if is of type AEntity
     *
     * @return the status
     */
    public boolean isEntity(final Class genericClazz) {
        return genericClazz != null && AEntity.class.isAssignableFrom(genericClazz);
    }


    /**
     * Check if the class is an entityBean class.
     * 1) Controlla che il parametro in ingresso non sia vuoto <br>
     *
     * @param canonicalName of the class to be checked if is of type AREntity
     *
     * @return true if the class is of type AREntity
     */
    public boolean isResetEntity(final String canonicalName) {
        Class clazz = null;

        try {
            clazz = Class.forName(canonicalName);
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "isEntityClass");
        }

        if (clazz != null) {
            return isResetEntity(clazz);
        }
        else {
            return false;
        }
    }


    /**
     * Controlla se la classe è una AREntity. <br>
     *
     * @param genericClazz to be checked if is of type AREntity
     *
     * @return the status
     */
    public boolean isResetEntity(final Class genericClazz) {
        return genericClazz != null && AREntity.class.isAssignableFrom(genericClazz);
    }

    public String getProjectName() {
        String projectName = VUOTA;
        String pathCurrent = System.getProperty("user.dir") + SLASH;
        projectName = fileService.estraeDirectoryFinale(pathCurrent);

        if (projectName.endsWith(SLASH)) {
            projectName = text.levaCoda(projectName, SLASH);
        }

        return projectName;
    }

    /**
     * Crea una nuova entity (vuota) dalla classe <br>
     *
     * @param entityClazz the entity class
     *
     * @return new entityBean
     */
    public AEntity getEntityFromClazz(final Class entityClazz) throws AlgosException {
        AEntity entityBean;
        Constructor constructor;
        String message;

        if (entityClazz == null) {
            throw AlgosException.stack("Manca la entityClazz in ingresso", getClass(), "getEntityFromClazz");
        }

        if (!AEntity.class.isAssignableFrom(entityClazz)) {
            message = String.format("La classe %s non è una sottoclasse di AEntity", entityClazz.getSimpleName());
            throw AlgosException.stack(message, getClass(), "getEntityFromClazz");
        }

        try {
            constructor = entityClazz.getDeclaredConstructor();
            entityBean = (AEntity) constructor.newInstance(null);
        } catch (Exception unErrore) {
            message = String.format("Non sono riuscito a costruire una entityBean di classe %s", entityClazz.getSimpleName());
            throw AlgosException.stack(unErrore, message, getClass(), "getEntityFromClazz");
        }
        return entityBean;
    }


    /**
     * Recupera la classe AEntity associata a questa classe del package <br>
     *
     * @param genericClazz to be checked if is of type AEntity
     *
     * @return classe individuata
     */
    public Class getEntityClazzFromClazz(final Class genericClazz) throws AlgosException {
        Class entityClazz = null;
        String message = VUOTA;
        String clazzName;

        if (genericClazz == null) {
            throw AlgosException.stack("Manca la genericClazz in ingresso", getClass(), "getEntityClazzFromClazz");
        }

        if (annotation.isEntityClass(genericClazz)) {
            return genericClazz;
        }

        clazzName = genericClazz.getSimpleName();
        if (clazzName.endsWith(SUFFIX_LOGIC_LIST) || clazzName.endsWith(SUFFIX_LOGIC_FORM) || clazzName.endsWith(SUFFIX_SERVICE)) {
            clazzName = text.levaCoda(clazzName, SUFFIX_LOGIC_LIST);
            clazzName = text.levaCoda(clazzName, SUFFIX_LOGIC_FORM);
            clazzName = text.levaCoda(clazzName, SUFFIX_SERVICE);
        }
        if (text.isValid(clazzName)) {
            return getClazzFromSimpleName(clazzName);
        }

        message = String.format("Non esiste una AEntity associata alla classe %s", genericClazz.getSimpleName());
        throw AlgosException.stack(message, getClass(), "getEntityClazzFromClazz");
    }


    /**
     * Recupera la classe AEntity associata a questa collection del database mongoDB <br>
     *
     * @param collectionName della collezione su mongoDB <br>
     *
     * @return classe individuata
     */
    public Class getEntityClazzFromCollection(final String collectionName) throws AlgosException {
        if (text.isEmpty(collectionName)) {
            throw AlgosException.stack("Manca il nome della collection in ingresso", getClass(), "getEntityClazzFromCollection");
        }

        if (!mongo.isExistsCollection(collectionName)) {
            throw AlgosException.stack(String.format("Non esiste la collection %s nel database mongoDB", collectionName), this.getClass(), "getEntityClazzFromCollection");
        }

        return getClazzFromSimpleName(collectionName);
    }

}
