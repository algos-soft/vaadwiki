package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Collection;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: mer, 10-giu-2020
 * Time: 16:19
 */
public interface AIField   {

    void setItem(Collection collection);

    void setWidth(String width);

    void setText(String caption);

    AbstractSinglePropertyField getBinder();

    Component get();

    public void setAutofocus();

    public String getKey();

    public void setErrorMessage(String errorMessage);



}
