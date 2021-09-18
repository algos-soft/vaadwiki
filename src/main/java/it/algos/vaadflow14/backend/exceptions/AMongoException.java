package it.algos.vaadflow14.backend.exceptions;

import it.algos.vaadflow14.backend.entity.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 09-ago-2021
 * Time: 14:12
 */
public class AMongoException extends Exception {

    private AEntity entityBean;

    public AMongoException(String message) {
        super(message);
    }

    public AMongoException(Throwable cause) {
        super(cause);
    }

    public AMongoException(Throwable cause, AEntity entityBean) {
        super(cause);
        this.entityBean = entityBean;
    }

    public AMongoException(Throwable cause, String message) {
        super(message, cause);
    }

    public AMongoException(Throwable cause, AEntity entityBean, String message) {
        super(message, cause);
        this.entityBean = entityBean;
    }

    public AEntity getEntityBean() {
        return entityBean;
    }

}
