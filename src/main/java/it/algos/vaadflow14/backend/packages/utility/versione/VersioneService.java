package it.algos.vaadflow14.backend.packages.utility.versione;

import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.time.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * First time: ven, 25-dic-2020
 * Last doc revision: mer, 19-mag-2021 alle 18:38 <br>
 * <p>
 * Classe (facoltativa) di un package con personalizzazioni <br>
 * Se manca, usa la classe EntityService <br>
 * Layer di collegamento tra il 'backend' e mongoDB <br>
 * Mantiene lo 'stato' della classe AEntity ma non mantiene lo stato di un'istanza entityBean <br>
 * L' istanza (SINGLETON) viene creata alla partenza del programma <br>
 * <p>
 * Annotated with @Service (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per iniettare questo singleton nel costruttore di xxxLogicList <br>
 * Annotated with @Scope (obbligatorio con SCOPE_SINGLETON) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
//Spring
@Service
//Spring
@Qualifier("utility/versioneService")
//Spring
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//Algos
@AIScript(sovraScrivibile = false, doc = AEWizDoc.inizioRevisione)
public class VersioneService extends AService {


    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public VersioneService() {
        super(Versione.class);
    }


    /**
     * Crea e registra una entity solo se non esisteva <br>
     *
     * @param code        di riferimento
     * @param giorno      di ricompilazione del programma
     * @param descrizione completa
     *
     * @return la nuova entity appena creata e salvata
     */
    public Versione creaIfNotExist(final String code, final LocalDate giorno, final String descrizione) {
        return (Versione) checkAndSave(newEntity(code, giorno, descrizione));
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Versione newEntity() {
        return newEntity(VUOTA, null, VUOTA);
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param code        di riferimento
     * @param giorno      di ricompilazione del programma
     * @param descrizione completa
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Versione newEntity(final String code, final LocalDate giorno, final String descrizione) {
        Versione newEntityBean = Versione.builderVersione()
                .code(text.isValid(code) ? code : null)
                .giorno(giorno != giorno ? giorno : LocalDate.now())
                .descrizione(text.isValid(descrizione) ? descrizione : null)
                .build();

        return (Versione) fixKey(newEntityBean);
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
    public Versione findById(final String keyID) {
        return (Versione) super.findById(keyID);
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
    public Versione findByKey(final String keyValue) {
        return (Versione) super.findByKey(keyValue);
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
    //    @Override
    public AIResult resetEmptyOnly() {
        AIResult result=null;
        //        AIResult result = super.resetEmptyOnly();
        int numRec = 0;

        if (result.isErrato()) {
            return result;
        }

        numRec = creaIfNotExist("Setup", LocalDate.now(), "Installazione iniziale di " + FlowVar.projectNameUpper) != null ? numRec + 1 : numRec;

        return super.fixPostResetOnly(AETypeReset.hardCoded, numRec);
    }

}