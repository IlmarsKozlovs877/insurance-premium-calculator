package com.proofIt.bicycleInsurance.service;

import com.proofIt.bicycleInsurance.RiskType;
import com.proofIt.bicycleInsurance.utils.FileReaderUtil;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.proofIt.bicycleInsurance.Constants.*;

@Service
public class PremiumCalculationService {

    public double calculateRiskInsurance(RiskType riskType, double sumInsured) {
        switch (riskType) {
            case THEFT:
                return sumInsured;
            case DAMAGE:
                return sumInsured / DAMAGE_DIVISION_FACTOR;
            case THIRD_PARTY_DAMAGE:
                return THIRD_PARTY_DAMAGE_INSURANCE;
            default:
                throw new IllegalArgumentException("Invalid risk type");
        }
    }

    public double calculatePremium(RiskType riskType, double riskInsurance, String model, String make, int ageActual, int riskCount) {
        switch (riskType) {
            case THEFT:
                return calculateTheftPremium(riskInsurance);
            case DAMAGE:
                return calculateDamagePremium(riskInsurance, model, make, ageActual);
            case THIRD_PARTY_DAMAGE:
                return calculateThirdPartyDamagePremium(riskInsurance, riskCount);
            default:
                throw new IllegalArgumentException("Risk type not supported");
        }
    }

    private double calculateThirdPartyDamagePremium(double sumInsured, int riskCount) {
        return calculateRiskBasePremium(RiskType.THIRD_PARTY_DAMAGE) * calculateSumInsuredFactor(sumInsured) * calculateRiskCountFactor(riskCount);
    }

    private double calculateDamagePremium(double sumInsured, String model, String make, int ageActual) {
        return setScale(calculateRiskBasePremium(RiskType.DAMAGE) * calculateSumInsuredFactor(sumInsured) * calculateBicycleAgeFactor(model, make, ageActual));
    }

    private double calculateTheftPremium(double sumInsured) {
        return calculateRiskBasePremium(RiskType.THEFT) * calculateSumInsuredFactor(sumInsured);
    }

    private double calculateSumInsuredFactor(double sumInsured) {
        return getFactor(sumInsured, getSumInsuredFactorData(sumInsured));
    }

    private double calculateRiskCountFactor(double riskCount) {
        return getFactor(riskCount, getRiskCountFactorData(riskCount));
    }

    private double calculateBicycleAgeFactor(String model, String make, int ageActual) {
        return getFactor(ageActual, getBicycleAgeFactorData(model, make, ageActual));
    }

    private double calculateRiskBasePremium(RiskType riskType) {
        ArrayList<LinkedHashMap<String, Serializable>> riskBasePremiumData = FileReaderUtil.getDataFromSourceFiles(RISK_BASE_PREMIUM_DATA);
        for (LinkedHashMap<String, Serializable> map : riskBasePremiumData) {
            if (riskType.toString().equals(map.get(RISK_TYPE))){
                return  (double) map.get(PREMIUM);
            }
        }
        throw new IllegalArgumentException("No base premium found for the given risk type");
    }

    private Map<String, Serializable> getSumInsuredFactorData(double sumInsured) {
        ArrayList<LinkedHashMap<String, Serializable>> sumInsuredFactorData = FileReaderUtil.getDataFromSourceFiles(SUM_INSURED_FACTOR_DATA);

        for (LinkedHashMap<String, Serializable> map : sumInsuredFactorData) {
            if (sumInsured >= (Double) map.get(VALUE_FROM) && sumInsured <= (Double) map.get(VALUE_TO)){
                return map;
            }
        }
        throw new IllegalArgumentException("Sum insured not supported");
    }

    private Map<String, Serializable> getRiskCountFactorData(double riskCount) {
        ArrayList<LinkedHashMap<String, Serializable>> riskCountFactorData = FileReaderUtil.getDataFromSourceFiles(RISK_COUNT_FACTOR_DATA);

        for (LinkedHashMap<String, Serializable> map : riskCountFactorData) {
            if (riskCount >= (double) map.get(VALUE_FROM) && riskCount <= (double) map.get(VALUE_TO)){
                return map;
            }
        }
        throw new IllegalArgumentException("Risk count not supported");
    }

    private Map<String, Serializable> getBicycleAgeFactorData(String model, String make, int age) {
        ArrayList<LinkedHashMap<String, Serializable>> riskCountFactorData = FileReaderUtil.getDataFromSourceFiles(AGE_FACTOR_DATA);

        if (make.equals(MAKE_TYPE_OTHER)){
            for (LinkedHashMap<String, Serializable> map : riskCountFactorData) {
                if (age >= (double) map.get(VALUE_FROM) && age <= (double) map.get(VALUE_TO) && map.get(MAKE) == null){
                    return map;
                }
            }
            throw new IllegalArgumentException("Bicycle age not supported");
        }

        for (LinkedHashMap<String, Serializable> map : riskCountFactorData) {
            if (model.equals(map.get(MODEL)) && make.equals(map.get(MAKE)) && age >= (double) map.get(VALUE_FROM) && age <= (double) map.get(VALUE_TO)){
                return map;
            }
        }
        for (LinkedHashMap<String, Serializable> map : riskCountFactorData) {
            if (make.equals(map.get(MAKE)) && age >= (double) map.get(VALUE_FROM) && age <= (double) map.get(VALUE_TO)){
                return map;
            }
        }
        for (LinkedHashMap<String, Serializable> map : riskCountFactorData) {
            if (age >= (double) map.get(VALUE_FROM) && age <= (double) map.get(VALUE_TO)){
                return map;
            }
        }
        throw new IllegalArgumentException("Bicycle age not supported");
    }

    private double getFactor(double riskCount, Map<String, Serializable> riskCountFactorData) {
        double factorMax = (double) riskCountFactorData.get(FACTOR_MAX);
        double factorMin = (double) riskCountFactorData.get(FACTOR_MIN);
        double valueTo = (double) riskCountFactorData.get(VALUE_TO);
        double valueFrom = (double) riskCountFactorData.get(VALUE_FROM);

        return factorMax - (factorMax - factorMin) * (valueTo - riskCount) / (valueTo - valueFrom);
    }

    private double setScale(double factor) {
        BigDecimal bd = new BigDecimal(factor);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
