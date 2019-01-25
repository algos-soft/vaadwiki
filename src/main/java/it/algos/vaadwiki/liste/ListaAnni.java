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

import static it.algos.vaadflow.application.FlowCost.USA_DEBUG;
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


//    /**
//     * Costruisce una lista di biografie che hanno una valore valido per questa pagina specifica (Giorno o Anno)
//     *
//     * @param anno di cui creare la lista
//     */
//    public LinkedHashMap<Integer, ArrayList<String>> esegue(Anno anno) {
//        this.anno = anno;
//        return super.esegue();
//    }// fine del metodo


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
//    @Override
//    @SuppressWarnings("all")
//    public TreeMap<Integer, Map> ordina(ArrayList<Bio> listaGrezzaBio) {
//        TreeMap<Integer, Map> mappa = new TreeMap<Integer, Map>();
//        ArrayList<Bio> listaOrdinata = null;
//        Map<String, Bio> mappa2;
//        Integer key;
//        String keyText = "";
//        String cognome = "";
//        String wikiTitle = "";
//        String keyCognomeTitle = "";
//
//        for (Bio bio : listaGrezzaBio) {
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
//                log.info("Algos - Lista.ordina() - qualcosa non ha funzionato nel giorno " + key + " per la bio " + keyCognomeTitle);
//            } else {
//                mappa2.put(keyCognomeTitle, bio);
//            }// end of if/else cycle
//        }// end of for cycle
//
//        if (pref.isBool(USA_DEBUG)) {
////            testBio(mappa);
//        }// end of if cycle
//
//        return mappa;
//    }// fine del metodo

}// end of class
