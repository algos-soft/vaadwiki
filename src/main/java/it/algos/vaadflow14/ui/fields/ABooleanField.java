package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.radiobutton.*;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.*;
import java.util.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: gio, 11-giu-2020
 * Time: 21:54
 * Layer around boolean value <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ABooleanField extends AField<Boolean> {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService text;

    private boolean usaCheckBox;

    private Checkbox checkBox;

    private RadioButtonGroup<Boolean> radioGroup;

    private AETypeBoolField typeBool;

    private String boolEnum = VUOTA;

    private String caption = VUOTA;

    private String captionCheckBox = VUOTA;

    private String firstItem;

    private String secondItem;


    //    /**
    //     * Costruttore con parametri <br>
    //     * L' istanza viene costruita con appContext.getBean(AEmailField.class, caption) <br>
    //     *
    //     * @param typeBool per la tipologia di visualizzazione
    //     * @param caption  label visibile del field
    //     */
    //    public ABooleanField(String caption, AETypeBoolField typeBool) {
    //        this(VUOTA, typeBool, caption, VUOTA);
    //    } // end of SpringBoot constructor


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(ABooleanField.class, typeBool) <br>
     *
     * @param typeBool per la tipologia di visualizzazione
     */
    public ABooleanField(AETypeBoolField typeBool) {
        this(typeBool, VUOTA);
    } // end of SpringBoot constructor


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(ABooleanField.class, typeBool, boolEnum) <br>
     *
     * @param typeBool per la tipologia di visualizzazione
     * @param mixValue valori custom della scelta booleana oppure testo del checkBox
     */
    public ABooleanField(AETypeBoolField typeBool, String mixValue) {
        this.typeBool = typeBool;
        usaCheckBox = typeBool == AETypeBoolField.checkBox;
        if (usaCheckBox) {
            this.captionCheckBox = mixValue;
        } else {
            this.boolEnum = mixValue;
        }
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
        initView();
    }


    protected void initView() {
        List<Boolean> items = new ArrayList<>();

        if (usaCheckBox) {
            checkBox = new Checkbox();
            checkBox.getElement().setAttribute("style", "align-left: flex-start;");
        } else {
            radioGroup = new RadioButtonGroup<>();
        }

        items.add(true);
        items.add(false);

        if (typeBool != null) {
            switch (typeBool) {
                case radioTrueFalse:
                    radioGroup.setItems(items);
                    radioGroup.setLabel(caption);
                    radioGroup.setRenderer(new TextRenderer<>(e -> e ? "vero" : "falso"));
                    break;
                case checkBox:
                    checkBox.setLabelAsHtml(captionCheckBox);
                    break;
                case radioSiNo:
                    radioGroup.setItems(items);
                    radioGroup.setLabel(caption);
                    radioGroup.setRenderer(new TextRenderer<>(e -> e ? "Si" : "No"));
                    break;
                case radioCustomHoriz:
                    radioGroup.setItems(items);
                    radioGroup.setRenderer(new TextRenderer<>(e -> e ? getItems().get(0) : getItems().get(1)));
                    break;
                case radioCustomVert:
                    radioGroup.setItems(items);
                    radioGroup.setLabel(caption);
                    radioGroup.setRenderer(new TextRenderer<>(e -> e ? getItems().get(0) : getItems().get(1)));
                    //                    radioGroup.getElement().setAttribute("style", "spacing:0em; margin:0em; padding:0em;");
                    radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
                    break;
                default:
                    //                    logger.warn("Switch - caso non definito", this.getClass(), "nomeDelMetodo");
                    break;
            }
        }

        if (usaCheckBox) {
            add(checkBox);
        } else {
            add(radioGroup);
        }
    }


    protected List<String> getItems() {
        List<String> items = new ArrayList<>();
        String[] parti;

        if (text.isValid(boolEnum) && boolEnum.contains(VIRGOLA)) {
            parti = boolEnum.split(VIRGOLA);
            if (parti != null && parti.length == 2) {
                firstItem = parti[0].trim();
                secondItem = parti[1].trim();
                firstItem = text.primaMaiuscola(firstItem);
                secondItem = text.primaMaiuscola(secondItem);
                items.add(firstItem);
                items.add(secondItem);
            }
        } else {
            items.add("Si");
            items.add("No");
        }

        return items;
    }


    @Override
    protected Boolean generateModelValue() {
        if (usaCheckBox) {
            return checkBox.getValue();
        } else {
            return radioGroup.getValue();
        }
    }


    @Override
    protected void setPresentationValue(Boolean value) {
        if (usaCheckBox) {
            checkBox.setValue(value);
        } else {
            if (value) {
                radioGroup.setValue(value);
            } else {
                radioGroup.setValue(value);
            }
        }
    }

}
