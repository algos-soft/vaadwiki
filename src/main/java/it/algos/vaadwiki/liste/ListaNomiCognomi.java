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
     * Ordina la lista di didascalie specifiche <br>
     */
    @SuppressWarnings("all")
    public ArrayList<WrapDidascalia> ordinaListaDidascalie(ArrayList<WrapDidascalia> listaDisordinata) {
        return listaService.ordinaListaDidascalieNomiCognomi(listaDisordinata);
    }// fine del metodo

}// end of class
