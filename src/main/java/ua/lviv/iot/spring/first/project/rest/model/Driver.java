package ua.lviv.iot.spring.first.project.rest.model;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Driver {
    private Integer id;
    private String name;
    private int age;
    private int transportId;
}
