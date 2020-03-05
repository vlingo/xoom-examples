package io.examples.calculation.endpoint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.examples.CalculationApplication;
import io.examples.calculation.domain.Operation;
import io.micronaut.test.annotation.MicronautTest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static io.micronaut.http.HttpStatus.OK;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(application = CalculationApplication.class)
public class CalculationEndPointTests {

    private static final String API_VERSION = "v1";
    private static final String HOST = "http://localhost:8080/";
    private static final ObjectMapper MAPPER = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    public void testAddition() throws IOException {
        final String payload = createCalculationPayload("addition", 22, 23);
        final HttpPost request = new HttpPost(HOST + API_VERSION + "/calculations");
        request.setEntity(new StringEntity(payload, APPLICATION_JSON));

        final HttpResponse response =
                HttpClientBuilder.create().build().execute(request);

        final JsonNode calculationJson =
                retrieveCalculationJson(response);

        final List<Integer> operands =
                StreamSupport.stream(calculationJson.get("operands").spliterator(), false)
                        .map(JsonNode::intValue).collect(Collectors.toList());

        assertEquals(OK.getCode(), response.getStatusLine().getStatusCode());
        assertEquals("ADDITION", calculationJson.get("operation").asText());
        assertEquals(45, calculationJson.get("result").intValue());
        assertEquals(2, operands.size());
        assertTrue(operands.contains(22));
        assertTrue(operands.contains(23));
    }

    @Test
    public void testSubtraction() throws IOException {
        final String payload = createCalculationPayload("subtraction", 1017, 980);
        final HttpPost request = new HttpPost(HOST + API_VERSION + "/calculations");
        request.setEntity(new StringEntity(payload, APPLICATION_JSON));

        final HttpResponse response =
                HttpClientBuilder.create().build().execute(request);

        final JsonNode calculationJson =
                retrieveCalculationJson(response);

        final List<Integer> operands =
                StreamSupport.stream(calculationJson.get("operands").spliterator(), false)
                        .map(JsonNode::intValue).collect(Collectors.toList());

        assertEquals(OK.getCode(), response.getStatusLine().getStatusCode());
        assertEquals("SUBTRACTION", calculationJson.get("operation").asText());
        assertEquals(37, calculationJson.get("result").intValue());
        assertEquals(2, operands.size());
        assertTrue(operands.contains(1017));
        assertTrue(operands.contains(980));
    }

    @Test
    public void testMultiplication() throws IOException {
        final String payload = createCalculationPayload("multiplication", 4, 55);
        final HttpPost request = new HttpPost(HOST + API_VERSION + "/calculations");
        request.setEntity(new StringEntity(payload, APPLICATION_JSON));

        final HttpResponse response =
                HttpClientBuilder.create().build().execute(request);

        final JsonNode calculationJson =
                retrieveCalculationJson(response);

        final List<Integer> operands =
                StreamSupport.stream(calculationJson.get("operands").spliterator(), false)
                        .map(JsonNode::intValue).collect(Collectors.toList());

        assertEquals(OK.getCode(), response.getStatusLine().getStatusCode());
        assertEquals("MULTIPLICATION", calculationJson.get("operation").asText());
        assertEquals(220, calculationJson.get("result").intValue());
        assertEquals(2, operands.size());
        assertTrue(operands.contains(4));
        assertTrue(operands.contains(55));
    }

    @Test
    public void testOperationsRetrieval() throws IOException {
        final HttpGet request =
                new HttpGet(HOST + API_VERSION + "/calculations/operations");

        final HttpResponse response =
                HttpClientBuilder.create().build().execute(request);

        final List<Operation> operations =
                retrieveOperationsFromJson(response);

        assertEquals(OK.getCode(), response.getStatusLine().getStatusCode());
        assertTrue(operations.containsAll(Arrays.asList(Operation.values())));
    }

    private static String createCalculationPayload(final String operationName,
                                                   final Integer firstOperand,
                                                   final Integer secondOperand) {
        final ObjectNode node = MAPPER.createObjectNode();
        node.put("operationName", operationName);
        node.put("firstOperand", firstOperand);
        node.put("secondOperand", secondOperand);
        return node.toString();
    }

    private static JsonNode retrieveCalculationJson(final HttpResponse response) throws IOException {
        return MAPPER.readTree(EntityUtils.toString(response.getEntity()));
    }

    private static List<Operation> retrieveOperationsFromJson(final HttpResponse response) throws IOException {
        final Spliterator<JsonNode> nodes = MAPPER.readTree(EntityUtils.toString(response.getEntity())).spliterator();
        return StreamSupport.stream(nodes, false)
                .map(node -> Operation.valueOf(node.asText()))
                .collect(Collectors.toList());
    }

}
