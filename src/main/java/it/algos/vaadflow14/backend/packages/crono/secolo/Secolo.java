package it.algos.vaadflow14.backend.packages.crono.secolo;

import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

import javax.validation.constraints.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * First time: dom, 02-ago-2020
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
@Document(collection = "secolo")
//Spring data
@TypeAlias("secolo")
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderSecolo")
@EqualsAndHashCode(callSuper = false)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.entity, doc = AEWizDoc.inizioRevisione)
@AIEntity(recordName = "Secolo", keyPropertyName = "secolo", usaReset = true, usaBoot = true, usaNew = false)
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
    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolField.checkBox, caption = "Ante" + FlowCost.HTLM_SPAZIO + "Cristo", usaCheckBox3Vie = true, widthEM = 6)
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
    @AIColumn(widthEM = 6)
    public int fine;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getSecolo();
    }

}