package it.algos.vaadflow.modules.logtype;

import it.algos.vaadflow.application.FlowCost;

import java.util.ArrayList;
import java.util.List;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 26-set-2018
 * Time: 07:39
 */
public enum EALogType {
    setup(FlowCost.SETUP),
    nuovo(FlowCost.NEW),
    edit(FlowCost.EDIT),
    delete(FlowCost.DELETE),
    info(FlowCost.INFO),
    warn(FlowCost.WARN),
    error(FlowCost.ERROR),
    importo(FlowCost.IMPORT);

    private String tag;

    EALogType(String tag) {
        this.setTag(tag);
    }// fine del costruttore

    public static EALogType getType(String tag) {
        EALogType[] types = values();

        for (EALogType type : values()) {
            if (type.getTag().equals(tag)) {
                return type;
            }// end of if cycle
        }// end of for cycle

        return null;
    }// end of static method

    public static List<String> getAll() {
        List<String> lista = new ArrayList<>();

        for (EALogType type : values()) {
            lista.add(type.tag);
        }// end of for cycle

        return lista;
    }// end of static method

    private void setTag(String tag) {
        this.tag = tag;
    }// end of method

    public String getTag() {
        return tag;
    }// end of method

}// end of enumeration

