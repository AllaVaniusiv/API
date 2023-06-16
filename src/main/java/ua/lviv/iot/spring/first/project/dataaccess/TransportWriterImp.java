package ua.lviv.iot.spring.first.project.dataaccess;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;
import ua.lviv.iot.spring.first.project.rest.model.Transport;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;


@Component
public final class TransportWriterImp implements TransportWriter {
    public static final int MAGIC_NUMBER_1 = 3;
    public static final int MAGIC_NUMBER_2 = 4;
    public static final int MAGIC_NUMBER_3 = 5;
    public static final int MAGIC_NUMBER_4 = 6;


    private final String basePath = System.getProperty("user.dir");
    private final String transportsBasePath =
            String.format("%s/data/transports", basePath);
    private final String todayFileName = String.format("transport-%s.csv",
            LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
    private final String todayFilePath = String.format("%s/%s",
            transportsBasePath, todayFileName);
    private final String[] fields = Arrays.stream(Transport.class
            .getDeclaredFields()).map(Field::getName).toArray(String[]::new);

    private final Map<Integer, Transport> transportsMap = new HashMap<>();
    private final Map<Integer, String> transportPathMap = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        List<String> paths = getMonthPaths(transportsBasePath,
                LocalDateTime.now());

        for (String path : paths) {
            Map<Integer, Transport> transports = loadTransports(path);
            for (Map.Entry<Integer, Transport> entry : transports.entrySet()) {
                transportsMap.put(entry.getKey(), entry.getValue());
                transportPathMap.put(entry.getKey(), path);
            }
        }
    }

    public List<String> getMonthPaths(final String basePath,
                                      final LocalDateTime date)
            throws IOException {
        String currentMonth = date.format(DateTimeFormatter
                .ofPattern("yyyy-MM"));
        String currentMonthFileName = String.format("transport-%s-\\d{2}\\.csv",
                currentMonth);
        Pattern pattern = Pattern.compile(currentMonthFileName);
        return Files
                .list(Paths.get(basePath))
                .map(Path::toString)
                .filter(path -> pattern.matcher(path).find())
                .sorted()
                .toList();
    }

    public List<Transport> findAll() {
        return new ArrayList<>(transportsMap.values());
    }

    public Transport findById(final Integer transportId) {
        return transportsMap.get(transportId);
    }

    public Transport save(final Transport transport) {
        transportsMap.put(transport.getId(), transport);

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
                    transport.getId().toString(),
                    transport.getName(),
                    String.valueOf(transport.getCarryingCapacity()),
                    String.valueOf(transport.getDriverId()),
                    transport.getNameCareer(),
                    transport.getStartOfWork(),
                    transport.getEndOfWork()
            };
            csvWriter.writeNext(values);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return transport;
    }

    public void delete(final Transport transport) {
        transportsMap.remove(transport.getId());

        String path = transportPathMap.get(transport.getId());
        Map<Integer, Transport> transports = loadTransports(path);

        transports.remove(transport.getId());
        writeTransports(path, new ArrayList<>(transports.values()));
    }

    @Override
    public Transport update(final Transport transport) {
        transportsMap.put(transport.getId(), transport);

        String path = transportPathMap.get(transport.getId());
        Map<Integer, Transport> transports = loadTransports(path);

        transports.put(transport.getId(), transport);
        writeTransports(path, new ArrayList<>(transports.values()));

        return transport;
    }

    public int getLastId() {
        if (transportsMap.isEmpty()) {
            return 0;
        }
        return Collections.max(transportsMap.keySet());
    }

    public Map<Integer, Transport> loadTransports(final String path) {
        Map<Integer, Transport> transports = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            reader.skip(1);

            String[] values;
            while ((values = reader.readNext()) != null) {
                Transport transport = new Transport(
                        Integer.parseInt(values[0]),
                        values[1],
                        Integer.parseInt(values[2]),
                        Integer.parseInt(values[MAGIC_NUMBER_1]),
                        values[MAGIC_NUMBER_2],
                        values[MAGIC_NUMBER_3],
                        values[MAGIC_NUMBER_4]

                );
                transports.put(transport.getId(), transport);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return transports;
    }

    public void writeTransports(final String path,
                                final List<Transport> transports) {
        try (
                FileWriter fileWriter = new FileWriter(path);
                CSVWriter csvWriter = new CSVWriter(fileWriter)
        ) {
            csvWriter.writeNext(fields);
            for (Transport transport : transports) {
                String[] values = new String[]{
                        transport.getId().toString(),
                        transport.getName(),
                        String.valueOf(transport.getCarryingCapacity()),
                        String.valueOf(transport.getDriverId()),
                        transport.getNameCareer(),
                        transport.getStartOfWork(),
                        transport.getEndOfWork()
                };
                csvWriter.writeNext(values);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
