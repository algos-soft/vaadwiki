package it.algos.vaadwiki.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.didascalia.*;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.wiki.Api;
import it.algos.wiki.web.AQueryWrite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 06-feb-2019
 * Time: 17:43
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class DidascaliaService extends ABioService {


    public static final String wikiPagineDidascalie = "Progetto:Biografie/Didascalie";

    public static final String wikiTitleDebug = "Utente:Biobot/2";

    public String wikiTitle = "Ron Clarke";

    //    @Autowired
    private Didascalia didascalia;


    public void esegue() {
        String testo = VUOTA;

        testo += topLayout();
        testo += bodyLayout(wikiTitle);
        testo += bottomLayout();

        if (pref.isBool(USA_DEBUG)) {
            Api.scriveVoce(wikiTitleDebug, testo);
        } else {
            Api.scriveVoce(wikiPagineDidascalie, testo);
        }// end of if/else cycle
    }// end of method


    public void esegueTest() {
        String testo = VUOTA;

        testo += topLayout();
        testo += bodyLayout(wikiTitle);
        testo += bottomLayout();

        appContext.getBean(AQueryWrite.class, wikiTitleDebug, testo).urlRequest();
//        Api.scriveVoce(WIKI_TITLE_DEBUG, testo);
    }// end of method


    public String topLayout() {
        String testo = VUOTA;
        String oggi = date.get();

        testo += "<noinclude>__NOTOC__{{StatBio|data=" + oggi + "}}</noinclude>";
        testo += A_CAPO;

        return testo;
    }// end of method


    public String bodyLayout(String wikiTitle) {
        String testo = VUOTA;
        Bio bio = api.leggeBio(wikiTitle);
        String paragrafo = "Didascalie";
        String pagina = "Nella pagina con la lista dei ";

        testo += LibBio.setParagrafo(paragrafo);

        testo += "Pagina di servizio per il '''controllo''' delle didascalie utilizzate nelle pagine delle liste di giorni ed anni. Le didascalie sono di diversi tipi:";
        testo += A_CAPO;

        if (text.isValid(bio.getGiornoNato())) {
            testo += ASTERISCO;
            testo += pagina;
//            testo += "[[Nati il " + bio.getGiornoNato() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.giornoNato) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoNato())) {
            testo += ASTERISCO;
            testo += pagina;
//            testo += "[[Nati nel " + bio.getAnnoNato() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.annoNato) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getGiornoMorto())) {
            testo += ASTERISCO;
            testo += pagina;
//            testo += "[[Morti il " + bio.getGiornoMorto() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.giornoMorto) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoMorto())) {
            testo += ASTERISCO;
            testo += pagina;
//            testo += "[[Morti nel " + bio.getAnnoMorto() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.annoMorto) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoMorto())) {
            testo += ASTERISCO;
            testo += "Nelle liste di attività, nazionalità, nome e cognomi";
//            testo += " -> " + "'''" + didascalia.esegue(bio, EADidascalia.standard) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoMorto())) {
            testo += ASTERISCO;
            testo += "Completa (nella biografia)";
//            testo += " -> " + "'''" + didascalia.esegue(bio, EADidascalia.completa) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        return testo;
    }// end of method


    public String bottomLayout() {
        String testo = VUOTA;
        String catText;

        testo += A_CAPO;
        testo += "{{BioCorrelate}}";
        testo += A_CAPO;
        testo += A_CAPO;

        catText = "[[Categoria:Progetto Biografie|Didascalie]]";
        catText = LibBio.setNoIncludeMultiRiga(catText);
        testo += catText;

        return testo;
    }// end of method


    public DidascaliaGiornoNato getDidascaliaGiornoNato(Bio bio) {
        return appContext.getBean(DidascaliaGiornoNato.class, bio);
    }// end of method


    public DidascaliaGiornoMorto getDidascaliaGiornoMorto(Bio bio) {
        return appContext.getBean(DidascaliaGiornoMorto.class, bio);
    }// end of method


    public DidascaliaAnnoNato getDidascaliaAnnoNato(Bio bio) {
        return appContext.getBean(DidascaliaAnnoNato.class, bio);
    }// end of method


    public DidascaliaAnnoMorto getDidascaliaAnnoMorto(Bio bio) {
        return appContext.getBean(DidascaliaAnnoMorto.class, bio);
    }// end of method


    public DidascaliaListe getDidascaliaListe(Bio bio) {
        return appContext.getBean(DidascaliaListe.class, bio);
    }// end of method


    public DidascaliaBiografie getDidascaliaBiografie(Bio bio) {
        return appContext.getBean(DidascaliaBiografie.class, bio);
    }// end of method


    public String getBase(Bio bio, EADidascalia type) {
        Didascalia didascalia = null;

        if (bio != null && type != null) {
            switch (type) {
                case giornoNato:
                    didascalia = getDidascaliaGiornoNato(bio);
                    break;
                case giornoMorto:
                    didascalia = getDidascaliaGiornoMorto(bio);
                    break;
                case annoNato:
                    didascalia = getDidascaliaAnnoNato(bio);
                    break;
                case annoMorto:
                    didascalia = getDidascaliaAnnoMorto(bio);
                    break;
                case liste:
                    didascalia = getDidascaliaListe(bio);
                    break;
                case biografie:
                    didascalia = getDidascaliaBiografie(bio);
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

        return didascalia != null ? didascalia.testo : VUOTA;
    }// end of method


    public String getGiornoNato(Bio bio) {
        return getBase(bio, EADidascalia.giornoNato);
    }// end of method


    public String getGiornoMorto(Bio bio) {
        return getBase(bio, EADidascalia.giornoMorto);
    }// end of method


    public String getAnnoNato(Bio bio) {
        return getBase(bio, EADidascalia.annoNato);
    }// end of method


    public String getAnnoMorto(Bio bio) {
        return getBase(bio, EADidascalia.annoMorto);
    }// end of method


    public String getListe(Bio bio) {
        return getBase(bio, EADidascalia.liste);
    }// end of method


    public String getBiografie(Bio bio) {
        return getBase(bio, EADidascalia.biografie);
    }// end of method


}// end of class
