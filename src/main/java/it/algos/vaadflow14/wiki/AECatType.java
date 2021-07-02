package it.algos.vaadflow14.wiki;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 01-lug-2021
 * Time: 22:32
 */
public enum AECatType {
    file("file"),
    page("page"),
    subcat("subcat"),
    all("page|subcat"),
    ;

    private String tag;


    AECatType(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
