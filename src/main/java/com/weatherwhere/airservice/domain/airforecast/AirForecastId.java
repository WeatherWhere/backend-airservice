package com.weatherwhere.airservice.domain.airforecast;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder
public class AirForecastId implements Serializable {
    @Column(name="base_date")
    private LocalDate baseDate;

    @Column(name="city")
    private String city;
}
