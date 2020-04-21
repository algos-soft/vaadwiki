package it.algos.vaadwiki.service;

import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.*;
import it.algos.vaadflow.wrapper.WrapTreStringhe;
import it.algos.vaadwiki.download.*;
import it.algos.vaadwiki.enumeration.EAGraffe;
import it.algos.vaadwiki.modules.attivita.AttivitaService;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.modules.genere.GenereService;
import it.algos.vaadwiki.modules.nazionalita.NazionalitaService;
import it.algos.vaadwiki.modules.professione.ProfessioneService;
import it.algos.vaadwiki.upload.UploadService;
import it.algos.wiki.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: dom, 12-ago-2018
 * Time: 18:45
 */
@Slf4j
public class ABioService {


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public BioService bioService;

//    /**
//     * La injection viene fatta da SpringBoot in automatico <br>
//     */
//    @Autowired
//    protected WikiLoginOld wikiLoginOld;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public PreferenzaService pref;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public ElaboraService elaboraService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public ADateService date;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public Api api;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public ATextService text;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected UploadService uploadService;

    @Autowired
    protected ApplicationContext appContext;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AttivitaService attivitaService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected NazionalitaService nazionalitaService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ProfessioneService professioneService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected GenereService genereService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected NewService newService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected DeleteService deleteService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected UpdateService updateService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected PageService pageService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AArrayService array;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMongoService mongo;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMailService mail;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected LogService logger;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected LibBio libBio;


    /**
     * Controlla che il mongoDb delle voci biografiche abbia una dimensione accettabile, altrimenti non esegue <br>
     */
    protected boolean checkMongo() {
        boolean scarso = false;

        if (checkBioScarso()) {
            mail.send("Upload attivita", "Abortito l'upload delle attività perché il mongoDb delle biografie sembra vuoto o comunque carente di voci che invece dovrebbero esserci.");
            scarso = true;
        }// end of if cycle

        return scarso;
    }// end of method


    /**
     * Controlla che il mongoDb delle voci biografiche abbia una dimensione accettabile <br>
     * Per evitare di 'sparare' sul server pagine con biografie 'mancanti' <br>
     * Valore da aggiornare ogni tanto <br>
     */
    protected boolean checkBioScarso() {
        return bioService.count() < BIO_NEEDED_MINUMUM_SIZE;
    }// end of method


    /**
     * Numero di occorrenze di un tag in un testo. <br>
     * Il tag non viene trimmato ed è sensibile agli spazi prima e dopo
     * NON si usano le regex <br>
     *
     * @param testoDaSpazzolare di riferimento
     * @param tag               da cercare
     *
     * @return numero di occorrenze - zero se non ce ne sono
     */
    public int getNumTag(String testoDaSpazzolare, String tag) {
        int numTag = 0;
        int pos;

        //--controllo di congruità
        if (text.isValid(testoDaSpazzolare) && text.isValid(tag)) {
            if (testoDaSpazzolare.contains(tag)) {
                pos = testoDaSpazzolare.indexOf(tag);
                while (pos != -1) {
                    pos = testoDaSpazzolare.indexOf(tag, pos + tag.length());
                    numTag++;
                }// fine di while
            }// end of if cycle
        }// end of if cycle

        return numTag;
    } // fine del metodo


    /**
     * Restituisce il numero di occorrenze di una coppia di graffe di apertura nel testo. <br>
     * Le graffe possono essere poszionate in qualsiasi punto del testo <br>
     *
     * @param testoDaSpazzolare di riferimento
     *
     * @return numero di occorrenze - zero se non ce ne sono
     */
    public int getNumGraffeIni(String testoDaSpazzolare) {
        return getNumTag(testoDaSpazzolare, DOPPIE_GRAFFE_INI);
    } // fine del metodo


    /**
     * Restituisce il numero di occorrenze di una coppia di graffe di chiusura nel testo. <br>
     * Le graffe possono essere poszionate in qualsiasi punto del testo <br>
     *
     * @param testoDaSpazzolare di riferimento
     *
     * @return numero di occorrenze - zero se non ce ne sono
     */
    public int getNumGraffeEnd(String testoDaSpazzolare) {
        return getNumTag(testoDaSpazzolare, DOPPIE_GRAFFE_END);
    } // fine del metodo


    /**
     * Controlla che le occorrenze del tag iniziale e di quello finale si pareggino all'interno del testo. <br>
     * Ordine ed annidamento NON considerato <br>
     * Se inizio e fine sono uguali, devono esercene due per restituire un valore valido <br>
     *
     * @param testoDaSpazzolare di riferimento
     * @param tagIni            tag iniziale
     * @param tagEnd            tag finale
     *
     * @return vero se il numero di tagIni è uguale al numero di tagEnd
     */
    public boolean isPariTag(String testoDaSpazzolare, String tagIni, String tagEnd) {
        boolean pari = false;
        int numIni = 0;
        int numEnd = 0;

        // controllo di congruità
        if (text.isValid(testoDaSpazzolare) && text.isValid(tagIni) && text.isValid(tagEnd)) {
            if (tagIni.equals(tagEnd)) {
                numIni = getNumTag(testoDaSpazzolare, tagIni);
                pari = (numIni == 2);
            } else {
                numIni = getNumTag(testoDaSpazzolare, tagIni);
                numEnd = getNumTag(testoDaSpazzolare, tagEnd);
                pari = (numIni == numEnd);
            }// end of if/else cycle
        }// fine del blocco if

        return pari;
    } // fine del metodo


    /**
     * Controlla che le graffe di apertura 'pareggino' con le graffe di chiusura'. <br>
     * Ordine ed annidamento NON considerato <br>
     *
     * @param testoDaSpazzolare di riferimento
     *
     * @return vero se il numero di graffe di apertura è uguale al numero di graffe di chiusura
     */
    public boolean isPariGraffe(String testoDaSpazzolare) {
        return isPariTag(testoDaSpazzolare, DOPPIE_GRAFFE_INI, DOPPIE_GRAFFE_END);
    } // fine del metodo


    /**
     * Controlla le graffe interne al testo
     * <p>
     * Casi da controllare (all'interno delle graffe principali, già eliminate); i primi 7 non sono validi:
     * 1-......                         (manca)
     * 2-...{{...                       (mezza apertura)
     * 3-...{{...{{...}}...             (mezza apertura)
     * 4-...{{...}}...{{...             (mezza apertura)
     * 5-...}}...                       (mezza chiusura)
     * 6-...}}...{{...}}...             (mezza chiusura)
     * 7-...{{...}}...}}...             (mezza chiusura)
     * 8-...{{..}}...                   (singola)
     * 9-...{{}}...                     (vuota)
     * 10-{{..}}...                     (iniziale)
     * 11-...{{..}}                     (terminale)
     * 12-...{{..{{...}}...}}...        (interna)
     * 13-...{{..}}...{{...}}...        (doppie)
     * 14-...{{..}}..{{..}}..{{...}}... (tre o più)
     * 15-...{{..}}..|..{{..}}...       (due in punti diversi)
     * 16-...{{...|...}}...             (pipe interno)
     * 17-...{{...|...|...}}...         (doppio pipe)
     * 18-...{{..|...}}..|..{{..}}...   (due pipe in due graffe)
     * <p>
     * Se una o più graffe valide esistono, restituisce:
     * <p>
     * keyMapGraffeEsistono = se esistono                                       (boolean)
     * keyMapGraffeType = type della/della graffe trovate                       (EAGraffe)
     * keyMapGraffeNumero = quante ce ne sono                                   (integer)
     * keyMapGraffeTestoPrecedente = testo che precede la prima graffa o testo originale se non ce ne sono
     * keyMapGraffeListaWrapper = lista di WrapTreStringhe coi seguenti dati:   (list<WrapTreStringhe>)
     * - prima = valore del contenuto delle graffe                              (stringa)
     * - seconda = nome del parametro interessato                               (stringa)
     * - terza = valore completo del parametro che le contiene                  (stringa)
     *
     * @param testoDaSpazzolare di riferimento
     *
     * @return mappa col risultato
     */
    public HashMap<String, Object> checkGraffe(String testoDaSpazzolare) {
        HashMap<String, Object> mappa = new HashMap<>();
        boolean continua = false;
        String tagIni = DOPPIE_GRAFFE_INI;
        String tagEnd = DOPPIE_GRAFFE_END;
        int numIni = 0;
        int numEnd = 0;
        int posIni;
        int posEnd;
        WrapTreStringhe wrap;
        List<WrapTreStringhe> listaWrap = new ArrayList<>();
        String testoPrimaGraffa = VUOTA;
        int posIniSeconda = 0;
        int posEndSeconda = 0;
        String testoSecondaGraffa = VUOTA;
        int posIniTerza = 0;
        int posEndTerza = 0;
        String testoTerzaGraffa = VUOTA;

        //--reset con dati vuoti
        mappa.put(KEY_MAP_GRAFFE_ESISTONO, false);
        mappa.put(KEY_MAP_GRAFFE_TYPE, EAGraffe.manca);
        mappa.put(KEY_MAP_GRAFFE_NUMERO, 0);
        mappa.put(KEY_MAP_GRAFFE_TESTO_PRECEDENTE, testoDaSpazzolare);
        mappa.put(KEY_MAP_GRAFFE_LISTA_WRAPPER, null);

        if (text.isEmpty(testoDaSpazzolare)) {
            return mappa;
        }// fine del blocco if

        //--controllo di esistenza delle graffe
        //--se trova le mezze graffe, esce
        if (testoDaSpazzolare.contains(tagIni) && testoDaSpazzolare.contains(tagEnd)) {
            mappa.put(KEY_MAP_GRAFFE_ESISTONO, true);
        } else {
            mappa.put(KEY_MAP_GRAFFE_ESISTONO, false);
            if (testoDaSpazzolare.contains(tagIni)) {
                mappa.put(KEY_MAP_GRAFFE_TYPE, EAGraffe.mezzaInizio);
            }// end of if cycle
            if (testoDaSpazzolare.contains(tagEnd)) {
                mappa.put(KEY_MAP_GRAFFE_TYPE, EAGraffe.mezzaFine);
            }// end of if cycle

            return mappa;
        }// fine del blocco if-else

        //--conta se le graffe sono dispari
        numIni = getNumTag(testoDaSpazzolare, DOPPIE_GRAFFE_INI);
        numEnd = getNumTag(testoDaSpazzolare, DOPPIE_GRAFFE_END);
        if (numIni != numEnd) {
            if (numIni > numEnd) {
                mappa.put(KEY_MAP_GRAFFE_TYPE, EAGraffe.mezzaInizio);
            } else {
                mappa.put(KEY_MAP_GRAFFE_TYPE, EAGraffe.mezzaFine);
            }// end of if/else cycle
            mappa.put(KEY_MAP_GRAFFE_ESISTONO, false);

            return mappa;
        }// end of if/else cycle

        //--contiene un numero pari di graffe
        mappa.put(KEY_MAP_GRAFFE_NUMERO, numIni);
        posIni = testoDaSpazzolare.indexOf(DOPPIE_GRAFFE_INI) + DOPPIE_GRAFFE_INI.length();
        posEnd = testoDaSpazzolare.indexOf(DOPPIE_GRAFFE_END);
        testoPrimaGraffa = testoDaSpazzolare.substring(posIni, posEnd);
        listaWrap.add(new WrapTreStringhe(testoPrimaGraffa, VUOTA, VUOTA));
        mappa.put(KEY_MAP_GRAFFE_LISTA_WRAPPER, listaWrap);

        if (numIni >0) {
            if (text.isValid(testoPrimaGraffa)) {
                if (posIni == 2) {
                    mappa.put(KEY_MAP_GRAFFE_TYPE, EAGraffe.iniziale);
                } else {
                    if (posEnd + 2 == testoDaSpazzolare.length()) {
                        mappa.put(KEY_MAP_GRAFFE_TYPE, EAGraffe.finale);
                    } else {
                        mappa.put(KEY_MAP_GRAFFE_TYPE, EAGraffe.singola);
                    }// end of if/else cycle
                }// end of if/else cycle
            } else {
                mappa.put(KEY_MAP_GRAFFE_TYPE, EAGraffe.vuota);
            }// end of if/else cycle
        }// end of if cycle

        if (numIni > 1) {
            posIniSeconda = testoDaSpazzolare.indexOf(DOPPIE_GRAFFE_INI,posIni) + DOPPIE_GRAFFE_INI.length();
            posEndSeconda = testoDaSpazzolare.indexOf(DOPPIE_GRAFFE_END,posEnd+DOPPIE_GRAFFE_END.length());
            testoSecondaGraffa = testoDaSpazzolare.substring(posIniSeconda, posEndSeconda);
            listaWrap.add(new WrapTreStringhe(testoSecondaGraffa, VUOTA, VUOTA));
            mappa.put(KEY_MAP_GRAFFE_LISTA_WRAPPER, listaWrap);
            mappa.put(KEY_MAP_GRAFFE_TYPE, EAGraffe.doppie);
        }// end of if cycle

        if (numIni >2) {
            posIniTerza = testoDaSpazzolare.indexOf(DOPPIE_GRAFFE_INI,posIniSeconda) + DOPPIE_GRAFFE_INI.length();
            posEndTerza = testoDaSpazzolare.indexOf(DOPPIE_GRAFFE_END,posEndSeconda+DOPPIE_GRAFFE_END.length());
            testoTerzaGraffa = testoDaSpazzolare.substring(posIniTerza, posEndTerza);
            listaWrap.add(new WrapTreStringhe(testoTerzaGraffa, VUOTA, VUOTA));
            mappa.put(KEY_MAP_GRAFFE_LISTA_WRAPPER, listaWrap);
            mappa.put(KEY_MAP_GRAFFE_TYPE, EAGraffe.triple);
        }// end of if cycle

//        // spazzola il testo per ogni coppia di graffe
//        if (continua) {
//            while (testoTemplate.contains(tagIni) && testoTemplate.contains(tagEnd)) {
//                testoTemplate = LibBio.levaGraffa(mappa, testoTemplate);
//            } //fine del ciclo while
//        }// fine del blocco if

        return mappa;
    }// fine del metodo


}// end of class
