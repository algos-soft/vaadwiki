package it.algos.vaadwiki.backend.packages.bio;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.ui.enumeration.*;
import it.algos.vaadflow14.ui.fields.*;
import it.algos.vaadflow14.ui.interfaces.*;
import it.algos.vaadwiki.backend.packages.wiki.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * Fix date: lun, 26-apr-2021 <br>
 * Fix time: 13:45 <br>
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

@Route(value = "bioForm", layout = MainLayout.class)
@AIScript(sovraScrivibile = false)
public class BioLogicForm extends WikiLogicForm {


    /**
     * Costruttore con parametro <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Il framework SpringBoot/Vaadin con l'Annotation @Autowired inietta automaticamente un riferimento al singleton xxxService <br>
     * L'annotation @Autowired potrebbe essere omessa perché c'è un solo costruttore <br>
     * Usa un @Qualifier perché la classe AService è astratta ed ha diverse sottoclassi concrete <br>
     * Regola (nella superclasse) la entityClazz (final) associata a questa logicView <br>
     *
     * @param bioService (@Autowired) (@Qualifier) riferimento al service specifico correlato a questa istanza (prototype) di LogicForm
     */
    public BioLogicForm(@Autowired @Qualifier("bioService") final AIService bioService) {
        super(bioService, Bio.class);
    }// end of Vaadin/@Route constructor


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        super.beforeEnter(beforeEnterEvent);
    }


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
     * Costruisce una lista di bottoni (enumeration) al Top della view <br>
     * Costruisce i bottoni come dai Flag regolati di default o nella sottoclasse <br>
     * Nella sottoclasse possono essere aggiunti i bottoni specifici dell'applicazione <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected List<AIButton> getListaAEBottoniTop() {
        return Collections.singletonList(AEButton.download);
    }

    /**
     * Regolazioni finali di alcuni oggetti <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void regolazioniFinali() {
        super.regolazioniFinali();

        //--aggiunge un listener al field wikiTitle
        AField fieldWikiTitle = currentForm.getField("wikiTitle");
        if (fieldWikiTitle != null) {
            fieldWikiTitle.addValueChangeListener(e -> sincroDownload());
        }

        //--disabilita inizialmente il bottone download
        sincroDownload();
    }

    /**
     * Sincronizza lo stato del bottone download col contenuto del field wikiTitle <br>
     */
    private void sincroDownload() {
        Button bottoneDownload = topLayout.getMappaBottoni().get(AEButton.download);
        if (bottoneDownload != null) {
            bottoneDownload.setEnabled(text.isValid(getWikiTitle()));
        }
    }

    /**
     * Titolo della pagina wiki <br>
     */
    private String getWikiTitle() {
        String valueWikiTitle = VUOTA;
        AField fieldWikiTitle = currentForm.getField("wikiTitle");
        valueWikiTitle = fieldWikiTitle != null ? (String) fieldWikiTitle.getValue() : VUOTA;

        return valueWikiTitle;
    }


    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto <br>
     *
     * @return true se l'azione è stata eseguita
     */
    public boolean download() {
        downloadBio();
        return true;
    }

    /**
     * Scarica una singola biografia <br>
     */
    private void downloadBio() {
        String allText;

        String valueWikiTitle = getWikiTitle();
        if (text.isValid(getWikiTitle())) {
            allText = wikiApi.legge(valueWikiTitle);
            System.out.println(allText);
        }
    }

}