package com.proofIt.bicycleInsurance.controller;

import com.proofIt.bicycleInsurance.dto.BicycleResponseDto;
import com.proofIt.bicycleInsurance.dto.BicyclesRequestDto;
import com.proofIt.bicycleInsurance.Coverage;
import com.proofIt.bicycleInsurance.RiskType;
import com.proofIt.bicycleInsurance.service.PremiumCalculationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.proofIt.bicycleInsurance.utils.DateUtil.getAge;

@RestController
@RequestMapping(value = ("/api/v1"), produces = MediaType.APPLICATION_JSON_VALUE)
public class BicycleController {

    private final PremiumCalculationService premiumCalculationService;

    @Autowired
    public BicycleController(PremiumCalculationService premiumCalculationService) {
        this.premiumCalculationService = premiumCalculationService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<BicycleResponseDto> doSomething(@RequestBody @Valid BicyclesRequestDto bicycles) {
        BicycleResponseDto.BicycleResponse[] bicycleResponseDto = new BicycleResponseDto.BicycleResponse[bicycles.getBicycles().length];

        int bicycleIteration = 0;
        for(BicyclesRequestDto.Bicycle bicycle : bicycles.getBicycles()){
            double premiumTotal = 0.0;
            BicycleResponseDto.Risk[] risks = new BicycleResponseDto.Risk[bicycle.getRisks().length];
            int riskIteration = 0;
            for (RiskType riskType: bicycle.getRisks()) {

                double riskInsurance = premiumCalculationService.calculateRiskInsurance(riskType,
                        bicycle.getSumInsured());

                int actualAge = getAge(bicycle.getManufactureYear());
                if (actualAge > 10){
                    throw new IllegalArgumentException("Bicycle is too old");
                }

                double riskPremium = premiumCalculationService.calculatePremium(riskType,
                        riskInsurance,
                        bicycle.getModel(),
                        bicycle.getMake(),
                        actualAge,
                        bicycle.getRisks().length);

                premiumTotal += riskPremium;

                risks[riskIteration] = new BicycleResponseDto.Risk(
                        riskType,
                        riskInsurance,
                        riskPremium);
                riskIteration++;
            }

            bicycleResponseDto[bicycleIteration] =
                    new BicycleResponseDto.BicycleResponse(
                            new BicycleResponseDto.Attributes(
                                    bicycle.getManufactureYear(),
                                    bicycle.getModel(),
                                    bicycle.getMake()),
                            bicycle.getCoverage() == null ? Coverage.STANDARD : bicycle.getCoverage(),
                            risks,
                            bicycle.getSumInsured(),
                            premiumTotal);

            bicycleIteration++;
        }

        return new ResponseEntity<>(new BicycleResponseDto(bicycleResponseDto), HttpStatus.OK);
    }

}
