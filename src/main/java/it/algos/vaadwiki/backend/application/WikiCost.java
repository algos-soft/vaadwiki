package it.algos.vaadwiki.backend.application;

import it.algos.vaadflow14.backend.annotation.AIScript;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 14-apr-2021
 * Time: 8:22
 * <p>
 * Classe astratta di costanti statiche <br>
 * Completa la classe FlowCost con le costanti statiche specifiche di questa applicazione <br>
 * Le costanti statiche sono sempre 'static final' (nell'ordine) <br>
 * <p>
 * Not annotated with @SpringComponent (astratta, inutile) <br>
 * Not annotated with @Scope (astratta, inutile) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file da wizard/scripts di algos <br>
 */
@AIScript(sovraScrivibile = false)
public abstract class WikiCost {

    public static final String TAG_WIKI_DATA = "wikiData";

}// end of static class