package it.algos.vaadwiki.security;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.role.RoleService;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadflow.service.ABootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Implements the {@link UserDetailsService}.
 * <p>
 * This implementation searches for {@link Utente} entities by the e-mail address
 * supplied in the login screen.
 */
@Service
@Primary
@AIScript(sovrascrivibile = false)
public class AUserDetailsService implements UserDetailsService {

    private final UtenteService utenteService;
    private final RoleService roleService;

    public PasswordEncoder passwordEncoder;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected ABootService boot;

    @Autowired
    public AUserDetailsService( UtenteService utenteService, RoleService roleService) {
        this.utenteService = utenteService;
        this.roleService = roleService;
    }// end of Spring constructor

    /**
     * Recovers the {@link Utente} from the database using the e-mail address supplied
     * in the login screen. If the user is found, returns a
     * {@link org.springframework.security.core.userdetails.User}.
     *
     * @param username User's e-mail address
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String passwordHash = "";
        Collection<? extends GrantedAuthority> authorities;
        Utente utente = (Utente) utenteService.findById(username);

        if (null == utente) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            passwordHash = passwordEncoder.encode(utente.getPassword());
            authorities = roleService.getAuthorities(utente);
            return new User(utente.getUsername(), passwordHash, authorities);
        }// end of if/else cycle

    }// end of method

}// end of class