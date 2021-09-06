package it.algos.vaadflow14.backend.wrapper;

import it.algos.vaadflow14.backend.enumeration.*;
import org.bson.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.*;

import java.io.*;
import java.util.regex.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 19-dic-2019
 * Time: 11:01
 */
public class AFiltro implements Serializable {

    private Criteria criteria;

    private Sort sort;

    private String tag;

    public AFiltro() {
    }

    public AFiltro(Criteria criteria) {
        this.criteria = criteria;
        this.sort = null;
    }


    public AFiltro(Criteria criteria, Sort sort) {
        this.criteria = criteria;
        this.sort = sort;
    }


    public AFiltro(Sort sort) {
        this.sort = sort;
    }

    public static AFiltro start(String fieldName, String value) {
        AFiltro filtro = new AFiltro();

        String questionPattern = "^" + Pattern.quote(value) + ".*";
        Criteria criteria = Criteria.where(fieldName).regex(questionPattern, "i");
        filtro.criteria = criteria;

        return filtro;
    }


    public static AFiltro contains(String fieldName, String value) {
        AFiltro filtro = new AFiltro();

        String questionPattern = ".*" + Pattern.quote(value) + ".*";
        Criteria criteria = Criteria.where(fieldName).regex(questionPattern, "i");
        filtro.criteria = criteria;

        return filtro;
    }


    public static AFiltro ugualeStr(String fieldName, String value) {
        return new AFiltro(AETypeBson.uguale.getCriteria(fieldName, value));
    }


    public static AFiltro ugualeObj(String fieldName, Object value) {
        AFiltro filtro = new AFiltro();

        Criteria criteria = Criteria.where(fieldName).is(value);
        filtro.criteria = criteria;

        return filtro;
    }


    public static AFiltro vero(String fieldName) {
        return booleano(fieldName,true);
    }


    public static AFiltro falso(String fieldName) {
        return booleano(fieldName,false);
    }


    public static AFiltro booleano(String fieldName, boolean value) {
        AFiltro filtro = new AFiltro();

        Criteria criteria = Criteria.where(fieldName).is(value);
        filtro.criteria = criteria;

        return filtro;
    }


    public Criteria getCriteria() {
        return criteria;
    }

    public Sort getSort() {
        return sort;
    }

    //    public BasicDBObject getQuery() {
    //        return query;
    //    }

    public String getTag() {
        return tag;
    }

    public AFiltro getClone() {
        AFiltro deepCopy = new AFiltro();

        Document doc = this.criteria.getCriteriaObject();
        deepCopy.criteria = Criteria.byExample(doc);

        return deepCopy;
    }

}
