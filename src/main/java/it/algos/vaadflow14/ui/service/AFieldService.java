package it.algos.vaadflow14.ui.service;

import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.validator.*;
import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.exception.*;
import it.algos.vaadflow14.ui.fields.*;
import it.algos.vaadflow14.ui.validator.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 03-ago-2020
 * Time: 15:28
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L' istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AAnnotationService.class); <br>
 * 3) @Autowired public AArrayService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AFieldService extends AbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Create a single field.
     *
     * @param entityBean    di riferimento
     * @param operationForm tipologia di Form in uso
     * @param fieldKey      della property
     */
    public AField crea(AEntity entityBean, Binder binder, AEOperation operationForm, String fieldKey) {
        AField field = null;
        Field reflectionJavaField = null;

        try {
             reflectionJavaField = reflection.getField(entityBean.getClass(), fieldKey);
        } catch (AlgosException unErrore) {
            logger.warn(unErrore, this.getClass(), "crea");
        }

        field = this.creaOnly(entityBean, reflectionJavaField, operationForm);
        if (field != null) {
            this.addToBinder(entityBean, binder, operationForm, reflectionJavaField, field);
        }
        else {
            AETypeField type = annotation.getFormType(reflectionJavaField);
            logger.warn("Non sono riuscito a creare il field " + fieldKey + " di type " + type, this.getClass(), "crea");
        }

        return field;
    }


    /**
     * Create a single field.
     *
     * @param entityBean          di riferimento
     * @param reflectionJavaField di riferimento
     * @param operationForm       tipologia di Form in uso
     */
    public AField creaOnly(AEntity entityBean, Field reflectionJavaField, AEOperation operationForm) {
        AField field = null;
        AETypeField type;
        String caption = VUOTA;
        AETypeBoolField typeBool;
        AETypePref typePref;
        String boolEnum;
        String fieldKey;
        //        Class comboClazz = null;
        //        Sort sort;
        List items;
        List<String> enumItems;
        boolean isRequired = false;
        boolean isEnabled = false;
        boolean isAllowCustomValue = false;
        //        boolean usaComboMethod = false;
        String width = VUOTA;
        String height = VUOTA;
        boolean autofocus = false;

        if (reflectionJavaField == null) {
            return null;
        }

        fieldKey = reflectionJavaField.getName();
        width = annotation.getFormWith(reflectionJavaField);
        autofocus = operationForm == AEOperation.addNew && annotation.isFocus(reflectionJavaField);
        isEnabled = annotation.isEnabled(reflectionJavaField);
        type = annotation.getFormType(reflectionJavaField);
        if (type != null) {
            switch (type) {
                case text:
                case phone:
                    field = appContext.getBean(ATextField.class);
                    break;
                case textArea:
                    field = appContext.getBean(ATextAreaField.class);
                    break;
                case password:
                    field = appContext.getBean(APasswordField.class);
                    break;
                case email:
                    field = appContext.getBean(AEmailField.class);
                    break;
                case cap:
                    field = appContext.getBean(ATextField.class);
                    break;
                case integer:
                    field = appContext.getBean(AIntegerField.class);
                    break;
                case lungo:
                    field = appContext.getBean(ALongField.class);
                    break;
                case doppio:
                    field = appContext.getBean(ADoppioField.class);
                    break;
                case booleano:
                    typeBool = annotation.getTypeBoolField(reflectionJavaField);
                    if (typeBool == AETypeBoolField.checkBox) {
                        caption = annotation.getFormFieldName(reflectionJavaField);
                        field = appContext.getBean(ABooleanField.class, typeBool, caption);
                    }
                    else {
                        boolEnum = annotation.getBoolEnumField(reflectionJavaField);
                        field = appContext.getBean(ABooleanField.class, typeBool, boolEnum);
                    }
                    break;
                case localDateTime:
                    field = appContext.getBean(ADateTimeField.class);
                    break;
                case localDate:
                    //                    field = appContext.getBean(ADateField.class);
                    field = new ADateField();
                    break;
                case localTime:
                    field = appContext.getBean(ATimeField.class);
                    break;
                case combo:
                    ComboBox combo = getCombo(reflectionJavaField);
                    field = appContext.getBean(AComboField.class, combo);
                    break;
                case stringLinkClassCombo:
                    ComboBox linkCombo = getLinkCombo(reflectionJavaField);
                    field = appContext.getBean(AStringLinkClassComboField.class,  reflectionJavaField,linkCombo);
                    break;
                case enumeration:
                    items = getEnumerationItems(reflectionJavaField);
                    if (items != null) {
                        field = appContext.getBean(AComboField.class, items, true, false);
                    }
                    else {
                        logger.warn("Mancano gli items per l' enumeration di " + fieldKey, this.getClass(), "creaOnly.enumeration");
                    }
                    break;
                case preferenza:
                    field = appContext.getBean(APreferenzaField.class, entityBean, operationForm);
                    caption = "nonUsata";
                    break;
                case image:
                    height = annotation.getFormHeight(reflectionJavaField);
                    field = appContext.getBean(AImageField.class);
                    ((AImageField) field).setHeight(height);
                    break;
                case gridShowOnly:
                    field = creaGridField(entityBean, reflectionJavaField);
                    break;
                case mappa:
                    field = appContext.getBean(AMappaField.class);
                    break;
                default:
                    logger.warn("Switch - caso non definito per type=" + type, this.getClass(), "creaOnly");
                    break;
            }
        }

        if (field != null) {
            if (text.isEmpty(caption)) {
                caption = annotation.getFormFieldName(reflectionJavaField);
                if (AEPreferenza.usaFormFieldMaiuscola.is()) {
                    caption = text.primaMaiuscola(caption);
                }
                else {
                    caption = text.primaMinuscola(caption);
                }
                if (text.isEmpty(field.getLabel())) {
                    field.setLabel(caption);
                }
            }
            field.setFieldKey(fieldKey);
            field.setWidth(width);
            if (autofocus) {
                field.setAutofocus();
            }
            field.setEnabled(isEnabled);
        }

        return field;
    }


    public void addToBinder(AEntity entityBean, Binder binder, AEOperation operation, Field reflectionJavaField, AField field) {
        Binder.BindingBuilder builder = null;
        AETypeField fieldType = annotation.getFormType(reflectionJavaField);
        String fieldName = VUOTA;
        AETypeNum numType = AETypeNum.positiviOnly;
        AStringBlankValidator stringBlankValidator = null;
        ANotNullValidator notNullValidator = null;
        StringLengthValidator stringLengthValidator = null;
        AIntegerValidator integerValidator = null;
        AUniqueValidator uniqueValidator = null;
        String message = VUOTA;
        String messageSize = VUOTA;
        String messageNotBlank = VUOTA;
        String messageNotNull = VUOTA;
        String messageMail = "Indirizzo eMail non valido";
        int stringMin = 0;
        int stringMax = 0;
        int intMin = 0;
        int intMax = 0;
        boolean isRequired = false;
        boolean isUnique = false;
        Serializable propertyOldValue = null;

        fieldName = reflectionJavaField.getName();
        message = annotation.getMessage(reflectionJavaField);
        messageSize = annotation.getMessageSize(reflectionJavaField);
        messageNotBlank = annotation.getMessageBlank(reflectionJavaField);
        messageNotNull = annotation.getMessageNull(reflectionJavaField);
        numType = annotation.getTypeNumber(reflectionJavaField);
        stringMin = annotation.getStringMin(reflectionJavaField);
        stringMax = annotation.getStringMax(reflectionJavaField);
        intMin = annotation.getNumberMin(reflectionJavaField);
        intMax = annotation.getNumberMax(reflectionJavaField);
        isRequired = annotation.isRequired(reflectionJavaField);
        isUnique = annotation.isUnique(reflectionJavaField);

        if (isRequired) {
            stringBlankValidator = appContext.getBean(AStringBlankValidator.class, messageNotBlank);
            notNullValidator = appContext.getBean(ANotNullValidator.class, messageNotNull);
        }

        if (stringMin > 0 || stringMax > 0) {
            stringLengthValidator = new StringLengthValidator(messageSize, stringMin, stringMax);
        }
        if (isUnique) {
            try {
                propertyOldValue = (Serializable) reflectionJavaField.get(entityBean);
            } catch (Exception unErrore) {
                logger.error(unErrore, this.getClass(), "addToBinder");
            }
            uniqueValidator = appContext.getBean(AUniqueValidator.class, operation, entityBean, fieldName, propertyOldValue);
        }

        if (fieldType == AETypeField.integer) {
            if (numType == AETypeNum.range || numType == AETypeNum.rangeControl) {
                if (intMin > 0 || intMax > 0) {
                    if (intMin >= intMax) {
                        throw new RangeException("I valori del range sono errati");
                    }
                    else {
                        integerValidator = appContext.getBean(AIntegerValidator.class, numType, intMin, intMax);
                    }
                }
            }
            else {
                integerValidator = appContext.getBean(AIntegerValidator.class, numType);
            }
        }

        if (fieldType != null) {
            builder = binder.forField(field);
            switch (fieldType) {
                case text:
                    if (stringBlankValidator != null) {
                        builder.withValidator(stringBlankValidator);
                    }
                    if (stringLengthValidator != null) {
                        builder.withValidator(stringLengthValidator);
                    }
                    if (uniqueValidator != null) {
                        builder.withValidator(uniqueValidator);
                    }
                    if (isRequired) {
                        builder.asRequired();
                    }
                    break;
                case phone:
                    builder.withValidator(appContext.getBean(APhoneValidator.class));
                    if (isRequired) {
                        builder.asRequired();
                    }
                    break;
                case password:
                    if (stringBlankValidator != null) {
                        builder.withValidator(stringBlankValidator);
                    }
                    if (isRequired) {
                        builder.asRequired();
                    }
                    break;
                case email:
                    if (isRequired) {
                        builder.asRequired();
                    }
                    break;
                case cap:
                    builder.withValidator(appContext.getBean(ACapValidator.class));
                    if (isRequired) {
                        builder.asRequired();
                    }
                    break;
                case textArea:
                    break;
                case integer:
                    if (integerValidator != null) {
                        builder.withValidator(integerValidator);
                    }
                    if (uniqueValidator != null) {
                        builder.withValidator(uniqueValidator);
                    }
                    break;
                case lungo:
                    break;
                case doppio:
                    break;
                case booleano:
                    break;
                case localDateTime:
                    break;
                case localDate:
                    break;
                case localTime:
                    break;
                case combo:
                    if (notNullValidator != null) {
                        builder.withValidator(notNullValidator);
                    }
                    if (isRequired) {
                        builder.asRequired();
                        //                        builder.withValidator(notNullValidator);
                    }
                    break;
                case stringLinkClassCombo:
                    if (notNullValidator != null) {
                        builder.withValidator(notNullValidator);
                    }
                    if (isRequired) {
                        builder.asRequired();
                    }
                    break;
                case enumeration:
                    if (notNullValidator != null) {
                        builder.withValidator(notNullValidator);
                    }
                    if (isRequired) {
                        builder.asRequired();
                    }
                    break;
                case preferenza:
                    if (isRequired) {
                        builder.asRequired();
                    }
                    break;
                case image:
                    break;
                case gridShowOnly:
                    break;
                case mappa:
                    break;
                default:
                    logger.warn("Switch - caso non definito per il field \"" + reflectionJavaField.getName() + "\" del tipo " + fieldType, this.getClass(), "addToBinder");
                    break;
            }
            if (builder != null) {
                builder.bind(fieldName);
            }
        }
    }


    /**
     *
     */
    public ComboBox getCombo(Field reflectionJavaField) {
        ComboBox combo = new ComboBox();
        boolean usaComboMethod;
        Class<AEntity> comboClazz;
        Class<AService> logicClazz;
        String methodName;
        Method metodo;
        AService logicInstance;
        List items = null;

        usaComboMethod = annotation.usaComboMethod(reflectionJavaField);
        comboClazz = annotation.getComboClass(reflectionJavaField);
        if (usaComboMethod) {
            logicClazz = annotation.getLogicClass(reflectionJavaField);
            logicInstance = (AService) StaticContextAccessor.getBean(logicClazz);
            methodName = annotation.getMethodName(reflectionJavaField);
            try {
                metodo = logicClazz.getDeclaredMethod(methodName);
                combo = (ComboBox) metodo.invoke(logicInstance);
            } catch (Exception unErrore) {
                logger.error(unErrore, this.getClass(), "getCombo");
            }
        }
        else {
            items = ((MongoService) mongo).findAll(comboClazz);//@todo da controllare
            combo = new ComboBox();
            if (array.isEmpty(items)) {
                items = new ArrayList();
                items.add("test");
            }
            combo.setItems(items);
        }

        return combo;
    }

    /**
     *
     */
    public ComboBox getLinkCombo(Field reflectionJavaField) {
        ComboBox combo = new ComboBox();
        boolean usaComboMethod;
        Class<AEntity> comboClazz;
        Class<AService> logicClazz;
        String methodName;
        Method metodo;
        AService logicInstance;
        List items = null;

        usaComboMethod = annotation.usaComboMethod(reflectionJavaField);
        comboClazz = annotation.getComboClass(reflectionJavaField);
        if (usaComboMethod) {
            logicClazz = annotation.getLogicClass(reflectionJavaField);
            logicInstance = (AService) StaticContextAccessor.getBean(logicClazz);
            methodName = annotation.getMethodName(reflectionJavaField);
            try {
                metodo = logicClazz.getDeclaredMethod(methodName);
                combo = (ComboBox) metodo.invoke(logicInstance);
            } catch (Exception unErrore) {
                logger.error(unErrore, this.getClass(), "getCombo");
            }
        }
        else {
            items = ((MongoService) mongo).findAll(comboClazz);//@todo da controllare
            combo = new ComboBox();
            if (array.isEmpty(items)) {
                items = new ArrayList();
                items.add("test");
            }
            combo.setItems(items);
        }

        return combo;
    }

    /**
     * Prima cerca i valori nella @Annotation items=... dell' interfaccia AIField <br>
     * Poi cerca i valori di una classe enumeration definita in enumClazz=... dell' interfaccia AIField <br>
     * Poi cerca i valori di una collection definita con serviceClazz=...dell' interfaccia AIField <br>
     */
    public List getEnumerationItems(Field reflectionJavaField) {
        List items = null;
        Class enumClazz = null;
        List<String> enumItems;
        Object[] elementiEnum = null;

        enumItems = annotation.getEnumItems(reflectionJavaField);
        if (array.isAllValid(enumItems)) {
            return enumItems;
        }

        enumClazz = annotation.getEnumClass(reflectionJavaField);
        if (enumClazz != null) {
            elementiEnum = enumClazz.getEnumConstants();
            if (elementiEnum != null) {
                return Arrays.asList(elementiEnum);
            }
        }

        return items;
    }


    /**
     *
     */
    public AGridField creaGridField(AEntity entityBean, Field reflectionJavaField) {
        AGridField field = null;
        List items;
        Class linkClazz;
        List<String> gridProperties;
        String linkProperty;
        String caption;

        if (entityBean == null || reflectionJavaField == null) {
            return null;
        }

        linkClazz = annotation.getLinkClass(reflectionJavaField);
        linkProperty = annotation.getLinkProperty(reflectionJavaField);
        items = ((MongoService) mongo).findAll(linkClazz, linkProperty, entityBean);//@todo da controllare
        gridProperties = annotation.getLinkProperties(reflectionJavaField);
        caption = annotation.getFormFieldName(reflectionJavaField);
        caption = AEPreferenza.usaFormFieldMaiuscola.is() ? text.primaMaiuscola(caption) : caption;

        field = appContext.getBean(AGridField.class, linkClazz, gridProperties, items);
        field.setLabel(caption + DUE_PUNTI_SPAZIO + items.size());
        return field;
    }

}