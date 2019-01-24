package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.modules.bio.Bio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 10:33
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ListaAnni extends Lista {


    protected Anno anno;


    /**
     * Esegue
     *
     * @param anno di cui creare la lista
     */
    public LinkedHashMap<Integer, ArrayList<String>> esegue(Anno anno) {
        this.anno = anno;
        elaboraListaBiografie();
        return mappaListaDidascalie;
    }// fine del metodo


    /**
     * Costruisce una lista di biografie che hanno una valore valido per la pagina specifica
     * Esegue una query
     * Sovrascritto
     */
    protected void elaboraListaBiografie() {

        if (anno != null) {
            listaGrezzaBio = listaBio();
            mappaOrdinataBio = ordina(listaGrezzaBio);
            mappaListaDidascalie = elaboraDidascalie(mappaOrdinataBio);
        }// end of if cycle
    }// fine del metodo


//    /**
//     * Recupera una lista (array) di records Bio che usano questa istanza di Giorno nella property giornoNato
//     * oppure
//     * Recupera una lista (array) di records Bio che usano questa istanza di Giorno nella property giornoMorto
//     *
//     * @return lista delle istanze di Bio che usano questo istanza
//     */
//    @SuppressWarnings("all")
//    public ArrayList<Bio> listaBio() {
//        return bioService.findAllByAnnoNato(anno.titolo);
//    }// fine del metodo


//    /**
//     * Riordina la lista delle istanze di Bio per anno di nascita
//     * oppure
//     * Riordina la lista delle istanze di Bio per giorno di nascita
//     * <p>
//     * Sovrascritto nella sottoclasse concreta
//     *
//     * @param listaGrezzaBio delle biografie che hanno una valore valido per questa pagina specifica (Giorno o Anno)
//     *
//     * @return lista ordinata delle istanze di Bio
//     */
//    @SuppressWarnings("all")
//    public TreeMap<Integer, Map> ordina(ArrayList<Bio> listaDisordinata) {
//        TreeMap<Integer, Map> mappa = new TreeMap<Integer, Map>();
//        ArrayList<Bio> listaOrdinata = null;
//        Map<String, Bio> mappa2;
//        Integer key;
//        String keyText = "";
//        String cognome = "";
//        String wikiTitle = "";
//        String keyCognomeTitle = "";
//
//        for (Bio bio : listaDisordinata) {
//            keyText = getKeyText(bio);
//            wikiTitle = bio.getWikiTitle();
//            cognome = text.isValid(bio.getCognome()) ? bio.getCognome() : wikiTitle;
//            keyCognomeTitle = cognome + wikiTitle;
//
//            if (text.isValid(keyText)) {
//                try { // prova ad eseguire il codice
//                    key = Integer.decode(keyText);
//                } catch (Exception unErrore) { // intercetta l'errore
//                    key = 0;
//                }// fine del blocco try-catch
//            } else {
//                key = 0;
//            }// end of if/else cycle
//
//            if (mappa.get(key) == null) {
//                mappa2 = new TreeMap<String, Bio>();
//                mappa.put(key, mappa2);
//            } else {
//                mappa2 = (Map<String, Bio>) mappa.get(key);
//            }// end of if/else cycle
//            if (mappa2.containsKey(keyCognomeTitle)) {
//                log.info("Algos - ListaGiornoMorto.ordina() - qualcosa non ha funzionato nel giorno " + key + " per la bio " + keyCognomeTitle);
//            } else {
//                mappa2.put(keyCognomeTitle, bio);
//            }// end of if/else cycle
//        }// end of for cycle
//
//        if (pref.isBool(FlowCost.USA_DEBUG)) {
////            testBio(mappa);
//        }// end of if cycle
//
//        return mappa;
//    }// fine del metodo



    /**
     * Recupera dalla entity Bio, la property giornoNato
     * oppure
     * Recupera dalla entity Bio, la property giornoMorto
     *
     * @param bio entity
     *
     * @return property Bio richiesta
     */
    public String getKeyText(Bio bio) {
        return VUOTA;
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
                didascalia = getDidascalia(bio);
                listaDida.add(didascalia);
            }// end of for cycle
            mappaListaDidascalie.put(anno, listaDida);
        }// end of for cycle

        if (pref.isBool(FlowCost.USA_DEBUG)) {
//            testDidascalie(mappaListaDidascalie);
        }// end of if cycle

        return mappaListaDidascalie;
    }// fine del metodo


    /**
     * Recupera dalla entity Bio, la didascalia giornoNato
     * oppure
     * Recupera dalla entity Bio, la didascalia giornoMorto
     *
     * @param bio entity
     *
     * @return didascalia Bio richiesta
     */
    public String getDidascalia(Bio bio) {
        return VUOTA;
    }// fine del metodo


//    public void testBio(Map<Integer, Map> mappa) {
//        Map<String, Bio> mappa2;
//        Bio bioPrint;
//        int k = 0;
//
//        System.out.println("");
//        System.out.println("Giorno: " + anno.titolo);
//        System.out.println("");
//        for (Map.Entry<Integer, Map> entry : mappa.entrySet()) {
//            mappa2 = (Map) entry.getValue();
//            for (Map.Entry<String, Bio> entry2 : mappa2.entrySet()) {
//                entry2.getKey();
//                bioPrint = entry2.getValue();
//                k++;
//                if (text.isValid(bioPrint.getAnnoNato()) && text.isValid(bioPrint.getCognome())) {
//                    System.out.println(k + ": " + bioPrint.getAnnoNato() + " - " + bioPrint.getCognome());
//                } else {
//                    if (text.isEmpty(bioPrint.getAnnoNato()) && text.isEmpty(bioPrint.getCognome())) {
//                        System.out.println(k + ": ? - " + bioPrint.getWikiTitle());
//                    } else {
//                        if (text.isValid(bioPrint.getAnnoNato())) {
//                            System.out.println(k + ": " + bioPrint.getAnnoNato() + " - " + bioPrint.getWikiTitle());
//                        }// end of if cycle
//                        if (text.isValid(bioPrint.getCognome())) {
//                            System.out.println(k + ": ? - " + bioPrint.getCognome());
//                        }// end of if cycle
//                    }// end of if/else cycle
//                }// end of if/else cycle
//            }// end of for cycle
//        }// end of for cycle
//    }// fine del metodo


//    public void testDidascalie(Map<Integer, ArrayList<String>> mappaLista) {
//        System.out.println("");
//        System.out.println("Giorno: " + giorno.titolo);
//        System.out.println("");
//
//        if (mappaLista != null) {
//            for (Map.Entry<Integer, ArrayList<String>> entry : mappaLista.entrySet()) {
//                for (String dida : entry.getValue()) {
//                    System.out.println(entry.getKey() + " - " + dida);
//                }// end of for cycle
//            }// end of for cycle
//        }// end of if cycle
//
//    }// fine del metodo

}// end of class
