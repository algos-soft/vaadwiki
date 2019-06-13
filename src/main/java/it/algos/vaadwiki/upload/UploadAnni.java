package it.algos.vaadwiki.upload;

import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.Giorno;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 17-gen-2019
 * Time: 19:05
 * <p>
 * Classe specializzata per caricare (upload) le liste sul server wiki. <br>
 * <p>
 * Viene chiamato da Scheduler (con frequenza giornaliera ?) <br>
 * Può essere invocato dal bottone 'Upload all' della classe WikiAnnoViewList <br>
 * <p>
 * Necessita del login come bot <br>
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 */
public abstract class UploadAnni extends Upload {

    protected String titoloAnno;

    @Autowired
    protected AnnoService annoService;

    protected Anno anno;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public UploadAnni() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(UploadAnnoNato.class, giorno) <br>
     * Usa: appContext.getBean(UploadAnnoMorto.class, giorno) <br>
     *
     * @param anno di cui costruire la pagina sul server wiki
     */
    public UploadAnni(Anno anno) {
        this.anno = anno;
    }// end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se hanno la stessa firma, chiama prima @PostConstruct della sottoclasse <br>
     * Se hanno firme diverse, chiama prima @PostConstruct della superclasse <br>
     */
    @PostConstruct
    protected void inizia() {
        esegue(anno);
    }// fine del metodo


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste di nati e morti per ogni giorno dell'anno
     */
    public void esegueAll() {
        ArrayList<Anno> listaAnni = annoService.findAll();
        long inizio = System.currentTimeMillis();
        int modNati = 0;
        int modMorti = 0;
        String modTxt;

        for (Anno anno : listaAnni) {
//            uploadAnnoNato.esegue(anno);
            modNati++;

//            uploadAnnoMorto.esegue(anno);
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
    public void esegue(Anno anno) {
        this.anno = anno;
        esegue();
    }// fine del metodo


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste di nati e morti per ogni giorno dell'anno
     */
    public void esegueTest(Anno anno) {
        this.anno = anno;
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

        if (anno != null) {
            titolo += anno.getTitolo();
        }// fine del blocco if

        return titolo;
    }// fine del metodo


    /**
     * Titolo della pagina Nati/Morti da creare/caricare su wikipedia
     * Sovrascritto
     */
    public String getTitoloPagina(Anno anno) {
        return "";
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
        String titoloTemplate = "Lista persone per anno";
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
        return "Anni";
    }// end of method

}// end of class
