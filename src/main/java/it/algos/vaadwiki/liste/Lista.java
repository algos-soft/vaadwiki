package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 10:34
 * <p>
 * Classe specializzata la creazione di liste. <br>
 * <p>
 * Liste cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno <br>
 * Liste di nomi e cognomi (in namespace principale). <br>
 * Liste di attività e nazionalità (in Progetto:Biografie). <br>
 * <p>
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 */
@Slf4j
public abstract class Lista {

    @Autowired
    public GiornoService giornoService;

    @Autowired
    public AnnoService annoService;

    @Autowired
    protected ApplicationContext appContext;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected ATextService text;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected PreferenzaService pref;

    protected ArrayList<Bio> listaGrezzaBio;

    public LinkedHashMap<String, ArrayList<String>> mappa = null;

    protected TreeMap<Integer, Map> mappaOrdinataBio;

    protected LinkedHashMap<Integer, ArrayList<String>> mappaListaDidascalie;

    @Autowired
    protected BioService bioService;


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
        this.esegue();
    }// end of method


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public LinkedHashMap<String, ArrayList<String>> esegue() {
        LinkedHashMap<String, ArrayList<String>> mappa = null;
        ArrayList<WrapDidascalia> lista = null;

        listaGrezzaBio = listaBio();
        lista = creaListaDidascalie(listaGrezzaBio);
        ordinaListaDidascalie(lista);
        mappa = creaMappa(lista);

        this.mappa = mappa;
        return mappa;
    }// fine del metodo


    /**
     * Recupera una lista (array) di records Bio che usano questa istanza di Giorno nella property giornoNato
     * oppure
     * Recupera una lista (array) di records Bio che usano questa istanza di Giorno nella property giornoMorto
     * oppure
     * Recupera una lista (array) di records Bio che usano questa istanza di Anno nella property annoNato
     * oppure
     * Recupera una lista (array) di records Bio che usano questa istanza di Anno nella property annoMorto
     * <p>
     * Sovrascritto nella sottoclasse concreta
     *
     * @return lista delle istanze di Bio che usano questo istanza nella property appropriata
     */
    @SuppressWarnings("all")
    public ArrayList<Bio> listaBio() {
        return null;
    }// fine del metodo


    /**
     * Costruisce una lista di didascalie (Wrap) che hanno una valore valido per la pagina specifica <br>
     * La lista NON è ordinata <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @param listaGrezzaBio di persone che hanno una valore valido per la pagina specifica
     *
     * @return lista NON ORDINATA di didascalie (Wrap)
     */
    public ArrayList<WrapDidascalia> creaListaDidascalie(ArrayList<Bio> listaGrezzaBio) {
        return null;
    }// fine del metodo


    /**
     * Ordina la lista di didascalie (Wrap) che hanno una valore valido per la pagina specifica <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @param listaDisordinata di didascalie
     *
     * @return lista di didascalie (Wrap) ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public void ordinaListaDidascalie(ArrayList<WrapDidascalia> listaDisordinata) {
        if (listaDisordinata != null) {

            listaDisordinata.sort(new Comparator<WrapDidascalia>() {

                int w1Ord;

                int w2Ord;


                @Override
                public int compare(WrapDidascalia dida1, WrapDidascalia dida2) {
                    w1Ord = dida1.getOrdine();
                    w2Ord = dida2.getOrdine();

                    return text.compareInt(w1Ord, w2Ord);
                }// end of method
            });//end of lambda expressions and anonymous inner class


            listaDisordinata.sort(new Comparator<WrapDidascalia>() {

                int w1Ord;

                int w2Ord;

                String w1Cog;

                String w2Cog;

                int resultOrdine;

                int resultCognomi;


                @Override
                public int compare(WrapDidascalia dida1, WrapDidascalia dida2) {
                    w1Ord = dida1.getOrdine();
                    w2Ord = dida2.getOrdine();
                    w1Cog = dida1.getSottoChiave();
                    w2Cog = dida2.getSottoChiave();

                    resultOrdine = text.compareInt(w1Ord, w2Ord);

                    if (resultOrdine == 0) {
                        return text.compareStr(w1Cog, w2Cog);
                    } else {
                        return resultOrdine;
                    }// end of if/else cycle

                }// end of method
            });//end of lambda expressions and anonymous inner class
        }// end of if cycle
    }// fine del metodo


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
     *
     * @listaOrdinata di didascalie (Wrap) ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public LinkedHashMap<String, ArrayList<String>> creaMappa(ArrayList<WrapDidascalia> listaDisordinata) {
        LinkedHashMap<String, ArrayList<String>> mappa = new LinkedHashMap<>();
        ArrayList<String> lista = null;
        String chiave;

        for (WrapDidascalia wrap : listaDisordinata) {
            chiave = wrap.getChiave();
            chiave = text.isValid(chiave) ? LibWiki.setQuadre(chiave) : "";

            if (mappa.get(chiave) == null) {
                lista = new ArrayList<String>();
                mappa.put(chiave, lista);
            } else {
                lista = (ArrayList<String>) mappa.get(chiave);
            }// end of if/else cycle
            lista.add(wrap.getTestoSenza()); //@todo rimettere

        }// end of for cycle

        return mappa;
    }// fine del metodo


//    /**
//     * Riordina la lista delle istanze di Bio per anno di nascita
//     * oppure
//     * Riordina la lista delle istanze di Bio per giorno di nascita
//     * Sovrascritto nelle sottoclassi
//     *
//     * @param listaGrezzaBio delle biografie che hanno una valore valido per questa pagina specifica (Giorno o Anno)
//     *
//     * @return mappa ordinata delle istanze di Bio
//     */
//    @SuppressWarnings("all")
//    public TreeMap<Integer, Map> ordina(ArrayList<Bio> listaGrezzaBio) {
//        return null;
//    }// fine del metodo


//    /**
//     * Recupera dalla entity Bio, la property annoNato (per le liste di GiornoNato)
//     * oppure
//     * Recupera dalla entity Bio, la property annoMorto (per le liste di GiornoMorto)
//     * oppure
//     * Recupera dalla entity Bio, la property giornoNato (per le liste di AnnoNato)
//     * oppure
//     * Recupera dalla entity Bio, la property giornoMorto (per le liste di AnnoMorto)
//     *
//     * @param bio entity
//     *
//     * @return property Bio richiesta
//     */
//    public String getKeyText(Bio bio) {
//        return VUOTA;
//    }// fine del metodo


//    /**
//     * Costruisce una mappa ordinata che ha come chiave il giorno o l'anno e come valore la didascalia
//     *
//     * @param mappaBioOrdinata mappa ordinata delle istanze di Bio
//     *
//     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
//     */
//    @SuppressWarnings("all")
//    public LinkedHashMap<Integer, ArrayList<String>> elaboraDidascalie(TreeMap<Integer, Map> mappaBioOrdinata) {
//        LinkedHashMap<Integer, ArrayList<String>> mappaListaDidascalie = new LinkedHashMap<>();
//        Map<String, Bio> mappa;
//        ArrayList<String> listaDida;
//        Integer anno;
//        String didascalia;
//        Bio bio;
//
//        for (Map.Entry<Integer, Map> entry : mappaBioOrdinata.entrySet()) {
//            listaDida = new ArrayList<>();
//            anno = entry.getKey();
//            mappa = entry.getValue();
//            for (Map.Entry<String, Bio> entry2 : mappa.entrySet()) {
//                bio = entry2.getValue();
//                didascalia = getDidascalia(bio);
//                listaDida.add(didascalia);
//            }// end of for cycle
//            mappaListaDidascalie.put(anno, listaDida);
//        }// end of for cycle
//
//        if (pref.isBool(USA_DEBUG)) {
////            testDidascalie(mappaListaDidascalie);
//        }// end of if cycle
//
//        return mappaListaDidascalie;
//    }// fine del metodo


//    /**
//     * Recupera dalla entity Bio, la didascalia giornoNato
//     * oppure
//     * Recupera dalla entity Bio, la didascalia giornoMorto
//     * oppure
//     * Recupera dalla entity Bio, la didascalia annoNato
//     * oppure
//     * Recupera dalla entity Bio, la didascalia annoMorto
//     *
//     * @param bio entity
//     *
//     * @return didascalia Bio richiesta
//     */
//    public String getDidascalia(Bio bio) {
//        return VUOTA;
//    }// fine del metodo

}// end of class
