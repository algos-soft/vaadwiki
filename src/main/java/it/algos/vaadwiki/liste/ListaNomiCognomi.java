package it.algos.vaadwiki.liste;

import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.genere.GenereService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Fri, 14-Jun-2019
 * Time: 18:38
 * <p>
 * Crea la lista delle persone col nome o col cognome indicati <br>
 */
public abstract class ListaNomiCognomi extends Lista {

    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è uno dei giorni/anni/nomi/cognomi in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     *
     * @param listaDidascalie
     */
    protected void creaMappa(ArrayList<WrapDidascalia> listaDidascalie) {
        if (usaSuddivisioneParagrafi) {
            usaParagrafoSize=false;
            mappaComplessa = listaService.creaMappaChiaveUno(listaDidascalie, titoloParagrafoVuoto, usaParagrafoSize, paragrafoVuotoInCoda);
        } else {
            mappaSemplice = listaService.creaMappa(listaDidascalie);
        }// end of if/else cycle
    }// fine del metodo

}// end of class
