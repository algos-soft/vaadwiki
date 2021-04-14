package it.algos.vaadwiki.backend.packages.genere;

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
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * Fix date: mer, 14-apr-2021 <br>
 * Fix time: 9:14 <br>
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
@Document(collection = "genere")
@TypeAlias("genere")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderGenere")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovraScrivibile = false)
@AIEntity(recordName = "Genere", keyPropertyName = "singolare", usaCompany = false)
@AIView(menuName = "Genere", menuIcon = VaadinIcon.ASTERISK, searchProperty = "singolare", sortProperty = "singolare")
@AIList(fields = "singolare,pluraleMaschile,pluraleFemminile", usaRowIndex = true)
@AIForm(fields = "singolare,pluraleMaschile,pluraleFemminile", usaSpostamentoTraSchede = true)
public class Genere extends AEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * larghezza delle colonne
     */
    private static final transient int WIDTHEM = 20;

    /**
     * singolare maschile e femminile (obbligatorio ed unico)
     */
    @NotBlank(message = "Il singolare è obbligatorio")
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @Size(min = 2, max = 50)
    @AIField(type = AETypeField.text, required = true, focus = true, caption = "singolare", widthEM = WIDTHEM)
    @AIColumn(header = "singolare", widthEM = WIDTHEM)
    public String singolare;

    /**
     * pluraleMaschile (facoltativo, non unico) <br>
     */
    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @Size(min = 2, max = 50)
    @AIField(type = AETypeField.text, firstCapital = true, caption = "plurale maschile", widthEM = WIDTHEM)
    @AIColumn(header = "plurale maschile", widthEM = WIDTHEM)
    public String pluraleMaschile;


    /**
     * pluraleMaschile (facoltativo, non unico) <br>
     */
    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @Size(min = 3, max = 50)
    @AIField(type = AETypeField.text, firstCapital = true, caption = "plurale maschile", widthEM = WIDTHEM)
    @AIColumn(header = "plurale maschile", widthEM = WIDTHEM)
    public String pluraleFemminile;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return singolare;
    }


}// end of entity class