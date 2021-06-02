package it.algos.vaadflow14.backend.packages.company;

import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * First time: lun, 21-dic-2020
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
@Qualifier("companyService")
//Spring
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//Algos
@AIScript(sovraScrivibile = false, doc = AEWizDoc.inizioRevisione)
public class CompanyService extends AService {


    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public CompanyService() {
        super(Company.class);
    }


    /**
     * Crea e registra una entityBean solo se non esisteva <br>
     * Deve esistere la keyPropertyName della collezione, in modo da poter creare una nuova entityBean <br>
     * solo col valore di un parametro da usare anche come keyID <br>
     * Controlla che non esista già una entityBean con lo stesso keyID <br>
     * Deve esistere il metodo newEntity(keyPropertyValue) con un solo parametro <br>
     *
     * @param code        obbligatorio
     * @param descrizione obbligatorio
     *
     * @return la nuova entityBean appena creata e salvata
     */
    private Company creaIfNotExist(final String code, final String descrizione) {
        return creaIfNotExist(code, descrizione, VUOTA, VUOTA);
    }


    /**
     * Crea e registra una entity solo se non esisteva <br>
     *
     * @param code        di riferimento
     * @param descrizione completa
     *
     * @return la nuova entity appena creata e salvata
     */
    private Company creaIfNotExist(final String code, final String descrizione, final String telefono, final String email) {
        return (Company) checkAndSave(newEntity(code, descrizione, telefono, email));
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Company newEntity() {
        return newEntity(VUOTA, VUOTA, VUOTA, VUOTA);
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param code        di riferimento
     * @param descrizione completa
     * @param telefono    fisso o cellulare
     * @param email       di posta elettronica
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Company newEntity(final String code, final String descrizione, final String telefono, final String email) {
        Company newEntityBean = Company.builderCompany()

                .code(text.isValid(code) ? code : null)

                .descrizione(text.isValid(descrizione) ? descrizione : null)

                .telefono(text.isValid(telefono) ? telefono : null)

                .email(text.isValid(email) ? email : null)

                .build();

        return (Company) fixKey(newEntityBean);
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
    public Company findById(final String keyID) {
        return (Company) super.findById(keyID);
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
    public Company findByKey(final String keyValue) {
        return (Company) super.findByKey(keyValue);
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

        numRec = creaIfNotExist("Algos", "Company Algos di prova", VUOTA, "info@algos.it") != null ? numRec + 1 : numRec;
        numRec = creaIfNotExist("Demo", "Company demo", "345 994487", "demo@algos.it") != null ? numRec + 1 : numRec;
        numRec = creaIfNotExist("Test", "Company di test", "", "presidentePonteTaro@crocerossa.it") != null ? numRec + 1 : numRec;

        return super.fixPostResetOnly(AETypeReset.hardCoded, numRec);
    }

    /**
     * Recupera dal db mongo la company (se esiste)
     */
    public Company getAlgos() {
        return findById(FlowCost.COMPANY_ALGOS);
    }


    /**
     * Recupera dal db mongo la company (se esiste)
     */
    public Company getDemo() {
        return findById(FlowCost.COMPANY_DEMO);
    }


    /**
     * Recupera dal db mongo la company (se esiste)
     */
    public Company getTest() {
        return findById(FlowCost.COMPANY_TEST);
    }

}