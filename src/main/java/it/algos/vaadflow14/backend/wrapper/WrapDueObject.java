package it.algos.vaadflow14.backend.wrapper;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: sab, 27-giu-2020
 * Time: 19:07
 */
public class WrapDueObject {
    private Object primo;

    private Object secondo;


    public WrapDueObject(Object primo, Object secondo) {
        this.primo = primo;
        this.secondo = secondo;
    }


    public Object getPrimo() {
        return primo;
    }


    public Object getSecondo() {
        return secondo;
    }

}
