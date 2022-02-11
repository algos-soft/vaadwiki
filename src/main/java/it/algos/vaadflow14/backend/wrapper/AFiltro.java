package it.algos.vaadflow14.backend.wrapper;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.service.*;
import org.bson.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.*;

import java.io.*;
import java.util.regex.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 19-dic-2019
 * Time: 11:01
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AFiltro implements Serializable {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     */
    @Autowired
    public TextService text;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     */
    @Autowired
    public AnnotationService annotation;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     */
    @Autowired
    public ReflectionService reflection;


    private Criteria criteria;

    private Sort sort;

    private String tag;

    private Class<? extends AEntity> entityClazz;

    private AETypeFilter type;

    private String propertyField;

    private Object propertyValue;


    public AFiltro() {
    }

    public AFiltro(Criteria criteria) {
        this.criteria = criteria;
        this.sort = null;
    }


    public AFiltro(Criteria criteria, Sort sort) {
        this.criteria = criteria;
        this.sort = sort;
    }


    public AFiltro(Sort sort) {
        this.sort = sort;
    }

    public AFiltro(final Class<? extends AEntity> entityClazz) {
        this.entityClazz = entityClazz;
    }

    public AFiltro(final Class<? extends AEntity> entityClazz, AETypeFilter type, String propertyField, Object propertyValue) {
        this.entityClazz = entityClazz;
        this.type = type;
        this.propertyField = propertyField;
        this.propertyValue = propertyValue;
    }

    public static AFiltro start(String fieldName, String value) {
        AFiltro filtro = new AFiltro();

        String questionPattern = "^" + Pattern.quote(value) + ".*";
        Criteria criteria = Criteria.where(fieldName).regex(questionPattern, "i");
        filtro.criteria = criteria;

        return filtro;
    }

    public static AFiltro contains(String fieldName, String value) {
        AFiltro filtro = new AFiltro();

        String questionPattern = ".*" + Pattern.quote(value) + ".*";
        Criteria criteria = Criteria.where(fieldName).regex(questionPattern, "i");
        filtro.criteria = criteria;

        return filtro;
    }

    public static AFiltro ugualeStr(String fieldName, String value) {
        return new AFiltro(AETypeFilter.uguale.getCriteria(fieldName, value));
    }

    public static AFiltro ugualeObj(String fieldName, Object value) {
        AFiltro filtro = new AFiltro();
        Criteria criteria;

        if (AEntity.class.isAssignableFrom(value.getClass())) {
            fieldName += FIELD_NAME_ID_LINK;
            value = ((AEntity) value).id;
        }

        criteria = Criteria.where(fieldName).is(value);
        filtro.criteria = criteria;

        return filtro;
    }

    public static AFiltro vero(String fieldName) {
        return booleano(fieldName, true);
    }

    public static AFiltro falso(String fieldName) {
        return booleano(fieldName, false);
    }

    public static AFiltro booleano(String fieldName, boolean value) {
        AFiltro filtro = new AFiltro();

        Criteria criteria = Criteria.where(fieldName).is(value);
        filtro.criteria = criteria;

        return filtro;
    }

    public static AFiltro checkBox3Vie(String fieldName, Object value) {
        if (value != null && value instanceof Boolean booleanValue) {
            return AFiltro.booleano(fieldName, booleanValue);
        }
        else {
            return new AFiltro();
        }
    }

    public AFiltro regola() throws AlgosException {
        String message;
        String keyField;

        if (entityClazz == null) {
            throw AlgosException.stack("Manca la entityClazz", this.getClass(), "regola");
        }

        if (!AEntity.class.isAssignableFrom(entityClazz)) {
            throw AlgosException.stack(String.format("La entityClazz %s non è una classe valida", entityClazz.getSimpleName()), WrapFiltri.class, "regola");
        }

        if (type == null) {
            throw AlgosException.stack("Manca la tipologia del filtro", this.getClass(), "regola");
        }

        if (text.isEmpty(propertyField)) {
            throw AlgosException.stack("Manca la propertyField del filtro", this.getClass(), "regola");
        }

        propertyField = text.levaCoda(propertyField, FIELD_NAME_ID_LINK);
        keyField = propertyField;

        if (!reflection.isEsisteFieldOnSuperClass(entityClazz, propertyField)) {
            message = String.format("La entityClazz %s esiste ma non esiste la property %s", entityClazz.getSimpleName(), propertyField);
            throw AlgosException.stack(message, this.getClass(), "regola");
        }

        if (propertyValue == null) {
            throw AlgosException.stack("Manca la propertyValue del filtro", this.getClass(), "regola");
        }

        if (annotation.isDBRef(entityClazz, propertyField)) {
            propertyField += FIELD_NAME_ID_LINK;
            type = AETypeFilter.link;
        }

        switch (type) {
            case uguale:
                if (propertyValue instanceof String) {
                    criteria = AETypeFilter.uguale.getCriteria(propertyField, (String) propertyValue);
                }
                else {
                    criteria = AETypeFilter.uguale.getCriteria(propertyField, propertyValue);
                }
                break;
            case inizia:
                criteria = AETypeFilter.inizia.getCriteria(propertyField, propertyValue);
                break;
            case contiene:
                criteria = AETypeFilter.contiene.getCriteria(propertyField, propertyValue);
                break;
            case link:
                if (!propertyField.endsWith(FIELD_NAME_ID_LINK)) {
                    propertyField += FIELD_NAME_ID_LINK;
                }
                if (propertyValue != null && propertyValue instanceof AEntity) {
                    propertyValue = ((AEntity) propertyValue).id;
                }
                criteria = Criteria.where(propertyField).is(propertyValue);
                break;
            default:
                throw AlgosException.stack(String.format("Manca il filtro %s nello switch", type), this.getClass(), "regola");
        }

        return this;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public Sort getSort() {
        return sort;
    }


    public String getTag() {
        return tag;
    }

    public AFiltro getClone() {
        AFiltro deepCopy = new AFiltro();

        Document doc = this.criteria.getCriteriaObject();
        deepCopy.criteria = Criteria.byExample(doc);

        return deepCopy;
    }

    public AETypeFilter getType() {
        return type;
    }

    public void setType(AETypeFilter type) {
        this.type = type;
    }

    public String getPropertyField() {
        return propertyField;
    }

    public Object getPropertyValue() {
        return propertyValue;
    }

}
