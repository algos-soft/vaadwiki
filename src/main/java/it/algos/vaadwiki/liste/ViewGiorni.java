package it.algos.vaadwiki.liste;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadwiki.upload.UploadGiornoNato;
import org.springframework.beans.factory.annotation.Autowired;

import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_GIORNI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Tue, 28-May-2019
 * Time: 05:38
 */
@Route(value = ROUTE_VIEW_GIORNI)
public class ViewGiorni extends Div implements HasUrlParameter<String> {

    @Autowired
    GiornoService giornoService;

    @Autowired
    private UploadGiornoNato uploadGiornoNato;

    private String idKey;

    private Giorno giorno;


    @Override
    public void setParameter(BeforeEvent event, String giornoIdKey) {
        idKey = giornoIdKey;
        inizia();
    }// end of method


    public void inizia() {
        giorno = giornoService.findById(idKey);

        uploadGiornoNato.esegueTest(giorno);
        String testo = uploadGiornoNato.righeSemplici();
        this.setText(testo);
    }// end of method

}// end of class
