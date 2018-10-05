package it.algos.wiki.entities.wiki;

import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.wiki.Api;
import it.algos.wiki.LibWiki;
import it.algos.wiki.Page;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by gac on 18 ago 2015.
 * .
 */
@Entity
public class Wiki extends AEntity {

    /**
     * nomi interni dei campi (ordine non garantito)
     */

    //--parametri wiki base
    @Column(unique = true)
    @NotNull
    @Index
    private long pageid;

    @NotEmpty
    private String title;

    private long ns;

    //--parametri wiki info
    private String pagelanguage;

    //--parametri wiki revisions
    private long revid;
    private long parentid;
    private boolean minor;
    private String user;
    private boolean anon;
    private long userid;
    private Timestamp timestamp;
    private long size;
    private String comment;
    private String contentformat;
    private String contentmodel;


    //--tempo di DOWNLOAD
    //--uso il formato Timestamp, per confrontarla col campo timestamp
    //--molto meglio che siano esattamente dello stesso tipo
    //--ultima lettura della voce effettuata dal programma Botbio
    //--momento in cui il record BioWiki è stato modificato in allineamento alla voce sul server wiki
    private Timestamp ultimalettura;

//    //--tempo di UPLOAD
//    //--uso il formato Timestamp, per confrontarla col campo timestamp
//    //--molto meglio che siano esattamente dello stesso tipo
//    //--momento in cui la voce sul server wiki è stata modificata con il WrapBio costruito dal programma
//    private Timestamp ultimaScrittura;
//
//    //--ridondante, costruito con il timestamp esatto della pagina sul server wiki
//    //--serve per visualizzare la data in forma ''breve'' più leggibile,
//    //--mentre rimane il valore esatto del campo originario timestamp
//    private Date modificaWiki;
//
//    //--ridondante, costruito con il ultimaLettura esatto della pagina
//    //--serve per visualizzare la data in forma ''breve'' più leggibile,
//    //--mentre rimane il valore esatto del campo originario timestamp
//    private Date letturaWiki;
//
//    //serve per le voci che sono state modificate sul server wiki rispetto alla versione sul database
//    //si basa sul parametro lastrevid
//    //per sicurezza è false (quindi all'inizio controllo tutto)
//    private boolean allineata = false;

    //--forse
//    private String starttimestamp;


    public Wiki() {
    }// end of constructor

    /**
     * Recupera una istanza di Versione usando la query specifica
     *
     * @return istanza di Versione, null se non trovata
     */
    public static Wiki find(long id) {
        Wiki instance = null;
//        AEntity entity = AQuery.queryById(Wiki.class, id);
//
//        if (entity != null) {
//            if (entity instanceof Wiki) {
//                instance = (Wiki) entity;
//            }// end of if cycle
//        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di Versione usando la query specifica
     *
     * @return istanza di Wiki, null se non trovata
     */
    public static Wiki find(String titolo) {
        Wiki instance = null;
//        BaseEntity entity = AQuery.queryOne(Wiki.class, Versione_.titolo, titolo);
//
//        if (entity != null) {
//            if (entity instanceof Wiki) {
//                instance = (Wiki) entity;
//            }// end of if cycle
//        }// end of if cycle

        return instance;
    }// end of method

    public synchronized static int count() {
        int totRec = 0;
//        long totTmp = AQuery.getCount(Wiki.class);
//
//        if (totTmp > 0) {
//            totRec = (int) totTmp;
//        }// fine del blocco if

        return totRec;
    }// end of method

//    public synchronized static List<Wiki> findAllWiki() {
//        return (List<Wiki>) AQuery.getList(Wiki.class);
//    }// end of method

    /**
     * Crea una istanza di wiki dal titolo della voce
     * Registra l'istanza
     *
     * @param titolo della voce sul server wiki
     * @return istanza di Wiki, null se non trovata
     */
    public static Wiki save(String titolo) {
        Wiki wiki = null;
        HashMap mappa = null;
        Page pagina = Api.leggePage(titolo);

        if (pagina != null) {
            mappa = pagina.getMappaDB();
        }// fine del blocco if

        if (mappa != null) {
            wiki = new Wiki();
            wiki = LibWiki.fixMappa(wiki, mappa);
//            wiki.save();
        }// fine del blocco if

        return wiki;
    }// end of method

    @Override
    public String toString() {
        return title;
    }// end of method

    public long getPageid() {
        return pageid;
    }

    public void setPageid(long pageid) {
        this.pageid = pageid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getNs() {
        return ns;
    }

    public void setNs(long ns) {
        this.ns = ns;
    }

    public String getContentmodel() {
        return contentmodel;
    }

    public void setContentmodel(String contentmodel) {
        this.contentmodel = contentmodel;
    }

    public String getPagelanguage() {
        return pagelanguage;
    }

    public void setPagelanguage(String pagelanguage) {
        this.pagelanguage = pagelanguage;
    }

    public long getRevid() {
        return revid;
    }

    public void setRevid(long revid) {
        this.revid = revid;
    }

    public long getParentid() {
        return parentid;
    }

    public void setParentid(long parentid) {
        this.parentid = parentid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getContentformat() {
        return contentformat;
    }

    public void setContentformat(String contentformat) {
        this.contentformat = contentformat;
    }

    public boolean isMinor() {
        return minor;
    }

    public void setMinor(boolean minor) {
        this.minor = minor;
    }

    public boolean isAnon() {
        return anon;
    }

    public void setAnon(boolean anon) {
        this.anon = anon;
    }


    public Timestamp getUltimalettura() {
        return ultimalettura;
    }

    public void setUltimalettura(Timestamp ultimalettura) {
        this.ultimalettura = ultimalettura;
    }

}// end of entity class
