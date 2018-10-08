package it.algos.vaadflow.modules.utente;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.backend.data.AData;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadflow.modules.role.Role;
import it.algos.vaadflow.modules.role.RoleService;
import it.algos.vaadflow.service.IAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_UTE;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 14-set-2018
 * Time: 15:34
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class UtenteData extends AData {


    @Autowired
    private RoleService roleService;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param service di collegamento per la Repository
     */
    @Autowired
    public UtenteData(@Qualifier(TAG_UTE) IAService service) {
        super(Utente.class, service);
    }// end of Spring constructor


    /**
     * Creazione della collezione
     */
    protected int creaAll() {
        int num = 0;
        String userName;
        String passwordInChiaro;
        EARole ruolo;
        List<Role> ruoli;
        String mail;

        for (EAUtente utente : EAUtente.values()) {
            userName = utente.getUserName();
            passwordInChiaro = utente.getPasswordInChiaro();
            ruolo = utente.getRuolo();
            ruoli = roleService.getRoles(ruolo);
            mail = utente.getMail();

            ((UtenteService)service).crea(userName, passwordInChiaro, ruoli, mail, false);
            num++;
        }// end of for cycle

        return num;
    }// end of method


}// end of class
