package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.shared.Registration;
import it.algos.vaadflow14.backend.service.ArrayService;
import it.algos.vaadflow14.backend.service.ALogService;
import it.algos.vaadflow14.backend.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Collection;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: mer, 10-giu-2020
 * Time: 16:25
 */
public abstract class AField<T> extends CustomField<T> implements AIField {

    /**
     * Istanza di una interfaccia SpringBoot <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

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
    public ArrayService array;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ALogService logger;

    protected String fieldKey;

    protected String caption;


//    /**
//     * Costruttore con parametri <br>
//     * L' istanza viene costruita nella sottoclasse concreta <br>
//     *
//     * @param fieldKey nome interno del field
//     * @param caption  label visibile del field
//     */
//    public AField(String fieldKey, String caption) {
//        this.fieldKey = fieldKey;
//        this.caption = caption;
//    } // end of SpringBoot constructor


    @Override
    public void setItem(Collection collection) {
    }


    @Override
    protected T generateModelValue() {
        return null;
    }


    @Override
    protected void setPresentationValue(T o) {
    }


    //    @Override
    //    public void setValue(T o) {
    //
    //    }


    @Override
    public Registration addValueChangeListener(ValueChangeListener valueChangeListener) {
        return null;
    }


    @Override
    public void setWidth(String width) {
    }


    @Override
    public void setText(String caption) {

    }


    //    @Override
    //    public void setValue(Object o) {
    //
    //    }


    @Override
    public AbstractSinglePropertyField getBinder() {
        return null;
    }


    @Override
    public Component get() {
        return this;
    }


    @Override
    public void setAutofocus() {
    }


    @Override
    public String getKey() {
        return fieldKey;
    }


    @Override
    public void setErrorMessage(String errorMessage) {
        super.setErrorMessage(errorMessage);
    }


    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

}

