package it.algos.vaadflow14.ui.utility;

import com.opencsv.*;
import com.opencsv.exceptions.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.upload.*;
import com.vaadin.flow.component.upload.receivers.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.util.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.io.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 24-set-2021
 * Time: 19:48
 */
@Route("csv")
public class CSVView extends VerticalLayout {
    private Grid<String[]> grid = new Grid<>();

    public CSVView() {
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes(".csv");
        upload.addSucceededListener(e -> displayCsv(buffer.getInputStream()));
        add(upload, grid);
    }

    private void displayCsv(InputStream resourceAsStream) {
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader reader = new CSVReaderBuilder(new InputStreamReader(resourceAsStream)).withCSVParser(parser).build();

        try {
            List<String[]> entries = reader.readAll();
            String[] headers = entries.get(0);
            grid.removeAllColumns();

            for (int i = 0; i < headers.length; i++) {
                int colIndex = i;
                grid.addColumn(row -> row[colIndex])
                        .setHeader(SharedUtil.camelCaseToHumanFriendly(headers[colIndex]));
            }

            grid.setItems(entries.subList(1, entries.size()));
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
}
