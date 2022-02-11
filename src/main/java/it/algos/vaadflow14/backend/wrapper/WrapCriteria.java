package it.algos.vaadflow14.backend.wrapper;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.enumeration.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.*;

import java.io.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 11-gen-2022
 * Time: 17:48
 * Wrapper con tutte le informazioni per una Criteria delle Query <br>
 * Creata con appContext.getBean(WrapCriteria.class) <br>
 * In alternativa coi metodi statici:
 * WrapCriteria crea(final AETypeFilter typeFilter, final String propertyField, final Serializable propertyValue) <br>
 * WrapCriteria crea(final String propertyField, final Serializable propertyValue) <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WrapCriteria {

    //--property
    private AETypeFilter typeFilter = AETypeFilter.uguale;

    //--property
    private String propertyField;

    //--property
    private Object propertyValue;

    /**
     * Costruttore unico senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(WrapCriteria.class) <br>
     */
    public WrapCriteria() {
    }// end of constructor


    public static WrapCriteria crea(final AETypeFilter typeFilter, final String propertyField, final Serializable propertyValue) {
        WrapCriteria wrapCriteria = new WrapCriteria();

        wrapCriteria.typeFilter = typeFilter;
        wrapCriteria.propertyField = propertyField;
        wrapCriteria.propertyValue = propertyValue;

        return wrapCriteria;
    }// end of static method


    public static WrapCriteria crea(final String propertyField, final Serializable propertyValue) {
        WrapCriteria wrapCriteria = new WrapCriteria();

        wrapCriteria.propertyField = propertyField;
        wrapCriteria.propertyValue = propertyValue;

        return wrapCriteria;
    }// end of static method


    public AETypeFilter getTypeFilter() {
        return typeFilter;
    }

    public String getPropertyField() {
        return propertyField;
    }

    public Object getPropertyValue() {
        return propertyValue;
    }

    public Criteria getCriteria() {
        return typeFilter.getCriteria(propertyField, propertyValue);
    }

}
