package it.algos.vaadwiki;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import it.algos.vaadflow14.backend.annotation.AIScript;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 16-apr-2021
 * <p>
 * The entry point of the Spring Boot application. <br>
 * Spring boot web application initializer. <br>
 * <p>
 * Questa classe contiene il metodo 'main' che è il punto di ingresso di una applicazione Java <br>
 * In fase di sviluppo si possono avere diverse configurazioni, ognuna delle quali punta un ''main' diverso <br>
 * Nel JAR finale (runtime) si può avere una sola classe col metodo 'main' <br>
 * Nel WAR finale (runtime) occorre (credo) inserire dei servlet di context diversi <br>
 * Senza @ComponentScan, SpringBoot non 'vede' le classi con @SpringView che sono in una directory diversa da questo package <br>
 * <p>
 * Questa classe non fa praticamente niente se non avere le Annotation riportate qui <br>
 * Annotated with @SpringBootApplication (obbligatorio) <br>
 * Annotated with @EnableVaadin (obbligatorio) <br>
 * Annotated with @EntityScan (obbligatorio) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * <p>
 * Tutte le view devono essere comprese nel path di questa classe (directory interne incluse) <br>
 * Una sola view può avere @Route("") <br>
 * <p>
 * Spring Boot introduces the @SpringBootApplication annotation. <br>
 * This single annotation is equivalent to using @Configuration, @EnableAutoConfiguration, and @ComponentScan. <br>
 * Se l'applicazione NON usa la security, aggiungere exclude = {SecurityAutoConfiguration.class} a @SpringBootApplication <br>
 */
@SpringBootApplication(scanBasePackages = {"it.algos"}, exclude = {SecurityAutoConfiguration.class})
@EnableVaadin({"it.algos"})
@EntityScan({"it.algos"})
@AIScript(sovraScrivibile = false)
public class WikiApplication extends SpringBootServletInitializer {

    /**
     * Constructor
     *
     * @param args eventuali parametri in ingresso
     */
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(WikiApplication.class, args);
    }// end of SpringBoot constructor


}// end of main class