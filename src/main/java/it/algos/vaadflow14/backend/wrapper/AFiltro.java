package it.algos.vaadflow14.backend.wrapper;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 19-dic-2019
 * Time: 11:01
 */
public class AFiltro {

    private CriteriaDefinition criteria;

    private Sort sort;


    public AFiltro(CriteriaDefinition criteria) {
        this.criteria = criteria;
        this.sort = null;
    }


    public AFiltro(CriteriaDefinition criteria, Sort sort) {
        this.criteria = criteria;
        this.sort = sort;
    }


    public AFiltro(Sort sort) {
        this.sort = sort;
    }


    public CriteriaDefinition getCriteria() {
        return criteria;
    }


    public Sort getSort() {
        return sort;
    }

}
