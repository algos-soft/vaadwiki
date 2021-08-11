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
@AIEntity(recordName = "Bio", keyPropertyName = "pageId")
@AIView(menuName = "Bio", menuIcon = VaadinIcon.ASTERISK, searchProperty = "wikiTitle", sortProperty = "lastMongo")
@AIList(fields = "pageId,wikiTitle,valido,nome,cognome,giornoNato,annoNato,luogoNato,giornoMorto,annoMorto,luogoMorto,attivita,nazionalita", usaRowIndex = true)
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

    private static final transient int WIDTHEM_TITLE = 18;

    private static final transient int WIDTHEM_NOME = 11;

    private static final transient int WIDTHEM_COGNOME = 12;

    private static final transient int WIDTH_GIORNO = 8;

    private static final transient int WIDTH_CITTA = 10;

    private static final transient int WIDTH_ANNO = 6;

    private static final transient int WIDTH_ATTNAZ = 9;

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
    @AIColumn(typeBool = AETypeBoolCol.checkIcon, header = "x", widthEM = 4)
    public boolean valido;

    /**
     * nome (facoltativo, non unico)
     */
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIColumn(widthEM = WIDTHEM_NOME)
    public String nome;

    /**
     * cognome (facoltativo, non unico)
     */
    @Indexed(direction = IndexDirection.DESCENDING)
    @Size(min = 2, max = 50)
    @AIColumn(widthEM = WIDTHEM_COGNOME)
    public String cognome;

    /**
     * sesso (facoltativo, non unico)
     * ammette solo M o F
     */
    @AIColumn(header = "X", widthEM = 2)
    public String sesso;

    /**
     * giorno di nascita (facoltativo, non unico)
     */
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIColumn(header = "Nascita", widthEM = WIDTH_GIORNO)
    public String giornoNato;

    /**
     * anno di nascita (facoltativo, non unico)
     */
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIColumn(header = "Anno", widthEM = WIDTH_ANNO)
    public String annoNato;

    @Indexed(direction = IndexDirection.DESCENDING)
    @AIColumn(header = "Nato", widthEM = WIDTH_CITTA)
    public String luogoNato;

    @Indexed(direction = IndexDirection.DESCENDING)
    @AIColumn(header = "NatoLink", widthEM = WIDTH_CITTA)
    public String luogoNatoLink;

    /**
     * giorno di morte (facoltativo, non unico)
     */
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIColumn(header = "Morte", widthEM = WIDTH_GIORNO)
    public String giornoMorto;

    /**
     * anno di norte (facoltativo, non unico)
     */
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIColumn(header = "Anno", widthEM = WIDTH_ANNO)
    public String annoMorto;

    @Indexed(direction = IndexDirection.DESCENDING)
    @AIColumn(header = "Morto", widthEM = WIDTH_CITTA)
    public String luogoMorto;

    @Indexed(direction = IndexDirection.DESCENDING)
    @AIColumn(header = "MortoLink", widthEM = WIDTH_CITTA)
    public String luogoMortoLink;

    /**
     * attività principale (facoltativa, non unica)
     */
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIColumn(widthEM = WIDTH_ATTNAZ)
    public String attivita;

    /**
     * seconda attività (facoltativa, non unica)
     */
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIColumn(widthEM = WIDTH_ATTNAZ)
    public String attivita2;

    /**
     * terza attività (facoltativa, non unica)
     */
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIColumn(widthEM = WIDTH_ATTNAZ)
    public String attivita3;


    /**
     * nazionalità (facoltativa, non unica)
     */
    @Indexed(direction = IndexDirection.DESCENDING)
    @AIColumn(widthEM = WIDTH_ATTNAZ)
    public String nazionalita;

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return wikiTitle;
    }


}// end of entity class