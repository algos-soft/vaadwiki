package it.algos.vaadflow.modules.role;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadflow.service.AService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_ROL;


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
@Qualifier(TAG_ROL)
@Slf4j
@AIScript(sovrascrivibile = false)
public class RoleService extends AService {


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public RoleRepository repository;
    Collection<? extends GrantedAuthority> authorities;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    public RoleService(@Qualifier(TAG_ROL) MongoRepository repository) {
        super(repository);
        super.entityClass = Role.class;
        this.repository = (RoleRepository) repository;
    }// end of Spring constructor

    /**
     * Ricerca una entity <br>
     * Se non esiste, la crea <br>
     *
     * @param code di riferimento (obbligatorio ed unico)
     *
     * @return la entity trovata o appena creata
     */
    public Role findOrCrea(String code) {
        Role entity = findByKeyUnica(code);

        if (entity == null) {
            entity = newEntity(0, code);
            save(entity);
        }// end of if cycle

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
    public Role newEntity() {
        return newEntity(0, "");
    }// end of method

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok) <br>
     *
     * @param ordine di presentazione (obbligatorio con inserimento automatico se è zero)
     * @param code   codice di riferimento (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Role newEntity(int ordine, String code) {
        Role entity = findByKeyUnica(code);

        if (entity == null) {
            entity = Role.builderRole()
                    .ordine(ordine != 0 ? ordine : this.getNewOrdine())
                    .code(text.isValid(code) ? code : null)
                    .build();
        }// end of if cycle

        return (Role) creaIdKeySpecifica(entity);
    }// end of method

    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param code di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Role findByKeyUnica(String code) {
        return repository.findByCode(code);
    }// end of method

    /**
     * Returns all instances of the type <br>
     * La Entity è EACompanyRequired.nonUsata. Non usa Company. <br>
     * Lista ordinata <br>
     *
     * @return lista ordinata di tutte le entities
     */
    @Override
    public List<Role> findAll() {
        return repository.findAllByOrderByOrdineAsc();
    }// end of method

    /**
     * Property unica (se esiste).
     */
    public String getPropertyUnica(AEntity entityBean) {
        return text.isValid(((Role) entityBean).getCode()) ? ((Role) entityBean).getCode() : "";
    }// end of method

    /**
     * Guest role
     */
    public Role getGuest() {
        return findByKeyUnica(EARole.guest.name());
    }// end of method

    /**
     * User role
     */
    public Role getUser() {
        return findByKeyUnica(EARole.user.name());
    }// end of method

    /**
     * Admin role
     */
    public Role getAdmin() {
        return findByKeyUnica(EARole.admin.name());
    }// end of method

    /**
     * Developer role
     */
    public Role getDeveloper() {
        return findByKeyUnica(EARole.developer.name());
    }// end of method

    /**
     * Guest roles
     */
    public List<Role> getAllGuestRoles() {
        List<Role> lista = new ArrayList<>();
        lista.add(getGuest());
        return lista;
    }// end of method

    /**
     * User roles
     */
    public List<Role> getUserRole() {
        List<Role> lista = new ArrayList<>();
        lista.add(getUser());
        return lista;
    }// end of method

    /**
     * User roles
     */
    public List<Role> getAllUserRoles() {
        List<Role> lista = getAllGuestRoles();
        lista.add(getUser());
        return lista;
    }// end of method

    /**
     * Admin roles
     */
    public List<Role> getAllAdminRoles() {
        List<Role> lista = getAllUserRoles();
        lista.add(getAdmin());
        return lista;
    }// end of method

    /**
     * Developer roles
     */
    public List<Role> getAllDeveloperRoles() {
        List<Role> lista = getAllAdminRoles();
        lista.add(getDeveloper());
        return lista;
    }// end of method

    /**
     * Roles
     */
    public List<Role> getRoles(EARole ruolo) {
        List<Role> lista;

        switch (ruolo) {
            case guest:
                lista = getAllGuestRoles();
                break;
            case user:
                lista = getAllUserRoles();
                break;
            case admin:
                lista = getAllAdminRoles();
                break;
            case developer:
                lista = getAllDeveloperRoles();
                break;
            default:
                lista = getAllGuestRoles();
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        return lista;
    }// end of method

    /**
     * Roles
     */
    public String[] getAllRoles() {
        return new String[]{"admin", "user"};
    }// end of method

    /**
     * GrantedAuthority
     */
    public Collection<? extends GrantedAuthority> getAuthorities(List<Role> ruoli) {
        List<GrantedAuthority> listAuthority = new ArrayList<>();
        GrantedAuthority authority;

        for (Role ruolo : ruoli) {
            authority = new SimpleGrantedAuthority(ruolo.code);
            listAuthority.add(authority);
        }// end of for cycle

        return listAuthority;
    }// end of method


    /**
     * GrantedAuthority
     */
    public Collection<? extends GrantedAuthority> getAuthorities(Utente utente) {
        List<Role> ruoli = utente.ruoli;

        return getAuthorities(ruoli);
    }// end of method

}// end of class