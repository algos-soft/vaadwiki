package it.algos.vaadwiki.backend.packages.bio;

import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * First time: dom, 18-apr-2021 <br>
 * Last doc revision: mer, 19-mag-2021 alle 16:43 <br>
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
@Document(collection = "bio")
//Spring data
@TypeAlias("bio")
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderBio")
@EqualsAndHashCode(callSuper = false)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.entity, doc = AEWizDoc.inizioRevisione)
@AIEntity(recordName = "Bio", keyPropertyName = "wikiTitle")
@AIView(menuName = "Bio", menuIcon = VaadinIcon.ASTERISK, searchProperty = "wikiTitle", sortProperty = "lastModifica")
@AIList(fields = "pageId,wikiTitle,lastModifica,lastLettura", usaRowIndex = true)
@AIForm(fields = "pageId,wikiTitle,lastModifica,tmplBioServer", operationForm = AEOperation.edit, usaSpostamentoTraSchede = false)
public class Bio extends AEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * larghezza delle colonne
     */
    private static final transient int WIDTHEM_ID = 7;

    private static final transient int WIDTHEM_TITLE = 20;

    private static final transient int WIDTHEM = 12;

    /**
     * pageId (obbligatorio, unico) <br>
     */
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.lungo, required = true, caption = "PageId interno della pagina wiki", typeNum = AETypeNum.positiviOnly, widthEM = WIDTHEM_ID, enabled = false)
    @AIColumn(header = "#", widthEM = WIDTHEM_ID)
    public long pageId;


    /**
     * wikiTitle di riferimento (obbligatorio, unico) <br>
     */
    @NotBlank(message = "Il wikiTitle è obbligatorio")
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @Size(min = 2, max = 50)
    @AIField(type = AETypeField.text, required = true, focus = true, caption = "Titolo esatto della pagina wiki", widthEM = WIDTHEM_TITLE, enabled = false)
    @AIColumn(widthEM = WIDTHEM_TITLE)
    public String wikiTitle;


    @Lob
    @Field("tmpls")
    @AIField(type = AETypeField.textArea, required = true, caption = "Template presente sul server", widthEM = 48, enabled = false)
    public String tmplBioServer;


    //--ultima modifica sul server wiki
    //--uso il formato Timestamp, per confrontarla col campo timestamp
    //--molto meglio che siano esattamente dello stesso tipo
    @Field("mod")
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.localDateTime, caption = "Ultima modifica effettuata (non dal Bot) sul server", enabled = false)
    @AIColumn(typeData = AETypeData.normaleOrario)
    public LocalDateTime lastModifica;


    //--ultima lettura/aggiornamento della voce, effettuata dal programma VaadBio
    //--uso il formato Timestamp, per confrontarla col campo timestamp
    //--molto meglio che siano esattamente dello stesso tipo
    @Field("let")
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.localDateTime, help = "ultima lettura/aggiornamento della voce effettuata dal programma VaadBio", enabled = false)
    public LocalDateTime lastLettura;


    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @Size(min = 2, max = 50)
    @AIField(type = AETypeField.text, required = false, widthEM = WIDTHEM)
    @AIColumn(widthEM = WIDTHEM)
    public String nome;


    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @Size(min = 2, max = 50)
    @AIField(type = AETypeField.text, required = false, widthEM = WIDTHEM)
    @AIColumn(widthEM = WIDTHEM)
    public String cognome;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return wikiTitle;
    }


}// end of entity class