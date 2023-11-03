package com.proofIt.bicycleInsurance.dto;

import com.proofIt.bicycleInsurance.Coverage;
import com.proofIt.bicycleInsurance.RiskType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BicycleResponseDto {

    private BicycleResponse[] objects;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BicycleResponse {
        private Attributes attributes;

        private Coverage coverageType;

        private Risk[] risks;

        private Double sumInsured;

        private Double premium;


    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Attributes {
        private int manufactureYear;
        private String model;
        private String make;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Risk {
        private RiskType riskType;
        private Double sumInsured;
        private Double premium;
    }

}
