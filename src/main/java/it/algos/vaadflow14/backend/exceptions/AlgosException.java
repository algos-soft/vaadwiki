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

    private Class clazz;

    private String method;

    public AlgosException(final String message) {
        super(message);
    }

    public AlgosException(final Throwable cause) {
        super(cause);
    }

    public AlgosException(final Throwable cause, final String message) {
        super(message, cause);
    }

    public AlgosException(final Throwable cause, final AEntity entityBean) {
        super(cause);
        this.entityBean = entityBean;
    }

    public AlgosException(final Throwable cause, final AEntity entityBean, final String message) {
        super(message, cause);
        this.entityBean = entityBean;
    }

    public AlgosException(final Throwable cause, final AEntity entityBean, final String message,final Class clazz, final String method) {
        super(message, cause);
        this.entityBean = entityBean;
        this.clazz = clazz;
        this.method = method;
    }

    public static AlgosException cause(final Throwable cause) {
        return new AlgosException(cause);
    }


    public static AlgosException message(final String message) {
        return new AlgosException(message);
    }


    public static AlgosException crea(final Throwable cause, final String message) {
        return (AlgosException) new Exception(message, cause);
    }

    public static AlgosException stack(final Throwable cause, final Class clazz, final String method) {
        AlgosException algosException = new AlgosException(cause);
        algosException.clazz = clazz;
        algosException.method = method;

        return algosException;
    }

    public static AlgosException stack(final String message, final Class clazz, final String method) {
        AlgosException algosException = new AlgosException(message);
        algosException.clazz = clazz;
        algosException.method = method;

        return algosException;
    }

    public static AlgosException stack(final Throwable cause, final String message, final Class clazz, final String method) {
        AlgosException algosException = new AlgosException(cause, message);
        algosException.clazz = clazz;
        algosException.method = method;

        return algosException;
    }

    public static AlgosException stack(final Throwable cause, final AEntity entityBean, final String message,final Class clazz, final String method) {
        AlgosException algosException = new AlgosException(cause, message);
        algosException.entityBean = entityBean;
        algosException.clazz = clazz;
        algosException.method = method;

        return algosException;
    }

    public AEntity getEntityBean() {
        return entityBean;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getMethod() {
        return method;
    }

}
