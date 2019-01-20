package it.algos.vaadwiki.upload;


import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadwiki.liste.ListaGiornoNato;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

import static it.algos.vaadflow.application.FlowCost.SPAZIO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Esegue un ciclo di creazione (UPLOAD) delle liste di nati e morti per ogni giorno dell'anno
 * <p>
 * Il ciclo viene chiamato da DaemonCrono (con frequenza giornaliera ?)
 * Il ciclo può essere invocato dal bottone 'Upload all' nella grid Giorno
 * Il ciclo necessita del login come bot
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class UploadGiorni extends Upload {


    protected String titoloGiorno;


    @Autowired
    private ListaGiornoNato listaGiornoNato;

    @Autowired
    private GiornoService giornoService;

    private Giorno giorno;


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
            esegueNati(giorno);
            modNati++;

            esegueMorti(giorno);
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
    public void esegueNati(Giorno giorno) {
        this.giorno = giorno;
        esegue();
    }// fine del metodo


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste di nati e morti per ogni giorno dell'anno
     */
    public void esegueMorti(Giorno giorno) {
        this.giorno = giorno;
        esegue();
    }// fine del metodo


    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    protected void elaboraParametri() {
        // head
//        usaHeadNonScrivere = pref.isBool(CostBio.USA_HEAD_NON_SCRIVERE, true);
        usaHeadInclude = true; //--tipicamente sempre true. Si attiva solo se c'è del testo (iniziale) da includere
        usaHeadToc = true; //--tipicamente sempre true.
        usaHeadTocIndice = true; //--normalmente true. Sovrascrivibile da preferenze
        usaHeadRitorno = false; //--normalmente false. Sovrascrivibile da preferenze
        usaHeadTemplateAvviso = true; //--normalmente true. Sovrascrivibile nelle sottoclassi
        tagHeadTemplateAvviso = "ListaBio"; //--Sovrascrivibile da preferenze
        tagHeadTemplateProgetto = "biografie"; //--Sovrascrivibile da preferenze
        usaHeadIncipit = false; //--normalmente false. Sovrascrivibile da preferenze

        // body
//        usaSortCronologico = false;
        usaSuddivisioneParagrafi = false;
        usaOrdineAlfabeticoParagrafi = false;
        usaBodySottopagine = true; //--normalmente true. Sovrascrivibile nelle sottoclassi
        usaBodyRigheMultiple = false; //--normalmente false. Sovrascrivibile da preferenze
        usaBodyDoppiaColonna = true; //--normalmente true. Sovrascrivibile nelle sottoclassi
//        usaBodyTemplate = false; //--normalmente false. Sovrascrivibile nelle sottoclassi
//        usaSottopagine = false; //--normalmente false. Sovrascrivibile nelle sottoclassi
//        maxVociParagrafo = Pref.getInt(CostBio.MAX_VOCI_PARAGRAFO, 50);//--tipicamente 50. Sovrascrivibile nelle sottoclassi
//        tagParagrafoNullo = ALTRE;
//        usaTitoloParagrafoConLink = false;
//        usaTaglioVociPagina = false;
//        maxVociPagina = 0;

        // footer
//        usaFooterPortale = false;
//        usaFooterCategorie = false;

//        usaSuddivisioneUomoDonna = false
//        usaAttivitaMultiple = false
//        usaTitoloParagrafoConLink = true
//        usaTitoloSingoloParagrafo = false
//        tagLivelloParagrafo = '=='
//        tagParagrafoNullo = 'Altre...'
//        usaSottopaginaAltri == Pref.getBool(LibBio.USA_SOTTOPAGINA_ALTRI, false)
    }// fine del metodo


    /**
     * Titolo della pagina da creare/caricare su wikipedia
     * Sovrascritto
     */
    protected void elaboraTitolo() {
        if (giorno != null) {
            titoloPagina = getTitoloPagina(giorno, "Nati");
        }// fine del blocco if
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
        if (!titolo.equals("")) {
            if (titolo.startsWith("8") || titolo.startsWith("11")) {
                titoloLista = tag + SPAZIO + articoloBis + titolo;
            } else {
                titoloLista = tag + SPAZIO + articolo + SPAZIO + titolo;
            }// fine del blocco if-else
        }// fine del blocco if

        return titoloLista;
    }// fine del metodo


    /**
     * Costruisce una lista di biografie che hanno una valore valido per la pagina specifica
     * Esegue una query
     * Sovrascritto
     */
    protected void elaboraMappaListaDidascalieBio() {
        mappaListaOrdinataDidascalie = listaGiornoNato.esegue(giorno);
        super.elaboraMappaListaDidascalieBio();
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
