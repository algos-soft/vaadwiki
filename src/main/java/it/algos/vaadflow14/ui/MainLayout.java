package it.algos.vaadflow14.ui;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.tabs.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.service.*;
import it.algos.vaadflow14.ui.topbar.*;
import org.springframework.beans.factory.annotation.*;

import javax.annotation.*;
import java.util.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: lun, 27-apr-2020
 * Time: 21:03
 * The main view is a top-level placeholder for other views.
 */
//@Theme(value = Lumo.class)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/menu-buttons.css", themeFor = "vaadin-button")
public class MainLayout extends AppLayout {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AVaadinService vaadinService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ALayoutService layoutService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AnnotationService annotationService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService textService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public PreferenzaService preferenzaService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ALogService logger;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ClassService classService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ARouteService routeService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    TopbarComponent topbarComponent;

    //    /**
    //     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
    //     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
    //     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
    //     */
    //    @Autowired
    //    public ALogin login;

    private H1 viewTitle;

    private Tabs menu;


    private Button logoutButton;



    /**
     * Costruttore. <br>
     */
    public MainLayout() {

    }

    /**
     * Performing the initialization in a constructor is not suggested <br>
     * as the state of the UI is not properly set up when the constructor is invoked. <br>
     * <p>
     * La injection viene fatta da SpringBoot solo alla fine del metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * L'istanza può essere creata con  appContext.getBean(xxxClass.class);  oppure con @Autowired <br>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());

        //        //--allinea il login alla sessione
        //        //--lo crea se manca e lo rende disponibile a tutti
        //        if (FlowVar.usaSecurity) {
        //            vaadinService.fixLogin();
        //        }

        //        addToNavbar(true, new DrawerToggle());
        //        menu = layoutService.creaMenuTabs();
        //        addToDrawer(menu);
        //        this.setDrawerOpened(false); //@todo Creare una preferenza e sostituirla qui
        //        addToNavbar(topbarComponent);

        //        // questo non lo metterei
        //        if (FlowVar.usaSecurity) {
        //            logoutButton = layoutService.creaLogoutButton();//@todo Va levato quando funziona nella barra a destra
        //            logoutButton.addClickListener(e -> logout());
        //            menu.add(logoutButton);
        //        }
    }


    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassName("text-secondary");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("m-0", "text-l");

        Header header = new Header(toggle, viewTitle);
        header.addClassNames("bg-base", "border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-center", "w-full");
        return header;
    }


    private Component createDrawerContent() {
        H2 appName = new H2(FlowVar.projectNameUpper);
        appName.addClassNames("flex", "items-center", "h-xl", "m-0", "px-m", "text-m");

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(appName, createNavigation());
        section.addClassNames("flex", "flex-col", "items-stretch", "max-h-full", "min-h-full");
        return section;
    }


    private Nav createNavigation() {
        Nav nav = new Nav();
        nav.addClassNames("border-b", "border-contrast-10", "flex-grow", "overflow-auto");
        nav.getElement().setAttribute("aria-labelledby", "views");

        H3 views = new H3(FlowVar.projectNameUpper);
        views.addClassNames("flex", "h-m", "items-center", "mx-m", "my-0", "text-s", "text-tertiary");
        views.setId("views");

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames("list-none", "m-0", "p-0");
        nav.add(list);

        List<RouterLink> lista = createLinks();
        if (lista != null) {
            for (RouterLink link : lista) {
                ListItem item = new ListItem(link);
                list.add(item);
            }
        }

        return nav;
    }

    private List<RouterLink> createLinks() {
        List<RouterLink> links = new ArrayList<>();
        RouterLink link;

        //        MenuItemInfo[] menuItems = new MenuItemInfo[]{
        //                new MenuItemInfo("Via", "la la-eye-slash", ViaLogicList.class),
        //                new MenuItemInfo("Mese", "edit", MeseLogicList.class),
        //                new MenuItemInfo("Continente", "la edit", ContinenteLogicList.class)
        //        };

        for (Class clazz : FlowVar.menuRouteList) {
            try {
                links.add(createLink(clazz));
            } catch (AlgosException unErrore) {
                logger.info(unErrore, getClass(), "createLinks");
            }
        }
        return links;
    }

    private RouterLink createLink(final Class clazz) throws AlgosException {
        RouterLink link = new RouterLink();
        String message = VUOTA;
        Class viewClazz = clazz;
        Class entityClazz = null;
        QueryParameters query;
        Icon icon = null;
        Span spanIcon = null;
        String text;
        Span spanText;
        link.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary");

        if (clazz == null) {
            throw AlgosException.stack("Manca la clazz", getClass(), "createLink");
        }

        text = annotationService.getMenuName(clazz);
        spanText = new Span(text);
        spanText.addClassNames("font-medium", "text-s");
        link.add(spanText);
        try {
            if (annotationService.isRouteView(viewClazz)) {
                link.setRoute(viewClazz);
            }
            else {
                try {
                    viewClazz = classService.getLogicListClassFromEntityClazz(clazz);
                } catch (AlgosException unErrore) {
                    throw AlgosException.stack(unErrore, this.getClass(), "createLink");
                }

                if (viewClazz == null) {
                    //--provo a creare la classe GenericLogicList
                    query = routeService.getQueryList(clazz);
                    link.setQueryParameters(query);
                    viewClazz = GenericLogicList.class;
                    message = String.format("Non esiste la classe %s e uso GenericLogicList", clazz.getSimpleName());
                    logger.logDebug(AETypeLog.checkMenu, message);
                }

                try {
                    link.setRoute(viewClazz);
                } catch (Exception unErrore) {
                    throw AlgosException.stack(String.format("Non sono riuscito a creare una @Route verso %s", viewClazz.getSimpleName()), getClass(), "createLink");
                }
            }
        } catch (Exception unErrore) {
            throw AlgosException.stack(unErrore, getClass(), "createLink");
        }

        // ricerca dell'icona
        try {
            entityClazz = classService.getEntityClazzFromClazz(clazz);
        } catch (AlgosException unErrore) {
        }
        if (entityClazz != null) {
            icon = annotationService.getMenuIcon(entityClazz);
        }
        if (icon != null) {
            spanIcon = new Span();
            spanIcon.addClassNames("me-s", "text-l");
            link.addComponentAtIndex(0, icon);
        }

        return link;
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

        if (AEPreferenza.usaEvidenziaMenuSelezionato.is()) {
            //            selectTab();
        }

        viewTitle.setText(getCurrentPageTitle());
    }


    private void selectTab() {
        String target = RouteConfiguration.forSessionScope().getUrl(getContent().getClass());
        Optional<Component> tabToSelect = menu.getChildren().filter(tab -> {
            Component child = tab.getChildren().findFirst().get();
            return child instanceof RouterLink && ((RouterLink) child).getHref().equals(target);
        }).findFirst();
        tabToSelect.ifPresent(tab -> menu.setSelectedTab((Tab) tab));
    }


    /**
     * Prima cerca l'Annotation standard nella view <br>
     * Se la view è un'istanza di GenericLogicList, cerca il nome del menu nella EntityClass <br>
     */
    private String getCurrentPageTitle() {
        String menuName;
        PageTitle title;
        Class<?> clazz;

        clazz = getContent().getClass();
        title = clazz.getAnnotation(PageTitle.class);
        menuName = title == null ? VUOTA : title.value();

        return menuName;
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

