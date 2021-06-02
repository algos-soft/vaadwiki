package it.algos.vaadflow14.ui.topbar;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.contextmenu.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.menubar.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.server.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.login.*;
import it.algos.vaadflow14.backend.packages.company.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import javax.annotation.*;

/**
 * Componente per la barra superiore della finestra <br>
 * Se è FlowVar.usaSecurity=false <br>
 * 1 - immagine (opzionale) dell' applicazione in FlowVar.pathLogo; se manca sostituisce un' immagine di default <br>
 * 2 - nome (obbligatorio) dell' applicazione previsto in FlowVar.projectName <br>
 * 3 - descrizione (opzionale) dell' applicazione prevista in FlowVar.projectDescrizione; in seconda riga e più piccola <br>
 * <p>
 * Se è FlowVar.usaSecurity=true e FlowVar.usaCompany=false <br>
 * Tre oggetti da sinistra a destra (occupano tutto lo spazio disponibile, col componente centrale che si espande):
 * 1 - immagine (opzionale); se manca sostituisce un' immagine di default <br>
 * 2 - descrizione della company/applicazione (obbligatorio);
 * se multiCompany una sigla o una descrizione della company,
 * altrimenti una sigla o una descrizione dell' applicazione stessa
 * 3 - utente loggato (opzionale); se multiCompany l' username dell' utente loggato
 * <p>
 * Se è FlowVar.usaSecurity=true e FlowVar.usaCompany=true <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TopbarComponent extends HorizontalLayout {

    //    private Image image;

    //    private Label label;

    //    public MenuBar menuUser;

    //    public SubMenu projectSubMenu;

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

    protected ALogin login;

    protected boolean usaProfile; //@todo Creare una preferenza e sostituirla qui

    protected MenuItem itemUser;

    private LogoutListener logoutListener;

    private ProfileListener profileListener;

    //--property
    private String titolo;

    //--property
    private String sottotitolo;

    //    private AMenuService menuService;

    //--property
    private String nickName;


    /**
     * Costruttore senza parametri <br>
     * //     * L' istanza viene costruita con appContext.getBean(TopbarComponent.class) <br>
     */
    public TopbarComponent() {
    } // end of SpringBoot constructor


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le (eventuali) istanze @Autowired <br>
     * Questo metodo viene chiamato subito dopo che il framework ha terminato l' init() implicito <br>
     * del costruttore e PRIMA di qualsiasi altro metodo <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l' ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
        this.initView();
    }


    /**
     * Creazione dei componenti grafici <br>
     */
    protected void initView() {
        String style;
        Image logo = null;
        Button device = null;
        Div divTitolo = null;
        MenuBar menuUser = null;
        login = vaadinService.getLogin();

        setWidth("100%");
        setDefaultVerticalComponentAlignment(Alignment.CENTER);
        style = "display:inline-flex; width:100%; flex-direction:row; padding-left:0em; padding-top:0em; padding-bottom:0em; padding-right:1em; align-items:center";
        this.getElement().setAttribute("style", style);

        //--Preferenze specifiche
        this.fixPreferenze();

        //--Immagine facoltativa
        logo = this.fixLogo();

        //--titolo
        divTitolo = this.fixTitolo();

        //--controllo del browser collegato
        device = this.fixDevice();

        //--menu utente eventuale
        menuUser = fixMenuUser();

        Div elasticSpacer = new Div();
        elasticSpacer.getStyle().set("flex-grow", "1");
        if (menuUser != null) {
            this.add(logo, divTitolo, elasticSpacer, menuUser);
        }
        else {
            this.add(logo, divTitolo);
        }

        //--giustifica a sinistra ed a destra
        //setFlexGrow(0, image);
        //setFlexGrow(1, label);
        //if (menuUser != null) {
        //    setFlexGrow(0, menuUser);
        //}

    }


    /**
     * Logo della pagina (facoltativo) <br>
     */
    private Image fixLogo() {
        Image image = null;

        //@todo Creare una preferenza e sostituirla qui
        image = new Image(FlowVar.pathLogo, "Algos");
        image.setHeight("9mm");

        return image;
    }


    /**
     * Titolo della pagina <br>
     * <p>
     * Se è FlowVar.usaSecurity=false, su due righe di grandezza diversa (la seconda più piccola) <br>
     * 1 - nome (obbligatorio) dell' applicazione previsto in FlowVar.projectName <br>
     * 2 - descrizione (opzionale) dell' applicazione prevista in FlowVar.projectDescrizione <br>
     */
    private Div fixTitolo() {
        Div div = new Div();
        Company company;
        String commonStyle = "line-height:120%; white-space:nowrap; overflow:hidden; color:" + LUMO_PRIMARY_COLOR;
        Label primaRiga = new Label(FlowVar.projectNameUpper);
        Label secondaRiga = new Label(FlowVar.projectDescrizione);

        if (FlowVar.usaCompany) {
            company = login != null ? login.getCompany() : null;
            if (company != null) {
                primaRiga = new Label(company.getCode());
                secondaRiga = new Label(company.getDescrizione());
            }
        }
        else {
        }
        div.getElement().setAttribute("style", "display:flex; flex-direction:column; min-width:2em");

        primaRiga.getElement().setAttribute("style", commonStyle);
        primaRiga.getStyle().set("font-size", "120%");
        primaRiga.getStyle().set("font-weight", "bold");

        secondaRiga.getElement().setAttribute("style", commonStyle);
        secondaRiga.getStyle().set("font-size", "70%");

        div.add(primaRiga);
        div.add(secondaRiga);

        return div;
    }

    /**
     * Logo del tipo di browser collegato (facoltativo) <br>
     */
    private Button fixDevice() {
        Button button = new Button();
        VaadinSession vaadSession = VaadinSession.getCurrent();
        if (vaadSession != null) {
            button.setText("Mobile");
        }
        else {
            button.setText("Desktop");
        }

        //        FontAwesome.Brands.Icon image = null;
        //        image=FontAwesome.Brands.FIREFOX.create();
        return button;
    }

    private MenuBar fixMenuUser() {
        MenuBar menuUser = null;
        MenuItem itemUser;
        SubMenu projectSubMenu;

        Button profileButton;
        Button logoutButton;

        if (FlowVar.usaSecurity && vaadinService != null && vaadinService.isLogin()) {
            menuUser = new MenuBar();
            menuUser.setOpenOnHover(true);

            if (login != null) {
                itemUser = menuUser.addItem(login.getUtente().username);
                itemUser.addComponentAsFirst(getIcon());
                projectSubMenu = itemUser.getSubMenu();

                if (itemUser != null) {
                    if (login.isDeveloper()) {
                        itemUser.getElement().getStyle().set("color", "red");
                    }
                    else {
                        if (login.isAdmin()) {
                            itemUser.getElement().getStyle().set("color", "blue");
                        }
                        else {
                            itemUser.getElement().getStyle().set("color", "green");
                        }
                    }
                }

                if (usaProfile) {
                    profileButton = layoutService.creaProfileButton();
                    projectSubMenu.addItem(profileButton);
                    profileButton.addClickListener(e -> profile());
                }

                logoutButton = layoutService.creaLogoutButton();
                projectSubMenu.addItem(logoutButton);
                logoutButton.addClickListener(e -> logout());
            }
        }

        return menuUser;
    }


    /**
     * Preferenze <br>
     * Può essere sovrascritto, per modificare le preferenze standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        this.usaProfile = true;
    }


    protected Icon getIcon() {
        Icon icon = new Icon(VaadinIcon.ABACUS);

        if (login != null) {
            icon = new Icon(VaadinIcon.USER);
            if (login.isDeveloper()) {
                icon = new Icon(VaadinIcon.MAGIC);
            }
            if (login.isAdmin()) {
                icon = new Icon(VaadinIcon.SPECIALIST);
            }
        }

        return icon;
    }


    private void profile() {
    }


    private void logout() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().getPage().executeJavaScript("location.assign('logout')");
    }


    public void setLogoutListener(LogoutListener listener) {
        this.logoutListener = listener;
    }


    public void setProfileListener(ProfileListener listener) {
        this.profileListener = listener;
    }


    public interface LogoutListener {

        void logout();

    }


    public interface ProfileListener {

        void profile();

    }

}
