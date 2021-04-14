package it.algos.vaadflow14.backend.enumeration;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import it.algos.vaadflow14.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.time.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 18-set-2020
 * Time: 15:41
 * <p>
 * Classi concrete di Enumeration:
 * AEPreferenza (obbligatoria) di VaadFlow14 <br>
 * AExxxPreferenza (facoltativa) specifica del progetto corrente <br>
 */
public enum AEPreferenza implements AIPreferenza {

    usaDebug(PREF_USA_DEBUG, "Flag generale di debug", AETypePref.bool, false, "Ce ne possono essere di specifici, validi solo se questo è vero"),
    usaSearchClearButton(PREF_USA_SEARCH_CLEAR, "Bottone per pulire il filtro di ricerca", AETypePref.bool, true, false),
    usaBandiereStati(PREF_USA_BANDIERE_STATI, "Bandierine nel combobox degli stati", AETypePref.bool, true, true),
    usaGridHeaderMaiuscola(PREF_USA_GRID_MAIUSCOLA, "Prima lettera maiuscola nell' header della Grid", AETypePref.bool, true, true),
    usaFormFieldMaiuscola(PREF_USA_FORM_MAIUSCOLA, "Prima lettera maiuscola nella label di un field", AETypePref.bool, true, true),
    usaSearchCaseSensitive(PREF_USA_SEARCH_SENSITIVE, "Search delle query sensibile alle maiuscole", AETypePref.bool, false, true),
    //    iconaDetail("iconaDetail", "VaadinIcon per aprire il Form", AETypePref.icona, "TRUCK"),
    iconaEdit(PREF_ICONA_EDIT, "Icona per il dettaglio della scheda", AETypePref.enumeration, AETypeIconaEdit.edit, true),
    mailTo(PREF_EMAIL, "Indirizzo email", AETypePref.email, "gac@algos.it", true, "Email di default a cui spedire i log di posta"),
    maxRigheGrid(PREF_MAX_RIGHE_GRID, "Righe massime della griglia semplice", AETypePref.integer, 20, "Numero di elementi oltre il quale scatta la pagination automatica della Grid (se attiva)"),
    maxEnumRadio(PREF_MAX_RADIO, "Massimo numero di 'radio' nelle Enum' ", AETypePref.integer, 3, "Numero massimo di items nella preferenza di tipo Enum da visualizzare con i radioBottoni; se superiore, usa un ComboBox"),
    lineHeight(LINE_HEIGHT, "Altezza di una riga nelle View", AETypePref.enumeration, AETypeHeight.normal, false, false, false),
    numeroBottoni(BUTTONS_NUMBER, "Numero di bottoni nella prima riga sopra la Grid", AETypePref.integer, 4, false, false, false),
    usaLogVisibile(PREF_USA_LOG_VIDEO, "Visualizza il log nella finestra", AETypePref.bool, true, false),
    usaButtonOnlyIcon(PREF_USA_BUTTON_ONLY_ICON, "Usa solo l'icona del bottone e non il testo", AETypePref.bool, true, false),
    durataAvviso(PREF_DURATA_AVVISO_VIDEO, "Durata in millisecondi dell'avviso a video", AETypePref.integer, 2000, false, false, false),
    usaMenuReset(PREF_USA_MENU_RESET, "Usa i bottoni deleteAll e reset nelle xxxLogicList (se previsto dal package)", AETypePref.bool, true, false, true, false, VUOTA),
    usaMenuCrono(PREF_USA_MENU_CRONO, "Usa i menu del gruppo crono", AETypePref.bool, false, false, true, false, VUOTA),
    usaMenuGeo(PREF_USA_MENU_GEO, "Usa i menu del gruppo geografia", AETypePref.bool, false, false, true, false, VUOTA),
    ;


    //--codice di riferimento. Se è usaCompany=true, DEVE contenere anche il code della company come prefisso.
    private String keyCode;

    //--tipologia di dato da memorizzare.
    //--Serve per convertire (nei due sensi) il valore nel formato byte[] usato dal mongoDb
    private AETypePref type;

    //--Valore java iniziale da convertire in byte[] a seconda del type
    private Object defaultValue;

    //--preferenze singole per ogni company; usa un prefisso col codice della company
    private boolean usaCompany;

    //--preferenze generale del framework e NON specifica di un'applicazione
    private boolean vaadFlow;

    //--preferenze che necessita di un riavvio del programma per avere effetto
    private boolean needRiavvio;

    //--preferenze visibile agli admin se l'applicazione è usaSecurity=true
    private boolean visibileAdmin;

    //--descrizione breve ma comprensibile. Ulteriori (eventuali) informazioni nel campo 'note'
    private String descrizione;

    //--descrizione aggiuntiva eventuale
    private String note;

    //--Link injettato da un metodo static
    private PreferenzaService preferenzaService;

    //--Link injettato da un metodo static
    private ALogService logger;

    //--Link injettato da un metodo static
    private ADateService date;

    //--Link injettato da un metodo static
    private AEnumerationService enumService;


    AEPreferenza(String keyCode, String descrizione, AETypePref type, Object defaultValue, String note) {
        this(keyCode, descrizione, type, defaultValue, false, false, false, note);
    }// fine del costruttore

    AEPreferenza(String keyCode, String descrizione, AETypePref type, Object defaultValue, boolean usaCompany) {
        this(keyCode, descrizione, type, defaultValue, usaCompany, false, false, VUOTA);
    }// fine del costruttore

    AEPreferenza(String keyCode, String descrizione, AETypePref type, Object defaultValue, boolean usaCompany, String note) {
        this(keyCode, descrizione, type, defaultValue, usaCompany, false, false, note);
    }// fine del costruttore

    AEPreferenza(String keyCode, String descrizione, AETypePref type, Object defaultValue, boolean usaCompany, boolean needRiavvio, boolean visibileAdmin) {
        this(keyCode, descrizione, type, defaultValue, usaCompany, needRiavvio, visibileAdmin, VUOTA);
    }// fine del costruttore


    AEPreferenza(String keyCode, String descrizione, AETypePref type, Object defaultValue, boolean usaCompany, boolean needRiavvio, boolean visibileAdmin, String note) {
        this.keyCode = keyCode;
        this.descrizione = descrizione;
        this.type = type;
        this.setDefaultValue(defaultValue);
        this.vaadFlow = true; //--vale per tutte le preferenze generali di vaadFlow
        this.usaCompany = usaCompany;
        this.needRiavvio = needRiavvio;
        this.visibileAdmin = visibileAdmin;
        this.setNote(note);
    }// fine del costruttore


    public void setPreferenzaService(PreferenzaService preferenzaService) {
        this.preferenzaService = preferenzaService;
    }

    public void setLogger(ALogService logger) {
        this.logger = logger;
    }

    public void setDate(ADateService date) {
        this.date = date;
    }

    public void setEnumService(AEnumerationService enumService) {
        this.enumService = enumService;
    }

    @Override
    public String getKeyCode() {
        return keyCode;
    }

    @Override
    public AETypePref getType() {
        return type;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        if (type == AETypePref.enumeration) {
            if (defaultValue instanceof String) {
                this.defaultValue = defaultValue;
            }
            else {
                this.defaultValue = ((AIEnum) defaultValue).getPref();
            }
        }
        else {
            this.defaultValue = defaultValue;
        }
    }

    @Override
    public boolean isVaadFlow() {
        return vaadFlow;
    }

    @Override
    public boolean isUsaCompany() {
        return usaCompany;
    }

    @Override
    public boolean isNeedRiavvio() {
        return needRiavvio;
    }

    @Override
    public boolean isVisibileAdmin() {
        return visibileAdmin;
    }

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    public Object getValue() {
        Object javaValue;
        Preferenza preferenza = null;

        if (preferenzaService == null) {
            return null;
        }

        preferenza = preferenzaService.findByKey(keyCode);
        javaValue = preferenza != null ? type.bytesToObject(preferenza.getValue()) : null;

        return javaValue;
    }


    public String getStr() {
        String valore = VUOTA;
        Object value = defaultValue;
        String message;

        switch (type) {
            case string:
            case email:
                valore = (String) value;
                break;
            case bool:
                valore = (boolean) getValue() ? VERO : FALSO;
                message = String.format("La preferenza %s è di type boolean. Meglio chiamare is() invece di getStr()", keyCode);
                logger.warn(message);
                break;
            case integer:
                valore += getValue();
                message = String.format("La preferenza %s è di type integer. Meglio chiamare getInt() invece di getStr()", keyCode);
                logger.warn(message);
                break;
            case lungo:
                break;
            case localdate:
                valore = date.get((LocalDate) getValue());
                break;
            case localdatetime:
                valore = date.get((LocalDateTime) getValue());
                break;
            case localtime:
                valore = date.get((LocalTime) getValue());
                break;
            case enumeration:
                valore = ((String) getValue()).substring(((String) getValue()).indexOf(PUNTO_VIRGOLA) + 1);
                break;
            case icona:
                break;
            default:
                message = String.format("La preferenza: AEPreferenza.%s è di tipo %s e non può essere convertita in stringa.", name(), type);
                logger.error(message);
                break;
        }

        return valore;
    }

    public boolean is() {
        String message;

        if (type == AETypePref.bool) {
            return getValue() != null ? (boolean) getValue() : false;
        }
        else {
            message = String.format("La preferenza %s è di type %s. Non puoi usare is()", keyCode, type);
            logger.error(message);
            return false;
        }
    }


    public int getInt() {
        String message;

        if (type == AETypePref.integer) {
            return getValue() != null ? (int) getValue() : 0;
        }
        else {
            message = String.format("La preferenza %s è di type %s. Non puoi usare getInt()", keyCode, type);
            logger.error(message);
            return 0;
        }
    }


    public String getNote() {
        return note;
    }


    public void setNote(String note) {
        this.note = note;
    }


    @Component
    public static class APreferenzaServiceInjector {

        @Autowired
        private PreferenzaService preferenzaService;

        @Autowired
        private ALogService logger;

        @Autowired
        private AEnumerationService enumService;

        @Autowired
        private ADateService date;

        @PostConstruct
        public void postConstruct() {
            for (AEPreferenza pref : AEPreferenza.values()) {
                pref.setPreferenzaService(preferenzaService);
                pref.setLogger(logger);
                pref.setDate(date);
                pref.setEnumService(enumService);
            }
        }

    }

}
