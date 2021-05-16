package it.algos.vaadflow14.backend.packages.crono.giorno;

import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.packages.crono.mese.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

import javax.validation.constraints.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 14-ago-2020
 * Time: 15:19
 * <p>
 * Classe (obbligatoria) di un package <br>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 * Le properties sono PUBLIC per poter usare la Reflection <br>
 * Unica classe obbligatoria per un package. <br>
 * Le altre servono solo se si vuole qualcosa in più dello standard minimo. <br>
 * <p>
 * Annotated with Spring: @SpringComponent (vaadin), @QueryEntity (querydsl), @Document (mongodb), @TypeAlias (data) <br>
 * Annotated with @SpringComponent, @QueryEntity, @Document, @TypeAlias <br>
 * Annotated with Lombok: @Data, @NoArgsConstructor, @AllArgsConstructor, @Builder, @EqualsAndHashCode <br>
 * Annotated with Algos: @AIScript per controllare il typo di file e la ri-creazione con Wizard <br>
 * Annotated with Algos: @AIEntity per informazioni sulle property per il DB <br>
 * Annotated with Algos: @AIView per info su menu, icon, route, search e sort <br>
 * Annotated with Algos: @AIList per info sulla Grid e sulle colonne <br>
 * Annotated with Algos: @AIForm per info sul Form e sulle properties <br>
 */
@SpringComponent
@QueryEntity
@Document(collection = "giorno")
@TypeAlias("giorno")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderGiorno")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovraScrivibile = false)
@AIEntity(recordName = "Giorno", keyPropertyName = "giorno", usaCompany = false, usaCreazione = false, usaModifica = false, usaBoot  = true)
@AIView(menuIcon = VaadinIcon.CALENDAR, searchProperty = "giorno", sortProperty = "ordine")
@AIList(fields = "ordine,giorno", usaRowIndex = false)
@AIForm(fields = "ordine,giorno,mese", usaSpostamentoTraSchede = false)
public class Giorno extends AEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * ordinamento (obbligatorio, unico) <br>
     */
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.integer, caption = "progressivo", typeNum = AETypeNum.positiviOnly)
    @AIColumn(header = "#", widthEM = 3)
    public int ordine;


    /**
     * nome completo (obbligatorio, unico) <br>
     */
    @NotBlank()
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, focus = true)
    @AIColumn(flexGrow = true)
    public String giorno;


    /**
     * mese di riferimento (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @NotNull
    @DBRef
    @AIField(type = AETypeField.combo, comboClazz = Mese.class, usaComboBoxGrid = true)
    @AIColumn(widthEM = 8)
    public Mese mese;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return giorno;
    }

}