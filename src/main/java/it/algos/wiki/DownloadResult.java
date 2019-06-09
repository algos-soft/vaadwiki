package it.algos.wiki;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 06-dic-2018
 * Time: 15:04
 */
public class DownloadResult {

    /**
     * Nome della categoria utilizzata
     */
    private String nomeCategoria;

    private int numVociCategoria;

    private LocalDateTime inizio;

//    private ArrayList<String> vociDaCancellare;
//
//    private ArrayList<String> vociDaCreare;

    private List<Long> vociDaCancellare;

    private List<Long> vociDaCreare;

    private ArrayList<String> vociNonCreate;

    private int numVociCancellate;

    private int numVociCreate;

    private int numVociDaAggiornare;

    private int numVociAggiornate;


    public DownloadResult(String nomeCategoria) {
        this.inizio = LocalDateTime.now();
        this.nomeCategoria = nomeCategoria;
        this.vociNonCreate = new ArrayList<>();
    }// end of constructor


    public DownloadResult(ArrayList<Long> vociDaCreare) {
        this.vociDaCreare = vociDaCreare;
    }// end of constructor


    public ArrayList<String> getVociNonCreate() {
        return vociNonCreate;
    }// end of method


    public void setVociNonCreate(ArrayList<String> vociNonCreate) {
        this.vociNonCreate = vociNonCreate;
    }// end of method


    public void addNo(String titoloVoceNonRegistrata) {
        vociNonCreate.add(titoloVoceNonRegistrata);
    }// end of method


    public boolean isValid() {
        return numVociCreate == vociDaCreare.size();
    }// end of method


    public LocalDateTime getInizio() {
        return inizio;
    }// end of method


    public void setInizio(LocalDateTime inizio) {
        this.inizio = inizio;
    }// end of method


    public long getInizioLong() {
        return Timestamp.valueOf(inizio).getTime();
    }// end of method


    public int getNumVociCreate() {
        return numVociCreate;
    }// end of method


    public void setNumVociCreate(int numVociCreate) {
        this.numVociCreate = numVociCreate;
    }// end of method


    public List<Long> getVociDaCreare() {
        return vociDaCreare;
    }// end of method


    public void setVociDaCreare(List<Long> vociDaCreare) {
        this.vociDaCreare = vociDaCreare;
    }// end of method


    public int getNumVociCategoria() {
        return numVociCategoria;
    }// end of method


    public void setNumVociCategoria(int numVociCategoria) {
        this.numVociCategoria = numVociCategoria;
    }// end of method


    public List<Long> getVociDaCancellare() {
        return vociDaCancellare;
    }// end of method


    public void setVociDaCancellare(List<Long> vociDaCancellare) {
        this.vociDaCancellare = vociDaCancellare;
    }// end of method


    public int getNumVociDaAggiornare() {
        return numVociDaAggiornare;
    }// end of method


    public void setNumVociDaAggiornare(int numVociDaAggiornare) {
        this.numVociDaAggiornare = numVociDaAggiornare;
    }// end of method


    public int getNumVociAggiornate() {
        return numVociAggiornate;
    }// end of method


    public void setNumVociAggiornate(int numVociAggiornate) {
        this.numVociAggiornate = numVociAggiornate;
    }// end of method


    public String getNomeCategoria() {
        return nomeCategoria;
    }// end of method


    public int getNumVociCancellate() {
        return numVociCancellate;
    }// end of method


    public void setNumVociCancellate(int numVociCancellate) {
        this.numVociCancellate = numVociCancellate;
    }// end of method


    public void addVoceCreata() {
        this.setNumVociCreate(this.getNumVociCreate() + 1);
    }// end of method


    public void addVoceDaAggiornare() {
        this.setNumVociDaAggiornare(this.getNumVociDaAggiornare() + 1);
    }// end of method


    public void addVoceAggiornata() {
        this.setNumVociAggiornate(this.getNumVociAggiornate() + 1);
    }// end of method

}// end of class
