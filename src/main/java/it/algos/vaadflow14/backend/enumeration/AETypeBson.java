package it.algos.vaadflow14.backend.enumeration;

import org.springframework.data.mongodb.core.query.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mer, 28-apr-2021
 * Time: 21:06
 */
public enum AETypeBson {
    uguale("$eq", "Matches values that are equal to a specified value."){
       @Override
        public Criteria getCriteria(final String fieldName, final String value) {
            return Criteria.where(fieldName).is(value);
        }
    },
    maggiore("$gt", "Matches values that are greater than a specified value."){
        @Override
        public Criteria getCriteria(final String fieldName, final String value) {
            return Criteria.where(fieldName).gt(value);
        }
    },
    maggioreUguale("$gte", "Matches values that are greater than or equal to a specified value."){
        @Override
        public Criteria getCriteria(final String fieldName, final String value) {
            return Criteria.where(fieldName).gte(value);
        }
    },
    regex("$regex", "Selects documents where values match a specified regular expression."){
        @Override
        public Criteria getCriteria(final String fieldName, final String value) {
            return Criteria.where(fieldName).regex(value);
        }
    },
    ;

    private String tag;

    private String descrizione;


    AETypeBson(String tag, String descrizione) {
        this.tag = tag;
        this.descrizione = descrizione;
    }

    public Criteria getCriteria(final String fieldName, final String value) {
        return null;
    }
}
