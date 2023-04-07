import client.CourierClientSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.CourierCreds;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class CourierCreationTest {

    List<String> ids;
    private CourierClientSteps courierClient;
    private Courier courier;


    @Before
    public void setUp() {
        courierClient = new CourierClientSteps();
        ids = new ArrayList<>();
        courier = new Courier("Ioan", "ioan7", "Ioan");

    }

    @After
    public void tearDown() {
        for (String id : ids) {
            courierClient.deleteCourier(id);
        }
    }


    @Test
    @DisplayName("Авторизация курьера")
    @Description("Проверяем, что после авторизации возвращается код 200")
    public void createCourierTest() {
        ValidatableResponse response = courierClient.registerNewCourier(courier);
        response.assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);

        CourierCreds courierCredentials = CourierCreds.from(courier);
        String courierId = courierClient.loginCourier(courierCredentials)
                .assertThat()
                .extract()
                .body()
                .path("id").toString();
        ids.add(courierId);
    }

    @Test
    @DisplayName("Регистрация двух одинаковых курьеров")
    @Description("Проверяем, что при регистрации двух одинаковых курьеров возвращается статус код 409" +
            " и уведомление о невозможнности такой регистрации")
    public void cannotCreateTwoIdenticalСouriersTest() {

        ValidatableResponse response = courierClient.registerNewCourier(courier);
        response.assertThat()
                .statusCode(201);
        ValidatableResponse response2 = courierClient.registerNewCourier(courier);
        String message2 = response2.extract().path("message");
        assertEquals("Статус код не верный", 409, response2.extract().statusCode());
        assertEquals(message2, "Этот логин уже используется. Попробуйте другой.");

        CourierCreds courierCredentials = CourierCreds.from(courier);
        String courierId = courierClient.loginCourier(courierCredentials)
                .assertThat()
                .extract()
                .body()
                .path("id").toString();
        ids.add(courierId);
    }

    @Test
    @DisplayName("чтобы создать курьера, нужно передать в ручку все обязательные поля")
    public void createACourierFillTheRequiredFieldsTest() {
        Courier courier = new Courier("Ioan", "ioan7", null);
        ValidatableResponse response = courierClient.registerNewCourier(courier);
        assertEquals("Статус код не верный", 201, response.extract().statusCode());
        CourierCreds courierCredentials = CourierCreds.from(courier);
        String courierId = courierClient.loginCourier(courierCredentials)
                .assertThat()
                .extract()
                .body()
                .path("id").toString();
        ids.add(courierId);
    }

    @Test
    @DisplayName("Регистрация с незаполненным полем пароль")
    @Description("Проверяем, что при регистрации с незаполненным полем парооль возвращается статус код 400 " +
            "и уведомление об ошибке")
    public void testRegisterCourierWithoutPasswordReturn400BadRequest() {
        Courier courier = new Courier("Ioan", null, "Ioan");
        ValidatableResponse response = courierClient.registerNewCourier(courier);
        assertEquals("Статус код не верный", 400, response.extract().statusCode());
        response.body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Регистрация с незаполненным полем логин")
    @Description("Проверяем, что при регистрации с незаполненным полем логин возвращается статус код 400 " +
            "и уведомление об ошибке")
    public void testRegisterCourierWithoutLoginReturn400BadRequest() {
        Courier courier = new Courier(null, "ioan7", "Ioan");
        ValidatableResponse response = courierClient.registerNewCourier(courier);
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Регистрация с повторяющемся логином")
    @Description("Проверяем, что при создании пользователя с логином, который уже есть, возвращается ошибка")
    public void registrationWithRepeatedLoginTest() {
        ValidatableResponse response = courierClient.registerNewCourier(courier);
        response.assertThat().statusCode(201);

        Courier courier2 = new Courier("Ioan", "123", "Ioan");
        ValidatableResponse response2 = courierClient.registerNewCourier(courier2);
        String message2 = response2.extract().path("message");
        assertEquals("Статус код не верный", 409, response2.extract().statusCode());
        assertEquals(message2, "Этот логин уже используется. Попробуйте другой.");

        CourierCreds courierCredentials = CourierCreds.from(courier);
        String courierId = courierClient.loginCourier(courierCredentials)
                .assertThat()
                .extract()
                .body()
                .path("id").toString();
        ids.add(courierId);


    }


}


