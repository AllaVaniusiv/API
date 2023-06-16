package ua.lviv.iot.spring.first.project.dataaccess;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;
import ua.lviv.iot.spring.first.project.rest.model.Driver;


import javax.annotation.PostConstruct;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.regex.Pattern;

@Component
public final class DriverWriterImp implements DriverWriter {
    public static final int INDEX = 3;

    private final String basePath = System.getProperty("user.dir");
    private final String driversBasePath = String.format("%s/data/drivers",
            basePath);
    private final String todayFileName = String.format("driver-%s.csv",
            LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
    private final String todayFilePath = String.format("%s/%s", driversBasePath,
            todayFileName);
    private final String[] fields = Arrays.stream(Driver.class
            .getDeclaredFields()).map(Field::getName).toArray(String[]::new);

    private final Map<Integer, Driver> driversMap = new HashMap<>();
    private final Map<Integer, String> driverPathMap = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        List<String> paths = getMonthPaths(driversBasePath,
                LocalDateTime.now());

        for (String path : paths) {
            Map<Integer, Driver> drivers = loadDrivers(path);
            for (Map.Entry<Integer, Driver> entry : drivers.entrySet()) {
                driversMap.put(entry.getKey(), entry.getValue());
                driverPathMap.put(entry.getKey(), path);
            }
        }
    }

    public List<String> getMonthPaths(final String basePath,
                                      final LocalDateTime date)
            throws IOException {
        String currentMonth = date.format(DateTimeFormatter
                .ofPattern("yyyy-MM"));
        String currentMonthFileName = String.format("driver-%s-\\d{2}\\.csv",
                currentMonth);
        Pattern pattern = Pattern.compile(currentMonthFileName);
        return Files
                .list(Paths.get(basePath))
                .map(Path::toString)
                .filter(path -> pattern.matcher(path).find())
                .sorted()
                .toList();
    }

    public List<Driver> findAll() {
        return new ArrayList<>(driversMap.values());
    }

    public Driver findById(final Integer transportId) {
        return driversMap.get(transportId);
    }

    public Driver save(final Driver driver) {
        driversMap.put(driver.getId(), driver);

        File f = new File(todayFilePath);
        boolean exists = f.exists();

        try (
                FileWriter fileWriter = new FileWriter(f, true);
                CSVWriter csvWriter = new CSVWriter(fileWriter)
        ) {
            if (!exists) {
                csvWriter.writeNext(fields);
            }

            String[] values = new String[]{
                    driver.getId().toString(),
                    driver.getName(),
                    String.valueOf(driver.getAge()),
                    String.valueOf(driver.getTransportId())
            };
            csvWriter.writeNext(values);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return driver;
    }

    public void delete(final Driver driver) {
        driversMap.remove(driver.getId());

        String path = driverPathMap.get(driver.getId());
        Map<Integer, Driver> drivers = loadDrivers(path);

        drivers.remove(driver.getId());
        writeDrivers(path, new ArrayList<>(drivers.values()));
    }

    @Override
    public Driver update(final Driver driver) {
        driversMap.put(driver.getId(), driver);

        String path = driverPathMap.get(driver.getId());
        Map<Integer, Driver> drivers = loadDrivers(path);

        drivers.put(driver.getId(), driver);
        writeDrivers(path, new ArrayList<>(drivers.values()));

        return driver;
    }

    public int getLastId() {
        if (driversMap.isEmpty()) {
            return 0;
        }
        return Collections.max(driversMap.keySet());
    }

    public Map<Integer, Driver> loadDrivers(final String path) {
        Map<Integer, Driver> drivers = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            reader.skip(1);

            String[] values;
            while ((values = reader.readNext()) != null) {
                Driver driver = new Driver(
                        Integer.parseInt(values[0]),
                        values[1],
                        Integer.parseInt(values[2]),
                        Integer.parseInt(values[INDEX])
                );
                drivers.put(driver.getId(), driver);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return drivers;
    }

    public void writeDrivers(final String path, final List<Driver> drivers) {
        try (
                FileWriter fileWriter = new FileWriter(path);
                CSVWriter csvWriter = new CSVWriter(fileWriter)
        ) {
            csvWriter.writeNext(fields);
            for (Driver driver : drivers) {
                String[] values = new String[]{
                        driver.getId().toString(),
                        driver.getName(),
                        String.valueOf(driver.getAge()),
                        String.valueOf(driver.getTransportId())
                };
                csvWriter.writeNext(values);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
