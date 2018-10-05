package it.algos.vaadflow.modules.preferenza;


import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.enumeration.EAPrefType;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 30-mag-2018
 * Time: 07:27
 */
public enum EAPreferenza {

    usaDebug(FlowCost.USA_DEBUG, "Flag generale di debug (ce ne possono essere di specifici, validi solo se questo è vero)", EAPrefType.bool, false),
    usaLogDebug(FlowCost.USA_LOG_DEBUG, "Uso del log di registrazione per il livello debug. Di default false", EAPrefType.bool, false),
    usaCompany(FlowCost.USA_COMPANY, "L'applicazione è multiCompany", EAPrefType.bool, false),
    usaSecurity(FlowCost.USA_SECURITY, "L'applicazione usa la security", EAPrefType.bool, false),
    usaCheckBox(FlowCost.USA_CHECK_BOX, "Uso del checkbox in lista, per i valori booleani", EAPrefType.bool, true),
    showCompany(FlowCost.SHOW_COMPANY, show(), EAPrefType.bool, true),
    showPreferenza(FlowCost.SHOW_PREFERENZA, show(), EAPrefType.bool, true),
    showWizard(FlowCost.SHOW_WIZARD, show(), EAPrefType.bool, true),
    showDeveloper(FlowCost.SHOW_DEVELOPER, show(), EAPrefType.bool, true),
    showAddress(FlowCost.SHOW_ADDRESS, show(), EAPrefType.bool, true),
    showPerson(FlowCost.SHOW_PERSON, show(), EAPrefType.bool, true),
    showRole(FlowCost.SHOW_ROLE, show(), EAPrefType.bool, true),
    showUser(FlowCost.SHOW_USER, show(), EAPrefType.bool, true),
    showVersione(FlowCost.SHOW_VERSION, show(), EAPrefType.bool, true),
    showLog(FlowCost.SHOW_LOG, show(), EAPrefType.bool, true),
    showLogType(FlowCost.SHOW_LOGTYPE, show(), EAPrefType.bool, true),
    usaLogMail(FlowCost.USA_LOG_MAIL, "Uso della mail spedita da un log. Di default false", EAPrefType.bool, false),
    logMailAddress(FlowCost.LOG_MAIL_ADDRESS, "Email a cui spedire i log di posta", EAPrefType.string, "gac@algos.it"),;


    private String code;
    private String desc;
    private EAPrefType type;
    private Object value;


    EAPreferenza(String code, String desc, EAPrefType type, Object value) {
        this.setCode(code);
        this.setDesc(desc);
        this.setType(type);
        this.setValue(value);
    }// fine del costruttore

    public static String show() {
        return "Flag per costruire la UI con il modulo visibile nel menu";
    }// end of method

    public String getCode() {
        return code;
    }// end of method

    public void setCode(String code) {
        this.code = code;
    }// end of method

    public String getDesc() {
        return desc;
    }// end of method

    public void setDesc(String desc) {
        this.desc = desc;
    }// end of method

    public EAPrefType getType() {
        return type;
    }// end of method

    public void setType(EAPrefType type) {
        this.type = type;
    }// end of method

    public Object getValue() {
        return value;
    }// end of method

    public void setValue(Object value) {
        this.value = value;
    }// end of method

} // end of enumeration
