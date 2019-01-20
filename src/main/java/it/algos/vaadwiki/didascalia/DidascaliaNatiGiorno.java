package it.algos.vaadwiki.didascalia;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadwiki.application.WikiCost.TAG_SEPARATORE;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 18-gen-2019
 * Time: 19:05
 * Didascalia specializzata per le liste di nati nel giorno.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class DidascaliaNatiGiorno extends Didascalia {

    private static String TAG_VIRGOLA = "," + " ";

    private static String TAG_NATO = "n.";

    private static String TAG_MORTO = "†";

    protected String wikiTitle = VUOTA;

    protected String nome = VUOTA;

    protected String cognome = VUOTA;

    protected String luogoNatoLink = VUOTA;

    protected String giornoNato = VUOTA;

    protected String annoNato = VUOTA;

    protected String luogoMortoLink = VUOTA;

    protected String giornoMorto = VUOTA;

    protected String annoMorto = VUOTA;

    protected String attivita = VUOTA;

    protected String attivita2 = VUOTA;

    protected String attivita3 = VUOTA;

    protected String nazionalita = VUOTA;

    private String testo = VUOTA;


    public String esegue(Bio bio) {
        this.recuperaDatiAnagrafici(bio);
        this.recuperaDatiCrono(bio);
        this.recuperaDatiLocalita(bio);
        this.recuperaDatiAttNaz(bio);
        this.regolaDidascalia(bio);

        return testo;
    }// end of method


    /**
     * Recupera dal record di biografia i valori anagrafici
     */
    private void recuperaDatiAnagrafici(Bio bio) {
        if (bio.getWikiTitle() != null) {
            this.wikiTitle = bio.getWikiTitle();
        }// fine del blocco if

        if (bio.getNome() != null) {
            this.nome = bio.getNome();
        }// fine del blocco if

        if (bio.getCognome() != null) {
            this.cognome = bio.getCognome();
        }// fine del blocco if
    }// end of method


    /**
     * Recupera dal record di biografia i valori cronografici
     */
    private void recuperaDatiCrono(Bio bio) {
        if (bio.getGiornoNato() != null) {
            this.giornoNato = bio.getGiornoNato();
        }// fine del blocco if

        if (bio.getGiornoMorto() != null) {
            this.giornoMorto = bio.getGiornoMorto();
        }// fine del blocco if

        if (bio.getAnnoNato() != null) {
            this.annoNato = bio.getAnnoNato();
        }// fine del blocco if

        if (bio.getAnnoMorto() != null) {
            this.annoMorto = bio.getAnnoMorto();
        }// fine del blocco if
    }// end of method


    /**
     * Recupera dal record di biografia i valori delle località
     */
    private void recuperaDatiLocalita(Bio bio) {
        if (bio.getLuogoNatoLink() != null) {
            this.luogoNatoLink = bio.getLuogoNatoLink();
        }// fine del blocco if

        if (bio.getLuogoMortoLink() != null) {
            this.luogoMortoLink = bio.getLuogoMortoLink();
        }// fine del blocco if
    }// end of method


    /**
     * Recupera dal record di biografia i valori delle attività e della nazionalità
     */
    private void recuperaDatiAttNaz(Bio bio) {
        if (bio.getAttivita() != null) {
            this.attivita = bio.getAttivita();
        }// fine del blocco if

        if (bio.getAttivita2() != null) {
            this.attivita2 = bio.getAttivita2();
        }// fine del blocco if

        if (bio.getAttivita3() != null) {
            this.attivita3 = bio.getAttivita3();
        }// fine del blocco if

        if (bio.getNazionalita() != null) {
            this.nazionalita = bio.getNazionalita();
        }// fine del blocco if
    }// end of method


    /**
     * Costruisce il testo della didascalia
     * Sovrascritto
     */
    protected void regolaDidascalia(Bio bio) {
        testo = VUOTA;

        // blocco iniziale (potrebbe non esserci)
        testo += getBloccoIniziale();

        // titolo e nome (obbligatori)
        testo += this.getNomeCognome();

        // attivitaNazionalita (potrebbe non esserci)
        testo += this.getAttNaz();

        // blocco finale (potrebbe non esserci)
        testo += this.getBloccoFinale(bio);
    }// end of method


    /**
     * Costruisce il blocco iniziale (potrebbe non esserci)
     * Sovrascritto
     */
    protected String getBloccoIniziale() {
        return VUOTA;
    }// end of method


    /**
     * Costruisce il nome e cognome (obbligatori)
     * Si usa il titolo della voce direttamente, se non contiene parentesi
     */
    private String getNomeCognome() {
        String nomeCognome = VUOTA;
        String titoloVoce;
        String tagPar = "(";
        String tagPipe = "|";
        String nomePrimaDellaParentesi;
        titoloVoce = this.wikiTitle;
        boolean usaNomeCognomePerTitolo = false;

        if (usaNomeCognomePerTitolo) {
            nomeCognome = this.nome + SPAZIO + this.cognome;
            if (!nomeCognome.equals(titoloVoce)) {
                nomeCognome = titoloVoce + tagPipe + nomeCognome;
            }// fine del blocco if
        } else {
            // se il titolo NON contiene la parentesi, il nome non va messo perché coincide col titolo della voce
            if (titoloVoce.contains(tagPar)) {
                nomePrimaDellaParentesi = titoloVoce.substring(0, titoloVoce.indexOf(tagPar));
                nomeCognome = titoloVoce + tagPipe + nomePrimaDellaParentesi;
            } else {
                nomeCognome = titoloVoce;
            }// fine del blocco if-else
        }// fine del blocco if-else

        nomeCognome = nomeCognome.trim();
        nomeCognome = LibWiki.setQuadre(nomeCognome);

        return nomeCognome;
    }// end of method


    /**
     * Costruisce la stringa attività e nazionalità della didascalia
     * <p>
     * I collegamenti alle tavole Attività e Nazionalità, potrebbero esistere nella biografia
     * anche se successivamente i corrispondenti records (di Attività e Nazionalità) sono stati cancellati
     * Occorre quindi proteggere il codice dall'errore (dovuto ad un NON aggiornamento dei dati della biografia)
     *
     * @return testo
     */
    private String getAttNaz() {
        String attNazDidascalia = VUOTA;
        String attivita = this.attivita;
        String attivita2 = this.attivita2;
        String attivita3 = this.attivita3;
        String nazionalita = this.nazionalita;
        String tagAnd = SPAZIO + "e" + SPAZIO;
        String tagSpa = SPAZIO;
        String tagVir = "," + SPAZIO;
        boolean virgolaDopoPrincipale = false;
        boolean andDopoPrincipale = false;
        boolean andDopoSecondaria = false;

        // la virgolaDopoPrincipale c'è se è presente la seconda attività e la terza
        if (!attivita2.equals(VUOTA) && !attivita3.equals(VUOTA)) {
            virgolaDopoPrincipale = true;
        }// fine del blocco if

        // la andDopoPrincipale c'è se è presente la seconda attività e non la terza
        if (!attivita2.equals(VUOTA) && attivita3.equals(VUOTA)) {
            andDopoPrincipale = true;
        }// fine del blocco if

        // la andDopoSecondaria c'è se è presente terza attività
        if (!attivita3.equals(VUOTA)) {
            andDopoSecondaria = true;
        }// fine del blocco if

        // attività principale
        if (!attivita.equals(VUOTA)) {
            attNazDidascalia += attivita;
        }// fine del blocco if

        // virgola
        if (virgolaDopoPrincipale) {
            attNazDidascalia += tagVir;
        }// fine del blocco if

        // and
        if (andDopoPrincipale) {
            attNazDidascalia += tagAnd;
        }// fine del blocco if

        // attività secondaria
        if (!attivita2.equals(VUOTA)) {
            attNazDidascalia += attivita2;
        }// fine del blocco if

        // and
        if (andDopoSecondaria) {
            attNazDidascalia += tagAnd;
        }// fine del blocco if

        // attività terziaria
        if (!attivita3.equals(VUOTA)) {
            attNazDidascalia += attivita3;
        }// fine del blocco if

        // nazionalità facoltativo
        if (!nazionalita.equals(VUOTA)) {
            attNazDidascalia += tagSpa;
            attNazDidascalia += nazionalita;
        }// fine del blocco if

        if (!attNazDidascalia.equals(VUOTA)) {
            attNazDidascalia = tagVir + attNazDidascalia + tagSpa;
        }// fine del blocco if

        // valore di ritorno
        return attNazDidascalia.trim();
    }// end of method


    /**
     * Costruisce il blocco finale (potrebbe non esserci)
     * Sovrascritto
     *
     * @return testo
     */
    protected String getBloccoFinale(Bio bio) {
        String text = VUOTA;
        String tagParIni = SPAZIO + "(";
        String tagParEnd = ")";
        String textNascita = getBloccoFinaleNascita();
        String textMorte = getBloccoFinaleMorte();
        boolean isEsisteNascita = !textNascita.equals(VUOTA);
        boolean isEsisteMorte = !textMorte.equals(VUOTA);

        if (isEsisteNascita || isEsisteMorte) {
        } else {
            return VUOTA;
        }// end of if/else cycle

        // costruisce il blocco finale (potrebbe non esserci)
        text += tagParIni;
        if (isEsisteNascita) {
            text += textNascita;
        }// fine del blocco if

        if (isEsisteNascita && isEsisteMorte) {
            text += TAG_SEPARATORE;
        }// end of if cycle

        if (isEsisteMorte) {
            text += textMorte;
        }// fine del blocco if

//        text = getMostraAnni(bio, text);

        text += tagParEnd;

        return text;
    }// end of method


    /**
     * Parte nascita del blocco finale (potrebbe non esserci)
     *
     * @return testo
     */
    private String getBloccoFinaleNascita() {
        String text = VUOTA;
        boolean isEsisteLocalita = !luogoNatoLink.equals(VUOTA);
        boolean isEsisteNascita = !annoNato.equals(VUOTA);

        if (isEsisteLocalita) {
            if (!isEsisteNascita) {
                text += TAG_NATO;
            }// end of if cycle
            text += LibWiki.setQuadre(luogoNatoLink);
        }// fine del blocco if

        if (isEsisteLocalita && isEsisteNascita) {
            text += TAG_VIRGOLA;
        }// end of if cycle

        if (isEsisteNascita) {
            text += TAG_NATO;
            text += LibWiki.setQuadre(annoNato);
        }// fine del blocco if

        return text;
    }// end of method


    /**
     * Parte morte del blocco finale (potrebbe non esserci)
     *
     * @return testo
     */
    private String getBloccoFinaleMorte() {
        String text = VUOTA;
        boolean isEsisteLocalita = !luogoMortoLink.equals(VUOTA);
        boolean isEsisteMorte = !annoMorto.equals(VUOTA);

        if (isEsisteLocalita) {
            if (!isEsisteMorte) {
                text += TAG_MORTO;
            }// end of if cycle
            text += LibWiki.setQuadre(luogoMortoLink);
        }// fine del blocco if

        if (isEsisteLocalita && isEsisteMorte) {
            text += TAG_VIRGOLA;
        }// fine del blocco if

        if (isEsisteMorte) {
            text += TAG_MORTO;
            text += LibWiki.setQuadre(annoMorto);
        }// fine del blocco if

        return text;
    }// end of method


//    protected String getMostraAnni(Bio bio, String testoIn) {
//        String testoOut = testoIn;
//        boolean mostraAnniVissuti = Pref.getBool(CostBio.USA_ETA_VISSUTA);
//        int giorni = 365;
//        Anno annoNato = bio.getAnnoNatoPunta();
//        Anno annoMorto = bio.getAnnoMortoPunta();
//        Giorno giornoNato = bio.getGiornoNatoPunta();
//        Giorno giornoMorto = bio.getGiornoMortoPunta();
//        int delta = 0;
//        int anni = 0;
//        int numeroNato = 0;
//        int numeroMorto = 0;
//
//        if (annoNato != null && giornoNato != null) {
//            numeroNato = annoNato.getOrdinamento() * giorni + giornoNato.getOrdinamento();
//        }// end of if cycle
//        if (annoMorto != null && giornoMorto != null) {
//            numeroMorto = annoMorto.getOrdinamento() * giorni + giornoMorto.getOrdinamento();
//        }// end of if cycle
//
//        if (numeroNato > 0 && numeroMorto > 0) {
//            delta = numeroMorto - numeroNato;
//        }// end of if cycle
//
//        if (delta > giorni) {
//            anni = delta / giorni;
//        }// end of if cycle
//
//        if (mostraAnniVissuti && anni > 0) {
//            testoOut += ", " + anni + " anni";
//        }// end of if cycle
//
//        return testoOut;
//    }// end of method

}// end of class
