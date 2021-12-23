package it.algos.vaadflow14.ui.form;

import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.ui.fields.*;

import java.util.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: mer, 10-giu-2020
 * Time: 21:15
 * Wrap di informazioni passate dalla Logic alla creazione del Form <br>
 * La Logic mantiene lo stato ed elabora informazioni che verranno usate dal Form <br>
 */
public class WrapForm {


    /**
     * Tipologia di Form in uso <br>
     */
    private AEOperation operationForm;

    private AEntity entityBean = null;

    private Class<? extends AEntity> entityClazz = null;

    private boolean usaTopLayout = true;

    private int stepTopLayout = 1;

    private boolean usaBottomLayout = false;

    private int stepBottomLayout = 1;

    private List<String> fieldsName = null;

    private HashMap<String, AField> fieldsMap;

    private LinkedHashMap<String, List> enumMap;

    private String minWidthForm = "50em";



    public WrapForm() {
    }


    public WrapForm(AEntity entityBean) {
        this.entityBean = entityBean;
    }


    public WrapForm(AEntity entityBean, AEOperation operationForm) {
        this.entityBean = entityBean;
        this.operationForm = operationForm;
    }

    public WrapForm(AEntity entityBean, AEOperation operationForm,List<String> fieldsName) {
        this.entityBean = entityBean;
        this.operationForm = operationForm;
        this.fieldsName = fieldsName;
    }


    public WrapForm(AEntity entityBean, Class<? extends AEntity> entityClazz, boolean usaTopLayout, int stepTopLayout, boolean usaBottomLayout, int stepBottomLayout, List<String> fieldsName, HashMap<String, AField> fieldsMap, String minWidthForm) {
        this.entityBean = entityBean;
        this.entityClazz = entityClazz;
        this.usaTopLayout = usaTopLayout;
        this.stepTopLayout = stepTopLayout;
        this.usaBottomLayout = usaBottomLayout;
        this.stepBottomLayout = stepBottomLayout;
        this.fieldsName = fieldsName;
        this.fieldsMap = fieldsMap;
        this.minWidthForm = minWidthForm;
    }


    public AEntity getEntityBean() {
        return entityBean;
    }


    public Class<? extends AEntity> getEntityClazz() {
        return entityClazz;
    }


    public boolean isUsaTopLayout() {
        return usaTopLayout;
    }


    public int getStepTopLayout() {
        return stepTopLayout;
    }


    public boolean isUsaBottomLayout() {
        return usaBottomLayout;
    }


    public int getStepBottomLayout() {
        return stepBottomLayout;
    }


    public String getMinWidthForm() {
        return minWidthForm;
    }


    public List<String> getFieldsName() {
        return fieldsName;
    }


    public HashMap<String, AField> getFieldsMap() {
        return fieldsMap;
    }


    public LinkedHashMap<String, List> getEnumMap() {
        return enumMap;
    }


    public void setEnumMap(LinkedHashMap<String, List> enumMap) {
        this.enumMap = enumMap;
    }


    public AEOperation getOperationForm() {
        return operationForm;
    }


    public void setUsaBottomLayout(boolean usaBottomLayout) {
        this.usaBottomLayout = usaBottomLayout;
    }

}
