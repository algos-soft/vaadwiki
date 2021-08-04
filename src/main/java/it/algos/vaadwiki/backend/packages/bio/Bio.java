package it.algos.vaadwiki.backend.packages.bio;

import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.nazionalita.*;
import it.algos.vaadwiki.backend.packages.nome.*;
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
@AIEntity(recordName = "Bio", keyPropertyName = "pageId")
@AIView(menuName = "Bio", menuIcon = VaadinIcon.ASTERISK, searchProperty = "wikiTitle", sortProperty = "lastMongo")
@AIList(fields = "pageId,wikiTitle,valido,nome,nomeLink,cognome,giornoNato,annoNato,giornoMorto,annoMorto,attivita,nazionalita,lastServer,lastMongo", usaRowIndex = true)
@AIForm(fields = "pageId,wikiTitle,valido,nome,cognome,giornoNato,annoNato,giornoMorto,annoMorto,attivita,nazionalita,lastServer,lastMongo,tmplBio", operationForm = AEOperation.edit, usaSpostamentoTraSchede = false)
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
    @AIField(type = AETypeField.textArea, required = true, caption = "Template presente sul server", widthEM = 48, enabled = false)
    public String tmplBio;


    //--ultima modifica sul server wiki
    //--uso il formato Timestamp, per confrontarla col campo timestamp
    //--molto meglio che siano esattamente dello stesso tipo
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.localDateTime, caption = "Ultima modifica effettuata (non dal Bot) sul server", enabled = false)
    @AIColumn(typeData = AETypeData.normaleOrario)
    public LocalDateTime lastServer;


    //--ultima lettura/aggiornamento della voce, effettuata dal programma VaadBio
    //--uso il formato Timestamp, per confrontarla col campo timestamp
    //--molto meglio che siano esattamente dello stesso tipo
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.localDateTime, help = "ultima lettura/aggiornamento della voce effettuata dal programma VaadBio", enabled = false)
    public LocalDateTime lastMongo;


    /**
     * flag valido (facoltativo, di default false) <br>
     * valido se lastLettura >= lastModifica
     */
    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolField.checkBox, caption = "valido")
    @AIColumn(typeBool = AETypeBoolCol.checkIcon, header = "OK", widthEM = 5)
    public boolean valido;


    /**
     * nome (facoltativo, non unico)
     * riferimento dinamico CON @DBRef
     */
    @DBRef
    @AIField(type = AETypeField.link, comboClazz = NomeService.class, help = "Nome proprio")
    @AIColumn(header = "Nome", widthEM = 8)
    public Nome nome;


    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @Size(min = 2, max = 50)
    @AIField(type = AETypeField.text, required = false, widthEM = WIDTHEM)
    @AIColumn(widthEM = WIDTHEM)
    public String cognome;


    /**
     * giorno di nascita (facoltativo, non unica)
     * riferimento dinamico CON @DBRef
     */
    @DBRef
    @AIField(type = AETypeField.combo, comboClazz = GiornoService.class, help = "Giorno nato")
    @AIColumn(header = "Nato", widthEM = 8)
    public Giorno giornoNato;


    /**
     * anno di nascita (facoltativo, non unica)
     * riferimento dinamico CON @DBRef
     */
    @DBRef
    @AIField(type = AETypeField.combo, serviceClazz = AnnoService.class, help = "Anno nato")
    @AIColumn(header = "Nato", widthEM = 8)
    public Anno annoNato;

    /**
     * giorno di morte (facoltativo, non unica)
     * riferimento dinamico CON @DBRef
     */
    @DBRef
    @AIField(type = AETypeField.combo, comboClazz = GiornoService.class, help = "Giorno morto")
    @AIColumn(header = "Morto", widthEM = 8)
    public Giorno giornoMorto;

    /**
     * anno di norte (facoltativo, non unica)
     * riferimento dinamico CON @DBRef
     */
    @DBRef
    @AIField(type = AETypeField.combo, serviceClazz = AnnoService.class, help = "Anno morto")
    @AIColumn(header = "Morto", widthEM = 8)
    public Anno annoMorto;

    /**
     * attività principale (facoltativo, non unica)
     * riferimento dinamico CON @DBRef
     */
    @DBRef
    @AIField(type = AETypeField.combo, comboClazz = AttivitaService.class, help = "Attività")
    @AIColumn(header = "attivita", widthEM = 8)
    public Attivita attivita;

    /**
     * seconda attività (facoltativo, non unica)
     * riferimento dinamico CON @DBRef
     */
    @DBRef
    @AIField(type = AETypeField.combo, comboClazz = AttivitaService.class, help = "Attività2")
    @AIColumn(header = "attivita2", widthEM = 8)
    public Attivita attivita2;

    /**
     * terza attività (facoltativo, non unica)
     * riferimento dinamico CON @DBRef
     */
    @DBRef
    @AIField(type = AETypeField.combo, comboClazz = AttivitaService.class, help = "Attività3")
    @AIColumn(header = "attivita3", widthEM = 8)
    public Attivita attivita3;


    /**
     * nazionalità (facoltativo, non unica)
     * riferimento dinamico CON @DBRef
     */
    @DBRef
    @Field("naz")
    @AIField(type = AETypeField.combo, comboClazz = NazionalitaService.class, help = "Nazionalità")
    @AIColumn(header = "nazionalita", widthEM = 8)
    public Nazionalita nazionalita;

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return wikiTitle;
    }


}// end of entity class