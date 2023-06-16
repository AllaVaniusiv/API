package ua.lviv.iot.spring.first.project.dataaccess;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.lviv.iot.spring.first.project.rest.model.Driver;


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
public class DriverWriterTests {

    @Autowired
    DriverWriter driverWriter;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    void testWriteDrivers() throws IOException {
        tempFolder.create();

        List<Driver> drivers = new ArrayList<>();
        drivers.add(new Driver(1, "John", 25, 1));
        drivers.add(new Driver(2, "Smith", 35, 2));
        drivers.add(new Driver(3, "Bond", 45, 3));

        String path = String.format("%s/driver_test.csv", tempFolder.getRoot().getPath());

        driverWriter.writeDrivers(path, drivers);

        Collection<Driver> loadedDrivers = driverWriter.loadDrivers(path).values();
        assert CollectionUtils.isEqualCollection(drivers, loadedDrivers);
    }

    @Test
    void testGetMonthPaths() throws IOException {
        tempFolder.create();

        String basePath = tempFolder.getRoot().getPath();

        List<Path> prevMonthPaths = List.of(Paths.get(basePath, "driver-2023-05-10.csv"));
        List<Path> currentMonthPaths = Arrays.asList(
                Paths.get(basePath, "driver-2023-06-13.csv"),
                Paths.get(basePath, "driver-2023-06-14.csv")
        );
        List<Path> nextMonthPaths = List.of(Paths.get(basePath, "driver-2023-07-05.csv"));

        List<Path> createPaths = new ArrayList<>();
        createPaths.addAll(prevMonthPaths);
        createPaths.addAll(currentMonthPaths);
        createPaths.addAll(nextMonthPaths);

        for (Path path : createPaths)
            new File(path.toString()).createNewFile();

        LocalDateTime date = LocalDateTime.of(2023, 6, 15, 0, 0);
        List<String> actualPaths = driverWriter.getMonthPaths(tempFolder.getRoot().getPath(), date);

        List<String> expectedPaths = currentMonthPaths.stream().map(Path::toString).toList();
        assert CollectionUtils.isEqualCollection(actualPaths, expectedPaths);
    }

}
