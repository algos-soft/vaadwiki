package it.algos.vaadwiki.liste;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.HasUrlParameter;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwiki.modules.wiki.WikiGiornoViewList;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Wed, 29-May-2019
 * Time: 22:34
 * <p>
 * Classe astratta per la visualizzazione di una lista di prova di biografie di un particolare giorno/anno <br>
 * Viene invocata da WikiGiornoViewList e da WikiAnnoViewList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Due sottoclassi (concrete) per i Nati e per i Morti dei giorni <br>
 * Due sottoclassi (concrete) per i Nati e per i Morti degli anni <br>
 */
public abstract class ViewListe extends VerticalLayout implements HasUrlParameter<String> {

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ADateService date;


    protected String idKey;


    protected String testo;

//    protected Button backButton;


    protected void inizia() {
        TextArea area = new TextArea();

        this.add(backButton());

        this.levaHeader();
        this.levaFooter();
        this.addTitolo();

        area.setValue(testo);
        area.setSizeFull();
        this.add(area);

        this.add(backButton());
    }// end of method


    protected Component backButton() {
        Button backButton = new Button("Back", new Icon(VaadinIcon.ARROW_LEFT));
        backButton.getElement().setAttribute("theme", "primary");
        backButton.addClassName("view-toolbar__button");
        backButton.addClickListener(e -> UI.getCurrent().getPage().getHistory().back());
        return backButton;
    }// end of method


    /**
     * Leva il testo iniziale (sempre) fino al tag1 <br>
     * Leva il testo iniziale (opzionale) fino al tag2 <br>
     */
    protected void levaHeader() {
        String tag1 = "</noinclude>";
        String tag2 = "{{Div col}}";
        int pos = 0;

        pos = testo.indexOf(tag1);
        if (pos > 0) {
            pos += tag1.length();
            testo = testo.substring(pos);
        }// end of if cycle

        pos = testo.indexOf(tag2);
        if (pos > 0) {
            pos += tag2.length();
            testo = testo.substring(pos);
        }// end of if cycle

        testo.trim();
    }// end of method


    /**
     * Leva il testo finale (sempre) dopo il tag1 <br>
     * Leva il testo finale (opzionale) dopo il tag2 <br>
     */
    protected void levaFooter() {
        String tag1 = "<noinclude>";
        String tag2 = "{{Div col end}}";
        int pos = 0;

        pos = testo.indexOf(tag1);
        if (pos > 0) {
            testo = testo.substring(0, pos);
        }// end of if cycle

        pos = testo.indexOf(tag2);
        if (pos > 0) {
            testo = testo.substring(0, pos);
        }// end of if cycle

        testo.trim();
    }// end of method


    protected void addTitolo() {
    }// end of method

}// end of class
