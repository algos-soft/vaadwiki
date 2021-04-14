package it.algos.vaadflow14.backend.wrapper;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDate;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 28-nov-2020
 * Time: 17:48
 * <p>
 * Immutable time period class <br>
 * Enforce the invariant that the start of a period does not follow its end <br>
 * LocalDate is an immutable date-time object that represents a date, so is not
 * necessary to make defensive copies of parameters in the constructor <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public final class APeriodo {

    private final LocalDate inizio;

    private final LocalDate fine;


    public APeriodo(LocalDate inizio, LocalDate fine) {

        if (inizio.compareTo(fine) > 0) {
            throw new IllegalArgumentException(inizio + " after " + fine);
        }

        this.inizio = inizio;
        this.fine = fine;
    }

    public LocalDate getInizio() {
        return inizio;
    }

    public LocalDate getFine() {
        return fine;
    }

}
