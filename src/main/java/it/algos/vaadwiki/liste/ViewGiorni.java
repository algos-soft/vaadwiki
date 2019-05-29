package it.algos.vaadwiki.liste;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadwiki.upload.UploadGiornoNato;
import org.springframework.beans.factory.annotation.Autowired;

import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_GIORNO_NATI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Tue, 28-May-2019
 * Time: 05:38
 */
public abstract class ViewGiorni extends VerticalLayout implements HasUrlParameter<String> {

    @Autowired
    protected GiornoService giornoService;


    protected String idKey;

    protected Giorno giorno;

    protected String testo;



    public void inizia() {
        TextArea area = new TextArea();

        testo = levaHeader();
        testo = levaFooter();

        area.setValue(testo);
        area.setSizeFull();
        this.add(area);
    }// end of method


    public String levaHeader() {
        String tag = "{{Div col}}";
        int posIni = testo.indexOf(tag) + tag.length();

        testo = testo.substring(posIni);

        return testo.trim();
    }// end of method



    public String levaFooter() {
        String tag = "{{Div col end}}";
        int posEnd = testo.indexOf(tag);

        testo = testo.substring(0, posEnd);

        return testo.trim();
    }// end of method

}// end of class
