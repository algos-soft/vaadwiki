package it.algos.vaadflow14.backend.exceptions;

import it.algos.vaadflow14.backend.entity.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mer, 25-ago-2021
 * Time: 20:00
 */
public class AlgosException extends Exception {

    private AEntity entityBean;

    private String stack;

    public AlgosException(final String message) {
        super(message);
    }

    public AlgosException(final Throwable cause) {
        super(cause);
    }

    public AlgosException(final Throwable cause, final AEntity entityBean) {
        super(cause);
        this.entityBean = entityBean;
    }

    public AlgosException(final Throwable cause, final AEntity entityBean, final String message) {
        super(message, cause);
        this.entityBean = entityBean;
    }

    public AlgosException(final Throwable cause, final AEntity entityBean, final String message, final String stack) {
        super(message, cause);
        this.entityBean = entityBean;
        this.stack = stack;
    }

    public static AlgosException message(final String message) {
        return new AlgosException(message);
    }

    public static AlgosException cause(final Throwable cause) {
        return new AlgosException(cause);
    }

    public static AlgosException crea(final Throwable cause, final String message) {
        return (AlgosException) new Exception(message, cause);
    }

    public static AlgosException stack(final Throwable cause, final String stack) {
        AlgosException algosException = new AlgosException(cause);
        algosException.stack = stack;

        return algosException;
    }
    public static AlgosException stack(final String message, final String stack) {
        AlgosException algosException = new AlgosException(message);
        algosException.stack = stack;

        return algosException;
    }

    public AEntity getEntityBean() {
        return entityBean;
    }

    public String getStack() {
        return stack;
    }

}
