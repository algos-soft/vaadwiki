package it.algos.wiki.request;


/**
 * Created by gac on 03 dic 2015.
 * .
 */
public class RequestWikiReadMulti extends RequestWiki{


//    //--stringa (separata da pipe oppure da virgola) delle pageids
//    protected String stringaPageIds;
//
//    @Autowired
//    protected ATextService text;

//    /**
//     * Costruttore
//     * <p>
//     * Le sottoclassi non invocano il costruttore
//     * Prima regolano alcuni parametri specifici
//     * Poi invocano il metodo doInit() della superclasse astratta
//     *
//     * @param listaPageIds elenco di pageids (long)
//     */
//    public RequestWikiReadMulti(long[] listaPageIds) {
//        this(array.fromLong(listaPageIds));
//    }// fine del metodo costruttore
//
//    /**
//     * Costruttore
//     * <p>
//     * Le sottoclassi non invocano il costruttore
//     * Prima regolano alcuni parametri specifici
//     * Poi invocano il metodo doInit() della superclasse astratta
//     *
//     * @param arrayPageIds elenco di pageids (ArrayList)
//     */
//    public RequestWikiReadMulti(ArrayList<Long> arrayPageIds) {
//        this(array.toStringaPipe(arrayPageIds));
//    }// fine del metodo costruttore

//    /**
//     * Costruttore completo
//     * <p>
//     * Le sottoclassi non invocano il costruttore
//     * Prima regolano alcuni parametri specifici
//     * Poi invocano il metodo doInit() della superclasse astratta
//     *
//     * @param stringaPageIds stringa (separata da pipe oppure da virgola) delle pageids
//     */
//    public RequestWikiReadMulti(String stringaPageIds) {
//
//        if (stringaPageIds.contains(",")) {
//            stringaPageIds = text.sostituisce(stringaPageIds, ",", "|");
//        }// end of if/else cycle
//
//        if (stringaPageIds.contains("|")) {
//            this.stringaPageIds = stringaPageIds;
//            tipoRicerca = TipoRicerca.listaPageids;
//        }// end of if/else cycle
//
//        super.doInit();
//    }// fine del metodo costruttore completo

    /**
     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    public void doInit() {
        super.needPost = false;
        super.needLogin = true;
        super.needToken = true;
        super.needContinua = false;
        super.doInit();
    } // fine del metodo


} // fine della classe
