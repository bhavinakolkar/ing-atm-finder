package com.finder.atm.routes;

import com.finder.atm.model.ATMDetail;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.NotifyBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestAPIRouterTest {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static boolean isSetupDone = false;

    @Before
    public void init() {
        if(!isSetupDone) {
            NotifyBuilder notify = new NotifyBuilder(camelContext)
                    .fromRoute("process-atm-details-timer")
                    .whenDone(1).create();
            assertThat(notify.matches(10, TimeUnit.SECONDS)).isTrue();
            isSetupDone = true;
        }
    }


    /**
     *  Test fetch ATM List REST API
     * @throws Exception
     */
    @Test
    public void testFetchAllATMList() throws Exception {
        ResponseEntity<List<ATMDetail>> listResponseEntity = testRestTemplate.withBasicAuth("admin", "admin")
                .exchange("/ing-atm-finder/atm", HttpMethod.GET, null, new ParameterizedTypeReference<List<ATMDetail>>() {
                });
        List<ATMDetail> atmDetails = listResponseEntity.getBody();
        assertThat(atmDetails).isNotNull();
        assertThat(atmDetails).isNotEmpty();
        assertThat(atmDetails.size()).isEqualTo(202);
        assertThat(listResponseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    /**
     * Test fetch ATM List by given City
     * @throws Exception
     */
    @Test
    public void testFetchATMListByCity() throws Exception {
        ResponseEntity<List<ATMDetail>> listResponseEntity = testRestTemplate.withBasicAuth("admin", "admin")
                .exchange("/ing-atm-finder/atm/Amsterdam", HttpMethod.GET, null, new ParameterizedTypeReference<List<ATMDetail>>() {
                });
        List<ATMDetail> atmDetails = listResponseEntity.getBody();
        assertThat(atmDetails).isNotNull();
        assertThat(atmDetails).isNotEmpty();
        assertThat(atmDetails.size()).isEqualTo(19);
        assertThat(listResponseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    /**
     * Test to fetch all cities
     *
     * @throws Exception
     */
    @Test
    public void testFetchAllCities() throws Exception {
        ResponseEntity<Set<String>> response = testRestTemplate.withBasicAuth("admin", "admin")
                .exchange("/ing-atm-finder/atm/cities", HttpMethod.GET, null, new ParameterizedTypeReference<Set<String>>() {
                });
        Set<String> cities = response.getBody();
        assertThat(cities).isNotNull();
        assertThat(cities).isNotEmpty();
        assertThat(cities.size()).isEqualTo(136);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
}
