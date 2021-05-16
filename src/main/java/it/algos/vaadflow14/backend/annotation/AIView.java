package it.algos.vaadflow14.backend.annotation;

import com.vaadin.flow.component.icon.VaadinIcon;
import static it.algos.vaadflow14.backend.application.FlowCost.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
 * vaadFlow()=false -> (Optional) Appartenenza al progetto Base VaadFlow14
 * searchProperty()=VUOTA -> (Mandatory) Property per la ricerca tramite il searchField
 * sortProperty()=VUOTA -> (Mandatory) Property per l'ordinamento
 * sortDirection()="ASC" -> (Optional) Direzione per l'ordinamento
 * startListEmpty()=false -> (Optional) Mostra la lista vuota all'apertura.
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
     * (Optional) Appartenenza al progetto Base VaadFlow14
     *
     * @return the boolean
     */
    boolean vaadFlow() default false;

    /**
     * (Mandatory) Property per la ricerca tramite il searchField
     *
     * @return the string
     */
    String searchProperty() default VUOTA;

    /**
     * (Mandatory) Property per l'ordinamento
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

    /**
     * (Optional) Mostra la lista vuota all'apertura. Da usare SOLO se ci sono filtri di selezione.
     * Altrimenti non si vedrà mai niente
     *
     * @return the boolean
     */
    boolean startListEmpty() default false;



}
