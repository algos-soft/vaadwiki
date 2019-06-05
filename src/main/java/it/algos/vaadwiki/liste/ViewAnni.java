package it.algos.vaadwiki.liste;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.HasUrlParameter;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwiki.modules.wiki.WikiAnnoViewList;
import it.algos.vaadwiki.modules.wiki.WikiGiornoViewList;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Wed, 29-May-2019
 * Time: 22:13
 * <p>
 * Classe astratta per la visualizzazione di una lista di prova di biografie di un particolare anno <br>
 * Viene invocata da WikiAnnoViewList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Due sottoclassi (concrete) per i Nati e per i Morti <br>
 */
public abstract class ViewAnni extends ViewListe {


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AnnoService annoService;



    protected Anno anno;



}// end of class
