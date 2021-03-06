package com.assignment.spring.weather;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "weather")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeatherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "City is mandatory")
    private String city;

    private String country;

    @NotNull(message = "Temperature is mandatory")
    private Double temperature;

    public Integer getId() {
        return id;
    }
}
