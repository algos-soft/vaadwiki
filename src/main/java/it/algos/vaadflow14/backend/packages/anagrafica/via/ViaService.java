package it.algos.vaadflow14.backend.packages.anagrafica.via;

import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 08-mar-2021
 * Time: 11:43
 * <p>
 * Classe (facoltativa) di un package con personalizzazioni <br>
 * Se manca, si usa la classe EntityService <br>
 * Layer di collegamento tra il 'backend' e mongoDB <br>
 * Mantiene lo 'stato' della classe AEntity ma non mantiene lo stato di un'istanza entityBean <br>
 * L' istanza (SINGLETON) viene creata alla partenza del programma <br>
 * <p>
 * Annotated with @Service (obbligatorio) <br>
 * Annotated with @Scope (obbligatorio con SCOPE_SINGLETON) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovraScrivibile = true)
public class ViaService extends AService {


    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public ViaService() {
        super(Via.class);
    }


    /**
     * Crea e registra una entityBean solo se non esisteva <br>
     *
     * @param aeVia: enumeration per la creazione-reset di tutte le entities
     *
     * @return la nuova entityBean appena creata e salvata
     */
    private Via creaIfNotExist(final AEVia aeVia) {
        return creaIfNotExist(aeVia.getPos(), aeVia.toString());
    }


    /**
     * Crea e registra una entityBean solo se non esisteva <br>
     *
     * @param ordine di presentazione nel popup/combobox (obbligatorio, unico)
     * @param nome   completo (obbligatorio, unico)
     *
     * @return true se la nuova entityBean è stata creata e salvata
     */
    private Via creaIfNotExist(final int ordine, final String nome) {
        return (Via) checkAndSave(newEntity(ordine, nome));
    }


    /**
     * Crea e registra una entityBean solo se non esisteva <br>
     * Deve esistere la keyPropertyName della collezione, in modo da poter creare una nuova entityBean <br>
     * solo col valore di un parametro da usare anche come keyID <br>
     * Controlla che non esista già una entityBean con lo stesso keyID <br>
     * Deve esistere il metodo newEntity(keyPropertyValue) con un solo parametro <br>
     *
     * @param keyPropertyValue obbligatorio
     *
     * @return la nuova entityBean appena creata e salvata
     */
    public Via creaIfNotExist(final String keyPropertyValue) {
        return (Via) checkAndSave(newEntity(keyPropertyValue));
    }


    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    @Override
    public Via newEntity() {
        return newEntity(0, VUOTA);
    }


    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param nome completo (obbligatorio, unico)
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    private Via newEntity(final String nome) {
        return newEntity(0, nome);
    }

    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param ordine di presentazione nel popup/combobox (obbligatorio, unico)
     * @param nome   completo (obbligatorio, unico)
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    public Via newEntity(final int ordine, final String nome) {
        Via newEntityBean = Via.builderVia()
                .ordine(ordine > 0 ? ordine : this.getNewOrdine())
                .nome(text.isValid(nome) ? nome : null)
                .build();

        return (Via) fixKey(newEntityBean);
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param keyID must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public Via findById(final String keyID) {
        return (Via) super.findById(keyID);
    }


    /**
     * Retrieves an entity by its keyProperty.
     *
     * @param keyValue must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public Via findByKey(final String keyValue) {
        return (Via) super.findByKey(keyValue);
    }

    /**
     * Creazione o ricreazione di alcuni dati iniziali standard <br>
     * Invocato in fase di 'startup' e dal bottone Reset di alcune liste <br>
     * <p>
     * 1) deve esistere lo specifico metodo sovrascritto
     * 2) deve essere valida la entityClazz
     * 3) deve esistere la collezione su mongoDB
     * 4) la collezione non deve essere vuota
     * <p>
     * I dati possono essere: <br>
     * 1) recuperati da una Enumeration interna <br>
     * 2) letti da un file CSV esterno <br>
     * 3) letti da Wikipedia <br>
     * 4) creati direttamente <br>
     * DEVE essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @return wrapper col risultato ed eventuale messaggio di errore
     */
    @Override
    public AIResult resetEmptyOnly() {
        AIResult result = super.resetEmptyOnly();
        int numRec = 0;

        if (result.isErrato()) {
            return result;
        }

        for (AEVia aeVia : AEVia.values()) {
            numRec = creaIfNotExist(aeVia) != null ? numRec + 1 : numRec;
        }

        return super.fixPostReset(AETypeReset.enumeration, numRec);
    }

}// end of Singleton class