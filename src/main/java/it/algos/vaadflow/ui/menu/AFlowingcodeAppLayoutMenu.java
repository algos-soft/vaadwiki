package it.algos.vaadflow.ui.menu;

import com.flowingcode.addons.applayout.AppLayout;
import com.flowingcode.addons.applayout.menu.MenuItem;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.shared.ui.LoadMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.ui.AViewList;
import it.algos.vaadflow.ui.IAView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Map;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 23-dic-2018
 * Time: 08:59
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@HtmlImport(value = "styles/algos-styles.html", loadMode = LoadMode.INLINE)
@Slf4j
public class AFlowingcodeAppLayoutMenu extends AMenu {

    private AppLayout appLayout;

    private ArrayList<MenuItem> listaMenu;


    /**
     * Costruttore @Autowired <br>
     */
    @Autowired
    public AFlowingcodeAppLayoutMenu() {
        super();
    }// end of Spring constructor


    /**
     * Costruisce il componente di questa classe concreta di menu <br>
     * Sovrascritto
     */
    @Override
    protected void inizia() {
        appLayout = new AppLayout(null, createAvatarComponent(), FlowCost.LAYOUT_TITLE);
        listaMenu = new ArrayList<>();
    }// end of method


    /**
     * Eventuali item di menu, se collegato come sviluppatore
     */
    @Override
    protected void creaMenuDeveloper() {
        MenuItem developerItem;
        MenuItem[] matrice;
        MenuItem menu;
        ArrayList<MenuItem> listaSubMenuDev = new ArrayList<>();

        if (array.isValid(devClazzList)) {
            for (Class viewClazz : devClazzList) {
                menu = creaItem(viewClazz);
                listaSubMenuDev.add(menu);
            }// end of for cycle
        }// end of if cycle

        //--crea i menu del developer, inserendoli come sub-menu
        this.addCronoDev(listaSubMenuDev, mappaClassi);

        matrice = listaSubMenuDev.toArray(new MenuItem[listaSubMenuDev.size()]);
        developerItem = new MenuItem("Developer", "build", matrice);
        listaMenu.add(developerItem);
    }// end of method

    /**
     * Eventuali item di menu, se collegato come admin
     */
    protected void creaMenuAdmin() {
        MenuItem adminItem;
        MenuItem[] matrice;
        MenuItem menu;
        ArrayList<MenuItem> listaSubMenuAdmin = new ArrayList<>();

        if (array.isValid(adminClazzList)) {
            for (Class viewClazz : adminClazzList) {
                menu = creaItem(viewClazz);
                listaSubMenuAdmin.add(menu);
            }// end of for cycle
        }// end of if cycle

        matrice = listaSubMenuAdmin.toArray(new MenuItem[listaSubMenuAdmin.size()]);
        adminItem = new MenuItem("Admin", "build", matrice);
        listaMenu.add(adminItem);
    }// end of method

    /**
     * Crea i menu crono, visibili solo per il developer <br>
     * Li incapsula come sub-menu <br>
     */
    private void addCronoDev(ArrayList<MenuItem> listaMenuDev, Map<String, ArrayList<Class<? extends IAView>>> mappaClassi) {
        MenuItem developerItem;
        MenuItem[] matrice;
        ArrayList<MenuItem> listaSubMenuCronoDev = new ArrayList<>();
        MenuItem menu;

        if (array.isValid(cronoClazzList)) {
            for (Class viewClazz : cronoClazzList) {
                menu = creaItem(viewClazz);
                listaSubMenuCronoDev.add(menu);
            }// end of for cycle
        }// end of if cycle

        if (array.isValid(cronoClazzList)) {
            matrice = listaSubMenuCronoDev.toArray(new MenuItem[listaSubMenuCronoDev.size()]);
            developerItem = new MenuItem("Crono", "build", matrice);
            listaMenuDev.add(developerItem);
        }// end of if cycle
    }// end of method


    /**
     * Item di menu logout sempre presente
     */
    protected void creaMenuLogout() {
        MenuItem item = new MenuItem("Logout", "exit-to-app", () -> goTo("logout"));
        listaMenu.add(item);
        appLayout.setToolbarIconButtons(item);
    }// end of method


    /**
     * Regolazioni finali
     */
    @Override
    protected void fixFinali() {
        appLayout.setMenuItems(listaMenu.toArray(new MenuItem[listaMenu.size()]));
    }// end of method


    /**
     * Crea il singolo item di menu <br>
     */
    @Override
    protected MenuItem creaItem(Class<? extends AViewList> viewClazz) {
        String menuName = annotation.getMenuName(viewClazz);
        String icon = reflection.getIronIcon(viewClazz);
        String linkRoute = annotation.getQualifierName(viewClazz);

        return new MenuItem(menuName, icon, () -> goTo(linkRoute));
    }// end of method


    /**
     * Crea il singolo item di menu <br>
     *
     * @param viewClazz
     */
    @Override
    protected void addItem(Class<? extends AViewList> viewClazz) {
        MenuItem item = creaItem(viewClazz);
        listaMenu.add(item);
    }// end of method


    /**
     * Crea l'immagine (se esiste) dell'utente collegato <br>
     */
    private Component createAvatarComponent() {
        Div container = new Div();
        H5 userName;
        container.getElement().setAttribute("style", "text-align: center;");
        Image i = new Image("/frontend/images/avatar.png", "avatar");
        i.getElement().setAttribute("style", "width: 60px; margin-top:10px");

        if (login != null && login.getUtente() != null) {
            container.add(i, new H5(login.getUtente().userName));
        } else {
            container.add(i);
        }// end of if/else cycle

        return container;
    }// end of method


    @Override
    protected void onAttach(final AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }// end of method


    @Override
    public Component getComp() {
        return appLayout;
    }// end of method

}// end of class
