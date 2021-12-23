package it.algos.vaadflow14.backend.login;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.login.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import static it.algos.vaadflow14.backend.application.FlowVar.*;

import javax.annotation.*;
import java.util.*;

/**
 * The Login view
 */
@Tag("sa-login-view")
@Route(value = ROUTE_NAME_LOGIN)
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {


	//--componente di Vaadin flow invocato dall' Annotation @Tag("sa-login-view")
	private LoginOverlay loginOverlay = new LoginOverlay();

	@PostConstruct
	protected void postConstruct() {

		loginOverlay.setAction(ROUTE_NAME_LOGIN);

		// personalizza il branding
		loginOverlay.setTitle(projectNameUpper);
		loginOverlay.setDescription(projectDescrizione);

		// non mostra bottone lost password
		loginOverlay.setForgotPasswordButtonVisible(false);

		// personalizza i messaggi
		LoginI18n i18n = LoginI18n.createDefault();
		LoginI18n.ErrorMessage errore = new LoginI18n.ErrorMessage();
		errore.setTitle("Riprova");
		errore.setMessage("Username o password non corretti");
		i18n.setErrorMessage(errore);
		loginOverlay.setI18n(i18n);

		// apre l' overlay
		loginOverlay.setOpened(true);

	}


	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		// inform the user about an authentication error
		// (yes, the API for resolving query parameters is annoying...)
		if (!event.getLocation().getQueryParameters().getParameters().getOrDefault("error", Collections.emptyList()).isEmpty()) {
			loginOverlay.setError(true);
		}
	}

}
