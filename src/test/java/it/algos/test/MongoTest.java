package it.algos.test;

import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.anagrafica.via.*;
import it.algos.vaadflow14.backend.packages.company.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadflow14.backend.packages.crono.mese.*;
import it.algos.vaadflow14.backend.packages.geografica.continente.*;
import it.algos.vaadflow14.backend.packages.geografica.regione.*;
import it.algos.vaadflow14.backend.packages.geografica.stato.*;
import it.algos.vaadflow14.backend.packages.security.utente.*;
import it.algos.vaadflow14.backend.wrapper.*;
import static org.junit.Assert.*;
import org.junit.jupiter.params.provider.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 11-ott-2021
 * Time: 17:13
 */
public abstract class MongoTest extends ATest {

    //--da regolare per mostrare errori previsti oppure nasconderli per velocizzare
    //--da usare SOLO come controllo per errori previsti
    protected boolean flagRisultatiEsattiObbligatori = true;


    protected static String[] NOMI() {
        return new String[]{VUOTA, "pomeriggio", Via.class.getSimpleName(), Via.class.getCanonicalName(), "via", "Via", Via.class.getSimpleName().toLowerCase(Locale.ROOT), "Utente", "LogicList"};
    }

    //--clazz
    //--previstoIntero
    //--risultatoEsatto
    protected static Stream<Arguments> CLAZZ() {
        return Stream.of(
                Arguments.of(null, 0, false),
                Arguments.of(LogicList.class, 0, false),
                Arguments.of(Utente.class, 0, false),
                Arguments.of(Mese.class, 12, true),
                Arguments.of(AIType.class, 0, true),
                Arguments.of(Company.class, 3, false),
                Arguments.of(Stato.class, 249, true)
        );
    }


    //--clazz
    //--previstoIntero
    //--risultatoEsatto
    //--offset (eventuale)
    //--limit (eventuale)
    protected static Stream<Arguments> CLAZZ_OFFSET() {
        return Stream.of(
                Arguments.of(null, 0, false, 0, 0),
                Arguments.of(LogicList.class, 0, false, 0, 0),
                Arguments.of(Utente.class, 0, false, 0, 0),
                Arguments.of(Mese.class, 12, true, 0, 0),
                Arguments.of(Mese.class, 12, true, 5, 2),
                Arguments.of(Mese.class, 12, true, 2, 0),
                Arguments.of(Mese.class, 12, true, 0, 2),
                Arguments.of(Giorno.class, 366, true, 31, 28),
                Arguments.of(Via.class, 26, false, 8, 4),
                Arguments.of(AIType.class, 0, true, 0, 0),
                Arguments.of(Company.class, 3, false, 0, 0),
                Arguments.of(Stato.class, 249, true, 0, 0),
                Arguments.of(Stato.class, 249, true, 150, 5),
                Arguments.of(Stato.class, 249, true, 240, 0),
                Arguments.of(Continente.class, 7, true, 2, 3)
        );
    }

    protected static Stream<Arguments> CLAZZ_KEY_ID() {
        return Stream.of(
                Arguments.of((Class) null, VUOTA, false),
                Arguments.of(Utente.class, VUOTA, false),
                Arguments.of(Mese.class, null, false),
                Arguments.of(Mese.class, VUOTA, false),
                Arguments.of(Mese.class, "termidoro", false),
                Arguments.of(Giorno.class, "2agosto", true),
                Arguments.of(Giorno.class, "2 agosto", false),
                Arguments.of(Anno.class, "104", true),
                Arguments.of(Mese.class, "marzo", true),
                Arguments.of(Mese.class, "Marzo", true),
                Arguments.of(Mese.class, "marzo esatto", false),
                Arguments.of(Regione.class, "calabria", true),
                Arguments.of(Regione.class, "Calabria", true)
        );
    }

    //--clazz
    //--propertyName
    //--propertyValue
    //--previstoIntero
    //--doc e/o entityBean valida
    protected static Stream<Arguments> CLAZZ_PROPERTY() {
        return Stream.of(
                Arguments.of((Class) null, VUOTA, null, 0, false),
                Arguments.of(Utente.class, VUOTA, null, 0, false),
                Arguments.of(LogicList.class, null, null, 0, false),
                Arguments.of(AIType.class, null, null, 0, false),
                Arguments.of(Mese.class, VUOTA, null, 0, false),
                Arguments.of(Mese.class, "manca", null, 0, false),
                Arguments.of(Mese.class, "manca", 31, 0, false),
                Arguments.of(Mese.class, "mese", "pippoz", 0, false),
                Arguments.of(Mese.class, "mese", null, 0, false),
                Arguments.of(Mese.class, "mese", VUOTA, 0, false),
                Arguments.of(Giorno.class, "_id", "2agosto", 1, true),
                Arguments.of(Giorno.class, "_id", "2 agosto", 0, false),
                Arguments.of(Giorno.class, "mese", "ottobre", 31, true),
                Arguments.of(Mese.class, "mese", "ottobre", 1, true),
                Arguments.of(Mese.class, "giorni", 31, 7, false),
                Arguments.of(Mese.class, "giorni", 30, 4, false),
                Arguments.of(Mese.class, "giorni", 28, 1, true),
                Arguments.of(Mese.class, "giorni", 32, 0, false),
                Arguments.of(Via.class, "belzebù", "piazza", 0, false),
                Arguments.of(Via.class, "nome", "belzebù", 0, false),
                Arguments.of(Via.class, "nome", "piazza", 1, true)
        );
    }

    //--clazz
    //--typeFilter
    //--propertyName
    //--propertyValue
    //--previstoIntero
    protected static Stream<Arguments> CLAZZ_FILTER() {
        return Stream.of(
                Arguments.of((Class) null, null, VUOTA, VUOTA, 0),
                Arguments.of(Utente.class, null, null, null, 0),
                Arguments.of(Utente.class, AETypeFilter.uguale, null, null, 0),
                Arguments.of(Utente.class, AETypeFilter.uguale, VUOTA, VUOTA, 0),
                Arguments.of(Utente.class, AETypeFilter.uguale, NAME_ANNO, "forse", 0),
                Arguments.of(Utente.class, AETypeFilter.uguale, NAME_NOME, "forse", 0),
                Arguments.of(Giorno.class, AETypeFilter.link, null, "ottobre", 0),
                Arguments.of(Giorno.class, AETypeFilter.link, "mese", "ottobre", 31),
                Arguments.of(Giorno.class, AETypeFilter.link, "mese.$id", "ottobre", 31),
                Arguments.of(Mese.class, AETypeFilter.uguale, "giorni", 31, 7),
                Arguments.of(Mese.class, AETypeFilter.uguale, "giorni", 30, 4),
                Arguments.of(Mese.class, AETypeFilter.uguale, "giorni", 28, 1),
                Arguments.of(Mese.class, AETypeFilter.uguale, "giorni", 32, 0),
                Arguments.of(Via.class, AETypeFilter.uguale, NAME_NOME, "viale", 1),
                Arguments.of(Via.class, AETypeFilter.uguale, NAME_NOME, "viale ", 0),
                Arguments.of(Via.class, AETypeFilter.uguale, NAME_NOME, " viale", 0),
                Arguments.of(Via.class, AETypeFilter.contiene, NAME_NOME, "viale", 1),
                Arguments.of(Via.class, AETypeFilter.uguale, NAME_NOME, "belzebù", 0),
                Arguments.of(Via.class, AETypeFilter.contiene, NAME_NOME, "co", 6),
                Arguments.of(Via.class, AETypeFilter.inizia, NAME_NOME, "v", 4),
                Arguments.of(Mese.class, AETypeFilter.inizia, "mese", "m", 2)
        );
    }

    protected void printCount(final String simpleName, final int size, final String property, final Object value) {
        System.out.println(String.format(String.format("La classe %s ha %s entities filtrate con %s=%s", simpleName, size, property, value)));
        System.out.println(VUOTA);
    }

    protected void printCount(final Class clazz, final int previstoIntero, final int ottenutoIntero, final boolean risultatoEsatto) {
        if (clazz == null) {
            System.out.println("Manca la entityClazz");
            return;
        }
        if (ottenutoIntero == previstoIntero) {
            if (risultatoEsatto) {
                System.out.println(String.format("La collezione '%s' contiene %s records (entities) totali che sono esattamente quelli previsti (obbligatori)", clazz.getSimpleName(), ottenutoIntero));
            }
            else {
                System.out.println(String.format("La collezione '%s' contiene %s records (entities) totali che sono uguali a quelli indicativamente previsti (facoltativi)", clazz.getSimpleName(), ottenutoIntero));
            }
        }
        else {
            if (ottenutoIntero > previstoIntero) {
                if (risultatoEsatto) {
                    System.out.println(String.format("La collezione '%s' contiene %s records (entities) totali che sono più dei %s previsti e non va bene", clazz.getSimpleName(), ottenutoIntero, previstoIntero));
                }
                else {
                    System.out.println(String.format("La collezione '%s' contiene %s records (entities) totali che sono più dei %s indicativamente previsti", clazz.getSimpleName(), ottenutoIntero, previstoIntero));
                }
            }
            else {
                if (risultatoEsatto) {
                    System.out.println(String.format("La collezione '%s' contiene %s records (entities) totali che sono meno dei %s previsti e non va bene", clazz.getSimpleName(), ottenutoIntero, previstoIntero));
                }
                else {
                    System.out.println(String.format("La collezione '%s' contiene %s records (entities) totali che sono meno dei %s indicativamente previsti", clazz.getSimpleName(), ottenutoIntero, previstoIntero));
                }
            }
        }

        if (risultatoEsatto) {
            assertEquals(previstoIntero, ottenutoIntero);
        }
    }

    protected void printCount(final Class clazz, final String propertyName, final Serializable propertyValue, final int previstoIntero, final int ottenutoIntero) {
        String clazzName;
        if (clazz == null) {
            System.out.println(String.format("Manca la entityClazz"));
            return;
        }
        else {
            clazzName = clazz.getSimpleName();
        }

        if (ottenutoIntero == previstoIntero) {
            System.out.println(String.format("La collezione '%s' contiene %s records (entities) filtrati con %s=%s che sono quelli previsti", clazzName, ottenutoIntero, propertyName, propertyValue));
        }
        else {
            System.out.println(String.format("La collezione '%s' contiene %s records (entities) filtrati con %s=%s che non sono i %s previsti", clazzName, ottenutoIntero, propertyName, propertyValue, previstoIntero));
        }
    }


    protected void printCount(final Class clazz, AFiltro filtro, final int previstoIntero, final int ottenutoIntero) {
        String clazzName;
        String key = filtro.getCriteria().getCriteriaObject().keySet().toString();
        String value = filtro.getCriteria().getCriteriaObject().values().toString();

        if (clazz == null) {
            System.out.println(String.format("Manca la entityClazz"));
            return;
        }
        else {
            clazzName = clazz.getSimpleName();
        }

        if (ottenutoIntero == previstoIntero) {
            System.out.println(String.format("La collezione '%s' contiene %s records (entities) filtrati con %s = %s che sono quelli previsti", clazzName, ottenutoIntero, key, value));
        }
        else {
            System.out.println(String.format("La collezione '%s' contiene %s records (entities) filtrati con %s = %s che non sono i %s previsti", clazzName, ottenutoIntero, key, value, previstoIntero));
        }
    }

    protected void printCount(final Class clazz, Map<String, AFiltro> mappaFiltri, final int previstoIntero, final int ottenutoIntero) {
        String clazzName;
        AFiltro filtro;
        String property;
        String value;
        if (clazz == null) {
            System.out.println(String.format("Manca la entityClazz"));
            return;
        }
        else {
            clazzName = clazz.getSimpleName();
        }

        if (ottenutoIntero == previstoIntero) {
            System.out.println(String.format("La collezione '%s' contiene %s records (entities) che sono quelli previsti", clazzName, ottenutoIntero));
            for (String key : mappaFiltri.keySet()) {
                filtro = mappaFiltri.get(key);
                property = filtro.getCriteria().getCriteriaObject().keySet().toString();
                value = filtro.getCriteria().getCriteriaObject().values().toString();
                System.out.println(String.format("Filtro %s = %s", property, value));
            }
        }
        else {
            System.out.println(String.format("La collezione '%s' contiene %s records (entities) che non sono i %s previsti", clazzName, ottenutoIntero, previstoIntero));
            for (String key : mappaFiltri.keySet()) {
                filtro = mappaFiltri.get(key);
                property = filtro.getCriteria().getCriteriaObject().keySet().toString();
                value = filtro.getCriteria().getCriteriaObject().values().toString();
                System.out.println(String.format("Filtro %s = %s", property, value));
            }
        }
    }


    protected void printEntityBeanFromKeyId(final Class clazz, final Serializable keyPropertyValue, final AEntity entityBean, final int previstoIntero) {
        printEntityBeanFromProperty(clazz, FlowCost.FIELD_ID, keyPropertyValue, entityBean, previstoIntero);
    }

    protected void printEntityBeanFromProperty(final Class clazz, final String propertyName, final Serializable propertyValue, final AEntity entityBean, final int previstoIntero) {
        if (clazz == null) {
            System.out.print("Non esiste la classe indicata");
            System.out.println(VUOTA);
            System.out.println(VUOTA);
            return;
        }

        if (entityBean == null) {
            System.out.println(String.format("non è stata creata nessuna entityBean di classe %s", clazz.getSimpleName()));
            System.out.println(VUOTA);
            System.out.println(VUOTA);
            return;
        }

        System.out.print(String.format("%s%s%s: ", propertyName, UGUALE_SEMPLICE, propertyValue));
        printMappa(entityBean);
    }

    protected void printLista(final List<AEntity> listaBean) {
        for (AEntity bean : listaBean) {
            System.out.println(bean);
        }
    }


    protected void printWrapFiltro(final Class clazz, final AETypeFilter filter, final String propertyName, final String propertyValue, final int previstoIntero, final int ottenutoIntero) {
        String message;
        if (clazz == null) {
            return;
        }

        String nota = filter != null ? filter.getOperazione(propertyName, propertyValue) : VUOTA;

        if (previstoIntero == 0) {
            nota = textService.isValid(nota) ? nota : "col filtro indicato";
            message = String.format("Nella entityClass %s non ho trovato nessuna entities %s", clazz.getSimpleName(), nota);
            System.out.println(message);
        }
        else {
            message = String.format("Nella entityClass %s ho trovato %d entities %s", clazz.getSimpleName(), ottenutoIntero, nota);
            System.out.println(message);
        }
    }

    protected void printWrapFiltro(final Class clazz, final int previstoIntero, final List<AEntity> listaBean, final boolean risultatoEsatto) {
        printWrapFiltro(clazz, null, VUOTA, VUOTA, previstoIntero, listaBean, risultatoEsatto);
    }

    protected void printWrapFiltro(final Class clazz, final AETypeFilter filter, final String propertyName, final String propertyValue, final int previstoIntero) {
        printWrapFiltro(clazz, filter, propertyName, propertyValue, previstoIntero, null);
    }

    protected void printWrapFiltro(final Class clazz, final AETypeFilter filter, final String propertyName, final String propertyValue, final int previstoIntero, final List<AEntity> listaBean) {
        printWrapFiltro(clazz, filter, propertyName, propertyValue, previstoIntero, listaBean, false);
    }

    protected void printWrapFiltro(final Class clazz, final String propertyName, final Serializable propertyValue, final int previstoIntero, final List<AEntity> listaBean) {
        printWrapFiltro(clazz, null, propertyName, propertyValue, previstoIntero, listaBean, true);
    }


    protected void printWrapFiltro(final Class clazz, final AETypeFilter filter, final String propertyName, final Serializable propertyValue, final int previstoIntero, final List<AEntity> listaBean, final boolean risultatoEsatto) {
        String message;
        String propertyValueVideo = getPropertyVideo(propertyValue);
        String nota = filter != null ? filter.getOperazione(propertyName, propertyValueVideo) : VUOTA;

        if (previstoIntero == 0) {
            nota = textService.isValid(nota) ? nota : "col filtro indicato";
            message = String.format("Nella entityClass %s non ho trovato nessuna entities%s", clazz.getSimpleName(), SPAZIO + nota);
            System.out.println(message);
        }
        else {
            nota = textService.isValid(nota) ? SPAZIO + nota + SPAZIO : SPAZIO;
            if (risultatoEsatto) {
                message = String.format("Nella entityClass %s ho trovato %d entities%sche sono esattamente quelle previste (obbligatorio)", clazz.getSimpleName(), listaBean.size(), nota);
            }
            else {
                message = String.format("Nella entityClass %s ho trovato %d entities%sche sono indicativamente quelle previste (facoltativo)", clazz.getSimpleName(), listaBean.size(), nota);
            }
            System.out.println(message);
            printLista(listaBean);
        }
    }

    protected void printLimit(final Class clazz, final int limit, final List<AEntity> listaBean) {
        String message;

        System.out.println(VUOTA);
        if (limit == 0) {
            message = String.format("Nella entityClass %s non ho trovato nessuna entities perché il filtro è nullo (limit=0)", clazz.getSimpleName());
            System.out.println(message);
        }
        else {
            printWrapFiltro(clazz, limit, listaBean, true);
        }
    }

    protected String getPropertyVideo(final Serializable propertyValue) {
        String propertyValueVideo;
        if (propertyValue == null) {
            propertyValueVideo = "(null)";
        }
        else {
            if (propertyValue instanceof String && (propertyValue).equals(VUOTA)) {
                propertyValueVideo = "(vuota)";
            }
            else {
                propertyValueVideo = propertyValue + VUOTA;
            }
        }

        return propertyValueVideo;
    }

}
