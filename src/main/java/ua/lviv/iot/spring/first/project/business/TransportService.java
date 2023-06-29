package ua.lviv.iot.spring.first.project.business;

import ua.lviv.iot.spring.first.project.rest.model.Transport;

import java.util.List;

public interface TransportService {
    List<Transport> findAllTransports();

    Transport findTransportById(Integer transportId);

    Transport createTransport(Transport transport);

    Transport remove(Integer transportId);

    Transport updateTransport(Integer id, Transport transport);
}
