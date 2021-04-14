package it.algos.vaadflow14.backend.packages.geografica.continente;

import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.packages.geografica.stato.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

import javax.validation.constraints.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 29-set-2020
 * Time: 14:34
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
@Document(collection = "continente")
@TypeAlias("continente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderContinente")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovraScrivibile = false)
@AIEntity(recordName = "Continente", keyPropertyName = "nome", usaCompany = false, usaCreazione = false, usaModifica = false)
@AIView(menuName = "Continente", menuIcon = VaadinIcon.GLOBE, searchProperty = "nome", sortProperty = "ordine")
@AIList(fields = "ordine,nome,abitato", usaRowIndex = false, usaReset = true)
@AIForm(fields = "ordine,nome,stati,abitato", usaSpostamentoTraSchede = true)
public class Continente extends AEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * ordine di presentazione per il popup (obbligatorio, unico) <br>
     */
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    @AIField(type = AETypeField.integer, typeNum = AETypeNum.positiviOnly)
    @AIColumn(header = "#", widthEM = 2)
    public int ordine;

    /**
     * nome (obbligatorio, unico)
     */
    @NotBlank()
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, required = true, focus = true)
    @AIColumn(widthEM = 14)
    public String nome;


    /**
     * esistono abitanti (facoltativa)
     */
    @AIField(type = AETypeField.booleano, caption = "Continente abitato")
    @AIColumn(header = "Ab.")
    public boolean abitato;

    /**
     * divisione amministrativa di secondo livello (facoltativa) <br>
     */
    @Transient()
    @AIField(type = AETypeField.gridShowOnly, caption = "stati presenti", linkClazz = Stato.class, linkProperty = "continente", properties = "stato,alfatre")
    public List<Stato> stati;

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getNome();
    }

}