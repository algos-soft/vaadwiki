package it.algos.vaadflow.modules.person;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadflow.modules.address.Address;
import it.algos.vaadflow.modules.address.AddressPresenter;
import it.algos.vaadflow.modules.utente.Utente;
import lombok.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static it.algos.vaadflow.application.FlowCost.TAG_PER;

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

/**
 * Alcune property vengono gestite dalla superclasse Utente <br>
 */
@Entity
@Document(collection = "person")
@TypeAlias("person")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderPerson")
@EqualsAndHashCode(callSuper = false)
@AIEntity(company = EACompanyRequired.facoltativa)
@AIList(fields = {"nome", "cognome", "telefono", "mail", "indirizzo"})
@AIForm(fields = {"nome", "cognome", "telefono", "indirizzo", "mail"})
@AIScript(sovrascrivibile = false)
public class Person extends Utente {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    @Transient
    public boolean usaSuperClasse;

    /**
     * nome (obbligatorio, non unico)
     */
    @NotNull
    @Indexed()
    @Size(min = 4, max = 40)
    @Field("nome")
    @AIField(type = EAFieldType.text, required = true, focus = true, widthEM = 20)
    @AIColumn(width = 200,name="nome")
    public String nome;

    /**
     * cognome (obbligatorio, non unica)
     */
    @NotNull
    @Indexed()
    @Size(min = 4, max = 40)
    @Field("cognome")
    @AIField(type = EAFieldType.text, firstCapital = true, widthEM = 20)
    @AIColumn(width = 200,name="cognome")
    public String cognome;

    /**
     * telefono (facoltativo)
     */
    @Field("tel")
    @AIField(type = EAFieldType.text)
    @AIColumn(width = 160,name="tel")
    public String telefono;


    /**
     * indirizzo (facoltativo, non unica)
     * riferimento statico SENZA @DBRef (embedded)
     */
    @Field("ind")
    @AIField(type = EAFieldType.link, clazz = AddressPresenter.class, help = "Indirizzo")
    @AIColumn(width = 400, name = "ind")
    public Address indirizzo;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return nome + " " + cognome;
    }// end of method


}// end of entity class