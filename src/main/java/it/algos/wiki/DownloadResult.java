package it.algos.wiki;

import java.util.ArrayList;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 06-dic-2018
 * Time: 15:04
 */
public class DownloadResult {

    public ArrayList<String> vociDaRegistrare;

    public ArrayList<String> vociRegistrate = new ArrayList<>();

    public ArrayList<String> vociNonRegistrate = new ArrayList<>();


    /**
     * Eventuale nome della categoria utilizzata
     */
    private String nomeCategoria;

    /**
     * inizio delle operazioni
     */
    private long inizio ;


    public DownloadResult() {
    }// end of constructor


    public DownloadResult(ArrayList<String> vociDaRegistrare) {
        this.vociDaRegistrare = vociDaRegistrare;
    }// end of constructor


    public void addSi(String voceRegistrata) {
        vociRegistrate.add(voceRegistrata);
    }// end of method


    public void addNo(String titoloVoceNonRegistrata) {
        vociNonRegistrate.add(titoloVoceNonRegistrata);
    }// end of method


    public int getNumVociDaRegistrare() {
        return vociDaRegistrare.size();
    }// end of method


    public int getNumVociRegistrate() {
        return vociRegistrate.size();
    }// end of method


    public int getNumVociNonRegistrate() {
        return vociNonRegistrate.size();
    }// end of method


    public boolean isValid() {
        return vociDaRegistrare.size() == vociRegistrate.size();
    }// end of method


    public String getNomeCategoria() {
        return nomeCategoria;
    }// end of method


    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }// end of method


    public long getInizio() {
        return inizio;
    }// end of method


    public void setInizio(long inizio) {
        this.inizio = inizio;
    }// end of method

}// end of class
