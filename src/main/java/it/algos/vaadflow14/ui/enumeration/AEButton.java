package it.algos.vaadflow14.ui.enumeration;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.icon.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.ui.interfaces.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: gio, 21-mag-2020
 * Time: 06:30
 * <p>
 * Classi concrete di Enumeration:
 * AEButton (obbligatoria) di VaadFlow14 <br>
 * AESimpleButton (facoltativa) specifica del progetto corrente <br>
 * <p>
 * KeyModifier.META -> quarto da sinistra, mela, command
 * KeyModifier.CONTROL -> secondo da sinistra, control,  ^
 * KeyModifier.ALT -> boh?
 * KeyModifier.SHIFT -> fila a sinistra, secondo dal basso, freccia in su
 */
public enum AEButton implements AIButton {

    //--List
    deleteAll("Delete", VaadinIcon.CLOSE, "error", AEAction.deleteAll, true, "Cancella tutta la collezione", "cross", Key.KEY_D, KeyModifier.CONTROL),
    resetList("Reset", VaadinIcon.RECYCLE, "error", AEAction.resetList, true, "Ripristina tutta la collezione", "cross", Key.KEY_R, KeyModifier.CONTROL),
    nuovo("New", VaadinIcon.PLUS_CIRCLE, "secondary", AEAction.nuovo, true, "Crea una nuova entity", "plus", Key.KEY_N, KeyModifier.CONTROL),
    searchDialog("Cerca...", VaadinIcon.SEARCH, "secondary", AEAction.searchDialog, true, "Apre un dialogo di ricerca", "search", Key.KEY_F, KeyModifier.CONTROL),
    export("Export", VaadinIcon.DOWNLOAD, "error", AEAction.export, true, "Esporta la lista", "cross", null, null),
    wiki("Wiki", VaadinIcon.GLOBE_WIRE, "secondary", AEAction.showWiki, true, "Apre una pagina di Wikipedia", "cross", Key.KEY_R, KeyModifier.CONTROL),
    download("Download", VaadinIcon.DOWNLOAD, "primary", AEAction.download, true, "Download di una pagina", "cross"),
    upload("Upload", VaadinIcon.UPLOAD, "error", AEAction.upload, true, "Upload di una pagina", "cross"),

    //--Form
    resetForm("Reset", VaadinIcon.REFRESH, "secondary", AEAction.resetForm, true, "Ripristina tutte le properties della scheda", "cross", Key.KEY_R, KeyModifier.CONTROL),
    back("Back", VaadinIcon.ARROW_BACKWARD, "secondary", AEAction.back, true, "Torna indietro", "arrow-left"),
    annulla("Annulla", VaadinIcon.ARROW_LEFT, "primary", AEAction.annulla, true, "Annulla l' operazione", "arrow-left", Key.ESCAPE, null),
    delete("Delete", VaadinIcon.CLOSE, "error", AEAction.delete, true, "Cancella la scheda", "cross", null, null),
    conferma("Conferma", VaadinIcon.CHECK, "secondary", AEAction.conferma, true, "Conferma l' operazione", "checkmark", Key.KEY_S, KeyModifier.CONTROL),
    registra("Save", VaadinIcon.CHECK, "primary", AEAction.registra, true, "Registra le modifiche", "download", Key.ENTER, null),
    prima("Before", VaadinIcon.CARET_LEFT, "secondary", AEAction.prima, true, "Va alla scheda precedente", "cross", Key.ARROW_LEFT, KeyModifier.CONTROL, false, true),
    dopo("Next", VaadinIcon.CARET_RIGHT, "secondary", AEAction.dopo, true, "Va alla scheda successiva", "cross", Key.ARROW_RIGHT, KeyModifier.CONTROL, false, true),

    ;

    /**
     * Flag di preferenza per il titolo del bottone. <br>
     */
    public String testo;

    /**
     * Flag di preferenza per l'icona (Vaadin) del bottone. <br>
     */
    public VaadinIcon vaadinIcon;

    /**
     * Flag di preferenza per l'attributo 'theme' del bottone. <br>
     */
    public String theme;

    /**
     * Flag di preferenza per l'azione del bottone. <br>
     */
    public AEAction action;

    /**
     * Flag di preferenza per la property 'enabled' del bottone. <br>
     */
    public boolean enabled;

    /**
     * Flag di preferenza per l'attributo 'tooltip' del bottone. <br>
     */
    public String toolTip;

    /**
     * Flag di preferenza per l'icona (Lumo) del bottone. <br>
     */
    public String lumoIcon;

    /**
     * Flag di preferenza per l'attributo 'keyShortCut' del bottone. <br>
     */
    public Key keyShortCut;

    /**
     * Flag di preferenza per l'attributo 'keyModifier' del bottone. <br>
     */
    public KeyModifier keyModifier;

    /**
     * Flag di preferenza per posizionare a destra l'icona del bottone. <br>
     */
    public boolean iconaAfterText;

    /**
     * Flag di preferenza per mostrare solo l'icona del bottone, SENZA il testo. <br>
     */
    public boolean iconaOnly;


    AEButton(String testo, VaadinIcon vaadinIcon, String theme, AEAction action) {
        this(testo, vaadinIcon, theme, action, true, VUOTA, VUOTA);
    }


    AEButton(String testo, VaadinIcon vaadinIcon, String theme, AEAction action, boolean enabled, String toolTip, String lumoIcon) {
        this(testo, vaadinIcon, theme, action, enabled, toolTip, lumoIcon, (Key) null, (KeyModifier) null, false, false);
    }


    AEButton(String testo, VaadinIcon vaadinIcon, String theme, AEAction action, boolean enabled, String toolTip, String lumoIcon, Key keyShortCut, KeyModifier keyModifier) {
        this(testo, vaadinIcon, theme, action, enabled, toolTip, lumoIcon, keyShortCut, keyModifier, false, false);
    }


    AEButton(String testo, VaadinIcon vaadinIcon, String theme, AEAction action, boolean enabled, String toolTip, String lumoIcon, Key keyShortCut, KeyModifier keyModifier, boolean iconaAfterText, boolean iconaOnly) {
        this.testo = testo;
        this.vaadinIcon = vaadinIcon;
        this.theme = theme;
        this.action = action;
        this.enabled = enabled;
        this.toolTip = toolTip;
        this.lumoIcon = lumoIcon;
        this.keyShortCut = keyShortCut;
        this.keyModifier = keyModifier;
        this.iconaAfterText = iconaAfterText;
        this.iconaOnly = iconaOnly;
    }

    @Override
    public String getTesto() {
        return testo;
    }

    @Override
    public VaadinIcon getVaadinIcon() {
        return vaadinIcon;
    }

    @Override
    public String getTheme() {
        return theme;
    }

    @Override
    public AEAction getAction() {
        return action;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getToolTip() {
        return toolTip;
    }

    @Override
    public String getLumoIcon() {
        return lumoIcon;
    }

    @Override
    public Key getKeyShortCut() {
        return keyShortCut;
    }

    @Override
    public KeyModifier getKeyModifier() {
        return keyModifier;
    }

    @Override
    public boolean isIconaAfterText() {
        return iconaAfterText;
    }

    @Override
    public boolean isIconaOnly() {
        return iconaOnly;
    }

}
