package it.algos.vaadwiki.backend.packages.wiki;

import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.interfaces.*;
import it.algos.vaadflow14.wiki.*;
import it.algos.vaadwiki.backend.service.*;
import it.algos.vaadwiki.ui.enumeration.*;
import org.springframework.beans.factory.annotation.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * First time: lun, 26-apr-2021 <br>
 * Last doc revision: mar, 18-mag-2021 alle 18:37 <br>
 * <p>
 * Classe (facoltativa) di un package con personalizzazioni <br>
 * Se manca, usa la classe GenericLogicForm con @Route <br>
 * Gestione della 'view' di @Route e della 'business logic' <br>
 * Mantiene lo 'stato' <br>
 * L' istanza (PROTOTYPE) viene creata ad ogni chiamata del browser <br>
 * Eventuali parametri (opzionali) devono essere passati nell'URL <br>
 * <p>
 * Annotated with @Route (obbligatorio) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
public abstract class WikiLogicForm extends LogicForm {


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WikiBotService wikiBot;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BioUtility bioUtility;

    /**
     * Costruttore con parametri <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Il framework SpringBoot/Vaadin con l'Annotation @Autowired inietta automaticamente un riferimento al singleton xxxService <br>
     * L'annotation @Autowired potrebbe essere omessa perché c'è un solo costruttore <br>
     * Usa un @Qualifier perché la classe AService è astratta ed ha diverse sottoclassi concrete <br>
     * Regola (nella superclasse) la entityClazz (final) associata a questa logicView <br>
     *
     * @param entityService (obbligatorio) riferimento al service specifico correlato a questa istanza (prototype) di LogicList
     * @param entityClazz   (obbligatorio)  the class of type AEntity
     */
    public WikiLogicForm(final AIService entityService, final Class<? extends AEntity> entityClazz) {
        super(entityService, entityClazz);
    }// end of Vaadin/@Route constructor



    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Puo essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
    }

    /**
     * Esegue l'azione del bottone, textEdit o comboBox. <br>
     * Interfaccia utilizzata come parametro per poter sovrascrivere il metodo <br>
     * Nella classe base eseguirà un casting a AEAction <br>
     * Nella (eventuale) sottoclasse specifica del progetto eseguirà un casting a AExxxAction <br>
     *
     * @param iAzione interfaccia dell'azione selezionata da eseguire
     *
     * @return false se il parametro non è una enumeration valida o manca lo switch
     */
    @Override
    public boolean performAction(AIAction iAzione) {
        boolean status = super.performAction(iAzione);
        AEWikiAction azione = iAzione instanceof AEWikiAction ? (AEWikiAction) iAzione : null;

        if (status) {
            return true;
        }

        if (azione == null) {
            return false;
        }

        status = true;
        switch (azione) {
            case elabora:
                elabora();
                break;
            case wikiPaginaView:
                openWikiPage(AWikiApiService.API_VIEW);
                break;
            case wikiPaginaEdit:
                openWikiPage(AWikiApiService.API_EDIT);
                break;
            case wikiPaginaHistory:
                openWikiPage(AWikiApiService.API_HISTORY);
                break;
            case didascalie:
                super.openWeb("https://it.wikipedia.org/wiki/Progetto:Antroponimi/Didascalie");
                break;
            default:
                status = false;
                break;
        }

        return status;
    }

    /**
     * Elabora una singola biografia. <br>
     * DEVE essere sovrascritto, SENZA invocare prima il metodo della superclasse <br>
     */
    protected void elabora() {
    }

    /**
     * Apre una pagina di wikipedia. <br>
     */
    protected void openWikiPage(String path) {
        super.openWeb(path + wikiPageTitle);
    }

}