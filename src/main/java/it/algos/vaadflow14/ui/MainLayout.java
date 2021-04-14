package it.algos.vaadflow14.ui;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.tabs.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.*;
import com.vaadin.flow.theme.*;
import com.vaadin.flow.theme.lumo.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.service.*;
import it.algos.vaadflow14.ui.topbar.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: lun, 27-apr-2020
 * Time: 21:03
 * The main view is a top-level placeholder for other views.
 */
@Theme(value = Lumo.class)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/menu-buttons.css", themeFor = "vaadin-button")
public class MainLayout extends AppLayout {


    //    /**
    //     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
    //     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
    //     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
    //     */
    //    @Autowired
    //    public AVaadinService vaadin;

    //    /**
    //     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
    //     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
    //     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
    //     */
    //    @Autowired
    //    public ATextService text;

    //    /**
    //     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
    //     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
    //     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
    //     */
    //    @Autowired
    //    public ALogin login;

    private Tabs menu;


    private Button logoutButton;


    /**
     * Costruttore con alcune classi di servizio iniettate. <br>
     *
     * @param layoutService the layout service
     */
    @Autowired
    public MainLayout(AVaadinService vaadinService, ALayoutService layoutService,TopbarComponent topbarComponent) {
        setPrimarySection(Section.DRAWER);

        //--allinea il login alla sessione
        //--lo crea se manca e lo rende disponibile a tutti
        if (FlowVar.usaSecurity) {
            vaadinService.fixLogin();
        }

        addToNavbar(true, new DrawerToggle());

        menu = layoutService.creaMenuTabs();
        addToDrawer(menu);

        this.setDrawerOpened(false); //@todo Creare una preferenza e sostituirla qui

        addToNavbar(topbarComponent);

        // questo non lo metterei
        if (FlowVar.usaSecurity) {
            logoutButton = layoutService.creaLogoutButton();//@todo Va levato quando funziona nella barra a destra
            logoutButton.addClickListener(e -> logout());
            menu.add(logoutButton);
        }

    }



    private void logout() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().getPage().executeJavaScript("location.assign('logout')");
        //        System.exit(0); //@todo Creare una preferenza e sostituirla qui
    }


    //    private void registerAdminViewIfApplicable(AccessControl accessControl) {
    //        // register the admin view dynamically only for any admin user logged in
    //        if (accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)
    //                && !RouteConfiguration.forSessionScope()
    //                .isRouteRegistered(AdminView.class)) {
    //            RouteConfiguration.forSessionScope().setRoute(AdminView.VIEW_NAME,
    //                    AdminView.class, MainLayout.class);
    //            // as logout will purge the session route registry, no need to
    //            // unregister the view on logout
    //        }
    //    }


    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        selectTab();
    }


    private void selectTab() {
        String target = RouteConfiguration.forSessionScope().getUrl(getContent().getClass());
        Optional<Component> tabToSelect = menu.getChildren().filter(tab -> {
            Component child = tab.getChildren().findFirst().get();
            return child instanceof RouterLink && ((RouterLink) child).getHref().equals(target);
        }).findFirst();
        tabToSelect.ifPresent(tab -> menu.setSelectedTab((Tab) tab));
    }


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // User can quickly activate logout with Ctrl+E (Exit)
        attachEvent.getUI().addShortcutListener(() -> logout(), Key.KEY_E, KeyModifier.CONTROL);

        //        // add the admin view menu item if user has admin role
        //        final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();
        //        if (accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
        //
        //            // Create extra navigation target for admins
        //            registerAdminViewIfApplicable(accessControl);
        //
        //            // The link can only be created now, because the RouterLink checks
        //            // that the target is valid.
        //            addToDrawer(createMenuLink(AdminView.class, AdminView.VIEW_NAME,
        //                    VaadinIcon.DOCTOR.create()));
        //        }

        // Finally, add logout button for all users
        //        addToDrawer(logoutButton);

    }

}

