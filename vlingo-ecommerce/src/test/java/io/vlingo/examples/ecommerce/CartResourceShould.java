package io.vlingo.examples.ecommerce;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class CartResourceShould {


    @Before
    public void setUp() throws InterruptedException {
        Bootstrap.instance();
        Thread.sleep(500);
    }

    @After
    public void cleanUp() throws InterruptedException {
        Bootstrap.instance().stop();
    }

    @Test(timeout = 1500)
    public void createEmptyCartForUser_whenNewCartCreated() throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://localhost:8081/cart");
        String json = "{\"id\": 118}";
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);

        Integer code = response.getStatusLine().getStatusCode();
        assertThat(code, is((equalTo(201))));
        String locationUrl = response.getFirstHeader("Location").getValue();
        assertThat(locationUrl, notNullValue());
        response.close();

        HttpGet httpGet = new HttpGet(String.format("http://localhost:8081%s", locationUrl));

        httpGet.setHeader("Accept", "application/json");

        response = client.execute(httpGet);
        code = response.getStatusLine().getStatusCode();
        assertThat(code, is((equalTo(201))));
    }
}