package it.algos.vaadwiki.liste;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadwiki.upload.UploadGiornoMorto;
import org.springframework.beans.factory.annotation.Autowired;

import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_GIORNO_MORTI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Wed, 29-May-2019
 * Time: 15:46
 * Classe per la visualizzazione di una lista di prova di biografie di un particolare giorno <br>
 * Viene invocata da WikiGiornoViewList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Lista dei Morti nel giorno <br>
 */
@Route(value = ROUTE_VIEW_GIORNO_MORTI)
public class ViewGiornoMorto extends ViewGiorni {

    @Autowired
    private UploadGiornoMorto uploadGiornoMorto;


    @Override
    public void setParameter(BeforeEvent event, String giornoIdKey) {
        super.idKey = giornoIdKey;
        this.inizia();
    }// end of method


    public void inizia() {
        giorno = giornoService.findById(idKey);
        uploadGiornoMorto.esegueTest(giorno);
        testo = uploadGiornoMorto.getTesto();

        super.inizia();
    }// end of method


    protected void addTitolo() {
        String titolo = "Lista di biografie di persone morte il " + giorno.getTitolo() + "\n";
        testo = titolo + testo;
    }// end of method

}// end of class
