package it.algos.wiki;


import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 27-8-13
 * Time: 19:20
 */
public class WrapTime {

    private long pageid;

    private String wikiTitle;

    private Timestamp timestamp;

    private boolean trovata;


    public WrapTime(long pageid, String wikiTitle, Timestamp timestamp) {
        this.setPageid(pageid);
        this.setWikiTitle(wikiTitle);
        this.setTimestamp(timestamp);
    }// fine del metodo costruttore


    public WrapTime(long pageid, String wikiTitle, String timestampStr) {
        this(pageid, wikiTitle, timestampStr, true);
    }// fine del metodo costruttore


    public WrapTime(long pageid, String wikiTitle, String timestampStr, boolean trovata) {
        this.setPageid(pageid);
        this.setWikiTitle(wikiTitle);

        if (timestampStr == null || timestampStr.equals("")) {
            this.setTimestamp(null);
        } else {
            this.setTimestamp(VaadWiki.getWikiTime(timestampStr));
        }// end of if/else cycle

        this.setTrovata(trovata);
    }// fine del metodo costruttore


    public static WrapTime get(ArrayList<WrapTime> lista, long pageid) {
        WrapTime wrap = null;

        for (WrapTime wrapTmp : lista) {
            if (wrapTmp.pageid == pageid) {
                wrap = wrapTmp;
                return wrap;
            }// end of if cycle
        }// end of for cycle

        return wrap;
    }//end of setter method

    public static WrapTime get(ArrayList<WrapTime> lista, String wikiTitle) {
        WrapTime wrap = null;

        for (WrapTime wrapTmp : lista) {
            if (wrapTmp.wikiTitle == wikiTitle) {
                wrap = wrapTmp;
                return wrap;
            }// end of if cycle
        }// end of for cycle

        return wrap;
    }//end of setter method


    public static Timestamp getTimestamp(ArrayList<WrapTime> lista, long pageid) {
        Timestamp timestamp = null;
        WrapTime wrap = get(lista, pageid);

        if (wrap != null) {
            timestamp = wrap.timestamp;
        }// end of if cycle

        return timestamp;
    }//end of setter method


    public long getPageid() {
        return pageid;
    }// end of getter method


    public void setPageid(long pageid) {
        this.pageid = pageid;
    }//end of setter method


    public String getWikiTitle() {
        return wikiTitle;
    }// end of getter method


    public void setWikiTitle(String wikiTitle) {
        this.wikiTitle = wikiTitle;
    }//end of setter method


    public Timestamp getTimestamp() {
        return timestamp;
    }// end of getter method


    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }//end of setter method


    public boolean isTrovata() {
        return trovata;
    }// end of getter method


    public void setTrovata(boolean trovata) {
        this.trovata = trovata;
    }//end of setter method


    public LocalDateTime getDate() {
        LocalDateTime localDateTime = LocalDateTime.of(2100, 1, 1, 0, 0);

        if (timestamp != null) {
            Instant instant = Instant.ofEpochMilli(timestamp.getTime());
            localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }// end of if cycle

        return localDateTime;
    }//end of setter method

} // fine della classe
