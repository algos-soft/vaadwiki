package it.algos.vaadflow14.backend.service;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: sab, 30-mag-2020
 * Time: 22:32
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AClassService extends AAbstractService {


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
     * Istanza della sottoclasse xxxLogic associata al nome delle Entity inviata  <br>
     *
     * @param entityClazzCanonicalName the canonical name of entity class
     *
     * @return istanza di xxxLogic associata alla Entity
     */
    public AILogicOld getLogicFromEntityName(String entityClazzCanonicalName) {
        AILogicOld entityLogic = null;
        String logicClazzCanonicalName;
        AEntity entityBean;

        if (text.isValid(entityClazzCanonicalName)) {
            logicClazzCanonicalName = entityClazzCanonicalName + SUFFIX_LOGIC_LIST;
            try {
                entityLogic = (AILogicOld) appContext.getBean(Class.forName(logicClazzCanonicalName));
            } catch (Exception unErrore) {
                try {
                    entityBean = (AEntity) appContext.getBean(Class.forName(entityClazzCanonicalName));
                    entityLogic = appContext.getBean(EntityLogicOld.class, entityBean.getClass());
                } catch (Exception unErrore2) {
                    logger.error(unErrore2.getMessage(), this.getClass(), "getLogicFromEntityName");
                }
            }
        }
        return entityLogic;
    }

    /**
     * Controlla che esiste una classe xxxLogicList associata alla Entity inviata  <br>
     *
     * @param dovrebbeEssereUnaEntityClazz the entity class
     *
     * @return classe xxxLogicList associata alla Entity
     */
    public boolean isLogicListClassFromEntityClazz(final Class dovrebbeEssereUnaEntityClazz) {
        return getLogicListClassFromEntityClazz(dovrebbeEssereUnaEntityClazz) != null;
    }

    /**
     * Classe xxxLogicList associata alla Entity inviata  <br>
     *
     * @param dovrebbeEssereUnaEntityClazz the entity class
     *
     * @return classe xxxLogicList associata alla Entity
     */
    public Class getLogicListClassFromEntityClazz(final Class dovrebbeEssereUnaEntityClazz) {
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
                        entityLogic = (AILogic) appContext.getBean(EntityLogicOld.class, entityClazz, operationForm);
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
     *
     */
    public Class getClazzFromName(String canonicalName) {
        Class clazz = null;

        if (canonicalName.endsWith(JAVA_SUFFIX)) {
            canonicalName = text.levaCoda(canonicalName, JAVA_SUFFIX);
        }

        try {
            clazz = Class.forName(canonicalName);
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "getClazzFromName");
        }

        return clazz;
    }

}
