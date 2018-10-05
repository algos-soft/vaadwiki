package it.algos.wiki;

/**
 * Legge una pagina internet
 * Accetta SOLO un domain (indirizzo) completo
 */
public class Read {

//    // codifica dei caratteri
//    private static String INPUT = "UTF8";
//    private static String WEB_MISSING = " <html><!--<?xml version=\"1.0\" encoding=\"UTF-8\"?><WISPAccessGatewayParam xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance";
//    // indirizzo internet da legger
//    protected String domain;
//    // contenuto della pagina
//    protected String contenuto;
//    // verifica finale
//    protected boolean trovata = false;
//    private String errore = "";
//
//    /**
//     * Costruttore utilizzato da una sottoclasse
//     * <p>
//     * L'istanza della sottoclasse usa un costruttore senza parametri
//     * Regola alcune property
//     * Regola il domain
//     * Invoca il metodo inizializza() della superclasse (questa)
//     */
//    public Read() {
//    }// fine del metodo costruttore senza parametri
//
//    /**
//     * Costruttore completo
//     * <p>
//     * L'istanza di questa classe viene chiamata con il domain già regolato
//     * Parte subito il metodo inizializza() che esegue la Request
//     */
//    public Read(String domain) {
//        this.domain = domain;
//        this.inizializza();
//    }// fine del metodo costruttore completo
//
//    protected void inizializza() {
//        try { // prova ad eseguire il codice
////            this.firstRequest();
//        } catch (Exception unErrore) { // intercetta l'errore
//            errore = unErrore.getClass().getSimpleName();
//        }// fine del blocco try-catch
//    } // fine del metodo
//
//    /**
//     * Recupera il contenuto completo della pagina
//     */
//    private void firstRequest() throws Exception {
//        URLConnection connection = null;
//        InputStream input = null;
//        InputStreamReader inputReader = null;
//        BufferedReader readBuffer = null;
//        StringBuilder textBuffer = new StringBuilder();
//        String stringa;
//
//        // find the target
//        connection = new URL(domain).openConnection();
//        connection.setDoOutput(true);
//        connection.setRequestProperty("Accept-Encoding", "GZIP");
//        connection.setRequestProperty("Content-Encoding", "GZIP");
//        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
//        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; it-it) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3");
//
//        // regola l'entrata
//        input = connection.getInputStream();
//        inputReader = new InputStreamReader(input, INPUT);
//
//        // legge la risposta
//        readBuffer = new BufferedReader(inputReader);
//        while ((stringa = readBuffer.readLine()) != null) {
//            textBuffer.append(stringa);
//        }// fine del blocco while
//
//        // chiude
//        readBuffer.close();
//        inputReader.close();
//        input.close();
//
//        // valore di ritorno della request
//        contenuto = textBuffer.toString();
//        trovata = isValida();
//    } // fine del metodo
//
//
//    protected boolean isValida() {
//        boolean valida = true;
//
//        if (contenuto.equals("")) {
//            valida = false;
//        }// fine del blocco if
//
//        if (contenuto.startsWith(WEB_MISSING)) {
//            valida = false;
//            contenuto = null;
//            errore = "UnknownHostException";
//        }// fine del blocco if
//
//        return valida;
//    } // end of getter method
//
//    public String getContenuto() {
//        return contenuto;
//    } // end of getter method
//
//    public boolean isTrovata() {
//        return trovata;
//    } // end of getter method
//
//    public String getErrore() {
//        return errore;
//    } // end of getter method
} // fine della classe
