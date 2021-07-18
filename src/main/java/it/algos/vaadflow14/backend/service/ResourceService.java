package it.algos.vaadflow14.backend.service;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.*;

import static it.algos.vaadflow14.backend.application.FlowCost.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 24-set-2020
 * Time: 21:06
 * <p>
 * Static files: {project directory}/src/main/resources/META-INF/resources/
 * CSS files e JavaScript: {project directory}/frontend/styles/
 * Risorse esterne al JAR: {project directory}/config/
 *
 * @see(https://vaadin.com/docs/flow/importing-dependencies/tutorial-ways-of-importing.html)
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ResourceService extends AbstractService {


    /**
     * versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Legge un file di risorse da {project directory}/config/ <br>
     *
     * @param simpleNameFileToBeRead nome del file senza path e senza directory
     *
     * @return testo completo grezzo del file
     */
    public String leggeConfig(String simpleNameFileToBeRead) {
        String tag = "config";
        return fileService.leggeFile(tag + File.separator + simpleNameFileToBeRead);
    }


    /**
     * Legge una lista di righe di risorse da {project directory}/config/ <br>
     *
     * @param simpleNameFileToBeRead nome del file senza path e senza directory
     *
     * @return lista di righe grezze
     */
    public List<String> leggeListaConfig(String simpleNameFileToBeRead) {
        return leggeListaConfig(simpleNameFileToBeRead, true);
    }


    /**
     * Legge una lista di righe di risorse da {project directory}/config/ <br>
     * La prima riga contiene i titoli
     *
     * @param simpleNameFileToBeRead nome del file senza path e senza directory
     * @param compresiTitoli         parte dalla prima riga, altrimenti dalla seconda
     *
     * @return lista di righe grezze
     */
    public List<String> leggeListaConfig(String simpleNameFileToBeRead, boolean compresiTitoli) {
        List<String> listaRighe = null;
        String rawText = leggeConfig(simpleNameFileToBeRead);
        String[] righe = null;

        if (text.isValid(rawText)) {
            listaRighe = new ArrayList<>();
            righe = rawText.split(A_CAPO);
            if (righe != null && righe.length > 0) {
                for (String riga : righe) {
                    riga = text.estrae(riga, DOPPIE_GRAFFE_INI, DOPPIE_GRAFFE_END);
                    if (text.isValid(riga)) {
                        listaRighe.add(riga);
                    }
                }
            }
        }

        if (listaRighe != null) {
            if (!compresiTitoli) {
                listaRighe = listaRighe.subList(1, listaRighe.size());
            }
        }

        return listaRighe;
    }


    /**
     * Legge una mappa di risorse da {project directory}/config/ <br>
     * La prima riga contiene i titoli
     *
     * @param simpleNameFileToBeRead nome del file senza path e senza directory
     *
     * @return lista di righe grezze
     */
    public Map<String, List<String>> leggeMappaConfig(String simpleNameFileToBeRead) {
        Map<String, List<String>> mappa = null;
        List<String> listaParti = null;
        String rawText = leggeConfig(simpleNameFileToBeRead);
        String[] righe = null;
        String[] parti = null;

        if (text.isValid(rawText)) {
            righe = rawText.split(A_CAPO);
            if (righe != null && righe.length > 0) {
                mappa = new LinkedHashMap<>();
                for (String riga : righe) {
                    listaParti = new ArrayList<>();
                    parti = riga.split(VIRGOLA);

                    if (parti != null && parti.length > 1) {
                        for (int k = 1; k < parti.length; k++) {
                            listaParti.add(parti[k]);
                        }
                    }
                    mappa.put(parti[0], listaParti);
                }
            }
        }

        return mappa;
    }


    /**
     * Legge un file di risorse da {project directory}/frontend/ <br>
     *
     * @param simpleNameFileToBeRead nome del file senza path e senza directory
     *
     * @return testo completo grezzo del file
     */
    public String leggeFrontend(String simpleNameFileToBeRead) {
        String tag = "frontend";
        return fileService.leggeFile(tag + File.separator + simpleNameFileToBeRead);
    }


    /**
     * Legge un file di risorse da {project directory}/src/main/resources/META-INF/resources/ <br>
     *
     * @param simpleNameFileToBeRead nome del file senza path e senza directory
     *
     * @return testo completo grezzo del file
     */
    public String leggeMetaInf(String simpleNameFileToBeRead) {
        String tag = "src/main/resources/META-INF/resources";
        return fileService.leggeFile(tag + File.separator + simpleNameFileToBeRead);
    }


    /**
     * Legge un file di risorse <br>
     *
     * @param nameFileToBeRead nome del file
     *
     * @return testo grezzo del file
     */
    public String leggeRisorsa(String nameFileToBeRead) {
        //        File filePath = new File("config" + File.separator + nameFileToBeRead);
        File filePath = new File("META-INF.resources" + File.separator + nameFileToBeRead);
        return fileService.leggeFile(filePath.getAbsolutePath());
    }


    /**
     * Costruisce un file di risorse, partendo dal nome semplice <br>
     * La risorsa DEVE essere nel path 'src/main/resources/META-INF/resources/' <br>
     *
     * @param simpleNameFileToBeRead nome del file all'interno della directory 'resources'
     *
     * @return bytes
     */
    public File getFile(String simpleNameFileToBeRead) {
        File resourceFile = null;
        String pathResourceFileName = VUOTA;

        if (text.isEmpty(simpleNameFileToBeRead)) {
            return null;
        }

        if (simpleNameFileToBeRead.startsWith(PATH_RISORSE)) {
            pathResourceFileName = simpleNameFileToBeRead;
        } else {
            pathResourceFileName = PATH_RISORSE + simpleNameFileToBeRead;
        }

        resourceFile = new File(pathResourceFileName);

        return resourceFile;
    }


    /**
     * Legge i bytes di un file di risorse <br>
     * La risorsa DEVE essere nel path 'src/main/resources/META-INF/resources/' <br>
     *
     * @param simpleResourceFileName nome del file all'interno della directory 'resources'
     *
     * @return bytes
     */
    public byte[] getBytes(String simpleResourceFileName) {
        byte[] bytes = null;
        File resourceFile = getFile(simpleResourceFileName);

        if (resourceFile.exists() && resourceFile.isFile()) {
            try {
                bytes = FileUtils.readFileToByteArray(resourceFile);
            } catch (Exception unErrore) {
                logger.warn(unErrore, this.getClass(), "getBytes");
            }
        }

        return bytes;
    }


    /**
     * Costruisce una Image partendo dai bytes <br>
     *
     * @param bytes dell'immagine
     *
     * @return image
     */
    public Image getImageFromBytes(byte[] bytes) {
        Image image = null;
        StreamResource resource;

        if (bytes != null) {
            try {
                resource = new StreamResource("manca.jpg", () -> new ByteArrayInputStream(bytes));
                image = new Image(resource, "manca");
            } catch (Exception unErrore) {
                logger.error(unErrore, this.getClass(), "nomeDelMetodo");
            }
        }

        return image;
    }


    /**
     * Costruisce una Image partendo da un file di risorse <br>
     * Legge i bytes di un file di risorse <br>
     * Legge le risorse di un file <br>
     *
     * @param simpleResourceFileName nome del file all'interno della directory 'resources'
     *
     * @return image
     */
    public Image getImageFromFile(String simpleResourceFileName) {
        Image image = null;
        byte[] bytes = getBytes(simpleResourceFileName);

        if (bytes != null) {
            image = getImageFromBytes(bytes);
        }

        return image;
    }


    /**
     * Costruisce una Image partendo dal mongoDB <br>
     * Legge i bytes decodificando il valore memorizzato <br>
     *
     * @param mongoValue valore codificato nel mongoDB
     *
     * @return image
     */
    public Image getImageFromMongo(String mongoValue) {
        Image image = null;
        byte[] bytes = Base64.decodeBase64(mongoValue);

        if (bytes != null) {
            image = getImageFromBytes(bytes);
        }

        return image;
    }


    public Image getImagePng(String simpleResourceFileName) {
        return getImageFromFile(simpleResourceFileName + PUNTO + "png");
    }


    public Image getBandieraFromFile(String simpleResourceFileName) {
        Image image = getImagePng("bandiere/" + simpleResourceFileName.toLowerCase());

        if (image != null) {
            image.setWidth("30px");
            image.setHeight("21px");
        }

        return image;
    }


    public Image getBandieraFromMongo(String mongoValue) {
        Image image = getImageFromMongo(mongoValue);

        if (image != null) {
            image.setWidth("30px");
            image.setHeight("21px");
        }

        return image;
    }


    /**
     * Legge una stringa codificata dal file di risorse <br>
     *
     * @param simpleResourceFileName nome del file all'interno della directory 'resources'
     *
     * @return valore codificato
     */
    public String getSrc(String simpleResourceFileName) {
        String bytesCodificati = VUOTA;
        byte[] bytes = getBytes(simpleResourceFileName);

        if (bytes != null) {
            bytesCodificati = Base64.encodeBase64String(bytes);
        }

        return bytesCodificati;
    }


    /**
     * Legge una stringa codificata dal file di risorse <br>
     *
     * @param simpleResourceFileNameWithoutSuffix nome del file all'interno della directory 'bandiere'
     *
     * @return valore codificato
     */
    public String getSrcBandieraPng(String simpleResourceFileNameWithoutSuffix) {
        return getSrc("bandiere/" + simpleResourceFileNameWithoutSuffix.toLowerCase() + PUNTO + "png");
    }


    /**
     * Legge una mappa CSV da un file <br>
     * Prima lista (prima riga): titoli
     * Liste successive (righe successive): valori
     *
     * @param pathFileToBeRead nome completo del file
     *
     * @return lista di mappe di valori
     */
    public List<LinkedHashMap<String, String>> leggeMappaCSV(String pathFileToBeRead) {
        return fileService.leggeMappaCSV(pathFileToBeRead);
    }

}