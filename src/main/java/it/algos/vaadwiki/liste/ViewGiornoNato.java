package it.algos.vaadwiki.liste;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadwiki.upload.UploadGiornoNato;
import org.springframework.beans.factory.annotation.Autowired;

import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_GIORNO_NATI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Tue, 28-May-2019
 * Time: 22:38
 */
@Route(value = ROUTE_VIEW_GIORNO_NATI)
public class ViewGiornoNato extends ViewGiorni {

    @Autowired
    private UploadGiornoNato uploadGiornoNato;


    @Override
    public void setParameter(BeforeEvent event, String giornoIdKey) {
        super.idKey = giornoIdKey;
        this.inizia();
    }// end of method

    public void inizia() {
        giorno = giornoService.findById(idKey);
        uploadGiornoNato.esegueTest(giorno);
        testo = uploadGiornoNato.getTesto();

        super.inizia();
    }// end of method

}// end of class
