package it.algos.vaadflow.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.developer.DeveloperView;
import it.algos.vaadflow.modules.address.AddressViewList;
import it.algos.vaadflow.modules.company.CompanyViewList;
import it.algos.vaadflow.modules.log.LogViewList;
import it.algos.vaadflow.modules.logtype.LogtypeViewList;
import it.algos.vaadflow.modules.person.PersonViewList;
import it.algos.vaadflow.modules.preferenza.EAPreferenza;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.modules.preferenza.PreferenzaViewList;
import it.algos.vaadflow.modules.role.RoleViewList;
import it.algos.vaadflow.modules.utente.UtenteViewList;
import it.algos.vaadflow.modules.versione.VersioneViewList;
import it.algos.vaadflow.wizard.WizardView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 21-set-2018
 * Time: 19:31
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ABootService {

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PreferenzaService pref;


    public void creaRouteStandard() {
        FlowCost.MENU_CLAZZ_LIST = new ArrayList<>();

        //--developer
        if (pref.isBool(EAPreferenza.showDeveloper.getCode())) {
            FlowCost.MENU_CLAZZ_LIST.add(DeveloperView.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showRole.getCode())) {
            FlowCost.MENU_CLAZZ_LIST.add(RoleViewList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showUser.getCode())) {
            FlowCost.MENU_CLAZZ_LIST.add(UtenteViewList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showWizard.getCode())) {
            FlowCost.MENU_CLAZZ_LIST.add(WizardView.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showLogType.getCode())) {
            FlowCost.MENU_CLAZZ_LIST.add(LogtypeViewList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showCompany.getCode())) {
            FlowCost.MENU_CLAZZ_LIST.add(CompanyViewList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showAddress.getCode())) {
            FlowCost.MENU_CLAZZ_LIST.add(AddressViewList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showPerson.getCode())) {
            FlowCost.MENU_CLAZZ_LIST.add(PersonViewList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showVersione.getCode())) {
            FlowCost.MENU_CLAZZ_LIST.add(VersioneViewList.class);
        }// end of if cycle

        //--admin
        if (pref.isBool(EAPreferenza.showPreferenza.getCode())) {
            FlowCost.MENU_CLAZZ_LIST.add(PreferenzaViewList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showLog.getCode())) {
            FlowCost.MENU_CLAZZ_LIST.add(LogViewList.class);
        }// end of if cycle

    }// end of method


    public void creaRouteStandardDeveloper() {
        FlowCost.MENU_CLAZZ_LIST = new ArrayList<>();

        FlowCost.MENU_CLAZZ_LIST.add(DeveloperView.class);
        FlowCost.MENU_CLAZZ_LIST.add(RoleViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(UtenteViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(WizardView.class);

        FlowCost.MENU_CLAZZ_LIST.add(PreferenzaViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(VersioneViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(LogViewList.class);

        FlowCost.MENU_CLAZZ_LIST.add(CompanyViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(AddressViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(PersonViewList.class);

        FlowCost.MENU_CLAZZ_LIST.add(LogtypeViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(RoleViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(PersonViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(AddressViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(LogViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(PreferenzaViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(VersioneViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(UtenteViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(CompanyViewList.class);
    }// end of method


    public void creaRouteStandardAdmin() {
        FlowCost.MENU_CLAZZ_LIST = new ArrayList<>();

        FlowCost.MENU_CLAZZ_LIST.add(PreferenzaViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(LogViewList.class);

    }// end of method

}// end of class