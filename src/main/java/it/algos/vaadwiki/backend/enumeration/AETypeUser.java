package it.algos.vaadwiki.backend.enumeration;

import static it.algos.vaadflow14.backend.application.FlowCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 14-lug-2021
 * Time: 16:53
 */
public enum AETypeUser {

    anonymous(VUOTA, 500),
    user("user", 500),
    bot("bot", 500),
    ;

    private static final String ASSERT = "&assert=";

    private static final String LIMIT = "&cmlimit=";

    private final String affermazione;

    private final int limit;

    private AETypeUser(String affermazione, int limit) {
        this.affermazione = affermazione;
        this.limit = limit;
    }


    public String affermazione() {
        return affermazione.length() > 0 ? ASSERT + affermazione : VUOTA;
    }

    public String limit() {
        return LIMIT + limit;
    }

}
