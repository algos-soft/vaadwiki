package it.algos.vaadflow14.backend.enumeration;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 13-mag-2021
 * Time: 11:35
 * Tipologia dei file Algos sia generici che del package <br>
 */
public enum AETypeFile {

    nessuno("File generico", false),
    entity("Classe base obbligatoria di un package", true),
    list("Gestione della 'business logic' e della 'grafica' di @Route", true),
    form("Gestione del Form della singola entity", true),
    servicePackage("Prototype di collegamento tra il 'backend' e mongoDB", true),
    serviceSingleton("Singleton di libreria", false),
    ;

    private String descrizione;

    private boolean usatoNeiPackages;


    AETypeFile(String descrizione, boolean usatoNeiPackages) {
        this.descrizione = descrizione;
        this.usatoNeiPackages = usatoNeiPackages;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public boolean isUsatoNeiPackages() {
        return usatoNeiPackages;
    }
}
