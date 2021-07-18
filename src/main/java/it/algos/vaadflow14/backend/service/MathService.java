package it.algos.vaadflow14.backend.service;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mar, 19-nov-2019
 * Time: 20:43
 * <p>
 * Utility per la gestione di operatori e flussi matematici <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce <br>
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AMathService.class); <br>
 * 2) AMathService.getInstance(); <br>
 * 3) @Autowired private AMathService math; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, basta il 'pattern') <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MathService extends AbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Divisione tra due numeri double <br>
     *
     * @param dividendo quantità da dividere
     * @param divisore  quantità che divide
     *
     * @return valore risultante di tipo double
     */
    public double divisione(double dividendo, double divisore) {
        double quoziente = dividendo / divisore;
        return quoziente;
    }


    /**
     * Divisione tra due numeri interi <br>
     *
     * @param dividendo quantità da dividere
     * @param divisore  quantità che divide
     *
     * @return valore risultante di tipo double
     */
    public double divisione(int dividendo, int divisore) {
        return divisione((double) dividendo, (double) divisore);
    }


    /**
     * Percentuale tra due numeri interi <br>
     *
     * @param dividendo quantità da dividere
     * @param divisore  quantità che divide
     *
     * @return valore risultante di tipo double
     */
    public double percentuale(int dividendo, int divisore) {
        return divisione((double) dividendo, (double) divisore) * 100;
    }


    /**
     * Percentuale tra due numeri interi <br>
     *
     * @param dividendo quantità da dividere
     * @param divisore  quantità che divide
     *
     * @return valore risultante di tipo String
     */
    public String percentualeDueDecimali(int dividendo, int divisore) {
        return new DecimalFormat("0.00").format(percentuale(dividendo, divisore)) + "%";
    }


    /**
     * Numero di cicli
     *
     * @param totale da dividere
     * @param blocco divisore
     *
     * @return numero di cicli
     */
    public int numCicli(int totale, int blocco) {
        int cicli = 0;
        int resto;

        if (blocco < 0) {
            return 0;
        }

        if (blocco == 0) {
            return totale;
        }

        if (blocco > totale) {
            return 1;
        }

        cicli = totale / blocco;
        resto = totale % blocco;
        if (resto > 0) {
            cicli++;
        }

        return cicli;
    }


    /**
     * Multiplo esatto
     *
     * @param dividendo
     * @param divisore
     *
     * @return true se la divisione è senza resto
     */
    public boolean divisibileEsatto(int dividendo, int divisore) {
        return dividendo % divisore == 0;
    }

}
