package it.algos.vaadwiki.statistiche;

import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.giorno.Giorno;

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
    }// end of constructor


    public MappaStatistiche(String chiave, int numNazionalita) {
        this.chiave = chiave;
        this.numNazionalita = numNazionalita;
    }// end of constructor


    public MappaStatistiche(String chiave, int numAttivitaUno, int numAttivitaDue, int numAttivitaTre, int numAttivitaTotali) {
        this.chiave = chiave;
        this.numAttivitaUno = numAttivitaUno;
        this.numAttivitaDue = numAttivitaDue;
        this.numAttivitaTre = numAttivitaTre;
        this.numAttivitaTotali = numAttivitaTotali;
    }// end of constructor


    public MappaStatistiche(Giorno giorno, int numGiornoNato, int numGiornoMorto) {
        this.chiave = giorno.titolo;
        this.ordine = giorno.ordine;
        this.numGiornoNato = numGiornoNato;
        this.numGiornoMorto = numGiornoMorto;
    }// end of constructor


    public MappaStatistiche(Anno anno, int ordine, int numAnnoNato, int numAnnoMorto) {
        this.chiave = anno.titolo;
        this.ordine = ordine;
        this.numAnnoNato = numAnnoNato;
        this.numAnnoMorto = numAnnoMorto;
    }// end of constructor


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
