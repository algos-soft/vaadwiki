package it.algos.wiki;



import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 30-10-12
 * Time: 13:33
 * Memorizza i risultati di una Query (che viene usata per l'effettivo collegamento)
 * Quattordici (14) parametri letti SEMPRE:
 * titolo, pageid, testo, ns, contentformat, revid, parentid, minor, user, userid, size, comment, timestamp, contentformat, contentmodel
 */
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class Page implements Serializable {

//    /**
//     * La injection viene fatta da SpringBoot in automatico <br>
//     */
//    @Autowired
public Api api;

    private static String APICI = "\"";
    private static String PUNTI = ":";
    private static String GRAFFA_INI = "{";
    private static String GRAFFA_END = "}";
    private static String VIR = ",";

    private boolean valida = false;
//    private boolean paginaScrittura = false

    //--tipo di request - solo una per leggere - due per scrivere
    //--di default solo lettura (per la scrittura serve il login)
    private TipoRequest tipoRequest = TipoRequest.read;

//    private String text //risultato completo della pagina

    private HashMap<String, Object> mappaReadTxt = new HashMap<String, Object>();
    private HashMap<String, Object> mappaReadObj = new HashMap<String, Object>();
    private HashMap<String, Object> mappaDB = new HashMap<String, Object>();

    public Page() {
    }// fine del metodo costruttore


    public Page(String testoPagina) {
        this(testoPagina, TipoRequest.read);
    }// fine del metodo costruttore

    public Page(JSONObject paginaJSON) {
        this.tipoRequest = TipoRequest.read;
        mappaReadTxt = LibWiki.creaMappaJSON(paginaJSON);
        mappaReadObj = LibWiki.converteMappa(mappaReadTxt);
        mappaDB = creaMappaDB(mappaReadObj);
        valida = PagePar.isParValidiRead(mappaReadObj);
    }// fine del metodo costruttore


    public Page(String testoPagina, TipoRequest tipoRequest) {
        this.tipoRequest = tipoRequest;
        mappaReadTxt = LibWiki.creaMappaQuery(testoPagina);
        mappaReadObj = LibWiki.converteMappa(mappaReadTxt);
        mappaDB = creaMappaDB(mappaReadObj);
        valida = PagePar.isParValidiRead(mappaReadObj);
    }// fine del metodo costruttore

    /**
     * Crea la mappa per il Database
     * Aggiunge alla mappa eventuali parametri NON letti dal server
     * In particolare la data di questa lettura
     *
     * @param mappaRead in ingresso
     * @return mappa modificata
     */
    private static HashMap<String, Object> creaMappaDB(HashMap<String, Object> mappaRead) {
        HashMap<String, Object> mappaDatabase = new HashMap<String, Object>();

        for (String key : mappaRead.keySet()) {
            if (mappaRead.containsKey(key) && PagePar.isDatabase(key)) {
                mappaDatabase.put(key, mappaRead.get(key));
            }// fine del blocco if
        } // fine del ciclo for-each

        mappaDatabase.put((String) PagePar.ultimalettura.toString(), LibWiki.getTime());

        return mappaDatabase;
    }// fine del metodo

    private static String apici(String entrata) {
        return APICI + entrata + APICI;
    }// fine del metodo

    private static String graffe(String entrata) {
        return GRAFFA_INI + entrata + GRAFFA_END;
    }// fine del metodo


    public HashMap getMappaReadTxt() {
        return mappaReadTxt;
    }// fine del metodo

    public HashMap getMappaReadObj() {
        return mappaReadObj;
    }// fine del metodo

    public HashMap getMappaDB() {
        return mappaDB;
    }// fine del metodo

    public long getPageid() {
        return (long) mappaReadObj.get(PagePar.pageid.toString());
    }// fine del metodo

    public String getTitle() {
        return (String) mappaReadObj.get(PagePar.title.toString());
    }// fine del metodo

    public String getText() {
        return (String) mappaReadObj.get(PagePar.content.toString());
    }// fine del metodo

    public Timestamp getTimestamp() {
        return (Timestamp) mappaReadObj.get(PagePar.timestamp.toString());
    }// fine del metodo


    public boolean isValida() {
        return valida;
    }// fine del metodo


    public void setValida(boolean valida) {
        this.valida = valida;
    }// fine del metodo
} //fine della classe
