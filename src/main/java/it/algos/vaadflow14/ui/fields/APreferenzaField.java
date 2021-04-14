package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.enumeration.AEOperation;
import it.algos.vaadflow14.backend.enumeration.AEPreferenza;
import it.algos.vaadflow14.backend.enumeration.AETypeBoolField;
import it.algos.vaadflow14.backend.enumeration.AETypePref;
import it.algos.vaadflow14.backend.packages.preferenza.Preferenza;
import it.algos.vaadflow14.backend.service.AEnumerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;

import static it.algos.vaadflow14.backend.application.FlowCost.ENUM_FIELD_NEW;
import static it.algos.vaadflow14.backend.application.FlowCost.ENUM_FIELD_SHOW;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 27-ago-2020
 * Time: 18:05
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class APreferenzaField extends AField<byte[]> {


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AEnumerationService enumerationService;

    private AField valueField;

    private AETypePref type;

    private Preferenza entityBean;

    private AEOperation operationForm;

    private boolean usaEnumCombo;


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(APreferenzaField.class, entityBean) <br>
     *
     * @param entityBean    preferenza di riferimento
     * @param operationForm tipologia di Form in uso
     */
    public APreferenzaField(Preferenza entityBean, AEOperation operationForm) {
        this.entityBean = entityBean;
        this.operationForm = operationForm;
        this.type = entityBean.type;
    } // end of SpringBoot constructor


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le (eventuali) istanze @Autowired <br>
     * Questo metodo viene chiamato subito dopo che il framework ha terminato l' init() implicito <br>
     * del costruttore e PRIMA di qualsiasi altro metodo <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l' ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
        sincro(type);
    }


    @Override
    protected byte[] generateModelValue() {
        if (type == AETypePref.enumeration) {
            return enumerationService.generateModelValue(entityBean, valueField);
        }
        else {
            return type.objectToBytes(valueField.getValue());
        }
    }


    @Override
    protected void setPresentationValue(byte[] bytes) {
        if (valueField != null) {
            if (type == AETypePref.enumeration) {
                valueField.setValue(enumerationService.setPresentationValue(bytes));
            }
            else {
                valueField.setValue(type.bytesToObject(bytes));
            }
        }
        else {
            logger.warn("Manca valueField", this.getClass(), "setPresentationValue");
        }
    }


    @Override
    public void setWidth(String width) {
    }


    /**
     * Cambia il valueField sincronizzandolo col comboBox
     * Senza valori, perché è attivo SOLO in modalità AddNew (new record)
     */
    public AField sincro(AETypePref type) {
        String tag = "Valore ";
        List<String> items;
        //        String enumValue = getString();

        if (valueField != null) {
            this.remove(valueField);
        }
        valueField = null;

        type = type != null ? type : AETypePref.string;
        switch (type) {
            case string:
                valueField = appContext.getBean(ATextField.class);
                valueField.setLabel(tag + "(string)");
                String message = "Valore non valido";
                //                ((ATextField) valueField).setErrorMessage(message);
                break;
            case email:
                valueField = appContext.getBean(AEmailField.class);
                valueField.setLabel(tag + "(email)");
                message = "L' indirizzo eMail non è valido";
                ((AEmailField) valueField).setErrorMessage(message);
                break;
            case integer:
                valueField = appContext.getBean(AIntegerField.class);
                valueField.setLabel(tag + "(solo interi)");
                break;
            case bool:
                valueField = appContext.getBean(ABooleanField.class, AETypeBoolField.radioTrueFalse);
                valueField.setLabel(tag + "(booleano)");
                break;
            case localdate:
                try {
                    valueField = appContext.getBean(ADateField.class);
                    valueField.setLabel(tag + "(solo data)");
                } catch (Exception unErrore) {
                    logger.error(unErrore, this.getClass(), "sincro-localdate");
                }
                break;
            case localdatetime:
                try {
                    valueField = appContext.getBean(ADateTimeField.class);
                    valueField.setLabel(tag + "(data e orario)");
                } catch (Exception unErrore) {
                    logger.error(unErrore, this.getClass(), "sincro-localdatetime");
                }
                break;
            case localtime:
                valueField = appContext.getBean(ATimeField.class);
                valueField.setLabel(tag + "(solo orario)");
                //                ((ATimePicker) valueField).setStep(Duration.ofMinutes(15));
                break;
            case enumeration:
                if (operationForm == AEOperation.addNew) {
                    valueField = appContext.getBean(ATextField.class, ENUM_FIELD_NEW);
                }
                else {
                    items = enumerationService.getList(entityBean);
                    if (array.isAllValid(items)) {
                        usaEnumCombo = items.size() <= AEPreferenza.maxEnumRadio.getInt();
                        if (usaEnumCombo) {
                            valueField = appContext.getBean(ARadioField.class, items);
                        }
                        else {
                            valueField = appContext.getBean(AComboField.class, items);
                        }
                        valueField.setLabel(ENUM_FIELD_SHOW);
                    }
                }
                break;
            case icona:
                valueField = appContext.getBean(ATextField.class);
                valueField.setLabel(tag + "(vaadin icon)");
                break;
            default:
                logger.warn("Switch - caso non definito", this.getClass(), "sincro");
                break;
        }

        if (valueField != null) {
            this.add(valueField);
        }

        this.type = type;
        return valueField;
    }


}
