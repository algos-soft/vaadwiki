package it.algos.vaadflow14.wiki;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 02-lug-2021
 * Time: 07:29
 */
public enum AECatProp {

    pageid("ids"),
    title("title"),
    all("ids|title"),
    ;

    private String tag;


    AECatProp(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

}
