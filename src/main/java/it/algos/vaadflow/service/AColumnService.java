package it.algos.vaadflow.service;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadflow.enumeration.EAPrefType;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 23-set-2018
 * Time: 21:18
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con:
 * 1) StaticContextAccessor.getBean(AFieldService.class);
 * 2) AFieldService.getInstance();
 * 3) @Autowired private AFieldService fieldService;
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AColumnService {

    /**
     * Private final property
     */
    private static final AColumnService INSTANCE = new AColumnService();

    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AAnnotationService annotation = AAnnotationService.getInstance();

    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AReflectionService reflection = AReflectionService.getInstance();

    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PreferenzaService pref;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected ADateService date;


    /**
     * Private constructor to avoid client applications to use constructor
     */
    private AColumnService() {
    }// end of constructor

    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static AColumnService getInstance() {
        return INSTANCE;
    }// end of static method


    /**
     * Create a single column.
     * The column type is chosen according to the annotation @AIColumn or, if is not present, a @AIField.
     *
     * @param grid         a cui aggiungere la colonna
     * @param entityClazz  modello-dati specifico
     * @param propertyName della property
     */
    public void create(Grid<AEntity> grid, Class<? extends AEntity> entityClazz, String propertyName) {
        Grid.Column<AEntity> colonna = null;
        EAFieldType type = annotation.getFormType(entityClazz, propertyName);
        String header = annotation.getColumnName(entityClazz, propertyName);
        String width = annotation.getColumnWithPX(entityClazz, propertyName);
        Class clazz = annotation.getComboClass(entityClazz, propertyName);

        if (type == null) {
            colonna = grid.addColumn(propertyName);
            colonna.setSortProperty(propertyName);
            return;
        }// end of if cycle

        switch (type) {
            case text:
                colonna = grid.addColumn(propertyName);
                break;
            case integer:
                colonna = grid.addColumn(propertyName);
                break;
            case lungo:
                colonna = grid.addColumn(propertyName);
                break;
            case checkbox:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Checkbox checkbox;
                    Boolean status = false;
                    Field field = reflection.getField(entityClazz, propertyName);
                    try { // prova ad eseguire il codice
                        status = field.getBoolean(entity);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch
                    checkbox = new Checkbox(status);
                    return checkbox;
                }));
                break;
            case enumeration:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Object obj = reflection.getPropertyValue(entity, propertyName);
                    return new Label(obj.toString());
                }));
                break;
            case combo:
                colonna = grid.addColumn(propertyName);
//                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
//                    ComboBox combo = new ComboBox();
//                    Object entityBean = reflection.getPropertyValue(entity, property);
//                    IAService service = (IAService) StaticContextAccessor.getBean(clazz);
//                    List items = ((IAService) service).findAll();
//                    if (array.isValid(items)) {
//                        combo.setItems(items);
//                        combo.setValue(entityBean);
//                    }// end of if cycle
//                    combo.setEnabled(false);
//                    return combo;
//                }));
                break;
            case weekdate:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    LocalDate data;
                    String testo = "X";
                    Field field = reflection.getField(entityClazz, propertyName);
                    try { // prova ad eseguire il codice
                        data = (LocalDate) field.get(entity);
                        testo = date.getDayWeekShort(data);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch
                    return new Label(testo);
                }));
                break;
            case localdate:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    LocalDate data;
                    String testo = "Y";
                    Field field = reflection.getField(entityClazz, propertyName);
                    try { // prova ad eseguire il codice
                        data = (LocalDate) field.get(entity);
                        testo = date.getDate(data);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch
                    return new Label(testo);
                }));
                break;
            case localdatetime:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Object obj;
                    LocalDateTime timeStamp;
                    String testo = "Y";
                    Field field = reflection.getField(entityClazz, propertyName);
                    try { // prova ad eseguire il codice
                        obj = field.get(entity);
                        if (obj instanceof LocalDateTime) {
                            timeStamp = (LocalDateTime) obj;
                            testo = date.getTime(timeStamp);
                        } else {
                            log.warn("localdatetime non definito");
                        }// end of if/else cycle
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch
                    return new Label(testo);
                }));
                break;
            case email:
                colonna = grid.addColumn(propertyName);
                break;
            case link:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Object obj = null;
                    Field field = reflection.getField(entityClazz, propertyName);
                    try { // prova ad eseguire il codice
                        obj = field.get(entity);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch
                    return new Label(obj != null ? obj.toString() : "");
                }));
                break;
            case pref:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    EAPrefType typePref = (EAPrefType) reflection.getPropertyValue(entity, "type");
                    byte[] bytes = null;
                    Object value = null;
                    Field field = reflection.getField(entityClazz, propertyName);
                    try { // prova ad eseguire il codice
                        bytes = (byte[]) field.get(entity);
                        value = typePref.bytesToObject(bytes);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch
                    if (typePref == EAPrefType.bool && pref.isBool(FlowCost.USA_CHECK_BOX)) {
                        return new Checkbox((boolean) value ? "si" : "no", (boolean) value);
                    } else {
                        return new Label(value != null ? value.toString() : "");
                    }// end of if/else cycle
                }));
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        if (colonna != null) {
            colonna.setHeader(text.isValid(header) ? header : propertyName);
            colonna.setSortProperty(propertyName);
//            if (property.equals("id")) {
//                colonna.setWidth("1px");
//            }// end of if cycle

//            if (text.isValid(width)) {
//                colonna.setWidth(width);
//                colonna.setFlexGrow(0);
//            }// end of if cycle
        }// end of if cycle

    }// end of method

}// end of class
