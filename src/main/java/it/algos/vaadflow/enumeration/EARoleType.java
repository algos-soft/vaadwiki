package it.algos.vaadflow.enumeration;

import lombok.extern.slf4j.Slf4j;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: dom, 01-ott-2017
 * Time: 09:01
 */
public enum EARoleType {
    nobody, developer, admin, user, guest, asEntity;


    public static EARoleType getType(String code) {
        EARoleType[] types = values();

        for (EARoleType type : values()) {
            if (type.toString().equals(code)) {
                return type;
            }// end of if cycle
        }// end of for cycle

        return EARoleType.nobody;
    }// end of static method


}// end of enumeration
