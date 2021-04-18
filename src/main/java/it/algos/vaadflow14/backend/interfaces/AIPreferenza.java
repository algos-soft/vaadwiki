package it.algos.vaadflow14.backend.interfaces;

import it.algos.vaadflow14.backend.enumeration.*;

import java.time.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 09-ott-2019
 * Time: 23:18
 * <p>
 * Interfaccia per la Enumeration di preferenze <br>
 * Classi concrete di Enumeration:
 * AEPreferenza (obbligatoria) di VaadFlow14 <br>
 * AExxxPreferenza (facoltativa) specifica del progetto corrente <br>
 */
public interface AIPreferenza {

    String getKeyCode();

    AETypePref getType();

    Object getValue();

    Object getDefaultValue();

    boolean isVaadFlow();

    boolean isUsaCompany();

    boolean isNeedRiavvio();

    boolean isVisibileAdmin();

    String getDescrizione();

    public boolean is();

    public int getInt();

    public LocalDateTime getDate();

    String getNote();

}// end of interface
