package it.algos.vaadwiki.backend.packages.bio;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.data.value.*;
import com.vaadin.flow.router.*;
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
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 06-mag-2021
 * Time: 16:29
 * <p>
 */
@Route(value = "bioFormNew", layout = MainLayout.class)
public class BioLogicFormNew extends WikiLogicForm {


    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


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
    public BioLogicFormNew(@Autowired @Qualifier("bioService") final AIService bioService) {
        super(bioService, Bio.class);
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
     * Costruisce una lista di bottoni (enumeration) al Top della view <br>
     * Costruisce i bottoni come dai Flag regolati di default o nella sottoclasse <br>
     * Nella sottoclasse possono essere aggiunti i bottoni specifici dell'applicazione <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected List<AIButton> getListaAEBottoniTop() {
        return Collections.singletonList(AEButton.download);
    }

    /**
     * Costruisce una lista ordinata di nomi delle properties del Form. <br>
     * La lista viene usata per la costruzione automatica dei campi e l' inserimento nel binder <br>
     * Nell' ordine: <br>
     * 1) Cerca nell' annotation @AIForm della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxLogic <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * Se serve, modifica l' ordine della lista oppure esclude una property che non deve andare nel binder <br>
     * todo ancora da sviluppare
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getFormPropertyNamesList() {
        List<String> fieldsNameList = new ArrayList<>();

        fieldsNameList.add("wikiTitle");
        fieldsNameList.add("tmpBioServer");

        return fieldsNameList;
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
            fieldWikiTitle.addValueChangeListener(e -> sincroDownload((String) e.getValue()));
            com.vaadin.flow.component.Component comp = fieldWikiTitle.get();
            ((ATextField) comp).textField.setValueChangeMode(ValueChangeMode.EAGER);
        }

        //--disabilita inizialmente il bottone download
        sincroDownload(VUOTA);
    }

    /**
     * Sincronizza lo stato del bottone download col contenuto del field wikiTitle <br>
     */
    private void sincroDownload(final String value) {
        Button bottoneDownload = topLayout.getMappaBottoni().get(AEButton.download);
        if (bottoneDownload != null) {
            bottoneDownload.setEnabled(text.isValid(value) && value.length() > 2);
        }
    }

    /**
     * Titolo della pagina wiki <br>
     */
    @Deprecated
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
        String textTmpl;
        AField fieldTmpBioServer = currentForm.getField("tmpBioServer");

        String wikiTitle = getWikiTitle();
        if (text.isValid(getWikiTitle())) {
            textTmpl= wiki.leggeTmpl(wikiTitle,"Bio");
            if (text.isValid(textTmpl)) {
                fieldTmpBioServer.setValue(textTmpl);
            }
        }
    }


}// end of Route class



