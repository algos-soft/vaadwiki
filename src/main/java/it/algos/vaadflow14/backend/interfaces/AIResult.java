package it.algos.vaadflow14.backend.interfaces;

import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.service.*;

import java.util.*;

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

    String getPreliminaryResponse();

    void setPreliminaryResponse(final String preliminaryResponse);

    String getResponse();

    void setResponse(final String response);

    String getCodeMessage();

    void setCodeMessage(final String codeMessage);

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

    String getWikiTitle();

    void setWikiTitle(String wikiTitle);

    String getUrlPreliminary();

    void setUrlPreliminary(String urlPreliminary);

    String getUrlRequest();

    void setUrlRequest(String urlRequest);

    String getToken();

    void setToken(final String token);

    String getQueryType();

    void setQueryType(String queryType);

    List getLista();

    void setLista(List lista);

    Map getMappa();

    void setMappa(Map mappa);

}// end of interface

