package it.algos.vaadwiki.backend.packages.bio;

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

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * Fix date: dom, 18-apr-2021 <br>
 * Fix time: 8:44 <br>
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
@Document(collection = "bio")
@TypeAlias("bio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderBio")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovraScrivibile = false)
@AIEntity(recordName = "Bio", keyPropertyName = "wikiTitle", usaCompany = false)
@AIView(menuName = "Bio", menuIcon = VaadinIcon.ASTERISK, searchProperty = "wikiTitle", sortProperty = "wikiTitle")
@AIList(fields = "pageId,wikiTitle,nome,cognome", usaRowIndex = true)
@AIForm(fields = "pageId,wikiTitle,nome,cognome", operationForm = AEOperation.edit, usaSpostamentoTraSchede = true)
public class Bio extends AEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * larghezza delle colonne
     */
    private static final transient int WIDTHEM = 20;

    /**
     * pageId (obbligatorio, unico) <br>
     */
    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.lungo, caption = "pageId", typeNum = AETypeNum.positiviOnly, widthEM = WIDTHEM)
    @AIColumn(header = "#", widthEM = WIDTHEM)
    public long pageId;


    /**
     * wikiTitle di riferimento (obbligatorio, unico) <br>
     */
    @NotBlank(message = "Il wikiTitle è obbligatorio")
    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @Size(min = 2, max = 50)
    @AIField(type = AETypeField.text, required = true, focus = true, widthEM = WIDTHEM)
    @AIColumn(widthEM = WIDTHEM)
    public String wikiTitle;


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

    @Lob
    @Field("tmpl")
    @AIField(type = AETypeField.textArea, required = true, help = "Template effettivamente presente sul server.", widthEM = 48)
    public String tmplBioServer;

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return wikiTitle;
    }


}// end of entity class