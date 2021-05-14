package it.algos.vaadflow14.backend.login;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import it.algos.vaadflow14.backend.application.FlowCost;
import it.algos.vaadflow14.backend.application.FlowVar;

import javax.annotation.PostConstruct;
import java.util.Collections;

import static it.algos.vaadflow14.backend.application.FlowCost.ROUTE_NAME_LOGIN;
import static it.algos.vaadflow14.backend.application.FlowVar.*;

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
