package io.vlingo.examples.ecommerce;

import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class CartResourceShould {

    private CloseableHttpClient client;

    @Before
    public void setUp() throws InterruptedException {
        Boolean startUpSuccess = Bootstrap.instance().serverStartup().await(100);
        assertThat(startUpSuccess, is(equalTo(true)));
        client = HttpClientBuilder.create().build();
    }

    @After
    public void cleanUp() throws InterruptedException {
        Bootstrap.instance().stop();
    }

    private HttpEntityEnclosingRequestBase getHttp
            (Supplier<HttpEntityEnclosingRequestBase> supplier, String entity)
            throws UnsupportedEncodingException {

        HttpEntityEnclosingRequestBase base = supplier.get();
        base.setHeader("Accept", "application/json");
        base.setHeader("Content-type", "application/json");
        base.setEntity(new StringEntity(entity));
        return base;
    }

    private HttpRequestBase getHttp
            (Supplier<HttpRequestBase> supplier) {
        HttpRequestBase base = supplier.get();
        base.setHeader("Accept", "application/json");
        base.setHeader("Content-type", "application/json");
        return base;
    }

    private String getLocation(CloseableHttpResponse response) {
        String locationUrl = response.getFirstHeader("Location").getValue();
        assertThat(locationUrl, notNullValue());
        return locationUrl;
    }

    @Test
    public void createEmptyCartForUser_whenNewCartCreated() throws IOException {

        String cartLocationPath;
        HttpEntityEnclosingRequestBase createCartPost = getHttp(
                () -> new HttpPost("http://localhost:8081/cart"),
                "{\"id\": 118}");

        try (CloseableHttpResponse response = client.execute(createCartPost)) {
            assertThat(response.getStatusLine().getStatusCode(), is((equalTo(201))));
            cartLocationPath = getLocation(response);
        }

        System.out.println(String.format("Created cart with url: %s", cartLocationPath));
        // todo: look at issue with partial path match via POST calling create()
        HttpEntityEnclosingRequestBase addItemPatch = getHttp(
                () -> new HttpPatch(String.format("http://localhost:8081%s/pid1", cartLocationPath)),
                "{operation: \"add\"}");
        try (CloseableHttpResponse response = client.execute(addItemPatch)) {
            assertThat(response.getStatusLine().getStatusCode(), is((equalTo(200))));
            String addItemJsonResponse = EntityUtils.toString(response.getEntity());
            assertThat(addItemJsonResponse, is(equalTo("[{\"productId\":{\"id\":\"pid1\"},\"quantity\":1}]")));
        }

        HttpRequestBase httpGet = getHttp(
                () -> new HttpGet(String.format("http://localhost:8081%s", cartLocationPath)));

        try (CloseableHttpResponse response = client.execute(httpGet)) {
            assertThat(response.getStatusLine().getStatusCode(), is((equalTo(200))));
            String queryResponseJson = EntityUtils.toString(response.getEntity());
            assertThat(queryResponseJson, is(equalTo("[{\"productId\":{\"id\":\"pid1\"},\"quantity\":1}]")));
        }
    }
}