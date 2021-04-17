package it.algos.vaadflow14.backend.packages.crono.mese;

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
 * Date: ven, 25-dic-2020
 * Time: 16:47
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
@Qualifier("meseService")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovraScrivibile = false)
public class MeseService extends AService {


    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public MeseService() {
        super(Mese.class);
    }


    /**
     * Crea e registra una entity solo se non esisteva <br>
     *
     * @param aeMese enumeration per la creazione-reset di tutte le entities
     *
     * @return la nuova entity appena creata e salvata
     */
    public Mese creaIfNotExist(final AEMese aeMese) {
        return creaIfNotExist(aeMese.getNome(), aeMese.getGiorni(), aeMese.getGiorniBisestili(), aeMese.getSigla());
    }


    /**
     * Crea e registra una entity solo se non esisteva <br>
     *
     * @param mese            nome completo (obbligatorio, unico)
     * @param giorni          numero di giorni presenti (obbligatorio)
     * @param giorniBisestile numero di giorni presenti in un anno bisestile (obbligatorio)
     * @param sigla           nome abbreviato di tre cifre (obbligatorio, unico)
     *
     * @return la nuova entity appena creata e salvata
     */
    public Mese creaIfNotExist(final String mese, final int giorni, final int giorniBisestile, final String sigla) {
        return (Mese) checkAndSave(newEntity(mese, giorni, giorniBisestile, sigla));
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Mese newEntity() {
        return newEntity(VUOTA, 0, 0, VUOTA);
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param aeMese: enumeration per la creazione-reset di tutte le entities
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Mese newEntity(final AEMese aeMese) {
        return newEntity(aeMese.getNome(), aeMese.getGiorni(), aeMese.getGiorniBisestili(), aeMese.getSigla());
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param mese            nome completo (obbligatorio, unico)
     * @param giorni          numero di giorni presenti (obbligatorio)
     * @param giorniBisestile numero di giorni presenti in un anno bisestile (obbligatorio)
     * @param sigla           nome abbreviato di tre cifre (obbligatorio, unico)
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Mese newEntity(final String mese, final int giorni, final int giorniBisestile, final String sigla) {
        Mese newEntityBean = Mese.builderMese()
                .ordine(getNewOrdine())
                .mese(text.isValid(mese) ? mese : null)
                .giorni(giorni)
                .giorniBisestile(giorniBisestile)
                .sigla(text.isValid(sigla) ? sigla : null)
                .build();

        return (Mese) fixKey(newEntityBean);
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
    public Mese findById(final String keyID) {
        return (Mese) super.findById(keyID);
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
    public Mese findByKey(final String keyValue) {
        return (Mese) super.findByKey(keyValue);
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

        for (AEMese aeMese : AEMese.values()) {
            numRec = creaIfNotExist(aeMese) != null ? numRec + 1 : numRec;
        }

        return super.fixPostReset(AETypeReset.hardCoded, numRec);
    }

}