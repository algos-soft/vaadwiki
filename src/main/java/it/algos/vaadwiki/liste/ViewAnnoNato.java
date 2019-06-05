package it.algos.vaadwiki.liste;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadwiki.upload.UploadAnnoNato;
import it.algos.vaadwiki.upload.UploadGiornoNato;
import org.springframework.beans.factory.annotation.Autowired;

import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_ANNO_NATI;
import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_GIORNO_NATI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Wed, 29-May-2019
 * Time: 22:14
 * <p>
 * Classe per la visualizzazione di una lista di prova di biografie di un particolare anno <br>
 * Viene invocata da WikiAnnoViewList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Lista dei Nati nell'anno <br>
 */
@Route(value = ROUTE_VIEW_ANNO_NATI)
public class ViewAnnoNato extends ViewAnni {

    @Autowired
    private UploadAnnoNato uploadAnnoNato;


    @Override
    public void setParameter(BeforeEvent event, String giornoIdKey) {
        super.idKey = giornoIdKey;
        this.inizia();
    }// end of method


    public void inizia() {
        anno = annoService.findById(idKey);
        uploadAnnoNato.esegueTest(anno);
        testo = uploadAnnoNato.getTesto();

        super.inizia();
    }// end of method


    protected void addTitolo() {
        String titolo = "Lista di biografie di persone nate nel " + anno.getTitolo() + "\n";
        testo = titolo + testo;
    }// end of method

}// end of class
