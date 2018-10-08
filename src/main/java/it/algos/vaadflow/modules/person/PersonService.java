package it.algos.vaadflow.modules.person;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.modules.address.Address;
import it.algos.vaadflow.modules.address.AddressService;
import it.algos.vaadflow.modules.address.EAAddress;
import it.algos.vaadflow.modules.preferenza.EAPreferenza;
import it.algos.vaadflow.modules.role.Role;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadflow.service.AService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_PER;

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

/**
 * La newEntity() usa il metodo newEntity() della superclasse per usare 'builderUtente' <br>
 */
@SpringComponent
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_PER)
@Slf4j
@AIScript(sovrascrivibile = false)
public class PersonService extends AService {

    public final static List<String> PROPERTIES_SECURED =
            Arrays.asList("userName", "passwordInChiaro", "locked", "nome", "cognome", "telefono", "mail", "indirizzo");
    public final static List<String> PROPERTIES_NOT_SECURED =
            Arrays.asList("nome", "cognome", "telefono", "mail", "indirizzo");

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected UtenteService utenteService;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected AddressService addressService;

    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private PersonRepository repository;


    /**
     * Costruttore <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public PersonService(@Qualifier(TAG_PER) MongoRepository repository) {
        super(repository);
        super.entityClass = Person.class;
        this.repository = (PersonRepository) repository;
    }// end of Spring constructor

    /**
     * Crea una entity <br>
     *
     * @param nome:      (obbligatorio, non unico)
     * @param cognome:   (obbligatorio, non unico)
     * @param telefono:  (facoltativo)
     * @param indirizzo: via, nome e numero (facoltativo)
     *
     * @return la entity trovata o appena creata
     */
    public Person crea(String nome, String cognome, String telefono, Address indirizzo) {
        return crea(nome, cognome, telefono, indirizzo, "", "", (List<Role>) null, "", false, false);
    }// end of method

    /**
     * Crea una entity <br>
     * Se esiste già, la cancella prima di ricrearla <br>
     *
     * @param nome:            (obbligatorio, non unico)
     * @param cognome:         (obbligatorio, non unico)
     * @param telefono:        (facoltativo)
     * @param indirizzo:       via, nome e numero (facoltativo)
     * @param userName         userName o nickName (obbligatorio, unico)
     * @param passwordInChiaro password in chiaro (obbligatoria, non unica)
     *                         con inserimento automatico (prima del 'save') se è nulla
     * @param ruoli            Ruoli attribuiti a questo utente (lista di valori obbligatoria)
     *                         con inserimento del solo ruolo 'user' (prima del 'save') se la lista è nulla
     *                         lista modificabile solo da developer ed admin
     * @param mail             posta elettronica (facoltativo)
     * @param locked           flag locked (facoltativo, di default false)
     * @param usaSuperClasse   (transient) per utilizzare le properties di Security della superclasse Utente (facoltativo)
     *
     * @return la entity trovata o appena creata
     */
    public Person crea(
            String nome,
            String cognome,
            String telefono,
            Address indirizzo,
            String userName,
            String passwordInChiaro,
            List<Role> ruoli,
            String mail,
            boolean locked,
            boolean usaSuperClasse) {
        return (Person) save(newEntity(nome, cognome, telefono, indirizzo, userName, passwordInChiaro, ruoli, mail, locked, usaSuperClasse));
    }// end of method

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Person newEntity() {
        return newEntity("", "");
    }// end of method

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Non usa le properties di security della superclasse Utente
     * Usato come record 'embedded' in altre classi (Company,...)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Person newEntityNoSuperclasse() {
        return newEntity("", "", "", (Address) null, "", "", (List<Role>) null, "", false, false);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param nome:    obbligatorio
     * @param cognome: obbligatorio
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Person newEntity(String nome, String cognome) {
        return newEntity(nome, cognome, "", (Address) null);
    }// end of method

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param nome:      (obbligatorio, non unico)
     * @param cognome:   (obbligatorio, non unico)
     * @param telefono:  (facoltativo)
     * @param indirizzo: via, nome e numero (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Person newEntity(String nome, String cognome, String telefono, Address indirizzo) {
        return newEntity(nome, cognome, telefono, indirizzo, "", "", (List<Role>) null, "", false, true);
    }// end of method

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok) <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param nome:            (obbligatorio, non unico)
     * @param cognome:         (obbligatorio, non unico)
     * @param telefono:        (facoltativo)
     * @param indirizzo:       via, nome e numero (facoltativo)
     * @param userName         userName o nickName (obbligatorio, unico)
     * @param passwordInChiaro password in chiaro (obbligatoria, non unica)
     *                         con inserimento automatico (prima del 'save') se è nulla
     * @param ruoli            Ruoli attribuiti a questo utente (lista di valori obbligatoria)
     *                         con inserimento del solo ruolo 'user' (prima del 'save') se la lista è nulla
     *                         lista modificabile solo da developer ed admin
     * @param mail             posta elettronica (facoltativo)
     * @param locked           flag locked (facoltativo, di default false)
     * @param usaSuperClasse   (transient) per utilizzare le properties di Security della superclasse Utente (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Person newEntity(
            String nome,
            String cognome,
            String telefono,
            Address indirizzo,
            String userName,
            String passwordInChiaro,
            List<Role> ruoli,
            String mail,
            boolean locked,
            boolean usaSuperClasse) {
        Person entity = null;
        Utente entityDellaSuperClasseUtente = null;

        //--controlla il flag passato come parametro e specifico di questa entity (entity embedded non usano Utente)
        //--controlla il flag generale dell'applicazione
        //--se usa la security, la persona eredità tutte le property della superclasse Utente
        //--prima viene creata una entity di Utente, usando le regolazioni automatiche di quella superclasse.
        //--poi vengono ricopiati i valori in Persona
        //--poi vengono aggiunte le property specifiche di Persona
        //--se non usa la security, utilizza il metodo builderPerson
        if (usaSuperClasse && pref.isBool(EAPreferenza.usaSecurity.getCode())) {
            //--prima viene creata una entity di Utente, usando le regolazioni automatiche di quella superclasse.
            entityDellaSuperClasseUtente = utenteService.newEntity(userName, passwordInChiaro, ruoli, mail, locked);

            //--poi vengono ricopiati i valori in Persona
            //--casting dalla superclasse alla classe attuale
            entity = (Person) super.cast(entityDellaSuperClasseUtente, new Person());
            entity.usaSuperClasse = true;
        } else {
            entity = Person.builderPerson().build();
            entity.usaSuperClasse = false;
        }// end of if/else cycle

        //--poi vengono aggiunte le property specifiche di Persona
        //--regola le property di questa classe
        entity.setNome(text.isValid(nome) ? nome : null);
        entity.setCognome(text.isValid(cognome) ? cognome : null);
        entity.setTelefono(text.isValid(telefono) ? telefono : null);
        entity.setIndirizzo(indirizzo);

        return (Person) creaIdKeySpecifica(entity);
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
        if (((Person) entityBean).usaSuperClasse) {
            entityBean = utenteService.beforeSave(entityBean, operation);
        }// end of if cycle
        Person entity = (Person) super.beforeSave(entityBean, operation);

        if (entity == null) {
            log.error("entity è nullo in PersonService.beforeSave()");
            return null;
        }// end of if cycle

        if (text.isValid(entity.nome)) {
            entity.nome = text.primaMaiuscola(entity.nome);
        }// end of if cycle

        if (text.isValid(entity.cognome)) {
            entity.cognome = text.primaMaiuscola(entity.cognome);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Property unica (se esiste).
     */
    public String getPropertyUnica(AEntity entityBean) {
        Person persona = (Person) entityBean;
        String nome = persona.nome != null ? persona.nome : "";
        String cognome = persona.cognome != null ? persona.cognome : "";

        return pref.isBool(FlowCost.USA_SECURITY) ? utenteService.getPropertyUnica(entityBean) : nome + cognome;
    }// end of method


    /**
     * Crea una entity <br>
     *
     * @param eaPerson: enumeration di dati iniziali di prova
     *
     * @return la entity trovata o appena creata
     */
    public Person crea(EAPerson eaPerson) {
        return (Person) save(newEntity(eaPerson));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata, per essere usata anche embedded <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param eaPerson: enumeration di dati iniziali di prova
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Person newEntity(EAPerson eaPerson) {
        String nome;
        String cognome;
        String telefono;
        EAAddress eaAddress;
        Address indirizzo;
        String mail;
        String userName;

        if (eaPerson != null) {
            nome = eaPerson.getNome();
            cognome = eaPerson.getCognome();
            telefono = eaPerson.getTelefono();
            eaAddress = eaPerson.getAddress();
            indirizzo = addressService.newEntity(eaAddress);
            mail = eaPerson.getMail();
            userName = eaPerson.getUserName();

            if (pref.isBool(EAPreferenza.usaCompany.getCode())) {
                return newEntity(nome, cognome, telefono, indirizzo, userName, "", (List<Role>) null, mail, false, false);
            } else {
                return newEntity(nome, cognome, telefono, indirizzo);
            }// end of if/else cycle

        } else {
            return null;
        }// end of if/else cycle
    }// end of method

    /**
     * Costruisce una lista di nomi delle properties della Grid nell'ordine:
     * 1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getGridPropertyNamesList() {
        return pref.isBool(FlowCost.USA_SECURITY) ? PROPERTIES_SECURED : PROPERTIES_NOT_SECURED;
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties del Form nell'ordine:
     * 1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxService
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getFormPropertyNamesList(AEntity curremtItem) {
        return ((Person) curremtItem).usaSuperClasse ? PROPERTIES_SECURED : PROPERTIES_NOT_SECURED;
    }// end of method

}// end of class