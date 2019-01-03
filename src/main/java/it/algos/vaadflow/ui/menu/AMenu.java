package it.algos.vaadflow.ui.menu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Div;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.*;
import it.algos.vaadflow.ui.AViewList;
import it.algos.vaadflow.ui.IAView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

import static it.algos.vaadflow.ui.MainLayout.KEY_MAPPA_CRONO;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 21-dic-2018
 * Time: 18:02
 * <p>
 * Classe per la gestione di una barra di menu, generale per tutta l'applicazione.
 * Può essere realizzato in diverse modalità, che restano comunque trasparenti per gli utilizzatori
 */
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public abstract class AMenu extends Div implements IAMenu {

    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe e usato come libreria <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    protected AAnnotationService annotation = AAnnotationService.getInstance();

    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe e usato come libreria <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    protected AReflectionService reflection = AReflectionService.getInstance();

    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe e usato come libreria <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    protected ATextService text = ATextService.getInstance();

    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe e usato come libreria <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    protected AArrayService array = AArrayService.getInstance();


    /**
     * Service (@Scope = 'singleton') iniettato da StaticContextAccessor e usato come libreria <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    protected AVaadinService vaadinService = StaticContextAccessor.getBean(AVaadinService.class);

    /**
     * Recuperato dalla sessione, quando la @route fa partire la UI. <br>
     * Viene regolato nel service specifico (AVaadinService) <br>
     */
    protected AContext context;

    /**
     * Mantenuto nel 'context' <br>
     */
    protected ALogin login;

    protected Map<String, ArrayList<Class<? extends IAView>>> mappaClassi = creaMappa(FlowCost.MENU_CLAZZ_LIST);

    protected ArrayList<Class<? extends IAView>> devClazzList = null;

    protected ArrayList<Class<? extends IAView>> cronoClazzList = null;

    protected ArrayList<Class<? extends IAView>> adminClazzList = null;

    protected ArrayList<Class<? extends IAView>> userClazzList = null;


    /**
     * Costruttore @Autowired <br>
     */
    @Autowired
    public AMenu() {
        inizia();
    }// end of Spring constructor


    /**
     * Costruisce il componente di questa classe concreta di menu <br>
     * Sovrascritto
     */
    protected void inizia() {
    }// end of method


    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     */
    @PostConstruct
    protected void initViewPostConstruct() {
        fixContext();
        creaBarraMenu();
    }// end of method


    /**
     * Login and context della sessione <br>
     */
    protected void fixContext() {
        context = vaadinService.fixLoginAndContext(null);
        login = context.getLogin();
    }// end of method


    /**
     * Costruisce la barra di menu <br>
     */
    protected void creaBarraMenu() {
        //--regolazioni finali (sempre)
        fixIniziali();

        //--crea brand (sempre)
        creaBrand();

        //--crea menu dello sviluppatore (se loggato)
        if (context.isDev()) {
            creaMenuDeveloper();
        }// end of if cycle

        //--crea menu dell'admin (se loggato)
        if (context.isAdmin()) {
            creaMenuAdmin();
        }// end of if cycle

        //--crea menu utente normale (sempre)
        creaMenuUser();

        //--crea menu logout (sempre)
        creaMenuLogout();

        //--regolazioni finali (eventuali)
        fixFinali();
    }// end of method


    /**
     * Regolazioni iniziali
     */
    private void fixIniziali() {
        if (mappaClassi.get(EARole.developer.toString()) != null) {
            devClazzList = mappaClassi.get(EARole.developer.toString());
        }// end of if cycle

        if (mappaClassi.get(KEY_MAPPA_CRONO) != null) {
            cronoClazzList = mappaClassi.get(KEY_MAPPA_CRONO);
        }// end of if cycle

        if (mappaClassi.get(EARole.admin.toString()) != null) {
            adminClazzList = mappaClassi.get(EARole.admin.toString());
        }// end of if cycle

        if (mappaClassi.get(EARole.user.toString()) != null) {
            userClazzList = mappaClassi.get(EARole.user.toString());
        }// end of if cycle
    }// end of method


    /**
     * Eventuale brand a sinistra
     */
    protected void creaBrand() {
    }// end of method


    /**
     * Eventuali item di menu, se collegato come sviluppatore
     */
    protected void creaMenuDeveloper() {
        if (array.isValid(devClazzList)) {
            for (Class viewClazz : devClazzList) {
                addItem(viewClazz);
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Eventuali item di menu, se collegato come admin
     */
    protected void creaMenuAdmin() {
        if (array.isValid(adminClazzList)) {
            for (Class viewClazz : adminClazzList) {
                addItem(viewClazz);
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Item di menu (normali) sempre presenti
     */
    protected void creaMenuUser() {
        if (array.isValid(userClazzList)) {
            for (Class viewClazz : userClazzList) {
                addItem(viewClazz);
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Item di menu logout sempre presente
     */
    protected void creaMenuLogout() {
    }// end of method


    /**
     * Regolazioni finali
     */
    protected void fixFinali() {
    }// end of method


    /**
     * Crea il singolo item di menu <br>
     */
    protected Object creaItem(Class<? extends AViewList> viewClazz) {
        return null;
    }// end of method


    /**
     * Aggiunge il singolo item di menu <br>
     */
    protected void addItem(Class<? extends AViewList> viewClazz) {
    }// end of method


    /**
     * Crea una mappa di viewClazz, in funzione dell'@Annotation presente nella classe <br>
     */
    private Map<String, ArrayList<Class<? extends IAView>>> creaMappa(List<Class> listaClassiMenu) {
        Map<String, ArrayList<Class<? extends IAView>>> mappa = new HashMap<>();
        ArrayList<Class<? extends IAView>> devClazzList = new ArrayList<>();
        ArrayList<Class<? extends IAView>> cronoDevClazzList = new ArrayList<>();
        ArrayList<Class<? extends IAView>> adminClazzList = new ArrayList<>();
        ArrayList<Class<? extends IAView>> utenteClazzList = new ArrayList<>();
        EARoleType type = null;
        List<String> cronoList = Arrays.asList(new String[]{"Secolo", "Anno", "Mese", "Giorno"});
        String menuName;

        for (Class viewClazz : listaClassiMenu) {
            type = annotation.getViewRoleType(viewClazz);

            switch (type) {
                case developer:
                    menuName = annotation.getMenuName(viewClazz);
                    if (cronoList.contains(menuName)) {
                        cronoDevClazzList.add(viewClazz);
                    } else {
                        devClazzList.add(viewClazz);
                    }// end of if/else cycle
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

        mappa.put(EARole.developer.toString(), devClazzList);
        mappa.put(EARole.admin.toString(), adminClazzList);
        mappa.put(KEY_MAPPA_CRONO, cronoDevClazzList);
        mappa.put(EARole.user.toString(), utenteClazzList);

        return mappa;
    }// end of method


    protected ArrayList<String> getNamesList(ArrayList<Class<? extends IAView>> clazzList) {
        ArrayList<String> namesList = null;

        if (array.isValid(clazzList)) {
            namesList = new ArrayList<>();
            for (Class<? extends IAView> viewClazz : clazzList) {
                namesList.add(annotation.getMenuName(viewClazz));
            }// end of for cycle
        }// end of if cycle

        return namesList;
    }// end of method


    protected Class<? extends AViewList> getViewClazz(String menuName) {
        if (array.isValid(devClazzList)) {
            for (Class viewClazz : devClazzList) {
                if (annotation.getMenuName(viewClazz).equals(menuName)) {
                    return viewClazz;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        if (array.isValid(adminClazzList)) {
            for (Class viewClazz : adminClazzList) {
                if (annotation.getMenuName(viewClazz).equals(menuName)) {
                    return viewClazz;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        if (array.isValid(userClazzList)) {
            for (Class viewClazz : userClazzList) {
                if (annotation.getMenuName(viewClazz).equals(menuName)) {
                    return viewClazz;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return null;
    }// end of method


    public void goTo(String linkRoute) {
        UI.getCurrent().getPage().executeJavaScript("location.assign('" + linkRoute + "')");
    }// end of method


    @Override
    public AppLayout getAppLayout() {
        return null;
    }// end of method


    @Override
    public Component getComp() {
        return null;
    }// end of method

}// end of class
