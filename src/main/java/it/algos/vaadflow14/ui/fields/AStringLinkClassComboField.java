package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.shared.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.entity.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 13-dic-2021
 * Time: 17:22
 * Simple layer around ComboBox value <br>
 * Banale, ma serve per avere tutti i fields omogenei <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AStringLinkClassComboField<T> extends AField<Object> {


    public ComboBox comboBox;

    private Field reflectionJavaField;

    private List items;


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(AComboField.class, items) <br>
     *
     * @param items collezione dei valori previsti
     */
    public AStringLinkClassComboField(List<String> items) {
        this(items, true);
    } // end of SpringBoot constructor


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(AComboField.class, items, isRequired) <br>
     *
     * @param items      collezione dei valori previsti
     * @param isRequired true, se NON ammette il valore nullo
     */
    public AStringLinkClassComboField(List<String> items, boolean isRequired) {
        this(items, isRequired, false);
    } // end of SpringBoot constructor


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(AComboField.class, items, isRequired, isAllowCustomValue) <br>
     *
     * @param items      collezione dei valori previsti
     * @param isRequired true, se NON ammette il valore nullo
     * @param isRequired true, se si possono aggiungere valori direttamente
     */
    public AStringLinkClassComboField(List<String> items, boolean isRequired, boolean isAllowCustomValue) {
        comboBox = new ComboBox();
        this.setItems(items);
        comboBox.setClearButtonVisible(!isRequired);

        /**
         * Allow users to enter a value which doesn't exist in the data set, and
         * set it as the value of the ComboBox.
         */
        if (isAllowCustomValue) {
            comboBox.setAllowCustomValue(true);
            this.addCustomListener();
        }

        add(comboBox);
    } // end of SpringBoot constructor


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(AComboField.class, comboBox) <br>
     */
    public AStringLinkClassComboField(final Field reflectionJavaField, final ComboBox comboBox) {
        this.reflectionJavaField = reflectionJavaField;
        this.comboBox = comboBox;
        this.comboBox.setClearButtonVisible(false);
        add(this.comboBox);
    } // end of SpringBoot constructor


    @Override
    protected Object generateModelValue() {
        String stringLinkClassValue;
        AEntity entityBeanLinkata = (AEntity) comboBox.getValue();
        Class entityClazz = entityBeanLinkata.getClass();
        String keyPropertyName;

        keyPropertyName = annotation.getLinkProperty(reflectionJavaField);
        if (text.isEmpty(keyPropertyName)) {
            keyPropertyName = annotation.getKeyPropertyName(entityClazz);
        }
        if (!reflection.isEsiste(entityClazz,keyPropertyName)) {
            keyPropertyName = annotation.getKeyPropertyName(entityClazz);
        }
        if (text.isEmpty(keyPropertyName)) {
            keyPropertyName = FlowCost.FIELD_ID_PROPERTY;
        }
        stringLinkClassValue = reflection.getPropertyValueStr(entityBeanLinkata, keyPropertyName);

        return stringLinkClassValue;
    }

    @Override
    protected void setPresentationValue(Object value) {
        comboBox.setValue(value);
    }

    public void addCustomListener() {
        comboBox.addCustomValueSetListener(event -> {
            String newValue = ((GeneratedVaadinComboBox.CustomValueSetEvent) event).getDetail();
            items.add(newValue);
            setItems(items);
            comboBox.setValue(newValue);
        });
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener valueChangeListener) {
        return comboBox.addValueChangeListener(valueChangeListener);
    }

    @Override
    public void setWidth(String width) {
        comboBox.setWidth(width);
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        comboBox.setErrorMessage(errorMessage);
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        if (items != null) {
            try {
                this.items = items;
                comboBox.setItems(items);
            } catch (Exception unErrore) {
                System.out.println("Items nulli in AComboField.setItems()");
            }
        }
    }

}
