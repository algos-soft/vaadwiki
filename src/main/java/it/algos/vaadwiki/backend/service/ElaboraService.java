package it.algos.vaadwiki.backend.service;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.packages.nazionalita.*;
import it.algos.vaadwiki.backend.packages.nome.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.regex.*;

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
public class ElaboraService extends WService {

    public static final List<String> MASCHI = Arrays.asList("m", "uomo", "maschio", "maschile", "M", "Uomo", "Maschio", "Maschile");

    public static final List<String> FEMMINE = Arrays.asList("f", "donna", "femmina", "femminile", "F", "Donna", "Femmina", "Femminile");

    public static final List<String> TRANS = Arrays.asList("", "trans", "incerto", "non si sa", "dubbio", "?", "*", "ǝ");


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
                } catch (AlgosException unErrore) {
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
     * Quello che resta è affidabile e utilizzabile per le liste <br>
     */
    public Bio esegue(Bio bio) {

        //--Recupera i valori base di tutti i parametri dal tmplBioServer
        LinkedHashMap<String, String> mappa = bioUtility.estraeMappa(bio.getTmplBio());

        //--Elabora valori validi dei parametri significativi
        //--Inserisce i valori nella entity Bio
        if (mappa != null) {
            setValue(bio, mappa);
        }

        return bio;
    }


    //--Inserisce i valori nella entity Bio
    public void setValue(Bio bio, HashMap<String, String> mappa) {
        String value;
        String message;

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

                    try {
                        par.setValue(bio, value);
                    } catch (AlgosException unErrore) {
                        message = String.format("Exception %s nel ParBio %s della bio %s", unErrore.getMessage(), par.getTag(), bio.wikiTitle);
                        logger.info(message, this.getClass(), "setValue");
                    } catch (Exception unErrore) {
                        logger.info(String.format("%s nel ParBio %s", unErrore.toString(), par.getTag()), this.getClass(), "setValue");
                    }
                }
            }
        }
    }

    /**
     * Regola la property <br>
     * Indipendente dalla lista di Nomi <br>
     * Nei nomi composti, prende solo il primo <br>
     * Se esiste nella lista dei Prenomi (nomi doppi), lo accetta <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixNome(final String testoGrezzo) {
        String testoValido = wikiBotService.estraeValoreInizialeGrezzoPuntoAmmesso(testoGrezzo);
        List<String> listaPrenomi;

        if (testoValido.contains(SPAZIO)) {
            listaPrenomi = prenomeService.fetchCode();
            if (!listaPrenomi.contains(testoValido)) {
                testoValido = testoValido.substring(0, testoValido.indexOf(SPAZIO)).trim();
            }
        }

        return testoValido;
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
            testoValido = fixNome(testoGrezzo);
        }

        if (text.isValid(testoValido)) {
            try {
                nome = nomeService.findByKey(testoValido);
            } catch (AlgosException unErrore) {
                logger.warn(unErrore, this.getClass(), "fixNomeLink");
            }
            if (nome == null) {
                nome = nomeService.newEntity(1, testoValido, true);
            }
            else {
                nome.setVoci(nome.getVoci() + 1);
            }
            try {
                nomeService.save(nome, null);
            } catch (AlgosException unErrore) {
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
    public String fixCognome(String testoGrezzo) {
        return wikiBotService.estraeValoreInizialeGrezzoPuntoAmmesso(testoGrezzo);
    }


    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     * A seconda del flag:
     * CONTROLLA che il valore sia valido - solo M o F <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixSesso(String testoGrezzo) {
        String testoValido = fixValoreGrezzo(testoGrezzo);
        testoValido = testoValido.toLowerCase();

        if (ElaboraService.MASCHI.contains(testoValido)) {
            testoValido = "M";
        }

        if (ElaboraService.FEMMINE.contains(testoValido)) {
            testoValido = "F";
        }

        //--non serve
        //        if (ElaboraService.TRANS.contains(testoValido)) {
        //            testoValido = VUOTA;
        //        }

        return testoValido;
    }

    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni specifiche della property <br>
     * Controlla che il valore esista nella collezione linkata <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixGiornoValido(String testoGrezzo) {
        String testoValido = fixGiorno(testoGrezzo);
        Giorno giorno = null;

        try {
            giorno = giornoService.findByKey(testoValido);
        } catch (Exception unErrore) {
            logger.info(unErrore, this.getClass(), "fixGiornoValido");
        }

        return giorno != null ? giorno.getTitolo() : VUOTA;
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
    public String fixGiorno(String testoGrezzo) {
        //--se contiene un punto interrogativo (in coda) è valido
        String testoValido = wikiBotService.estraeValoreInizialeGrezzoPuntoEscluso(testoGrezzo);
        int pos;
        String primo;
        String mese;

        //--spazio singolo
        testoValido = text.fixOneSpace(testoValido);

        //--senza spazio
        if (!testoValido.contains(SPAZIO)) {
            testoValido = separaMese(testoValido);
        }

        if (!testoValido.contains(SPAZIO)) {
            return VUOTA;
        }

        //--elimina eventuali quadre (ini o end) rimaste
        testoValido = testoValido.replaceAll(QUADRA_INI_REGEX, VUOTA);
        testoValido = testoValido.replaceAll(QUADRA_END_REGEX, VUOTA);

        //--deve iniziare con un numero
        if (!Character.isDigit(testoValido.charAt(0))) {
            return VUOTA;
        }

        //--deve finire con una lettera
        if (Character.isDigit(testoValido.charAt(testoValido.length() - 1))) {
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

        return testoValido.trim();
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
    public Giorno fixGiornoLink(String testoGrezzo) throws Exception {
        Giorno giorno = null;
        String testoValido = fixGiorno(testoGrezzo);

        if (text.isValid(testoValido)) {
            giorno = giornoService.findByKey(testoValido);
        }

        return giorno;
    }


    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni specifiche della property <br>
     * Controlla che il valore esista nella collezione linkata <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixAnnoValido(String testoGrezzo) {
        String testoValido = fixAnno(testoGrezzo);
        Anno anno = null;

        try {
            anno = this.findAnnoByKey(testoValido);
        } catch (Exception unErrore) {
            logger.info(unErrore, this.getClass(), "fixAnnoValido");
        }

        return anno != null ? anno.getTitolo() : VUOTA;
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
    public String fixAnno(String testoGrezzo) {
        //--se contiene un punto interrogativo (in coda) è valido
        String testoValido = wikiBotService.estraeValoreInizialeGrezzoPuntoAmmesso(testoGrezzo);

        if (text.isEmpty(testoValido)) {
            return VUOTA;
        }

        //--deve iniziare con un numero
        if (!Character.isDigit(testoValido.charAt(0))) {
            return VUOTA;
        }

        //--tag non ammesso
        if (testoValido.contains("secolo")) {
            return VUOTA;
        }

        //--non deve contenere caratteri alfabetici
        //--solo (eventualmente): A, a, C, c
        //--per gli anni prima di Cristo
        if (contieneCaratteriAlfabetici(testoValido)) {
            return VUOTA;
        }

        //--non deve contenere caratteri divisivi di due anni
        if (testoValido.contains(SLASH) || testoValido.contains(PIPE) || testoValido.contains(TRATTINO)) {
            return VUOTA;
        }

        return testoValido.trim();
    }

    public boolean contieneCaratteriAlfabetici(String testoIn) {
        boolean contiene = false;
        // Create a Pattern object
        Pattern pattern = Pattern.compile("[bd-zBD-Z]");

        // Now create matcher object.
        Matcher matcher = pattern.matcher(testoIn);

        if (matcher != null && matcher.find()) {
            return true;
        }

        return contiene;
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
    public Anno fixAnnoLink(final String testoGrezzo) throws Exception {
        Anno anno = null;
        String titoloAncheAnteCristo = fixAnno(testoGrezzo);
        ;

        if (text.isValid(titoloAncheAnteCristo)) {
            anno = this.findAnnoByKey(titoloAncheAnteCristo);
        }

        return anno;
    }

    /**
     * Retrieves an entity by its keyProperty.
     * Considera anche gli anni Ante Cristo, eventualmente scritti male <br>
     *
     * @param titoloAncheAnteCristo must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     */
    public Anno findAnnoByKey(final String titoloAncheAnteCristo) throws Exception {
        Anno anno = null;
        String titoloEsatto = titoloAncheAnteCristo;
        String tagA = "a";
        String tagC = "C";

        //--a minuscola
        titoloEsatto = titoloEsatto.replaceAll("A", tagA);

        //--c maiuscola
        titoloEsatto = titoloEsatto.replaceAll("c", tagC);

        //--manca spazio
        if (!titoloEsatto.contains(SPAZIO)) {
            titoloEsatto = titoloEsatto.replace(tagA, SPAZIO + tagA);
        }

        //--manca punto dopo 'a'
        if (!titoloEsatto.contains(tagA + PUNTO)) {
            titoloEsatto = titoloEsatto.replace(tagA, tagA + PUNTO);
        }

        //--manca punto dopo 'C'
        if (!titoloEsatto.contains(tagC + PUNTO)) {
            titoloEsatto = titoloEsatto.replace(tagC, tagC + PUNTO);
        }

        anno = annoService.findByKey(titoloEsatto);

        return anno;
    }


    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni specifiche della property <br>
     * Controlla che il valore esista nella collezione linkata <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixAttivitaValida(String testoGrezzo) {
        String testoValido = fixAttivita(testoGrezzo);
        Attivita attivita = null;

        try {
            attivita = attivitaService.findByKey(testoValido);
        } catch (Exception unErrore) {
            logger.info(unErrore, this.getClass(), "fixAttivitaValida");
        }

        return attivita != null ? attivita.getSingolare() : VUOTA;
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
    public String fixAttivita(String testoGrezzo) {
        //--se contiene un punto interrogativo (in coda) è valido
        String testoValido = wikiBotService.estraeValoreInizialeGrezzoPuntoEscluso(testoGrezzo);

        //--minuscola
        testoValido = testoValido.toLowerCase();

        //--eventuali quadre rimaste (può succedere per le attività ex-)
        testoValido = testoValido.replaceAll(QUADRA_INI_REGEX,VUOTA);
        testoValido = testoValido.replaceAll(QUADRA_END_REGEX,VUOTA);

        return testoValido.trim();
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
    public Attivita fixAttivitaLink( String testoGrezzo) throws Exception {
        Attivita attivita = null;
        String testoValido = fixAttivita(testoGrezzo);

        if (text.isValid(testoValido)) {
            attivita = attivitaService.findByKey(testoValido);
        }

        return attivita;
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
        String testoValido = fixNazionalita(testoGrezzo);
        Nazionalita nazionalita=null;

        try {
            nazionalita = nazionalitaService.findByKey(testoValido);
        } catch (Exception unErrore) {
            logger.info(unErrore, this.getClass(), "fixNazionalitaValida");
        }

        return nazionalita != null ? nazionalita.getSingolare() : VUOTA;
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
    public String fixNazionalita(String testoGrezzo) {
        //--se contiene un punto interrogativo (in coda) è valido
        String testoValido = wikiBotService.estraeValoreInizialeGrezzoPuntoEscluso(testoGrezzo);

        //--minuscola
        testoValido = testoValido.toLowerCase();

//        //--eventuali duadre rimaste (può succedere per le attività ex-)
//        testoValido = testoValido.replaceAll(QUADRA_INI_REGEX,VUOTA);
//        testoValido = testoValido.replaceAll(QUADRA_END_REGEX,VUOTA);

        return testoValido.trim();
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
            try {
                nazionalita = nazionalitaService.findByKey(testoValido);
            } catch (AlgosException unErrore) {
                logger.warn(unErrore, this.getClass(), "fixNazionalitaLink");
            }
        }

        return nazionalita;
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

        //--elimina quadra in coda
        valoreValido = text.levaDopo(valoreValido, QUADRA_INI);

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

        if (text.isEmpty(testoTuttoAttaccato)) {
            return VUOTA;
        }

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

    //    /**
    //     * Regola questa property <br>
    //     * <p>
    //     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
    //     *
    //     * @param testoGrezzo in entrata da elaborare
    //     *
    //     * @return testo/parametro regolato in uscita
    //     */
    //    public String fixAttivitaValida(String testoGrezzo) throws AlgosException {
    //        String testoValido = fixValoreGrezzo(testoGrezzo).toLowerCase();
    //        String message;
    //
    //        //        String tag1 = "ex ";
    //        //        String tag2 = "ex-";
    //
    //        if (text.isValid(testoValido)) {
    //            try {
    //                if (attivitaService.isEsisteSingolare(testoValido)) {
    //                    return testoValido.trim();
    //                }
    //                else {
    //                    message = String.format("Nella bio di %s, l'attività %s non è stata trovata", testoValido);
    //                    throw new AlgosException(message);
    //                }
    //            } catch (AMongoException unErrore) {
    //                logger.warn(unErrore, this.getClass(), "fixAttivitaValida");
    //                return VUOTA;
    //            }
    //        }
    //        else {
    //            return VUOTA;
    //        }
    //    }


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