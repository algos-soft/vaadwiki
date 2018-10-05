package it.algos.vaadflow.service;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIField;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.converter.AConverterPrefByte;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadflow.ui.fields.*;
import it.algos.vaadflow.validator.AIntegerZeroValidator;
import it.algos.vaadflow.validator.AStringNullValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 11-mag-2018
 * Time: 17:43
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con:
 * 1) StaticContextAccessor.getBean(AFieldService.class);
 * 2) AFieldService.getInstance();
 * 3) @Autowired private AFieldService fieldService;
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AFieldService {

    /**
     * Private final property
     */
    private static final AFieldService INSTANCE = new AFieldService();
    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AAnnotationService annotation = AAnnotationService.getInstance();
    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AReflectionService reflection = AReflectionService.getInstance();
    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();

    @Autowired
    private AConverterPrefByte prefConverter;


    /**
     * Private constructor to avoid client applications to use constructor
     */
    private AFieldService() {
    }// end of constructor

    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static AFieldService getInstance() {
        return INSTANCE;
    }// end of static method


    /**
     * Create a single field.
     * The field type is chosen according to the annotation @AIField.
     *
     * @param binder       collegamento tra i fields e la entityBean
     * @param binderClass  della Entity di riferimento
     * @param propertyName della property
     */
    public AbstractField create(Binder binder, Class binderClass, String propertyName) {
        Field reflectionJavaField = reflection.getField(binderClass, propertyName);

        if (reflectionJavaField != null) {
            return create(binder, reflectionJavaField);
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    /**
     * Create a single field.
     * The field type is chosen according to the annotation @AIField.
     *
     * @param binder              collegamento tra i fields e la entityBean
     * @param reflectionJavaField di riferimento per estrarre le Annotation
     */
    public AbstractField create(Binder binder, Field reflectionJavaField) {
        AbstractField field = null;
        String fieldName = reflectionJavaField.getName();
//        int minDefault = 3;
        Class clazz = null;
        EAFieldType type = annotation.getFormType(reflectionJavaField);
        boolean notNull;
        String stringMessage = "Code must contain at least 3 printable characters";
        String intMessage = " deve contenere solo caratteri numerici";
        String messageNotNull = "";
        String mess = "";
        StringLengthValidator stringValidator = null;
        StringToIntegerConverter integerConverter = null;
        String message;
        String messageSize;
        int min = 0;

        clazz = annotation.getClazz(reflectionJavaField);
        notNull = annotation.isNotNull(reflectionJavaField);
        message = annotation.getMessage(reflectionJavaField);
        messageSize = annotation.getMessageSize(reflectionJavaField);
        messageNotNull = annotation.getMessageNull(reflectionJavaField);
        min = annotation.getSizeMin(reflectionJavaField);
        type = annotation.getFormType(reflectionJavaField);
        String caption = annotation.getFormFieldNameCapital(reflectionJavaField);
        AIField fieldAnnotation = annotation.getAIField(reflectionJavaField);
        String width = annotation.getWidthEM(reflectionJavaField);
        boolean required = annotation.isRequired(reflectionJavaField);
        boolean focus = annotation.isFocus(reflectionJavaField);
//        boolean enabled = annotation.isFieldEnabled(reflectedJavaField, nuovaEntity);
        Class targetClazz = annotation.getComboClass(reflectionJavaField);
        AStringNullValidator nullValidator = new AStringNullValidator(messageNotNull);
        AIntegerZeroValidator zeroValidator = new AIntegerZeroValidator();

        if (type == null) {
            field = new ATextField(caption.equals("") ? "noname" : caption);
            field.setReadOnly(false);
            binder.forField(field).bind(fieldName);
            return field;
        }// end of if cycle

        switch (type) {
            case text:
                field = new ATextField(caption);
                if (notNull) {
                    if (min > 0) {
                        stringValidator = new StringLengthValidator(messageSize, min, null);
                        binder
                                .forField(field)
                                .withValidator(nullValidator)
                                .withValidator(stringValidator)
                                .bind(fieldName);
                    } else {
                        binder
                                .forField(field)
                                .withValidator(nullValidator)
                                .bind(fieldName);
                    }// end of if/else cycle
                } else {
                    if (min > 0) {
                        stringValidator = new StringLengthValidator(messageSize, min, null);
                        binder.forField(field).withValidator(stringValidator).bind(fieldName);
                    } else {
                        binder.forField(field).bind(fieldName);
                    }// end of if/else cycle
                }// end of if/else cycle

                if (focus) {
                    ((ATextField) field).focus();
                }// end of if cycle
                break;
            case email:
                field = new ATextField(caption);
                binder.forField(field).bind(fieldName);
                break;
            case textarea:
                field = new ATextArea(caption);
                binder.forField(field).bind(fieldName);
                field.setReadOnly(false);
                break;
            case integer:
                mess = fieldName + intMessage;
                message = text.isValid(message) ? message : mess;
                integerConverter = new StringToIntegerConverter(0, message);
                field = new AIntegerField(caption);
                binder.forField(field)
                        .withConverter(integerConverter)
                        .withValidator(zeroValidator)
                        .bind(fieldName);

                if (focus) {
                    ((AIntegerField) field).focus();
                }// end of if cycle
                break;
            case combo:
                field = new AComboBox(caption);
                if (clazz != null) {
                    IAService service = (IAService) StaticContextAccessor.getBean(clazz);
                    List items = ((IAService) service).findAll();
                    if (items != null) {
                        ((AComboBox) field).setItems(items);
                    }// end of if cycle
                }// end of if cycle
                field.setReadOnly(false);
                binder.forField(field).bind(fieldName);
                break;
            case enumeration:
                field = new AComboBox(caption);
                if (clazz != null) {
                    Object[] items = clazz.getEnumConstants();
                    if (items != null) {
                        ((AComboBox) field).setItems(items);
                    }// end of if cycle
                }// end of if cycle
                binder.forField(field).bind(fieldName);
                break;
            case checkbox:
                field = new Checkbox(caption);
                binder.forField(field).bind(fieldName);
                break;
            case localdate:
                field = new ADatePicker(caption);
                binder.forField(field).bind(fieldName);
                break;
            case localdatetime:
                //@todo andrà inserito quando ci sarà un DatePicker che accetti i LocalDateTime
//                field = new ADatePicker(caption);
//                field = new ATextField(caption);
//                binder.forField(field).bind(fieldName);
                break;
            case link:
                field = new ATextField(caption);
                break;
            case pref:
//                field = new ATextField(caption);
//                binder.forField(field).withConverter(prefConverter).bind(fieldName);
                break;
            case noBinder:
                field = new ATextField(caption);
                break;
            default:
                field = new ATextField(caption);
                break;
        } // end of switch statement

        return field;
    }// end of method

}// end of class
