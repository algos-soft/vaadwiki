package it.algos.vaadflow14.backend.packages.security.utente;

import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.company.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

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
@Qualifier("security/utenteService")
//Spring
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//Algos
@AIScript(sovraScrivibile = false, doc = AEWizDoc.inizioRevisione)
public class UtenteService extends AService {


    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin nel costruttore <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    CompanyService companyService;

    /**
     * Costruttore @Autowired. <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore <br>
     * Se ci sono DUE o più costruttori, va in errore <br>
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public UtenteService(final CompanyService companyService) {
        super(Utente.class);
        this.companyService = companyService;
    }


    /**
     * Crea e registra una entity solo se non esisteva <br>
     *
     * @param username o nickName
     * @param password in chiaro
     *
     * @return la nuova entity appena creata e salvata
     */
    public Utente creaIfNotExist(final String username, final String password) {
        return (Utente) checkAndSave(newEntity(username, password, (AERole) null));
    }


    /**
     * Crea e registra una entity solo se non esisteva <br>
     * Può forzare una company DIVERSA da quella corrente usata da newEntity() <br>
     *
     * @param company  obbligatoria se FlowVar.usaCompany=true
     * @param username o nickName
     * @param password in chiaro
     * @param role     authority per il login
     *
     * @return la nuova entity appena creata e salvata
     */
    public Utente creaIfNotExist(final Company company, final String username, final String password, final AERole role) {
        Utente entity = newEntity(username, password, role);
        entity.company = company;
        return (Utente) checkAndSave(entity);
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public AEntity newEntity() {
        return newEntity(VUOTA, VUOTA, (AERole) null);
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * <p>
     *
     * @param company  obbligatoria se FlowVar.usaCompany=true
     * @param username o nickName
     * @param password in chiaro
     * @param role     authority per il login
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Utente newEntity(final String username, final String password, final AERole role) {
        Utente newEntityBean = Utente.builderUtente()
                .username(text.isValid(username) ? username : null)
                .password(text.isValid(password) ? password : null)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .role(role != null ? role : AERole.user)
                .build();

        return (Utente) fixKey(newEntityBean);
    }

    /**
     * Operazioni eseguite PRIMA di save o di insert <br>
     * Regolazioni automatiche di property <br>
     * Controllo della validità delle properties obbligatorie <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @param entityBean da regolare prima del save
     * @param operation  del dialogo (NEW, Edit)
     *
     * @return the modified entity
     */
    //    @Override
    public Utente beforeSave(final AEntity entityBean, final AEOperation operation) {
        Utente entity = (Utente) super.beforeSave(entityBean, operation);

        if (entity != null && entity.username != null) {
            entity.username = text.levaSpazi(entity.username);
        }

        return entity;
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
    public Utente findById(final String keyID) throws AlgosException {
        return (Utente) super.findById(keyID);
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
    public Utente findByKey(final String keyValue) throws AlgosException {
        return (Utente) super.findByKey(keyValue);
    }

    /**
     * Retrieves an entity by userName.
     *
     * @param userName must not be {@literal null}.
     */
    public Utente findByUser(final String userName) {
        return (Utente) ((MongoService) mongo).findOneUnique(Utente.class, MONGO_FIELD_USER, userName);//@todo da controllare
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
        AIResult result = null;
        //        AIResult result = super.resetEmptyOnly();
        int numRec = 0;

        if (result.isErrato()) {
            return result;
        }

        if (companyService == null) {
            companyService = appContext.getBean(CompanyService.class);
        }

        numRec = creaIfNotExist(companyService.getAlgos(), "Gac", "fulvia", AERole.developer) != null ? numRec + 1 : numRec;
        numRec = creaIfNotExist(companyService.getDemo(), "mario_rossi", "rossi123", AERole.admin) != null ? numRec + 1 : numRec;
        numRec = creaIfNotExist(null, "marco.beretta", "beretta123", AERole.admin) != null ? numRec + 1 : numRec;
        numRec = creaIfNotExist(companyService.getTest(), "antonia-pellegrini", "pellegrini123", AERole.user) != null ? numRec + 1 : numRec;
        numRec = creaIfNotExist(null, "paolo cremona", "cremona123", AERole.guest) != null ? numRec + 1 : numRec;

        return result;
    }

}