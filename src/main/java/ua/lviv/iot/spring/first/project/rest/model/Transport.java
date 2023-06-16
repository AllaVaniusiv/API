package ua.lviv.iot.spring.first.project.rest.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Transport {
    private Integer id;
    private String name;
    private int carryingCapacity;
    private int driverId;
    private String nameCareer;
    private String startOfWork;
    private String endOfWork;

}

