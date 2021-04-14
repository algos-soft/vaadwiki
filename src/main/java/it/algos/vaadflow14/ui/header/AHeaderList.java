package it.algos.vaadflow14.ui.header;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.application.FlowCost;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 02-ago-2020
 * Time: 11:22
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AHeaderList extends AHeader {

    /**
     * Costruttore base senza parametri <br>
     * Non usa @Autowired perché l' istanza viene creata con appContext.getBean(AHeader.class) <br>
     */
    public AHeaderList() {
    }


    /**
     * Costruttore base con parametro <br>
     * Non usa @Autowired perché l' istanza viene creata con appContext.getBean(AHeader.class, alertWrap) <br>
     */
    public AHeaderList(List<String> alertHtml) {
        this.alertHtml = alertHtml;
    }


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l' ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
        initView();
    }


    /**
     * Qui va tutta la logica iniziale della view <br>
     * Di default la scritta viene presentata in blue <br>
     * Se arrivano indicazioni di uno specifico colore, vengono mantenute <br>
     */
    protected void fixView() {
        Span span = null;
        String tagIni = "<span style=\"color";
        String tagIniBlue = "<span style=\"color:blue\">";
        String tagEnd = "</span>";
        String tagFine = FlowCost.PUNTO;
        boolean puntoFinale = true; //@todo Creare una preferenza e sostituirla qui

        if (alertHtml != null) {
            for (String alert : alertHtml) {
                alert = alert.trim();

                if (puntoFinale) {
                    if (!alert.endsWith(tagFine)) {
                        alert += tagFine;
                    }
                } else {
                    if (alert.endsWith(tagFine)) {
                        alert = text.levaCoda(alert, tagFine);
                    }
                }

                if (!alert.startsWith(tagIni)) {
                    alert = tagIniBlue + alert + tagEnd;
                }

                span = new Span();
                span.getElement().setProperty("innerHTML", alert);
                this.add(span);
            }
        }

    }

}
