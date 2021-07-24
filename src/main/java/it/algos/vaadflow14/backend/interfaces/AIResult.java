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

    void setValido(final boolean valido);

    boolean isErrato();

    String getResponse();

    void setResponse(final String response);

    String getMessage();

    void setMessage(final String message);

    String getErrorMessage();

    AIResult setErrorMessage(final String message);

    String getErrorCode();

    void setErrorCode(String errorCode);

    String getValidMessage();

    void setValidMessage(String validMessage);

    int getValue();

    void setValue(int value);

    void print(final ALogService logger, final AETypeLog typeLog);

    String getUrl();

    void setUrl(String url);

    String getWikiTitle();

    void setWikiTitle(String wikiTitle);

}// end of interface

