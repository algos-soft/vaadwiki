package it.algos.vaadwiki.service;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: ven, 10-ago-2018
 * Time: 08:27
 */
public class KeyPage {
    private long pageid;
    private String title;

    public KeyPage(long pageid, String title) {
        this.pageid = pageid;
        this.title = title;
    }

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
}// end of class
