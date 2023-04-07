package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.CancelOrderModel;
import model.CreateOrderModel;

import static io.restassured.RestAssured.given;

public class ScooterOrderSteps extends RestAssuredClient {
    private static final String PATH_ORDER = "/api/v1/orders";

    @Step("Send POST request to /api/v1/orders")
    public ValidatableResponse createNewOrder(CreateOrderModel createOrderScooter) {
        return given()
                .log().all()
                .spec(getBaseSpec())
                .body(createOrderScooter.toJsonString())
                .when()
                .post(PATH_ORDER)
                .then();

    }

    @Step("Send DELETE request to /api/v1/orders/cancel")
    public ValidatableResponse cancelOrder(CancelOrderModel cancelOrderRequest) {
        return given()
                .spec(getBaseSpec())
                .body(cancelOrderRequest.toJsonString())
                .when()
                .put(PATH_ORDER + "/cancel")
                .then();
    }

    @Step("Send GET request to /api/v1/orders")
    public ValidatableResponse getOrdersList() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(PATH_ORDER)
                .then();
    }
}

