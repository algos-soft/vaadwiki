package it.algos.vaadflow14.backend.interfaces;

import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.service.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 27-nov-2020
 * Time: 14:29
 */
public interface AIResult {

    boolean isValido();

    boolean isErrato();

    String getText();

    void setText(final String text);

    String getMessage();

    AIResult setMessage(final String message);

    String getErrorMessage();

    AIResult setErrorMessage(final String message);

    String getValidationMessage();

    AIResult setValidationMessage(final String message);

    int getValore();

    void setValore(int value);

    void print(final  ALogService logger, final AETypeLog typeLog);

}// end of interface

