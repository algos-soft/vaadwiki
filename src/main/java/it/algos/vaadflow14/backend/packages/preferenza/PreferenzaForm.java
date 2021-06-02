package it.algos.vaadflow14.backend.packages.preferenza;

import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.fields.*;
import it.algos.vaadflow14.ui.form.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.time.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 27-ago-2020
 * Time: 06:37
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//Algos
@AIScript(sovraScrivibile = false, doc = AEWizDoc.inizioRevisione)
public class PreferenzaForm extends AForm {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AEnumerationService enumerationService;

    private AField valueField;


    public PreferenzaForm(ALogicOld logic, WrapForm wrap) {
        super(logic,wrap);
    }


    public void fixType() {
        AField field;
        Object comp = null;
        ComboBox combo;

        if (fieldsMap != null) {

            field = fieldsMap.get(FIELD_TYPE);
            if (field != null) {
                //            if (field != null && field instanceof ComboBox) {
                //                combo = (ComboBox) comp;
                //                combo.addValueChangeListener(e -> sincro((AETypePref) e.getValue()));
            }
            //@todo Linea di codice provvisoriamente commentata e DA RIMETTERE
        }
    }


    /**
     * Eventuali aggiustamenti finali al layout <br>
     * Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap) <br>
     * Regola eventuali valori delle property in apertura della scheda <br>
     * Può essere sovrascritto <br>
     */
    @Override
    protected void fixLayoutFinal() {
        //--recupera il field comboBox type
        AComboField fieldType = (AComboField) fieldsMap.get("type");

        //--recupera il field value
        APreferenzaField fieldValue = (APreferenzaField) fieldsMap.get("value");

        //--aggiunge un listener per invocare la sincronizzazione del field value
        fieldType.addValueChangeListener(event -> fieldValue.sincro((AETypePref)event.getValue()));
    }


//    /**
//     * Cambia il valueField sincronizzandolo col comboBox
//     * Senza valori, perché è attivo SOLO in modalità AddNew (new record)
//     */
//    protected AField sincro(AETypePref type) {
//        String caption = "Valore ";
//        List<String> items;
//        String enumValue = getString();
//
//        if (valueField != null) {
//            topLayout.remove(valueField);
//            valueField = null;
//        }
//
//        type = type != null ? type : AETypePref.string;
//        switch (type) {
//            case string:
//                caption += "(string)";
//                valueField = appContext.getBean(ATextField.class, caption);
//                break;
//            case email:
//                caption += "(email)";
//                valueField = appContext.getBean(AEmailField.class, caption);
//                String message = "L' indirizzo eMail non è valido";
//                ((AEmailField) valueField).setErrorMessage(message);
//                break;
//            case integer:
//                caption += "(solo interi)";
//                valueField = appContext.getBean(AIntegerField.class, caption);
//                break;
//            case bool:
//                valueField = appContext.getBean(ABooleanField.class, AETypeBoolField.radioTrueFalse, BOOL_FIELD);
//                break;
//            case localdate:
//                valueField = appContext.getBean(ADateField.class);
//                break;
//            case localdatetime:
//                valueField = appContext.getBean(ADateTimeField.class);
//                break;
//            case localtime:
//                valueField = appContext.getBean(ATimeField.class);
//                //                ((ATimePicker) valueField).setStep(Duration.ofMinutes(15));
//                break;
//            case enumeration:
//                if (operationForm == AEOperation.addNew) {
//                    valueField = appContext.getBean(ATextField.class, ENUM_FIELD_NEW);
//                } else {
//                    if (enumValue.contains(PUNTO_VIRGOLA)) {
//                        enumValue = text.levaCodaDa(enumValue, PUNTO_VIRGOLA);
//                    }
//                    items = enumerationService.getList(enumValue);
//                    if (array.isValid(items)) {
//                        valueField = appContext.getBean(AComboField.class, ENUM_FIELD_SHOW, items);
//                    }
//                }
//                break;
//            default:
//                logger.warn("Switch - caso non definito");
//                break;
//        }
//
//        if (valueField != null) {
//            topLayout.add(valueField);
//        }
//
//        return valueField;
//    }


    /**
     * Regola in lettura eventuali valori NON associati al binder. <br>
     * Dal DB alla UI <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    //    @Override
    protected void readFieldsExtraNo() {
        super.readFieldsExtra();
        fixType();

        if (valueField != null) {
            topLayout.remove(valueField);
        }

        AETypePref type = getType();
        Object genericValue = getValue();
        String stringValue = getString();

//        valueField = sincro(type);
        switch (type) {
            case string:
                valueField.setValue(stringValue);
                break;
            case email:
                valueField.setValue(stringValue);
                break;
            case integer:
                if (genericValue instanceof Number) {
                    valueField.setValue(genericValue);
                }
                break;
            case bool:
                valueField.setValue((boolean) genericValue);
                break;
            case localdate:
                if (genericValue instanceof LocalDate) {
                    valueField.setValue(genericValue);
                }
                break;
            case localdatetime:
                if (genericValue instanceof LocalDateTime) {
                    valueField.setValue(genericValue);
                }
                break;
            case localtime:
                if (genericValue instanceof LocalTime) {
                    valueField.setValue(genericValue);
                }
                break;
            case enumeration:
                if (text.isValid(stringValue)) {
                    if (stringValue.contains(PUNTO_VIRGOLA)) {
                        stringValue = text.levaTestoPrimaDi(stringValue, PUNTO_VIRGOLA);
                    } else {
                        stringValue = VUOTA;
                    }
                    valueField.setValue(stringValue);
                }
                break;
            default:
                logger.warn("Switch - caso non definito");
                break;
        }

        //        AComboBox comboType = (AComboBox) getField(TIPO_FIELD_NAME);
        //        comboType.setValue(type);
        //        if (operation == EAOperation.addNew) {
        //            comboType.setEnabled(true);
        //            comboType.addValueChangeListener(e -> sincro((EAPrefType) e.getValue()));
        //        } else {
        //            comboType.setEnabled(false);
        //        }

    }


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dalla  UI al DB
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void writeFieldsExtra() {
        super.writeFieldsExtra();

        AETypePref type = getType();
        byte[] byteValue;
        Object genericFieldValue = null;
        String newSelectedFieldValue;
        String rawValue = VUOTA;

        if (valueField != null) {
            genericFieldValue = valueField.getValue();
            newSelectedFieldValue = genericFieldValue != null && genericFieldValue instanceof String ? (String) genericFieldValue : VUOTA;

            switch (type) {
                case string:
                    break;
                case email:
                    break;
                case integer:
                    break;
                case bool:
                    break;
                case localdate:
                    break;
                case localdatetime:
                    break;
                case localtime:
                    break;
                case enumeration:
//                    rawValue = ((PreferenzaLogic) entityLogic).getEnumRawValue(entityBean.id);
                    genericFieldValue = enumerationService.convertToModel(rawValue, newSelectedFieldValue);
                    //                    if (currentItem != null && text.isValid(currentItem.id)) {
                    //                        mongoEnumValue = (String) currentItem.getType().bytesToObject(currentItem.value);
                    //                        genericFieldValue = enumService.convertToModel(mongoEnumValue, (String) genericFieldValue);
                    //                    } else {
                    //                        genericFieldValue = genericFieldValue;
                    //                    }// end of if/else cycle
                    break;
                default:
                    logger.warn("Switch - caso non definito");
                    break;
            }

            byteValue = type.objectToBytes(genericFieldValue);
            ((Preferenza) entityBean).setValue(byteValue);
        }

    }


    private AETypePref getType() {
        AETypePref type = null;
        Preferenza preferenza = (Preferenza) entityBean;

        if (preferenza != null) {
            type = preferenza.getType();
        }

        return type != null ? type : AETypePref.string;
    }


    private Object getValue() {
        Object genericValue;

        byte[] byteValue = ((Preferenza) entityBean).getValue();
        AETypePref type = getType();
        genericValue = type.bytesToObject(byteValue);

        return genericValue;
    }


    private String getString() {
        String stringValue = VUOTA;
        Object genericValue = getValue();

        if (genericValue instanceof String) {
            stringValue = (String) genericValue;
        }

        return stringValue;
    }


}
