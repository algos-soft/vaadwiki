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
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

import javax.persistence.*;
import javax.validation.constraints.*;

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
@AIEntity(recordName = "Bio", keyPropertyName = "wikiTitle", usaCompany = false)
@AIView(menuName = "Bio", menuIcon = VaadinIcon.ASTERISK, searchProperty = "wikiTitle", sortProperty = "wikiTitle")
@AIList(fields = "pageId,wikiTitle,nome,cognome", usaRowIndex = true)
@AIForm(fields = "pageId,wikiTitle,nome,cognome,tmplBioServer", operationForm = AEOperation.edit, usaSpostamentoTraSchede = false)
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
    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.lungo, caption = "pageId", typeNum = AETypeNum.positiviOnly, widthEM = WIDTHEM_ID )
    @AIColumn(header = "#", widthEM = WIDTHEM_ID )
    public long pageId;


    /**
     * wikiTitle di riferimento (obbligatorio, unico) <br>
     */
    @NotBlank(message = "Il wikiTitle è obbligatorio")
    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @Size(min = 2, max = 50)
    @AIField(type = AETypeField.text, required = true, focus = true, widthEM = WIDTHEM_TITLE)
    @AIColumn(widthEM = WIDTHEM_TITLE)
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

    @Transient()
    @Lob
    @AIField(type = AETypeField.textArea, required = true, help = "Template semplificato.", widthEM = 48)
    public String tmplBioGac;

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return wikiTitle;
    }


}// end of entity class