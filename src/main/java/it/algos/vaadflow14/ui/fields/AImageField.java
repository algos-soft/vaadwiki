package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.enumeration.AEPreferenza;
import it.algos.vaadflow14.backend.service.AReflectionService;
import it.algos.vaadflow14.backend.service.AResourceService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import static it.algos.vaadflow14.backend.application.FlowCost.VUOTA;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 24-set-2020
 * Time: 20:35
 * <p>
 * Simple layer around TextField <br>
 * Banale, ma serve per avere tutti i fields omogenei <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AImageField extends AField<String> {

    private final Image imageField;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AResourceService resourceService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AReflectionService reflection;

    private HorizontalLayout layout;

    private String width;

    private String height;

    private String valoreCodificato = VUOTA;


    /**
     * Costruttore senza parametri <br>
     * L' istanza viene costruita con appContext.getBean(ATextField.class) <br>
     */
    public AImageField() {
        imageField = new Image();

        if (VaadinSession.getCurrent() != null && AEPreferenza.usaDebug.is()) {
            fixUpload();
        } else {
            add(imageField);
        }
    } // end of SpringBoot constructor


    protected Upload fixUpload() {
        MemoryBuffer buffer = new MemoryBuffer();
        HorizontalLayout layer = new HorizontalLayout();
        HorizontalLayout output = new HorizontalLayout();
        Upload upload = new Upload(buffer);
        upload.setDropLabel(new Span());
        upload.setUploadButton(new Button("Seleziona..."));
        //        upload.addSucceededListener(event -> {
        //            Component component = createComponent(event.getMIMEType(), event.getFileName(), buffer.getInputStream());
        //            showOutput(event.getFileName(), component, output);
        //        });
        upload.addSucceededListener(event -> seleziona(event));
        layer.add(imageField, upload);
        add(layer);

        return upload;
    }


    protected void seleziona(SucceededEvent event) {
        String fileName = event.getFileName();
        valoreCodificato = resourceService.getSrc("bandiere/" + fileName);
        setPresentationValue(valoreCodificato);
        updateValue();
    }


    @Override
    protected String generateModelValue() {
        return valoreCodificato;
    }


    @Override
    protected void setPresentationValue(String valoreCodificato) {
        byte[] bytesDue = Base64.decodeBase64(valoreCodificato);
        StreamResource resource = null;
        try {
            resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(bytesDue));
            imageField.setSrc(resource);
        } catch (Exception unErrore) {
        }
    }


    @Override
    public void setWidth(String width) {
        this.width = width;
        imageField.setWidth(width);
    }


    @Override
    public void setHeight(String height) {
        this.height = height;
        imageField.setHeight(height);
    }


    @Deprecated
    private Component createComponent(String mimeType, String fileName, InputStream stream) {
        if (mimeType.startsWith("text")) {
            return createTextComponent(stream);
        } else if (mimeType.startsWith("image")) {
            Image image = new Image();
            try {

                byte[] bytes = IOUtils.toByteArray(stream);
                image.getElement().setAttribute("src", new StreamResource(fileName, () -> new ByteArrayInputStream(bytes)));
                try (ImageInputStream in = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
                    final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
                    if (readers.hasNext()) {
                        ImageReader reader = readers.next();
                        try {
                            reader.setInput(in);
                            image.setWidth(reader.getWidth(0) + "px");
                            image.setHeight(reader.getHeight(0) + "px");
                        } finally {
                            reader.dispose();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            image.setWidth(width);
            image.setHeight(height);
            return image;
        }
        Div content = new Div();
        String text = String.format("Mime type: '%s'\nSHA-256 hash: '%s'", mimeType, MessageDigestUtil.sha256(stream.toString()));
        content.setText(text);
        return content;

    }


    @Deprecated
    private Component createTextComponent(InputStream stream) {
        String text;
        try {
            text = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            text = "exception reading stream";
        }
        return new Text(text);
    }


    @Deprecated
    private void showOutput(String text, Component content, HasComponents outputContainer) {
        HtmlComponent p = new HtmlComponent(Tag.P);
        p.getElement().setText(text);
        outputContainer.add(p);
        outputContainer.add(content);
    }


}
