package it.algos.vaadflow14.backend.wrapper;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.service.*;
import org.springframework.stereotype.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 27-nov-2020
 * Time: 14:31
 * <p>
 * Semplice wrapper per veicolare una risposta con diverse property <br>
 */
@Component
public class AResult implements AIResult {

    private boolean valido;

    private String errorMessage = VUOTA;

    private String validationMessage = VUOTA;

    private String text = VUOTA;

    //    /**
    //     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
    //     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
    //     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
    //     */
    //    @Autowired
    //    private ATextService text;

    private int valore = 0;

    private AResult() {
        this(true, VUOTA);
    }

    private AResult(final boolean valido, final String message) {
        this(valido, message, 0);
    }

    private AResult(final boolean valido, final String message, final int valore) {
        this.valido = valido;
        if (valido) {
            this.validationMessage = message;
        }
        else {
            this.errorMessage = message;
        }
        this.valore = valore;
    }

    public static AIResult valido() {
        return new AResult();
    }

    public static AIResult valido(final String validationMessage) {
        return new AResult(true, validationMessage);
    }

    public static AIResult valido(final String validationMessage, final int valore) {
        return new AResult(true, validationMessage, valore);
    }

    public static AIResult contenuto(final String text, final String message) {
        AResult result = new AResult();

        if (text != null && text.length() > 0) {
            result.setValido(true);
            result.setText(text);
            result.setMessage(message);
        }
        else {
            result.setValido(false);
        }

        return result;
    }

    public static AIResult contenuto(final String text) {
        return contenuto(text, VUOTA);
    }

    public static AIResult errato() {
        return new AResult(false, "Non effettuato");
    }

    public static AIResult errato(final int valore) {
        return new AResult(false, VUOTA, valore);
    }

    public static AIResult errato(final String errorMessage) {
        return new AResult(false, errorMessage);
    }

    @Override
    public boolean isValido() {
        return valido;
    }

    public void setValido(final boolean valido) {
        this.valido = valido;
    }

    @Override
    public boolean isErrato() {
        return !valido;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    @Override
    public String getMessage() {
        return isValido() ? getValidationMessage() : getErrorMessage();
    }

    @Override
    public AIResult setValidationMessage(final String message) {
        validationMessage = message;
        return this;
    }

    @Override
    public AIResult setErrorMessage(final String message) {
        errorMessage = message;
        return this;
    }


    @Override
    public AIResult setMessage(final String message) {
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
    public void print(final ALogService logger, final AETypeLog typeLog) {
        if (isValido()) {
            logger.log(typeLog, getValidationMessage());
        }
        else {
            logger.log(typeLog, getErrorMessage());
        }
    }


}
