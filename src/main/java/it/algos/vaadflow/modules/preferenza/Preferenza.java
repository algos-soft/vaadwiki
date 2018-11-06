package it.algos.vaadflow.modules.preferenza;

import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.backend.entity.ACEntity;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAFieldType;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 26-ott-2018 9.59.58 <br>
 * <p>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 * <p>
 * Not annotated with @SpringComponent (inutile).  <br>
 * Not annotated with @Scope (inutile). Le istanze 'prototype' vengono generate da xxxService.newEntity() <br>
 * Not annotated with @Qualifier (inutile) <br>
 * Annotated with @Document (facoltativo) per avere un nome della collection (DB Mongo) diverso dal nome della Entity <br>
 * Annotated with @TypeAlias (facoltativo) to replace the fully qualified class name with a different value. <br>
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter <br>
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications <br>
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service <br>
 * Annotated with @Builder (Lombok) con un metodo specifico, per usare quello standard nella (eventuale) sottoclasse <br>
 * - lets you automatically produce the code required to have your class be instantiable with code such as:
 * - Person.builder().name("Adam Savage").city("San Francisco").build(); <br>
 * Annotated with @EqualsAndHashCode (Lombok) per l'uguaglianza di due istanze della classe <br>
 * Annotated with @AIEntity (facoltativo Algos) per alcuni parametri generali del modulo <br>
 * Annotated with @AIList (facoltativo Algos) per le colonne automatiche della Grid nella lista <br>
 * Annotated with @AIForm (facoltativo Algos) per i fields automatici nel dialogo del Form <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * <p>
 * Inserisce SEMPRE la versione di serializzazione <br>
 * Le singole property sono pubbliche in modo da poterne leggere il valore tramite 'reflection' <br>
 * Le singole property sono annotate con @AIColumn (facoltativo Algos) per il tipo di Column nella Grid <br>
 * Le singole property sono annotate con @AIField (obbligatorio Algos) per il tipo di fields nel dialogo del Form <br>
 * Le singole property sono annotate con @Field("xxx") (facoltativo)
 * -which gives a name to the key to be used to store the field inside the document.
 * -The property name (i.e. 'descrizione') would be used as the field key if this annotation was not included.
 * -Remember that field keys are repeated for every document so using a smaller key name will reduce the required space.
 */
@Entity
@Document(collection = "preferenza")
@TypeAlias("preferenza")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderPreferenza")
@EqualsAndHashCode(callSuper = false)
@AIEntity(company = EACompanyRequired.facoltativa)
@AIList(fields = {"company", "ordine", "code", "descrizione", "type", "value"})
@AIForm(fields = {"company", "ordine", "code", "descrizione", "type"})
@AIScript(sovrascrivibile = false)
public class Preferenza extends ACEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * ordine di presentazione (obbligatorio, unico) <br>
     * il più importante per primo <br>
     */
    @NotNull
    @Indexed()
    @Field("ord")
    @AIField(type = EAFieldType.integer, widthEM = 3)
    @AIColumn(name = "#", width = 55)
    public int ordine;

    /**
     * codice di riferimento (obbligatorio, unico) <br>
     */
    @NotNull
    @Indexed()
    @Size(min = 3)
    @Field("cod")
    @AIField(type = EAFieldType.text, required = true, focus = true, widthEM = 12)
    @AIColumn(width = 210)
    public String code;

    /**
     * descrizione (obbligatoria, non unica) <br>
     */
    @NotNull(message = "La descrizione è obbligatoria")
    @Size(min = 2, max = 50)
    @Field("desc")
    @AIField(type = EAFieldType.text, firstCapital = true, widthEM = 24)
    @AIColumn(width = 370)
    public String descrizione;


    /**
     * tipo di dato memorizzato (obbligatorio)
     */
    @NotNull
    @Field("type")
    @AIField(type = EAFieldType.enumeration, clazz = EAPrefType.class, required = true, focus = true, widthEM = 12)
    public EAPrefType type;


    //--valore della preferenza (obbligatorio)
    @NotNull
    @Field("val")
    @AIField(type = EAFieldType.pref, required = true, name = "Valore", widthEM = 12)
    public byte[] value;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return code;
    }// end of method

//    @Deprecated
//    public Object getAValue() {
//        return type.bytesToObject(value);
//    }// end of method
//
//    @Deprecated
//    public void setAValue(Object value) {
//        this.value = type.objectToBytes(value);
//    }// end of method

}// end of entity class