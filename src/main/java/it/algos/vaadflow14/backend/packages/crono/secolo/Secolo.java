package it.algos.vaadflow14.backend.packages.crono.secolo;

import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

import javax.validation.constraints.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 02-ago-2020
 * Time: 06:48
 * <p>
 * Classe (obbligatoria) di un package <br>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 * Le properties sono PUBLIC per poter usare la Reflection <br>
 * Unica classe obbligatoria per un package. Le altre servono solo per personalizzare. <br>
 * <p>
 * Annotated with Lombok: @Data, @NoArgsConstructor, @AllArgsConstructor, @Builder, @EqualsAndHashCode <br>
 * Annotated with Algos: @AIScript per controllare la ri-creazione di questo file dal Wizard <br>
 * Annotated with Algos: @AIEntity per informazioni sulle property per il DB <br>
 * Annotated with Algos: @AIView per info su menu, icon, route, search e sort <br>
 * Annotated with Algos: @AIList per info sulle colonne della Grid <br>
 * Annotated with Algos: @AIForm per info sulle properties del Form <br>
 */
@SpringComponent
@QueryEntity
@Document(collection = "secolo")
@TypeAlias("secolo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderSecolo")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovraScrivibile = false)
@AIEntity(recordName = "Secolo", keyPropertyName = "secolo", usaCompany = false, usaCreazione = false, usaModifica = false)
@AIView(menuName = "Secolo", menuIcon = VaadinIcon.CALENDAR, searchProperty = "secolo", sortProperty = "ordine")
@AIList(fields = "ordine,secolo,anteCristo,inizio,fine", usaRowIndex = false)
@AIForm(fields = "secolo,anteCristo,inizio,fine", usaSpostamentoTraSchede = false)
public class Secolo extends AEntity {

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
    @AIColumn()
    public String secolo;


    /**
     * flag di separazione (obbligatorio)
     */
    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolField.checkBox, caption = "Ante" + FlowCost.HTLM_SPAZIO + "Cristo", widthEM = 6)
    @AIColumn(typeBool = AETypeBoolCol.yesNo, header = "A.C.")
    public boolean anteCristo;


    /**
     * primo anno (obbligatorio, unico) <br>
     */
    @AIField(type = AETypeField.integer, typeNum = AETypeNum.positiviOnly, caption = "Anno iniziale")
    @AIColumn(widthEM = 6)
    public int inizio;

    /**
     * ultimo anno (obbligatorio, unico) <br>
     */
    @AIField(type = AETypeField.integer, typeNum = AETypeNum.positiviOnly, caption = "Anno finale")
    @AIColumn(flexGrow = true)
    public int fine;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getSecolo();
    }

}