package it.algos.vaadflow14.ui.validator;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.entity.AEntity;
import it.algos.vaadflow14.backend.enumeration.AEOperation;
import it.algos.vaadflow14.backend.service.ALogService;
import it.algos.vaadflow14.backend.service.AMongoService;
import it.algos.vaadflow14.backend.service.ATextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;

import static it.algos.vaadflow14.backend.application.FlowCost.VUOTA;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 06-ago-2020
 * Time: 15:39
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AUniqueValidator implements Validator {

    private static final long serialVersionUID = 1L;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ALogService logger;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AMongoService mongo;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ATextService text;


    private AEOperation operationForm;

    private AEntity entityBean;

    private String propertyName = VUOTA;

    private Serializable propertyOldValue = null;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public AUniqueValidator() {
    } // end of SpringBoot constructor


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(AUniqueValidator.class, operation) <br>
     *
     * @param operationForm    per differenziare tra nuova entity e modifica di esistente
     * @param entityBean       the single entity
     * @param propertyName     per costruire la query
     * @param propertyOldValue (serializable) esistente nella entity
     */
    public AUniqueValidator(AEOperation operationForm, AEntity entityBean, String propertyName, Serializable propertyOldValue) {
        this.operationForm = operationForm;
        this.entityBean = entityBean;
        this.propertyName = propertyName;
        this.propertyOldValue = propertyOldValue;
    } // end of SpringBoot constructor


    /**
     * Prima passa da StringToIntegerConverter ed arriva sempre un integer
     * Se il field è vuoto, arriva un integer uguale a zero
     * Non viene accettato
     *
     * @return the function result
     */
    @Override
    public ValidationResult apply(Object obj, ValueContext valueContext) {
        AEntity entity = null;
        Serializable propertyNewValue = null;
        String message = VUOTA;

        if (obj == null) {
            return ValidationResult.error("Occorre inserire un valore");
        }

        if (obj instanceof Serializable) {
            propertyNewValue = (Serializable) obj;
            message = text.primaMaiuscola(propertyName) + " indicato esiste già";

            if (operationForm == AEOperation.addNew) {
                entity = mongo.findOneUnique(entityBean.getClass(), propertyName, (Serializable) obj);
                return entity != null ? ValidationResult.error(message) : ValidationResult.ok();
            } else {
                if (propertyNewValue.equals(propertyOldValue)) {
                    return ValidationResult.ok();
                } else {
                    entity = mongo.findOneUnique(entityBean.getClass(), propertyName, (Serializable) obj);
                    return entity != null ? ValidationResult.error(message) : ValidationResult.ok();
                }
            }
        } else {
            return ValidationResult.ok();
        }
    }


    /**
     * Applies this function to the given arguments.
     *
     * @param o  the first function argument
     * @param o2 the second function argument
     *
     * @return the function result
     */
    @Override
    public Object apply(Object o, Object o2) {
        return null;
    }

}
