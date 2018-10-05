package it.algos.wiki.request;


import it.algos.wiki.LibWiki;
import it.algos.wiki.TipoRisultato;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by gac on 06 dic 2015.
 * <p>
 * Token
 * <p>
 * To edit a page, an edit token is required.
 * This token is the same for all pages, but changes at every login.
 * The preferred method to obtain an edit token depends on the MediaWiki version:
 * Versions 1.24 and later: action=query&meta=tokens
 * Versions 1.20-1.23: action=tokens
 * Versions 1.19 and earlier: action=query&prop=info
 * <p>
 * Currently, all older methods continue to work, but are deprecated.
 * <p>
 * Obtaining an edit token
 * When passing this to the Edit API, always pass the token parameter last (or at least after the text parameter).
 * That way, if the edit gets interrupted, the token won't be passed and the edit will fail.
 * This is done automatically by mw.Api.
 * <p>
 * Parameters
 * title: Title of the page you want to edit. Cannot be used together with pageid.
 * pageid: Page ID of the page you want to edit. Cannot be used together with title. 1.20+
 * section: Section number. 0 for the top section, 'new' for a new section. Omit to act on the entire page.
 * sectiontitle: Title to use if creating a new section. If not specified, summary will be used instead. 1.19+
 * text: New page (or section) content.
 * summary: Edit summary. Also section title when section=new and sectiontitle is not set.
 * minor: If set, mark the edit as minor.
 * notminor: If set, don't mark the edit as minor, even if you have the "Mark all my edits minor by default" preference enabled.
 * bot: If set, mark the edit as bot; even if you are using a bot account the edits will not be marked unless you set this flag.
 * basetimestamp: Timestamp of the base revision (obtained through prop=revisions&rvprop=timestamp). Used to detect edit conflicts; leave unset to ignore conflicts. Note: Edit conflicts will be ignored if you are conflicting with the current user.
 * starttimestamp: Timestamp when you started editing the page (e.g., when you fetched the current revision's text to begin editing it or checked the (non-)existence of the page). Used to detect if the page has been deleted since you started editing; leave unset to ignore conflicts. 1.14+
 * recreate: Override any errors about the article having been deleted in the meantime.
 * createonly: Don't edit the page if it exists already.
 * nocreate: Throw an error if the page doesn't exist.
 * watch: Add the page to your watchlist. Deprecated. Use the watchlist argument (deprecated in 1.16)
 * unwatch: Remove the page from your watchlist. Deprecated. Use the watchlist argument (deprecated in 1.16)
 * watchlist: Specify how the watchlist is affected by this edit, set to one of "watch", "unwatch", "preferences", "nochange": (Default: preferences) 1.16+
 * watch: add the page to the watchlist.
 * unwatch: remove the page from the watchlist.
 * preferences: use the preference settings.
 * nochange: don't change the watchlist.
 * md5: MD5 hash (hex) of the text parameter or the prependtext and appendtext parameters concatenated. If this parameter is set and the hashes don't match, the edit is rejected. This can be used to guard against data corruption.
 * prependtext: Add this text to the beginning of the page. Overrides text.
 * appendtext: Add this text to the end of the page. Overrides text. Use section=new to append a new section.
 * undo: Revision ID to undo. Overrides text, prependtext and appendtext. 1.15+
 * undoafter: Undo all revisions from undo up to but not including this one. If not set, just undo one revision. 1.15+
 * redirect: Automatically resolve redirects. 1.17+
 * contentformat: Content serialization format used for the input text. Possible values: text/x-wiki (wikitext), text/javascript (javascript), text/css (css), text/plain (plain text), application/json (json). 1.21+
 * contentmodel: Content model of the new content. Possible values: wikitext, javascript, css, text. This list may include additional values registered by extensions; on Wikimedia wikis, these include JsonZeroConfig, Scribunto, JsonSchema. 1.21+
 * token: Edit token. Especially if you are not using the md5 parameter, the token should be sent as the last parameter, or at least after the text parameter, to prevent a bad edit from getting committed if transmission of the body is interrupted for some reason.
 * captchaid: CAPTCHA ID from the previous request. Although removed from the edit API in 1.18, edit confirmation extensions, such as Extension:ConfirmEdit, can still add their own parameters to the edit API. On Wikimedia wikis, these continue to be captchaid and captchaword, however different extensions use different parameters. (removed in 1.18)
 * captchaword: Answer to the CAPTCHA. (removed in 1.18)
 * <p>
 * Link: https://www.mediawiki.org/wiki/API:Edit
 */
public class RequestWrite extends ARequest {

    private static final String SUMMARY = LibWiki.getSummary("fix vari");

    //--azione API specifica */
    protected static String API_EDIT = "edit";

    // contenuto della pagina in scrittura
    private String newText;

    /**
     * Costruttore
     * Rinvia al costruttore completo
     *
     * @param wikiTitle titolo della pagina wiki su cui scrivere
     * @param newText   da inserire
     */
    public RequestWrite(String wikiTitle, String newText) {
        this(wikiTitle, newText, SUMMARY);
    }// fine del metodo costruttore completo

    /**
     * Costruttore
     *
     * @param wikiTitle titolo della pagina wiki su cui scrivere
     * @param newText   da inserire
     * @param summary   oggetto dello spostamento
     */
    public RequestWrite(String wikiTitle, String newText, String summary) {
        super(wikiTitle, newText, summary);
    }// fine del metodo costruttore completo


    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    @Override
    protected void elaboraParametri() {
        super.elaboraParametri();
        this.newText = super.newTitleNewText;
        needPreliminary = true;
        needLogin = true;
        needBot = true;
        needPost = true;
    }// fine del metodo

    /**
     * Stringa del browser per la request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected String elaboraDomain() {
        String domainTmp = API_BASE + API_ACTION + API_EDIT;
        String titolo = "";

        try { // prova ad eseguire il codice
            titolo = URLEncoder.encode(wikiTitle, ENCODE);
            domainTmp += "&title=" + titolo;
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        if (needBot) {
            domainTmp += API_ASSERT;
        }// end of if cycle

        domain = domainTmp;
        return domainTmp;
    } // fine del metodo

    /**
     * Crea il testo del POST della request
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected String elaboraPost() {
        String testoPost = "";
        String testoVoce = "";
        String testoSummary = "";

        try { // prova ad eseguire il codice
            if (newText != null && !newText.equals("")) {
                testoVoce = URLEncoder.encode(newText, ENCODE);
            }// fine del blocco if
            if (summary != null && !summary.equals("")) {
                testoSummary = URLEncoder.encode(summary, ENCODE);
            }// fine del blocco if
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        testoPost += "text=" + testoVoce;
        testoPost += "&bot=true";
        testoPost += "&minor=true";
        if (!testoSummary.equals("")) {
            testoPost += "&summary=" + testoSummary;
        }// end of if cycle

        //--token per ultimo (obbligatoria per le API Mediawiki, la posizione in coda)
        testoPost += super.elaboraPost();

        return testoPost;
    } // fine del metodo

    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void elaboraRisposta(String rispostaRequest) {
        boolean noChange;
        HashMap<String, Object> mappaResponse = LibWiki.creaMappaEdit(rispostaRequest);

        if (mappaResponse != null) {
            if (mappaResponse.get(LibWiki.RESULT) != null && mappaResponse.get(LibWiki.RESULT).equals(LibWiki.SUCCESS)) {
                valida = true;
            }// end of if cycle

            if (mappaResponse.get(LibWiki.NOCHANGE) != null) {
                noChange = (Boolean) mappaResponse.get(LibWiki.NOCHANGE);
                if (noChange) {
                    risultato = TipoRisultato.nochange;
                } else {
                    risultato = TipoRisultato.modificaRegistrata;
                }// end of if/else cycle
            } else {
                risultato = TipoRisultato.modificaRegistrata;
            }// end of if/else cycle
        }// end of if cycle

    } // end of getter method

} // fine della classe
