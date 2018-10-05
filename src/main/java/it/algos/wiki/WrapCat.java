package it.algos.wiki;

/**
 * Wrapper di dati per le categorie
 */
public class WrapCat {

    private long ns;
    private long pageid;
    private String title;

    public WrapCat(Long ns, long pageid, String title) {
        this.setNs(ns);
        this.setPageid(pageid);
        this.setTitle(title);
    }// fine del metodo costruttore

    public long getNs() {
        return ns;
    }// end of getter method

    private void setNs(long ns) {
        this.ns = ns;
    }//end of setter method

    public long getPageid() {
        return pageid;
    }// end of getter method

    private void setPageid(long pageid) {
        this.pageid = pageid;
    }//end of setter method

    public String getTitle() {
        return title;
    }// end of getter method

    private void setTitle(String title) {
        this.title = title;
    }//end of setter method

} // fine della classe
