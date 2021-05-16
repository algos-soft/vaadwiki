package it.algos.vaadflow14.backend.packages.security.utente;

import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;

import javax.validation.constraints.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 20-ago-2020
 * Time: 07:09
 * <p>
 * Classe (obbligatoria) di un package <br>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 * Le properties sono PUBLIC per poter usare la Reflection <br>
 * Unica classe obbligatoria per un package. <br>
 * Le altre servono solo se si vuole qualcosa in più dello standard minimo. <br>
 * <p>
 * Annotated with Spring: @SpringComponent (vaadin), @QueryEntity (querydsl), @Document (mongodb), @TypeAlias (data) <br>
 * Annotated with @SpringComponent, @QueryEntity, @Document, @TypeAlias <br>
 * Annotated with Lombok: @Data, @NoArgsConstructor, @AllArgsConstructor, @Builder, @EqualsAndHashCode <br>
 * Annotated with Algos: @AIScript per controllare il typo di file e la ri-creazione con Wizard <br>
 * Annotated with Algos: @AIEntity per informazioni sulle property per il DB <br>
 * Annotated with Algos: @AIView per info su menu, icon, route, search e sort <br>
 * Annotated with Algos: @AIList per info sulla Grid e sulle colonne <br>
 * Annotated with Algos: @AIForm per info sul Form e sulle properties <br>
 */
@SpringComponent
@QueryEntity
@Document(collection = "utente")
@TypeAlias("utente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderUtente")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovraScrivibile = false)
@AIEntity(recordName = "Utente", keyPropertyName = "username", usaCompany = true)
@AIView(menuName = "Utente", menuIcon = VaadinIcon.USERS, searchProperty = "username", sortProperty = "username")
@AIList(fields = "username,enabled,role,accountNonExpired,accountNonLocked,credentialsNonExpired", usaRowIndex = true)
@AIForm(fields = "username,password,enabled,role,accountNonExpired,accountNonLocked,credentialsNonExpired", usaSpostamentoTraSchede = true)
public class Utente extends ACEntity implements UserDetails {

    private static final int WIDTH = 5;

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * username o nickName (obbligatorio, unico)
     */
    @NotBlank()
    @Size(min = 3)
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, focus = true)
    @AIColumn(widthEM = 12)
    public String username;

    /**
     * password in chiaro (obbligatoria, non unica)
     * con inserimento automatico (prima del 'save') se è nulla
     */
    @NotBlank()
    @AIField(type = AETypeField.password)
    @AIColumn(header = "pass")
    public String password;

    /**
     * flag abilitato (facoltativo, di default true)
     */
    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolField.checkBox)
    @AIColumn(typeBool = AETypeBoolCol.checkIcon, header = "OK", widthEM = WIDTH)
    public boolean enabled;

    /**
     * role authority (obbligatorio se FlowVar.usaCompany=true, di default user)
     */
    @AIField(type = AETypeField.enumeration, enumClazz = AERole.class)
    @AIColumn()
    public AERole role;


    /**
     * flag account valido (facoltativo, di default true)
     */
    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolField.checkBox)
    @AIColumn(typeBool = AETypeBoolCol.checkBox, header = "Expr", widthEM = WIDTH)
    public boolean accountNonExpired;


    /**
     * flag account non bloccato (facoltativo, di default true)
     */
    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolField.checkBox)
    @AIColumn(typeBool = AETypeBoolCol.checkBox, header = "Lock", widthEM = WIDTH)
    public boolean accountNonLocked;


    /**
     * flag credenziali non scadute (facoltativo, di default true)
     */
    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolField.checkBox)
    @AIColumn(typeBool = AETypeBoolCol.checkBox, header = "Cexp", flexGrow = true)
    public boolean credentialsNonExpired;


    /**
     * GrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> listAuthority = new ArrayList<>();
        GrantedAuthority authority;

        authority = new SimpleGrantedAuthority("user");
        listAuthority.add(authority);

        return listAuthority;
    }


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getUsername();
    }

}