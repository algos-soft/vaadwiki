package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 31-ago-2020
 * Time: 12:29
 * Simple layer around RadioButtonGroup <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ARadioField extends AField<String> {


    private RadioButtonGroup<String> radioGroup;

    private List<String> items;


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(ARadioField.class, items) <br>
     *
     * @param items da visualizzare
     */
    public ARadioField(List<String> items) {
        this.items = items;
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
        radioGroup = new RadioButtonGroup<>();
        radioGroup.setItems(items);
        add(radioGroup);
    }


    @Override
    protected String generateModelValue() {
        return radioGroup.getValue();
    }


    @Override
    protected void setPresentationValue(String value) {
        radioGroup.setValue(value);
    }


}
