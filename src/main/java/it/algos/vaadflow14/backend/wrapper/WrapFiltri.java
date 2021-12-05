package it.algos.vaadflow14.backend.wrapper;

import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 12-ott-2021
 * Time: 08:36
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WrapFiltri {

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

    public Class<? extends AEntity> entityClazz;

    /**
     * Filtri per dataProvider <br>
     */
    private Map<String, AFiltro> mappaFiltri;

    // Esistono DUE tipi di Sort: quello di Spring e quello di Vaadin
    private Sort sortSpring;

    // Ordine delle colonne
    private List<QuerySortOrder> sortVaadin;


    public WrapFiltri() {
    }

    public WrapFiltri(final Class<? extends AEntity> entityClazz) {
        this.entityClazz = entityClazz;
    }


    public void regola(AETypeFilter filter, String propertyField, final Object propertyValue) throws AlgosException {
        regola(entityClazz, filter, propertyField, propertyValue);
    }

    public void regola(final Class<? extends AEntity> entityClazz, AETypeFilter filter, String propertyField, Object propertyValue) throws AlgosException {
        this.mappaFiltri = mappaFiltri != null ? mappaFiltri : new HashMap<>();
        String message;
        String keyField;

        if (entityClazz == null) {
            throw AlgosException.stack("Manca la entityClazz", this.getClass(), "regola");
        }

        if (!AEntity.class.isAssignableFrom(entityClazz)) {
            throw AlgosException.stack(String.format("La entityClazz %s non è una classe valida", entityClazz.getSimpleName()), WrapFiltri.class, "regola");
        }

        if (filter == null) {
            throw AlgosException.stack("Manca la tipologia del filtro", this.getClass(), "regola");
        }

        if (text.isEmpty(propertyField)) {
            throw AlgosException.stack("Manca la propertyName del filtro", this.getClass(), "regola");
        }

        propertyField = text.levaCoda(propertyField, FIELD_NAME_ID_LINK);
        keyField = propertyField;

        if (!reflection.isEsisteFieldOnSuperClass(entityClazz, propertyField)) {
            message = String.format("La entityClazz %s esiste ma non esiste la property %s", entityClazz.getSimpleName(), propertyField);
            throw AlgosException.stack(message, this.getClass(), "regola");
        }

        if (propertyValue == null && filter != AETypeFilter.checkBox3Vie) {
            throw AlgosException.stack("Manca la propertyValue del filtro", this.getClass(), "regola");
        }

        if (annotation.isDBRef(entityClazz, propertyField)) {
            propertyField += FIELD_NAME_ID_LINK;
            filter = AETypeFilter.link;
        }

        if (mappaFiltri == null) {
            throw AlgosException.stack("Manca la mappa dei filtri", this.getClass(), "regola");
        }
        if (filter == AETypeFilter.inizia || filter == AETypeFilter.contiene) {
            mappaFiltri.remove(KEY_MAPPA_SEARCH);
        }
        else {
            mappaFiltri.remove(keyField);
        }

        switch (filter) {
            case uguale:
                if (propertyValue instanceof String) {
                    mappaFiltri.put(keyField, AFiltro.ugualeStr(propertyField, (String) propertyValue));
                }
                else {
                    mappaFiltri.put(keyField, AFiltro.ugualeObj(propertyField, propertyValue));
                }
                break;
            case inizia:
                if (text.isValid((String) propertyValue)) {
                    mappaFiltri.put(keyField, AFiltro.start(propertyField, (String) propertyValue));
                }
                break;
            case contiene:
                if (text.isValid((String) propertyValue)) {
                    mappaFiltri.put(keyField, AFiltro.contains(propertyField, (String) propertyValue));
                }
                break;
            case link:
                if (!propertyField.endsWith(FIELD_NAME_ID_LINK)) {
                    propertyField += FIELD_NAME_ID_LINK;
                }
                if (propertyValue != null && propertyValue instanceof AEntity) {
                    propertyValue = ((AEntity) propertyValue).id;
                }
                mappaFiltri.put(keyField, AFiltro.ugualeObj(propertyField, propertyValue));
                break;
            case checkBox3Vie:
                mappaFiltri.put(keyField, AFiltro.checkBox3Vie(propertyField, propertyValue));
                break;
            default:
                throw AlgosException.stack(String.format("Manca il filtro %s nello switch", filter), this.getClass(), "regola");
        }

        if (mappaFiltri.get(keyField) != null) {
            mappaFiltri.get(keyField).setType(filter);
        }
    }

    public Map<String, AFiltro> getMappaFiltri() {
        return mappaFiltri;
    }

    public void setMappaFiltri(Map<String, AFiltro> mappaFiltri) {
        this.mappaFiltri = mappaFiltri;
    }

}
