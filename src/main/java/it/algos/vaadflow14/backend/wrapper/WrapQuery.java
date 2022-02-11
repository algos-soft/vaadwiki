package it.algos.vaadflow14.backend.wrapper;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.*;

import java.io.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 11-gen-2022
 * Time: 11:39
 * <p>
 * Wrapper con tutte le informazioni per una query di mongoDB <br>
 * Costruttore obbligatorio con la entityClazz <br>
 * Creata con appContext.getBean(WrapQuery.class, entityClazz) <br>
 * In alternativa coi metodi statici:
 * WrapQuery.crea(final Class entityClazz, final AETypeFilter typeFilter, final String propertyField, final Serializable propertyValue) <br>
 * <p>
 * La query è del type.nulla: -> zero
 * 1) Se manca la entityClazz
 * <p>
 * La query è del type.errata: -> zero
 * 2) Se la entityClazz non è una AEntity
 * <p>
 * La query è del type.incompleta: -> zero
 * 3) Se il propertyField è nullo
 * 4) Se il propertyField è vuoto
 * 5) Se la entityClazz non ha quel propertyField
 * <p>
 * La query è del type.validaSenzaFiltri: -> secondo il filtro
 * 6) Se il propertyValue è nullo
 * 7) Se il propertyValue è vuoto
 * <p>
 * La query è del type.validaConFiltri: -> secondo il filtro
 * 6) Se la entityClazz ha quel propertyField valido
 * 7) Se il propertyValue è un valore valido
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WrapQuery {

    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public static ApplicationContext appContext;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService text;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AnnotationService annotation;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ReflectionService reflection;

    //--property
    public Type type;

    //--obbligatoria
    private Class<? extends AEntity> entityClazz;

    //--property
    //    private AETypeFilter typeFilter;

    //--property
    //    private String propertyField;

    //--property
    //    private Serializable propertyValue;

    /**
     * Filtri per dataProvider <br>
     */
    private Map<String, AFiltro> mappaFiltri;

    //--property
    private Sort sortSpring;

    //--property
    private List<WrapCriteria> listaCriteria;

    /**
     * Costruttore unico con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(WrapQuery.class, entityClazz) <br>
     *
     * @param entityClazz (obbligatorio)  the class of type AEntity
     */
    public WrapQuery(final Class<? extends AEntity> entityClazz) throws AlgosException {
        if (entityClazz == null) {
            throw AlgosException.stack("Manca la entityClazz che è obbligatoria", getClass(), "costruttore");
        }
        if (!AEntity.class.isAssignableFrom(entityClazz)) {
            throw AlgosException.stack(String.format("La entityClazz %s non è una classe di tipo AEntity", entityClazz.getSimpleName()), WrapFiltri.class, "costruttore");
        }

        this.entityClazz = entityClazz;
        this.listaCriteria = new ArrayList<>();
        this.type = Type.validaSenzaFiltri;
    }// end of constructor


    public static WrapQuery crea(final Class<? extends AEntity> entityClazz) throws AlgosException {
        WrapQuery wrapQuery;

        try {
            wrapQuery = appContext.getBean(WrapQuery.class, entityClazz);
        } catch (Exception unErrore) {
            throw unErrore;
        }

        if (entityClazz == null) {
            wrapQuery.type = Type.nulla;
            return wrapQuery;
        }

        if (AEntity.class.isAssignableFrom(entityClazz)) {
            wrapQuery.type = Type.validaSenzaFiltri;
        }
        else {
            wrapQuery.type = Type.errata;
        }

        return wrapQuery;
    }// end of static method


    public static WrapQuery crea(final Class entityClazz, final AETypeFilter typeFilter, final String propertyField, final Serializable propertyValue) throws AlgosException {
        WrapQuery wrapQuery = crea(entityClazz);
        WrapCriteria wrapCriteria = WrapCriteria.crea(typeFilter, propertyField, propertyValue);
        String message;

        if (propertyField == null || propertyField.equals(VUOTA)) {
            wrapQuery.type = Type.incompleta;
            throw AlgosException.stack("Manca la propertyField della query", wrapQuery.getClass(), "crea");
        }

        if (!wrapQuery.reflection.isEsisteFieldOnSuperClass(entityClazz, propertyField)) {
            wrapQuery.type = Type.incompleta;
            message = String.format("La entityClazz %s esiste ma non esiste la property %s", entityClazz.getSimpleName(), propertyField);
            throw AlgosException.stack(message, wrapQuery.getClass(), "crea");
        }

        wrapQuery.type = Type.validaConFiltri;
        if (propertyValue == null || propertyValue.equals(VUOTA)) {
            wrapQuery.type = Type.validaSenzaFiltri;
        }

        wrapQuery.listaCriteria.add(wrapCriteria);
        return wrapQuery;
    }// end of static method


    public static WrapQuery crea(final Class entityClazz, final String propertyField, final Serializable propertyValue) throws AlgosException {
        return crea(entityClazz, AETypeFilter.uguale, propertyField, propertyValue);
    }// end of static method


    public static WrapQuery creaAscendente(final Class<? extends AEntity> entityClazz) throws AlgosException {
        WrapQuery wrapQuery = crea(entityClazz);
        String propertySort = wrapQuery.annotation.getSortProperty(entityClazz);
        wrapQuery.sortSpring = Sort.by(Sort.Direction.ASC, propertySort);

        return wrapQuery;
    }// end of static method

    public static WrapQuery creaAscendente(final Class<? extends AEntity> entityClazz, final String propertySort) throws AlgosException {
        WrapQuery wrapQuery = crea(entityClazz);
        wrapQuery.sortSpring = Sort.by(Sort.Direction.ASC, propertySort);

        return wrapQuery;
    }// end of static method

    public static WrapQuery creaAscendente(final Class<? extends AEntity> entityClazz, final String propertyField, final Serializable propertyValue) throws AlgosException {
        WrapQuery wrapQuery = crea(entityClazz, propertyField, propertyValue);
        String propertySort = wrapQuery.annotation.getSortProperty(entityClazz);
        wrapQuery.sortSpring = Sort.by(Sort.Direction.ASC, propertySort);

        return wrapQuery;
    }// end of static method

    public static WrapQuery creaAscendente(final Class<? extends AEntity> entityClazz, final String propertyField, final Serializable propertyValue, final String propertySort) throws AlgosException {
        WrapQuery wrapQuery = crea(entityClazz, propertyField, propertyValue);
        wrapQuery.sortSpring = Sort.by(Sort.Direction.ASC, propertySort);

        return wrapQuery;
    }// end of static method


    public static WrapQuery creaDiscendente(final Class<? extends AEntity> entityClazz) throws AlgosException {
        WrapQuery wrapQuery = crea(entityClazz);
        String propertySort = wrapQuery.annotation.getSortProperty(entityClazz);
        wrapQuery.sortSpring = Sort.by(Sort.Direction.DESC, propertySort);

        return wrapQuery;
    }// end of static method

    public static WrapQuery creaDiscendente(final Class<? extends AEntity> entityClazz, final String propertySort) throws AlgosException {
        WrapQuery wrapQuery = crea(entityClazz);
        wrapQuery.sortSpring = Sort.by(Sort.Direction.DESC, propertySort);

        return wrapQuery;
    }// end of static method

    public static WrapQuery creaDiscendente(final Class<? extends AEntity> entityClazz, final String propertyField, final Serializable propertyValue) throws AlgosException {
        WrapQuery wrapQuery = crea(entityClazz, propertyField, propertyValue);
        String propertySort = wrapQuery.annotation.getSortProperty(entityClazz);
        wrapQuery.sortSpring = Sort.by(Sort.Direction.DESC, propertySort);

        return wrapQuery;
    }// end of static method

    public static WrapQuery creaDiscendente(final Class<? extends AEntity> entityClazz, final String propertyField, final Serializable propertyValue, final String propertySort) throws AlgosException {
        WrapQuery wrapQuery = crea(entityClazz, propertyField, propertyValue);
        wrapQuery.sortSpring = Sort.by(Sort.Direction.DESC, propertySort);

        return wrapQuery;
    }// end of static method

    public void add(final AETypeFilter typeFilter, final String propertyField, final Serializable propertyValue) throws AlgosException {
        WrapCriteria wrapCriteria = WrapCriteria.crea(typeFilter, propertyField, propertyValue);
        listaCriteria.add(wrapCriteria);
    }

    public void addCriteria(WrapCriteria wrapCriteria) {
        listaCriteria.add(wrapCriteria);
    }

    public void setSort(final Sort sortSpring) {
        this.sortSpring = sortSpring;
    }

    public void setSortAscendente(final String propertyName) {
        this.sortSpring = Sort.by(Sort.Direction.ASC, propertyName);
    }

    public void setSortDiscendente(final String propertyName) {
        this.sortSpring = Sort.by(Sort.Direction.DESC, propertyName);
    }

    public Class<? extends AEntity> getEntityClazz() {
        return entityClazz;
    }

    public List<WrapCriteria> getListaCriteria() {
        return listaCriteria;
    }

    public Query getQuery() {
        Query query = new Query();

        if (listaCriteria != null && listaCriteria.size() > 0) {
            for (WrapCriteria criteria : listaCriteria) {
                query.addCriteria(criteria.getCriteria());
            }
        }

        if (sortSpring != null) {
            query.with(sortSpring);
        }

        return query;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        indefinita(false),
        nulla(false),
        errata(false),
        incompleta(false),
        validaSenzaFiltri(true),
        validaConFiltri(true);

        private boolean risultatoDiversoDaZero;

        Type(final boolean risultatoDiversoDaZero) {
            this.risultatoDiversoDaZero = risultatoDiversoDaZero;
        }

        public boolean isRisultatoDiversoDaZero() {
            return risultatoDiversoDaZero;
        }
    }

}
