package it.algos.vaadwiki.liste;

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
 * <p>
 * Classe per la visualizzazione di una lista di prova di biografie di un particolare giorno <br>
 * Viene invocata da WikiGiornoViewList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Lista dei Nati nel giorno <br>
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


    protected void addTitolo() {
        String titolo = "Lista di biografie di persone nate il " + giorno.getTitolo() + "\n";
        testo = titolo + testo;
    }// end of method

}// end of class
