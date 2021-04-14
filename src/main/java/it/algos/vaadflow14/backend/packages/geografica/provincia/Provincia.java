package it.algos.vaadflow14.backend.packages.geografica.provincia;

import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.packages.geografica.regione.*;
import it.algos.vaadflow14.backend.packages.geografica.stato.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

import javax.validation.constraints.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 15-set-2020
 * Time: 11:29
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
@Document(collection = "provincia")
@TypeAlias("provincia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderProvincia")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovraScrivibile = false)
@AIEntity(recordName = "Provincia", keyPropertyName = "nome", usaCompany = false, usaCreazione = false, usaModifica = false)
@AIView(menuName = "Provincia", menuIcon = VaadinIcon.GLOBE, searchProperty = "nome", sortProperty = "ordine")
@AIList(fields = "ordine,nome,sigla,regione,iso,status", usaRowIndex = false)
@AIForm(fields = "ordine,nome,sigla,regione,iso,status", usaSpostamentoTraSchede = false)
public class Provincia extends AEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * ordinamento (obbligatorio, unico) <br>
     */
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.integer, caption = "ordine", typeNum = AETypeNum.positiviOnly)
    @AIColumn(header = "#", widthEM = 3)
    public int ordine;

    /**
     * nome (obbligatorio, unico) <br>
     */
    @NotBlank()
    @Size(min = 3)
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, required = true, focus = true, firstCapital = true, caption = "provincia")
    @AIColumn()
    public String nome;


    /**
     * sigla (obbligatorio, unico) <br>
     */
    @NotBlank(message = "Sigla obbligatoria")
    @Size(min = 2)
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, required = true, caption = "sigla breve", widthEM = 12)
    @AIColumn(header = "#", widthEM = 5)
    public String sigla;

    /**
     * regione (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @NotNull
    @DBRef
    @AIField(type = AETypeField.combo, comboClazz = Regione.class, logicClazz = RegioneService.class, usaComboMethod = true, methodName = "creaComboRegioniItaliane")
    @AIColumn(widthEM = 11)
    public Regione regione;


    /**
     * stato (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    //    @NotNull
    @DBRef
    @AIField(type = AETypeField.combo, comboClazz = Stato.class, logicClazz = StatoService.class, usaComboMethod = true, methodName = "creaComboStati")
    @AIColumn(widthEM = 8)
    public Stato stato;

    /**
     * codice iso di riferimento (obbligatorio, unico) <br>
     */
    @NotBlank(message = "Il codice ISO numerico è obbligatorio")
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, required = true, caption = "Codice ISO 3166-2:IT", widthEM = 12)
    @AIColumn(header = "iso", widthEM = 5)
    public String iso;


    /**
     * statuto normativo (obbligatorio) <br>
     */
    @NotNull()
    @AIField(type = AETypeField.enumeration, enumClazz = AETypeProvincia.class, widthEM = 16)
    @AIColumn(widthEM = 15)
    public AETypeProvincia status;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getNome();
    }

}