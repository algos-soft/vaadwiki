package it.algos.vaadflow14.backend.packages.geografica.continente;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.enumeration.AEContinente;
import it.algos.vaadflow14.backend.logic.ALogicOld;
import it.algos.vaadflow14.ui.fields.AField;
import it.algos.vaadflow14.ui.fields.AGridField;
import it.algos.vaadflow14.ui.form.AForm;
import it.algos.vaadflow14.ui.form.WrapForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.List;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 10-ott-2020
 * Time: 13:25
 * <p>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ContinenteForm extends AForm {


    public ContinenteForm(ALogicOld logic, WrapForm wrap) {
        super(logic, wrap);
    }


    /**
     * Preferenze standard <br>
     * Normalmente il primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
    }


    /**
     * Crea in automatico i fields normali associati al binder <br>
     * Aggiunge i fields normali al binder <br>
     * Trasferisce (binder read) i valori dal DB alla UI <br>
     * Li aggiunge alla fieldsList <br>
     * Lista ordinata di tutti i fields normali del form <br>
     * Serve per presentarli (ordinati) dall' alto in basso nel form <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void creaFieldsBinder() {
        super.creaFieldsBinder();
    }


    /**
     * Costruisce una lista ordinata di nomi delle properties del Form. <br>
     * La lista viene usata per la costruzione automatica dei campi e l' inserimento nel binder <br>
     * Nell' ordine: <br>
     * 1) Cerca nell' annotation @AIForm della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxLogic <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * Se serve, modifica l' ordine della lista oppure esclude una property che non deve andare nel binder <br>
     *
     * @return lista di nomi di properties
     */
    @Override
    protected List<String> getPropertyNamesList() {
        return super.getPropertyNamesList();
    }


    /**
     * Crea gli eventuali fields extra NON associati al binder, oltre a quelli normali <br>
     * Li aggiunge alla fieldsList <br>
     * Lista ordinata di tutti i fields normali del form <br>
     * Serve per presentarli (ordinati) dall' alto in basso nel form <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaFieldsExtra() {
        super.creaFieldsExtra();
    }


    /**
     * Regola in lettura eventuali valori NON associati al binder. <br>
     * Dal DB alla UI <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void readFieldsExtra() {
        super.readFieldsExtra();
    }


    /**
     * Riordina (eventualmente) la lista fieldsList <br>
     * I fieldsExtra vengono necessariamente inseriti DOPO i fields normali mentre potrebbero dover apparire prima <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void reorderFieldList() {
        super.reorderFieldList();
    }


    /**
     * Aggiunge ogni singolo field della lista fieldsList al layout <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void addFieldsToLayout() {
        super.addFieldsToLayout();
    }


    /**
     * Crea una mappa fieldMap, per recuperare i fields dal nome <br>
     */
    @Override
    protected void creaMappaFields() {
        super.creaMappaFields();
    }


    /**
     * Eventuali aggiustamenti finali al layout <br>
     * Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap) <br>
     * Regola eventuali valori delle property in apertura della scheda <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixLayoutFinal() {
        String keyStati = "stati";
        AField field;
        AGridField gridField;

        if (entityBean.id.equals(AEContinente.europa.name())) {
            field = fieldsMap.get(keyStati);

            if (field != null && field instanceof AGridField) {
                gridField = (AGridField) field;
                gridField.addColumnsGrid("stato,ue,alfatre");
            }
        }
    }


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dalla  UI al DB
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void writeFieldsExtra() {
        super.writeFieldsExtra();
    }

}