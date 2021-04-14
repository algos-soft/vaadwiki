package it.algos.vaadflow14.ui.service;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.tabs.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.service.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.*;


/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 20-set-2019 18.19.24 <br>
 * <p>
 * Classe di servizio <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ALayoutService extends AAbstractService {

    //    /**
    //     * Menu drawer toggle a sinistra. <br>
    //     *
    //     * @return the drawer toggle
    //     */
    //    public DrawerToggle creaDrawer() {
    //        final DrawerToggle drawerToggle = new DrawerToggle();
    //
    //        //@todo Controllare
    //        //        drawerToggle.addClassName("menu-toggle");
    //
    //        return drawerToggle;
    //    }


    /**
     * image, logo. <br>
     *
     * @return the horizontal layout
     */
    public HorizontalLayout creaTop() {
        final HorizontalLayout top = new HorizontalLayout();
        top.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        top.setClassName("menu-header");

        //@todo Funzionalità ancora da implementare
        //        // Note! Image resource url is resolved here as it is dependent on the
        //        // execution mode (development or production) and browser ES level support
        //        final String resolvedImage = VaadinService.getCurrent().resolveResource(pathLogo);
        //
        //        final Image image = new Image(resolvedImage, "");
        //        image.setHeight("24px");
        //        image.setWidth("24px");
        String check = FlowVar.projectName;
        final Label title = new Label(check);
        //        top.add(image, title);//@todo Funzionalità ancora da implementare
        top.add(title);

        return top;
    }


    /**
     * Crea menu tabs tabs.
     *
     * @return the tabs
     */
    public Tabs creaMenuTabs() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(getAvailableMenu());
        tabs.setSizeFull();

        return tabs;
    }


    /**
     * Get available menu tab [ ].
     *
     * @return the tab [ ]
     */
    public Tab[] getAvailableMenu() {
        final List<Tab> tabs = new ArrayList<>();
        Tab tab = null;
        LinkedHashMap<String, Tab> mappaTab;
        String menuKey;
        String message;
        List<String> listaNomi = new ArrayList<>();

        logger.logDebug(AETypeLog.checkMenu, VUOTA);
        if (array.isAllValid(FlowVar.menuRouteList)) {
            for (Class<?> menuClazz : FlowVar.menuRouteList) {
                mappaTab = createTab(menuClazz);
                if (mappaTab != null && mappaTab.size() == 1) {
                    menuKey = (String) mappaTab.keySet().toArray()[0];
                    tab = mappaTab.get(menuKey);
                    tabs.add(tab);
                    listaNomi.add(menuKey);
                }
            }
        }
        message = String.format("Ci sono %d righe di menu visibili", listaNomi.size());
        logger.logDebug(AETypeLog.checkMenu, message);
        logger.logDebug(AETypeLog.checkMenu, array.toStringa(listaNomi));

        return tabs.toArray(new Tab[tabs.size()]);
    }


    private LinkedHashMap<String, Tab> createTab(Class menuClazz) {
        LinkedHashMap<String, Tab> mappaTab = new LinkedHashMap<>();
        LinkedHashMap<String, RouterLink> mappaRouters = createMenuLink(menuClazz);
        String menuKey;
        RouterLink routerLink;
        Tab tab;

        if (mappaRouters != null && mappaRouters.size() == 1) {
            menuKey = (String) mappaRouters.keySet().toArray()[0];
            routerLink = mappaRouters.get(menuKey);
            tab = new Tab(routerLink);
            mappaTab.put(menuKey, tab);
            return mappaTab;
        }
        else {
            return null;
        }
    }


    /**
     * Nel menu metto tutte le classi che hanno un'annotation @Route <br>
     * Le classi List specifiche; link diretto alla classe (tipo FatturaLogicList) <br>
     * Le classi List implicite; link alla classe GenericLogicList con parametro canonicalName della AEntity <br>
     * Altre classi grafiche; link diretto alla classe <br>
     *
     * @param menuClazz inserito da FlowBoot.fixMenuRoutes() e sue sottoclassi
     */
    private LinkedHashMap<String, RouterLink> createMenuLink(final Class menuClazz) {
        LinkedHashMap<String, RouterLink> mappaRouter = new LinkedHashMap<>();
        RouterLink routerLink = null;
        QueryParameters query = null;
        String packageName = VUOTA;
        Icon icon = annotation.getMenuIcon(menuClazz);
        String menuName = annotation.getMenuName(menuClazz);
        String message;
        String canonicalName;
        Class listClazz = null;

        //--se è una route, va direttamente
        if (annotation.isRouteView(menuClazz) && Component.class.isAssignableFrom(menuClazz)) {
            routerLink = new RouterLink(null, menuClazz);
            message = String.format("La classe %s è una @Route e viene inserita nel menu", menuClazz.getSimpleName());
            logger.logDebug(AETypeLog.checkMenu, message);
        }
        else {
            //--se è una entity, cerca la classe specifica xxxLogicList altrimenti usa GenericLogicList
            if (annotation.isEntityClass(menuClazz)) {
                listClazz = classService.getLogicListClassFromEntityClazz(menuClazz);

                //--controllo che la classe specifica xxxLogicList esista e che contenga @Route
                if (listClazz != null) {
                    if (annotation.isRouteView(listClazz)) {
                        routerLink = new RouterLink(null, listClazz);
                        message = String.format("Nel package %s esiste la classe %s che è una @Route", packageName, listClazz.getSimpleName());
                        logger.logDebug(AETypeLog.checkMenu, message);
                    }
                    else {
                        message = String.format("Nel package %s la classe %s non ha l'Annotation @Route e pertanto non viene inserita nel menu", packageName, listClazz.getSimpleName());
                        logger.logDebug(AETypeLog.checkMenu, message);
                    }
                }
                else {
                    //--provo a creare la classe GenericLogicList
                    query = route.getQueryList(menuClazz);
                    routerLink = new RouterLink(null, GenericLogicList.class);
                    routerLink.setQueryParameters(query);
                    canonicalName = menuClazz.getCanonicalName();
                    message = String.format("Nel package %s non esiste la classe %s e uso GenericLogicList", packageName, fileService.estraeClasseFinale(canonicalName));
                    logger.logDebug(AETypeLog.checkMenu, message);
                }
            }
            else {
                message = String.format("La classe %s NON è ne una AEntity ne una @Route e pertanto non viene inserita nel menu", menuClazz.getSimpleName());
                logger.logDebug(AETypeLog.checkMenu, message);
            }
        }

        if (routerLink == null) {
            return null;
        }
        routerLink.setClassName("menu-link");

        if (icon != null) {
            routerLink.add(icon);
            //            icon.setSize("24px");
        }

        if (text.isValid(menuName)) {
            menuName = text.primaMaiuscola(menuName);
            Span span = new Span();
            span.getElement().setProperty("innerHTML", FlowCost.HTLM_SPAZIO + menuName);
            routerLink.add(span);
        }

        mappaRouter.put(menuName, routerLink);
        return mappaRouter;
    }


    /**
     * Create profile button but don't add it yet; admin view might be added. <br>
     * in between (see #onAttach()) <br>
     *
     * @return the button
     */
    public Button creaProfileButton() {
        Button profileButton;

        profileButton = createMenuButton("Profile", VaadinIcon.EDIT.create());
        profileButton.getElement().setAttribute("title", "Profile (Ctrl+E)");

        return profileButton;
    }

    /**
     * Create logout button but don't add it yet; admin view might be added. <br>
     * in between (see #onAttach()) <br>
     *
     * @return the button
     */
    public Button creaLogoutButton() {
        Button logoutButton;

        logoutButton = createMenuButton("Logout", VaadinIcon.SIGN_OUT.create());
        logoutButton.getElement().setAttribute("title", "Logout (Ctrl+L)");

        return logoutButton;
    }

    //    private void logout() {
    //        VaadinSession.getCurrent().getSession().invalidate();
    //        UI.getCurrent().getPage().executeJavaScript("location.assign('logout')");
    //    }

    private Button createMenuButton(String caption, Icon icon) {
        final Button routerButton = new Button(caption);
        routerButton.setClassName("menu-button");
        routerButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        routerButton.setIcon(icon);
        icon.setSize("24px");

        return routerButton;
    }

}