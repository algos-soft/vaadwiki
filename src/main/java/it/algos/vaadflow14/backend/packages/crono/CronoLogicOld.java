package it.algos.vaadflow14.backend.packages.crono;

import it.algos.vaadflow14.backend.application.FlowVar;
import it.algos.vaadflow14.backend.enumeration.AEOperation;
import it.algos.vaadflow14.backend.enumeration.AEPreferenza;
import it.algos.vaadflow14.backend.logic.ALogicOld;
import it.algos.vaadflow14.backend.service.AIService;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 15-ago-2020
 * Time: 11:36
 */
public abstract class CronoLogicOld extends ALogicOld {

    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l' istanza SOLO come SCOPE_PROTOTYPE <br>
     * Costruttore usato da AView <br>
     * L' istanza DEVE essere creata con (ALogic) appContext.getBean(Class.forName(canonicalName), entityService, operationForm) <br>
     *
     * @param entityService layer di collegamento tra il 'backend' e mongoDB
     * @param operationForm tipologia di Form in uso
     */
    public CronoLogicOld(AIService entityService, AEOperation operationForm) {
        super(entityService, operationForm);
    }


    /**
     * Preferenze standard <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        //--Quattro packages cronologici (secolo, anno, mese, giorno)

        //--Bottoni DeleteAll e Reset presenti solo se FlowVar.usaDebug=true (debug si abilita solo per il developer)
        //--Bottone New presente solo login.isDeveloper()=true
        //--Entity modificabile e cancellabile solo login.isDeveloper()=true, altrimenti AEOperation.showOnly

        super.usaHeaderWrap = true;
        if (FlowVar.usaSecurity) {
            if (vaadinService.isDeveloper()) {
                super.usaBottoneDelete = true;
                super.usaBottoneResetList = true;
                super.usaBottoneNew = true;
            } else {
                super.usaBottoneNew = false;
                super.operationForm = AEOperation.showOnly;
            }
            super.usaBottoneExport = vaadinService.isAdminOrDeveloper();
        } else {
            if (AEPreferenza.usaDebug.is()) {
                super.usaBottoneDelete = true;
                super.usaBottoneResetList = true;
                super.usaBottoneNew = true;
            } else {
                super.usaBottoneNew = false;
                super.operationForm = AEOperation.showOnly;
            }
            super.usaBottoneExport = true;
        }
    }

}
