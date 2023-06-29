package ua.lviv.iot.spring.first.project.rest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import ua.lviv.iot.spring.first.project.business.TransportService;
import ua.lviv.iot.spring.first.project.rest.model.Transport;

import java.util.List;


@RequestMapping("/transports")
@RestController
public final class TransportControllerImp implements TransportController {
    private int lastId = 0;


    @Autowired
    private TransportService transportService;

    @GetMapping
    public List<Transport> getTransports() {
        return transportService.findAllTransports();
    }

    @GetMapping(path = "/{id}")
    public Transport getTransport(
            @PathVariable("id") final Integer transportId) {
        return transportService.findTransportById(transportId);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Transport createTransport(@RequestBody final Transport transport) {
        transport.setId(++lastId);
        return transportService.createTransport(transport);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Transport> deleteTransport(
            @PathVariable("id") final Integer transportId) {

        HttpStatus status = transportService.remove(transportId)
                == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return ResponseEntity.status(status).build();
    }

    @PutMapping(path = "/{id}")
    public Transport updateTransport(
            @PathVariable("id") final Integer transportId,
                                     @RequestBody final Transport transport) {
        return transportService.updateTransport(transportId, transport);
    }
}

