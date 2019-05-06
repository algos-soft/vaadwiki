package it.algos.vaadflow.service;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import it.algos.vaadflow.annotation.AIField;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadflow.ui.fields.*;
import it.algos.vaadflow.validator.AIntegerZeroValidator;
import it.algos.vaadflow.validator.ALongZeroValidator;
import it.algos.vaadflow.validator.AStringNullValidator;
import it.algos.vaadflow.validator.AUniqueValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 11-mag-2018
 * Time: 17:43
 * <p>
 * Gestisce la creazione dei campi nel Form nel tipo adeguato <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce <br>
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AFieldService.class); <br>
 * 2) AFieldService.getInstance(); <br>
 * 3) @Autowired private AFieldService fieldService; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, basta il 'pattern') <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * <p>
 */
@Service
@Slf4j
public class AFieldService extends AbstractService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Private final property
     */
    private static final AFieldService INSTANCE = new AFieldService();


//    @Autowired
//    private AConverterPrefByte prefConverter;


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
    public AbstractField create(ApplicationContext appContext, Binder binder, Class binderClass, String propertyName) {
        Field reflectionJavaField = reflection.getField(binderClass, propertyName);

        if (reflectionJavaField != null) {
            return create(appContext, binder, reflectionJavaField);
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
    public AbstractField create(ApplicationContext appContext, Binder binder, Field reflectionJavaField) {
        AbstractField field = null;
        String fieldName = reflectionJavaField.getName();
//        int minDefault = 3;
        Class clazz = null;
        EAFieldType type = annotation.getFormType(reflectionJavaField);
        boolean notNull;
        boolean unique;
        String stringMessage = "Code must contain at least 3 printable characters";
        String intMessage = " deve contenere solo caratteri numerici";
        String messageNotNull = "";
        String mess = "";
        StringLengthValidator lengthValidator = null;
        StringToIntegerConverter integerConverter = null;
        StringToLongConverter longConverter = null;
        String message;
        String messageSize;
        int min = 0;

        clazz = annotation.getClazz(reflectionJavaField);
        notNull = annotation.isNotNull(reflectionJavaField);
        unique = annotation.isUnique(reflectionJavaField);
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
        AIntegerZeroValidator integerZeroValidator = new AIntegerZeroValidator();
        ALongZeroValidator longZeroValidator = new ALongZeroValidator();
        AUniqueValidator uniqueValidator = null;
        String color = annotation.getFieldColor(reflectionJavaField);

        if (type == null) {
            field = new ATextField(caption.equals("") ? "noname" : caption);
            field.setReadOnly(false);
            if (binder != null) {
                binder.forField(field).bind(fieldName);
            }// end of if cycle
            return field;
        }// end of if cycle

        lengthValidator = min > 0 ? new StringLengthValidator(messageSize, min, null) : null;
//        uniqueValidator = unique ? appContext.getBean(AUniqueValidator.class, reflectionJavaField.getDeclaringClass(), fieldName, caption) : null;

        switch (type) {
            case text:
                field = new ATextField(caption);
                if (binder != null) {
                    if (notNull) {
                        if (lengthValidator != null) {
                            if (uniqueValidator != null) {
                                binder
                                        .forField(field)
                                        .withValidator(nullValidator)
                                        .withValidator(lengthValidator)
//                                        .withValidator(uniqueValidator)
                                        .bind(fieldName);
                            } else {
                                binder
                                        .forField(field)
                                        .withValidator(nullValidator)
                                        .withValidator(lengthValidator)
                                        .bind(fieldName);
                            }// end of if/else cycle
                        } else {
                            if (uniqueValidator != null) {
                                binder
                                        .forField(field)
                                        .withValidator(nullValidator)
//                                        .withValidator(uniqueValidator)
                                        .bind(fieldName);
                            } else {
                                binder
                                        .forField(field)
                                        .withValidator(nullValidator)
                                        .bind(fieldName);
                            }// end of if/else cycle
                        }// end of if/else cycle
                    } else {
                        if (lengthValidator != null) {
                            if (uniqueValidator != null) {
                                binder
                                        .forField(field)
                                        .withValidator(lengthValidator)
//                                        .withValidator(uniqueValidator)
                                        .bind(fieldName);
                            } else {
                                binder
                                        .forField(field)
                                        .withValidator(lengthValidator)
                                        .bind(fieldName);
                            }// end of if/else cycle
                        } else {
                            if (uniqueValidator != null) {
                                binder
                                        .forField(field)
                                        .withValidator(lengthValidator)
                                        .bind(fieldName);
                            } else {
                                binder
                                        .forField(field)
                                        .bind(fieldName);
                            }// end of if/else cycle
                        }// end of if/else cycle
                    }// end of if/else cycle
                }// end of if cycle
                if (focus) {
                    ((ATextField) field).focus();
                }// end of if cycle
                break;
            case email:
                field = new ATextField(caption);
                if (binder != null) {
                    binder.forField(field).bind(fieldName);
                }// end of if cycle
                break;
            case textarea:
                field = new ATextArea(caption);
                if (binder != null) {
                    binder.forField(field).bind(fieldName);
                }// end of if cycle
                field.setReadOnly(false);
                break;
            case integer:
                mess = fieldName + intMessage;
                message = text.isValid(message) ? message : mess;
                integerConverter = new StringToIntegerConverter(0, message);
                field = new AIntegerField(caption);
                if (binder != null) {
                    binder.forField(field)
                            .withConverter(integerConverter)
                            .withValidator(integerZeroValidator)
                            .bind(fieldName);
                }// end of if cycle
                if (focus) {
                    ((AIntegerField) field).focus();
                }// end of if cycle
                break;
            case lungo:
                mess = fieldName + intMessage;
                message = text.isValid(message) ? message : mess;
                longConverter = new StringToLongConverter(0L, message);
                field = new AIntegerField(caption);
                if (binder != null) {
                    binder.forField(field)
                            .withConverter(longConverter)
                            .withValidator(longZeroValidator)
                            .bind(fieldName);
                }// end of if cycle
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
                if (binder != null) {
                    binder.forField(field).bind(fieldName);
                }// end of if cycle
                break;
            case enumeration:
                field = new AComboBox(caption);
                if (clazz != null) {
                    Object[] items = clazz.getEnumConstants();
                    if (items != null) {
                        ((AComboBox) field).setItems(items);
                    }// end of if cycle
                }// end of if cycle
                if (binder != null) {
                    binder.forField(field).bind(fieldName);
                }// end of if cycle
                break;
            case checkbox:
                field = new Checkbox(caption);
                if (binder != null) {
                    binder.forField(field).bind(fieldName);
                }// end of if cycle
                break;
            case localdate:
                field = new ADatePicker(caption);
                if (binder != null) {
                    binder.forField(field).bind(fieldName);
                }// end of if cycle
                break;
            case localdatetime:
                //@todo andrà inserito quando ci sarà un DatePicker che accetti i LocalDateTime
//                field = new ADatePicker(caption);
//                field = new ATextField(caption);
//                binder.forField(field).bind(fieldName);
                break;
            case vaadinIcon:
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
