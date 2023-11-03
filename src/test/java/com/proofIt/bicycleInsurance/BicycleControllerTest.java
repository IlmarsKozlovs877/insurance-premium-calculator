package com.proofIt.bicycleInsurance;

import com.proofIt.bicycleInsurance.dto.BicycleResponseDto;
import com.proofIt.bicycleInsurance.dto.BicyclesRequestDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BicycleControllerTest {

    private static final String API_ENDPOINT = "/api/v1/calculate";

    @Autowired
    private TestRestTemplate restTemplate;

    String JSON_REQUEST = "{\n" +
            "  \"bicycles\" : [\n" +
            "    {\n" +
            "      \"make\" : \"Pearl\",\n" +
            "      \"model\" : \"Gravel SL EVO\",\n" +
            "      \"coverage\" : \"EXTRA\",\n" +
            "      \"manufactureYear\" : 2015,\n" +
            "      \"sumInsured\" : 1000,\n" +
            "      \"risks\" : [\n" +
            "        \"THEFT\",\n" +
            "        \"DAMAGE\",\n" +
            "        \"THIRD_PARTY_DAMAGE\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"make\" : \"Sensa\",\n" +
            "      \"model\" : \"V2\",\n" +
            "      \"coverage\" : \"STANDARD\",\n" +
            "      \"manufactureYear\" : 2020,\n" +
            "      \"sumInsured\" : 225,\n" +
            "      \"risks\" : [\n" +
            "        \"DAMAGE\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"make\" : \"OTHER\",\n" +
            "      \"model\" : \"OTHER\",\n" +
            "      \"coverage\" : \"STANDARD\",\n" +
            "      \"manufactureYear\" : 2019,\n" +
            "      \"sumInsured\" : 200,\n" +
            "      \"risks\" : [\n" +
            "        \"DAMAGE\",\n" +
            "        \"THIRD_PARTY_DAMAGE\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    String INVALID_JSON_REQUEST = "{\n" +
            "  \"bicycles\" : [\n" +
            "    {\n" +
            "      \"make\" : \"Pearl\",\n" +
            "      \"model\" : \"Gravel SL EVO\",\n" +
            "      \"coverage\" : \"EXTRA\",\n" +
            "      \"manufactureYear\" : 1015,\n" +
            "      \"sumInsured\" : 100000,\n" +
            "      \"risks\" : [\n" +
            "        \"THEFT\",\n" +
            "        \"DAMAGE\",\n" +
            "        \"THIRD_PARTY_DAMAGE\"\n" +
            "      ]\n" +
            "    },\n" +
            "  ]\n" +
            "}";
    String JSON_RESPONSE = "{\n" +
            "    \"objects\": [\n" +
            "        {\n" +
            "            \"attributes\": {\n" +
            "                \"MANUFACTURE_YEAR\": 2015,\n" +
            "                \"MODEL\": \"Gravel SL EVO\",\n" +
            "                \"MAKE\": \"Pearl\"\n" +
            "            },\n" +
            "            \"coverageType\": \"EXTRA\",\n" +
            "            \"risks\": [\n" +
            "                {\n" +
            "                    \"riskType\": \"THIRD_PARTY_DAMAGE\",\n" +
            "                    \"sumInsured\": 100.00,\n" +
            "                    \"premium\": 12.00\n" +
            "                },\n" +
            "                {\n" +
            "                    \"riskType\": \"THEFT\",\n" +
            "                    \"sumInsured\": 1000.00,\n" +
            "                    \"premium\": 30.00\n" +
            "                },\n" +
            "                {\n" +
            "                    \"riskType\": \"DAMAGE\",\n" +
            "                    \"sumInsured\": 500.00,\n" +
            "                    \"premium\": 6.95\n" +
            "                }\n" +
            "\n" +
            "            ],\n" +
            "            \"sumInsured\": 1000,\n" +
            "            \"premium\": 48.95\n" +
            "        },\n" +
            "        {\n" +
            "            \"attributes\": {\n" +
            "                \"MANUFACTURE_YEAR\": 2020,\n" +
            "                \"MODEL\": \"V2\",\n" +
            "                \"MAKE\": \"Sensa\"\n" +
            "            },\n" +
            "            \"coverageType\": \"STANDARD\",\n" +
            "            \"risks\": [\n" +
            "                {\n" +
            "                    \"riskType\": \"DAMAGE\",\n" +
            "                    \"sumInsured\": 112.50,\n" +
            "                    \"premium\": 5.78\n" +
            "                }\n" +
            "            ],\n" +
            "            \"sumInsured\": 225,\n" +
            "            \"premium\": 5.78\n" +
            "        },\n" +
            "        {\n" +
            "            \"attributes\": {\n" +
            "                \"MANUFACTURE_YEAR\": 2019,\n" +
            "                \"MODEL\": \"OTHER\",\n" +
            "                \"MAKE\": \"OTHER\"\n" +
            "            },\n" +
            "            \"coverageType\": \"STANDARD\",\n" +
            "            \"risks\": [\n" +
            "                {\n" +
            "                    \"riskType\": \"THIRD_PARTY_DAMAGE\",\n" +
            "                    \"sumInsured\": 100.00,\n" +
            "                    \"premium\": 12.00\n" +
            "                },\n" +
            "                {\n" +
            "                    \"riskType\": \"DAMAGE\",\n" +
            "                    \"sumInsured\": 100.00,\n" +
            "                    \"premium\": 11.00\n" +
            "                }\n" +
            "            ],\n" +
            "            \"sumInsured\": 200,\n" +
            "            \"premium\": 23.00\n" +
            "        }\n" +
            "    ],\n" +
            "    \"premium\": 77.73\n" +
            "}";

    @Test
    public void testDoSomething() {
        Gson gson = new Gson();

        BicyclesRequestDto bicyclesRequestDto = gson.fromJson(JSON_REQUEST, BicyclesRequestDto.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BicyclesRequestDto> entity = new HttpEntity<>(bicyclesRequestDto, headers);

        ResponseEntity<BicycleResponseDto> response = restTemplate.exchange(API_ENDPOINT, HttpMethod.POST, entity, BicycleResponseDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        BicycleResponseDto bicycleResponseDto = gson.fromJson(JSON_RESPONSE, BicycleResponseDto.class);

        for (int i = 0; i< bicyclesRequestDto.getBicycles().length; i++){
            Assertions.assertEquals(Objects.requireNonNull(response.getBody()).getObjects()[i].getPremium(), bicycleResponseDto.getObjects()[i].getPremium());
        }
    }

    @Test
    public void testDoSomething2() {
        Gson gson = new Gson();

        BicyclesRequestDto bicyclesRequestDto = gson.fromJson(INVALID_JSON_REQUEST, BicyclesRequestDto.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BicyclesRequestDto> entity = new HttpEntity<>(bicyclesRequestDto, headers);

        ResponseEntity<BicycleResponseDto> response = restTemplate.exchange(API_ENDPOINT, HttpMethod.POST, entity, BicycleResponseDto.class);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
