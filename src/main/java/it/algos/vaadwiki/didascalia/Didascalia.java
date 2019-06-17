package it.algos.vaadwiki.didascalia;

import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.wiki.LibWiki;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import static it.algos.vaadflow.application.FlowCost.SPAZIO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.TAG_SEPARATORE;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 18-gen-2019
 * Time: 19:07
 * <p>
 * Classe specializzata per le didascalie costruibili a partire dal template Bio per le liste .
 * <p>
 * Didascalie cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno <br>
 * Didascalie di liste di nomi e cognomi (in namespace principale). <br>
 * Didascalie di liste di attività e nazionalità (in Progetto:Biografie). <br>
 * Ogni didascalia ha un testo completo ed un testo senza chiave identificativa del paragrafo <br>
 * <p>
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 */
public abstract class Didascalia {

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

    protected boolean usaChiave = true;

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

    protected EADidascalia type;

    protected Bio bio;

    /**
     * Testo della didascalia CON la chiave che viene usata nella composizione con 'righeParagrafo' <br>
     */
    public String testoCon = VUOTA;

    /**
     * Testo della didascalia SENZA la chiave che viene aggiunta nella composizione della pagina <br>
     * La chiave viene aggiunta in maniera differente tra 'righeSemplici' o 'righeRaggruppate' <br>
     */
    public String testoSenza = VUOTA;


    public Didascalia() {
    }// end of constructor


    public Didascalia(Bio bio, EADidascalia type) {
        this.bio = bio;
        this.type = type;
    }// end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se ci sono superclassi e sottoclassi, chiama prima @PostConstruct della superclasse <br>
     */
    @PostConstruct
    protected void inizia() {
        if (bio != null) {
            this.esegue(bio);
        }// end of if cycle
    }// end of method


    public void esegue(Bio bio) {
        this.reset();
        this.recuperaDatiAnagrafici(bio);
        this.recuperaDatiCrono(bio);
        this.recuperaDatiLocalita(bio);
        this.recuperaDatiAttNaz(bio);
        this.regolaDidascalia();
    }// end of method


    protected void reset() {
        testoCon = VUOTA;
        testoSenza = VUOTA;
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
        if (bio.getGiornoNascita() != null) {
            this.giornoNato = bio.getGiornoNascita().titolo;
        }// fine del blocco if

        if (bio.getGiornoMorte() != null) {
            this.giornoMorto = bio.getGiornoMorte().titolo;
        }// fine del blocco if

        if (bio.getAnnoNascita() != null) {
            this.annoNato = bio.getAnnoNascita().titolo;
        }// fine del blocco if

        if (bio.getAnnoMorte() != null) {
            this.annoMorto = bio.getAnnoMorte().titolo;
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
            this.attivita = bio.getAttivita().singolare;
        }// fine del blocco if

        if (bio.getAttivita2() != null) {
            this.attivita2 = bio.getAttivita2().singolare;
        }// fine del blocco if

        if (bio.getAttivita3() != null) {
            this.attivita3 = bio.getAttivita3().singolare;
        }// fine del blocco if

        if (bio.getNazionalita() != null) {
            this.nazionalita = bio.getNazionalita().singolare;
        }// fine del blocco if
    }// end of method


    /**
     * Costruisce il testo della didascalia
     * Sovrascritto
     */
    protected void regolaDidascalia() {
        testoSenza = VUOTA;
        testoCon = VUOTA;

        // blocco iniziale (potrebbe non esserci)
        testoSenza += getBloccoIniziale();

        // titolo e nome (obbligatori)
        testoSenza += this.getNomeCognome();

        // attivitaNazionalita (potrebbe non esserci)
        testoSenza += this.getAttNaz();

        // blocco finale (potrebbe non esserci)
        testoSenza += this.getBloccoFinale();

        testoCon = testoSenza;
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
