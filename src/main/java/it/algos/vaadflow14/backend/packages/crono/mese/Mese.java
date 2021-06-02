package it.algos.vaadflow14.backend.packages.crono.mese;

import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import lombok.*;
import org.hibernate.validator.constraints.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * First time: gio, 30-lug-2020
 * Last doc revision: mer, 19-mag-2021 alle 18:38 <br>
 * <p>
 * Classe (obbligatoria) di un package <br>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 * Le properties sono PUBLIC per poter usare la Reflection ed i Test <br>
 * Unica classe obbligatoria per un package. <br>
 * Le altre servono solo se si vuole qualcosa in più dello standard minimo. <br>
 * <p>
 * Annotated with Lombok: @Data, @NoArgsConstructor, @AllArgsConstructor, @Builder, @EqualsAndHashCode <br>
 * Annotated with Algos: @AIScript per controllare il typo di file e la ri-creazione con Wizard <br>
 * Annotated with Algos: @AIEntity per informazioni sulle property per il DB <br>
 * Annotated with Algos: @AIView per info su menu, icon, route, search e sort <br>
 * Annotated with Algos: @AIList per info sulla Grid e sulle colonne <br>
 * Annotated with Algos: @AIForm per info sul Form e sulle properties <br>
 */
//Vaadin spring
@SpringComponent
//querydsl
@QueryEntity
//Spring mongodb
@Document(collection = "mese")
//Spring data
@TypeAlias("mese")
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderMese")
@EqualsAndHashCode(callSuper = false)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.entity, doc = AEWizDoc.inizioRevisione)
@AIEntity(recordName = "Mese", keyPropertyName = "mese", usaBoot = true, usaNew = false)
@AIView(menuName = "Mese", menuIcon = VaadinIcon.CALENDAR, searchProperty = "mese", sortProperty = "ordine")
@AIList(fields = "ordine,mese,giorni,giorniBisestile,sigla", usaRowIndex = false)
@AIForm(fields = "mese,giorni,giorniBisestile,sigla", usaSpostamentoTraSchede = true)
public class Mese extends AREntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * ordinamento (obbligatorio, unico) <br>
     */
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.integer, caption = "progressivo", typeNum = AETypeNum.positiviOnly)
    @AIColumn(header = "#", widthEM = 2)
    public int ordine;

    /**
     * nome completo (obbligatorio, unico) <br>
     */
    @NotBlank()
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, focus = true)
    @AIColumn()
    public String mese;

    /**
     * numero di giorni presenti (obbligatorio) <br>
     */
    @Range(min = 28, max = 31)
    @NotNull(message = "I giorni devono essere 28, 30 o 31")
    @AIField(type = AETypeField.integer, typeNum = AETypeNum.rangeControl)
    @AIColumn(headerIcon = VaadinIcon.CALENDAR, headerIconColor = "green")
    public int giorni;


    /**
     * numero di giorni presenti se anno bisestile (obbligatorio) <br>
     */
    @Range(min = 29, max = 31)
    @AIField(type = AETypeField.integer, caption = "anno bisestile", typeNum = AETypeNum.rangeControl)
    @AIColumn(headerIcon = VaadinIcon.CALENDAR, headerIconColor = "red")
    public int giorniBisestile;


    /**
     * nome abbreviato di tre cifre (obbligatorio, unico) <br>
     */
    @NotBlank()
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @Size(min = 3, max = 3)
    @AIField(type = AETypeField.text, required = true, widthEM = 4, placeholder = "xxx")
    @AIColumn(flexGrow = true)
    public String sigla;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return mese;
    }

}