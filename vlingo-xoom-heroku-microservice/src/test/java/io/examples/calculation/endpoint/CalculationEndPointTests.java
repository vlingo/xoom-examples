package io.examples.calculation.endpoint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.examples.calculation.domain.Operation;
import io.examples.infrastructure.Bootstrap;
import io.vlingo.http.Body;
import io.vlingo.http.Request;
import io.vlingo.http.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static io.vlingo.http.Method.GET;
import static io.vlingo.http.Method.POST;
import static io.vlingo.http.RequestHeader.host;
import static io.vlingo.http.Response.Status.Ok;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("Disabled until the deprecated vlingo-xoom features are replaced")
public class CalculationEndPointTests {

    private static final ObjectMapper MAPPER = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    @Test
    public void testAddition() throws Exception {
        final String payload = createCalculationPayload("addition", 22, 23);
        final TestHttpClient client = TestHttpClient.instance(HOST, PORT);

        final Request request =
                Request.has(POST)
                        .and(URI.create("/v1/calculations"))
                        .and(host(HOST))
                        .and(Body.from(payload));

        final Response response = client.requestWith(request).await();
        final JsonNode calculationJson = asJson(response);

        final List<Integer> operands =
                StreamSupport.stream(calculationJson.get("operands").spliterator(), false)
                        .map(JsonNode::intValue).collect(Collectors.toList());

        assertEquals(Ok, response.status);
        assertEquals("ADDITION", calculationJson.get("operation").asText());
        assertEquals(45, calculationJson.get("result").intValue());
        assertEquals(2, operands.size());
        assertTrue(operands.contains(22));
        assertTrue(operands.contains(23));
    }

    @Test
    public void testSubtraction() throws Exception {
        final String payload = createCalculationPayload("subtraction", 1017, 980);
        final TestHttpClient client = TestHttpClient.instance(HOST, PORT);

        final Request request =
                Request.has(POST)
                        .and(URI.create("/v1/calculations"))
                        .and(host(HOST))
                        .and(Body.from(payload));

        final Response response = client.requestWith(request).await();
        final JsonNode calculationJson = asJson(response);

        final List<Integer> operands =
                StreamSupport.stream(calculationJson.get("operands").spliterator(), false)
                        .map(JsonNode::intValue).collect(Collectors.toList());

        assertEquals(Ok, response.status);
        assertEquals("SUBTRACTION", calculationJson.get("operation").asText());
        assertEquals(37, calculationJson.get("result").intValue());
        assertEquals(2, operands.size());
        assertTrue(operands.contains(1017));
        assertTrue(operands.contains(980));
    }

    @Test
    public void testMultiplication() throws Exception {
        final String payload = createCalculationPayload("multiplication", 4, 55);
        final TestHttpClient client = TestHttpClient.instance(HOST, PORT);

        final Request request =
                Request.has(POST)
                        .and(URI.create("/v1/calculations"))
                        .and(host(HOST))
                        .and(Body.from(payload));

        final Response response = client.requestWith(request).await();

        final JsonNode calculationJson =
                asJson(response);

        final List<Integer> operands =
                StreamSupport.stream(calculationJson.get("operands").spliterator(), false)
                        .map(JsonNode::intValue).collect(Collectors.toList());

        assertEquals(Ok, response.status);
        assertEquals("MULTIPLICATION", calculationJson.get("operation").asText());
        assertEquals(220, calculationJson.get("result").intValue());
        assertEquals(2, operands.size());
        assertTrue(operands.contains(4));
        assertTrue(operands.contains(55));
    }

    @Test
    public void testOperationsRetrieval() throws Exception {
        final TestHttpClient client = TestHttpClient.instance(HOST, PORT);

        final Request request =
                Request.has(GET)
                        .and(URI.create("/v1/calculations/operations"))
                        .and(host(HOST));

        final Response response = client.requestWith(request).await();

        final List<Operation> operations =
                deserializeOperations(response);

        assertEquals(Ok, response.status);
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

    private JsonNode asJson(final Response response) throws IOException {
        return MAPPER.readTree(response.entity.content());
    }

    private static List<Operation> deserializeOperations(final Response response) throws IOException {
        final Spliterator<JsonNode> nodes = MAPPER.readTree(response.entity.content()).spliterator();
        return StreamSupport.stream(nodes, false)
                .map(node -> Operation.valueOf(node.asText()))
                .collect(Collectors.toList());
    }

    @BeforeEach
    public void setUp() {
        Bootstrap.boot();
    }

    @AfterEach
    public void tearDown() throws Exception {
        Bootstrap.instance().stopAndCleanup();
        TestHttpClient.instance(HOST, PORT).close();
    }

}
