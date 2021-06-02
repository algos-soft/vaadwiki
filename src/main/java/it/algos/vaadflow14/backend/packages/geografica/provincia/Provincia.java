package it.algos.vaadflow14.backend.packages.geografica.provincia;

import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.packages.geografica.regione.*;
import it.algos.vaadflow14.backend.packages.geografica.stato.*;
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
 * First time: mar, 15-set-2020
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
@Document(collection = "provincia")
//Spring data
@TypeAlias("provincia")
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderProvincia")
@EqualsAndHashCode(callSuper = false)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.entity, doc = AEWizDoc.inizioRevisione)
@AIEntity(recordName = "Provincia", keyPropertyName = "nome", usaReset = true, usaBoot = true, usaNew = false)
@AIView(menuName = "Provincia", menuIcon = VaadinIcon.GLOBE, searchProperty = "nome", sortProperty = "ordine")
@AIList(fields = "nome,sigla,regione,iso,status", usaRowIndex = true)
@AIForm(fields = "nome,sigla,iso,status", usaSpostamentoTraSchede = true)
public class Provincia extends AEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


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
    @AIField(type = AETypeField.combo, comboClazz = Regione.class, logicClazz = RegioneService.class, usaComboBox = true, widthEM = 14, usaComboMethod = true, methodName = "creaComboRegioniItaliane")
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
    @AIField(type = AETypeField.enumeration, enumClazz = AETypeProvincia.class, usaComboBox = true, widthEM = 17)
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