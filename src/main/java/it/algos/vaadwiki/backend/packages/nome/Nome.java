package it.algos.vaadwiki.backend.packages.nome;

import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import static java.awt.image.ImageObserver.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

import javax.validation.constraints.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * First time: mer, 4-ago-2021 alle 15:13 <br>
 * Last doc revision: mer, 4-ago-2021 alle 15:13 <br>
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
@Document(collection = "nome")
//Spring data
@TypeAlias("nome")
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderNome")
@EqualsAndHashCode(callSuper = false)
//Algos
@AIScript(sovraScrivibile = false, doc = AEWizDoc.inizioRevisione)
@AIEntity(recordName = "Nome", keyPropertyName = "nome", usaBoot = false, usaNew = true, usaCompany = false)
@AIView(menuName = "Nome", menuIcon = VaadinIcon.ASTERISK, searchProperty = "nome", sortProperty = "voci", sortDirection = SORT_SPRING_DESC)
@AIList(fields = "nome,valido,voci", usaRowIndex = false)
@AIForm(fields = "voci,nome,valido", operationForm = AEOperation.showOnly, usaSpostamentoTraSchede = false)
public class Nome extends AEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * voci (obbligatorio, unico) <br>
     */
    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.integer, caption = "voci", typeNum = AETypeNum.positiviOnly, widthEM = 4)
    @AIColumn(header = "Voci", widthEM = 5)
    public int voci;

    /**
     * nome di riferimento (obbligatorio, unico) <br>
     * se in Bio il riferimento è @DBRef, unique = true
     * se in Bio il riferimento NON è @DBRef, unique = false
     */
    @NotBlank(message = "Il nome è obbligatorio")
    @Indexed(unique = false, direction = IndexDirection.ASCENDING)
    @Size(min = 2, max = 50)
    @AIField(type = AETypeField.text, required = true, focus = true, caption = "nome", widthEM = 20)
    @AIColumn(header = "nome", widthEM = 20)
    public String nome;


    /**
     * flag valido (facoltativo, di default false) <br>
     */
    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolField.checkBox, caption = "valido")
    @AIColumn(typeBool = AETypeBoolCol.checkIcon, header = "OK", widthEM = WIDTH)
    public boolean valido;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return nome;
    }


}// end of entity class