package it.algos.vaadflow14.backend.exceptions;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 15-ago-2021
 * Time: 20:50
 */
public class AQueryException extends Exception {

    private Class entityClazz;

    public AQueryException(String message) {
        super(message);
    }

    public AQueryException(Throwable cause, Class entityClazz) {
        super(cause);
        this.entityClazz = entityClazz;
    }

    public AQueryException(Throwable cause, Class entityClazz, String message) {
        super(message, cause);
        this.entityClazz = entityClazz;
    }

    public Class getEntityClazz() {
        return entityClazz;
    }

}
