package it.algos.vaadflow14.backend.packages.anagrafica.via;

import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
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
 * Date: gio, 10-set-2020
 * Time: 10:58
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
@Document(collection = "via")
@TypeAlias("via")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderVia")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovraScrivibile = false)
@AIEntity(recordName = "Via", keyPropertyName = "nome", usaCompany = false, usaCreazione = true, usaModifica = true, usaResetIniziale = true)
@AIView(menuName = "Via", menuIcon = VaadinIcon.ASTERISK, searchProperty = "nome", sortProperty = "ordine", sortDirection = "ASC")
@AIList(fields = "ordine,nome", usaRowIndex = false, usaReset = true)
@AIForm(fields = "ordine,nome", usaSpostamentoTraSchede = true)
public class Via extends AEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * ordine di presentazione per il popup (obbligatorio, unico) <br>
     */
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    @AIField(type = AETypeField.integer, caption = "ordine", typeNum = AETypeNum.positiviOnly)
    @AIColumn(header = "#", widthEM = 2.5)
    public int ordine;


    /**
     * nome (obbligatorio, unico)
     */
    @NotBlank()
    @Size(min = 3)
    @Indexed(unique = true)
    @AIField(type = AETypeField.text, focus = true)
    @AIColumn(widthEM = 20)
    public String nome;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getNome();
    }

}// end of Bean