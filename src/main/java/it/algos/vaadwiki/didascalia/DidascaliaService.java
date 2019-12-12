package it.algos.vaadwiki.didascalia;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.service.ABioService;
import it.algos.vaadwiki.service.LibBio;
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

    //--titolo della pagina di servizio sul server wiki
    public static final String TITOLO_PAGINA_WIKI = "Progetto:Biografie/Didascalie";


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
            Api.scriveVoce(TITOLO_PAGINA_WIKI, testo);
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

        if (text.isValid(bio.getGiornoNascita())) {
            testo += ASTERISCO;
            testo += pagina;
//            testo += "[[Nati il " + bio.getGiornoNato() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.giornoNato) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoNascita())) {
            testo += ASTERISCO;
            testo += pagina;
//            testo += "[[Nati nel " + bio.getAnnoNato() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.annoNato) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getGiornoMorte())) {
            testo += ASTERISCO;
            testo += pagina;
//            testo += "[[Morti il " + bio.getGiornoMorto() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.giornoMorto) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoMorte())) {
            testo += ASTERISCO;
            testo += pagina;
//            testo += "[[Morti nel " + bio.getAnnoMorto() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.annoMorto) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoMorte())) {
            testo += ASTERISCO;
            testo += "Nelle liste di attività, nazionalità, nome e cognomi";
//            testo += " -> " + "'''" + didascalia.esegue(bio, EADidascalia.standard) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoMorte())) {
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


    public String getBase(Bio bio, EADidascalia type, boolean conChiave) {
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
                case listaNomi:
                case listaCognomi:
                    didascalia = getDidascaliaListe(bio);
                    break;
                case listaAttivita:
                case listaNazionalita:
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

        if (conChiave) {
            return didascalia != null ? didascalia.testoCon : VUOTA;
        } else {
            return didascalia != null ? didascalia.testoSenza : VUOTA;
        }// end of if/else cycle
    }// end of method


    public String getBaseCon(Bio bio, EADidascalia type) {
        return getBase(bio, type, true);
    }// end of method


    public String getBaseSenza(Bio bio, EADidascalia type) {
        return getBase(bio, type, false);
    }// end of method


    public String getGiornoNatoCon(Bio bio) {
        return getBase(bio, EADidascalia.giornoNato, true);
    }// end of method


    public String getGiornoMortoCon(Bio bio) {
        return getBase(bio, EADidascalia.giornoMorto, true);
    }// end of method


    public String getAnnoNatoCon(Bio bio) {
        return getBase(bio, EADidascalia.annoNato, true);
    }// end of method


    public String getAnnoMortoCon(Bio bio) {
        return getBase(bio, EADidascalia.annoMorto, true);
    }// end of method


    public String getListeCon(Bio bio) {
        return getBase(bio, EADidascalia.listaNomi, true);
    }// end of method


    public String getGiornoNatoSenza(Bio bio) {
        return getBase(bio, EADidascalia.giornoNato, false);
    }// end of method


    public String getGiornoMortoSenza(Bio bio) {
        return getBase(bio, EADidascalia.giornoMorto, false);
    }// end of method


    public String getAnnoNatoSenza(Bio bio) {
        return getBase(bio, EADidascalia.annoNato, false);
    }// end of method


    public String getAnnoMortoSenza(Bio bio) {
        return getBase(bio, EADidascalia.annoMorto, false);
    }// end of method


    public String getListeSenza(Bio bio) {
        return getBase(bio, EADidascalia.listaNomi, false);
    }// end of method


    public String getBiografie(Bio bio) {
        return getBase(bio, EADidascalia.biografie, false);
    }// end of method


}// end of class
