package it.algos.vaadflow14.wizard.scripts;

import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.ui.fields.*;
import it.algos.vaadflow14.wizard.enumeration.*;


/**
 * Project vaadwiki14
 * Created by Algos
 * User: gac
 * Date: lun, 28-dic-2020
 * Time: 18:59
 * Layer per presentare un checkBox eventualmente seguito da un TextField <br>
 */
public class WizBox extends HorizontalLayout {

    private ACheckBox checkbox;

    private TextField textField;

    private String fieldCaption;


    public WizBox(AEWizCost aeCost, boolean isAccesoInizialmente) {
        checkbox = new ACheckBox(aeCost.getDescrizione() + FlowCost.FORWARD + "AECopyWiz." + aeCost.getCopyWiz());
        this.setValue(isAccesoInizialmente);
        this.add(checkbox);

    }

    public WizBox(AEPackage pack) {
        checkbox = new ACheckBox(pack.getDescrizione());
        this.setValue(pack.isAcceso());
        this.add(checkbox);
        fieldCaption = pack.getFieldName();

        if (pack.isFieldAssociato()) {
            textField = new TextField();
            if (pack.is()) {
                textField.setValue(pack.getFieldName());
            }
            textField.setAutoselect(true);
            checkbox.getBinder().addValueChangeListener(event -> sincroText());

            this.add(textField);
        }
    }


    public WizBox(AEModulo modulo) {
        checkbox = new ACheckBox(modulo.getDescrizione());
        this.setValue(true);
        this.add(checkbox);
        fieldCaption = modulo.getTag();
    }

    @Deprecated
    public WizBox(AECheck check) {
        checkbox = new ACheckBox(check.getCaption(), check.getCaption());
        this.setValue(check.isAccesoInizialmente());
        this.add(checkbox);
        this.fieldCaption = check.getFieldName();

        if (check.isFieldAssociato()) {
            textField = new TextField();
            if (check.is()) {
                textField.setValue(fieldCaption);
            }
            textField.setAutoselect(true);
            checkbox.getBinder().addValueChangeListener(event -> sincroText());

            this.add(textField);
        }
    }

    private void sincroText() {
        if (checkbox.getBinder().getValue()) {
            textField.setValue(fieldCaption);
        }
        else {
            textField.setValue(FlowCost.VUOTA);
        }
    }

    public Checkbox getBox() {
        return checkbox.getBinder();
    }

    public boolean is() {
        return getBox().getValue();
    }

    public String getValue() {
        return textField != null ? textField.getValue() : FlowCost.VUOTA;
    }

    public void setValue(boolean value) {
        getBox().setValue(value);
    }

}
