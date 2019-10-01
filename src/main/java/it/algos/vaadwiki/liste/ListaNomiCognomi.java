package it.algos.vaadwiki.liste;

import it.algos.vaadwiki.didascalia.WrapDidascalia;

import java.util.ArrayList;

import static it.algos.vaadwiki.application.WikiCost.*;

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
        super.titoloParagrafoVuoto = pref.getStr(TAG_PARAGRAFO_VUOTO_NOMI_COGNOMI);
        super.titoloSottoPaginaVuota = pref.getStr(TAG_SOTTOPAGINA_VUOTA_NOMI_COGNOMI);
        super.usaRigheRaggruppate = false;
        super.usaLinkAttivita = true;
        super.usaBodySottopagine = pref.isBool(USA_SOTTOPAGINE_NOMI_COGNOMI);
    }// end of method

}// end of class
