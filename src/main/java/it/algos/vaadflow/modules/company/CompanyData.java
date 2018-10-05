package it.algos.vaadflow.modules.company;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.backend.data.AData;
import it.algos.vaadflow.modules.address.Address;
import it.algos.vaadflow.modules.address.AddressService;
import it.algos.vaadflow.modules.address.EAAddress;
import it.algos.vaadflow.modules.person.EAPerson;
import it.algos.vaadflow.modules.person.Person;
import it.algos.vaadflow.modules.person.PersonService;
import it.algos.vaadflow.service.IAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.TAG_COM;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 02-set-2018
 * Time: 09:18
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class CompanyData extends AData {


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected AddressService addressService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PersonService personService;

    /**
     * Il service viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private CompanyService service;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param service di collegamento per la Repository
     */
    @Autowired
    public CompanyData(@Qualifier(TAG_COM) IAService service) {
        super(Company.class, service);
        this.service = (CompanyService) service;
    }// end of Spring constructor


    /**
     * Metodo invocato da ABoot <br>
     * <p>
     * Creazione di una collezione - Solo se non ci sono records
     */
    @Override
    public void loadData() {
        int numRec = super.count();

        if (numRec == 0) {
            numRec = creaAll();
            log.warn("Algos - Creazione dati iniziali CompanyData.loadData(): " + numRec + " schede");
        } else {
            log.info("Algos - Data. La collezione Company è presente: " + numRec + " schede");
        }// end of if/else cycle
    }// end of method


    /**
     * Creazione della collezione
     */
    private int creaAll() {
        int num = 0;
        String code;
        String descrizione;
        EAPerson eaPerson;
        Person contatto;
        String telefono;
        String email;
        EAAddress eaAddress;
        Address indirizzo;

        for (EACompany company : EACompany.values()) {
            code = company.getCode();
            descrizione = company.getDescrizione();
            eaPerson = company.getPerson();
            contatto = personService.newEntity(eaPerson);
            telefono = company.getTelefono();
            email = company.getEmail();
            eaAddress = company.getAddress();
            indirizzo = addressService.newEntity(eaAddress);

            service.crea(code, descrizione, contatto, telefono, email, indirizzo);
            num++;
        }// end of for cycle

        return num;
    }// end of method

}// end of class
