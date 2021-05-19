package it.algos.vaadwiki.backend.packages.attivita;

import com.querydsl.core.annotations.QueryEntity;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.AEntity;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * First time: mer, 14-apr-2021 alle 17:58 <br>
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
@Document(collection = "attivita")
//Spring data
@TypeAlias("attivita")
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderAttivita")
@EqualsAndHashCode(callSuper = false)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.entity, doc = AEWizDoc.inizioRevisione)
@AIEntity(recordName = "Attivita", keyPropertyName = "singolare", usaCreazione = false, usaModifica = false, usaCompany = false)
@AIView(menuName = "Attivita", menuIcon = VaadinIcon.ASTERISK, searchProperty = "singolare", sortProperty = "singolare")
@AIList(fields = "singolare,plurale,aggiunta", usaRowIndex = true)//
@AIForm(fields = "singolare,plurale,aggiunta", operationForm = AEOperation.showOnly, usaSpostamentoTraSchede = true)
public class Attivita extends AEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * larghezza delle colonne
     */
    private static final transient int WIDTHEM = 20;


    /**
     * singolare di riferimento (obbligatorio, unico) <br>
     */
     @NotBlank(message = "Il singolare è obbligatorio")
     @Indexed(unique = false, direction = IndexDirection.DESCENDING)
     @Size(min = 2, max = 50)
     @AIField(type = AETypeField.text, firstCapital = true, focus = true, caption = "singolare", widthEM = WIDTHEM)
     @AIColumn(header = "singolare", widthEM = WIDTHEM)
     public String singolare;


     /**
     * plurale (facoltativo, non unico) <br>
     */
     @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @Size(min = 2, max = 50)
    @AIField(type = AETypeField.text, firstCapital = true, caption = "plurale", widthEM = WIDTHEM)
     @AIColumn(header = "plurale", widthEM = WIDTHEM)
    public String plurale;

     /**
     * flag aggiunta (facoltativo, di default false) <br>
     */
    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolField.checkBox, caption = "aggiunta")
    @AIColumn(typeBool = AETypeBoolCol.checkIcon, header = "Add")
    public boolean aggiunta;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return singolare;
    }


}// end of entity class