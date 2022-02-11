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
import it.algos.vaadflow14.backend.packages.crono.secolo.*;
import it.algos.vaadflow14.backend.packages.geografica.continente.*;
import it.algos.vaadflow14.backend.packages.geografica.regione.*;
import it.algos.vaadflow14.backend.packages.geografica.stato.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import it.algos.vaadflow14.backend.packages.security.utente.*;
import it.algos.vaadflow14.backend.packages.utility.versione.*;
import it.algos.vaadflow14.backend.wrapper.*;
import static org.junit.Assert.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.data.domain.*;

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
    //--tipologia della query
    protected static Stream<Arguments> CLAZZ() {
        return Stream.of(
                Arguments.of(null, 0, false, WrapQuery.Type.nulla),
                Arguments.of(LogicList.class, 0, false, WrapQuery.Type.errata),
                Arguments.of(AIType.class, 0, true, WrapQuery.Type.errata),
                Arguments.of(Mese.class, 12, true, WrapQuery.Type.validaSenzaFiltri),
                Arguments.of(Stato.class, 249, true, WrapQuery.Type.validaSenzaFiltri),
                Arguments.of(Giorno.class, 366, true, WrapQuery.Type.validaSenzaFiltri),
                Arguments.of(Via.class, 25, false, WrapQuery.Type.validaSenzaFiltri)
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

    //--clazz
    //--keyValue
    //--doc e/o entityBean valida
    protected static Stream<Arguments> CLAZZ_KEY_ID() {
        return Stream.of(
                Arguments.of((Class) null, VUOTA, false),
                Arguments.of(Utente.class, VUOTA, false),
                Arguments.of(Mese.class, null, false),
                Arguments.of(Mese.class, VUOTA, false),
                Arguments.of(Mese.class, "termidoro", false),
                Arguments.of(Giorno.class, "2agosto", true),
                Arguments.of(Giorno.class, "2 agosto", true),
                Arguments.of(Anno.class, "104", true),
                Arguments.of(Mese.class, "marzo", true),
                Arguments.of(Mese.class, "Marzo", true),
                Arguments.of(Mese.class, "marzo esatto", false),
                Arguments.of(Regione.class, "calabria", true),
                Arguments.of(Regione.class, "Calabria", true),
                Arguments.of(Versione.class, "Setup", true),
                Arguments.of(Versione.class, "setup", false),
                Arguments.of(Preferenza.class, "usaDebug", true),
                Arguments.of(Preferenza.class, "usadebug", false)
        );
    }

    //--clazz
    //--propertyField
    //--propertyValue
    //--previstoIntero
    //--doc e/o entityBean valida
    //--tipologia della query
    protected static Stream<Arguments> CLAZZ_PROPERTY() {
        return Stream.of(
                Arguments.of((Class) null, VUOTA, null, 0, false, WrapQuery.Type.nulla),
                Arguments.of(LogicList.class, null, null, 0, false, WrapQuery.Type.errata),
                Arguments.of(AIType.class, null, null, 0, false, WrapQuery.Type.errata),
                Arguments.of(Utente.class, VUOTA, null, 0, false, WrapQuery.Type.incompleta),
                Arguments.of(Utente.class, "username", null, 0, false, WrapQuery.Type.validaSenzaFiltri),
                Arguments.of(Mese.class, null, null, 12, false, WrapQuery.Type.incompleta),
                Arguments.of(Mese.class, VUOTA, null, 12, false, WrapQuery.Type.incompleta),
                Arguments.of(Mese.class, "mese", null, 0, false, WrapQuery.Type.validaSenzaFiltri),
                Arguments.of(Mese.class, "mese", VUOTA, 0, false, WrapQuery.Type.validaSenzaFiltri),
                Arguments.of(Mese.class, "mese", "pippoz", 0, false, WrapQuery.Type.validaConFiltri),
                Arguments.of(Mese.class, "mese", "ottobre", 1, true, WrapQuery.Type.validaConFiltri),
                Arguments.of(Mese.class, "mese", "Ottobre", 0, false, WrapQuery.Type.validaConFiltri),
                Arguments.of(Mese.class, "propertyErrata", null, 0, false, WrapQuery.Type.incompleta),
                Arguments.of(Mese.class, "propertyMancante", 31, 0, false, WrapQuery.Type.incompleta),
                Arguments.of(Giorno.class, "_id", "2agosto", 1, true, WrapQuery.Type.validaConFiltri),
                Arguments.of(Giorno.class, "_id", "2 agosto", 0, true, WrapQuery.Type.validaConFiltri),
                Arguments.of(Giorno.class, "mese", "ottobre", 31, true, WrapQuery.Type.validaConFiltri),
                Arguments.of(Mese.class, "giorni", 31, 7, false, WrapQuery.Type.validaConFiltri),
                Arguments.of(Mese.class, "giorni", 30, 4, false, WrapQuery.Type.validaConFiltri),
                Arguments.of(Mese.class, "giorni", 28, 1, true, WrapQuery.Type.validaConFiltri),
                Arguments.of(Mese.class, "giorni", 32, 0, false, WrapQuery.Type.validaConFiltri),
                Arguments.of(Via.class, "belzebù", "piazza", 0, false, WrapQuery.Type.incompleta),
                Arguments.of(Via.class, "nome", "belzebù", 0, false, WrapQuery.Type.validaConFiltri),
                Arguments.of(Via.class, "nome", "piazza", 1, true, WrapQuery.Type.validaConFiltri),
                Arguments.of(Via.class, "nome", "Piazza", 0, false, WrapQuery.Type.validaConFiltri)
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
                Arguments.of(Mese.class, AETypeFilter.inizia, "mese", "m", 2),
                Arguments.of(Secolo.class, AETypeFilter.checkBox3Vie, "anteCristo", true, 20),
                Arguments.of(Secolo.class, AETypeFilter.checkBox3Vie, "anteCristo", false, 21),
                Arguments.of(Secolo.class, AETypeFilter.checkBox3Vie, "anteCristo", null, 41)
        );
    }

    //--clazz
    //--propertyName
    //--propertyValue
    //--previstoIntero
    //--sortSpring
    protected static Stream<Arguments> CLAZZ_SORT() {
        return Stream.of(
                Arguments.of((Class) null, null, VUOTA, 0, null),
                Arguments.of(Utente.class, null, null, 0, null),
                Arguments.of(Giorno.class, "mese", "ottobre", 31, Sort.by(Sort.Direction.ASC, "ordine")),
                Arguments.of(Giorno.class, "mese", "ottobre", 31, Sort.by(Sort.Direction.DESC, "ordine")),
                Arguments.of(Mese.class, "giorni", 31, 7, Sort.by(Sort.Direction.ASC, "ordine")),
                Arguments.of(Mese.class, "giorni", 31, 7, Sort.by(Sort.Direction.DESC, "ordine")),
                Arguments.of(Mese.class, "giorni", 31, 7, Sort.by(Sort.Direction.ASC, "mese")),
                Arguments.of(Mese.class, "giorni", 31, 7, Sort.by(Sort.Direction.DESC, "mese"))
        );
    }


    //--clazz
    //--typeFilter
    //--propertyName
    //--propertyValue
    protected static Stream<Arguments> CLAZZ_TYPE_FILTER() {
        return Stream.of(
                Arguments.of((Class) null, (AETypeFilter) null, VUOTA, VUOTA),
                Arguments.of(Mese.class, AETypeFilter.uguale, "mese", "marzo"),
                Arguments.of(Mese.class, AETypeFilter.contiene, "mese", "emb"),
                Arguments.of(Mese.class, AETypeFilter.uguale, "giorni", 30),
                Arguments.of(Mese.class, AETypeFilter.maggiore, "giorni", 30),
                Arguments.of(Mese.class, AETypeFilter.maggioreUguale, "giorni", 30),
                Arguments.of(Mese.class, AETypeFilter.minore, "giorni", 30),
                Arguments.of(Mese.class, AETypeFilter.minoreUguale, "giorni", 30),
                Arguments.of(Mese.class, AETypeFilter.diverso, "giorni", 30),
                Arguments.of(Mese.class, AETypeFilter.inizia, "mese", "m")
        );
    }

    //--clazz
    //--typeFilterUno
    //--propertyNameUno
    //--propertyValueUno
    //--typeFilterDue
    //--propertyNameDue
    //--propertyValueDue
    protected static Stream<Arguments> CLAZZ_TYPE_FILTER_DOPPI() {
        return Stream.of(
                Arguments.of((Class) null, null, VUOTA, VUOTA, null, VUOTA, VUOTA),
                Arguments.of(Mese.class, AETypeFilter.uguale, "mese", "settembre", AETypeFilter.minoreUguale, "giorni", 30),
                Arguments.of(Mese.class, AETypeFilter.uguale, "mese", "marzo", AETypeFilter.maggiore, "giorni", 25),
                Arguments.of(Mese.class, AETypeFilter.contiene, "mese", "emb", AETypeFilter.uguale, "giorni", 30),
                Arguments.of(Mese.class, AETypeFilter.inizia, "mese", "m", AETypeFilter.minoreUguale, "giorni", 30)
        );
    }


    protected void printCount(final String simpleName, final int size, final String property, final Object value) {
        System.out.println(String.format(String.format("La classe %s ha %s entities filtrate con %s=%s", simpleName, size, property, value)));
        System.out.println(VUOTA);
    }


    protected void printCount(final WrapQuery wrapQuery, final String propertyName, final Serializable propertyValue, final int previstoIntero, final int ottenutoIntero, final WrapQuery.Type typePrevisto) {
        System.out.println(String.format("Risultato %s %d", UGUALE_SEMPLICE, ottenutoIntero));
        System.out.println(VUOTA);
        WrapQuery.Type type;
        String message;
        String clazzName;

        //--non dovrebbe arrivare qui, ma non si sa mai
        if (wrapQuery == null) {
            System.out.println("Manca la wrapQuery");
            return;
        }

        //--non dovrebbe arrivare qui, ma non si sa mai
        if (wrapQuery.getEntityClazz() == null) {
            System.out.println("Manca la entityClazz");
            return;
        }

        type = wrapQuery.type;
        clazzName = wrapQuery.getEntityClazz().getSimpleName();

        if (typePrevisto != type) {
            System.out.println(String.format("La query era prevista di typo %s mentre è di tipo %s", typePrevisto, type));
            return;
        }

        message = switch (type) {
            case indefinita, nulla -> VUOTA;
            case errata -> "errata e restituisce il valore uguale a zero";
            case validaSenzaFiltri -> "valida con filtro=null o vuoto e restituisce i valori filtrati";
            case validaConFiltri -> "valida con filtro=esplicito e restituisce i valori filtrati";
            default -> VUOTA;
        };

        if (ottenutoIntero == previstoIntero) {
            System.out.println(String.format("La collezione '%s' contiene %s records (entities) filtrati con %s=%s che sono quelli previsti", clazzName, ottenutoIntero, propertyName, propertyValue));
        }
        else {
            System.out.println(String.format("La collezione '%s' contiene %s records (entities) filtrati con %s=%s che non sono i %s previsti", clazzName, ottenutoIntero, propertyName, propertyValue, previstoIntero));
        }

        System.out.println(String.format("La query è %s", message));

        if (flagRisultatiEsattiObbligatori) {
            assertEquals(previstoIntero, ottenutoIntero);
        }
        System.out.println(VUOTA);
    }


    protected void printCount(final WrapQuery wrapQuery, final int previstoIntero, final int ottenutoIntero, final boolean risultatoEsatto, final WrapQuery.Type typePrevisto) {
        System.out.println(String.format("Risultato %s %d", UGUALE_SEMPLICE, ottenutoIntero));
        System.out.println(VUOTA);
        WrapQuery.Type type;
        String message;
        String clazzName;

        //--non dovrebbe arrivare qui, ma non si sa mai
        if (wrapQuery == null) {
            System.out.println("Manca la wrapQuery");
            return;
        }

        //--non dovrebbe arrivare qui, ma non si sa mai
        if (wrapQuery.getEntityClazz() == null) {
            System.out.println("Manca la entityClazz");
            return;
        }

        type = wrapQuery.type;
        clazzName = wrapQuery.getEntityClazz().getSimpleName();

        if (typePrevisto != type) {
            System.out.println(String.format("La query della entityClazz %s era prevista di typo %s mentre è di tipo %s", clazzName, typePrevisto, type));
            return;
        }
        assertEquals(typePrevisto, type);
        message = switch (type) {
            case indefinita, nulla -> VUOTA;
            case errata -> {
                System.out.println(String.format("La entityClazz '%s' non contiene nessun records (entities) perché non è del tipo adatto", clazzName));
                yield VUOTA;
            }
            case validaSenzaFiltri -> "valida con filtro=null o vuoto e restituisce i valori filtrati";
            case validaConFiltri -> "valida con filtro=esplicito e restituisce i valori filtrati";
            default -> VUOTA;
        };

        if (textService.isEmpty(message)) {
            return;
        }

        if (ottenutoIntero == previstoIntero) {
            if (risultatoEsatto) {
                System.out.println(String.format("La collezione '%s' contiene %s records (entities) totali che sono esattamente quelli previsti (obbligatori)", clazzName, ottenutoIntero));
            }
            else {
                System.out.println(String.format("La collezione '%s' contiene %s records (entities) totali che sono uguali a quelli indicativamente previsti (facoltativi)", clazzName, ottenutoIntero));
            }
        }
        else {
            if (ottenutoIntero > previstoIntero) {
                if (risultatoEsatto) {
                    System.out.println(String.format("La collezione '%s' contiene %s records (entities) totali che sono più dei %s previsti e non va bene", clazzName, ottenutoIntero, previstoIntero));
                }
                else {
                    System.out.println(String.format("La collezione '%s' contiene %s records (entities) totali che sono più dei %s indicativamente previsti", clazzName, ottenutoIntero, previstoIntero));
                }
            }
            else {
                if (risultatoEsatto) {
                    System.out.println(String.format("La collezione '%s' contiene %s records (entities) totali che sono meno dei %s previsti e non va bene", clazzName, ottenutoIntero, previstoIntero));
                }
                else {
                    System.out.println(String.format("La collezione '%s' contiene %s records (entities) totali che sono meno dei %s indicativamente previsti", clazzName, ottenutoIntero, previstoIntero));
                }
            }
        }
        System.out.println(String.format("La query è %s", message));

        if (flagRisultatiEsattiObbligatori && risultatoEsatto) {
            assertEquals(previstoIntero, ottenutoIntero);
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

    protected String getSimpleName(final Class clazz) {
        return clazz != null ? clazz.getSimpleName() : "(manca la classe)";
    }

}