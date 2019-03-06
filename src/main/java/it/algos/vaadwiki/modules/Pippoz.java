package it.algos.vaadwiki.modules;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadflow.ui.AViewList;
import it.algos.vaadflow.ui.IAView;
import it.algos.vaadflow.ui.menu.APopupMenu;
import it.algos.vaadwiki.WikiLayout;
import it.algos.vaadwiki.modules.attivita.AttivitaViewList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 03-mar-2019
 * Time: 18:17
 */
@Route(value = "pippoz", layout = WikiLayout.class)
@Qualifier("pippoz")
@Slf4j
public class Pippoz extends AViewList {

    private Image immagine = new Image("frontend/images/ambulanza.jpg", "vaadin");

    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.BOAT;


    /**
     * Costruttore @Autowired <br>
     */
    @Autowired
    public Pippoz() {
        super(null,null);
        this.add(StaticContextAccessor.getBean(APopupMenu.class).getComp());
        add(immagine);
    }// end of Spring constructor


    @Override
    public String getMenuName() {
        return null;
    }


    @Override
    public void updateView() {

    }

}// end of class
