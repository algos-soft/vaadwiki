package it.algos.vaadwiki.service;


import com.vaadin.flow.component.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow.application.FlowCost.*;
import it.algos.vaadflow.enumeration.*;
import it.algos.vaadflow.modules.anno.*;
import it.algos.vaadflow.modules.giorno.*;
import it.algos.vaadflow.modules.log.*;
import it.algos.vaadflow.modules.preferenza.*;
import it.algos.vaadflow.service.*;
import static it.algos.vaadwiki.application.WikiCost.*;
import it.algos.vaadwiki.download.*;
import it.algos.vaadwiki.enumeration.*;
import it.algos.vaadwiki.modules.attivita.*;
import it.algos.vaadwiki.modules.bio.*;
import it.algos.vaadwiki.modules.nazionalita.*;
import it.algos.wiki.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.net.*;
import java.sql.*;
import java.time.*;
import java.util.*;


/**
 * Created by gac on 20 ago 2015.
 * Alcuni metodi sono statici per poter essere usati da una Enumeration <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class LibBio {


    /**
     * tag per la singola quadra di apertura
     */
    public static final String QUADRA_INI = "\\[";

    /**
     * tag per le doppie quadre di apertura
     */
    public static final String QUADRE_INI = QUADRA_INI + QUADRA_INI;

    /**
     * tag per la singola quadra di chiusura
     */
    public static final String QUADRA_END = "\\]";

    /**
     * tag per le doppie quadre di chiusura
     */
    public static final String QUADRE_END = QUADRA_END + QUADRA_END;

    /**
     * tag per la singola graffa di apertura
     */
    public static final String GRAFFA_INI = "\\{";

    /**
     * tag per le doppie graffe di apertura
     */
    public static final String GRAFFE_INI = GRAFFA_INI + GRAFFA_INI;

    /**
     * tag per la singola graffa di apertura
     */
    public static final String GRAFFA_END = "\\}";

    /**
     * tag per le doppie graffe di chiusura
     */
    public static final String GRAFFE_END = GRAFFA_END + GRAFFA_END;

    /**
     * tag di apertura delle note
     */
    public static final String REF_INI = "<ref>";

    /**
     * tag di chiusura delle note
     */
    public static final String REF_END = "</ref>";

    /**
     * tag di chiusura delle note
     */
    public static final String REF_END_DUE = "<ref/>";

    /**
     * tag pipe
     */
    public static final String PIPE = "|";

    /**
     * tag di apertura della parentesi tonda
     */
    public static final String TONDA_INI = "(";

    /**
     * tag di chiusura della parentesi tonda
     */
    public static final String TONDA_END = ")";


    /**
     * tag di apertura e chiusura di un apice
     */
    public static final String APICE = "'";

    /**
     * tag di apertura e chiusura di 3 apici per effetto bold
     */
    public static final String BOLD = APICE + APICE + APICE;

    /**
     * tag di apertura e chiusura di un paragrafo
     */
    public static final String PARAGRAFO = "==";

    /**
     * tag per i parametri bio
     */
    public static final String CIRCA = "circa";

    /**
     * tag per i parametri bio
     */
    public static final String INTERROGATIVO = "?";

    /**
     * Private final property
     */
    private static final LibBio INSTANCE = new LibBio();

    /**
     * Service (@Scope = 'singleton') iniettato da StaticContextAccessor e usato come libreria <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    public ATextService text = ATextService.getInstance();

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    public GiornoService giorno;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public AMongoService mongo;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public ADateService date;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public PreferenzaService pref;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public GiornoService giornoService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public AnnoService annoService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected BioService bio;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMailService mailService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected LogService logger;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private AnnoService anno;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private AttivitaService attivitaService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private NazionalitaService nazionalitaService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private ElaboraService elaboraService;


    /**
     * Private constructor to avoid client applications to use constructor
     */
    private LibBio() {
    }// end of constructor


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static LibBio getInstance() {
        return INSTANCE;
    }// end of static method


    /**
     * Estrae una mappa chiave valore dal testo di un template
     * Presuppone che le righe siano separate da pipe e return
     * Controllo della parità delle graffe interne (nel metodo estraeTemplate)
     * Gestisce l'eccezione delle graffe interne (nel metodo getMappaReali)
     * Elimina un eventuale pipe iniziale in tutte le chiavi della mappa
     *
     * @param testoTemplate del template
     *
     * @return mappa di TUTTI i parametri esistenti nel testo
     */
    public static LinkedHashMap getMappaReali(String testoTemplate) {
        return getMappaReali(testoTemplate, "");
    }// end of static method


    /**
     * Estrae una mappa chiave valore dal testo di un template
     * Presuppone che le righe siano separate da pipe e return
     * Controllo della parità delle graffe interne (nel metodo estraeTemplate)
     * Gestisce l'eccezione delle graffe interne (nel metodo getMappaReali)
     * Elimina un eventuale pipe iniziale in tutte le chiavi della mappa
     *
     * @param testoTemplate del template
     * @param titoloVoce    sulla wiki
     *
     * @return mappa di TUTTI i parametri esistenti nel testo
     */
    public static LinkedHashMap getMappaReali(String testoTemplate, String titoloVoce) {
        LinkedHashMap mappa = null;

        if (!testoTemplate.equals("")) {
            mappa = LibBio.estraeTmpMappa(testoTemplate);
            //            mappa = WikiLib.regolaMappaPipe(mappa);
            //            mappa = WikiLib.regolaMappaGraffe(mappa);
        }// fine del blocco if

        //--avviso di errore
        if (mappa == null && !titoloVoce.equals("")) {
            //            log.warn "getMappaRealiBio - La pagina ${titoloVoce}, non contiene ritorni a capo"
        }// fine del blocco if

        return mappa;
    }// end of static method


    /**
     * Estrae la mappa chiave/valore di un template dal testo completo di una voce
     * Gli estremi sono ESCLUSI
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     *
     * @param testoEssenziale testo essenziale del template, estremi esclusi
     *
     * @return mappa chiave/valore
     */
    public static LinkedHashMap estraeTmpMappa(String testoEssenziale) {
        LinkedHashMap mappa;
        String tagIni = "{{";

        if (testoEssenziale.startsWith(tagIni)) {
            testoEssenziale = estraeTmpRaw(testoEssenziale);
        }// fine del blocco if
        mappa = estraeMappaReali(testoEssenziale);

        return mappa;
    }// end of static method


    /**
     * Estrae il testo essenziale di un template dal testo completo di una voce
     * Gli estremi sono ESCLUSI
     * <p>
     * Il template DOVREBBE iniziare con {{Bio aCapo |
     * Il template DOVREBBE terminare con }} aCapo (eventuale)
     * Elimina doppie graffe iniziali e nome del template in modo che il raw parta col pipe
     * Elimina doppie graffe finali e aCapo (eventuale) finale
     *
     * @param testoTemplate testo completo del template
     *
     * @return testoEssenziale testo essenziale del template, estremi esclusi
     */
    public static String estraeTmpRaw(String testoTemplate) {
        String testoEssenziale = "";
        String testoDopoBio;
        String tagBio = "Bio";
        String tagEnd = "}}";
        int posBioEnd;
        int pos;

        posBioEnd = testoTemplate.indexOf(tagBio) + tagBio.length();
        testoDopoBio = testoTemplate.substring(posBioEnd);
        pos = testoDopoBio.indexOf(PIPE);
        if (pos != -1) {
            testoEssenziale = testoDopoBio.substring(pos);
            if (testoEssenziale.endsWith(tagEnd)) {
                testoEssenziale = testoEssenziale.substring(0, testoEssenziale.length() - tagEnd.length());
            }// fine del blocco if
        }// fine del blocco if-else

        return testoEssenziale;
    }// end of static method


    /**
     * Estrae una mappa chiave valore dal testo del template
     * Presuppone che le righe siano separate da pipe e return
     * Controlla che non ci siano doppie graffe annidate nel valore dei parametri
     *
     * @param testoTemplate testo completo del template
     *
     * @return mappa chiave/valore
     */
    public static LinkedHashMap estraeMappaReali(String testoTemplate) {
        LinkedHashMap mappa = null;
        LinkedHashMap mappaGraffe = null;
        boolean continua = false;
        String sep = PIPE;
        String sepRE = "\n\\|";
        String uguale = "=";
        String[] righe = null;
        String chiave;
        String valore;
        int pos;

        if (!testoTemplate.equals("")) {
            continua = true;
        }// fine del blocco if

        if (continua) {
            mappaGraffe = checkGraffe(testoTemplate);
            if (mappaGraffe.containsKey(KEY_MAP_GRAFFE_ESISTONO)) {
                //                testoTemplate = (String) mappaGraffe.get(KEY_MAP_GRAFFE_TESTO); //@todo non chiaro
            }// fine del blocco if
        }// fine del blocco if

        if (continua) {
            if (testoTemplate.startsWith(sep)) {
                testoTemplate = testoTemplate.substring(1).trim();
            }// fine del blocco if

            righe = testoTemplate.split(sepRE);
            if (righe.length == 1) {
                mappa = getMappaRigaUnica(testoTemplate);
                continua = false;
            }// fine del blocco if
        }// fine del blocco if

        if (continua) {
            if (righe != null) {
                mappa = new LinkedHashMap();

                for (String stringa : righe) {
                    pos = stringa.indexOf(uguale);
                    if (pos != -1) {
                        chiave = stringa.substring(0, pos).trim();
                        valore = stringa.substring(pos + 1).trim();
                        if (!chiave.equals("")) {
                            mappa.put(chiave, valore);
                        }// fine del blocco if
                    }// fine del blocco if
                } // fine del ciclo for-each

            }// fine del blocco if
        }// fine del blocco if

        // reinserisce il contenuto del parametro che eventualmente avesse avuto le doppie graffe
        if (continua) {
            if (mappaGraffe.containsKey(KEY_MAP_GRAFFE_ESISTONO)) {
                if ((Integer) mappaGraffe.get(KEY_MAP_GRAFFE_NUMERO) == 1) {
                    chiave = (String) mappaGraffe.get(KEY_MAP_GRAFFE_NOME_PARAMETRO);
                    valore = (String) mappaGraffe.get(KEY_MAP_GRAFFE_VALORE_PARAMETRO);
                    mappa.put(chiave, valore);
                }
                else {
                    for (int k = 0; k < (Integer) mappaGraffe.get(KEY_MAP_GRAFFE_NUMERO); k++) {
                        //                        chiave = mappaGraffe.nomeParGraffe[k];
                        //                        valore = mappaGraffe.valParGraffe[k];
                        //                        mappa.put(chiave, valore);
                    } // fine del ciclo for
                }// fine del blocco if-else
            }// fine del blocco if
        }// fine del blocco if

        return mappa;
    }// end of static method


    /**
     * Controlla le graffe interne al testo
     * <p>
     * Casi da controllare (all'interno delle graffe principali, già eliminate):
     * 1-...{{..}}...                   (singola)
     * 2-...{{}}...                     (vuota)
     * 3-{{..}}...                      (iniziale)
     * 3-...{{..}}                      (terminale)
     * 4-...{{..{{...}}...}}...         (interna)
     * 5-...{{..}}...{{...}}...         (doppie)
     * 6-...{{..}}..{{..}}..{{...}}...  (tre o più)
     * 7-...{{..}}..|..{{..}}...        (due in punti diversi)
     * 8-...{{...|...}}...              (pipe interno)
     * 8-...{{...|...|...}}...          (doppio pipe)
     * 7-...{{..|...}}..|..{{..}}...    (due pipe in due graffe)
     * 9-...{{....                      (singola apertura)
     * 10-...}}....                     (singola chiusura)
     * <p>
     * Se una o più graffe esistono, restituisce:
     * <p>
     * keyMapGraffeEsistono = se esistono                                       (boolean)
     * keyMapGraffeNumero = quante ce ne sono                                   (integer)
     * keyMapGraffeTestoPrecedente = testo che precede la prima graffa o testo originale se non ce ne sono
     * keyMapGraffeListaWrapper = lista di WrapTreStringhe coi seguenti dati:   (list<WrapTreStringhe>)
     * - prima = valore del contenuto delle graffe                              (stringa)
     * - seconda = nome del parametro interessato                               (stringa)
     * - terza = valore completo del parametro che le contiene                  (stringa)
     *
     * @param testoTemplate da analizzare
     *
     * @return mappa di risultati
     */
    public static LinkedHashMap checkGraffe(String testoTemplate) {
        LinkedHashMap mappa = null;
        boolean continua = false;
        String tagIni = "{{";
        String tagEnd = "}}";
        int numIni = getNumTag(testoTemplate, DOPPIE_GRAFFE_INI);
        int numEnd = getNumTag(testoTemplate, DOPPIE_GRAFFE_END);

        mappa = new LinkedHashMap();
        mappa.put(KEY_MAP_GRAFFE_ESISTONO, false);
        mappa.put(KEY_MAP_GRAFFE_NUMERO, 0);
        mappa.put(KEY_MAP_GRAFFE_TESTO_PRECEDENTE, testoTemplate);
        mappa.put(KEY_MAP_GRAFFE_VALORE_CONTENUTO, VUOTA);
        mappa.put(KEY_MAP_GRAFFE_NOME_PARAMETRO, VUOTA);
        mappa.put(KEY_MAP_GRAFFE_VALORE_PARAMETRO, VUOTA);

        if (!testoTemplate.equals("")) {
            continua = true;
        }// fine del blocco if

        // controllo di esistenza delle graffe
        if (continua) {
            if (testoTemplate.contains(tagIni) && testoTemplate.contains(tagEnd)) {
                mappa.put(KEY_MAP_GRAFFE_ESISTONO, true);
            }
            else {
                continua = false;
            }// fine del blocco if-else
        }// fine del blocco if

        // spazzola il testo per ogni coppia di graffe
        if (continua) {
            if (numIni == numEnd) {
                while (testoTemplate.contains(tagIni) && testoTemplate.contains(tagEnd)) {
                    testoTemplate = LibBio.levaGraffa(mappa, testoTemplate);
                } //fine del ciclo while
            }
            else {
            }// end of if/else cycle
        }// fine del blocco if

        return mappa;
    }// fine del metodo


    /**
     * Elabora ed elimina le prime graffe del testo
     * Regola la mappa
     * Restituisce il testo depurato delle prime graffe per ulteriore elaborazione
     *
     * @param testoTemplate testo completo del template
     */
    public static String levaGraffa(HashMap mappa, String testoTemplate) {
        String testoElaborato = "";
        boolean continua = false;
        String tagIni = "{{";
        String tagEnd = "}}";
        int posIni;
        int posEnd;
        String testoGraffa = "";

        if (mappa != null && !testoTemplate.equals("")) {
            testoElaborato = testoTemplate;
            continua = true;
        }// fine del blocco if

        // controllo di esistenza delle graffe
        if (continua) {
            if (testoTemplate.contains(tagIni) && testoTemplate.contains(tagEnd)) {
            }
            else {
                continua = false;
            }// fine del blocco if-else
        }// fine del blocco if

        // controllo (non si sa mai) che le graffe siano nell'ordine giusto
        if (continua) {
            posIni = testoTemplate.indexOf(tagIni);
            posEnd = testoTemplate.indexOf(tagEnd);
            if (posIni > posEnd) {
                continua = false;
            }// fine del blocco if
        }// fine del blocco if

        //spazzola il testo fino a pareggiare le graffe
        if (continua) {
            posIni = testoTemplate.indexOf(tagIni);
            posEnd = testoTemplate.indexOf(tagEnd, posIni);
            testoGraffa = testoTemplate.substring(posIni, posEnd + tagEnd.length());
            while (!isPariTag(testoGraffa, tagIni, tagEnd)) {
                posEnd = testoTemplate.indexOf(tagEnd, posEnd + tagEnd.length());
                if (posEnd != -1) {
                    testoGraffa = testoTemplate.substring(posIni, posEnd + tagEnd.length());
                }
                else {
                    mappa.put(KEY_MAP_GRAFFE_ESISTONO, false);
                    break;
                }// fine del blocco if-else
            } //fine del ciclo while
        }// fine del blocco if

        //estrae i dati rilevanti per la mappa
        //inserisce i dati nella mappa
        if (continua) {
            testoElaborato = regolaMappa(mappa, testoTemplate, testoGraffa);
        }// fine del blocco if

        return testoElaborato;
    }// fine del metodo


    /**
     * Elabora il testo della singola graffa
     * Regola la mappa
     */
    public static String regolaMappa(HashMap mappa, String testoTemplate, String testoGraffa) {
        String testoElaborato = "";
        boolean continua = false;
        ArrayList arrayValGraffe;
        ArrayList arrayNomeParGraffe;
        ArrayList arrayvValParGraffe;
        String valGraffe;
        String testoOut;
        String valParGraffe = "";
        String nomeParGraffe = "";
        String valRiga;
        String tagIni = "{{";
        String tagEnd = "}}";
        int posIni = 0;
        int posEnd = 0;
        String sep2 = "\n|";
        String txt = "";
        String sepParti = "=";
        String[] parti;
        int lenTemplate = 0;
        int numGraffe;
        String testo;

        // controllo di congruità
        if (mappa != null && !testoTemplate.equals("") && !testoGraffa.equals("")) {
            testoElaborato = LibBio.sostituisce(testoTemplate, testoGraffa, "");
            continua = true;
        }// fine del blocco if

        //estrae i dati rilevanti per la mappa
        //inserisce i dati nella mappa
        if (continua) {
            posIni = testoTemplate.indexOf(testoGraffa);
            posIni = testoTemplate.lastIndexOf(sep2, posIni);
            posIni += sep2.length();
            posEnd = testoTemplate.indexOf(sep2, posIni + testoGraffa.length());
            if (posIni == -1) {
                continua = false;
            }// fine del blocco if
            if (posEnd == -1) {
                posEnd = testoTemplate.length();
            }// fine del blocco if
        }// fine del blocco if

        //estrae i dati rilevanti per la mappa
        //inserisce i dati nella mappa
        if (continua) {
            valRiga = testoTemplate.substring(posIni, posEnd);
            posIni = valRiga.indexOf(sepParti);
            //nomeParGraffe = valRiga.substring(0, posIni).trim()
            //valParGraffe = valRiga.substring(posIni + sepParti.length()).trim()
            if (posIni != -1) {
                nomeParGraffe = valRiga.substring(0, posIni).trim();
                valParGraffe = valRiga.substring(posIni + sepParti.length()).trim();
            }
            else {
                continua = false;
            }// fine del blocco if-else
        }// fine del blocco if

        numGraffe = mappa.get(KEY_MAP_GRAFFE_NUMERO) != null ? (Integer) mappa.get(KEY_MAP_GRAFFE_NUMERO) : 0;
        numGraffe++;
        switch (numGraffe) {
            case 1:
                mappa.put(KEY_MAP_GRAFFE_VALORE_CONTENUTO, testoGraffa);
                mappa.put(KEY_MAP_GRAFFE_NOME_PARAMETRO, nomeParGraffe);
                mappa.put(KEY_MAP_GRAFFE_VALORE_PARAMETRO, valParGraffe);
                break;
            case 2:
                arrayValGraffe = new ArrayList();
                String oldValGraffe;
                oldValGraffe = (String) mappa.get(KEY_MAP_GRAFFE_VALORE_CONTENUTO);
                arrayValGraffe.add(oldValGraffe);
                arrayValGraffe.add(testoGraffa);
                mappa.put(KEY_MAP_GRAFFE_VALORE_CONTENUTO, arrayValGraffe);

                arrayNomeParGraffe = new ArrayList();
                String oldNomeParGraffe;
                oldNomeParGraffe = (String) mappa.get(KEY_MAP_GRAFFE_VALORE_PARAMETRO);
                arrayNomeParGraffe.add(oldNomeParGraffe);
                arrayNomeParGraffe.add(nomeParGraffe);
                mappa.put(KEY_MAP_GRAFFE_VALORE_PARAMETRO, arrayNomeParGraffe);

                //                arrayvValParGraffe = new ArrayList();
                //                String oldValParGraffe;
                //                arrayValGraffe = (ArrayList) mappa.get(KEY_MAP_GRAFFE_VALORE_CONTENUTO);
                //                arrayvValParGraffe.add(oldValParGraffe);
                //                arrayvValParGraffe.add(valParGraffe);
                //                mappa.put(KEY_MAP_GRAFFE_VALORE_CONTENUTO, arrayvValParGraffe);
                break;
            default: // caso non definito
                arrayValGraffe = (ArrayList) mappa.get(KEY_MAP_GRAFFE_VALORE_CONTENUTO);
                arrayValGraffe.add(testoGraffa);
                mappa.put(KEY_MAP_GRAFFE_VALORE_CONTENUTO, arrayValGraffe);

                arrayNomeParGraffe = (ArrayList) mappa.get(KEY_MAP_GRAFFE_VALORE_PARAMETRO);
                arrayNomeParGraffe.add(nomeParGraffe);
                mappa.put(KEY_MAP_GRAFFE_VALORE_PARAMETRO, arrayNomeParGraffe);

                arrayvValParGraffe = (ArrayList) mappa.get(KEY_MAP_GRAFFE_VALORE_PARAMETRO);
                arrayvValParGraffe.add(valParGraffe);
                mappa.put(KEY_MAP_GRAFFE_VALORE_PARAMETRO, arrayvValParGraffe);
                break;
        } // fine del blocco switch

        mappa.put(KEY_MAP_GRAFFE_NUMERO, numGraffe);
        mappa.put(KEY_MAP_GRAFFE_TESTO_PRECEDENTE, testoElaborato);

        return testoElaborato;
    }// fine della closure


    /**
     * Estrae una mappa chiave/valore dal testo contenuto tutto in una riga
     * Presuppone che la riga sia unica ed i parametri siano separati da pipe
     *
     * @param testo
     *
     * @return mappa chiave/valore
     */
    public static LinkedHashMap getMappaRigaUnica(String testo) {
        LinkedHashMap mappa = null;
        boolean continua = false;
        String sepRE = "\\|";
        String uguale = "=";
        String[] righe;
        String chiave;
        String valore;
        int pos;

        if (!testo.equals("")) {
            continua = true;
        }// fine del blocco if

        if (continua) {
            righe = testo.split(sepRE);
            if (righe != null && righe.length > 0) {
                mappa = new LinkedHashMap();

                for (String stringa : righe) {
                    pos = stringa.indexOf(uguale);
                    if (pos != -1) {
                        chiave = stringa.substring(0, pos).trim();
                        valore = stringa.substring(pos + 1).trim();
                        if (!chiave.equals("")) {
                            mappa.put(chiave, valore);
                        }// fine del blocco if
                    }// fine del blocco if
                } // fine del ciclo for-each

            }// fine del blocco if
        }// fine del blocco if

        return mappa;
    }// fine della closure


    /**
     * Controlla che le occorrenze dei due tag si pareggino all'interno del testo.
     *
     * @param testo  di riferimento
     * @param tagIni di apertura
     * @param tagEnd di chiusura
     *
     * @return vero se il numero di tagIni è uguale al numero di tagEnd
     */
    public static boolean isPariTag(String testo, String tagIni, String tagEnd) {
        boolean pari = false;
        int numIni;
        int numEnd;

        if (!testo.equals("") && !tagIni.equals("") && !tagEnd.equals("")) {
            numIni = getNumTag(testo, tagIni);
            numEnd = getNumTag(testo, tagEnd);
            pari = (numIni == numEnd);
        }// fine del blocco if

        return pari;
    } // fine del metodo


    /**
     * Controlla se nel testo ci sono occorrenze pari delle graffe.
     * Le graffe devono anche essere nel giusto ordine
     *
     * @param testo in ingresso
     *
     * @return vero se le occorrenze di apertura e chiusura sono uguali
     */
    public static boolean isPariGraffe(String testo) {
        return isPariTag(testo, GRAFFE_INI, GRAFFE_END);
    } // fine del metodo


    /**
     * Controlla se nel testo ci sono occorrenze pari delle quadre.
     * Le graffe devono anche essere nel giusto ordine
     *
     * @param testo in ingresso
     *
     * @return vero se le occorrenze di apertura e chiusura sono uguali
     */
    public static boolean isPariQuadre(String testo) {
        return isPariTag(testo, QUADRE_INI, QUADRE_END);
    } // fine del metodo


    /**
     * Restituisce il valore di occorrenze del tag nel testo.
     *
     * @param testo da analizzare
     * @param tag   da cercare
     *
     * @return numero di occorrenze
     * zero se non ce ne sono
     */
    @Deprecated // sostituito da ABioService.getNumTag()
    public static int getNumTag(String testo, String tag) {
        int numTag = 0;
        int pos;

        if (!testo.equals("") && !tag.equals("")) {
            if (testo.contains(tag)) {
                pos = testo.indexOf(tag);
                while (pos != -1) {
                    pos = testo.indexOf(tag, pos + tag.length());
                    numTag++;
                }// fine di while
            }
            else {
                numTag = 0;
            }// fine del blocco if-else
        }// fine del blocco if

        return numTag;
    } // fine del metodo


    /**
     * Regola una mappa chiave
     * Elimina un eventuale pipe iniziale in tutte le chiavi
     *
     * @param mappaIn dei parametri in entrata
     *
     * @return mappa dei parametri in uscita
     */
    public static LinkedHashMap regolaMappaPipe(Map mappaIn) {
        LinkedHashMap mappaOut = null;
        String chiave;
        String valore;
        String pipe = "|";
        int pos;

        if (mappaIn != null && mappaIn.size() > 0) {
            mappaOut = new LinkedHashMap();

            for (Object key : mappaIn.keySet()) {
                chiave = (String) key;
                valore = (String) mappaIn.get(key);
                if (chiave.startsWith(pipe)) {
                    pos = chiave.lastIndexOf(pipe);
                    pos++;
                    chiave = chiave.substring(pos);
                }// fine del blocco if
                mappaOut.put(chiave, valore);
            } // fine del ciclo for-each

        }// fine del blocco if

        return mappaOut;
    } // fine del metodo


    /**
     * Elimina il testo se inizia con un pipe
     */
    public static String regValore(String valoreIn) {
        String valoreOut = valoreIn;
        String pipe = "|";

        if (valoreIn.startsWith(pipe)) {
            valoreOut = "";
        }// fine del blocco if

        return valoreOut.trim();
    } // fine del metodo


    /**
     * Elimina le graffe finali
     */
    public static String regGraffe(String valoreIn) {
        String valoreOut = valoreIn;
        String graffe = "}}";

        if (valoreIn.endsWith(graffe)) {
            valoreOut = valoreOut.substring(0, valoreOut.length() - graffe.length());
        }// fine del blocco if

        return valoreOut.trim();
    } // fine del metodo


    /**
     * Controlla il primo aCapo che trova:
     * - se è all'interno di doppie graffe, non leva niente
     * - se non ci sono dopppie graffe, leva dopo l' aCapo
     */
    public static String regACapo(String valoreIn) {
        String valoreOut = valoreIn;
        String aCapo = "\n";
        String doppioACapo = aCapo + aCapo;
        String pipeACapo = aCapo + "|";
        int pos;
        HashMap mappaGraffe;

        if (!valoreIn.equals("") && valoreIn.contains(doppioACapo)) {
            valoreOut = valoreOut.replace(doppioACapo, aCapo);
        }// fine del blocco if

        if (!valoreIn.equals("") && valoreIn.contains(pipeACapo)) {
            mappaGraffe = LibBio.checkGraffe(valoreIn);

            if (mappaGraffe.containsKey(KEY_MAP_GRAFFE_ESISTONO)) {
            }
            else {
                pos = valoreIn.indexOf(pipeACapo);
                valoreOut = valoreIn.substring(0, pos);
            }// fine del blocco if-else
        }// fine del blocco if

        return valoreOut.trim();
    } // fine del metodo


    /**
     * Elimina un valore strano trovato (ed invisibile)
     * ATTENZIONE: non è uno spazio vuoto !
     * Trattasi del carattere: C2 A0 ovvero U+00A0 ovvero NO-BREAK SPACE
     * Non viene intercettato dal comando Java TRIM()
     */
    public static String regBreakSpace(String valoreIn) {
        String valoreOut = valoreIn;
        String strano = " ";   //NON cancellare: sembra uno spazio, ma è un carattere invisibile

        if (valoreIn.startsWith(strano)) {
            valoreOut = valoreIn.substring(1);
        }// fine del blocco if

        if (valoreIn.endsWith(strano)) {
            valoreOut = valoreIn.substring(0, valoreIn.length() - 1);
        }// fine del blocco if

        return valoreOut.trim();
    } // fine del metodo


    /**
     * Cancella un record
     * <p>
     * Cancella il record di BioWiki
     * Cancella (se esiste) anche il corrispondente record (medesimo pageid) di BioOriginale
     * Cancella (se esiste) anche il corrispondente record (medesimo pageid) di BioValida
     * Cancella (se esiste) anche il corrispondente record (medesimo pageid) di BioLista
     *
     * @param pageid della voce
     *
     * @deprecated
     */
    public static boolean delete(long pageid) {
        boolean status = false;
        //        BioWiki bioWiki;
        //        BioOriginale bioOriginale;
        //        BioValida bioValida;
        //        BioLista bioLista;
        //
        //        bioWiki = BioWiki.findPageid(pageid);
        //        if (bioWiki != null) {
        //            bioWiki.delete();
        //        }// fine del blocco if
        //
        //        bioOriginale = BioOriginale.findPageid(pageid);
        //        if (bioOriginale != null) {
        //            bioOriginale.delete();
        //        }// fine del blocco if
        //
        //        bioValida = BioValida.findPageid(pageid);
        //        if (bioValida != null) {
        //            bioValida.delete();
        //        }// fine del blocco if
        //
        //        bioLista = BioLista.findPageid(pageid);
        //        if (bioLista != null) {
        //            bioLista.delete();
        //        }// fine del blocco if

        return status;
    } // fine del metodo


    /**
     * Confronta il template risultante
     * <p>
     * Costruisce un template 'corretto' e lo confronta con quello attuale
     * A seconda del flag, considera il template presente sul server, oppure quello riformulato in BioOriginale
     * La differenza sono gli spazi e l'ordine dei parametri, mentre i valori sono gli stessi in entrambi i casi
     */
    public static boolean isTemplateDiversi(long pageid) {
        boolean status = true;
        //        BioWiki bioWiki;
        //        BioOriginale bioOrig;
        String tmplBioWiki = "";
        String tmplBioOriginario = "";
        String tmplBioNew = "";

        //        bioWiki = BioWiki.findPageid(pageid);
        //        if (bioWiki != null) {
        //            tmplBioWiki = bioWiki.getTmplBio();
        //        }// end of if cycle
        //
        //        bioOrig = BioOriginale.findPageid(pageid);
        //        if (bioOrig != null) {
        //            tmplBioOriginario = bioOrig.getTmplBio();
        //            tmplBioNew = bioOrig.creaNewTmpl();
        //        }// end of if cycle

        if (tmplBioNew.equals(tmplBioWiki)) {
            status = false;
        }// end of if cycle

        return status;
    }// end of static method


    /**
     * //    //--regola la lunghezza del campo
     * //    //--elimina il teasto successivo al ref
     * //    //--elimina il testo successivo alle note
     * //    //--elimina il testo successivo alle graffe
     * //    //--tronca comunque il testo a 255 caratteri
     * //    public String fixCampo( String testoIn) {
     * //        String testoOut = testoIn;
     * //
     * //        if (testoOut != null && !testoOut.equals("")) {
     * //            testoOut = testoOut.trim();
     * //            testoOut = LibText.levaDopoRef(testoOut);
     * //            testoOut = LibText.levaDopoNote(testoOut);
     * //            testoOut = LibText.levaDopoGraffe(testoOut);
     * //            testoOut = LibWiki.setNoQuadre(testoOut);
     * //            testoOut = testoOut.trim();
     * //        }// fine del blocco if
     * //
     * //        if (testoOut != null && testoOut.length() > 253) {
     * //            testoOut = testoOut.substring(0, 252);
     * //        }// fine del blocco if
     * //
     * //        return testoOut;
     * //    } // fine del metodo
     * <p>
     * <p>
     * /**
     * Aggiunge tag ref in testa e coda alla stringa.
     * Aggiunge SOLO se gia non esiste
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Elimina eventuali ref già presenti, per evitare di metterli doppi
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con tag ref aggiunti
     */
    public static String setRef(String stringaIn) {
        String stringaOut = stringaIn;

        stringaOut = REF_INI + stringaIn + REF_END;
        stringaOut = stringaOut.trim();

        return stringaOut;
    } // fine del metodo


    /**
     * Aggiunge una parentesi tonda in testa e coda alla stringa.
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con le parentesi aggiunte
     */
    public static String setTonde(String stringaIn) {
        return TONDA_INI + stringaIn.trim() + TONDA_END;
    } // fine del metodo


    /**
     * Aggiunge un apice in testa e coda alla stringa.
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con gli apici aggiunti
     */
    public static String setApici(String stringaIn) {
        return APICE + stringaIn.trim() + APICE;
    } // fine del metodo


    /**
     * Aggiunge 3 apici in testa e coda alla stringa per avere l'effetto bold.
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con i 3 apici aggiunti
     */
    public static String setBold(String stringaIn) {
        return BOLD + stringaIn.trim() + BOLD;
    } // fine del metodo

    //    /**
    //     * Elimina il testo successivo alla virgola
    //     *
    //     * @param testoIn entrata da elaborare
    //     * @return testoOut regolato in uscita
    //     * @deprecated
    //     */
    //    public static String fixCampoGiorno(String testoIn) {
    //        String testoOut = testoIn;
    //
    //        if (testoOut != null) {
    //            testoOut = testoOut.trim();
    //            testoOut = LibText.levaDopoInterrogativo(testoOut);
    //            testoOut = testoOut.trim();
    //        }// fine del blocco if
    //
    //        return testoOut;
    //    }// end of static method

    //    /**
    //     * Elimina il testo successivo alla virgola
    //     *
    //     * @param testoIn entrata da elaborare
    //     * @return testoOut regolato in uscita
    //     */
    //    public static String fixCampoAnno(String testoIn) {
    //        String testoOut = testoIn;
    //
    //        if (testoOut != null) {
    //            testoOut = testoOut.trim();
    //            testoOut = LibText.levaDopoInterrogativo(testoOut);
    //            testoOut = testoOut.trim();
    //        }// fine del blocco if
    //
    //        return testoOut;
    //    }// end of static method


    /**
     * Costruisce il titolo del paragrafo.
     * Aggiunge un ritorno a capo
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con la formattazione del paragrafo
     */
    public static String setParagrafo(String stringaIn) {
        return PARAGRAFO + stringaIn.trim() + PARAGRAFO;
    } // fine del metodo


    /**
     * Estrae una subLista parziale dalla lista.
     *
     * @param lista  da cui estrarre la subList
     * @param posIni iniziale
     * @param posEnd finale
     *
     * @return subLista
     */
    public static ArrayList subList(ArrayList<Long> lista, int posIni, int posEnd) {
        ArrayList subLista = null;
        List listaTmp = null;

        if (lista != null && lista.size() > 0) {
            if (posEnd > posIni) {
                if (posEnd < lista.size()) {
                    listaTmp = lista.subList(posIni, posEnd);
                }// end of if cycle
            }// end of if cycle
        }// fine del blocco if

        if (listaTmp != null && listaTmp instanceof List) {
            subLista = new ArrayList(listaTmp);
        }// end of if cycle

        return subLista;
    } // fine del metodo

    //    /**
    //     * Recupera i pageids di tutti i records, selezionati secondo la query ricevuta
    //     *
    //     * @param queryTxt per la selezione
    //     * @return lista di pageids (Long)
    //     */
    //    public synchronized static ArrayList<Long> queryFind(String queryTxt) {
    //        return queryFind(queryTxt, 0);
    //    }// end of static method

    //    /**
    //     * Recupera la property (text) di tutti i records, selezionati secondo la query ricevuta
    //     *
    //     * @param queryTxt per la selezione
    //     * @return lista di una property (String)
    //     */
    //    public synchronized static ArrayList<String> queryFindTxt(String queryTxt) {
    //        return queryFindTxt(queryTxt, 0);
    //    }// end of static method

    //    /**
    //     * Recupera i pageids dei primi (limit) records, selezionati secondo la query ricevuta
    //     *
    //     * @param queryTxt per la selezione
    //     * @param limit    di ricerca per la query
    //     *
    //     * @return lista di pageids (Long)
    //     */
    //    public synchronized static ArrayList<Long> queryFind(String queryTxt, int limit) {
    //        return queryFind(queryTxt, limit, 0);
    //    }// end of static method

    //    /**
    //     * Recupera la property (text) di tutti i records, selezionati secondo la query ricevuta
    //     *
    //     * @param queryTxt per la selezione
    //     * @param limit    di ricerca per la query
    //     *
    //     * @return lista di una property (String)
    //     */
    //    public synchronized static ArrayList<String> queryFindTxt(String queryTxt, int limit) {
    //        return queryFindTxt(queryTxt, limit, 0);
    //    }// end of static method

    //    /**
    //     * Recupera i pageids dei primi (limit) records, partendo da offSet, selezionati secondo la query ricevuta
    //     *
    //     * @param queryTxt per la selezione
    //     * @param limit    di ricerca per la query
    //     * @param offSet   di inizio per la query
    //     * @return lista di pageids (Long)
    //     */
    //    public synchronized static ArrayList<Long> queryFind(String queryTxt, int limit, int offSet) {
    //        ArrayList<Long> lista = null;
    //        List vettore;
    //        EntityManager manager = EM.createEntityManager();
    //        Query query = manager.createQuery(queryTxt);
    //
    //        if (limit > 0) {
    //            query.setMaxResults(limit);
    //        }// fine del blocco if
    //
    //        if (offSet > 0) {
    //            query.setFirstResult(offSet);
    //        }// fine del blocco if
    //
    //        vettore = query.getResultList();
    //        if (vettore != null) {
    //            lista = new ArrayList<Long>(vettore);
    //        }// end of if cycle
    //        manager.close();
    //
    //        return lista;
    //    }// end of static method

    //    /**
    //     * Recupera la property (text) di tutti i records, selezionati secondo la query ricevuta
    //     *
    //     * @param queryTxt per la selezione
    //     * @param limit    di ricerca per la query
    //     * @param offSet   di inizio per la query
    //     * @return lista di una property (String)
    //     */
    //    public synchronized static ArrayList<String> queryFindTxt(String queryTxt, int limit, int offSet) {
    //        ArrayList<String> lista = null;
    //        List vettore;
    //        EntityManager manager = EM.createEntityManager();
    //        Query query = manager.createQuery(queryTxt);
    //
    //        if (limit > 0) {
    //            query.setMaxResults(limit);
    //        }// fine del blocco if
    //
    //        if (offSet > 0) {
    //            query.setFirstResult(offSet);
    //        }// fine del blocco if
    //
    //        vettore = query.getResultList();
    //        if (vettore != null) {
    //            lista = new ArrayList<String>(vettore);
    //        }// end of if cycle
    //        manager.close();
    //
    //        return lista;
    //    }// end of static method

    //    /**
    //     * Recupera il conteggio dei records, selezionati secondo la query ricevuta
    //     *
    //     * @param queryTxt per la selezione
    //     * @return numero di records
    //     */
    //    public synchronized static int queryCount(String queryTxt) {
    //        int numero = 0;
    //        List vettore;
    //        EntityManager manager = EM.createEntityManager();
    //        Query query = manager.createQuery(queryTxt);
    //
    //        vettore = query.getResultList();
    //        if (vettore != null) {
    //            numero = vettore.size();
    //        }// end of if cycle
    //        manager.close();
    //
    //        return numero;
    //    }// end of static method


    /**
     * Crea una lista dalle matrici
     */
    public static ArrayList<String> sumTag(String[]... matrici) {
        ArrayList<String> lista = null;

        if (matrici != null && matrici.length > 0) {
            lista = new ArrayList<>();
            for (String[] matrice : matrici) {
                for (String singleTag : matrice) {
                    lista.add(singleTag);
                }// end of for cycle
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// fine del metodo


    /**
     * Sostituisce tutte le occorrenze.
     * Esegue solo se il testo è valido
     *
     * @param testoIn    in ingresso
     * @param oldStringa da eliminare
     * @param newStringa da sostituire
     *
     * @return testoOut convertito
     */
    public static String sostituisce(String testoIn, String oldStringa, String newStringa) {
        String testoOut = testoIn;
        int pos = 0;
        String prima = "";

        if (testoIn != null && oldStringa != null && newStringa != null) {
            testoOut = testoIn.trim();
            if (testoIn.contains(oldStringa)) {

                while (pos != -1) {
                    pos = testoIn.indexOf(oldStringa);
                    if (pos != -1) {
                        prima += testoIn.substring(0, pos);
                        prima += newStringa;
                        pos += oldStringa.length();
                        testoIn = testoIn.substring(pos);
                    }// fine del blocco if
                }// fine di while

                testoOut = prima + testoIn;
            }// fine del blocco if
        }// fine del blocco if

        return testoOut;
    }// end of static method


    /**
     * Aggiunge tag 'NoInclude' in testa e coda alla stringa.
     * <p>
     * Aggiunge SOLO se già non esiste TODO Non ancora
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Inserisce i tag (iniziale e finale) su due nuove righe
     * Elimina eventuali 'NoInclude' già presenti, per evitare di metterli doppi TODO Non ancora
     *
     * @param stringaIn in ingresso
     *
     * @return stringa coi tag aggiunti
     */
    public static String setNoIncludeMultiRiga(String stringaIn) {
        return setNoIncludeBase(stringaIn, true);
    } // fine del metodo


    /**
     * Aggiunge tag 'NoInclude' in testa e coda alla stringa.
     * <p>
     * Aggiunge SOLO se già non esiste TODO Non ancora
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Inserisce i tag (iniziale e finale) sulla stessa riga di testo
     * Elimina eventuali 'NoInclude' già presenti, per evitare di metterli doppi TODO Non ancora
     *
     * @param stringaIn in ingresso
     *
     * @return stringa coi tag aggiunti
     */
    public static String setNoIncludeRiga(String stringaIn) {
        return setNoIncludeBase(stringaIn, false);
    } // fine del metodo

    //    /**
    //     * Elimina gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
    //     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
    //     * Mantiene il punto interrogativo, anche se da solo <br>
    //     * @param testoOriginario in entrata da elaborare
    //     *
    //     * @return testoGrezzo troncato
    //     */
    //    public static String troncaParteFinaleMenoPuntoInterrogativo(String testoOriginario) {
    //        String testoGrezzo = VUOTA;
    //
    //        if (text.isEmpty(testoOriginario)) {
    //            return VUOTA;
    //        }// end of if cycle
    //
    //        testoGrezzo = testoOriginario.trim();
    //        testoGrezzo = text.levaDopoRef(testoGrezzo);
    //        testoGrezzo = text.levaDopoNote(testoGrezzo);
    //        testoGrezzo = text.levaDopoGraffe(testoGrezzo);
    //        testoGrezzo = text.levaDopoVirgola(testoGrezzo);
    //        testoGrezzo = testoGrezzo.trim();
    //
    //        return testoGrezzo;
    //    } // fine del metodo


    /**
     * Aggiunge tag 'NoInclude' in testa e coda alla stringa.
     * <p>
     * Aggiunge SOLO se già non esiste TODO Non ancora
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Inserisce i tag (iniziale e finale) sulla stessa riga di testo
     * Elimina eventuali 'NoInclude' già presenti, per evitare di metterli doppi TODO Non ancora
     *
     * @param stringaIn in ingresso
     *
     * @return stringa coi tag aggiunti
     */
    private static String setNoIncludeBase(String stringaIn, boolean righeDiverse) {
        String stringaOut = stringaIn.trim();
        String tagIni = "<noinclude>";
        String tagEnd = "</noinclude>";

        if (!stringaIn.equals("")) {
            stringaOut = tagIni;
            if (righeDiverse) {
                stringaOut += A_CAPO;
            }// end of if cycle
            stringaOut += stringaIn.trim();
            if (righeDiverse) {
                stringaOut += A_CAPO;
            }// end of if cycle
            stringaOut += tagEnd;
            stringaOut = stringaOut.trim();
        }// fine del blocco if

        return stringaOut;
    } // fine del metodo


    /**
     * Elimina gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * Elimina il punto interrogativo, se da solo <br>
     * Mantiene il punto interrogativo, se da solo <br>
     *
     * @param testoOriginario in entrata da elaborare
     *
     * @return testoGrezzo troncato
     */
    public String troncaParteFinaleGiornoAnno(String testoOriginario) {
        String testoGrezzo = VUOTA;
        testoGrezzo = text.levaDopoVirgola(testoOriginario);

        return troncaParteFinalePuntoInterrogativo(testoGrezzo);
    } // fine del metodo


    /**
     * Elimina gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * Elimina il punto interrogativo, se da solo <br>
     * Mantiene il punto interrogativo, se da solo <br>
     *
     * @param testoOriginario in entrata da elaborare
     *
     * @return testoGrezzo troncato
     */
    public String troncaParteFinalePuntoInterrogativo(String testoOriginario) {
        String testoGrezzo = VUOTA;

        if (text.isEmpty(testoOriginario)) {
            return VUOTA;
        }// end of if cycle

        testoGrezzo = testoOriginario.trim();
        testoGrezzo = text.levaDopoRef(testoGrezzo);
        testoGrezzo = text.levaDopoNote(testoGrezzo);
        testoGrezzo = text.levaDopoGraffe(testoGrezzo);
        //        testoGrezzo = text.levaDopoVirgola(testoGrezzo);
        testoGrezzo = text.levaDopoCirca(testoGrezzo);
        if (!testoGrezzo.equals("?")) {
            testoGrezzo = text.levaDopoInterrogativo(testoGrezzo);
        }// end of if cycle

        testoGrezzo = testoGrezzo.trim();

        return testoGrezzo;
    } // fine del metodo


    /**
     * Restituisce un valore grezzo troncato dopo alcuni tag chiave <br>
     * <p>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * Eventuali parti terminali inutili vengono scartate ma devono essere conservate a parte per il template <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) <br>
     */
    public String estraeValoreInizialeGrezzoPuntoAmmesso(String valorePropertyTmplBioServer) {
        return troncaDopoTag(valorePropertyTmplBioServer, true);
    } // fine del metodo


    /**
     * Restituisce un valore grezzo troncato dopo alcuni tag chiave <br>
     * <p>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * Eventuali parti terminali inutili vengono scartate ma devono essere conservate a parte per il template <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) <br>
     */
    public String estraeValoreInizialeGrezzoPuntoEscluso(String valorePropertyTmplBioServer) {
        return troncaDopoTag(valorePropertyTmplBioServer, false);
    } // fine del metodo


    /**
     * Elimina gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * <p>
     * Tag chiave di troncatura sempre validi:
     * REF = "<ref"
     * NOTE = "<!--"
     * GRAFFE = "{{"
     * UGUALE = "="
     * CIRCA = "circa";
     * ECC = "ecc."
     * INTERROGATIVO = "?"
     * <p>
     * Tag chiave di troncatura opzionali a seconda del parametro:
     * PARENTESI = "("
     * VIRGOLA = ","
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) <br>
     */
    public String troncaDopoTag(String valorePropertyTmplBioServer, boolean puntoAmmesso) {
        String valoreGrezzo = valorePropertyTmplBioServer.trim();

        if (text.isEmpty(valorePropertyTmplBioServer)) {
            return VUOTA;
        }// end of if cycle

        if (valorePropertyTmplBioServer.equals(INTERROGATIVO) && puntoAmmesso) {
            return INTERROGATIVO;
        }// end of if cycle

        valoreGrezzo = text.levaDopoRef(valoreGrezzo);
        valoreGrezzo = text.levaDopoNote(valoreGrezzo);
        valoreGrezzo = text.levaDopoGraffe(valoreGrezzo);
        valoreGrezzo = text.levaDopoUguale(valoreGrezzo);
        valoreGrezzo = text.levaDopoCirca(valoreGrezzo);
        valoreGrezzo = text.levaDopoEccetera(valoreGrezzo);
        valoreGrezzo = text.levaDopoInterrogativo(valoreGrezzo);

        //        testoGrezzo = text.levaDopoVirgola(testoGrezzo);
        //        testoGrezzo = troncaParteFinalePuntoInterrogativo(valorePropertyTmplBioServer);
        //        testoGrezzo = text.levaDopoInterrogativo(testoGrezzo);
        //        testoGrezzo = testoGrezzo.trim();

        return valoreGrezzo.trim();
    } // fine del metodo


    /**
     * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
     * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valoreGrezzo in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String fixValoreGrezzo(String valoreGrezzo) {
        String valoreValido = valoreGrezzo.trim();

        if (text.isEmpty(valoreGrezzo)) {
            return VUOTA;
        }// end of if cycle

        valoreValido = LibWiki.setNoQuadre(valoreValido);

        return valoreValido.trim();
    }// end of method


    /**
     * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
     *
     * @param valoreGrezzo in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String fixMaiuscola(String valoreGrezzo) {
        String valoreValido = fixValoreGrezzo(valoreGrezzo);

        if (text.isEmpty(valoreGrezzo)) {
            return VUOTA;
        }// end of if cycle

        valoreValido = text.primaMaiuscola(valoreValido);

        return valoreValido.trim();
    }// end of method


    /**
     * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
     *
     * @param valoreGrezzo in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String fixMinuscola(String valoreGrezzo) {
        String valoreValido = fixValoreGrezzo(valoreGrezzo);

        if (text.isEmpty(valoreGrezzo)) {
            return VUOTA;
        }// end of if cycle

        valoreValido = text.primaMinuscola(valoreValido);

        return valoreValido.trim();
    }// end of method


    /**
     * Regola questo campo
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixNomeValido(String testoGrezzo) {
        return fixValoreGrezzo(testoGrezzo);
    } // // end of method


    /**
     * Regola questo campo
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixCognomeValido(String testoGrezzo) {
        return fixValoreGrezzo(testoGrezzo);
    } // // end of method


    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     * Elimina il testo se NON contiene una spazio vuoto (tipico della data giorno-mese) <br>
     * Elimina eventuali spazi vuoti DOPPI o TRIPLI (tipico della data tra il giorno ed il mese) <br>
     * Forza a minuscolo il primo carattere del mese <br>
     * Forza a ordinale un eventuale primo giorno del mese scritto come numero o come grado <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixGiornoValido(String testoGrezzo) {
        return fixGiornoValido(testoGrezzo, false);
    } // // end of method else


    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     * Elimina il testo se NON contiene una spazio vuoto (tipico della data giorno-mese) <br>
     * Elimina eventuali spazi vuoti DOPPI o TRIPLI (tipico della data tra il giorno ed il mese) <br>
     * Forza a minuscolo il primo carattere del mese <br>
     * Forza a ordinale un eventuale primo giorno del mese scritto come numero o come grado <br>
     * A seconda del flag:
     * CONTROLLA che il valore esista nella collezione Giorno <br>
     * NON controlla che il valore esista nella collezione Giorno <br>
     *
     * @param testoGrezzo in entrata da elaborare
     * @param controlla   il valore nella collezione Giorno
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixGiornoValido(String testoGrezzo, boolean controlla) {
        String testoValido = fixValoreGrezzo(testoGrezzo);
        int pos;
        String primo;
        String mese;

        //--il punto interrogativo da solo è valido (il metodo fixPropertyBase lo elimina)
        if (testoGrezzo.trim().equals("?")) {
            return testoGrezzo;
        }// end of if cycle

        if (text.isEmpty(testoValido)) {
            return VUOTA;
        }// end of if cycle

        testoValido = text.levaDopo(testoValido, CIRCA);

        //--spazio singolo
        testoValido = text.fixOneSpace(testoValido);

        //--senza spazio
        if (!testoValido.contains(SPAZIO)) {
            testoValido = separaMese(testoValido);
        }// end of if cycle

        if (!testoValido.contains(SPAZIO)) {
            return VUOTA;
        }// end of if cycle

        //--minuscola
        testoValido = testoValido.toLowerCase();

        //--Forza a ordinale un eventuale primo giorno del mese scritto come numero o come grado
        if (testoValido.contains(SPAZIO)) {
            pos = testoValido.indexOf(SPAZIO);
            primo = testoValido.substring(0, pos);
            mese = testoValido.substring(pos + SPAZIO.length());

            if (primo.equals("1") || primo.equals("1°")) {
                primo = "1º";
                testoValido = primo + SPAZIO + mese;
            }// end of if cycle
        }// end of if cycle

        if (text.isValid(testoValido)) {
            if (controlla) {
                if (giornoService.isEsiste(testoValido)) {
                    return testoValido.trim();
                }
                else {
                    return VUOTA;
                }// end of if/else cycle
            }
            else {
                return testoValido.trim();
            }// end of if/else cycle
        }
        else {
            return VUOTA;
        }// end of if/else cycle

    } // // end of method


    public String separaMese(String testoTuttoAttaccato) {
        String testoSeparato = testoTuttoAttaccato.trim();
        String giorno = VUOTA;
        String mese = VUOTA;
        String inizio;

        if (testoSeparato.contains(SPAZIO)) {
            return testoSeparato;
        }// end of if cycle

        if (Character.isDigit(testoSeparato.charAt(0))) {
            if (Character.isDigit(testoSeparato.charAt(1))) {
                giorno = testoSeparato.substring(0, 2);
                mese = testoSeparato.substring(2);
            }
            else {
                giorno = testoSeparato.substring(0, 1);
                mese = testoSeparato.substring(1);
            }// end of if/else cycle
        }
        else {
            return testoSeparato;
        }// end of if/else cycle

        inizio = mese.substring(0, 1);
        if (inizio.equals(TRATTINO) || inizio.equals(SLASH)) {
            mese = mese.substring(1);
        }// end of if cycle

        if (text.isValid(giorno) && text.isValid(mese)) {
            testoSeparato = giorno + SPAZIO + mese;
        }// end of if cycle

        return testoSeparato;
    } // // end of method


    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     * Elimina il testo se contiene la dicitura 'circa' (tipico dell'anno)
     * A seconda del flag:
     * CONTROLLA che il valore esista nella collezione Giorno <br>
     * NON controlla che il valore esista nella collezione Giorno <br>
     *
     * @param testoGrezzo in entrata da elaborare
     * @param controlla   il valore nella collezione Anno
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixAnnoValido(String testoGrezzo, boolean controlla) {
        String testoValido = fixValoreGrezzo(testoGrezzo);

        //--il punto interrogativo da solo è valido (il metodo fixPropertyBase lo elimina)
        if (testoGrezzo.trim().equals("?")) {
            return testoGrezzo;
        }// end of if cycle

        if (text.isEmpty(testoGrezzo)) {
            return VUOTA;
        }// end of if cycle

        testoValido = text.levaDopo(testoValido, CIRCA);

        if (text.isValid(testoValido)) {
            if (controlla) {
                if (annoService.isEsiste(testoValido)) {
                    return testoValido.trim();
                }
                else {
                    return VUOTA;
                }// end of if/else cycle
            }
            else {
                return testoValido.trim();
            }// end of if/else cycle
        }
        else {
            return VUOTA;
        }// end of if/else cycle
    } // fine del metodo


    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     * A seconda del flag:
     * CONTROLLA che il valore esista nella collezione Giorno <br>
     * NON controlla che il valore esista nella collezione Giorno <br>
     *
     * @param testoGrezzo in entrata da elaborare
     * @param controlla   il valore nella collezione Anno
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixAttivitaValida(String testoGrezzo, boolean controlla) {
        String testoValido = fixValoreGrezzo(testoGrezzo).toLowerCase();
        String tag1 = "ex ";
        String tag2 = "ex-";

        if (text.isEmpty(testoValido)) {
            return VUOTA;
        }// end of if cycle

        if (text.isValid(testoValido)) {
            if (controlla) {
                if (attivitaService.isEsiste(testoValido)) {
                    return testoValido.trim();
                }
                else {
                    if (testoValido.startsWith(tag1)) {
                        testoValido = testoValido.substring(testoValido.indexOf(tag1) + tag1.length()).trim();
                    }// end of if cycle
                    if (testoValido.startsWith(tag2)) {
                        testoValido = testoValido.substring(testoValido.indexOf(tag1) + tag1.length()).trim();
                    }// end of if cycle
                    if (attivitaService.isEsiste(testoValido)) {
                        return testoValido.trim();
                    }
                    else {
                        return VUOTA;
                    }
                }// end of if/else cycle
            }
            else {
                return testoValido.trim();
            }// end of if/else cycle
        }
        else {
            return VUOTA;
        }// end of if/else cycle

    } // fine del metodo


    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     * A seconda del flag:
     * CONTROLLA che il valore esista nella collezione Giorno <br>
     * NON controlla che il valore esista nella collezione Giorno <br>
     *
     * @param testoGrezzo in entrata da elaborare
     * @param controlla   il valore nella collezione Anno
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixNazionalitaValida(String testoGrezzo, boolean controlla) {
        String testoValido = fixValoreGrezzo(testoGrezzo).toLowerCase();

        if (text.isEmpty(testoValido)) {
            return VUOTA;
        }// end of if cycle

        ////        if (text.isValid(testoValido) && mongo.isEsisteByProperty(Nazionalita.class, "singolare", testoValido)) {
        //        if (text.isValid(testoValido)) {
        //            return testoValido.trim();
        //        } else {
        //            return VUOTA;
        //        }// end of if/else cycle

        if (text.isValid(testoValido)) {
            if (controlla) {
                if (nazionalitaService.isEsiste(testoValido)) {
                    return testoValido.trim();
                }
                else {
                    return VUOTA;
                }// end of if/else cycle
            }
            else {
                return testoValido.trim();
            }// end of if/else cycle
        }
        else {
            return VUOTA;
        }// end of if/else cycle

    } // fine del metodo


    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     * A seconda del flag:
     * CONTROLLA che il valore sia valido - solo M o F <br>
     *
     * @param testoGrezzo in entrata da elaborare
     * @param controlla   il valore nella collezione Anno
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixSessoValido(String testoGrezzo, boolean controlla) {
        String testoValido = fixValoreGrezzo(testoGrezzo);
        testoValido = testoValido.toLowerCase();

        if (testoValido.equals("m") || testoValido.equals("maschio") || testoValido.equals("uomo")) {
            testoValido = "M";
        }// end of if cycle

        if (testoValido.equals("f") || testoValido.equals("femmina") || testoValido.equals("donna")) {
            testoValido = "F";
        }// end of if cycle

        if (controlla) {
            if (testoValido.equals("trans") || testoValido.equals("incerto") || testoValido.equals("non si sa") || testoValido.equals("dubbio") || testoValido.equals("?")) {
                testoValido = VUOTA;
            }// end of if cycle
        }// end of if cycle

        return testoValido;
    } // // end of method


    /**
     * Elimina (eventuali) doppie quadre in testa e coda della stringa.
     * <p>
     * Funziona solo se le quadre sono esattamente in TESTA ed in CODA alla stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn entrata da elaborare
     *
     * @return testoIn regolato in uscita con doppie quadre eliminate
     */
    public String setNoQuadre(String testoIn) {
        String testoOut = testoIn;

        if (testoOut != null) {
            if (testoIn.length() > 0) {
                testoOut = testoIn.trim();
                testoOut = text.levaTesta(testoOut, QUADRA_INI);
                testoOut = text.levaTesta(testoOut, QUADRA_INI);
                testoOut = text.levaCoda(testoOut, QUADRA_END);
                testoOut = text.levaCoda(testoOut, QUADRA_END);
                testoOut = testoOut.trim();
            }// fine del blocco if
        }// fine del blocco if

        return testoOut;
    }// end of method


    /**
     * Estrae una mappa chiave valore per un fix di parametri, da una biografia
     * <p>
     * E impossibile sperare in uno schema fisso
     * Occorre considerare le {{ graffe annidate, i | (pipe) annidati
     * i mancati ritorni a capo, ecc., ecc.
     * <p>
     * Uso la lista dei parametri che può riconoscere
     * (è meno flessibile, ma più sicuro)
     * Cerco il primo parametro nel testo e poi spazzolo il testo per cercare
     * il primo parametro noto e così via
     *
     * @param bio istanza della Entity
     *
     * @return mappa dei parametri esistenti nella enumeration e presenti nel testo
     */
    public LinkedHashMap<String, String> getMappaBio(Bio bio) {
        return getMappaDownload(bio.tmplBioServer);
    }// end of method


    /**
     * Estrae una mappa chiave valore per un fix di parametri, dal testo di una biografia <br>
     * <p>
     * E impossibile sperare in uno schema fisso
     * I parametri sono spesso scritti in ordine diverso da quello previsto
     * Occorre considerare le {{ graffe annidate, i | (pipe) annidati
     * i mancati ritorni a capo, ecc., ecc.
     * <p>
     * Uso la lista dei parametri che può riconoscere
     * (è meno flessibile, ma più sicuro)
     * Cerco il primo parametro nel testo e poi spazzolo il testo per cercare
     * il primo parametro noto e così via
     *
     * @param bio entityBio da cui estrarre il tmplBioServer
     *
     * @return mappa dei parametri esistenti nella enumeration e presenti nel testo
     */
    public LinkedHashMap<String, String> getMappaDownload(Bio bio) {
        LinkedHashMap<String, String> mappa = null;
        String tmplBioServer = VUOTA;

        if (bio != null) {
            tmplBioServer = bio.tmplBioServer;
        }// end of if cycle

        if (text.isValid(tmplBioServer)) {
            mappa = getMappaDownload(tmplBioServer);
        }// end of if cycle

        return mappa;
    }// end of method


    /**
     * Estrae una mappa chiave-valore per un fix di parametri, dal testo di una biografia <br>
     * <p>
     * E impossibile sperare in uno schema fisso
     * I parametri sono spesso scritti in ordine diverso da quello previsto
     * Occorre considerare le {{ graffe annidate, i | (pipe) annidati
     * i mancati ritorni a capo, ecc., ecc.
     * <p>
     * Uso la lista dei parametri che può riconoscere
     * (è meno flessibile, ma più sicuro)
     * Cerco il primo parametro nel testo e poi spazzolo il testo per cercare
     * il primo parametro noto e così via
     *
     * @param valorePropertyTmplBioServer del template Bio
     *
     * @return mappa dei parametri esistenti nella enumeration e presenti nel testo
     */
    public LinkedHashMap<String, String> getMappaDownload(String valorePropertyTmplBioServer) {
        LinkedHashMap<String, String> mappa = null;
        LinkedHashMap<Integer, String> mappaTmp = new LinkedHashMap<Integer, String>();
        String chiave;
        //        String sep = PIPE;
        //        String sep1 = PIPE + SPAZIO;
        //        String sep2 = PIPE + SPAZIO + SPAZIO;
        //        String spazio = " ";
        String uguale = "=";
        String valore = "";
        int pos = 0;
        int posUgu;
        //        ArrayList listaTag;
        int posEnd;

        if (valorePropertyTmplBioServer != null && !valorePropertyTmplBioServer.equals("")) {
            mappa = new LinkedHashMap();
            for (ParBio par : ParBio.values()) {
                if (par == ParBio.titolo) {
                    continue;
                }
                valore = par.getTag();

                try { // prova ad eseguire il codice
                    pos = text.getPosFirstTag(valorePropertyTmplBioServer, valore);
                } catch (Exception unErrore) { // intercetta l'errore
                    int a = 87;
                }// fine del blocco try-catch
                if (pos > 0) {
                    mappaTmp.put(pos, valore);
                }// fine del blocco if
            } // fine del ciclo for-each

            Object[] matrice = mappaTmp.keySet().toArray();
            Arrays.sort(matrice);
            ArrayList<Object> lista = new ArrayList<Object>();
            for (Object lungo : matrice) {
                lista.add(lungo);
            } // fine del ciclo for-each

            for (int k = 1; k <= lista.size(); k++) {
                chiave = mappaTmp.get(lista.get(k - 1));

                try { // prova ad eseguire il codice
                    if (k < lista.size()) {
                        posEnd = (Integer) lista.get(k);
                    }
                    else {
                        posEnd = valorePropertyTmplBioServer.length();
                    }// fine del blocco if-else
                    valore = valorePropertyTmplBioServer.substring((Integer) lista.get(k - 1), posEnd);
                } catch (Exception unErrore) { // intercetta l'errore
                    int c = 76;
                }// fine del blocco try-catch
                if (!valore.equals("")) {
                    valore = valore.trim();
                    posUgu = valore.indexOf(uguale);
                    if (posUgu != -1) {
                        posUgu += uguale.length();
                        valore = valore.substring(posUgu).trim();
                    }// fine del blocco if
                    valore = regValore(valore);
                    if (!LibBio.isPariTag(valore, "{{", "}}")) {
                        valore = regGraffe(valore);
                    }// end of if cycle
                    valore = regACapo(valore);
                    valore = regBreakSpace(valore);
                    valore = valore.trim();
                    mappa.put(chiave, valore);
                }// fine del blocco if
            } // fine del ciclo for
        }// fine del blocco if

        return mappa;
    }// end of method


    /**
     * Mappa chiave-valore con i valori 'troncati' <br>
     * Valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
     *
     * @param mappaDownload coi valori originali provenienti dalla property tmplBioServer della entity Bio
     *
     * @return mappa con i valori 'troncati'
     */
    public LinkedHashMap<String, String> getMappaTroncata(LinkedHashMap<String, String> mappaDownload) {
        LinkedHashMap<String, String> mappa = null;
        ParBio par = null;
        String key = VUOTA;
        String value = VUOTA;

        if (mappaDownload != null) {
            mappa = new LinkedHashMap<String, String>();
            for (Map.Entry<String, String> entry : mappaDownload.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                par = ParBio.getType(key);
                value = par.estraeValoreInizialeGrezzo(value);
                mappa.put(entry.getKey(), value);
            }// end of for cycle
        }// end of if cycle

        return mappa;
    }// end of method


    /**
     * Mappa chiave-valore con i valori 'elaborati' <br>
     * Valore elaborato valido (minuscole, quadre, ecc.) <br>
     *
     * @param mappaTroncata dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante
     *
     * @return mappa con i valori 'elaborati'
     */
    public LinkedHashMap<String, String> getMappaElaborata(LinkedHashMap<String, String> mappaTroncata) {
        LinkedHashMap<String, String> mappa = null;
        ParBio par = null;
        String key = VUOTA;
        String value = VUOTA;

        if (mappaTroncata != null) {
            mappa = new LinkedHashMap<String, String>();
            for (Map.Entry<String, String> entry : mappaTroncata.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                par = ParBio.getType(key);
                value = par.regolaValoreInizialeValido(value);
                mappa.put(entry.getKey(), value);
            }// end of for cycle
        }// end of if cycle

        return mappa;
    }// end of method


    /**
     * Mappa chiave-valore con i valori 'validi' <br>
     * Valore elaborato valido (minuscole, quadre, ecc.) <br>
     *
     * @param mappaElaborata con i valori validi (minuscole, quadre, ecc.)
     *
     * @return mappa con i valori 'validi'
     */
    public LinkedHashMap<String, String> getMappaValida(LinkedHashMap<String, String> mappaElaborata) {
        LinkedHashMap<String, String> mappa = null;

        return mappa;
    }// end of method


    /**
     * Mappa chiave-valore con i valori 'validi' <br>
     * Valore elaborato valido (minuscole, quadre, ecc.) <br>
     *
     * @param mappaElaborata con i valori validi (minuscole, quadre, ecc.)
     *
     * @return mappa con i valori
     */
    public LinkedHashMap<String, String> getMappaUpload(LinkedHashMap<String, String> mappaElaborata) {
        LinkedHashMap<String, String> mappa = null;

        return mappa;
    }// end of method


    /**
     * Estrae una mappa chiave valore per un fix di parametri, dal testo di una biografia
     * <p>
     * E impossibile sperare in uno schema fisso
     * I parametri sono spesso scritti in ordine diverso da quello previsto
     * Occorre considerare le {{ graffe annidate, i | (pipe) annidati
     * i mancati ritorni a capo, ecc., ecc.
     * <p>
     * Uso la lista dei parametri che può riconoscere
     * (è meno flessibile, ma più sicuro)
     * Cerco il primo parametro nel testo e poi spazzolo il testo per cercare
     * il primo parametro noto e così via
     *
     * @param testoTemplate del template Bio
     *
     * @return mappa dei parametri esistenti nella enumeration e presenti nel testo
     */
    public LinkedHashMap<String, String> getMappaBioOld(String testoTemplate) {
        LinkedHashMap<String, String> mappa = null;
        LinkedHashMap<Integer, String> mappaTmp = new LinkedHashMap<Integer, String>();
        //        Collection lista = null;
        String chiave;
        String sep = PIPE;
        String sep1 = PIPE + SPAZIO;
        String sep2 = PIPE + SPAZIO + SPAZIO;
        String spazio = " ";
        String uguale = "=";
        //        String tab = "\t";
        String valore = "";
        int pos = 0;
        int posUgu;
        ArrayList listaTag;
        int posEnd;

        if (testoTemplate != null && !testoTemplate.equals("")) {
            mappa = new LinkedHashMap();
            for (ParBio par : ParBio.values()) {
                valore = par.getTag();
                listaTag = new ArrayList();
                //                listaTag.add(sep + valore + spazio);
                listaTag.add(sep + valore + uguale);
                //                listaTag.add(sep + valore + tab);
                listaTag.add(sep + valore + spazio + uguale);
                //                listaTag.add(sep1 + valore + spazio);
                listaTag.add(sep1 + valore + uguale);
                //                listaTag.add(sep1 + valore + tab);
                listaTag.add(sep1 + valore + spazio + uguale);
                //                listaTag.add(sep2 + valore + spazio);
                listaTag.add(sep2 + valore + uguale);
                //                listaTag.add(sep2 + valore + tab);
                listaTag.add(sep2 + valore + spazio + uguale);

                try { // prova ad eseguire il codice
                    pos = text.getPos(testoTemplate, listaTag);
                } catch (Exception unErrore) { // intercetta l'errore
                    //                    log.error testoTemplate
                }// fine del blocco try-catch
                if (pos > 0) {
                    mappaTmp.put(pos, valore);
                }// fine del blocco if
            } // fine del ciclo for-each

            Object[] matrice = mappaTmp.keySet().toArray();
            Arrays.sort(matrice);
            ArrayList<Object> lista = new ArrayList<Object>();
            for (Object lungo : matrice) {
                lista.add(lungo);
            } // fine del ciclo for-each

            for (int k = 1; k <= lista.size(); k++) {
                chiave = mappaTmp.get(lista.get(k - 1));

                try { // prova ad eseguire il codice
                    if (k < lista.size()) {
                        posEnd = (Integer) lista.get(k);
                    }
                    else {
                        posEnd = testoTemplate.length();
                    }// fine del blocco if-else
                    valore = testoTemplate.substring((Integer) lista.get(k - 1), posEnd);
                } catch (Exception unErrore) { // intercetta l'errore
                    int c = 76;
                }// fine del blocco try-catch
                if (!valore.equals("")) {
                    valore = valore.trim();
                    posUgu = valore.indexOf(uguale);
                    if (posUgu != -1) {
                        posUgu += uguale.length();
                        valore = valore.substring(posUgu).trim();
                    }// fine del blocco if
                    valore = regValore(valore);
                    if (!LibBio.isPariTag(valore, "{{", "}}")) {
                        valore = regGraffe(valore);
                    }// end of if cycle
                    valore = regACapo(valore);
                    valore = regBreakSpace(valore);
                    valore = valore.trim();
                    mappa.put(chiave, valore);
                }// fine del blocco if
            } // fine del ciclo for

            //            for (int chiave : mappaTmp.keySet()) {
            ////                chiave = (Integer)mappaTmp.get(posizione - 1);
            //                valore = testoTemplate.substring(chiave, chiave + mappaTmp.get(chiave).length());
            //                if (!valore.equals("")) {
            //                    valore = valore.trim();
            //                    posUgu = valore.indexOf(uguale);
            //                    if (posUgu != -1) {
            //                        posUgu += uguale.length();
            //                        valore = valore.substring(posUgu).trim();
            //                    }// fine del blocco if
            //                    valore = regValore(valore);
            //                    valore = regACapo(valore);
            //                    valore = regBreakSpace(valore);
            //                    valore = valore.trim();
            //                    mappa.put(chiave, valore);
            //                }// fine del blocco if
            //
            //            } // fine del ciclo for-each

            //            HashMap.Entry lis = mappaTmp.keySet();
            //            if (lista != null) {
            //                lista.add(testoTemplate.length());
            //
            //                for (int k = 1; k < lista.size(); k++) {
            //                    chiave = mappaTmp.get(lista.get(k - 1));
            //                    valore = testoTemplate.substring((int) lista.get(k - 1), (int) lista.get(k));
            //                    if (!valore.equals("")) {
            //                        valore = valore.trim();
            //                        posUgu = valore.indexOf(uguale);
            //                        if (posUgu != -1) {
            //                            posUgu += uguale.length();
            //                            valore = valore.substring(posUgu).trim();
            //                        }// fine del blocco if
            //                        valore = regValore(valore);
            //                        valore = regACapo(valore);
            //                        valore = regBreakSpace(valore);
            //                        valore = valore.trim();
            //                        mappa.put(chiave, valore);
            //                    }// fine del blocco if
            //
            //                } // fine del ciclo for
            //            }// fine del blocco if
        }// fine del blocco if

        //        mappa = WikiLib.regolaMappaPipe(mappa);
        //        mappa = WikiLib.regolaMappaGraffe(mappa);

        return mappa;
    } // fine del metodo


    /**
     * Regola questa property <br>
     * <p>
     * Elimina spazi iniziali e finali <br>
     * Elimina il testo successivo al ref <br>
     * Elimina il testo successivo alle note <br>
     * Elimina il testo successivo alle graffe <br>
     * Elimina il testo successivo alla virgola <br>
     * Elimina il testo successivo al punto interrogativo <br>
     * Elimina eventuali quadre <br>
     * Tronca comunque il testo a 255 caratteri (segnalando un warning) <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    @Deprecated
    public String fixPropertyBase(String testoGrezzo) {
        String testoValido;

        if (text.isEmpty(testoGrezzo)) {
            return VUOTA;
        }// end of if cycle

        testoValido = testoGrezzo.trim();
        testoValido = text.levaDopoRef(testoValido);
        testoValido = text.levaDopoNote(testoValido);
        testoValido = text.levaDopoGraffe(testoValido);
        testoValido = text.levaDopoVirgola(testoValido);
        testoValido = text.levaDopoInterrogativo(testoValido);
        testoValido = LibWiki.setNoQuadre(testoValido);
        testoValido = testoValido.trim();

        return testoValido;
    }// end of method


    /**
     * Regola questo campo
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixLuogoValido(String testoGrezzo) {
        String testoValido;

        if (text.isEmpty(testoGrezzo)) {
            return VUOTA;
        }// end of if cycle

        testoValido = testoGrezzo.trim();
        testoValido = text.levaDopoRef(testoValido);
        testoValido = text.levaDopoNote(testoValido);
        testoValido = text.levaDopoGraffe(testoValido);
        testoValido = text.levaDopoInterrogativo(testoValido);
        testoValido = LibWiki.setNoQuadre(testoValido);
        testoValido = testoValido.trim();

        if (testoValido.length() > 253) {
            testoValido = testoValido.substring(0, 252);
            //@todo manca warning
        }// fine del blocco if

        return testoValido;
    } // // end of method


    /**
     * Regola questo campo
     * <p>
     * Elimina il testo successivo a varii tag (fixPropertyBase)
     * Elimina il testo se NON contiene una spazio vuoto (tipico della data giorno-mese)
     * Elimina eventuali DOPPI spazi vuoto (tipico della data tra il giorno ed il mese)
     * Controlla che il valore esista nella collezione Giorno
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return istanza di giorno valido
     */
    public Giorno fixGiornoLink(String testoGrezzo) {
        Giorno giorno = null;
        String testoValido = "";

        if (text.isValid(testoGrezzo)) {
            testoValido = fixGiornoValido(testoGrezzo, true);
        }// end of if cycle

        if (text.isValid(testoValido)) {
            giorno = giornoService.findByKeyUnica(testoValido);
        }// end of if cycle

        return giorno;
    } // // end of method


    /**
     * Regola questo campo
     * <p>
     * Elimina il testo successivo a varii tag (fixPropertyBase)
     * Elimina il testo se NON contiene una spazio vuoto (tipico della data giorno-mese)
     * Elimina eventuali DOPPI spazi vuoto (tipico della data tra il giorno ed il mese)
     * Controlla che il valore esista nella collezione Giorno
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return istanza di anno valido
     */
    public Anno fixAnnoLink(String testoGrezzo) {
        Anno anno = null;
        String testoValido = "";

        if (text.isValid(testoGrezzo)) {
            testoValido = fixAnnoValido(testoGrezzo, true);
        }// end of if cycle

        if (text.isValid(testoValido)) {
            anno = annoService.findByKeyUnica(testoValido);
        }// end of if cycle

        return anno;
    } // fine del metodo


    /**
     * Regola questo campo
     * <p>
     * Elimina il testo successivo a varii tag (fixPropertyBase)
     * Controlla che il valore esista nella collezione Attività
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return istanza di attività valida
     */
    public Attivita fixAttivitaLink(String testoGrezzo) {
        Attivita attivita = null;
        String testoValido = "";

        if (text.isValid(testoGrezzo)) {
            testoValido = fixAttivitaValida(testoGrezzo, true);
        }// end of if cycle

        if (text.isValid(testoValido)) {
            attivita = attivitaService.findByKeyUnica(testoValido);
        }// end of if cycle

        return attivita;
    } // fine del metodo


    /**
     * Regola questo campo
     * <p>
     * Elimina il testo successivo a varii tag (fixPropertyBase)
     * Controlla che il valore esista nella collezione Nazionalità
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return istanza di nazionalità valida
     */
    public Nazionalita fixNazionalitaLink(String testoGrezzo) {
        Nazionalita nazionalita = null;
        String testoValido = "";

        if (text.isValid(testoGrezzo)) {
            testoValido = fixNazionalitaValida(testoGrezzo, true);
        }// end of if cycle

        if (text.isValid(testoValido)) {
            nazionalita = nazionalitaService.findByKeyUnica(testoValido);
        }// end of if cycle

        return nazionalita;
    } // fine del metodo


    /**
     * Crea la singola riga del template chiave=valore con pipe e ritorno a capo
     *
     * @param parBioText nome della property del template
     * @param value      corrispondente
     *
     * @return riga di testo
     */
    public String creaRigaTemplate(String parBioText, String value) {
        //        return PIPE + parBioText + UGUALE_SPAZIATO + value + A_CAPO;
        return PIPE + parBioText + "=" + value + A_CAPO;//@todo versione 14
    } // fine del metodo


    /**
     * Crea la singola riga del template chiave=valore con pipe e ritorno a capo
     * Rimanda la metodo base recuperando la stringa dal ParBio
     *
     * @param parBio enumeration da cui estrarre il nome della property
     * @param value  corrispondente
     *
     * @return riga di testo
     */
    public String creaRigaTemplate(ParBio parBio, String value) {
        return creaRigaTemplate(parBio.getTag(), value);
    } // fine del metodo


    /**
     * Crea tutte le righe della mappa ricevuta
     * Usa SOLO i parametri presenti nella mappa
     *
     * @param mappa chiave=valore da costruire
     *
     * @return testo del template senza graffe
     */
    public String creaTemplateSenzaGraffe(LinkedHashMap<String, String> mappa) {
        String templateText = "";

        for (Map.Entry<String, String> entry : mappa.entrySet()) {
            templateText += creaRigaTemplate(entry.getKey(), entry.getValue());
        }// end of for cycle
        templateText = templateText.trim();

        return templateText;
    } // fine del metodo


    /**
     * Crea il template bio della mappa ricevuta
     * Usa SOLO i parametri presenti nella mappa
     *
     * @param mappa chiave=valore da costruire
     *
     * @return testo del template bio comprensivo di graffe di apertura e chiusura
     */
    public String creaTemplate(LinkedHashMap<String, String> mappa) {
        String templateText = "";

        templateText += LibWiki.GRAFFE_INI;
        templateText += "Bio";
        templateText += A_CAPO;
        templateText += creaTemplateSenzaGraffe(mappa);
        templateText += A_CAPO;
        templateText += LibWiki.GRAFFE_END;

        return templateText;
    } // fine del metodo


    /**
     * Costruisce il template Bio della mappa ricevuta con i parametri (valorizzati) in ingresso
     * Nella mappa ci possono essere anche parametri non obbligatori che vengono mantenuti
     * Aggiunge (vuoti) i parametri obbligatori mancanti
     *
     * @param mappa chiave=valore da costruire
     *
     * @return testo del template senza graffe
     */
    public String creaTemplateBioSenzaGraffe(LinkedHashMap<ParBio, String> mappa) {
        String templateText = "";
        String value = "";

        for (ParBio parBio : ParBio.values()) {
            value = mappa.containsKey(parBio) ? mappa.get(parBio) : "";
            if (parBio.isCampoNormale()) {
                templateText += creaRigaTemplate(parBio.getTag(), value);
            }
            else {
                if (text.isValid(value)) {
                    templateText += creaRigaTemplate(parBio.getTag(), value);
                }// end of if cycle
            }// end of if/else cycle
        }// end of for cycle
        templateText = templateText.trim();

        return templateText;
    } // fine del metodo


    /**
     * Costruisce il template Bio della mappa ricevuta con i parametri (valorizzati) in ingresso
     * Nella mappa ci possono essere anche parametri non obbligatori che vengono mantenuti
     * Aggiunge (vuoti) i parametri obbligatori mancanti
     *
     * @param mappa chiave=valore da costruire
     *
     * @return testo del template bio comprensivo di graffe di apertura e chiusura
     */
    public String creaTemplateBio(LinkedHashMap<ParBio, String> mappa) {
        String templateText = "";

        templateText += LibWiki.GRAFFE_INI;
        templateText += "Bio";
        templateText += A_CAPO;
        templateText += creaTemplateBioSenzaGraffe(mappa);
        templateText += A_CAPO;
        templateText += LibWiki.GRAFFE_END;

        return templateText;
    } // fine del metodo


    /**
     * Costruisce il template Bio dai dati della entity
     * Aggiunge (vuoti) i parametri obbligatori mancanti
     * NON aggiunge altri parametri non presenti nella entity e non obbligatori
     *
     * @param entity di riferimento
     *
     * @return testo del template bio senza graffe
     */
    public String creaTemplateBioSenzaGraffe(Bio entity) {
        String templateText = "";
        String value = "";

        for (ParBio parBio : ParBio.getCampiNormali()) {
            value = parBio.getValue(entity);
            templateText += creaRigaTemplate(parBio, value);
        }// end of for cycle
        templateText = templateText.trim();

        return templateText;
    }// end of method


    /**
     * Costruisce il template Bio dai dati della entity
     * Aggiunge (vuoti) i parametri obbligatori mancanti
     * NON aggiunge altri parametri non presenti nella entity e non obbligatori
     *
     * @param entity di riferimento
     *
     * @return testo del template bio comprensivo di graffe di apertura e chiusura
     */
    public String creaTemplateBio(Bio entity) {
        String templateText = "";

        templateText += LibWiki.GRAFFE_INI;
        templateText += "Bio";
        templateText += A_CAPO;
        templateText += creaTemplateBioSenzaGraffe(entity);
        templateText += A_CAPO;
        templateText += LibWiki.GRAFFE_END;

        return templateText;
    }// end of method


    /**
     * Costruisce il template Bio
     * Merge tra il template del server ed i dati (eventualmente) modificati della entity
     * Aggiunge (vuoti) i parametri obbligatori mancanti
     * NON aggiunge altri parametri non presenti sul server e non obbligatori
     *
     * @param tmplBioServer template originale del server
     * @param entity        di riferimento
     *
     * @return testo del template bio senza graffe
     */
    public String creaTemplateMergedSenzaGraffe(String tmplBioServer, Bio entity) {
        String templateText = "";
        String key = "";
        String valueServer;
        String valueMongo;
        String valueMerged;
        HashMap<String, String> mappa = getMappaDownload(tmplBioServer);

        //--spazzola TUTTI i parametri possibili in ordine
        for (ParBio parBio : ParBio.values()) {
            key = parBio.getTag();
            valueServer = "";
            valueMongo = "";
            valueMerged = "";

            if (mappa.containsKey(key)) {
                valueServer = mappa.get(key);
                valueMongo = parBio.getValue(entity);
                valueMerged = text.isValid(valueMongo) ? valueMongo : valueServer;

                //se è un campo normale, lo aggiunge sempre
                if (parBio.isCampoNormale()) {
                    if (parBio == ParBio.attivita && text.isEmpty(valueServer) && text.isValid(mappa.get(ParBio.categorie.getTag())) && text.isValid(mappa.get(ParBio.fineIncipit.getTag()))) {
                        int a = 87;
                    }
                    else {
                        templateText += creaRigaTemplate(parBio, valueMerged);
                    }// end of if/else cycle
                }
                else {
                    //altrimenti lo aggiunge solo se non è vuoto
                    if (text.isValid(valueMerged)) {
                        templateText += creaRigaTemplate(parBio, valueMerged);
                    }// end of if cycle
                }// end of if/else cycle
            }
            else {
                //se mancava ed è un campo normale, lo aggiunge
                if (parBio.isCampoNormale()) {
                    valueMongo = parBio.getValue(entity);
                    valueMerged = text.isValid(valueMongo) ? valueMongo : "";
                    templateText += creaRigaTemplate(parBio, valueMerged);
                }// end of if cycle
            }// end of if/else cycle
        }// end of for cycle
        templateText = templateText.trim();

        return templateText;
    }// end of method


    /**
     * Costruisce il template Bio
     * Merge tra il template del server ed i dati (eventualmente) modificati della entity
     * Aggiunge (vuoti) i parametri obbligatori mancanti
     * NON aggiunge altri parametri non presenti sul server e non obbligatori
     *
     * @param tmplBioServer template originale del server
     * @param entity        di riferimento
     *
     * @return testo del template bio comprensivo di graffe di apertura e chiusura
     */
    public String mergeTemplates(String tmplBioServer, Bio entity) {
        String templateText = "";

        templateText += LibWiki.GRAFFE_INI;
        templateText += "Bio";
        templateText += A_CAPO;
        templateText += creaTemplateMergedSenzaGraffe(tmplBioServer, entity);
        templateText += A_CAPO;
        templateText += LibWiki.GRAFFE_END;

        return templateText;
    }// end of method

    //    /**
    //     * Costruisce il template Bio come modificato nella entity
    //     * Merge tra il template del server ed i dati (eventualmente) modificati della entity
    //     * Aggiunge (vuoti) i parametri obbligatori mancanti
    //     * NON aggiunge altri parametri non presenti sul server e non obbligatori
    //     *
    //     * @param bioServer col template originale
    //     * @param bioMongo  coi parametri eventualemnte modificati
    //     *
    //     * @return testo del template bio merged comprensivo di graffe di apertura e chiusura
    //     */
    //    public String mergeTemplates(Bio bioServer, Bio bioMongo) {
    //        String templateText = "";
    //
    //        templateText += LibWiki.GRAFFE_INI;
    //        templateText += "Bio";
    //        templateText += A_CAPO;
    ////        templateText += creaTemplateMergedSenzaGraffe(entity);
    //        templateText += A_CAPO;
    //        templateText += LibWiki.GRAFFE_END;
    //
    //        return templateText;
    //    }// end of method


    public void sendDownload(DownloadResult result) {
        sendMailCiclo(result, EACicloType.download);
    } // fine del metodo


    public void sendUpdate(DownloadResult result) {
        sendMailCiclo(result, EACicloType.update);
    } // fine del metodo


    public void sendMailCiclo(DownloadResult result, EACicloType type) {
        LocalDateTime end;
        InetAddress inetAddress = null;
        String tag = type.name();
        String testo = "";
        testo += "Eseguito in automatico ";
        testo += type.getSchedule();
        testo += type.getEsegue();
        testo += A_CAPO;

        try { // prova ad eseguire il codice
            inetAddress = InetAddress.getLocalHost();
            testo += "IP Address:- " + inetAddress.getHostAddress();
            testo += A_CAPO;
            testo += "Host Name:- " + inetAddress.getHostName();
            testo += A_CAPO;
        } catch (Exception unErrore) { // intercetta l'errore
            logger.error(unErrore.toString());
        }// fine del blocco try-catch

        testo += "Ciclo del " + date.get();
        testo += A_CAPO;
        testo += "Iniziato alle " + date.getOrario(result.getInizio());
        testo += A_CAPO;
        end = LocalDateTime.now();
        testo += "Terminato alle " + date.getOrario(end);
        testo += A_CAPO;
        testo += "Durata totale: " + date.deltaText(result.getInizioLong());
        testo += A_CAPO;
        testo += "Nella categoria " + result.getNomeCategoria() + " sono presenti al momento " + text.format(result.getNumVociCategoria()) + " wikipagine biografiche";

        if (result.getNumVociCancellate() > 0) {
            testo += A_CAPO;
            testo += "Sono state cancellate ";
            testo += text.format(result.getNumVociCancellate());
            testo += " pagine eccedenti (non più utilizzate nella categoria)";
        }// end of if cycle
        if (result.getNumVociCreate() > 0) {
            testo += A_CAPO;
            testo += "Sono state create ";
            testo += text.format(result.getNumVociCreate());
            testo += " nuove biografie";
        }// end of if cycle

        testo += A_CAPO;
        testo += "Nel mongoDB ci sono adesso " + text.format(bio.count()) + " biografie";

        if (result.getVociNonCreate().size() > 0) {
            testo += A_CAPO;
            testo += "Non sono state registrate le seguenti biografie:";
            for (int k = 0; k < result.getVociNonCreate().size(); k++) {
                testo += A_CAPO;
                testo += (k + 1) + ") " + result.getVociNonCreate().get(k);
            }// end of for cycle
        }// end of if cycle

        if (type == EACicloType.update) {
            testo += A_CAPO;
            testo += "Sono state aggiornate " + text.format(result.getNumVociAggiornate()) + " biografie su un totale di " + text.format(result.getNumVociDaAggiornare()) + " biografie modificate";
        }// end of if cycle

        if (pref.isBool(SEND_MAIL_CICLO)) {
            mailService.send("Ciclo " + tag, testo);
        }// end of if cycle

    } // fine del metodo

    //    public void sendUpdate(DownloadResult result) {
    //        LocalDateTime end;
    //        String tag = "update";
    //        String testo = "";
    //        testo += "Eseguito in automatico ";
    //        testo += EASchedule.oreQuattro.getNota();
    //        testo += A_CAPO;
    //        testo += A_CAPO;
    //        testo += "Aggiunge le pagine biografiche mancanti,";
    //        testo += A_CAPO;
    //        testo += " cancella quelle eccedenti ";
    //        testo += A_CAPO;
    //        testo += " e ricarica tutte quelle esistenti che sono state modificate dall'ultima lettura";
    //        testo += A_CAPO;
    //        testo += A_CAPO;
    //        testo += "Ciclo del " + date.get();
    //        testo += A_CAPO;
    //        testo += "Iniziato alle " + date.getOrario(result.inizio);
    //        testo += A_CAPO;
    //
    //
    //        if (pref.isBool(SEND_MAIL_CICLO)) {
    //            end = LocalDateTime.now();
    //            testo += "Terminato alle " + date.getOrario(end);
    //            testo += A_CAPO;
    //            testo += "Durata totale: " + date.deltaText(result.getInizio());
    //            testo += A_CAPO;
    //            testo += "Nella categoria " + result.nomeCategoria + " ci sono " + text.format(result.numVociCategoria) + " pagine biografiche";
    //            testo += A_CAPO;
    //            testo += "Nel mongoDB ci sono " + text.format(bio.count()) + " pagine biografiche";
    //
    //            if (result.vociEccedenti.size() > 0) {
    //                testo += A_CAPO;
    //                testo += "Sono state eliminate ";
    //                testo += text.format(result.vociEccedenti.size());
    //                testo += " pagine non più presenti nella categoria ";
    //                testo += result.nomeCategoria;
    //            }// end of if cycle
    //
    //            if (result.vociMancanti.size() > 0) {
    //                testo += A_CAPO;
    //                testo += "Sono state aggiunte ";
    //                testo += text.format(result.vociMancanti.size());
    //                testo += " nuove pagine della categoria ";
    //                testo += result.nomeCategoria;
    //            }// end of if cycle
    //
    //            mailService.send("Ciclo " + tag, testo);
    //        }// end of if cycle
    //    } // fine del metodo


    public void sendMail(LocalDateTime start, String tag) {
        long inizio = Timestamp.valueOf(start).getTime();
        LocalDateTime end;
        String testo = "";
        testo += "Eseguito in automatico ";
        testo += EASchedule.biMensile.getNota();
        testo += A_CAPO;
        testo += A_CAPO;
        testo += "Cancella tutte le pagine biografiche e ricarica completamente il db";
        testo += A_CAPO;
        testo += "Ciclo del " + date.get();
        testo += A_CAPO;
        testo += "Iniziato alle " + date.getOrario(start);
        testo += A_CAPO;

        if (pref.isBool(SEND_MAIL_CICLO)) {
            end = LocalDateTime.now();
            testo += "Terminato alle " + date.getOrario(end);
            testo += A_CAPO;
            testo += "Durata totale: " + date.deltaText(inizio);
            testo += A_CAPO;
            testo += "Nel db ci sono " + text.format(bio.count()) + " pagine biografiche";
            mailService.send("Ciclo " + tag, testo);
        }// end of if cycle
    } // fine del metodo


    /**
     * Recupera una mappa completa (ordinata) dei nomi/cognomi e della loro frequenza
     *
     * @return mappa sigla (nomi/cognomi), numero di voci
     */
    public LinkedHashMap<String, Integer> findMappa(Vector vettoreAll, int taglio) {
        LinkedHashMap<String, Integer> mappa = new LinkedHashMap<>();
        LinkedHashMap<String, Object> mappaTmp = null;
        Object[] obj;
        String nomeText = "";
        long numVociBio = 0;
        String chiave;
        int valore;

        if (vettoreAll != null) {
            mappaTmp = new LinkedHashMap<>();
            for (Object vect : vettoreAll) {
                if (vect instanceof Object[]) {
                    obj = (Object[]) vect;
                    nomeText = (String) obj[0];
                    numVociBio = (long) obj[1];
                    if (numVociBio >= taglio) {
                        if (!nomeText.equals("")) {
                            mappaTmp.put(nomeText, (int) numVociBio);
                        }// end of if cycle
                    }// end of if cycle
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        //        if (mappaTmp != null) {
        //            mappaTmp = LibArray.ordinaMappaAccentiSensibile(mappaTmp);
        //            for (Map.Entry<String, Object> elementoDellaMappa : mappaTmp.entrySet()) {
        //                chiave = elementoDellaMappa.getKey();
        //                valore = (int) elementoDellaMappa.getValue();
        //                mappa.put(chiave, valore);
        //            }// end of for cycle
        //        }// end of if cycle

        return mappa;
    }// end of method


    public void showWikiPage(String wikiTitle) {
        String link = "\"" + PATH_WIKI + wikiTitle + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    public void editWikiPage(String wikiTitle) {
        String link = "\"" + PATH_WIKI_EDIT_ANTE + wikiTitle + PATH_WIKI_EDIT_POST + "&section=0\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    public String setCassetto(String titolo, String testoOriginario) {
        String testo = VUOTA;

        if (text.isValid(titolo) && text.isValid(testoOriginario)) {
            testo += "{{cassetto";
            testo += A_CAPO;
            testo += "|titolo = ";
            testo += titolo;
            testo += A_CAPO;
            testo += "|testo = ";
            testo += testoOriginario;
            testo += "}}";
            testo += A_CAPO;
        }// end of if cycle

        return testo;
    }// end of method

}// end of class
