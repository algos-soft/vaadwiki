package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.didascalia.DidascaliaGiornoNato;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 18-gen-2019
 * Time: 07:56
 * <p>
 * Crea la lista dei nati nel giorno e la carica sul server wiki
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ListaGiornoNato {

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

    @Autowired
    protected DidascaliaGiornoNato didascaliaGiornoNato;

    protected ArrayList<Bio> listaGrezzaBio;

    protected TreeMap<Integer, Map> mappaOrdinataBio;

    protected LinkedHashMap<Integer, ArrayList<String>> mappaListaDidascalie;

    @Autowired
    private BioService bioService;

    private Giorno giorno;


    /**
     * Costruttore
     */
    public ListaGiornoNato() {
    }// fine del costruttore


    /**
     * Esegue
     *
     * @param giorno di cui creare la lista
     */
    public LinkedHashMap<Integer, ArrayList<String>> esegue(Giorno giorno) {
        this.giorno = giorno;
        elaboraListaBiografie();
        return mappaListaDidascalie;
    }// fine del metodo


    /**
     * Costruisce una lista di biografie che hanno una valore valido per la pagina specifica
     * Esegue una query
     * Sovrascritto
     */
    protected void elaboraListaBiografie() {

        if (giorno != null) {
            listaGrezzaBio = listaBioNati();
            mappaOrdinataBio = ordina(listaGrezzaBio);
            mappaListaDidascalie = elaboraDidascalie(mappaOrdinataBio);
        }// end of if cycle
    }// fine del metodo


    /**
     * Riordina la lista per anno di nascita
     *
     * @param listaDisordinata
     *
     * @return lista ordinata
     */
    @SuppressWarnings("all")
    public TreeMap<Integer, Map> ordina(ArrayList<Bio> listaDisordinata) {
        TreeMap<Integer, Map> mappa = new TreeMap<Integer, Map>();
        ArrayList<Bio> listaOrdinata = null;
        Map<String, Bio> mappa2;
        Integer key;
        String annoText = "";
        String cognome = "";
        String wikiTitle = "";
        String keyCognomeTitle = "";

        for (Bio bio : listaDisordinata) {
            annoText = bio.getAnnoNato();
            wikiTitle = bio.getWikiTitle();
            cognome = text.isValid(bio.getCognome()) ? bio.getCognome() : wikiTitle;
            keyCognomeTitle = cognome + wikiTitle;

            if (text.isValid(annoText)) {
                try { // prova ad eseguire il codice
                    key = Integer.decode(annoText);
                } catch (Exception unErrore) { // intercetta l'errore
                    key = 0;
                }// fine del blocco try-catch
            } else {
                key = 0;
            }// end of if/else cycle

            if (mappa.get(key) == null) {
                mappa2 = new TreeMap<String, Bio>();
                mappa.put(key, mappa2);
            } else {
                mappa2 = (Map<String, Bio>) mappa.get(key);
            }// end of if/else cycle
            if (mappa2.containsKey(keyCognomeTitle)) {
                log.info("Algos - ListaGiornoNato.ordina() - qualcosa non ha funzionato nel giorno " + key + " per la bio " + keyCognomeTitle);
            } else {
                mappa2.put(keyCognomeTitle, bio);
            }// end of if/else cycle
        }// end of for cycle

        if (pref.isBool(FlowCost.USA_DEBUG)) {
//            testBio(mappa);
        }// end of if cycle

        return mappa;
    }// fine del metodo


    public void testBio(Map<Integer, Map> mappa) {
        Map<String, Bio> mappa2;
        Bio bioPrint;
        int k = 0;

        System.out.println("");
        System.out.println("Giorno: " + giorno.titolo);
        System.out.println("");
        for (Map.Entry<Integer, Map> entry : mappa.entrySet()) {
            mappa2 = (Map) entry.getValue();
            for (Map.Entry<String, Bio> entry2 : mappa2.entrySet()) {
                entry2.getKey();
                bioPrint = entry2.getValue();
                k++;
                if (text.isValid(bioPrint.getAnnoNato()) && text.isValid(bioPrint.getCognome())) {
                    System.out.println(k + ": " + bioPrint.getAnnoNato() + " - " + bioPrint.getCognome());
                } else {
                    if (text.isEmpty(bioPrint.getAnnoNato()) && text.isEmpty(bioPrint.getCognome())) {
                        System.out.println(k + ": ? - " + bioPrint.getWikiTitle());
                    } else {
                        if (text.isValid(bioPrint.getAnnoNato())) {
                            System.out.println(k + ": " + bioPrint.getAnnoNato() + " - " + bioPrint.getWikiTitle());
                        }// end of if cycle
                        if (text.isValid(bioPrint.getCognome())) {
                            System.out.println(k + ": ? - " + bioPrint.getCognome());
                        }// end of if cycle
                    }// end of if/else cycle
                }// end of if/else cycle
            }// end of for cycle
        }// end of for cycle
    }// fine del metodo


    /**
     * Elabora le didascalie
     *
     * @param mappaOrdinataBio
     *
     * @return mappaListaDidascalie
     */
    @SuppressWarnings("all")
    public LinkedHashMap<Integer, ArrayList<String>> elaboraDidascalie(TreeMap<Integer, Map> mappaBioOrdinata) {
        LinkedHashMap<Integer, ArrayList<String>> mappaListaDidascalie = new LinkedHashMap<>();
        Map<String, Bio> mappa;
        ArrayList<String> listaDida;
        Integer anno;
        String didascalia;
        Bio bio;

        for (Map.Entry<Integer, Map> entry : mappaBioOrdinata.entrySet()) {
            listaDida = new ArrayList<>();
            anno = entry.getKey();
            mappa = entry.getValue();
            for (Map.Entry<String, Bio> entry2 : mappa.entrySet()) {
                bio = entry2.getValue();
                didascalia = didascaliaGiornoNato.esegue(bio);
                listaDida.add(didascalia);
            }// end of for cycle
            mappaListaDidascalie.put(anno, listaDida);
        }// end of for cycle

        if (pref.isBool(FlowCost.USA_DEBUG)) {
//            testDidascalie(mappaListaDidascalie);
        }// end of if cycle

        return mappaListaDidascalie;
    }// fine del metodo


    public void testDidascalie(Map<Integer, ArrayList<String>> mappaLista) {
        System.out.println("");
        System.out.println("Giorno: " + giorno.titolo);
        System.out.println("");

        if (mappaLista != null) {
            for (Map.Entry<Integer, ArrayList<String>> entry : mappaLista.entrySet()) {
                for (String dida : entry.getValue()) {
                    System.out.println(entry.getKey() + " - " + dida);
                }// end of for cycle
            }// end of for cycle
        }// end of if cycle

    }// fine del metodo


    /**
     * Recupera una lista (array) di records Bio che usano questa istanza di Giorno nella property giornoNato
     *
     * @return lista delle istanze di Bio che usano questo istanza
     */
    @SuppressWarnings("all")
    public ArrayList<Bio> listaBioNati() {
        return bioService.findAllByGiornoNato(giorno.titolo);
    }// fine del metodo

}// end of class
