package it.algos.vaadwiki.statistiche;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 19-nov-2019
 * Time: 09:10
 */
public class MappaStatistiche {

    private String plurale;

    private int numAttivitaUno = 0;

    private int numAttivitaDue = 0;

    private int numAttivitaTre = 0;

    private int numAttivitaTotali = 0;

    private int numNazionalita = 0;


    public MappaStatistiche(String plurale) {
        this.plurale = plurale;
    }


    public MappaStatistiche(String plurale, int numAttivitaUno, int numAttivitaDue, int numAttivitaTre, int numAttivitaTotali) {
        this.plurale = plurale;
        this.numAttivitaUno = numAttivitaUno;
        this.numAttivitaDue = numAttivitaDue;
        this.numAttivitaTre = numAttivitaTre;
        this.numAttivitaTotali = numAttivitaTotali;
    }


    public String getPlurale() {
        return plurale;
    }


    public void setPlurale(String plurale) {
        this.plurale = plurale;
    }


    public int getNumAttivitaUno() {
        return numAttivitaUno;
    }


    public void setNumAttivitaUno(int numAttivitaUno) {
        this.numAttivitaUno = numAttivitaUno;
    }


    public int getNumAttivitaDue() {
        return numAttivitaDue;
    }


    public void setNumAttivitaDue(int numAttivitaDue) {
        this.numAttivitaDue = numAttivitaDue;
    }


    public int getNumAttivitaTre() {
        return numAttivitaTre;
    }


    public void setNumAttivitaTre(int numAttivitaTre) {
        this.numAttivitaTre = numAttivitaTre;
    }


    public int getNumAttivitaTotali() {
        return numAttivitaTotali;
    }


    public void setNumAttivitaTotali(int numAttivitaTotali) {
        this.numAttivitaTotali = numAttivitaTotali;
    }


    public int getNumNazionalita() {
        return numNazionalita;
    }


    public void setNumNazionalita(int numNazionalita) {
        this.numNazionalita = numNazionalita;
    }

}// end of class
