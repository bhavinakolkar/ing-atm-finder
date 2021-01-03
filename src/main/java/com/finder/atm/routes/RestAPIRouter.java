package com.finder.atm.routes;

import com.finder.atm.components.ATMFinder;
import com.finder.atm.model.ATMDetail;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;


@Component
public class RestAPIRouter extends RouteBuilder {

    @Value("${server.port}")
    private Integer port;

    @Value("${ing.atm.locator.url}")
    private String ingATMLocatorURL;

    @Override
    public void configure() throws Exception {
        // Configure REST and set contextPath
        restConfiguration()
                .component("servlet")
                .contextPath("/ing-atm-finder")
                .port(port)
                .bindingMode(RestBindingMode.json);

        /* Set Timer to call external web service to fetch all ATM on startup of the application
            Take care of invalid json and unmarshal it to List<ATMDetail>
        */
        from("timer:Timer?repeatCount=1")
                .setBody(simple("${null}"))
                .to(ingATMLocatorURL + "?httpMethod=GET")
                .process((exchange) -> {
                    String body = exchange.getIn().getBody(String.class);
                    if(!body.startsWith("[")) {
                        body = body.substring(body.indexOf("["));
                    }
                    exchange.getIn().setBody(body);
                }).unmarshal(new ListJacksonDataFormat(ATMDetail.class))
                .bean(ATMFinder.class, "processATMDetails");

        // REST route configuration
        rest("/atm").description("ATM REST")
                .get("/").description("Fetch list of all the atm across cities")
                    .produces(MediaType.APPLICATION_JSON_VALUE)
                    .route().routeId("list-atm-api")
                    .bean(ATMFinder.class, "findAll")
                    .endRest()
                .get("/{city}").description("Fetch list of all the atm for given city")
                    .produces(MediaType.APPLICATION_JSON_VALUE)
                    .route().routeId("list-atm-by-city-api")
                    .bean(ATMFinder.class, "findByCity(${header.city})")
                    .endRest()
                .get("/cities").description("Fetch list of all the atm cities of ING ATM")
                    .produces(MediaType.APPLICATION_JSON_VALUE)
                    .route().routeId("list-city-api")
                    .bean(ATMFinder.class, "findAllCities()")
                    .end();
    }
}
