package ua.lviv.iot.spring.first.project.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.spring.first.project.dataaccess.TransportWriter;
import ua.lviv.iot.spring.first.project.rest.model.Transport;

import java.util.List;

@Service
public final class TransportServiceImp implements TransportService {

    @Autowired
    private TransportWriter transportWriter;

    public List<Transport> findAllTransports() {
        return transportWriter.findAll();
    }

    public Transport findTransportById(final Integer transportId) {
        return transportWriter.findById(transportId);
    }


    public Transport createTransport(final Transport transport) {
        transport.setId(transportWriter.getLastId() + 1);
        return transportWriter.save(transport);
    }

    public Transport remove(final Integer transportId) {
        Transport transport = transportWriter.findById(transportId);
        if (transport != null) {
            transportWriter.delete(transport);
        }
        return transport;
    }

    public Transport updateTransport(final Integer id,
                                     final Transport transport) {
        transport.setId(id);
        return transportWriter.update(transport);
    }
}
