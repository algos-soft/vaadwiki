package it.algos.vaadflow14.backend.logic;

import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.interfaces.*;

import java.io.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 27-feb-2021
 * Time: 21:25
 */
public interface AILogic {


    /**
     * Costruisce una lista ordinata di colonne della Grid. <br>
     * Nell' ordine: <br>
     * 1) Cerca nell' annotation @AIList della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive la lista nella sottoclasse specifica xxxLogicList <br>
     * Può essere sovrascritto senza invocare il metodo della superclasse <br>
     *
     * @return lista dei nomi delle colonne
     */
    List<String> getGridColumns();


    /**
     * Costruisce una lista ordinata di nomi delle properties del Form. <br>
     * La lista viene usata per la costruzione automatica dei campi e l' inserimento nel binder <br>
     * Nell' ordine: <br>
     * 1) Cerca nell' annotation @AIForm della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxLogic <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * Se serve, modifica l' ordine della lista oppure esclude una property che non deve andare nel binder <br>
     *
     * @return lista di nomi di properties
     */
    List<String> getFormPropertyNamesList();


    /**
     * Esegue l'azione del bottone. Azione che non necessita di parametri. <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @param iAzione interfaccia dell'azione selezionata da eseguire
     *
     * @return false se il parametro non è una enumeration valida o manca lo switch
     */
    boolean performAction(final AIAction iAzione);


    /**
     * Esegue l'azione del bottone. Azione che necessita di una entityBean. <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @param iAzione    interfaccia dell'azione selezionata da eseguire
     * @param entityBean selezionata
     *
     * @return false se il parametro iAzione non è una enumeration valida o manca lo switch
     */
    boolean performAction(final AIAction iAzione, final AEntity entityBean);


    /**
     * Esegue l'azione del bottone. Azione che necessita di una stringa. <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @param iAzione          interfaccia dell'azione selezionata da eseguire
     * @param searchFieldValue valore corrente del campo searchField (solo per List)
     *
     * @return false se il parametro iAzione non è una enumeration valida o manca lo switch
     */
    boolean performAction(final AIAction iAzione, final String searchFieldValue);

    /**
     * Esegue l'azione del bottone. Azione che necessita di un field e di un valore. <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @param iAzione    interfaccia dell'azione selezionata da eseguire
     * @param fieldName  nome del field
     * @param fieldValue valore corrente del field
     *
     * @return false se il parametro iAzione non è una enumeration valida o manca lo switch
     */
    boolean performAction(final AIAction iAzione, final String fieldName, Object fieldValue);


    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto <br>
     *
     * @return true se l'azione è stata eseguita
     */
    boolean download();


    /**
     * The entityService obbligatorio, singleton di tipo xxxService che implementa l'interfaccia AIService <br>
     * È il riferimento al service specifico correlato a questa istanza (prototype) di LogicList/FormList <br>
     */
    AIService getEntityService();

}
