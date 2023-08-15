package com.example.best_travel.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@SuperBuilder // Se utiliza en vez de builder ya que esta implementando una clase abstracta
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseErrorResponse implements Serializable {
    private String status;
    private Integer code;
}
