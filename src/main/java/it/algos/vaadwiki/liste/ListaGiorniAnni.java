package it.algos.vaadwiki.liste;

import it.algos.vaadwiki.didascalia.WrapDidascalia;

import java.util.ArrayList;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 16-set-2019
 * Time: 08:06
 */
public abstract class ListaGiorniAnni extends Lista {

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
            mappaComplessa = listaService.creaMappaChiaveUno(listaDidascalie, titoloParagrafoVuoto, paragrafoVuotoInCoda);
        } else {
//            mappaSemplice = listaService.creaMappaQuadre(listaDidascalie);
        }// end of if/else cycle
    }// fine del metodo

}// end of class
