package it.algos.vaadwiki.didascalia;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.SPAZIO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.TAG_SEPARATORE;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 18-gen-2019
 * Time: 19:07
 * Didascalia specializzata per le liste costruibili a partire dal template Bio.
 * Cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno
 * Attività e nazionalità (in Progetto:Biografie).
 * <p>
 * Sovrascritta nelle sottoclassi concrete
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class Didascalia {

    public static String TAG_SEP = " - ";

    protected static String TAG_VIRGOLA = "," + " ";

    protected static String TAG_NATO = "n.";

    protected static String TAG_MORTO = "†";

    @Autowired
    public ATextService text;

    @Autowired
    public GiornoService giornoService;

    @Autowired
    public AnnoService annoService;

    @Autowired
    public DidascaliaCompleta didascaliaCompleta;

    @Autowired
    public DidascaliaGiornoNato didascaliaGiornoNato;

    @Autowired
    public DidascaliaAnnoNato didascaliaAnnoNato;

    @Autowired
    public DidascaliaGiornoMorto didascaliaGiornoMorto;

    @Autowired
    public DidascaliaAnnoMorto didascaliaAnnoMorto;

    @Autowired
    public DidascaliaStandard didascaliaStandard;

    protected String wikiTitle = VUOTA;

    protected String nome = VUOTA;

    protected String cognome = VUOTA;

    protected String luogoNato = VUOTA;

    protected String giornoNato = VUOTA;

    protected String annoNato = VUOTA;

    protected String luogoMorto = VUOTA;

    protected String giornoMorto = VUOTA;

    protected String annoMorto = VUOTA;

    protected String attivita = VUOTA;

    protected String attivita2 = VUOTA;

    protected String attivita3 = VUOTA;

    protected String nazionalita = VUOTA;

    protected String testo = VUOTA;


    public String esegue(Bio bio, EADidascalia type) {
        return esegue(bio, type, true);
    }// end of method


    public String esegue(Bio bio, EADidascalia type, boolean usaChiave) {
        String testo = VUOTA;

        switch (type) {
            case completa:
                testo = didascaliaCompleta.esegue(bio, usaChiave);
                break;
            case giornoNato:
                testo = didascaliaGiornoNato.esegue(bio, usaChiave);
                break;
            case annoNato:
                testo = didascaliaAnnoNato.esegue(bio, usaChiave);
                break;
            case giornoMorto:
                testo = didascaliaGiornoMorto.esegue(bio, usaChiave);
                break;
            case annoMorto:
                testo = didascaliaAnnoMorto.esegue(bio, usaChiave);
                break;
            case standard:
                testo = didascaliaStandard.esegue(bio, usaChiave);
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        return testo;
    }// end of method


    public String esegue(Bio bio) {
        return esegue(bio, true);
    }// end of method


    public String esegue(Bio bio, boolean usaChiave) {
        this.reset();
        this.recuperaDatiAnagrafici(bio);
        this.recuperaDatiCrono(bio);
        this.recuperaDatiLocalita(bio);
        this.recuperaDatiAttNaz(bio);
        this.regolaDidascalia(usaChiave);

        return testo;
    }// end of method


    protected void reset() {
        testo = VUOTA;
        wikiTitle = VUOTA;
        nome = VUOTA;
        cognome = VUOTA;
        luogoNato = VUOTA;
        giornoNato = VUOTA;
        annoNato = VUOTA;
        luogoMorto = VUOTA;
        giornoMorto = VUOTA;
        annoMorto = VUOTA;
        attivita = VUOTA;
        attivita2 = VUOTA;
        attivita3 = VUOTA;
        nazionalita = VUOTA;
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
        if (bio.getLuogoNato() != null) {
            this.luogoNato = bio.getLuogoNato();
        }// fine del blocco if

        if (bio.getLuogoMorto() != null) {
            this.luogoMorto = bio.getLuogoMorto();
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
    protected void regolaDidascalia(boolean usaChiave) {
        testo = VUOTA;

        // blocco iniziale (potrebbe non esserci)
        testo += getBloccoIniziale();

        // titolo e nome (obbligatori)
        testo += this.getNomeCognome();

        // attivitaNazionalita (potrebbe non esserci)
        testo += this.getAttNaz();

        // blocco finale (potrebbe non esserci)
        testo += this.getBloccoFinale();
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
    protected String getNomeCognome() {
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
    protected String getAttNaz() {
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
    protected String getBloccoFinale() {
        String testo = VUOTA;
        String tagParIni = SPAZIO + "(";
        String tagParEnd = ")";
        String textNascita = getBloccoFinaleNascita();
        String textMorte = getBloccoFinaleMorte();
        boolean isEsisteNascita = text.isValid(textNascita);
        boolean isEsisteMorte = text.isValid(textMorte);

        if (isEsisteNascita || isEsisteMorte) {
        } else {
            return VUOTA;
        }// end of if/else cycle

        // costruisce il blocco finale (potrebbe non esserci)
        testo += tagParIni;
        if (isEsisteNascita) {
            testo += textNascita;
        }// fine del blocco if

        if (isEsisteNascita && isEsisteMorte) {
            testo += TAG_SEPARATORE;
        }// end of if cycle

        if (isEsisteMorte) {
            testo += textMorte;
        }// fine del blocco if

//        text = getMostraAnni(bio, text);

        testo += tagParEnd;

        return testo;
    }// end of method


    /**
     * Parte nascita del blocco finale (potrebbe non esserci)
     *
     * @return testo
     */
    private String getBloccoFinaleNascita() {
        String testo = VUOTA;
        boolean isEsisteLocalita = text.isValid(luogoNato);
        boolean isEsisteNascita = text.isValid(annoNato);
        boolean isEsisteGiorno = text.isValid(giornoNato);

        if (isEsisteLocalita) {
            if (!isEsisteNascita) {
                testo += TAG_NATO;
            }// end of if cycle
            if (luogoNato.endsWith(")")) {
                testo += LibWiki.setQuadre(luogoNato + "|");
            } else {
                testo += LibWiki.setQuadre(luogoNato);
            }// end of if/else cycle
        }// fine del blocco if

        if (isEsisteLocalita && isEsisteNascita) {
            testo += TAG_VIRGOLA;
        }// end of if cycle

        if (isEsisteNascita) {
            if (isEsisteGiorno) {
                testo += LibWiki.setQuadre(giornoNato);
            } else {
                testo += TAG_NATO;
            }// end of if/else cycle
            testo += LibWiki.setQuadre(annoNato);
        }// fine del blocco if

        return testo;
    }// end of method


    /**
     * Parte morte del blocco finale (potrebbe non esserci)
     *
     * @return testo
     */
    private String getBloccoFinaleMorte() {
        String testo = VUOTA;
        boolean isEsisteLocalita = text.isValid(luogoMorto);
        boolean isEsisteMorte = text.isValid(annoMorto);
        boolean isEsisteGiorno = text.isValid(giornoMorto);

        if (isEsisteLocalita) {
            if (!isEsisteMorte) {
                testo += TAG_MORTO;
            }// end of if cycle
            if (luogoMorto.endsWith(")")) {
                testo += LibWiki.setQuadre(luogoMorto + "|");
            } else {
                testo += LibWiki.setQuadre(luogoMorto);
            }// end of if/else cycle
        }// fine del blocco if

        if (isEsisteLocalita && isEsisteMorte) {
            testo += TAG_VIRGOLA;
        }// fine del blocco if

        if (isEsisteMorte) {
            if (isEsisteGiorno) {
                testo += LibWiki.setQuadre(giornoMorto);
            } else {
                testo += TAG_MORTO;
            }// end of if/else cycle
            testo += LibWiki.setQuadre(annoMorto);
        }// fine del blocco if

        return testo;
    }// end of method


}// end of class
