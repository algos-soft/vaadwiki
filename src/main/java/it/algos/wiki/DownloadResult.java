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

    public ArrayList<Long> vociDaRegistrare;

    public ArrayList<Long> vociRegistrate = new ArrayList<>();

    public ArrayList<String> vociNonRegistrate = new ArrayList<>();


    public DownloadResult(ArrayList<Long> vociDaRegistrare) {
        this.vociDaRegistrare = vociDaRegistrare;
    }// end of constructor


    public void addSi(Long voceRegistrata) {
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

}// end of class
