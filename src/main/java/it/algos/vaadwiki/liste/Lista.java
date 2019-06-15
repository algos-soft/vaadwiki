package it.algos.vaadwiki.liste;

import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;

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
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public ATextService text;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public PreferenzaService pref;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public BioService bioService;

    public LinkedHashMap<String, ArrayList<String>> mappa = null;

    protected EADidascalia typeDidascalia;

    protected ArrayList<Bio> listaGrezzaBio;


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
        this.creaMappaIniziale();
    }// end of method


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public void creaMappaIniziale() {
        ArrayList<WrapDidascalia> lista = null;

        listaGrezzaBio = listaBio();
        lista = creaListaDidascalie(listaGrezzaBio);
        ordinaListaDidascalie(lista);
        mappa = creaMappa(lista);
    }// fine del metodo


    /**
     * Recupera una lista (array) di records Bio che usano un'istanza della property appropriata <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @return lista delle istanze di Bio che usano questa istanza della property appropriata
     */
    @SuppressWarnings("all")
    public ArrayList<Bio> listaBio() {
        return null;
    }// fine del metodo


    /**
     * Costruisce una lista di didascalie (Wrap) che hanno una valore valido per la pagina specifica <br>
     * La lista NON è ordinata <br>
     *
     * @param listaGrezzaBio di persone che hanno una valore valido per la pagina specifica
     *
     * @return lista NON ORDINATA di didascalie (Wrap)
     */
    public ArrayList<WrapDidascalia> creaListaDidascalie(ArrayList<Bio> listaGrezzaBio) {
        ArrayList<WrapDidascalia> lista = new ArrayList<WrapDidascalia>();
        WrapDidascalia wrap;

        for (Bio bio : listaGrezzaBio) {
            wrap = appContext.getBean(WrapDidascalia.class, bio, typeDidascalia);
            lista.add(wrap);
        }// end of for cycle

        return lista;
    }// fine del metodo


    /**
     * Ordina la lista di didascalie (Wrap) che hanno una valore valido per la pagina specifica <br>
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


}// end of class
