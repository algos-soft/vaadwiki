package it.algos.vaadwiki.backend.enumeration;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 22-feb-2022
 * Time: 17:24
 */
public enum AETypeLista {
    attivita("Progetto:Biografie/Attività/", 2),//@todo deve diventare 3
    nazionalita("Progetto:Biografie/Nazionalità/", 2),//@todo deve diventare 3
    nati("", 0),
    morti("", 0),
    nomi("", 0),
    cognomi("", 0);

    private String prefix;

    private int livello;

    AETypeLista(final String prefix, final int livello) {
        this.prefix = prefix;
        this.livello = livello;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getLivello() {
        return livello;
    }
}
