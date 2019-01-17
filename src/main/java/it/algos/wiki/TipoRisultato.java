package it.algos.wiki;

/**
 * Created by gac on 22 ott 2016.
 */
public enum TipoRisultato {
    erroreGenerico("Errore generico."),
    nonTrovata("Pagina inesistente."),
    esistente("Pagina esistente."),
    letta("Letta la pagina."),
    limitOver("cmlimit may not be over"),

    creata("Creata nuova pagina."),
    esistenteNuova("Pagina già esistente."),

    noLogin("Manca il login"),
    noPreliminaryToken("Manca il token"),
    mustbeposted("The login module requires a POST request"),
    assertuserfailed("Assertion that the user is logged in failed"),
    assertbotfailed("Assertion that the user has the bot right failed"),
    loginUser("Collegato come utente"),
    loginBot("Collegato come bot"),
    loginSysop("Collegato come admin"),
    notExists("The username you provided doesn't exist"),
    wrongPass("The password you provided is incorrect"),
    throttled("You've logged in too many times in a short time"),
    noto("The to parameter must be set"),
    notoken("The token parameter must be set"),
    invalidtitle("Bad title"),
    selfmove("Source and destination titles are the same; cannot move a page over itself"),
    missingtitle("The page you requested doesn't exist"),

    registrata("Pagina registrata."),
    nonRegistrata("Pagina non registrata (probabilmente cancellata)."),
    modificaRegistrata("Registrata modifica alla voce."),
    modificaInutile("La voce aveva già il testo richiesto."),
    nochange("La pagina non è stata modificata"),

    spostata("Pagina spostata"),
    articleexists("A page of that name already exists, or the name you have chosen is not valid. Please choose another name"),
    protectedtitle("You cannot move a page to this location because the new title has been protected from creation"),

    tooBig("This result was truncated because it would otherwise be larger than the limit of 12,582,912 bytes.");

    private String tag;


    /**
     * Costruttore completo con parametri.
     *
     * @param tag in entrata
     */
    TipoRisultato(String tag) {
        /* regola le variabili di istanza coi parametri */
        this.setTag(tag);
    }// fine del metodo costruttore


    public String getTag() {
        return tag;
    }// end of getter method


    public void setTag(String tag) {
        this.tag = tag;
    }//end of setter method

}
