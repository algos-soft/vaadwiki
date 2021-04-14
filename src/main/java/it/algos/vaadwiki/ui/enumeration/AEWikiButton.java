package it.algos.vaadwiki.ui.enumeration;

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
 * KeyModifier.META -> quarto da sinistra, mela, command
 * KeyModifier.CONTROL -> secondo da sinistra, control,  ^
 * KeyModifier.ALT -> boh?
 * KeyModifier.SHIFT -> fila a sinistra, secondo dal basso, freccia in su
 */
public enum AEWikiButton implements AIButton {

    update("Update", VaadinIcon.LIST, "error", AEWikiAction.update, true, "Update di una pagina", "cross", Key.KEY_R, null),
    modulo("Modulo", VaadinIcon.LIST, "secondary", AEWikiAction.modulo, true, "Modulo di Wikipedia", "cross", Key.KEY_R, null),
    elabora("Elabora", VaadinIcon.GLOBE_WIRE, "secondary", AEWikiAction.elabora, true, "Elabora un documento", "cross", Key.KEY_R, null),
    check("Check", VaadinIcon.GLOBE_WIRE, "secondary", AEWikiAction.elabora, true, "Controlla un documento", "cross", Key.KEY_R, null),
    test("Test", VaadinIcon.GLOBE_WIRE, "secondary", AEWikiAction.test, true, "Test di una funzionalità", "cross", Key.KEY_R, null),
    statistiche("Statistiche", VaadinIcon.TABLE, "secondary", AEWikiAction.statistiche, true, "Visione statistiche", "cross", Key.KEY_R, null),
    statisticheDue("Upload statistiche", VaadinIcon.UPLOAD, "error", AEWikiAction.statisticheDue, true, "Elaborazione statistiche", "cross", Key.KEY_R, null),

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
    public AIAction action;

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


    AEWikiButton(String testo, VaadinIcon vaadinIcon, String theme, AIAction action) {
        this(testo, vaadinIcon, theme, action, true, VUOTA, VUOTA);
    }


    AEWikiButton(String testo, VaadinIcon vaadinIcon, String theme, AIAction action, boolean enabled, String toolTip, String lumoIcon) {
        this(testo, vaadinIcon, theme, action, enabled, toolTip, lumoIcon, (Key) null, (KeyModifier) null, false, false);
    }


    AEWikiButton(String testo, VaadinIcon vaadinIcon, String theme, AIAction action, boolean enabled, String toolTip, String lumoIcon, Key keyShortCut, KeyModifier keyModifier) {
        this(testo, vaadinIcon, theme, action, enabled, toolTip, lumoIcon, keyShortCut, keyModifier, false, false);
    }


    AEWikiButton(String testo, VaadinIcon vaadinIcon, String theme, AIAction action, boolean enabled, String toolTip, String lumoIcon, Key keyShortCut, KeyModifier keyModifier, boolean iconaAfterText, boolean iconaOnly) {
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
    public AIAction getAction() {
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
