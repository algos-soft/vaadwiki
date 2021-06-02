package it.algos.vaadflow14.backend.wrapper;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.service.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 27-nov-2020
 * Time: 14:31
 * <p>
 * Semplice wrapper per veicolare una risposta con diverse property <br>
 */
public class AResult implements AIResult {

    private boolean valido;

    private String errorMessage = VUOTA;

    private String validationMessage = VUOTA;

    private int valore = 0;

    private AResult() {
        this(true, VUOTA);
    }

    private AResult(boolean valido, String message) {
        this(valido, message, 0);
    }

    private AResult(boolean valido, String message, int valore) {
        this.valido = valido;
        if (valido) {
            this.validationMessage = message;
        }
        else {
            this.errorMessage = message;
        }
        this.valore = valore;
    }

    public static AResult valido() {
        return new AResult();
    }

    public static AResult valido(String validationMessage) {
        return new AResult(true, validationMessage);
    }

    public static AResult valido(String validationMessage, int valore) {
        return new AResult(true, validationMessage, valore);
    }

    public static AResult errato() {
        return new AResult(false, "Non effettuato");
    }

    public static AResult errato(int valore) {
        return new AResult(false, VUOTA, valore);
    }

    public static AResult errato(String errorMessage) {
        return new AResult(false, errorMessage);
    }

    @Override
    public boolean isValido() {
        return valido;
    }

    @Override
    public boolean isErrato() {
        return !valido;
    }

    @Override
    public String getMessage() {
        return isValido() ? getValidationMessage() : getErrorMessage();
    }

    @Override
    public AIResult setValidationMessage(String message) {
        validationMessage = message;
        return this;
    }

    @Override
    public AIResult setErrorMessage(String message) {
        errorMessage = message;
        return this;
    }

    @Override
    public AIResult setMessage(String message) {
        if (isValido()) {
            return setValidationMessage(message);
        }
        else {
            return setErrorMessage(message);
        }
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String getValidationMessage() {
        return validationMessage;
    }

    @Override
    public int getValore() {
        return valore;
    }

    @Override
    public void print(ALogService logger, AETypeLog typeLog) {
        if (isValido()) {
            logger.log(typeLog, getValidationMessage());
        }
        else {
            logger.log(typeLog, getErrorMessage());
        }
    }

}
