package it.algos.vaadflow14.ui.validator;

import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 07-giu-2018
 * Time: 16:28
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AStringBlankValidator implements Validator {

    private static final long serialVersionUID = 1L;

    private String message = VUOTA;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public AStringBlankValidator() {
        this(VUOTA);
    } // end of SpringBoot constructor


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(AStringBlankValidator.class, message) <br>
     *
     * @param message da mostrare
     */
    public AStringBlankValidator(String message) {
        this.message = message;
    } // end of SpringBoot constructor


    @Override
    public ValidationResult apply(Object obj, ValueContext valueContext) {
        String testo = VUOTA;

        if (obj instanceof String) {
            testo = (String) obj;

            if (testo.length() == 0) {
                if (message.equals("")) {
                    return ValidationResult.error("Non può essere vuoto");
                } else {
                    return ValidationResult.error(message);
                }
            } else {
                return ValidationResult.ok();
            }
        } else {
            if (message.equals("")) {
                return ValidationResult.error("Non può essere vuoto");
            } else {
                return ValidationResult.error(message);
            }
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
