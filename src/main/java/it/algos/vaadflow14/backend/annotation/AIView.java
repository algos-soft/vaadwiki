package it.algos.vaadflow14.backend.annotation;

import com.vaadin.flow.component.icon.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;

import java.lang.annotation.*;

/**
 * /**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: lun, 27-apr-2020
 * Time: 11:15
 * <p>
 * Annotation Algos per le Entity Class <br>
 * Controlla aspetti del menu, visivi e della @Route <br>
 * <p>
 * Regola:
 * menuName()=False -> (Mandatory) Label del menu
 * menuIcon()=VaadinIcon.ASTERISK -> (Optional) Icona visibile nel menu
 * searchProperty()=VUOTA -> (Mandatory) Property per la ricerca tramite il searchField
 * sortProperty()=VUOTA -> (Mandatory) Property per l'ordinamento della lista
 * sortDirection()="ASC" -> (Optional) Direzione per l'ordinamento della lista
 * <p>
 * Standard:
 * AIView(menuName = "Xxx", menuIcon = VaadinIcon.ASTERISK, searchProperty = "code", sortProperty = "code")
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
//--Class, interface (including annotation type), or enum declaration
public @interface AIView {


    /**
     * (Mandatory) Label del menu
     * Vaadin usa SEMPRE il 'name' della Annotation @Route per identificare (internamente) e recuperare la view
     * Nella menuBar appare invece visibile (con il primo carattere maiuscolo) il menuName, indicato qui
     * Di default usa il 'name' della view (@Route)
     *
     * @return the string
     */
    String menuName() default VUOTA;


    /**
     * (Optional) Icona visibile nel menu
     * Di default un asterisco
     *
     * @return the vaadin icon
     */
    VaadinIcon menuIcon() default VaadinIcon.ASTERISK;


    /**
     * (Mandatory) Property per la ricerca tramite il searchField
     *
     * @return the string
     */
    String searchProperty() default VUOTA;


    /**
     * (Mandatory) Property per l'ordinamento della lista
     *
     * @return the string
     */
    String sortProperty() default VUOTA;


    /**
     * (Optional) Direzione per l'ordinamento
     *
     * @return the string
     */
    String sortDirection() default SORT_SPRING_ASC;

}
