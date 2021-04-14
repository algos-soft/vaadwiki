package it.algos.vaadflow14.backend.enumeration;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 28-nov-2020
 * Time: 06:13
 */
public enum AETypeReset {

    enumeration("da una Enumeration"),
    fileCSV("da un file CSV"),
    wikipedia("da Wikipedia"),
    hardCoded("scritti hardCoded"),
    file("da un file"),
    ;

    private final String descrizione;


    AETypeReset(String descrizione) {
        this.descrizione = descrizione;
    }


    public String get() {
        return descrizione;
    }
}

