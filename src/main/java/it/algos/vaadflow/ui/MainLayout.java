package it.algos.vaadflow.ui;

import com.flowingcode.addons.applayout.AppLayout;
import com.flowingcode.addons.applayout.menu.MenuItem;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.shared.ui.LoadMode;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.EARoleType;
import it.algos.vaadflow.footer.AFooter;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadflow.service.AAnnotationService;
import it.algos.vaadflow.service.AReflectionService;
import it.algos.vaadflow.service.ATextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.algos.vaadflow.application.FlowCost.PROJECT_NAME;

/**
 * Gestore dei menu. Unico nell'applicazione (almeno finche non riesco a farne girare un altro)
 * <p>
 * Not annotated with @SpringComponent (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @HtmlImport (obbligatorio) per usare il CSS
 * Annotated with @Push (obbligatorio)
 * Annotated with @PageTitle (facoltativo)
 */
@SuppressWarnings("serial")
@HtmlImport(value = "styles/shared-styles.html", loadMode = LoadMode.INLINE)
@Push
@PageTitle(value = MainLayout.SITE_TITLE)
@Slf4j
public class MainLayout extends VerticalLayout implements RouterLayout, PageConfigurator {


    public static final String SITE_TITLE = "World Cup 2018 Stats";


    /**
     * Recupera da StaticContextAccessor una istanza della classe <br>
     * La classe deve avere l'annotation @Scope = 'singleton', and is created at the time of class loading <br>
     */
    public ALogin login = StaticContextAccessor.getBean(ALogin.class);
    private AAnnotationService annotation = AAnnotationService.getInstance();
    private AReflectionService reflection = AReflectionService.getInstance();
    private ATextService text = ATextService.getInstance();


    public MainLayout() {
        setMargin(false);
        setSpacing(false);
        setPadding(false);

        creaAllMenu();
    }// end of method


    protected void creaAllMenu() {
        String title = login.getCompany() != null ? login.getCompany().descrizione : PROJECT_NAME;
        final AppLayout appLayout = new AppLayout(null, createAvatarComponent(), title);
        ArrayList<MenuItem> listaMenu = null;
        MenuItem menu = null;
        Map<EARole, List<Class>> mappaClassi = creaMappa(FlowCost.MENU_CLAZZ_LIST);

        if (mappaClassi != null && mappaClassi.size() > 0) {
            listaMenu = new ArrayList<>();

            //--crea i menu del developer, inserendoli come sub-menu
            if (login != null && login.isDeveloper()) {
                if (mappaClassi.get(EARole.developer) != null) {
                    this.addDev(listaMenu, mappaClassi.get(EARole.developer));
                }// end of if cycle
            }// end of if cycle

            //--crea i menu di admin, inserendoli come sub-menu
            if (login != null && login.isAdmin()) {
                if (mappaClassi.get(EARole.admin) != null) {
                    this.addAdmin(listaMenu, mappaClassi.get(EARole.admin));
                }// end of if cycle
            }// end of if cycle

            //--crea gli altri menu, esclusi quelli del developer e di admin che sono già inseriti
            if (mappaClassi.get(EARole.user) != null) {
                this.addUser(listaMenu, mappaClassi.get(EARole.user));
            }// end of if cycle

            //--crea il logout
            listaMenu.add(creaMenuLogout());
//            listaMenu.add(new MenuItem("Alfa2", () -> UI.getCurrent().navigate("alfa2")));
//            listaMenu.add(new MenuItem("Beta2", () -> UI.getCurrent().navigate("beta2")));

            //--aggiunge i menu
            appLayout.setMenuItems(listaMenu.toArray(new MenuItem[listaMenu.size()]));

            //--crea la barra di bottoni, in alto a destra
            appLayout.setToolbarIconButtons(new MenuItem("Logout", "exit-to-app", () -> UI.getCurrent().getPage().executeJavaScript("location.assign('logout')")));

            this.add(appLayout);
        }// end of if cycle
    }// end of method

    private Map<EARole, List<Class>> creaMappa(List<Class> listaClassiMenu) {
        Map<EARole, List<Class>> mappa = new HashMap<>();
        ArrayList<Class> devClazzList = new ArrayList<>();
        ArrayList<Class> adminClazzList = new ArrayList<>();
        ArrayList<Class> utenteClazzList = new ArrayList<>();
        EARoleType type = null;

        for (Class viewClazz : listaClassiMenu) {
            type = annotation.getViewRoleType(viewClazz);

            switch (type) {
                case developer:
                    devClazzList.add(viewClazz);
                    break;
                case admin:
                    adminClazzList.add(viewClazz);
                    break;
                case user:
                    utenteClazzList.add(viewClazz);
                    break;
                default:
                    utenteClazzList.add(viewClazz);
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of for cycle

        mappa.put(EARole.developer, devClazzList);
        mappa.put(EARole.admin, adminClazzList);
        mappa.put(EARole.user, utenteClazzList);

        return mappa;
    }// end of method


    private void addDev(ArrayList<MenuItem> listaMenu, List<Class> devClazzList) {
        MenuItem developerItem;
        MenuItem[] matrice;
        ArrayList<MenuItem> listaSubMenuDev = new ArrayList<>();
        MenuItem menu;

        for (Class viewClazz : devClazzList) {
            menu = creaMenu(viewClazz);
            listaSubMenuDev.add(menu);
        }// end of for cycle

        matrice = listaSubMenuDev.toArray(new MenuItem[listaSubMenuDev.size()]);
        developerItem = new MenuItem("Developer", "build", matrice);
        listaMenu.add(developerItem);
    }// end of method


    private void addAdmin(ArrayList<MenuItem> listaMenu, List<Class> adminClazzList) {
        MenuItem adminItem;
        MenuItem[] matrice;
        ArrayList<MenuItem> listaSubMenuDev = new ArrayList<>();
        MenuItem menu;

        for (Class viewClazz : adminClazzList) {
            menu = creaMenu(viewClazz);
            listaSubMenuDev.add(menu);
        }// end of for cycle

        matrice = listaSubMenuDev.toArray(new MenuItem[listaSubMenuDev.size()]);
        adminItem = new MenuItem("Admin", "apps", matrice);
        listaMenu.add(adminItem);
    }// end of method


    private void addUser(ArrayList<MenuItem> listaMenu, List<Class> userClazzList) {
        MenuItem menu;

        for (Class viewClazz : userClazzList) {
            menu = creaMenu(viewClazz);
            listaMenu.add(menu);
        }// end of for cycle

    }// end of method


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


    protected MenuItem creaMenu(Class<? extends AViewList> viewClazz) {
        String linkRoute;
        String menuName;
        String icon;

        linkRoute = annotation.getQualifierName(viewClazz);
        menuName = annotation.getViewName(viewClazz);
        menuName = text.primaMaiuscola(menuName);
        icon = reflection.getIronIcon(viewClazz);

        return new MenuItem(menuName, icon, () -> UI.getCurrent().navigate(linkRoute));
    }// end of method


    protected MenuItem creaMenuLogout() {
        MenuItem menuItem = null;
        String linkRoute = "login";
        String menuName = "Logout";

        menuItem = new MenuItem(menuName, "exit-to-app",() -> UI.getCurrent().getPage().executeJavaScript("location.assign('logout')"));
        return menuItem;
    }// end of method


    @Override
    protected void onAttach(final AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }// end of method

    @Override
    public void configurePage(final InitialPageSettings settings) {
        settings.addMetaTag("viewport", "width=device-width, initial-scale=1.0");
        settings.addLink("shortcut icon", "/frontend/images/favicons/favicon-96x96.png");
        settings.addLink("manifest", "/manifest.json");
        settings.addFavIcon("icon", "/frontend/images/favicons/favicon-96x96.png", "96x96");
    }// end of method

}// end of class