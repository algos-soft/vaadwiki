package it.algos.vaadwiki.backend.service;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.backend.application.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 15-ago-2021
 * Time: 08:18
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(ADidascaliaService.class); <br>
 * 3) @Autowired public DidascaliaService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DidascaliaService extends AbstractService {


    /**
     * Costruisce la didascalia completa per una lista (persone di nome, persone di cognome) <br>
     *
     * @param bio completa
     *
     * @return didascalia completa
     */
    public String getLista(final Bio bio) {
        String nomeCognome = this.getNomeCognome(bio);
        String attivitaNazionalita = this.getAttivitaNazionalita(bio);
        String natoMorto = this.getNatoMorto(bio);

        return String.format("%s%s%s%s%s", nomeCognome, VIRGOLA_SPAZIO, attivitaNazionalita, SPAZIO, natoMorto);
    }

    /**
     * Costruisce il nome e cognome (obbligatori) <br>
     * Si usa il titolo della voce direttamente, se non contiene parentesi <br>
     *
     * @param bio completa
     *
     * @return nome e cognome elaborati e inseriti nelle doppie quadre
     */
    public String getNomeCognome(final Bio bio) {
        return getNomeCognome(bio.wikiTitle, bio.nome, bio.cognome);
    }

    /**
     * Costruisce il nome e cognome (obbligatori) <br>
     * Si usa il titolo della voce direttamente, se non contiene parentesi <br>
     *
     * @param wikiTitle della pagina sul server wiki
     * @param nome      semplice (solo primo nome esclusi i nomi doppi)
     * @param cognome   completo
     *
     * @return nome e cognome elaborati e inseriti nelle doppie quadre
     */
    public String getNomeCognome(final String wikiTitle, final String nome, final String cognome) {
        String nomeCognome;
        String tagPar = PARENTESI_TONDA_INI;
        String tagPipe = PIPE;
        String nomePrimaDellaParentesi;
        boolean usaNomeCognomePerTitolo = false;

        if (usaNomeCognomePerTitolo) {
            nomeCognome = nome + SPAZIO + cognome;
            if (!nomeCognome.equals(wikiTitle)) {
                nomeCognome = wikiTitle + tagPipe + nomeCognome;
            }
        }
        else {
            // se il titolo NON contiene la parentesi, il nome non va messo perché coincide col titolo della voce
            if (wikiTitle.contains(tagPar)) {
                nomePrimaDellaParentesi = wikiTitle.substring(0, wikiTitle.indexOf(tagPar));
                nomeCognome = wikiTitle + tagPipe + nomePrimaDellaParentesi;
            }
            else {
                nomeCognome = wikiTitle;
            }
        }

        nomeCognome = nomeCognome.trim();
        nomeCognome = text.setDoppieQuadre(nomeCognome);

        return nomeCognome;
    }

    /**
     * Costruisce attività e nazionalità (obbligatori) <br>
     *
     * @param bio completa
     *
     * @return attività e nazionalità elaborati
     */
    public String getAttivitaNazionalita(final Bio bio) {
        return getAttivitaNazionalita(bio.wikiTitle, bio.attivita, bio.attivita2, bio.attivita3, bio.nazionalita);
    }

    /**
     * Costruisce attività e nazionalità (obbligatori) <br>
     *
     * @param wikiTitle   della pagina sul server wiki
     * @param attivita    principale
     * @param attivita2   facoltativa
     * @param attivita3   facoltativa
     * @param nazionalita unica
     *
     * @return attività e nazionalità elaborati
     */
    public String getAttivitaNazionalita(final String wikiTitle, final String attivita, final String attivita2, final String attivita3, final String nazionalita) {
        String attivitaNazionalita = VUOTA;

        if (text.isValid(attivita)) {
            attivitaNazionalita += attivita;
        }

        if (text.isValid(attivita2)) {
            if (text.isValid(attivita3)) {
                attivitaNazionalita += VIRGOLA_SPAZIO;
            }
            else {
                attivitaNazionalita += SPAZIO + "e" + SPAZIO;
            }
            attivitaNazionalita += attivita2;
        }

        if (text.isValid(attivita3)) {
            attivitaNazionalita += SPAZIO + "e" + SPAZIO;
            attivitaNazionalita += attivita3;
        }

        if (text.isValid(nazionalita)) {
            attivitaNazionalita += SPAZIO;
            attivitaNazionalita += nazionalita;
        }

        return attivitaNazionalita;
    }

    /**
     * Costruisce il blocco luogo-anno-nascita-morte (facoltativi) <br>
     *
     * @param bio completa
     *
     * @return luogo-anno-nascita-morte
     */
    public String getNatoMorto(final Bio bio) {
        String crono = VUOTA;
        String tagNato = WikiVar.simboloNato;
        String tagMorto = WikiVar.simboloMorto;
        String luogoNato = text.isValid(bio.luogoNato) ? bio.luogoNato : VUOTA;
        String luogoNatoLink = bio.luogoNatoLink;
        String annoNato = text.isValid(bio.annoNato) ? tagNato + bio.annoNato : VUOTA;
        String luogoMorto = text.isValid(bio.luogoMorto) ? bio.luogoMorto : VUOTA;
        String luogoMortoLink = bio.luogoMortoLink;
        String annoMorto = text.isValid(bio.annoMorto) ? tagMorto + bio.annoMorto : VUOTA;

        if (text.isValid(luogoNatoLink)) {
            luogoNato += PIPE + luogoNatoLink;
        }
        luogoNato = text.setDoppieQuadre(luogoNato);
        if (text.isValid(luogoMortoLink)) {
            luogoMorto += PIPE + luogoMortoLink;
        }
        luogoMorto = text.setDoppieQuadre(luogoMorto);

        crono += text.isValid(luogoNato) ? luogoNato : VUOTA;
        crono += text.isValid(luogoNato) && text.isValid(annoNato) ? VIRGOLA_SPAZIO : VUOTA;
        crono += annoNato;
        crono += text.isValid(luogoNato) || text.isValid(annoNato) ? SEP : VUOTA;
        crono += text.isValid(luogoMorto) ? luogoMorto : VUOTA;
        crono += text.isValid(luogoMorto) && text.isValid(annoMorto) ? VIRGOLA_SPAZIO : VUOTA;
        crono += annoMorto;

//        crono = text.levaCoda(crono, SEP);
        return text.isValid(crono) ? text.setTonde(crono) : VUOTA;
    }

    //    /**
    //     * Costruisce il blocco luogo-anno-nascita-morte (facoltativi) <br>
    //     *
    //     * @param wikiTitle      della pagina sul server wiki
    //     * @param luogoNato      facoltativo
    //     * @param luogoNatoLink  facoltativo
    //     * @param annoNato       facoltativo
    //     * @param luogoMorto     facoltativo
    //     * @param luogoMortoLink facoltativo
    //     * @param annoMorto      facoltativo
    //     *
    //     * @return luogo-anno-nascita-morte
    //     */
    //    public String getNatoMorto(final String wikiTitle, final String luogoNato, final String luogoNatoLink, final String annoNato, final String luogoMorto, final String luogoMortoLink, final String annoMorto) {
    //        String natoMorto = VUOTA;
    //
    //        natoMorto+=luogoNato;
    //        natoMorto+=luogoMorto;
    //
    //        return text.setTonde(natoMorto);
    //    }


}