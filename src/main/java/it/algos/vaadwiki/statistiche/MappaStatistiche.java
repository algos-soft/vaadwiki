package it.algos.vaadwiki.statistiche;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 19-nov-2019
 * Time: 09:10
 */
public class MappaStatistiche {

    private String chiave;

    private int numAttivitaUno = 0;

    private int numAttivitaDue = 0;

    private int numAttivitaTre = 0;

    private int numAttivitaTotali = 0;

    private int numNazionalita = 0;

    private int numGiornoNato = 0;

    private int numGiornoMorto = 0;

    private int numAnnoNato = 0;

    private int numAnnoMorto = 0;

    private int ordine;


    public MappaStatistiche(String chiave) {
        this.chiave = chiave;
    }


    public MappaStatistiche(String chiave, int numNazionalita) {
        this.chiave = chiave;
        this.numNazionalita = numNazionalita;
    }


    public MappaStatistiche(String chiave, int numAttivitaUno, int numAttivitaDue, int numAttivitaTre, int numAttivitaTotali) {
        this.chiave = chiave;
        this.numAttivitaUno = numAttivitaUno;
        this.numAttivitaDue = numAttivitaDue;
        this.numAttivitaTre = numAttivitaTre;
        this.numAttivitaTotali = numAttivitaTotali;
    }


    public MappaStatistiche(String chiave, int ordine, int numGiornoNato, int numGiornoMorto) {
        this.chiave = chiave;
        this.ordine = ordine;
        this.numGiornoNato = numGiornoNato;
        this.numGiornoMorto = numGiornoMorto;
    }


    public String getChiave() {
        return chiave;
    }


    public int getNumAttivitaUno() {
        return numAttivitaUno;
    }


    public int getNumAttivitaDue() {
        return numAttivitaDue;
    }


    public int getNumAttivitaTre() {
        return numAttivitaTre;
    }


    public int getNumAttivitaTotali() {
        return numAttivitaTotali;
    }


    public int getNumNazionalita() {
        return numNazionalita;
    }


    public int getNumGiornoNato() {
        return numGiornoNato;
    }


    public int getNumGiornoMorto() {
        return numGiornoMorto;
    }


    public int getNumAnnoNato() {
        return numAnnoNato;
    }


    public int getNumAnnoMorto() {
        return numAnnoMorto;
    }


    public int getOrdine() {
        return ordine;
    }


}// end of class
