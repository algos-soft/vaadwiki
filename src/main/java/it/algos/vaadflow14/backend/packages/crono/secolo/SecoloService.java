package it.algos.vaadflow14.backend.packages.crono.secolo;

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
 * Date: mer, 23-dic-2020
 * Time: 06:58
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
@Qualifier("secoloService")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovraScrivibile = false)
public class SecoloService extends AService {


    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public SecoloService() {
        super(Secolo.class);
    }


    /**
     * Crea e registra una entity solo se non esisteva <br>
     *
     * @param aeSecolo: enumeration per la creazione-reset di tutte le entities
     *
     * @return la nuova entityBean appena creata e salvata
     */
    public Secolo creaIfNotExist(final AESecolo aeSecolo) {
        return creaIfNotExist(aeSecolo.getNome(), aeSecolo.isAnteCristo(), aeSecolo.getInizio(), aeSecolo.getFine());
    }


    /**
     * Crea e registra una entity solo se non esisteva <br>
     *
     * @param secolo     (obbligatorio, unico)
     * @param anteCristo flag per i secoli prima di cristo (obbligatorio)
     * @param inizio     (obbligatorio, unico)
     * @param fine       (obbligatorio, unico)
     *
     * @return la nuova entityBean appena creata e salvata
     */
    public Secolo creaIfNotExist(final String secolo, final boolean anteCristo, final int inizio, final int fine) {
        return (Secolo) checkAndSave(newEntity(secolo, anteCristo, inizio, fine));
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    @Override
    public Secolo newEntity() {
        return newEntity(VUOTA, false, 0, 0);
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param aeSecolo: enumeration per la creazione-reset di tutte le entities
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    public Secolo newEntity(final AESecolo aeSecolo) {
        return newEntity(aeSecolo.getNome(), aeSecolo.isAnteCristo(), aeSecolo.getInizio(), aeSecolo.getFine());
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param secolo     (obbligatorio, unico)
     * @param anteCristo flag per i secoli prima di cristo (obbligatorio)
     * @param inizio     (obbligatorio, unico)
     * @param fine       (obbligatorio, unico)
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Secolo newEntity(final String secolo, final boolean anteCristo, final int inizio, final int fine) {
        Secolo newEntityBean = Secolo.builderSecolo()
                .ordine(getNewOrdine())
                .secolo(text.isValid(secolo) ? secolo : null)
                .anteCristo(anteCristo)
                .inizio(inizio)
                .fine(fine)
                .build();

        return (Secolo) fixKey(newEntityBean);
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
    public Secolo findById(final String keyID) {
        return (Secolo) super.findById(keyID);
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
    public Secolo findByKey(final String keyValue) {
        return (Secolo) super.findByKey(keyValue);
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

        for (AESecolo eaSecolo : AESecolo.values()) {
            numRec = creaIfNotExist(eaSecolo) != null ? numRec + 1 : numRec;
        }

        return super.fixPostReset(AETypeReset.enumeration, numRec);
    }

}