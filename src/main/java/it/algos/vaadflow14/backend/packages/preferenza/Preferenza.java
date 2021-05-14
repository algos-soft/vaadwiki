package it.algos.vaadflow14.backend.packages.preferenza;

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
 * Date: mar, 25-ago-2020
 * Time: 11:23
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
@Document(collection = "preferenza")
@TypeAlias("preferenza")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderPreferenza")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovraScrivibile = false)
@AIEntity(recordName = "Preferenza", keyPropertyName = "code", usaCompany = true, usaNote = true)
@AIView(menuName = "Preferenza", menuIcon = VaadinIcon.COG, searchProperty = "code", sortProperty = "code")
@AIList(fields = "code,type,value,vaadFlow,usaCompany,needRiavvio,visibileAdmin,descrizione", usaRowIndex = true)
@AIForm(fields = "code,vaadFlow,usaCompany,needRiavvio,visibileAdmin,descrizione,type,value", usaSpostamentoTraSchede = true)
public class Preferenza extends AEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * codice di riferimento (obbligatorio, unico)
     */
    @NotBlank()
    @Size(min = 3)
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = 13, focus = true, caption = "Codice")
    @AIColumn(widthEM = 14)
    public String code;


    /**
     * tipo di dato memorizzato (obbligatorio)
     */
    @NotNull
    @AIField(type = AETypeField.enumeration, enumClazz = AETypePref.class, usaComboBoxGrid = true, required = true, focus = true, widthEM = 12)
    @AIColumn(widthEM = 6, sortable = true)
    public AETypePref type;

    /**
     * valore della preferenza (obbligatorio)
     */
    @NotNull
    @AIField(type = AETypeField.preferenza, required = true, caption = "Valore", widthEM = 12)
    @AIColumn(widthEM = 10)
    public byte[] value;


    /**
     * generale (facoltativo, default true) se usata da vaadflow
     * specifica se usata da progetto derivato (vaadwam, vaadwiki)
     */
    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolField.checkBox, caption = "Preferenza standard")
    @AIColumn(headerIcon = VaadinIcon.HOME)
    public boolean vaadFlow;


    /**
     * usaCompany (facoltativo, default false) usa un prefisso col codice della company
     */
    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolField.checkBox, caption = "Specifica per ogni company")
    @AIColumn(headerIcon = VaadinIcon.FACTORY)
    public boolean usaCompany;


    /**
     * necessita di riavvio per essere utilizzata (facoltativo, default false)
     * alla partenza del programma viene acquisita nelle costanti di FlowCost
     * per evitare inutili accessi al mongoDB
     */
    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolField.checkBox, caption = "Occorre riavviare")
    @AIColumn(headerIcon = VaadinIcon.REFRESH)
    public boolean needRiavvio;

    /**
     * visibile e modificabile da un admin (facoltativo, default false)
     * per creare una lista di preferenze nella scheda utente dell'admin oppure
     * nella scheda della company
     */
    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolField.checkBox, caption = "Visibile agli admin")
    @AIColumn(headerIcon = VaadinIcon.USER)
    public boolean visibileAdmin;

    /**
     * descrizione (obbligatoria)
     */
    @NotBlank()
    @Size(min = 5)
    @AIField(type = AETypeField.text, widthEM = 24)
    @AIColumn(widthEM = 24, flexGrow = true)
    public String descrizione;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getCode();
    }


    public Object getValore() {
        return getType().bytesToObject(value);
    }

}