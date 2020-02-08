package it.algos.vaadwiki;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.shared.ui.LoadMode;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.ui.MainLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 03-mar-2019
 * Time: 17:17
 */
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@HtmlImport(value = "styles/shared-styles.html", loadMode = LoadMode.INLINE)
@HtmlImport(value = "styles/algos-styles.html", loadMode = LoadMode.INLINE)
@Push
@PageTitle(value = "wikibot")
@Slf4j
public class WikiLayout extends MainLayout {

//    /**
//     * Crea il singolo item di menu <br>
//     *
//     * @param viewClazz
//     */
//    @Override
//    public AppLayoutMenuItem addMenu(Class<? extends AViewList> viewClazz) {
//        return super.addMenu(viewClazz);
//    }


//    @Override
//    protected Map<String, List<Class>> creaMappa(List<Class> listaClassiMenu) {
//        mappaClassi = super.creaMappa(listaClassiMenu);
//        List<Class> userClazzList = null;
//        userClazzList = mappaClassi.get(EARole.user.toString());
//        userClazzList.add(Pippoz.class);
//        mappaClassi.put(EARole.user.toString(), userClazzList);
//        return mappaClassi;
//    }// end of method


//    /**
//     * Creazione iniziale del menu
//     */
//    protected void creaVaadindMenu() {
//        FlowVar.menuClazzList.add(Pippoz.class);
//        super.creaVaadindMenu();
////        appLayout.setToolbarIconButtons(new MenuItem("Logout", "exit-to-app", () -> UI.getCurrent().getPage().executeJavaScript("location.assign('logout')")));
//    }// end of method

//    /**
//     * Menu logout sempre presente
//     */
//    @Override
//    protected void creaMenuLogout() {
//
//        addMenu(Pippoz.class);
//    }// end of method

}// end of class
