package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.packages.geografica.regione.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 18-set-2020
 * Time: 06:49
 * <p>
 * Simple layer around TextField <br>
 * Banale, ma serve per avere tutti i fields omogenei <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ARegioneField extends AField<Regione> {


    public ComboBox<Regione> comboBox;


    /**
     * Costruttore senza parametri <br>
     * L' istanza viene costruita con appContext.getBean(ARegioneField.class) <br>
     */
    public ARegioneField() {
        comboBox = new ComboBox();
        comboBox.setClearButtonVisible(false);
        add(comboBox);
    } // end of SpringBoot constructor


    @Override
    protected Regione generateModelValue() {
        return comboBox.getValue();
    }


    @Override
    protected void setPresentationValue(Regione value) {
        comboBox.setValue(value);
    }


    public void setItems(List<Regione> items) {
        if (items != null) {
            try {
                comboBox.setItems(items);
            } catch (Exception unErrore) {
                System.out.println("Items nulli in AComboField.setItems()");
            }
        }
    }

    @Override
    public void setWidth(String width) {
        comboBox.setWidth(width);
    }

}
