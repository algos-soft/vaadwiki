package it.algos.wiki;

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 30-10-12
 * Time: 13:23
 */

public enum ErrLogin {

    success("Success", ""),
    noName("NoName", "You didn't set the lgname parameter"),
    illegal("Illegal", "You provided an illegal username"),
    notExists("NotExists", "The username you provided doesn't exist"),
    emptyPass("EmptyPass", "You didn't set the lgpassword parameter or you left it empty"),
    wrongPass("WrongPass", "The password you provided is incorrect"),
    wrongPluginPass("WrongPluginPass", "Same as WrongPass, returned when an authentication plugin rather than MediaWiki itself rejected the password"),
    createBlocked("CreateBlocked", "The wiki tried to automatically create a new account for you, but your IP address has been blocked from account creation"),
    throttled("Throttled", "You've logged in too many times in a short time. See also throttling"),
    blocked("Blocked", "User is blocked"),
    mustbeposted("mustbeposted", "The login module requires a POST request"),
    needToken("NeedToken", "Either you did not provide the login token or the sessionid cookie. Request again with the token and cookie given in this response"),
    lettura("Lettura", "Solo lettura pagina singola"),
    generico("generico", "errore generico"),
    noHost("noHost", "java.net.UnknownHostException");

    String tag;
    String messaggio;

    ErrLogin(String tag, String messaggio) {
        this.setTag(tag);
        this.setMessaggio(messaggio);
        String tagCorrente;
    }// end of constructor


    /**
     * Restituisce l'errore dal tag
     *
     * @param tag in entrata
     * @return errore
     */
    public static ErrLogin get(String tag) {
        ErrLogin errore = null;
        String tagCorrente;

        // controllo di congruità
        if (tag != null) {
            for (ErrLogin err : ErrLogin.values()) {
                tagCorrente = err.getTag();
                if (tagCorrente.equals(tag)) {
                    errore = err;
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        return errore;
    } // fine della closure


    public String getTag() {
        return tag;
    }

    private void setTag(String tag) {
        this.tag = tag;
    }

    public String getMessaggio() {
        return messaggio;
    }

    private void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

} // fine della Enumeration
