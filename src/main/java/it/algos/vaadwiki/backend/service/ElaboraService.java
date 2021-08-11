package it.algos.vaadwiki.backend.service;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.packages.nazionalita.*;
import it.algos.vaadwiki.backend.packages.nome.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 31-lug-2021
 * Time: 17:03
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AElaboraService.class); <br>
 * 3) @Autowired public ElaboraService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ElaboraService extends AbstractService {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public GiornoService giornoService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AnnoService annoService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AttivitaService attivitaService;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public NazionalitaService nazionalitaService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public NomeService nomeService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BioUtility bioUtility;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BioService bioService;


    /**
     * Elabora le voci biografiche indicate <br>
     */
    public boolean esegue(List<Bio> listaBio) {
        long inizio = System.currentTimeMillis();

        if (listaBio != null && listaBio.size() > 0) {
            for (Bio bio : listaBio) {
                esegue(bio);
                try {
                    bioService.save(bio, null);
                } catch (AMongoException unErrore) {
                }
            }
        }

        logger.info(AETypeLog.elabora, String.format("Sono state elaborate %s pagine in %s", text.format(listaBio.size()), date.deltaText(inizio)));
        return true;
    }

    /**
     * Elabora la singola voce biografica <br>
     * Estrae dal tmplBioServer i singoli parametri previsti nella enumeration ParBio <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile ed utilizzabile per le liste <br>
     */
    public Bio esegue(Bio bio) {

        //--Recupera i valori base di tutti i parametri dal tmplBioServer
        LinkedHashMap<String, String> mappa = bioUtility.estraeMappa(bio.getTmplBio());

        //--Elabora valori validi dei parametri significativi
        //--Inserisce i valori nella entity Bio
        if (mappa != null) {
            try {
                setValue(bio, mappa);
            } catch (Exception unErrore) {
                logger.error(unErrore, this.getClass(), "esegue");
            }
        }

        return bio;
    }


    //--Inserisce i valori nella entity Bio
    public void setValue(Bio bio, HashMap<String, String> mappa) {
        String value;

        try {
            if (bio != null) {

                //                // patch per i luoghi di nascita e morte
                //                // se è pieno il parametro link, lo usa
                //                if (text.isValid(mappa.get(ParBio.luogoNascitaLink.getTag()))) {
                //                    mappa.put(ParBio.luogoNascita.getTag(), mappa.get(ParBio.luogoNascitaLink.getTag()));
                //                }// end of if cycle
                //                if (text.isValid(mappa.get(ParBio.luogoMorteLink.getTag()))) {
                //                    mappa.put(ParBio.luogoMorte.getTag(), mappa.get(ParBio.luogoMorteLink.getTag()));
                //                }// end of if cycle

                for (ParBio par : ParBio.values()) {
                    value = mappa.get(par.getTag());
                    if (value != null) {
                        par.setValue(bio, value);
                    }
                }
            }
        } catch (Exception unErrore) {
            logger.error(unErrore.toString());
        }
    }

    /**
     * Regola la property <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixNomeValido(final String testoGrezzo) {
        return fixValoreGrezzo(testoGrezzo);
    }


    /**
     * Regola la property <br>
     * Controlla che il valore esista nella collezione Nome <br> ??????
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return istanza di nome valida
     */
    public String fixNomeLink(final String testoGrezzo) {
        Nome nome = null;
        String testoValido = VUOTA;

        if (text.isValid(testoGrezzo)) {
            testoValido = fixNomeValido(testoGrezzo);
        }

        if (text.isValid(testoValido)) {
            nome = nomeService.findByKey(testoValido);
            if (nome == null) {
                nome = nomeService.newEntity(1, testoValido, true);
            }
            else {
                nome.setVoci(nome.getVoci() + 1);
            }
            try {
                nomeService.save(nome, null);
            } catch (AMongoException unErrore) {
            }
        }

        return testoValido;
    }

    /**
     * Regola la property <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixCognomeValido(String testoGrezzo) {
        return fixValoreGrezzo(testoGrezzo);
    }

    /**
     * Regola la property <br>
     * <p>
     * Elimina il testo successivo a vari tag (fixPropertyBase) <br>
     * Elimina il testo se NON contiene una spazio vuoto (tipico della data giorno-mese) <br>
     * Elimina eventuali DOPPI spazi vuoto (tipico della data tra il giorno ed il mese) <br>
     * Elimina eventuali spazi vuoti (trim) <br>
     * Controlla che il valore esista nella collezione Giorno <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return istanza di giorno valido
     */
    public Giorno fixGiornoLink(String testoGrezzo) {
        Giorno giorno = null;
        String testoValido = VUOTA;

        if (text.isValid(testoGrezzo)) {
            testoValido = fixGiornoValido(testoGrezzo);
        }

        if (text.isValid(testoValido)) {
            giorno = giornoService.findByKey(testoValido);
        }

        return giorno;
    }

    /**
     * Regola la property <br>
     * <p>
     * Elimina il testo successivo a vari tag (fixPropertyBase) <br>
     * Elimina il testo se NON contiene una spazio vuoto (tipico della data giorno-mese) <br>
     * Elimina eventuali DOPPI spazi vuoto (tipico della data tra il giorno ed il mese) <br>
     * Elimina eventuali spazi vuoti (trim) <br>
     * Controlla che il valore esista nella collezione Anno <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return istanza di anno valido
     */
    public Anno fixAnnoLink(String testoGrezzo) {
        Anno anno = null;
        String testoValido = "";

        if (text.isValid(testoGrezzo)) {
            testoValido = fixAnnoValido(testoGrezzo);
        }

        if (text.isValid(testoValido)) {
            anno = annoService.findByKey(testoValido);
        }

        return anno;
    }

    /**
     * Regola la property <br>
     * <p>
     * Elimina il testo successivo a vari tag (fixPropertyBase) <br>
     * Controlla che il valore esista nella collezione Attività <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return istanza di attività valida
     */
    public Attivita fixAttivitaLink(String testoGrezzo) {
        Attivita attivita = null;
        String testoValido = VUOTA;

        if (text.isValid(testoGrezzo)) {
            testoValido = fixAttivitaValida(testoGrezzo);
        }

        if (text.isValid(testoValido)) {
            attivita = attivitaService.findByKey(testoValido);
        }

        return attivita;
    }

    /**
     * Regola la property <br>
     * <p>
     * Elimina il testo successivo a vari tag (fixPropertyBase) <br>
     * Controlla che il valore esista nella collezione Nazionalità <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return istanza di nazionalità valida
     */
    public Nazionalita fixNazionalitaLink(String testoGrezzo) {
        Nazionalita nazionalita = null;
        String testoValido = VUOTA;

        if (text.isValid(testoGrezzo)) {
            testoValido = fixNazionalitaValida(testoGrezzo);
        }

        if (text.isValid(testoValido)) {
            nazionalita = nazionalitaService.findByKey(testoValido);
        }

        return nazionalita;
    }

    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     * Elimina il testo se NON contiene una spazio vuoto (tipico della data giorno-mese) <br>
     * Elimina eventuali spazi vuoti DOPPI o TRIPLI (tipico della data tra il giorno ed il mese) <br>
     * Forza a minuscolo il primo carattere del mese <br>
     * Forza a ordinale un eventuale primo giorno del mese scritto come numero o come grado <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixGiornoValido(String testoGrezzo) {
        String testoValido = fixValoreGrezzo(testoGrezzo);
        int pos;
        String primo;
        String mese;

        //--il punto interrogativo da solo è valido (il metodo fixPropertyBase lo elimina)
        if (testoGrezzo.trim().equals("?")) {
            return testoGrezzo;
        }

        //--se contiene un punto interrogativo non è valido
        if (testoGrezzo.contains("?")) {
            return testoGrezzo;
        }

        if (text.isEmpty(testoValido)) {
            return VUOTA;
        }

        //--solo date certe ed esatte
        if (testoGrezzo.contains(CIRCA)) {
            return VUOTA;
        }

        //--elimina ref in coda. Data accettabile
        testoValido = text.levaDopo(testoValido, REF);

        //--spazio singolo
        testoValido = text.fixOneSpace(testoValido);

        //--senza spazio
        if (!testoValido.contains(SPAZIO)) {
            testoValido = separaMese(testoValido);
        }

        if (!testoValido.contains(SPAZIO)) {
            return VUOTA;
        }

        //--minuscola
        testoValido = testoValido.toLowerCase();

        //--Forza a ordinale un eventuale primo giorno del mese scritto come numero o come grado
        if (testoValido.contains(SPAZIO)) {
            pos = testoValido.indexOf(SPAZIO);
            primo = testoValido.substring(0, pos);
            mese = testoValido.substring(pos + SPAZIO.length());

            if (primo.equals("1") || primo.equals("1°")) {
                primo = "1º";
                testoValido = primo + SPAZIO + mese;
            }
        }

        if (text.isValid(testoValido)) {
            if (giornoService.findByKey(testoValido) != null) {
                return testoValido.trim();
            }
            else {
                return VUOTA;
            }
        }
        else {
            return VUOTA;
        }
    }


    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     * Elimina il testo se contiene la dicitura 'circa' (tipico dell'anno)
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixAnnoValido(String testoGrezzo) {
        String testoValido = fixValoreGrezzo(testoGrezzo);

        //--il punto interrogativo da solo è valido (il metodo fixPropertyBase lo elimina)
        if (testoGrezzo.trim().equals("?")) {
            return testoGrezzo;
        }

        if (text.isEmpty(testoGrezzo)) {
            return VUOTA;
        }

        testoValido = text.levaDopo(testoValido, CIRCA);

        if (text.isValid(testoValido)) {
            if (annoService.isEsiste(testoValido)) {
                return testoValido.trim();
            }
            else {
                return VUOTA;
            }
        }
        else {
            return VUOTA;
        }
    }

    /**
     * Regola questa property <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixLuogoValido(String testoGrezzo) {
        String testoValido;

        if (text.isEmpty(testoGrezzo)) {
            return VUOTA;
        }

        testoValido = testoGrezzo.trim();
        testoValido = text.levaDopoRef(testoValido);
        testoValido = text.levaDopoNote(testoValido);
        testoValido = text.levaDopoGraffe(testoValido);
        testoValido = text.levaDopoInterrogativo(testoValido);
        testoValido = text.setNoQuadre(testoValido);
        testoValido = testoValido.trim();

        if (testoValido.length() > 253) {
            testoValido = testoValido.substring(0, 252);
            //@todo manca warning
        }

        return testoValido;
    }

    /**
     * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
     * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valoreGrezzo in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String fixValoreGrezzo(String valoreGrezzo) {
        String valoreValido = valoreGrezzo.trim();

        if (text.isEmpty(valoreGrezzo)) {
            return VUOTA;
        }

        valoreValido = text.setNoQuadre(valoreValido);

        //--elimina ref in coda
        valoreValido = text.levaDopo(valoreValido, REF_OPEN);

        return valoreValido.trim();
    }

    /**
     * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
     *
     * @param valoreGrezzo in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String fixMinuscola(String valoreGrezzo) {
        String valoreValido = fixValoreGrezzo(valoreGrezzo);

        if (text.isEmpty(valoreGrezzo)) {
            return VUOTA;
        }

        valoreValido = text.primaMinuscola(valoreValido);

        return valoreValido.trim();
    }

    public String separaMese(String testoTuttoAttaccato) {
        String testoSeparato = testoTuttoAttaccato.trim();
        String giorno;
        String mese;
        String inizio;

        if (testoSeparato.contains(SPAZIO)) {
            return testoSeparato;
        }

        if (Character.isDigit(testoSeparato.charAt(0))) {
            if (Character.isDigit(testoSeparato.charAt(1))) {
                giorno = testoSeparato.substring(0, 2);
                mese = testoSeparato.substring(2);
            }
            else {
                giorno = testoSeparato.substring(0, 1);
                mese = testoSeparato.substring(1);
            }
        }
        else {
            return testoSeparato;
        }

        inizio = mese.substring(0, 1);
        if (inizio.equals(TRATTINO) || inizio.equals(SLASH)) {
            mese = mese.substring(1);
        }

        if (text.isValid(giorno) && text.isValid(mese)) {
            testoSeparato = giorno + SPAZIO + mese;
        }

        return testoSeparato;
    }


    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixAttivitaValida(String testoGrezzo) {
        String testoValido = fixValoreGrezzo(testoGrezzo).toLowerCase();
        //        String tag1 = "ex ";
        //        String tag2 = "ex-";

        if (text.isEmpty(testoValido)) {
            return VUOTA;
        }

        if (text.isValid(testoValido)) {
            if (attivitaService.isEsiste(testoValido)) {
                return testoValido.trim();
            }
            else {
                return VUOTA;
            }
        }
        else {
            return VUOTA;
        }
    }

    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixNazionalitaValida(String testoGrezzo) {
        String testoValido = fixValoreGrezzo(testoGrezzo).toLowerCase();

        if (text.isEmpty(testoValido)) {
            return VUOTA;
        }

        ////        if (text.isValid(testoValido) && mongo.isEsisteByProperty(Nazionalita.class, "singolare", testoValido)) {
        //        if (text.isValid(testoValido)) {
        //            return testoValido.trim();
        //        } else {
        //            return VUOTA;
        //        }// end of if/else cycle

        if (text.isValid(testoValido)) {
            if (nazionalitaService.isEsiste(testoValido)) {
                return testoValido.trim();
            }
            else {
                return VUOTA;
            }
        }
        else {
            return VUOTA;
        }
    }

    /**
     * Elimina gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * Elimina il punto interrogativo, se da solo <br>
     * Mantiene il punto interrogativo, se da solo <br>
     *
     * @param testoOriginario in entrata da elaborare
     *
     * @return testoGrezzo troncato
     */
    public String troncaParteFinaleGiornoAnno(String testoOriginario) {
        String testoGrezzo = text.levaDopoVirgola(testoOriginario);

        return troncaParteFinalePuntoInterrogativo(testoGrezzo);
    }


    /**
     * Elimina gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * Elimina il punto interrogativo, se da solo <br>
     * Mantiene il punto interrogativo, se da solo <br>
     *
     * @param testoOriginario in entrata da elaborare
     *
     * @return testoGrezzo troncato
     */
    public String troncaParteFinalePuntoInterrogativo(String testoOriginario) {
        String testoGrezzo = VUOTA;

        if (text.isEmpty(testoOriginario)) {
            return VUOTA;
        }

        testoGrezzo = testoOriginario.trim();
        testoGrezzo = text.levaDopoRef(testoGrezzo);
        testoGrezzo = text.levaDopoNote(testoGrezzo);
        testoGrezzo = text.levaDopoGraffe(testoGrezzo);
        //        testoGrezzo = text.levaDopoVirgola(testoGrezzo);
        testoGrezzo = text.levaDopoCirca(testoGrezzo);
        if (!testoGrezzo.equals("?")) {
            testoGrezzo = text.levaDopoInterrogativo(testoGrezzo);
        }

        testoGrezzo = testoGrezzo.trim();

        return testoGrezzo;
    }

}