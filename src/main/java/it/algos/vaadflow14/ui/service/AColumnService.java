package it.algos.vaadflow14.ui.service;

import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.data.renderer.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.fields.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.lang.reflect.*;
import java.util.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: sab, 25-lug-2020
 * Time: 13:55
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AColumnService.class); <br>
 * 3) @Autowired public AColumnService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @VaadinSessionScope() (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AColumnService extends AAbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Create a single column.
     * The column type is chosen according to the annotation @AIColumn or, if is not present, a @AIField.
     *
     * @param grid        a cui aggiungere la colonna
     * @param entityClazz modello-dati specifico
     * @param fieldName   della property da utilizzare per la colonna
     */
    public Grid.Column<AEntity> add(Grid grid, Class<? extends AEntity> entityClazz, String fieldName) {
        Grid.Column<AEntity> colonna = null;
        Field field = reflection.getField(entityClazz, fieldName);
        AETypeField type = null;
        AETypeBoolField typeBool = AETypeBoolField.checkBox;
        String header = VUOTA;
        VaadinIcon headerIcon = null;
        String colorHeaderIcon = VUOTA;
        String searchProperty = annotation.getSearchPropertyName(entityClazz);
        boolean isFlexGrow = false;
        String width = VUOTA;
        Label label = null;
        boolean sortable = false;

        if (field != null) {
            type = annotation.getColumnType(field);
            typeBool = annotation.getTypeBoolField(field);
            header = annotation.getColumnHeader(field);
            headerIcon = annotation.getHeaderIcon(field);
            colorHeaderIcon = annotation.getHeaderIconColor(field);
            width = annotation.getColumnWith(field);
            isFlexGrow = annotation.isFlexGrow(field);
        }

        if (type == null) {
            logger.error(String.format("Manca il type del field %s della entity &s", fieldName, entityClazz.getSimpleName()), this.getClass(), "add");
            return null;
        }
        else {
            switch (type) {
                case text:
                case phone:
                case password:
                case email:
                case cap:
                    colonna = grid.addColumn(fieldName);
                    sortable = true;
                    break;
                case textArea:
                    colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                        String testo = VUOTA;
                        try {
                            if (field.get(entity) instanceof String) {
                                testo = (String) field.get(entity);
                            }
                        } catch (Exception unErrore) {
                            logger.error(unErrore, this.getClass(), "add.text");
                        }

                        return new Label(testo);
                    }));//end of lambda expressions and anonymous inner class
                    break;
                case integer:
                    colonna = grid.addColumn(fieldName);
                    sortable = true;
                    break;
                case lungo:
                    colonna = grid.addColumn(fieldName);
                    sortable = true;
                    break;
                case booleano:
                    colonna = addBoolean(grid, field);
                    break;
                case localDate:
                case localDateTime:
                case localTime:
                    final AETypeData data = annotation.getTypeDataCol(field);
                    width = data.getWidthEM();
                    colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                        Object obj = null;
                        String testo = VUOTA;

                        try {
                            obj = field.get(entity);
                            testo = date.get(obj, data).trim();
                        } catch (Exception unErrore) {
                            logger.error(unErrore, this.getClass(), "add (data)");
                        }

                        return new Label(testo);
                    }));//end of lambda expressions and anonymous inner class
                    break;
                case combo:
                    colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                        String testo = VUOTA;

                        try {
                            testo = field.get(entity) != null ? field.get(entity).toString() : VUOTA;
                        } catch (Exception unErrore) {
                            logger.error(unErrore, this.getClass(), "add.combo");
                        }

                        return new Label(testo);
                    }));//end of lambda expressions and anonymous inner class
                    break;
                case enumeration:
                    colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                        Object obj = reflection.getPropertyValue((AEntity) entity, fieldName);
                        return new Label(obj != null ? obj.toString() : VUOTA);
                    }));//end of lambda expressions and anonymous inner class
                    break;
                case preferenza:
                    colonna = addPreferenza(grid, field);
                    break;
                case image:
                    colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                        String mongoValue = (String) reflection.getPropertyValue((AEntity) entity, fieldName);
                        Image image = resourceService.getBandieraFromMongo(mongoValue);
                        return image != null ? image : new Label("X");
                    }));//end of lambda expressions and anonymous inner class
                    break;
                case gridShowOnly:
                    break;
                default:
                    logger.warn("Switch - caso non definito", this.getClass(), "add");
                    break;
            }
        }

        if (colonna != null) {
            String headerText = "";
            label = new Label();
            label.getElement().getStyle().set("color", "blue");

            //--l'header di default viene sempre uguale al nome della property
            //--può essere minuscolo o con la prima maiuscola, a seconda del flag di preferenza
            //--può essere modificato con name = "Xyz" nell'annotation @AIColumn della Entity
            if (AEPreferenza.usaGridHeaderMaiuscola.is()) {
                header = text.primaMaiuscola(header);
            }
            else {
                header = text.primaMinuscola(header);
            }
            colonna.setHeader(header);

            //--eventuale aggiunta di una icona e l' header non è una String ma diventa un Component
            //--se c'è l' icona e manca il testo della annotation, NON usa il nome della property ma solo l' icona
            if (headerIcon != null) {
                Icon icon = new Icon(headerIcon);
                //                icon.setSize(widthHeaderIcon);
                icon.setColor(colorHeaderIcon);
                label.add(icon);
            }
            else {
                label.setText(header);
            }
            colonna.setHeader(label);

            if (fieldName.equals(searchProperty)) {
                Icon icon = new Icon(VaadinIcon.SEARCH);
                icon.setSize("10px");
                icon.getStyle().set("float", "center");
                icon.setColor("red");
                label = new Label();
                label.add(icon);
                label.add(text.primaMaiuscola(header));
                label.getElement().getStyle().set("fontWeight", "bold");
                colonna.setHeader(label);
            }// end of if cycle

            //--di default le colonne NON sono sortable
            //--può essere modificata con sortable = true, nell'annotation @AIColumn della Entity
            colonna.setSortable(sortable);
            colonna.setSortProperty(fieldName);

            colonna.setWidth(width);
            colonna.setFlexGrow(isFlexGrow ? 1 : 0);
        }
        return colonna;
    }


    /**
     * Prima cerca i valori nella @Annotation items=... dell' interfaccia AIField <br>
     * Poi cerca i valori di una classe enumeration definita in enumClazz=... dell' interfaccia AIField <br>
     * Poi cerca i valori di una collection definita con serviceClazz=...dell' interfaccia AIField <br>
     *
     * @param reflectionJavaField di riferimento
     * @param fieldKey            nome interno del field
     * @param caption             label sopra il field
     */
    public AField getEnumerationField(Field reflectionJavaField, String fieldKey, String caption) {
        AField field = null;
        List<String> enumItems = null;
        List enumObjects = null;
        Class enumClazz = null;

        if (reflectionJavaField == null) {
            return null;
        }

        enumItems = annotation.getEnumItems(reflectionJavaField);
        if (array.isEmpty(enumItems)) {
            enumClazz = annotation.getEnumClass(reflectionJavaField);
            if (enumClazz != null) {
                Object[] elementi = enumClazz.getEnumConstants();
                if (elementi != null) {
                    enumObjects = Arrays.asList(elementi);
                    field = appContext.getBean(AComboField.class, fieldKey, caption);
                    ((AComboField) field).setItem(enumObjects);
                    return field;
                }
            }
        }

        if (array.isAllValid(enumItems)) {
            field = appContext.getBean(AComboField.class, fieldKey, caption);
            ((AComboField) field).setItem(enumItems);
        }

        return field;
    }


    public Grid.Column<AEntity> addBoolean(Grid grid, Field field) {
        Grid.Column<AEntity> colonna = null;

        colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
            final AETypeBoolCol typeBool = annotation.getTypeBoolCol(field);
            final List<String> valori = annotation.getBoolEnumCol(field);
            boolean status = false;
            Icon icon;
            String testo = VUOTA;
            Label label = new Label();

            try {
                status = field.getBoolean(entity);
            } catch (Exception unErrore) {
                logger.error(unErrore.toString());
            }

            switch (typeBool) {
                case boolGrezzo:
                    try {
                        status = field.getBoolean(entity);
                        testo = status ? "vero" : "falso";
                    } catch (Exception unErrore) {
                        logger.error(unErrore.toString());
                    }

                    if (text.isValid(testo)) {
                        label.setText(testo);
                        if (status) {
                            label.getStyle().set("color", "green");
                        }
                        else {
                            label.getStyle().set("color", "red");
                        }
                    }
                    return label;
                case checkBox:
                    try {
                        status = field.getBoolean(entity);
                    } catch (Exception unErrore) {
                        logger.error(unErrore.toString());
                    }
                    return new Checkbox(status);
                case checkIcon:
                    if (status) {
                        icon = new Icon(VaadinIcon.CHECK);
                        icon.setColor("green");
                    }
                    else {
                        icon = new Icon(VaadinIcon.CLOSE);
                        icon.setColor("red");
                    }
                    icon.setSize("1em");
                    return icon;
                case checkIconReverse:
                    if (status) {
                        icon = new Icon(VaadinIcon.CLOSE);
                        icon.setColor("red");
                    }
                    else {
                        icon = new Icon(VaadinIcon.CHECK);
                        icon.setColor("green");
                    }
                    icon.setSize("1em");
                    return icon;
                case customLabel:
                    try {
                        status = field.getBoolean(entity);
                        testo = status ? valori.get(0) : valori.get(1);
                    } catch (Exception unErrore) {
                        logger.error(unErrore.toString());
                    }

                    if (text.isValid(testo)) {
                        label.setText(testo);
                        if (status) {
                            label.getStyle().set("color", "green");
                        }
                        else {
                            label.getStyle().set("color", "red");
                        }
                    }
                    return label;
                case yesNo:
                    try {
                        status = field.getBoolean(entity);
                        testo = status ? "si" : "no";
                    } catch (Exception unErrore) {
                        logger.error(unErrore.toString());
                    }

                    if (text.isValid(testo)) {
                        label.setText(testo);
                        if (status) {
                            label.getStyle().set("color", "green");
                        }
                        else {
                            label.getStyle().set("color", "red");
                        }
                    }
                    return label;
                case yesNoBold:
                    try {
                        status = field.getBoolean(entity);
                        testo = status ? "si" : "no";
                    } catch (Exception unErrore) {
                        logger.error(unErrore.toString());
                    }

                    if (text.isValid(testo)) {
                        label.setText(testo);
                        label.getStyle().set("font-weight", "bold");
                        if (status) {
                            label.getStyle().set("color", "green");
                        }
                        else {
                            label.getStyle().set("color", "red");
                        }
                    }
                    return label;
                default:
                    logger.warn("Switch - caso non definito", this.getClass(), "addBoolean");
                    break;
            }

            return new Label("g");
        }));//end of lambda expressions and anonymous inner class

        return colonna;
    }


    public Grid.Column<AEntity> addPreferenza(Grid grid, Field field) {
        Grid.Column<AEntity> colonna = null;
        final AETypeData typeData = annotation.getTypeDataCol(field);

        colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
            AETypePref typePref = (AETypePref) reflection.getPropertyValue((AEntity) entity, "type");
            byte[] bytes = null;
            Object value = null;
            Label label = new Label();
            String message = VUOTA;
            Icon icon = null;

            try {
                bytes = (byte[]) field.get(entity);
                if (bytes == null) {
                    label.setText("?");
                    label.getStyle().set("font-weight", "bold");
                    logger.warn("Non ci sono bytes come valore della preferenza " + ((Preferenza) entity).code);
                    return label;
                }
                value = typePref.bytesToObject(bytes);
            } catch (Exception unErrore) {
                logger.error(unErrore.toString());
            }

            switch (typePref) {
                case string:
                    label.getStyle().set("color", "blue");
                    message = (String) value;
                    break;
                case bool:
                    boolean status = (boolean) value;
                    message = status ? "si" : "no";
                    if (status) {
                        label.getStyle().set("color", "green");
                    }
                    else {
                        label.getStyle().set("color", "red");
                    }// end of if/else cycle
                    break;
                case integer:
                    label.getStyle().set("color", "maroon");
                    message = text.format(value);
                    break;
                case localdate:
                    message = value != null ? date.get(value, AETypeData.dateNormal) : VUOTA;
                    label.getStyle().set("color", "fuchsia");
                    break;
                case localdatetime:
                    message = value != null ? date.get(value, AETypeData.normaleOrario) : VUOTA;
                    label.getStyle().set("color", "fuchsia");
                    break;
                case localtime:
                    message = value != null ? date.get(value, AETypeData.orario) : VUOTA;
                    label.getStyle().set("color", "fuchsia");
                    break;

                //                    Object obj;

                //                    try {
                ////                        obj = field.get(entity);
                //                        message = value!=null?date.get(value, data).trim():VUOTA;
                //                    } catch (Exception unErrore) {
                //                        logger.error(unErrore, this.getClass(), "addPreferenza (data)");
                //                    }
                //
                //                    label.getStyle().set("color", "fuchsia");
                //                    break;
                case email:
                    label.getStyle().set("color", "lime");
                    message = (String) value;
                    break;
                case enumeration:
                    label.getStyle().set("color", "teal");
                    message = enumService.convertToPresentation((String) value);
                    break;
                case icona:
                    VaadinIcon vaadinIcon;

                    try {
                        vaadinIcon = (VaadinIcon) value;
                        icon = vaadinIcon.create();
                    } catch (Exception unErrore) {
                        logger.error(unErrore.toString());
                    }
                    if (icon != null) {
                        icon.getElement().setAttribute("style", "color: " + "red");
                    }
                    break;
                default:
                    logger.warn("Switch - caso non definito");
                    break;
            }
            if (icon != null) {
                return icon;
            }

            label.setText(message);
            label.getStyle().set("font-weight", "bold");
            return label;

        }));//end of lambda expressions and anonymous inner class

        return colonna;
    }

}