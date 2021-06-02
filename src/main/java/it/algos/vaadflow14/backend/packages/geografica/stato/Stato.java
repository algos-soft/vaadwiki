package it.algos.vaadflow14.backend.packages.geografica.stato;

import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.packages.geografica.continente.*;
import it.algos.vaadflow14.backend.packages.geografica.regione.*;
import it.algos.vaadflow14.wizard.enumeration.*;
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
 * First time: sab, 12-set-2020
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
@Document(collection = "stato")
//Spring data
@TypeAlias("stato")
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderStato")
@EqualsAndHashCode(callSuper = false)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.entity, doc = AEWizDoc.inizioRevisione)
@AIEntity(recordName = "Stato", keyPropertyName = "stato", usaReset = true, usaBoot = true, usaNew = false)
@AIView(menuName = "Stato", menuIcon = VaadinIcon.GLOBE, searchProperty = "stato", sortProperty = "ordine")
@AIList(fields = "ordine,bandiera,stato,ue,continente,numerico,alfadue,alfatre,locale", usaDeleteMenu = true, usaRowIndex = false)
@AIForm(fields = "ordine,stato,bandiera,regioni,ue,continente,numerico,alfadue,alfatre,locale", usaSpostamentoTraSchede = true)
public class Stato extends AEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * ordine di presentazione per il popup (obbligatorio, unico) <br>
     */
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    @AIField(type = AETypeField.integer, caption = "ordine", typeNum = AETypeNum.positiviOnly, widthEM = 4)
    @AIColumn(header = "#", widthEM = 4)
    public int ordine;


    /**
     * nome (obbligatorio, unico) <br>
     */
    @NotBlank(message = "Nome obbligatorio")
    @Size(min = 3)
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, required = true, focus = true, firstCapital = true, widthEM = 24)
    @AIColumn(widthEM = 12)
    public String stato;

    /**
     * bandierina per i popup (facoltativa) <br>
     */
    @AIField(type = AETypeField.image, heightEM = 6)
    @AIColumn(headerIcon = VaadinIcon.GLOBE)
    public String bandiera;


    /**
     * divisione amministrativa di secondo livello (facoltativa) <br>
     */
    @Transient()
    @AIField(type = AETypeField.gridShowOnly, caption = "divisioni amministrative di secondo livello", linkClazz = Regione.class, linkProperty = "stato", properties = "divisione,iso,sigla,status")
    @AIColumn(header = "Divisione")
    public List<Regione> regioni;


    /**
     * continente (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @DBRef
    @AIField(type = AETypeField.combo, comboClazz = Continente.class, widthEM = 14, usaComboBox = true, comboInitialValue = "Europa")
    @AIColumn(widthEM = 8)
    public Continente continente;


    /**
     * unione europea
     */
    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolField.checkBox, caption = "Appartenente all' Unione Europea", usaCheckBox3Vie = true)
    @AIColumn(typeBool = AETypeBoolCol.checkIcon, header = "UE")
    public boolean ue;


    /**
     * codice iso-numerico di riferimento (obbligatorio, unico) <br>
     */
    @NotBlank(message = "Il codice ISO numerico è obbligatorio")
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    @AIField(type = AETypeField.text, widthEM = 4)
    @AIColumn(header = "code", widthEM = 6)
    public String numerico;


    /**
     * codice iso-alfa2 di riferimento (obbligatorio) <br>
     */
    @NotBlank(message = "Il codice iso-alfa2 numerico è obbligatorio")
    @Indexed()
    @AIField(type = AETypeField.text, caption = "alfa-due", widthEM = 4)
    @AIColumn(header = "αDue", widthEM = 6)
    public String alfadue;


    /**
     * codice iso-alfa3 di riferimento (obbligatorio, unico) <br>
     */
    @NotBlank(message = "Il codice iso-alfa3 numerico è obbligatorio")
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, caption = "alfa-tre", widthEM = 4)
    @AIColumn(header = "αTre", widthEM = 6)
    public String alfatre;


    /**
     * codice iso-locale di riferimento (obbligatorio, unico) <br>
     */
    @NotBlank(message = "Il codice iso locale è obbligatorio")
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = 10)
    @AIColumn(header = "ISO locale")
    public String locale;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getStato();
    }

}