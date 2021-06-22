package it.algos.vaadflow14.backend.entity;

import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.enumeration.*;
import lombok.*;
import org.springframework.data.mongodb.core.index.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 30-mag-2021
 * Time: 20:38
 */
@Setter
@Getter
public abstract class AREntity extends AEntity {

    /**
     * flag per le collezioni che usano 'reset' e 'leggono' i dati
     * true se la entities viene costruita dal programma (all'avvio o dal bottone reset)
     * false se viene inserita direttamente dall'utente
     * di default reset=false
     */
    @Indexed()
    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolField.checkBox)
    @AIColumn(typeBool = AETypeBoolCol.checkBox, header = "R.", widthEM = 5)
    public boolean reset;


}
