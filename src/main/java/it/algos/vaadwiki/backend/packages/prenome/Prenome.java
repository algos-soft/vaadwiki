package it.algos.vaadwiki.backend.packages.prenome;

import com.querydsl.core.annotations.QueryEntity;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.AEntity;
import it.algos.vaadflow14.backend.enumeration.AETypeField;
import it.algos.vaadflow14.backend.enumeration.AETypeNum;
import it.algos.vaadflow14.backend.enumeration.*;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import static it.algos.vaadflow14.backend.application.FlowCost.VUOTA;
import static java.awt.image.ImageObserver.WIDTH;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * Fix date: dom, 18-apr-2021 <br>
 * Fix time: 7:42 <br>
 * <p>
 * Classe (obbligatoria) di un package <br>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 * Le properties sono PUBLIC per poter usare la Reflection <br>
 * Unica classe obbligatoria per un package. <br>
 * Le altre servono solo se si vuole qualcosa in più dello standard minimo. <br>
 * <p>
 * Annotated with Spring: @SpringComponent (vaadin), @QueryEntity (querydsl), @Document (mongodb), @TypeAlias (data) <br>
 * Annotated with @SpringComponent, @QueryEntity, @Document, @TypeAlias <br>
 * Annotated with Lombok: @Data, @NoArgsConstructor, @AllArgsConstructor, @Builder, @EqualsAndHashCode <br>
 * Annotated with Algos: @AIScript per controllare la ri-creazione di questo file dal Wizard <br>
 * Annotated with Algos: @AIEntity per informazioni sulle property per il DB <br>
 * Annotated with Algos: @AIView per info su menu, icon, route, search e sort <br>
 * Annotated with Algos: @AIList per info sulla Grid e sulle colonne <br>
 * Annotated with Algos: @AIForm per info sul Form e sulle properties <br>
 */
@SpringComponent
@QueryEntity
@Document(collection = "prenome")
@TypeAlias("prenome")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderPrenome")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovraScrivibile = false)
@AIEntity(recordName = "Prenome", keyPropertyName = "code", usaCreazione = false, usaModifica = false, usaCompany = false)
@AIView(menuName = "Prenome", menuIcon = VaadinIcon.ASTERISK, searchProperty = "code", sortProperty = "code")
@AIList(fields = "code", usaRowIndex = true)
@AIForm(fields = "code", operationForm = AEOperation.showOnly, usaSpostamentoTraSchede = true)
public class Prenome extends AEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * larghezza delle colonne
     */
    private static final transient int WIDTHEM = 20;


    /**
     * code di riferimento (obbligatorio, unico) <br>
     */
    @NotBlank(message = "Il code è obbligatorio")
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @Size(min = 2, max = 50)
    @AIField(type = AETypeField.text, required = true, focus = true, caption = "code", widthEM = WIDTHEM)
    @AIColumn(header = "code", widthEM = WIDTHEM)
    public String code;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return code;
    }


}// end of entity class