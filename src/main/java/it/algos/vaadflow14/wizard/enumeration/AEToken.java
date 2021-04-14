package it.algos.vaadflow14.wizard.enumeration;


import static it.algos.vaadflow14.backend.application.FlowCost.*;
import static it.algos.vaadflow14.wizard.scripts.WizCost.*;

import java.util.*;


/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mar, 06-mar-2018
 * Time: 19:46
 */
public enum AEToken {

    nameTargetProject("PROJECT", true),
    nameTargetProjectLower("PROJECTLOWER", true),
    pathTargetProject(VUOTA, true),
    projectNameUpper("PROJECTALLUPPER", true),
    moduleNameMinuscolo("MODULELOWER", true),
    moduleNameMaiuscolo("MODULEUPPER", true),
    first("FIRSTCHARPROJECT", true),
    //    pathVaadFlowWizTxtSources(VUOTA, true),
    packageNamePunti("PACKAGEPUNTI", true),
    packageNameSlash("PACKAGESLASH", true),
    packageNameLower("PACKAGENAMELOWER", true),
    packageNameUpper("PACKAGENAMEUPPER", true),
    projectCost("COST"),
    user("USER"),
    today("TODAY"),
    todayAnno("TDAYANNO"),
    todayMese("TODAYMESE"),
    todayGiorno("TODAYGIORNO"),
    time("TIME"),
    qualifier("QUALIFIER"),
    tagView("TAG_VIEW"),
    entityLower("ENTITYLOWER"),
    entityUpper("ENTITYUPPER"),
    estendeEntity("ESTENDEENTITY"),
    superClassEntity("SUPERCLASSENTITY"),
    parametersFind("PARAMETERSFIND"),
    parameters("PARAMETERS"),
    parametersDoc("PARAMETERSDOC"),
    parametersNewEntity("PARAMETERSNEWENTITY"),
    methodFind("FIND"),
    methodNewOrdine("NEWORDINE"),
    creaIfNotExist("CREAIFNOTEXIST"),
    newEntityKeyUnica("NEWENTITYKEYUNICA"),
    builder("BUILDER"),
    query("QUERY"),
    findAll("FINDALL"),
    keyProperty("KEYPROPERTY"),
    searchProperty("SEARCHPROPERTY"),
    sortProperty("SORTPROPERTY"),
    rowIndex("ROWINDEX"),
    properties("PROPERTIES"),
    propertyOrdineName("ORDINENAME"),
    propertyOrdine("ORDINE"),
    propertyCodeName("CODENAME"),
    propertyCode("CODE"),
    propertyDescrizioneName("DESCRIZIONENAME"),
    propertyDescrizione("DESCRIZIONE"),
    propertyValidoName("VALIDONAME"),
    propertyValido("VALIDO"),
    propertiesRinvio("PROPERTIESRINVIO"),
    propertiesDoc("PROPERTIESDOC"),
    propertiesParams("PROPERTIESPARAMS"),
    propertiesBuild("PROPERTIESBUILD"),
    codeDoc("CODEDOC"),
    codeParams("CODEPARAMS"),
    codeRinvio("CODERINVIO"),
    toString("TOSTRING"),
    usaCompany("USACOMPANY"),
    usaSecurity("USASECURITY"),
    readCompany("READCOMPANY"),
    grid("GRID"),
    creaGrid("CREAGRID"),
    postConstruct("POSTCONSTRUCT"),
    setParameter("SETPARAMETER"),
    beforeEnter("BEFOREENTER"),
    fixPreferenze("FIXPREFERENZE"),
    fixLayout("FIXLAYOUT"),
    creaAlertLayout("CREAALERTLAYOUT"),
    creaTopLayout("CREATOPLAYOUT"),
    creaPopupFiltro("CREAPOPUPFILTRO"),
    creaFiltri("CREAFILTRI"),
    updateFiltri("UPDATEFILTRI"),
    addListeners("ADDLISTENERS"),
    versionDate("VERSIONDATE"),
    ;

    private static String DELIMITER = "@";

    private String tokenTag;

    private boolean usaValue;

    private String value;


    AEToken(String tokenTag) {
        this(tokenTag, false);
    }


    AEToken(String tokenTag, boolean usaValue) {
        this(tokenTag, usaValue, VUOTA);
    }


    AEToken(String tokenTag, boolean usaValue, String value) {
        this.setTokenTag(tokenTag);
        this.setUsaValue(usaValue);
        this.setValue(value);
    }


    public static void reset() {
        for (AEToken aeToken : AEToken.values()) {
            aeToken.setValue(VUOTA);
        }
    }


    public static String replace(AEToken aeToken, String textReplacing, String value) {
        if (value != null) {
            return textReplacing.replaceAll(DELIMITER + aeToken.tokenTag + DELIMITER, value);
        }
        else {
            return textReplacing;
        }
    }


    public static String replaceAll(String textReplacing, Map<AEToken, String> valueMap) {

        for (AEToken aeToken : values()) {
            if (valueMap.containsKey(aeToken)) {
                textReplacing = replace(aeToken, textReplacing, valueMap.get(aeToken));
                //                textReplacing = textReplacing.replaceAll(DELIMITER + token.tokenTag + DELIMITER, valueMap.get(token));
            }
        }

        return textReplacing;
    }

    /**
     * Visualizzazione di controllo <br>
     */
    public static void printInfo(String posizione) {
        if (FLAG_DEBUG_WIZ) {
            System.out.println("********************");
            System.out.println("AEToken  - " + posizione);
            System.out.println("********************");
            for (AEToken token : AEToken.values()) {
                System.out.println("AEToken." + token.name() + " \"" + token.getTokenTag() + "\" = " + token.getValue());
            }
            System.out.println("");
        }
    }

    public String replace(String textReplacing, String currentTag) {
        return textReplacing.replaceAll(tokenTag, currentTag);
    }

    public String getTokenTag() {
        return tokenTag;
    }

    public void setTokenTag(String tokenTag) {
        this.tokenTag = tokenTag;
    }

    public boolean isUsaValue() {
        return usaValue;
    }

    public void setUsaValue(boolean usaValue) {
        this.usaValue = usaValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
