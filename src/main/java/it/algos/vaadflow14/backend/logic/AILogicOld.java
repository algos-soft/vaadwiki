package it.algos.vaadflow14.backend.logic;


import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.ui.button.*;
import it.algos.vaadflow14.ui.enumeration.*;
import it.algos.vaadflow14.ui.form.*;
import it.algos.vaadflow14.ui.header.*;
import it.algos.vaadflow14.ui.list.*;

import java.util.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: ven, 08-mag-2020
 * Time: 18:45
 * Interfaccia di collegamento tra backend e UI <br>
 * Contiene le API per fornire funzionalità alle Views ed altre classi <br>
 * L'implementazione astratta è ALogic <br>
 */
public interface AILogicOld {


    /**
     * Costruisce un (eventuale) layout per avvisi aggiuntivi in alertPlacehorder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Nell'implementazione standard di default NON presenta nessun avviso <br>
     * Recupera dal service specifico gli (eventuali) avvisi <br>
     * Costruisce un'istanza dedicata con le liste di avvisi <br>
     * Gli avvisi sono realizzati con tag html 'span' differenziati per colore anche in base all'utente collegato <br>
     * Se l'applicazione non usa security, il colore è deciso dal service specifico <br<
     * Se esiste, inserisce l'istanza (grafica) in alertPlacehorder della view <br>
     * alertPlacehorder viene sempre aggiunto, per poter (eventualmente) essere utilizzato dalle sottoclassi <br>
     *
     * @param typeVista in cui inserire gli avvisi
     *
     * @return componente grafico per il placeholder
     */
    AIHeader getAlertLayout(AEVista typeVista);

    /**
     * Costruisce un (eventuale) layout per avvisi aggiuntivi in alertPlacehorder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Normalmente ad uso esclusivo del developer <br>
     * Nell'implementazione standard di default NON presenta nessun avviso <br>
     * Recupera dal service specifico gli (eventuali) avvisi <br>
     * Costruisce un'istanza dedicata con le liste di avvisi <br>
     * Gli avvisi sono realizzati con label differenziate per colore in base all'utente collegato <br>
     * Se l'applicazione non usa security, il colore è unico <br<
     * Se esiste, inserisce l'istanza (grafica) in alertPlacehorder della view <br>
     * alertPlacehorder viene sempre aggiunto, per poter (eventualmente) essere utilizzato dalle sottoclassi <br>
     *
     * @param typeVista in cui inserire gli avvisi
     *
     * @return componente grafico per il placeHolder
     */
    AHeader getAlertHeaderLayout(AEVista typeVista);


    /**
     * Costruisce un layout per i bottoni di comando in topPlacehorder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * 1) Recupera dal service specifico una List<AEButton> di bottoni previsti <br>
     * Se List<AEButton> è vuota, ATopLayout usa i bottoni di default (solo New) <br>
     * 2) Recupera dal service specifico la condizione e la property previste (searchType,searchProperty) <br>
     * 3) Recupera dal service specifico una List<ComboBox> di popup di selezione e filtro <br>
     * Se List<ComboBox> è vuota, ATopLayout non usa popup <br>
     * Costruisce un'istanza dedicata con i bottoni, il campo textEdit di ricerca (eventuale) ed i comboBox (eventuali) <br>
     * Inserisce l'istanza (grafica) in topPlacehorder della view <br>
     *
     * @return componente grafico per il placeHolder
     */
    AButtonLayout getTopLayout();


    /**
     * Costruisce un layout per la Grid in bodyPlacehorder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Costruisce un'istanza dedicata <br>
     * Inserisce l'istanza (grafica) in bodyPlacehorder della view <br>
     *
     * @return componente grafico per il placeHolder
     */
    AGrid getBodyGridLayout();

    //    /**
    //     * Costruisce un layout per il Form in bodyPlacehorder della view <br>
    //     * <p>
    //     * Chiamato da AView.initView() <br>
    //     * Costruisce un'istanza dedicata <br>
    //     * Inserisce l'istanza (grafica) in bodyPlacehorder della view <br>
    //     *
    //     * @param entityClazz the class of type AEntity
    //     *
    //     * @return componente grafico per il placeHolder
    //     */
    //    AForm getBodyFormLayout(Class<? extends AEntity> entityClazz);


    /**
     * Costruisce un layout per il Form in bodyPlacehorder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Costruisce un'istanza dedicata <br>
     * Inserisce l'istanza (grafica) in bodyPlacehorder della view <br>
     *
     * @param entityBean interessata
     *
     * @return componente grafico per il placeHolder
     */
    AForm getBodyFormLayout(AEntity entityBean);


    /**
     * Costruisce un layout per i bottoni di comando in bottomPlacehorder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Recupera dal service specifico una List<AEButton> di bottoni previsti <br>
     * Se List<AEButton> è vuota, ATopLayout usa i bottoni di default (solo New) <br>
     * Costruisce un'istanza dedicata con i bottoni <br>
     * Inserisce l'istanza (grafica) in bottomPlacehorder della view <br>
     *
     * @return componente grafico per il placeHolder
     */
    ABottomLayout getBottomLayout(AEOperation operationForm);


    /**
     * Costruisce una lista di nomi delle properties del Form nell'ordine:
     * 1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxService
     *
     * @return lista di nomi delle properties da usare nel form
     */
    List<String> getListaPropertiesForm();

    //    /**
    //     * Costruisce una lista di nomi delle properties del Form, specializzata per una specifica operazione <br>
    //     * Di default utilizza la lista generale di getListaPropertiesForm() <br>
    //     * Sovrascritto nella sottoclasse concreta <br>
    //     *
    //     * @return lista di nomi delle properties da usare nel form
    //     */
    //    List<String> getListaPropertiesFormNew();


    /**
     * Costruisce una lista di nomi delle properties del Form, specializzata per una specifica operazione <br>
     * Di default utilizza la lista generale di getListaPropertiesForm() <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @return lista di nomi delle properties da usare nel form
     */
    List<String> getListaPropertiesFormEdit();

    //    /**
    //     * Costruisce una lista di nomi delle properties del Form, specializzata per una specifica operazione <br>
    //     * Di default utilizza la lista generale di getListaPropertiesForm() <br>
    //     * Sovrascritto nella sottoclasse concreta <br>
    //     *
    //     * @return lista di nomi delle properties da usare nel form
    //     */
    //    List<String> getListaPropertiesFormDelete();


    /**
     * Esegue l'azione del bottone, textEdit o comboBox. <br>
     *
     * @param azione selezionata da eseguire
     */
    public void performAction(AEAction azione);


    /**
     * Esegue l'azione del bottone, textEdit o comboBox. <br>
     * Invocata solo dalla Grid <br>
     *
     * @param azione           selezionata da eseguire
     * @param searchFieldValue valore corrente del campo editText (solo per List)
     */
    public void performAction(AEAction azione, String searchFieldValue);


    /**
     * Esegue l'azione del bottone, textEdit o comboBox. <br>
     * Invocata solo dal Form <br>
     *
     * @param azione     selezionata da eseguire
     * @param entityBean selezionata (solo per Form)
     */
    public void performAction(AEAction azione, AEntity entityBean);


    /**
     * Costruisce una lista di nomi delle properties della Grid nell'ordine: <br>
     * 1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive la lista nella sottoclasse specifica <br>
     *
     * @return lista di nomi di properties
     */
    List<String> getGridPropertyNamesList();


    /**
     * Costruisce una lista ordinata di nomi delle properties del Form. <br>
     * La lista viene usata per la costruzione automatica dei campi e l' inserimento nel binder <br>
     * Nell' ordine: <br>
     * 1) Cerca nell' annotation @AIForm della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxLogic <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * Se serve, modifica l' ordine della lista oppure esclude una property che non deve andare nel binder <br>
     * todo ancora da sviluppare
     *
     * @return lista di nomi di properties
     */
    List<String> getFormPropertyNamesList();

    //    /**
    //     * Crea e registra una entity solo se non esisteva <br>
    //     * Deve esistere la keyPropertyName della collezione, in modo da poter creare una nuova entity <br>
    //     * solo col valore di un parametro da usare anche come keyID <br>
    //     * Controlla che non esista già una entity con lo stesso keyID <br>
    //     * Deve esistere il metodo newEntity(keyPropertyValue) con un solo parametro <br>
    //     *
    //     * @param keyPropertyValue obbligatorio
    //     *
    //     * @return la nuova entity appena creata e salvata
    //     */
    //    public Object creaIfNotExist(String keyPropertyValue);


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    AEntity newEntity();


    /**
     * Ordine di presentazione (facoltativo) <br>
     * Viene calcolato in automatico alla creazione della entity <br>
     * Recupera dal DB il valore massimo pre-esistente della property <br>
     * Incrementa di uno il risultato <br>
     *
     * @return ordine di presentazione per la nuova entity
     */
    int getNewOrdine();


    /**
     * Retrieves an entity by its id.
     *
     * @param keyID must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    AEntity findById(String keyID);


    /**
     * Retrieves an entity by its keyProperty.
     *
     * @param keyPropertyValue must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    AEntity findByKey(final String keyPropertyValue);

}
