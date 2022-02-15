package it.algos.vaadflow14.backend.packages.utility.versione;

import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.company.*;
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
     * @param type        categorizzazione dell'intervento
     * @param titolo      breve
     * @param company     interessata alla versione (algos per tutte)
     * @param descrizione testuale completa
     * @param vaadFlow    versione del progetto base vs progetto specifico
     * @param usaCompany  versione specifica per una sola company vs tutte le companies
     *
     * @return la nuova entity appena creata e salvata
     */
    public Versione creaIfNotExist(final AETypeVers type, final String titolo, final Company company, final String descrizione, final boolean vaadFlow, final boolean usaCompany) {
        return (Versione) checkAndSave(newEntity(type, titolo, company, descrizione, vaadFlow, usaCompany));
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Versione newEntity() {
        return newEntity(null, VUOTA, null, VUOTA, true, false);
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param type        categorizzazione dell'intervento
     * @param titolo      breve
     * @param company     interessata alla versione (algos per tutte)
     * @param descrizione testuale completa
     * @param vaadFlow    versione del progetto base vs progetto specifico
     * @param usaCompany  versione specifica per una sola company vs tutte le companies
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Versione newEntity(final AETypeVers type, final String titolo, final Company company, final String descrizione, final boolean vaadFlow, final boolean usaCompany) {

        Versione newEntityBean =
                Versione.builderVersione()
                        .type(type)
                        .release(vaadFlow ? FlowVar.flowVersion : FlowVar.projectVersion)
                        .giorno(LocalDate.now())
                        .titolo(text.isValid(titolo) ? titolo : null)
                        .descrizione(text.isValid(descrizione) ? descrizione : null)
                        .vaadFlow(vaadFlow)
                        .usaCompany(FlowVar.usaCompany ? usaCompany : false)
                        .build();
        newEntityBean.company = company != null ? company : getCompanyBase();

        return (Versione) fixKey(newEntityBean);
    }

    public Company getCompanyBase() {
        return null;
    }

    /**
     * Regola la chiave se esiste il campo keyPropertyName. <br>
     * Se la company è nulla, la recupera dal login <br>
     * Se la company è ancora nulla, la entity viene creata comunque
     * ma verrà controllata ancora nel metodo beforeSave() <br>
     *
     * @param newEntityBean to be checked
     *
     * @return the checked entity
     */
    @Override
    public AEntity fixKey(AEntity newEntityBean) {
        Versione versione = (Versione) newEntityBean;

        if (text.isEmpty(newEntityBean.id)) {
            newEntityBean.id = versione.titolo + versione.descrizione;
        }

        return newEntityBean;
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
    public Versione findById(final String keyID) throws AlgosException {
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
    public Versione findByKey(final String keyValue) throws AlgosException {
        return (Versione) super.findByKey(keyValue);
    }

    //    /**
    //     * Creazione o ricreazione di alcuni dati iniziali standard <br>
    //     * Invocato in fase di 'startup' e dal bottone Reset di alcune liste <br>
    //     * <p>
    //     * 1) deve esistere lo specifico metodo sovrascritto
    //     * 2) deve essere valida la entityClazz
    //     * 3) deve esistere la collezione su mongoDB
    //     * 4) la collezione non deve essere vuota
    //     * <p>
    //     * I dati possono essere: <br>
    //     * 1) recuperati da una Enumeration interna <br>
    //     * 2) letti da un file CSV esterno <br>
    //     * 3) letti da Wikipedia <br>
    //     * 4) creati direttamente <br>
    //     * DEVE essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
    //     *
    //     * @return wrapper col risultato ed eventuale messaggio di errore
    //     */
    //    //    @Override
    //    public AIResult resetEmptyOnly() {
    //        AIResult result = null;
    //        //        AIResult result = super.resetEmptyOnly();
    //        int numRec = 0;
    //
    //        if (result.isErrato()) {
    //            return result;
    //        }
    //
    //        numRec = creaIfNotExist("Setup", LocalDate.now(), "Installazione iniziale di " + FlowVar.projectNameUpper) != null ? numRec + 1 : numRec;
    //
    //        return result;
    //    }

}