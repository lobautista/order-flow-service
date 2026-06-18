package com.bautista.order_flow_service.order;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class OrderApiIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private String orderJson() {
        return """
                {
                  "customerId": "%s",
                  "currency": "USD",
                  "orderItemRequests": [
                    { "productId": "%s", "quantity": 2, "unitPrice": 25.00 }
                  ]
                }
                """.formatted(UUID.randomUUID(), UUID.randomUUID());
    }

    @Test
    void createsOrder_persistsPendingHistory_isIdempotent_andIsReadable() throws Exception {
        UUID idempotencyKey = UUID.randomUUID();
        String body = orderJson();

        // 1. First POST creates the order -> 201 Created
        MvcResult result = mockMvc.perform(post("/orders")
                        .header("Idempotency-Key", idempotencyKey.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        String orderId = JsonPath.read(json, "$.orderId");
        double totalAmount = ((Number) JsonPath.read(json, "$.totalAmount")).doubleValue();
        assertThat(totalAmount).isEqualTo(50.00); // 2 x 25.00

        // 2. The creation event is recorded as the first PENDING history row
        Integer pendingHistoryRows = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM order_status_history WHERE order_id = ?::uuid AND new_status = 'PENDING'",
                Integer.class, orderId);
        assertThat(pendingHistoryRows).isEqualTo(1);

        Integer orderCount = jdbcTemplate.queryForObject("SELECT count(*) FROM orders", Integer.class);
        assertThat(orderCount).isEqualTo(1);

        // 3. Replaying the same Idempotency-Key returns the same order, with NO duplicate row
        mockMvc.perform(post("/orders")
                        .header("Idempotency-Key", idempotencyKey.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(orderId));

        Integer orderCountAfterReplay = jdbcTemplate.queryForObject("SELECT count(*) FROM orders", Integer.class);
        assertThat(orderCountAfterReplay).isEqualTo(1);

        // 4. GET /orders/{id} returns the persisted order -> 200 OK
        mockMvc.perform(get("/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.currency").value("USD"));

        // 5. GET with an unknown id -> 404 Not Found
        mockMvc.perform(get("/orders/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}
