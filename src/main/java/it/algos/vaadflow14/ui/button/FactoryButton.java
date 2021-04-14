package it.algos.vaadflow14.ui.button;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.icon.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.ui.interfaces.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: sab, 09-mag-2020
 * Time: 17:54
 */
public abstract class FactoryButton {


    /**
     * Costruzione standard del bottone <br>
     * Di default usa le vaadinIcon <br>
     *
     * @param aeButton enumeration con i parametri standard del bottone richiesto
     *
     * @return nuovo bottone costruito coi parametri standard previsti e SENZA listener
     */
    public static Button get(AIButton aeButton) {
        if (FlowVar.usaVaadinIcon) {
            return getVaadin(aeButton);
        }
        else {
            return getLumo(aeButton);
        }
    }


    /**
     * Costruzione standard del bottone con una vaadinIcon <br>
     *
     * @param aeButton enumeration con i parametri standard del bottone richiesto
     *
     * @return nuovo bottone costruito coi parametri standard previsti e SENZA listener
     */
    public static Button getVaadin(AIButton aeButton) {
        return get(aeButton, true);
    }


    /**
     * Costruzione standard del bottone con una lumoIcon <br>
     *
     * @param aeButton enumeration con i parametri standard del bottone richiesto
     *
     * @return nuovo bottone costruito coi parametri standard previsti e SENZA listener
     */
    public static Button getLumo(AIButton aeButton) {
        return get(aeButton, false);
    }


    /**
     * Costruzione base del bottone sia per la vaadinIcon che per la lumoIcon <br>
     *
     * @param aeButton   enumeration con i parametri standard del bottone richiesto
     * @param vaadinIcon flag per usare il typo di icona indicato
     *
     * @return nuovo bottone costruito coi parametri standard previsti e SENZA listener
     */
    private static Button get(AIButton aeButton, boolean vaadinIcon) {
        if (vaadinIcon) {
            return get(aeButton.getTesto(), aeButton.getVaadinIcon(), aeButton.getTheme(), aeButton.getToolTip(), aeButton.isEnabled(), aeButton.getKeyShortCut(), aeButton.getKeyModifier(), aeButton.isIconaAfterText(), aeButton.isIconaOnly());
        }
        else {
            return get(aeButton.getTesto(), aeButton.getLumoIcon(), aeButton.getTheme(), aeButton.getToolTip(), aeButton.isEnabled(), aeButton.getKeyShortCut(), aeButton.getKeyModifier(), aeButton.isIconaAfterText());
        }
    }


    /**
     * Costruzione base del bottone per la vaadinIcon <br>
     *
     * @param testo      visibile del bottone
     * @param vaadinIcon typo di icona
     *
     * @return nuovo bottone costruito coi parametri standard previsti e SENZA listener
     */
    public static Button getPrimary(String testo, VaadinIcon vaadinIcon) {
        return get(testo, vaadinIcon, "primary", VUOTA);
    }


    /**
     * Costruzione base del bottone per la lumoIcon <br>
     *
     * @param testo    visibile del bottone
     * @param lumoIcon typo di icona
     *
     * @return nuovo bottone costruito coi parametri standard previsti e SENZA listener
     */
    public static Button getPrimary(String testo, String lumoIcon) {
        return get(testo, lumoIcon, "primary", VUOTA, true, (Key) null, (KeyModifier) null, false);
    }


    /**
     * Costruzione base del bottone per la vaadinIcon <br>
     *
     * @param testo      visibile del bottone
     * @param vaadinIcon typo di icona
     *
     * @return nuovo bottone costruito coi parametri standard previsti e SENZA listener
     */
    public static Button getSecondary(String testo, VaadinIcon vaadinIcon) {
        return get(testo, vaadinIcon, "secondary", VUOTA);
    }


    /**
     * Costruzione base del bottone per la lumoIcon <br>
     *
     * @param testo    visibile del bottone
     * @param lumoIcon typo di icona
     *
     * @return nuovo bottone costruito coi parametri standard previsti e SENZA listener
     */
    public static Button getSecondary(String testo, String lumoIcon) {
        return get(testo, lumoIcon, "secondary", VUOTA, true, (Key) null, (KeyModifier) null, false);
    }


    /**
     * Costruzione base del bottone per la vaadinIcon <br>
     *
     * @param testo      visibile del bottone
     * @param vaadinIcon typo di icona
     * @param theme      di visualizzazione (primary, secondary, error)
     * @param toolTip    da mostrare
     *
     * @return nuovo bottone costruito coi parametri standard previsti e SENZA listener
     */
    public static Button get(String testo, VaadinIcon vaadinIcon, String theme, String toolTip) {
        return get(testo, vaadinIcon, theme, toolTip, true, (Key) null, (KeyModifier) null, false, false);
    }


    /**
     * Costruzione base del bottone per la vaadinIcon <br>
     *
     * @param testo       visibile del bottone
     * @param vaadinIcon  typo di icona
     * @param theme       di visualizzazione (primary, secondary, error)
     * @param toolTip     da mostrare
     * @param enabled     flag di abilitazione iniziale
     * @param keyShortCut eventuale
     * @param keyModifier eventuale
     *
     * @return nuovo bottone costruito coi parametri standard previsti e SENZA listener
     */
    public static Button get(String testo, VaadinIcon vaadinIcon, String theme, String toolTip, boolean enabled, Key keyShortCut, KeyModifier keyModifier, boolean iconaAfterText, boolean onlyIcon) {
        Button bottone;

        bottone = new Button(testo, new Icon(vaadinIcon));
        bottone.setIconAfterText(iconaAfterText);

        bottone.getElement().setAttribute("theme", theme);
        bottone.getElement().setAttribute("title", toolTip);
        bottone.addClassName("view-toolbar__button");
        bottone.setEnabled(enabled);
        if (true) { //@todo Creare una preferenza e sostituirla qui
            if (keyShortCut != null) {
                if (keyModifier != null) {
                    bottone.addClickShortcut(keyShortCut, keyModifier);
                }
                else {
                    bottone.addClickShortcut(keyShortCut);
                }
            }
        }
        if (onlyIcon && AEPreferenza.usaButtonOnlyIcon.is()) {
            bottone.setText(VUOTA);
            bottone.setWidth("2em");
        }

        return bottone;
    }


    /**
     * Costruzione base del bottone per la vaadinIcon <br>
     *
     * @param testo       visibile del bottone
     * @param iconType    typo di icona
     * @param theme       di visualizzazione (primary, secondary, error)
     * @param toolTip     da mostrare
     * @param enabled     flag di abilitazione iniziale
     * @param keyShortCut eventuale
     * @param keyModifier eventuale
     *
     * @return nuovo bottone costruito coi parametri standard previsti e SENZA listener
     */
    public static Button get(String testo, String iconType, String theme, String toolTip, boolean enabled, Key keyShortCut, KeyModifier keyModifier, boolean iconaAfterText) {
        Button bottone;

        bottone = new Button(testo, new Icon("lumo", iconType));
        bottone.setIconAfterText(iconaAfterText);

        bottone.getElement().setAttribute("theme", theme);
        bottone.getElement().setAttribute("title", toolTip);
        bottone.addClassName("view-toolbar__button");
        bottone.setEnabled(enabled);
        if (true) { //@todo Creare una preferenza e sostituirla qui
            if (keyShortCut != null) {
                if (keyModifier != null) {
                    bottone.addClickShortcut(keyShortCut, keyModifier);
                }
                else {
                    bottone.addClickShortcut(keyShortCut);
                }
            }
        }

        return bottone;
    }

    //    public static ButtonWrap getDelete() {
    //        return new ButtonWrap(AEAction.deleteAll, KEY_BUTTON_DELETE_ALL, "Delete all", VaadinIcon.CLOSE_CIRCLE, true, "error", "Cancella tutta la collezione");
    //    }
    //
    //
    //    public static ButtonWrap getReset() {
    //        return new ButtonWrap(AEAction.reset, KEY_BUTTON_RESET, "Reset", VaadinIcon.CLOSE_CIRCLE, true, "error", "Ripristina tutta la collezione");
    //    }
    //
    //
    //    public static ButtonWrap getNew() {
    //        return new ButtonWrap(AEAction.nuovo, KEY_BUTTON_NEW, "New", VaadinIcon.PLUS, true, "secondary", "Crea una nuova entity");
    //    }
    //
    //
    //    public static ButtonWrap getAnnulla() {
    //        return new ButtonWrap(AEAction.annulla, KEY_BUTTON_ANNULLA, "Back", VaadinIcon.ARROW_LEFT, true, "secondary", "Annulla l'operazione");
    //    }
    //
    //
    //    public static ButtonWrap getConferma() {
    //        return new ButtonWrap(AEAction.conferma, KEY_BUTTON_CONFERMA, "Conferma", VaadinIcon.CHECK, true, "secondary", "Conferma l'operazione");
    //    }
    //
    //
    //    public static ButtonWrap getRegistra() {
    //        return new ButtonWrap(AEAction.registra, KEY_BUTTON_REGISTRA, "Save", VaadinIcon.DATABASE, true, "error", "Registra le modifiche");
    //    }

}
