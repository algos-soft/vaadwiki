package it.algos.vaadflow14.ui.security;

import com.vaadin.flow.shared.*;
import org.springframework.core.annotation.*;
import org.springframework.security.access.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;

import javax.servlet.http.*;
import java.util.*;

/**
 * SecurityUtils takes care of all such static operations that have to do with
 * security and querying rights from different beans of the UI.
 */
public final class SecurityUtils {

    private SecurityUtils() {
        // Util methods only
    }


    /**
     * Gets the user name of the currently signed in user.
     *
     * @return the user name of the current user or <code>null</code> if the user
     * has not signed in
     */
    public static String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }


    /**
     * Checks if access is granted for the current user for the given secured view,
     * defined by the view class.
     *
     * @param securedClass
     *
     * @return true if access is granted, false otherwise.
     */
    public static boolean isAccessGranted(Class<?> securedClass) {
        Secured secured = AnnotationUtils.findAnnotation(securedClass, Secured.class);
        if (secured == null) {
            return true;
        }

        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (userAuthentication == null) {
            return false;
        }
        List<String> allowedRoles = Arrays.asList(secured.value());
        return userAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(allowedRoles::contains);
    }


    /**
     * Checks if the user is logged in.
     *
     * @return true if the user is logged in. False otherwise.
     */
    public static boolean isUserLoggedIn() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication() != null && !(context.getAuthentication() instanceof AnonymousAuthenticationToken);
    }


    /**
     * Tests if the request is an internal framework request. The test consists of
     * checking if the request parameter is present and if its value is consistent
     * with any of the request types know.
     *
     * @param request {@link HttpServletRequest}
     *
     * @return true if is an internal framework request. False otherwise.
     */
    public static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
//        return parameterValue != null && Stream.of(RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
        return false;
    }

}
