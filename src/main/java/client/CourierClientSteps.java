package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.CourierCreds;

import static io.restassured.RestAssured.given;

public class CourierClientSteps extends RestAssuredClient {
    private static final String PATH_COURIER = "/api/v1/courier/";

    @Step("Отправить запрос POST на /api/v1/courier")
    public ValidatableResponse registerNewCourier(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier.toJsonString())
                .when()
                .post(PATH_COURIER)
                .then();
    }

    @Step("Залогиниться и если успех возвращает ответ тело")
    public ValidatableResponse loginCourier(CourierCreds courierCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(courierCredentials.toJsonString())
                .when()
                .post(PATH_COURIER + "login")
                .then();
    }

    @Step("Отправить запрос удалить на /api/v1/courier")
    public ValidatableResponse deleteCourier(String idCourier) {

        return given()
                .spec(getBaseSpec())
                .when()
                .delete(PATH_COURIER + idCourier)
                .then();
    }

}

