package com.proofIt.bicycleInsurance.dto;

import com.proofIt.bicycleInsurance.Coverage;
import com.proofIt.bicycleInsurance.RiskType;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BicyclesRequestDto {

    private Bicycle [] bicycles;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bicycle {

        private String make;
        private String model;
        private Coverage coverage;

        private int manufactureYear;

        @Max(10000)
        private Double sumInsured;

        private RiskType[] risks;
    }
}