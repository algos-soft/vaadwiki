package it.algos.vaadwiki.modules.bio;

import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadflow.modules.giorno.Giorno;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


/**
 * Project vaadbio2 <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Date: 11-ago-2018 17.19.29 <br>
 * <p>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 * <p>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Document (facoltativo) per avere un nome della collection (DB Mongo) diverso dal nome della Entity <br>
 * Annotated with @Scope (obbligatorio = 'singleton') <br>
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter <br>
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications <br>
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service <br>
 * Annotated with @Builder (Lombok) lets you automatically produce the code required to have your class
 * be instantiable with code such as: Person.builder().name("Adam Savage").city("San Francisco").build(); <br>
 * Annotated with @EqualsAndHashCode (Lombok) per l'uguaglianza di due istanze dellaq classe <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica <br>
 * Annotated with @AIEntity (facoltativo Algos) per alcuni parametri generali del modulo <br>
 * Annotated with @AIList (facoltativo Algos) per le colonne automatiche della Lista  <br>
 * Annotated with @AIForm (facoltativo Algos) per i fields automatici del Dialog e del Form <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * Inserisce SEMPRE la versione di serializzazione <br>
 * Le singole property sono pubbliche in modo da poterne leggere il valore tramite 'reflection'
 * Le singole property sono annotate con @AIField (obbligatorio Algos) per il tipo di Field nel Dialog e nel Form <br>
 * Le singole property sono annotate con @AIColumn (facoltativo Algos) per il tipo di Column nella Grid <br>
 */
@Entity
@Document(collection = "bio")
@TypeAlias("bio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderBio")
@EqualsAndHashCode(callSuper = false)
@AIEntity(company = EACompanyRequired.nonUsata)
@AIList(fields = {"wikiTitle", "cognome", "lastModifica", "lastLettura", "nome", "sesso", "luogoNato", "annoNato", "annoMorto", "attivita", "attivita2", "attivita3", "nazionalita", "luogoMorto", "giornoNato", "giornoMorto"})
@AIForm(fields = {"pageid", "wikiTitle", "lastModifica", "lastLettura", "tmplBioServer", "nome", "cognome", "sesso", "luogoNato", "giornoNato", "annoNato", "luogoMorto", "giornoMorto", "annoMorto", "attivita", "attivita2", "attivita3", "attivitaAltre", "nazionalita", "lastModifica", "lastLettura"})
@AIScript(sovrascrivibile = false)
public class Bio extends AEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * pageid della pagina wiki (obbligatorio, unico) <br>
     * il più importante per primo <br>
     */
    @NotNull
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = EAFieldType.lungo, widthEM = 3)
    @AIColumn(widthEM = 9)
    public long pageid;

    /**
     * title della pagina wiki (obbligatorio, unico) <br>
     */
    @NotNull
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @Size(min = 3)
    @AIField(type = EAFieldType.text, required = true, focus = true, widthEM = 12)
    @AIColumn(widthEM = 18)
    public String wikiTitle;


    @Lob
    @AIField(type = EAFieldType.textarea, required = true, help = "Template effettivamente presente sul server.")
    public String tmplBioServer;


    //--ultima modifica sul server wiki
    //--uso il formato Timestamp, per confrontarla col campo timestamp
    //--molto meglio che siano esattamente dello stesso tipo
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = EAFieldType.localdatetime, required = true, help = "ultima modifica della voce effettuata sul server wiki")
    public LocalDateTime lastModifica;


    //--ultima lettura/aggiornamento della voce, effettuata dal programma VaadBio
    //--uso il formato Timestamp, per confrontarla col campo timestamp
    //--molto meglio che siano esattamente dello stesso tipo
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = EAFieldType.localdatetime, required = true, help = "ultima lettura/aggiornamento della voce effettuata dal programma VaadBio")
    public LocalDateTime lastLettura;


    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = EAFieldType.text)
    @AIColumn(widthEM = 10)
    private String nome;

    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = EAFieldType.text)
    @AIColumn(widthEM = 10)
    private String cognome;

    @AIField(type = EAFieldType.text)
    @AIColumn(name = "X", widthEM = 2)
    private String sesso;

    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = EAFieldType.text)
    @AIColumn(name = "LuogoNato", widthEM = 8)
    private String luogoNato;

    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = EAFieldType.text)
    @AIColumn(width = 210)
    private String giornoNato;

    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = EAFieldType.text)
    @AIColumn(name = "Nato", widthEM = 4)
    private String annoNato;

    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = EAFieldType.text)
    @AIColumn(name = "LuogoMorto", widthEM = 8)
    private String luogoMorto;

    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = EAFieldType.text)
    @AIColumn(width = 210)
    private String giornoMorto;

    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = EAFieldType.text)
    @AIColumn(name = "Morto", widthEM = 4)
    private String annoMorto;

    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = EAFieldType.text)
    @AIColumn(widthEM = 8)
    private String attivita;

    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = EAFieldType.text)
    @AIColumn(widthEM = 8)
    private String attivita2;

    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = EAFieldType.text)
    @AIColumn(widthEM = 8)
    private String attivita3;

    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = EAFieldType.text)
    @AIColumn(widthEM = 8)
    private String nazionalita;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return wikiTitle;
    }// end of method


}// end of entity class