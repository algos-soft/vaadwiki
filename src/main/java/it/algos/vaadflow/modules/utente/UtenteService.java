package it.algos.vaadflow.modules.utente;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.modules.role.Role;
import it.algos.vaadflow.modules.role.RoleService;
import it.algos.vaadflow.service.AService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_UTE;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 30-set-2018 16.14.56 <br>
 * <br>
 * Estende la classe astratta AService. Layer di collegamento per la Repository. <br>
 * <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Service (ridondante) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_UTE)
@Slf4j
@AIScript(sovrascrivibile = false)
public class UtenteService extends AService {

    private final static String SUFFIX = "123";
    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Service iniettato da Spring (@Scope = 'singleton'). Unica per tutta l'applicazione. Usata come libreria.
     */
    @Autowired
    public RoleService roleService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
//    @Autowired
//    private SecurityConfiguration securityConfiguration;

    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private UtenteRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public UtenteService(@Qualifier(TAG_UTE) MongoRepository repository) {
        super(repository);
        super.entityClass = Utente.class;
        this.repository = (UtenteRepository) repository;
    }// end of Spring constructor

    /**
     * Crea una entity e la registra <br>
     *
     * @param userName         userName o nickName (obbligatorio, unico)
     * @param passwordInChiaro password in chiaro (obbligatoria, non unica)
     *                         con inserimento automatico (prima del 'save') se è nulla
     * @param ruoli            Ruoli attribuiti a questo utente (lista di valori obbligatoria)
     *                         con inserimento del solo ruolo 'user' (prima del 'save') se la lista è nulla
     *                         lista modificabile solo da developer ed admin
     * @param mail             posta elettronica (facoltativo)
     * @param locked           flag locked (facoltativo, di default false)
     *
     * @return la entity appena creata
     */
    public Utente crea(String userName, String passwordInChiaro, List<Role> ruoli, String mail, boolean locked) {
        Utente entity;

        entity = newEntity(userName, passwordInChiaro, ruoli, mail, locked);
        save(entity);

        return entity;
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Utente newEntity() {
        return newEntity("", "", (List<Role>) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Properties obbligatorie <br>
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok) <br>
     *
     * @param userName         userName o nickName (obbligatorio, unico)
     * @param passwordInChiaro password in chiaro (obbligatoria, non unica)
     *                         con inserimento automatico (prima del 'save') se è nulla
     * @param ruoli            Ruoli attribuiti a questo utente (lista di valori obbligatoria)
     *                         con inserimento del solo ruolo 'user' (prima del 'save') se la lista è nulla
     *                         lista modificabile solo da developer ed admin
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Utente newEntity(String userName, String passwordInChiaro, List<Role> ruoli) {
        return newEntity(userName, passwordInChiaro, ruoli, "", false);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok) <br>
     *
     * @param userName         userName o nickName (obbligatorio, unico)
     * @param passwordInChiaro password in chiaro (obbligatoria, non unica)
     *                         con inserimento automatico (prima del 'save') se è nulla
     * @param ruoli            Ruoli attribuiti a questo utente (lista di valori obbligatoria)
     *                         con inserimento del solo ruolo 'user' (prima del 'save') se la lista è nulla
     *                         lista modificabile solo da developer ed admin
     * @param mail             posta elettronica (facoltativo)
     * @param locked           flag locked (facoltativo, di default false)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Utente newEntity(String userName, String passwordInChiaro, List<Role> ruoli, String mail, boolean locked) {
        Utente entity;

        entity = findByUserName(userName);
        if (entity != null) {
            return findByUserName(userName);
        }// end of if cycle

        entity = Utente.builderUtente()
                .userName(text.isValid(userName) ? userName : null)
                .passwordInChiaro(text.isValid(passwordInChiaro) ? passwordInChiaro : null)
                .ruoli(ruoli != null ? ruoli : roleService.getUserRole())
                .mail(text.isValid(mail) ? mail : null)
                .locked(locked)
                .build();

        return (Utente) creaIdKeySpecifica(entity);
    }// end of method


    /**
     * Operazioni eseguite PRIMA del save <br>
     * Regolazioni automatiche di property <br>
     *
     * @param entityBean da regolare prima del save
     * @param operation  del dialogo (NEW, EDIT)
     *
     * @return the modified entity
     */
    @Override
    public AEntity beforeSave(AEntity entityBean, AViewDialog.Operation operation) {
        Utente entity = (Utente) super.beforeSave(entityBean,operation);

        if (text.isEmpty(entity.userName)) {
            entity.id = FlowCost.STOP_SAVE;
            log.error("userName è vuoto in UtenteService.beforeSave()");
        }// end of if cycle

        if (text.isEmpty(entity.passwordInChiaro)) {
            entity.passwordInChiaro = entity.userName + SUFFIX;
        }// end of if cycle

        if (entity.ruoli == null) {
            entity.ruoli = roleService.getUserRole();
        }// end of if cycle

        return entity;
    }// end of method

    /**
     * Property unica (se esiste).
     */
    public String getPropertyUnica(AEntity entityBean) {
        return text.isValid(((Utente) entityBean).getUserName()) ? ((Utente) entityBean).getUserName() : "";
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param userName userName o nickName (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Utente findByUserName(String userName) {
        return repository.findByUserName(userName);
    }// end of method


    public boolean isUser(Utente utente) {
        for (Role role : utente.ruoli) {
            if (role.code.equals(roleService.getUser().code)) {
                return true;
            }// end of if cycle
        }// end of for cycle

        return false;
    }// end of method

    public boolean isAdmin(Utente utente) {
        for (Role role : utente.ruoli) {
            if (role.code.equals(roleService.getAdmin().code)) {
                return true;
            }// end of if cycle
        }// end of for cycle

        return false;
    }// end of method

    public boolean isDev(Utente utente) {
        for (Role role : utente.ruoli) {
            if (role.code.equals(roleService.getDeveloper().code)) {
                return true;
            }// end of if cycle
        }// end of for cycle

        return false;
    }// end of method

}// end of class