package it.algos.vaadwiki.upload;


import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Esegue un ciclo di creazione (UPLOAD) delle liste di nati e morti per ogni giorno dell'anno
 * <p>
 * Il ciclo viene chiamato da DaemonCrono (con frequenza giornaliera ?)
 * Il ciclo può essere invocato dal bottone 'Upload all' nella grid Giorno
 * Il ciclo necessita del login come bot
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier("ccc")
@Slf4j
public abstract class UploadGiorni extends Upload {


    protected String titoloGiorno;

    @Autowired
    protected GiornoService giornoService;

    protected Giorno giorno;

    @Autowired
    private UploadGiornoNato uploadGiornoNato;

    @Autowired
    private UploadGiornoMorto uploadGiornoMorto;


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste di nati e morti per ogni giorno dell'anno
     */
    public void esegueAll() {
        ArrayList<Giorno> listaGiorni = giornoService.findAll();
        long inizio = System.currentTimeMillis();
        int modNati = 0;
        int modMorti = 0;
        String modTxt;

        for (Giorno giorno : listaGiorni) {
            uploadGiornoNato.esegue(giorno);
            modNati++;

            uploadGiornoMorto.esegue(giorno);
            modMorti++;
        }// end of for cycle

//        if (Pref.getBool(CostBio.USA_LOG_DEBUG, false)) {
//            modTxt = LibNum.format(modNati) + "+" + LibNum.format(modMorti);
//            if (Pref.getBool(CostBio.USA_REGISTRA_SEMPRE_CRONO, true)) {
//                Log.debug("upload", "Aggiornate tutte (366*2) le pagine dei giorni (nati e morti) in " + LibTime.difText(inizio));
//            } else {
//                Log.debug("upload", "Aggiornate solo le pagine modificate (" + modTxt + ") dei giorni (nati e morti) in " + LibTime.difText(inizio));
//            }// end of if/else cycle
//        }// end of if cycle
    }// end of method


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste di nati e morti per ogni giorno dell'anno
     */
    public void esegue(Giorno giorno) {
        this.giorno = giorno;
        esegue();
    }// fine del metodo


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste di nati e morti per ogni giorno dell'anno
     */
    public void esegueTest(Giorno giorno) {
        this.giorno = giorno;
        esegueTest();
    }// fine del metodo


    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    protected void elaboraParametri() {
        super.elaboraParametri();
        usaHeadTocIndice = false;
    }// fine del metodo


    /**
     * Titolo della pagina 'madre'
     * <p>
     * Sovrascritto
     */
    @Override
    protected String getTitoloPaginaMadre() {
        String titolo = VUOTA;

        if (giorno != null) {
            titolo += giorno.getTitolo();
        }// fine del blocco if

        return titolo;
    }// fine del metodo


    /**
     * Titolo della pagina Nati/Morti da creare/caricare su wikipedia
     * Sovrascritto
     */
    public String getTitoloPagina(Giorno giorno) {
        return "";
    }// fine del metodo


    /**
     * Titolo della pagina Nati/Morti da creare/caricare su wikipedia
     */
    public String getTitoloPagina(Giorno giorno, String tag) {
        String titoloLista = VUOTA;
        String articolo = "il";
        String articoloBis = "l'";
        String titolo = giorno.getTitolo();

        tag = tag.trim();
        if (!titolo.equals(VUOTA)) {
            if (titolo.startsWith("8") || titolo.startsWith("11")) {
                titoloLista = tag + SPAZIO + articoloBis + titolo;
            } else {
                titoloLista = tag + SPAZIO + articolo + SPAZIO + titolo;
            }// fine del blocco if-else
        }// fine del blocco if

        return titoloLista;
    }// fine del metodo


    /**
     * Incapsula il testo come parametro di un (eventuale) template
     * Se non viene incapsulato, restituisce il testo in ingresso
     * Sovrascritto
     *
     * @param testoBody
     */
    @Override
    protected String elaboraTemplate(String testoBody) {
        String testoOut = testoBody;
        String titoloTemplate = "Lista persone per giorno";
        String testoIni = VUOTA;
        String testoEnd = "}}";

        testoIni += "{{" + titoloTemplate;
        testoIni += A_CAPO;
        testoIni += "|titolo=" + titoloPagina;
        testoIni += A_CAPO;
        testoIni += "|voci=" + numPersone;
        testoIni += A_CAPO;
        testoIni += "|testo=";
        testoIni += A_CAPO;

        if (!testoBody.equals(VUOTA)) {
            testoOut = testoIni + testoBody + testoEnd;
        }// fine del blocco if

        return testoOut;
    }// fine del metodo


    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "Giorni";
    }// end of method

}// fine della classe
