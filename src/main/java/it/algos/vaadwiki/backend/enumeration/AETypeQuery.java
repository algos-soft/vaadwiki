package it.algos.vaadwiki.backend.enumeration;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 27-lug-2021
 * Time: 09:25
 */
public enum AETypeQuery {
    get("GET senza loginCookies"),
    getCookies("GET con loginCookies"),
    post("POST"),
    login("preliminary GET + POST"),
    ;

    private  String tag;


     AETypeQuery(String tag) {
        this.tag = tag;
    }

    public String get() {
        return tag;
    }
}
