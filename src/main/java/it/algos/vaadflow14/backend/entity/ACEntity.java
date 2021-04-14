package it.algos.vaadflow14.backend.entity;

import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.packages.company.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 25-ago-2020
 * Time: 11:26
 * <p>
 * Superclasse di tutte le entities che usano la company <br>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class ACEntity extends AEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Riferimento dinamico alla company con @DBRef (per le sottoclassi che usano questa classe)
     * - Nullo se FlowVar.usaCompany=false
     * - Facoltativo od obbligatorio a seconda della sottoclasse, se FlowVar.usaCompany=true
     */
    @DBRef
    @AIField(type = AETypeField.combo, comboClazz = Company.class, logicClazz = CompanyService.class)
    @AIColumn()
    public Company company;


}