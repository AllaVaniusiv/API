package ua.lviv.iot.spring.first.project.dataaccess;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.lviv.iot.spring.first.project.rest.model.Transport;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@SpringBootTest
public class TransportWriterTests {

    @Autowired
    TransportWriter transportWriter;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    void testWriteTransports() throws IOException {
        tempFolder.create();

        List<Transport> transports = new ArrayList<>();
        transports.add(new Transport(1, "John Deere", 250, 1,"Granite career","11:00","21:00"));
        transports.add(new Transport(2, "Farmtrac", 350, 2,"Granite career","12:00","21:00"));
        transports.add(new Transport(3, "Deutz-Fahr", 450, 3,"Granite career","13:00","21:00"));

        String path = String.format("%s/transport_test.csv", tempFolder.getRoot().getPath());

        transportWriter.writeTransports(path, transports);

        Collection<Transport> loadedTransports = transportWriter.loadTransports(path).values();
        assert CollectionUtils.isEqualCollection(transports, loadedTransports);
    }

    @Test
    void testGetMonthPaths() throws IOException {
        tempFolder.create();

        String basePath = tempFolder.getRoot().getPath();

        List<Path> prevMonthPaths = List.of(Paths.get(basePath, "transport-2023-05-10.csv"));
        List<Path> currentMonthPaths = Arrays.asList(
                Paths.get(basePath, "transport-2023-06-13.csv"),
                Paths.get(basePath, "transport-2023-06-14.csv")
        );
        List<Path> nextMonthPaths = List.of(Paths.get(basePath, "transport-2023-07-05.csv"));

        List<Path> createPaths = new ArrayList<>();
        createPaths.addAll(prevMonthPaths);
        createPaths.addAll(currentMonthPaths);
        createPaths.addAll(nextMonthPaths);

        for (Path path : createPaths)
            new File(path.toString()).createNewFile();

        LocalDateTime date = LocalDateTime.of(2023, 6, 15, 0, 0);
        List<String> actualPaths = transportWriter.getMonthPaths(tempFolder.getRoot().getPath(), date);

        List<String> expectedPaths = currentMonthPaths.stream().map(Path::toString).toList();
        assert CollectionUtils.isEqualCollection(actualPaths, expectedPaths);
    }

}
