package it.algos.vaadflow14.backend.packages.geografica.continente;

import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.ui.fields.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import org.springframework.beans.factory.annotation.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * First time: sab, 10-ott-2020
 * Last doc revision: 13:25
 * <p>
 */
@Route(value = "continenteForm", layout = MainLayout.class)
//Algos
@AIScript(sovraScrivibile = false, doc = AEWizDoc.inizioRevisione)
public class ContinenteLogicForm extends LogicForm {


    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     */
    public ContinenteLogicForm(@Qualifier("continenteService") AIService entityService) {
        super.entityClazz = Continente.class;
        super.entityService = entityService;
    }// end of Vaadin/@Route constructor


    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixAlertForm() {
        super.fixAlertForm();

        addSpanBlu("Nella sub-lista tutti gli stati di questo continente");
    }

    /**
     * Regolazioni finali di alcuni oggetti <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void regolazioniFinali() {
        super.regolazioniFinali();
        String keyStati = "stati";
        AField field;
        AGridField gridField;

        if (entityBean.id.equals(AEContinente.europa.name())) {
            field = currentForm.fieldsMap.get(keyStati);

            if (field != null && field instanceof AGridField) {
                gridField = (AGridField) field;
                gridField.addColumnsGrid("stato,ue,alfatre");
            }
        }
    }


}