package it.algos.vaadflow14.backend.service;

import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.data.provider.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 30-apr-2021
 * Time: 08:27
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AUtilityService.class); <br>
 * 3) @Autowired public AUtilityService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UtilityService extends AbstractService {


    /**
     * Estrae il contenuto testuale dal Sort (springframework) <br>
     *
     * @param sortSpring di springframework
     *
     * @return array di parti
     */
    private String[] getParti(final Sort sortSpring) {
        String[] parti = null;
        String sortTxt = VUOTA;
        String[] partiTmp = null;

        if (sortSpring != null) {
            sortTxt = sortSpring.toString();
        }

        if (text.isValid(sortTxt) && sortTxt.contains(DUE_PUNTI)) {
            partiTmp = sortTxt.split(DUE_PUNTI);
        }

        if (partiTmp != null && partiTmp.length == 2) {
            parti = partiTmp;
        }

        return parti;
    }


    /**
     * Estrae il field dal Sort (springframework) <br>
     *
     * @param sortSpring di springframework
     *
     * @return fieldName
     */
    public String getSortFieldName(final Sort sortSpring) {
        String[] parti = getParti(sortSpring);
        return parti != null ? parti[0].trim() : VUOTA;
    }


    /**
     * Estrae la Direction dal Sort (springframework) <br>
     *
     * @param sortSpring di springframework
     *
     * @return direction di springframework
     */
    public Sort.Direction getSortDirection(final Sort sortSpring) {
        Sort.Direction directionSpring = null;
        String[] parti = getParti(sortSpring);
        String directionTxt = VUOTA;

        if (parti != null) {
            directionTxt = parti[1].trim();
            if (directionTxt.equals(SORT_SPRING_ASC)) {
                directionSpring = Sort.Direction.ASC;
            }
            if (directionTxt.equals(SORT_SPRING_DESC)) {
                directionSpring = Sort.Direction.DESC;
            }
        }

        return directionSpring;
    }


    /**
     * Estrae la Direction dal sortOrder (vaadin) <br>
     *
     * @param sortVaadin di vaadin
     *
     * @return direction di springframework
     */
    public Sort.Direction getSortDirection(final QuerySortOrder sortVaadin) {
        Sort.Direction directionSpring = null;
        SortDirection directionVaadin = null;

        directionVaadin = sortVaadin.getDirection();
        if (directionVaadin.name().equals(SORT_VAADIN_ASC)) {
            directionSpring = Sort.Direction.ASC;
        }
        if (directionVaadin.name().equals(SORT_VAADIN_DESC)) {
            directionSpring = Sort.Direction.DESC;
        }

        return directionSpring;
    }

    /**
     * DataProvider usa QuerySortOrder (Vaadin) <br>
     * Query di MongoDB usa Sort (springframework) <br>
     * Qui effettuo la conversione
     *
     * @param sortVaadin di Vaadin
     *
     * @return sortSpring di springframework
     */
    public Sort sortVaadinToSpring(final QuerySortOrder sortVaadin) {
        Sort sortSpring;

        SortDirection directionVaadin = sortVaadin.getDirection();
        String field = sortVaadin.getSorted();
        if (directionVaadin == SortDirection.ASCENDING) {
            sortSpring = Sort.by(Sort.Direction.ASC, field);
        }
        else {
            sortSpring = Sort.by(Sort.Direction.DESC, field);
        }

        return sortSpring;
    }


    /**
     * DataProvider usa QuerySortOrder (Vaadin) <br>
     * Query di MongoDB usa Sort (springframework) <br>
     * Qui effettuo la conversione
     *
     * @param sortVaadinList sort di Vaadin
     * @param entityClazz    corrispondente ad una collection sul database mongoDB. Obbligatoria.
     *
     * @return sortSpring di springframework
     */
    public Sort sortVaadinToSpring(final List<QuerySortOrder> sortVaadinList, final Class<? extends AEntity> entityClazz) {
        Sort sortSpring = null;
        Sort.Direction directionSpring = null;
        String fieldName = VUOTA;

        if (sortVaadinList != null) {
            for (QuerySortOrder sortVaadin : sortVaadinList) {
                directionSpring = getSortDirection(sortVaadin);
                fieldName = sortVaadin.getSorted();
                if (sortSpring == null) {
                    sortSpring = Sort.by(directionSpring, fieldName);
                }
                else {
                    sortSpring = sortSpring.and(Sort.by(directionSpring, fieldName));
                }
            }
        }

        if (sortSpring == null) {
            sortSpring = annotation.getSortSpring(entityClazz);
        }

        if (sortSpring == null) {
            sortSpring = Sort.by(Sort.DEFAULT_DIRECTION, FIELD_ID);
        }

        return sortSpring;
    }

    /**
     * Query di MongoDB usa Sort (springframework) <br>
     * DataProvider usa QuerySortOrder (Vaadin) <br>
     * Qui effettuo la conversione
     *
     * @param sortSpring di springframework
     *
     * @return sortVaadin di Vaadin
     */
    public QuerySortOrder sortSpringToVaadin(final Sort sortSpring) {
        QuerySortOrder sortVaadin = null;
        SortDirection directionVaadin = SortDirection.ASCENDING;
        String fieldName = getSortFieldName(sortSpring);
        Sort.Direction directionSpring = getSortDirection(sortSpring);

        if (directionSpring == Sort.Direction.DESC) {
            directionVaadin = SortDirection.DESCENDING;
        }

        if (text.isValid(fieldName)) {
            sortVaadin = new QuerySortOrder(fieldName, directionVaadin);
        }

        return sortVaadin;
    }

    /**
     * Crea un ComboBox <br>
     *
     * @param entityClazz  the class of type AEntity
     * @param fieldName    (obbligatorio) della property da utilizzare per il ComboBox
     * @param dataProvider fornitore degli items. Se manca lo costruisce con la collezione completa
     * @param width        larghezza a video del ComboBox. Se manca usa il default FlowCost.COMBO_WIDTH
     * @param initialValue eventuale valore iniziale di selezione
     */
    public ComboBox creaComboBox(final Class<? extends AEntity> entityClazz, final String fieldName, DataProvider dataProvider, final int width, Object initialValue) throws AlgosException{
        ComboBox combo = null;
        Field reflectionJavaField = null;
        Class comboClazz = null;
        Class enumClazz = null;
        AETypeField type;
        String widthEM;
        //        String widthEM = width > 0 ? width + TAG_EM : COMBO_WIDTH + TAG_EM;
        Sort sortSpring;
        List items;
        AIService serviceClazz;
        String textInitialValue;

        reflectionJavaField = reflection.getField(entityClazz, fieldName);
        type = annotation.getColumnType(reflectionJavaField);
        widthEM = width > 0 ? width + TAG_EM : annotation.getComboBoxGridWidth(reflectionJavaField);
        textInitialValue = annotation.getComboInitialValue(reflectionJavaField);

        if (type != AETypeField.combo && type != AETypeField.enumeration) {
            throw AlgosException.stack(String.format("La property non è di type combo e nemmeno di type enumeration"),this.getClass(),"creaComboBox");
        }

        combo = new ComboBox();
        combo.setWidth(widthEM);
        combo.setPreventInvalidInput(true);
        combo.setAllowCustomValue(false);
        combo.setPlaceholder(text.primaMaiuscola(fieldName) + TRE_PUNTI);
        combo.setClearButtonVisible(true);
        combo.setRequired(false);

        if (type == AETypeField.combo) {
            comboClazz = annotation.getComboClass(reflectionJavaField);
            //            sortSpring = annotation.getSortSpring(comboClazz);
            dataProvider = dataProvider != null ? dataProvider : provider.creaDataProvider(comboClazz);
            if (dataProvider != null) {
                combo.setDataProvider(dataProvider);
            }
        }

        if (type == AETypeField.enumeration) {
            enumClazz = annotation.getEnumClass(reflectionJavaField);
            items = fieldService.getEnumerationItems(reflectionJavaField);
            if (items != null) {
                combo.setItems(items);
            }
        }

        if (initialValue == null && comboClazz != null) {
            serviceClazz = classService.getServiceFromEntityClazz(comboClazz);
            try {
                if (text.isValid(textInitialValue)) {
                    initialValue = serviceClazz.findByKey(textInitialValue);
                }
            } catch (AlgosException unErrore) {
                throw AlgosException.stack(String.format("Nel comboBox %s manca il valore inziale %s", fieldName, textInitialValue), this.getClass(), "creaComboBox");
            }
        }

        if (initialValue != null) {
            combo.setValue(initialValue);
        }

        return combo;
    }

    /**
     * Controlla i flag della entityClazz per vedere se usare la colonna reset <br>
     *
     * @param entityClazz the class of type AEntity
     *
     * @return sortVaadin di Vaadin
     */
    public boolean usaReset(final Class<? extends AEntity> entityClazz) {
        boolean usaReset = false;

        if (annotation.usaReset(entityClazz) && annotation.usaNew(entityClazz) && reflection.isEsiste(entityClazz, FIELD_NAME_RESET)) {
            usaReset = true;
        }

        return usaReset;
    }

}