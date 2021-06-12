package it.algos.vaadwiki.wiki;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 08-mag-2021
 * Time: 10:50
 */
public class Pagina {

    /** nomi interni dei campi (ordine non garantito) */
    //--parametri wiki
    private int pageid;

    private String title;

    private int ns;

    private String contentmodel;

    private String contentformat;

    private String edittoken;

    private int lastrevid;

    private int length;

    private String pagelanguage;

    private String starttimestamp;

    private String touched;    //ultima visita effettuata da chicchessia sul server wiki - attualmente (27-10-13) non utilizzato

    private String testo; //contenuto completo della pagina

    public Pagina(int pageid, String title, int ns, String contentmodel, String contentformat, String edittoken, int lastrevid, int length, String pagelanguage, String starttimestamp, String touched, String testo) {
        this.pageid = pageid;
        this.title = title;
        this.ns = ns;
        this.contentmodel = contentmodel;
        this.contentformat = contentformat;
        this.edittoken = edittoken;
        this.lastrevid = lastrevid;
        this.length = length;
        this.pagelanguage = pagelanguage;
        this.starttimestamp = starttimestamp;
        this.touched = touched;
        this.testo = testo;
    }

    public int getPageid() {
        return pageid;
    }

    public String getTitle() {
        return title;
    }

    public int getNs() {
        return ns;
    }

    public String getContentmodel() {
        return contentmodel;
    }

    public String getContentformat() {
        return contentformat;
    }

    public String getEdittoken() {
        return edittoken;
    }

    public int getLastrevid() {
        return lastrevid;
    }

    public int getLength() {
        return length;
    }

    public String getPagelanguage() {
        return pagelanguage;
    }

    public String getStarttimestamp() {
        return starttimestamp;
    }

    public String getTouched() {
        return touched;
    }

    public String getTesto() {
        return testo;
    }

}
