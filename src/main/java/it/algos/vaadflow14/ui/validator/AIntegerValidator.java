package it.algos.vaadflow14.ui.validator;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.enumeration.AETypeNum;
import it.algos.vaadflow14.backend.service.ALogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 07-giu-2018
 * Time: 17:03
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AIntegerValidator implements Validator {

    private static final long serialVersionUID = 1L;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ALogService logger;

    private AETypeNum numType;

    private int min = 0;

    private int max = 0;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public AIntegerValidator() {
    } // end of SpringBoot constructor


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(AIntegerValidator.class, numType) <br>
     *
     * @param numType per stabilire quali numeri siano accettabili (negativi, zero, positivi) <br>
     */
    public AIntegerValidator(AETypeNum numType) {
        this(numType, 0, 0);
    } // end of SpringBoot constructor


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(AIntegerValidator.class, numType, min, max) <br>
     *
     * @param numType per stabilire quali numeri siano accettabili (negativi, zero, positivi) <br>
     * @param min     valore minimo accettabile (compreso)
     * @param max     valore massimo accettabile (compreso)
     */
    public AIntegerValidator(AETypeNum numType, int min, int max) {
        this.numType = numType;
        this.min = min;
        this.max = max;
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
        int numero = 0;

        if (obj == null) {
            return ValidationResult.error("Occorre inserire un numero");
        }

        if (numType == null) {
            return ValidationResult.error("Non è chiaro quali numeri accettare");
        }

        if (obj instanceof Integer) {
            numero = (Integer) obj;

            switch (numType) {
                case positiviOnly:
                    if (numero <= 0) {
                        if (numero == 0) {
                            return ValidationResult.error("Zero non ammesso");
                        } else {
                            return ValidationResult.error("Solo positivi");
                        }
                    } else {
                        return ValidationResult.ok();
                    }
                case positiviZero:
                    if (numero < 0) {
                        return ValidationResult.error("Esclusi negativi");
                    } else {
                        return ValidationResult.ok();
                    }
                case positiviNegativi:
                    if (numero == 0) {
                        return ValidationResult.error("Niente zero");
                    } else {
                        return ValidationResult.ok();
                    }
                case positiviNegativiZero:
                case range:
                case rangeControl:
                    if (numero < min) {
                        return ValidationResult.error("Almeno " + min);
                    }
                    if (numero > max) {
                        return ValidationResult.error("Massimo " + max);
                    }
                    return ValidationResult.ok();
                default:
                    logger.warn("Switch - caso non definito", this.getClass(), "apply");
                    break;
            }
        }

        return ValidationResult.error("Qualcosa non ha funzionato");
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
