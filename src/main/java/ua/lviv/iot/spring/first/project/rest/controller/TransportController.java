package ua.lviv.iot.spring.first.project.rest.controller;

import org.springframework.http.ResponseEntity;
import ua.lviv.iot.spring.first.project.rest.model.Transport;

import java.util.List;

public interface TransportController {
    List<Transport> getTransports();

    Transport getTransport(Integer transportId);

    Transport createTransport(Transport transport);

    ResponseEntity<Transport> deleteTransport(Integer transportId);

    Transport updateTransport(Integer transportId, Transport transport);
}
