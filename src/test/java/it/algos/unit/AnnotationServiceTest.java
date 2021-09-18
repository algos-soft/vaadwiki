package it.algos.unit;

import com.vaadin.flow.router.*;
import it.algos.test.*;
import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import static it.algos.vaadflow14.backend.service.AnnotationService.*;
import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.mapping.*;

import javax.validation.constraints.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 05-set-2020
 * Time: 16:21
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("AnnotationService - Annotazioni delle AEntity.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnnotationServiceTest extends ATest {

    private AIEntity aiEntity;

    private AIList aiList;

    private AIForm aiForm;

    private AIView aiView;

    private AIColumn aiColumn;

    private AIField aiField;

    private Document document;

    private Qualifier qualifier;

    private Route route;

    private NotNull notNull;


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private AnnotationService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = annotationService;
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();
    }


    /**
     * Qui passa al termine di ogni singolo test <br>
     */
    @AfterEach
    void tearDown() {
    }


    /**
     * Qui passa una volta sola, chiamato alla fine di tutti i tests <br>
     */
    @AfterAll
    void tearDownAll() {
    }


    @SuppressWarnings("javadoc")
    /**
     * Get the annotation Algos AIEntity.
     *
     * @param entityClazz the class of type AEntity
     *
     * @return the specific annotation
     */
    @Test
    public void getAIEntity() {
        aiEntity = service.getAIEntity(VIA_ENTITY_CLASS);
        assertNotNull(aiEntity);

    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the annotation Algos AIList.
     *
     * @param entityClazz the class of type AEntity
     *
     * @return the specific annotation
     */
    @Test
    public void getAIList() {
        aiList = service.getAIList(VIA_ENTITY_CLASS);
        assertNotNull(aiList);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the annotation Algos AIForm.
     *
     * @param entityClazz the class of type AEntity
     *
     * @return the specific annotation
     */
    @Test
    public void getAIForm() {
        aiForm = service.getAIForm(VIA_ENTITY_CLASS);
        assertNotNull(aiForm);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the annotation Algos AIView.
     *
     * @param viewClazz the class of type IAView
     *
     * @return the specific annotation
     */
    @Test
    public void getAIView() {
        aiView = service.getAIView(VIA_ENTITY_CLASS);
        assertNotNull(aiView);

    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the annotation Algos AIColumn.
     *
     * @param reflectionJavaField di riferimento per estrarre l'interfaccia
     *
     * @return the specific annotation
     */
    @Test
    public void getAIColumn() {
        aiColumn = service.getAIColumn(FIELD_NOME);
        assertNotNull(aiColumn);
    }// end of single test


    /**
     * Get the annotation Algos Algos.
     *
     * @param entityClazz the class of type AEntity
     * @param fieldName   the property name
     *
     * @return the specific interface
     */
    @Test
    public void getAIColumn2() {
        aiColumn = service.getAIColumn(VIA_ENTITY_CLASS, FIELD_NAME_NOTE);
        assertNull(aiColumn);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the annotation Algos AIField.
     *
     * @param reflectionJavaField di riferimento per estrarre l'interfaccia
     *
     * @return the specific annotation
     */
    @Test
    public void getAIField() {
        aiField = service.getAIField(FIELD_NOME);
        assertNotNull(aiField);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the annotation Algos AIField.
     *
     * @param entityClazz the class of type AEntity
     * @param fieldName   the property name
     *
     * @return the specific annotation
     */
    @Test
    public void getAIField2() {
        aiField = service.getAIField(VIA_ENTITY_CLASS, FIELD_NAME_NOTE);
        assertNull(aiField);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the annotation Document.
     *
     * @param entityClazz the class of type AEntity
     *
     * @return the specific Annotation
     */
    @Test
    public void getDocument() {
        document = service.getDocument(VIA_ENTITY_CLASS);
        assertNotNull(document);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the annotation Qualifier.
     *
     * @param viewClazz the class of type AViewList
     *
     * @return the specific Annotation
     */
    @Test
    public void getQualifier() {
        //        qualifier = annotation.getQualifier(VIA_ENTITY_CLASS);
        //        assertNotNull(qualifier);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the annotation Route.
     *
     * @param viewClazz the class of type AViewList
     *
     * @return the specific Annotation
     */
    @Test
    public void getRoute() {
        route = service.getRoute(VIA_ENTITY_CLASS);
        assertNull(route);
    }// end of single test


    /**
     * Get the annotation NotNull.
     *
     * @param reflectionJavaField di riferimento per estrarre l'annotation
     *
     * @return the Annotation for the specific field
     */
    @Test
    public void getNotNull() {
        notNull = service.getNotNull(FIELD_NOME);
        assertNull(notNull);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the name of the spring-view.
     *
     * @param clazz the entity class
     *
     * @return the name of the spring-view
     */
    @Test
    public void getViewName() {
        previsto = "Via";
        ottenuto = service.getMenuName(VIA_ENTITY_CLASS);
        assertEquals(previsto, ottenuto);
    }// end of single test

    //    @SuppressWarnings("javadoc")
    //    /**
    //     * Get the status listShowsID of the class.
    //     *
    //     * @param clazz the entity class
    //     *
    //     * @return status of class - default false
    //     */
    //    @Test
    //    public void isListShowsID() {
    //        previstoBooleano = false;
    //        ottenutoBooleano = service.isListShowsID(ROLE_ENTITY_CLASS);
    //        assertEquals(previstoBooleano, ottenutoBooleano);
    //    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Colonne visibili (e ordinate) nella Grid
     * Nomi dei fields da considerare per estrarre i Java Reflected Field dalle @Annotation della Entity
     * Se la classe AEntity->@AIList prevede una lista specifica, usa quella lista (con o senza ID)
     * Sovrascrivibile
     *
     * @param clazz the entity class
     *
     * @return nomi dei fields, oppure null se non esiste l'Annotation specifica @AIList() nella Entity
     */
    @Test
    @Order(1)
    @DisplayName("1 - getGridColumns")
    public void getGridColumns() {
        String[] stringArray = {"ordine", "nome"};
        previstoArray = new ArrayList(Arrays.asList(stringArray));

        ottenutoArray = service.getGridColumns(VIA_ENTITY_CLASS);
        assertEquals(previstoArray, ottenutoArray);
        System.out.println("Colonne previste per la Entity " + VIA_ENTITY_CLASS.getSimpleName() + ":");
        print(ottenutoArray);

        String[] stringArray2 = {"ordine","mese", "giorni","giorniBisestile","sigla"};
        previstoArray = new ArrayList(Arrays.asList(stringArray2));
        ottenutoArray = service.getGridColumns(MESE_ENTITY_CLASS);
        assertEquals(previstoArray, ottenutoArray);
        System.out.println(VUOTA);
        System.out.println("Colonne previste per la Entity " + MESE_ENTITY_CLASS.getSimpleName() + ":");
        print(ottenutoArray);

        String[] stringArray3 = {"code", "descrizione"};
        previstoArray = new ArrayList(Arrays.asList(stringArray3));
        ottenutoArray = service.getGridColumns(COMPANY_ENTITY_CLASS);
        //        assertEquals(previstoArray, ottenutoArray);
        System.out.println(VUOTA);
        System.out.println("Colonne previste per la Entity " + COMPANY_ENTITY_CLASS.getSimpleName() + ":");
        print(ottenutoArray);
    }


    @SuppressWarnings("javadoc")
    /**
     * Nomi delle properties del, estratti dalle @Annotation della Entity
     * Se la classe AEntity->@AIForm prevede una lista specifica, usa quella lista (con o senza ID)
     * Se l'annotation @AIForm non esiste od è vuota,
     * restituisce tutti i campi (properties della classe e superclasse)
     * Sovrascrivibile
     *
     * @return lista di nomi di property, oppure null se non esiste l'Annotation specifica @AIForm() nella Entity
     */
    @Test
    public void getFormPropertiesName() {
        String[] stringArray = {"ordine", "code"};
        previstoArray = new ArrayList(Arrays.asList(stringArray));

        //        ottenutoArray = annotation.get(VIA_ENTITY_CLASS);
        //        assertEquals(previstoArray, ottenutoArray);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Nomi dei fields da considerare per estrarre i Java Reflected Field dalle @Annotation della Entity
     * Se la classe AEntity->@AIForm prevede una lista specifica, usa quella lista (con o senza ID)
     * Sovrascrivibile
     *
     * @return nomi dei fields, oppure null se non esiste l'Annotation specifica @AIForm() nella Entity
     */
    @Test
    public void getFormFieldsName() {
        String[] stringArray = {"ordine", "nome"};
        previstoArray = new ArrayList(Arrays.asList(stringArray));

        ottenutoArray = service.getListaPropertiesForm(VIA_ENTITY_CLASS);
        assertEquals(previstoArray, ottenutoArray);

        //        ottenutoList = service.getFormFieldsName(USER_ENTITY_CLASS);
        //        assertNull(ottenutoList);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the name (columnService) of the property.
     * Se manca, usa il nome del Field
     * Se manca, usa il nome della property
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the name (columnService) of the field
     */
    @Test
    public void getColumnName() {
        previsto = HEADER_ORDINE;
        ottenuto = service.getColumnHeader(FIELD_ORDINE);
        assertEquals(previsto, ottenuto);

        previsto = HEADER_NOME;
        ottenuto = service.getColumnHeader(FIELD_NOME);
        assertEquals(previsto, ottenuto);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the type (columnService) of the property.
     * Se manca, usa il type del Field
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the type for the specific columnService
     */
    @Test
    public void getColumnType() {
        //        previstoType = EAFieldType.integer;
        //        ottenutoType = service.getColumnType(FIELD_ORDINE);
        //        assertEquals(previstoType, ottenutoType);
        //
        //        previstoType = EAFieldType.text;
        //        ottenutoType = service.getColumnType(FIELD_CODE);
        //        assertEquals(previstoType, ottenutoType);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the visibility of the columnService.
     * Di default true
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the visibility of the columnService
     */
    @Test
    public void isColumnVisibile() {
        //@todo RIMETTERE
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the type (field) of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the type for the specific columnService
     */
    @Test
    public void getFormType() {
        //        previstoType = EAFieldType.integer;
        //        ottenutoType = annotation.getFormType(ROLE_ENTITY_CLASS, FIELD_NAME_ORDINE);
        //        assertEquals(previstoType, ottenutoType);
        //
        //        previstoType = EAFieldType.text;
        //        ottenutoType = annotation.getFormType(ROLE_ENTITY_CLASS, FIELD_NAME_CODE);
        //        assertEquals(previstoType, ottenutoType);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the name (field) of the property.
     * Se manca, usa il nome della property
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the name (rows) of the field
     */
    @Test
    public void getFormFieldName() {
        previsto = textService.primaMaiuscola(NAME_ORDINE);
        ottenuto = service.getFormFieldNameCapital(FIELD_ORDINE);
        assertEquals(previsto, ottenuto);

        previsto = textService.primaMaiuscola(NAME_NOME);
        ottenuto = service.getFormFieldNameCapital(FIELD_NOME);
        assertEquals(previsto, ottenuto);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the alert message from @NotNull or from @Size
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the alert message
     */
    @Test
    public void getMessage() {
        previsto = FIELD_NAME_ORDINE + INT_NULL;
        previsto = textService.primaMaiuscola(previsto);

        ottenuto = service.getMessage(FIELD_ORDINE);
        assertEquals(VUOTA, ottenuto);

        ottenuto = service.getMessageNull(FIELD_ORDINE);
        assertEquals(VUOTA, ottenuto);

        //        previsto = "";
        //        ottenuto = annotation.getMessageSize(FIELD_ORDINE);
        //        assertEquals(VUOTA, ottenuto);

        //        previsto = FIELD_NAME_CODE + TESTO_NULL;
        //        previsto = text.primaMaiuscola(previsto);
        //        previsto = "Il codice è obbligatorio";
        //        ottenuto = annotation.getMessage(FIELD_NOME);
        //        assertEquals(previsto, ottenuto);
        //        ottenuto = annotation.getMessageNull(FIELD_NOME);
        //        assertEquals(previsto, ottenuto);

        //        previsto = FIELD_NAME_CODE + " deve contenere almeno 3 caratteri";
        //        previsto = text.primaMaiuscola(previsto);
        //        ottenuto = annotation.getMessage(FIELD_NOME);
        //        assertNotEquals(previsto, ottenuto);
        //        ottenuto = annotation.getMessageSize(FIELD_NOME);
        //        assertEquals(previsto, ottenuto);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the status of visibility for the field of ACompanyEntity.
     * <p>
     * Controlla se l'applicazione usa le company2 - flag  AlgosApp.USE_MULTI_COMPANY=true
     * Controlla se la collection (table) usa la company2
     * Controlla se l'buttonUser collegato è un developer
     *
     * @param clazz the entity class
     *
     * @return status - default true
     */
    @Test
    public void isCompanyFieldVisible() {
        //@todo RIMETTERE
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Tipo di lista (EAListButton) indicata nella AEntity class per la view AList
     *
     * @return valore della enumeration
     */
    @Test
    public void getListBotton() {
        //        EAListButton ottenutoListButton;
        //        EAListButton previstoListButton = EAListButton.standard;
        //
        //        ottenutoListButton = service.getListBotton(ROLE_ENTITY_CLASS);
        //        assertEquals(previstoListButton, ottenutoListButton);
        //@todo RIMETTERE
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Tipo di lista (EAFormButton) indicata nella AEntity class per la view AForm
     *
     * @return valore della enumeration
     */
    @Test
    public void getFormBotton() {
        //        EAFormButton ottenutoFormButton;
        //        EAFormButton previstoFormButton = EAFormButton.standard;
        //
        //        ottenutoFormButton = service.getFormBotton(ROLE_ENTITY_CLASS);
        //        assertEquals(previstoFormButton, ottenutoFormButton);
        //@todo RIMETTERE
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the status focus of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return status of field
     */
    @Test
    public void isFocus() {
        //        previstoBooleano = false;
        //        ottenutoBooleano = annotation.isFocus(FIELD_ORDINE);
        //        assertEquals(previstoBooleano, ottenutoBooleano);
        //
        //        previstoBooleano = true;
        //        ottenutoBooleano = annotation.isFocus(FIELD_CODE);
        //        assertEquals(previstoBooleano, ottenutoBooleano);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the class of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the class for the specific columnService
     */ public void getComboClass() {
        //        Class previstoClass = Role.class;
        //        Class ottenutoClass = annotation.getLinkClass(FIELD_CODE);
        //        assertEquals(previstoClass, ottenutoClass);
    }// end of method


    @SuppressWarnings("javadoc")
    /**
     * Get the widthEM of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the width of the field expressed in em
     */
    @Test
    public void getWidthEM() {
        previsto = "8.0em";
        ottenuto = service.getFormWith(FIELD_ORDINE);
        assertEquals(previsto, ottenuto);

        previsto = "14.0em";
        ottenuto = service.getFormWith(FIELD_NOME);
        assertEquals(previsto, ottenuto);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the status required of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return status of field
     */
    @Test
    public void isRequired() {
        previstoBooleano = false;
        ottenutoBooleano = service.isRequired(FIELD_ORDINE);
        assertEquals(previstoBooleano, ottenutoBooleano);

        previstoBooleano = true;
        ottenutoBooleano = service.isRequired(FIELD_NOME);
        assertEquals(previstoBooleano, ottenutoBooleano);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the status 'nonUsata, facoltativa, obbligatoria' of the class.
     *
     * @param clazz the entity class
     */
    @Test
    public void getCompanyRequired() {
        //        previstoCompany = EACompanyRequired.nonUsata;
        //        ottenutoCompany = service.getCompanyRequired(ROLE_ENTITY_CLASS);
        //        assertEquals(previstoCompany, ottenutoCompany);
        //
        //        previstoCompany = EACompanyRequired.obbligatoria;
        //        ottenutoCompany = service.getCompanyRequired(USER_ENTITY_CLASS);
        //        assertEquals(previstoCompany, ottenutoCompany);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the roleTypeVisibility of the class.
     * Viene usata come default, se manca il valore specifico del singolo field
     * La Annotation @AIEntity ha un suo valore di default per la property @AIEntity.roleTypeVisibility()
     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
     *
     * @param clazz the entity class
     *
     * @return the roleTypeVisibility of the class
     */
    @Test
    public void getEntityRoleType() {
        //        EARoleType roleTypeVisibilityOttenuta = null;
        //        EARoleType roleTypeVisibilityPrevista = EARoleType.developer;
        //        roleTypeVisibilityOttenuta = service.getEntityRoleType(ROLE_ENTITY_CLASS);
        //        assertEquals(roleTypeVisibilityPrevista, roleTypeVisibilityOttenuta);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the roleTypeVisibility of the View class.
     * La Annotation @AIView ha un suo valore di default per la property @AIView.roleTypeVisibility()
     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
     *
     * @param clazz the entity class
     *
     * @return the roleTypeVisibility of the class
     */
    @Test
    public void getViewRoleType() {
        //        EARoleType roleTypeVisibilityOttenuta = null;
        //        EARoleType roleTypeVisibilityPrevista = EARoleType.user;
        //        roleTypeVisibilityOttenuta = service.getViewRoleType(ROLE_VIEW_CLASS_LIST);
        //        assertEquals(roleTypeVisibilityPrevista, roleTypeVisibilityOttenuta);
        //
        //        roleTypeVisibilityPrevista = EARoleType.user;
        //        roleTypeVisibilityOttenuta = service.getViewRoleType(ROLE_VIEW_CLASS_FORM);
        //        assertEquals(roleTypeVisibilityPrevista, roleTypeVisibilityOttenuta);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the accessibility status of the class for the developer login.
     * Viene usata come default, se manca il valore specifico del singolo field
     * La Annotation @AIForm ha un suo valore di default per la property @AIForm.fieldsDev()
     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
     *
     * @param clazz the entity class
     *
     * @return accessibilità del Form
     */
    @Test
    public void getFormAccessibilityDev() {
        //        previstaAccessibilità = EAFieldAccessibility.allways;
        //        ottenutaAccessibilità = service.getFormAccessibilityDev(ROLE_ENTITY_CLASS);
        //        assertEquals(previstaAccessibilità, ottenutaAccessibilità);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the accessibility status of the class for the admin login.
     * Viene usata come default, se manca il valore specifico del singolo field
     * La Annotation @AIForm ha un suo valore di default per la property @AIForm.fieldsAdmin()
     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
     *
     * @param clazz the entity class
     *
     * @return accessibilità del Form
     */
    @Test
    public void getFormAccessibilityAdmin() {
        //        previstaAccessibilità = EAFieldAccessibility.never;
        //        ottenutaAccessibilità = service.getFormAccessibilityAdmin(ROLE_ENTITY_CLASS);
        //        assertEquals(previstaAccessibilità, ottenutaAccessibilità);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the accessibility status of the class for the user login.
     * Viene usata come default, se manca il valore specifico del singolo field
     * La Annotation @AIForm ha un suo valore di default per la property @AIForm.fieldsUser()
     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
     *
     * @param clazz the entity class
     *
     * @return accessibilità del Form
     */
    @Test
    public void getFormAccessibilityUser() {
        //        previstaAccessibilità = EAFieldAccessibility.never;
        //        ottenutaAccessibilità = service.getFormAccessibilityUser(ROLE_ENTITY_CLASS);
        //        assertEquals(previstaAccessibilità, ottenutaAccessibilità);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the accessibility status of the field for the developer login.
     * La Annotation @AIField ha un suo valore di default per la property @AIField.dev()
     * Se il field lo prevede (valore di default) ci si rifà al valore generico del Form
     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return accessibilità del field
     */
    @Test
    public void getFieldAccessibilityDev() {
        //        previstaAccessibilità = EAFieldAccessibility.showOnly;
        //        ottenutaAccessibilità = service.getFieldAccessibilityDev(FIELD_ORDINE);
        //        assertEquals(previstaAccessibilità, ottenutaAccessibilità);
        //
        //        previstaAccessibilità = EAFieldAccessibility.allways;
        //        ottenutaAccessibilità = service.getFieldAccessibilityDev(FIELD_CODE);
        //        assertEquals(previstaAccessibilità, ottenutaAccessibilità);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the accessibility status of the field for the admin login.
     * La Annotation @AIField ha un suo valore di default per la property @AIField.admin()
     * Se il field lo prevede (valore di default) ci si rifà al valore generico del Form
     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return accessibilità del field
     */
    @Test
    public void getFieldAccessibilityAdmin() {
        //        previstaAccessibilità = EAFieldAccessibility.never;
        //        ottenutaAccessibilità = service.getFieldAccessibilityAdmin(FIELD_ORDINE);
        //        assertEquals(previstaAccessibilità, ottenutaAccessibilità);
        //
        //        previstaAccessibilità = EAFieldAccessibility.showOnly;
        //        ottenutaAccessibilità = service.getFieldAccessibilityAdmin(FIELD_CODE);
        //        assertEquals(previstaAccessibilità, ottenutaAccessibilità);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the accessibility status of the field for the user login.
     * La Annotation @AIField ha un suo valore di default per la property @AIField.user()
     * Se il field lo prevede (valore di default) ci si rifà al valore generico del Form
     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return accessibilità del field
     */
    @Test
    public void getFieldAccessibilityUser() {
        //        previstaAccessibilità = EAFieldAccessibility.never;
        //        ottenutaAccessibilità = service.getFieldAccessibilityUser(FIELD_ORDINE);
        //        assertEquals(previstaAccessibilità, ottenutaAccessibilità);
        //
        //        previstaAccessibilità = EAFieldAccessibility.never;
        //        ottenutaAccessibilità = service.getFieldAccessibilityUser(FIELD_CODE);
        //        assertEquals(previstaAccessibilità, ottenutaAccessibilità);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the accessibility status of the field for the current login.
     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return accessibilità del field
     */
    @Test
    public void getFieldAccessibility() {
        //        //--developer
        //        session.setDeveloper(true);
        //        session.setAdmin(false);
        //        session.setUser(false);
        //        previstaAccessibilità = EAFieldAccessibility.showOnly;
        //        ottenutaAccessibilità = service.getFieldAccessibility(FIELD_ORDINE);
        //        assertEquals(previstaAccessibilità, ottenutaAccessibilità);
        //        previstaAccessibilità = EAFieldAccessibility.allways;
        //        ottenutaAccessibilità = service.getFieldAccessibility(FIELD_CODE);
        //        assertEquals(previstaAccessibilità, ottenutaAccessibilità);
        //
        //        //--admin Non funziona, perché ASessionService usa VaadinSession.getCurrent();
        //        session.setDeveloper(false);
        //        session.setAdmin(true);
        //        session.setUser(false);
        //        previstaAccessibilità = EAFieldAccessibility.never;
        //        ottenutaAccessibilità = service.getFieldAccessibility(FIELD_ORDINE);
        ////        assertEquals(previstaAccessibilità, ottenutaAccessibilità);
        //        previstaAccessibilità = EAFieldAccessibility.showOnly;
        //        ottenutaAccessibilità = service.getFieldAccessibility(FIELD_CODE);
        ////        assertEquals(previstaAccessibilità, ottenutaAccessibilità);
        //
        //        //--user Non funziona, perché ASessionService usa VaadinSession.getCurrent();
        //        session.setDeveloper(false);
        //        session.setAdmin(false);
        //        session.setUser(true);
        //        previstaAccessibilità = EAFieldAccessibility.never;
        //        ottenutaAccessibilità = service.getFieldAccessibility(FIELD_ORDINE);
        ////        assertEquals(previstaAccessibilità, ottenutaAccessibilità);
        //        previstaAccessibilità = EAFieldAccessibility.never;
        //        ottenutaAccessibilità = service.getFieldAccessibility(FIELD_CODE);
        ////        assertEquals(previstaAccessibilità, ottenutaAccessibilità);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the roleTypeVisibility of the field.
     * La Annotation @AIField ha un suo valore di default per la property @AIField.roleTypeVisibility()
     * Se il field lo prevede (valore di default) ci si rifà al valore generico del Form
     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
     *
     * @param reflectionJavaField di riferimento per estrarre le Annotation
     *
     * @return the ARoleType of the field
     */
    @Test
    public void getFieldRoleType() {
        //        EARoleType roleTypeVisibilityOttenuta = null;
        //        EARoleType roleTypeVisibilityPrevista = EARoleType.developer;
        //        roleTypeVisibilityOttenuta = service.getFieldRoleType(FIELD_ORDINE);
        //        assertEquals(roleTypeVisibilityPrevista, roleTypeVisibilityOttenuta);
    }// end of single test


    @SuppressWarnings("javadoc")
    /**
     * Get the visibility of the field.
     * Controlla il ruolo del login connesso
     *
     * @param reflectionJavaField di riferimento per estrarre le Annotation
     *
     * @return the visibility of the field
     */
    @Test
    public void isFieldVisibileRole() {
        //        //--developer
        //        session.setDeveloper(true);
        //        session.setAdmin(false);
        //        session.setUser(false);
        //        previstoBooleano = true;
        //        ottenutoBooleano = service.isFieldVisibileRole(FIELD_ORDINE);
        //        assertEquals(previstoBooleano, ottenutoBooleano);
        //
        //        //--admin
        //        session.setDeveloper(false);
        //        session.setAdmin(true);
        //        session.setUser(false);
        //        previstoBooleano = true;
        //        ottenutoBooleano = service.isFieldVisibileRole(FIELD_ORDINE);
        //        assertEquals(previstoBooleano, ottenutoBooleano);
        //
        //        //--user Non funziona, perché ASessionService usa VaadinSession.getCurrent();
        //        session.setDeveloper(false);
        //        session.setAdmin(false);
        //        session.setUser(true);
        //        previstoBooleano = false;
        //        ottenutoBooleano = service.isFieldVisibileRole(FIELD_ORDINE);
        ////        assertEquals(previstoBooleano, ottenutoBooleano);
    }// end of single test


    /**
     * Lista dei fields statici PUBBLICI dichiarati in una classe di tipo AEntity, che usano @DBRef <br>
     * Controlla che il parametro in ingresso non sia nullo <br>
     * Ricorsivo. Comprende la entity e tutte le sue superclassi (fino a ACEntity e AEntity) <br>
     * Esclusi i fields: PROPERTY_SERIAL, PROPERT_NOTE, PROPERTY_CREAZIONE, PROPERTY_MODIFICA <br>
     * Esclusi i fields PRIVATI <br>
     * Fields NON ordinati <br>
     * Class.getDeclaredFields() prende fields pubblici e privati della classe <br>
     * Class.getFields() prende fields pubblici della classe e delle superclassi
     * Nomi NON ordinati <br>
     * ATTENZIONE - Comprende ANCHE eventuali fields statici pubblici che NON siano property per il DB <br>
     *
     * @param entityClazz da cui estrarre i fields statici
     *
     * @return lista di static fields della Entity e di tutte le sue superclassi, che usano @DBRef
     */
    @Test
    public void getDBRefFields() {
        previstoIntero = 1;
        listaFields = service.getDBRefFields(ANNO_ENTITY_CLASS);
        assertNotNull(listaFields);
        assertEquals(previstoIntero, listaFields.size());
    }

    @Test
    @Order(2)
    @DisplayName("2 - isEntityClass")
    public void isEntityClass() {
        String canonicalName = VUOTA;

        ottenutoBooleano = service.isEntityClass((Class) null);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isEntityClass(ANNO_ENTITY_CLASS);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = service.isEntityClass(ANNO_LOGIC_LIST);
        assertFalse(ottenutoBooleano);

        canonicalName = ANNO_ENTITY_CLASS.getCanonicalName();
        ottenutoBooleano = service.isEntityClass(canonicalName);
        assertTrue(ottenutoBooleano);

        canonicalName = ANNO_LOGIC_LIST.getCanonicalName();
        ottenutoBooleano = service.isEntityClass(canonicalName);
        assertFalse(ottenutoBooleano);
    }

    @Test
    @Order(3)
    @DisplayName("3 - getSortSpring")
    public void getSortSpring() {
        Sort.Direction previstoDirection;
        Sort.Direction ottenutoDirection;

        previsto = NAME_ORDINE;
        previstoDirection = Sort.Direction.DESC;
        sortSpring = service.getSortSpring(ANNO_ENTITY_CLASS);
        Assert.assertNotNull(sortSpring);
        ottenuto = utilityService.getSortFieldName(sortSpring);
        Assert.assertEquals(previsto, ottenuto);
        ottenutoDirection = utilityService.getSortDirection(sortSpring);
        Assert.assertEquals(previstoDirection, ottenutoDirection);

        previsto = NAME_ORDINE;
        previstoDirection = Sort.Direction.ASC;
        sortSpring = service.getSortSpring(VIA_ENTITY_CLASS);
        Assert.assertNotNull(sortSpring);
        ottenuto = utilityService.getSortFieldName(sortSpring);
        Assert.assertEquals(previsto, ottenuto);
        ottenutoDirection = utilityService.getSortDirection(sortSpring);
        Assert.assertEquals(previstoDirection, ottenutoDirection);

        previsto = NAME_CODE;
        previstoDirection = Sort.Direction.ASC;
        sortSpring = service.getSortSpring(COMPANY_ENTITY_CLASS);
        Assert.assertNotNull(sortSpring);
        ottenuto = utilityService.getSortFieldName(sortSpring);
        Assert.assertEquals(previsto, ottenuto);
        ottenutoDirection = utilityService.getSortDirection(sortSpring);
        Assert.assertEquals(previstoDirection, ottenutoDirection);
    }

}
