package it.algos.vaadwiki.backend.enumeration;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 22-feb-2022
 * Time: 17:24
 */
public enum AETypeLista {
    attivita("Progetto:Biografie/Attività/"),
    nazionalita(""),
    nati(""),
    morti("");

    private String prefix;


    AETypeLista(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
