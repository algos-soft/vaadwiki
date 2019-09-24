package it.algos.vaadwiki.liste;

import it.algos.vaadwiki.didascalia.WrapDidascalia;

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
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaSuddivisioneParagrafi = true;
        super.usaRigheRaggruppate = false;
        super.usaLinkAttivita = true;
    }// end of method

}// end of class
