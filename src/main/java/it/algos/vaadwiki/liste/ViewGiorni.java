package it.algos.vaadwiki.liste;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.HasUrlParameter;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwiki.modules.wiki.WikiGiornoViewList;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Tue, 28-May-2019
 * Time: 05:38
 * Classe astratta per la visualizzazione di una lista di prova di biografie di un particolare giorno <br>
 * Viene invocata da WikiGiornoViewList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Due sottoclassi (concrete) per i Nati e per i Morti <br>
 */
public abstract class ViewGiorni extends VerticalLayout implements HasUrlParameter<String> {

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ADateService date;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected GiornoService giornoService;


    protected String idKey;

    protected Giorno giorno;

    protected String testo;


    protected void inizia() {
        TextArea area = new TextArea();

        this.add(backButton());

        this.levaHeader();
        this.levaFooter();
        this.addTitolo();

        area.setValue(testo);
        area.setSizeFull();
        this.add(area);
    }// end of method


    protected Component backButton() {
        Button button = new Button("Back", new Icon(VaadinIcon.ARROW_LEFT));
        button.getElement().setAttribute("theme", "primary");
        button.addClassName("view-toolbar__button");
        button.addClickListener(e -> UI.getCurrent().navigate(WikiGiornoViewList.class));

        return button;
    }// end of method


    protected void levaHeader() {
        String tag = "{{Div col}}";
        int posIni = testo.indexOf(tag) + tag.length();

        testo = testo.substring(posIni);
    }// end of method


    protected void levaFooter() {
        String tag = "{{Div col end}}";
        int posEnd = testo.indexOf(tag);

        testo = testo.substring(0, posEnd);
    }// end of method


    protected void addTitolo() {
    }// end of method

}// end of class
