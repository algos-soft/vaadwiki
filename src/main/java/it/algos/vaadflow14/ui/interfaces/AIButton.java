package it.algos.vaadflow14.ui.interfaces;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.icon.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 29-mar-2021
 * Time: 18:26
 * Interfaccia per la Enumeration di bottoni <br>
 * Classi concrete di Enumeration:
 * AEButton (obbligatoria) di VaadFlow14 <br>
 * AExxxButton (facoltativa) specifica del progetto corrente <br>
 * <p>
 * KeyModifier.META -> quarto da sinistra, mela, command
 * KeyModifier.CONTROL -> secondo da sinistra, control,  ^
 * KeyModifier.ALT -> boh?
 * KeyModifier.SHIFT -> fila a sinistra, secondo dal basso, freccia in su
 */
public interface AIButton {

    public String getTesto();

    public VaadinIcon getVaadinIcon();

    public String getTheme();

    public AIAction getAction();

    public boolean isEnabled();

    public String getToolTip();

    public String getLumoIcon();

    public Key getKeyShortCut();

    public KeyModifier getKeyModifier();

    public boolean isIconaAfterText();

    public boolean isIconaOnly();

    public Button get(final String text);

    public Button get();

}// end of interface

