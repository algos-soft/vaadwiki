package it.algos.vaadflow14.backend.wrapper;


import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: gio, 09-lug-2020
 * Time: 06:31
 * Wrap di informazioni per la ricerca in ViewList <br>
 */
public class WrapSearch {

    //--flag di preferenza per selezionare la ricerca testuale
    private AESearch searchType;

    //--property della entity su cui effettuare la ricerca
    private String searchProperty;


    /**
     * Costruttore <br>
     * Manca il parametro searchType quindi di default usa AESearch.nonUsata <br>
     */
    public WrapSearch() {
        this(AESearch.nonUsata);
    }


    /**
     * Costruttore <br>
     * Manca il parametro searchProperty che è obbligatorio per AESearch.editField <br>
     * Quindi searchType può essere solo AESearch.dialog oppure AESearch.nonUsata <br>
     *
     * @param searchType flag di preferenza per selezionare la ricerca testuale
     */
    public WrapSearch(AESearch searchType) {
        this(searchType,VUOTA);
    }


    /**
     * Costruttore <br>
     * Se uso il parametro searchProperty, searchType può essere solo AESearch.editField <br>
     *
     * @param searchProperty property della entity su cui effettuare la ricerca
     */
    public WrapSearch(String searchProperty) {
        this(AESearch.editField,searchProperty);
    }


    /**
     * Costruttore <br>
     *
     * @param searchType     flag di preferenza per selezionare la ricerca testuale
     * @param searchProperty property della entity su cui effettuare la ricerca
     */
    public WrapSearch(AESearch searchType, String searchProperty) {
        this.searchType = searchType;
        this.searchProperty = searchProperty;
    }


    public AESearch getSearchType() {
        return searchType;
    }


    public String getSearchProperty() {
        return searchProperty;
    }

}
