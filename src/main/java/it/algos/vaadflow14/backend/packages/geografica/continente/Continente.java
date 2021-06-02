package it.algos.vaadflow14.backend.packages.geografica.continente;

import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.packages.geografica.stato.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

import javax.validation.constraints.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * First time: mar, 29-set-2020
 * Last doc revision: mer, 19-mag-2021 alle 18:38 <br>
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
@Document(collection = "continente")
//Spring data
@TypeAlias("continente")
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderContinente")
@EqualsAndHashCode(callSuper = false)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.entity, doc = AEWizDoc.inizioRevisione)
@AIEntity(recordName = "Continente", keyPropertyName = "nome", usaBoot = true, usaNew = false)
@AIView(menuName = "Continente", menuIcon = VaadinIcon.GLOBE, sortProperty = "ordine")
@AIList(fields = "ordine,nome,abitato")
@AIForm(fields = "ordine,nome,stati,abitato", usaSpostamentoTraSchede = true)
public class Continente extends AREntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * ordine di presentazione per il popup (obbligatorio, unico) <br>
     */
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    @AIField(type = AETypeField.integer, typeNum = AETypeNum.positiviOnly)
    @AIColumn(header = "#", widthEM = 2)
    public int ordine;

    /**
     * nome (obbligatorio, unico)
     */
    @NotBlank()
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, required = true, focus = true)
    @AIColumn(widthEM = 14)
    public String nome;


    /**
     * esistono abitanti (facoltativa)
     */
    @AIField(type = AETypeField.booleano, caption = "Continente abitato")
    @AIColumn(header = "Ab.")
    public boolean abitato;

    /**
     * divisione amministrativa di secondo livello (facoltativa) <br>
     */
    @Transient()
    @AIField(type = AETypeField.gridShowOnly, caption = "stati presenti", linkClazz = Stato.class, linkProperty = "continente", properties = "stato,alfatre")
    public List<Stato> stati;

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getNome();
    }

}// end of Bean