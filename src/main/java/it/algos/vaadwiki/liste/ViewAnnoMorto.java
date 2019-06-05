package it.algos.vaadwiki.liste;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadwiki.upload.UploadAnnoMorto;
import it.algos.vaadwiki.upload.UploadAnnoNato;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Wed, 29-May-2019
 * Time: 22:15
 * <p>
 * Classe per la visualizzazione di una lista di prova di biografie di un particolare anno <br>
 * Viene invocata da WikiAnnoViewList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Lista dei Morti nell'anno <br>
 */
@Route(value = ROUTE_VIEW_ANNO_MORTI)
public class ViewAnnoMorto extends ViewAnni {

    @Autowired
    private UploadAnnoMorto uploadAnnoMorto;


    @Override
    public void setParameter(BeforeEvent event, String giornoIdKey) {
        super.idKey = giornoIdKey;
        this.inizia();
    }// end of method


    public void inizia() {
        anno = annoService.findById(idKey);
        uploadAnnoMorto.esegueTest(anno);
        testo = uploadAnnoMorto.getTesto();

        super.inizia();
    }// end of method


    protected void addTitolo() {
        String titolo = "Lista di biografie di persone morte nel " + anno.getTitolo() + "\n";
        testo = titolo + testo;
    }// end of method

}// end of class
