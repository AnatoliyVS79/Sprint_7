import client.CourierClientSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.Courier;
import model.CourierCreds;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierAuthorizationTest {
   private CourierClientSteps courierClient;
   private List<String> ids;
   private Courier courier;
   private String login;
   private String password;
   private String firstName;


    @Before
    public void setUp() {
        courierClient = new CourierClientSteps();
        ids = new ArrayList<>();

        courier = new Courier("Ioan", "ioan7", "Ioan");
        courierClient.registerNewCourier(courier)
                .assertThat()
                .statusCode(201);
        login = courier.getLogin();
        password = courier.getPassword();
        firstName = courier.getFirstName();

        CourierCreds courierCredentials = CourierCreds.from(courier);
        String courierId = courierClient.loginCourier(courierCredentials)
                .assertThat().statusCode(200)
                .extract()
                .body()
                .path("id").toString();

        ids.add(courierId);
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
    public void positiveAuthorizationTest() {
        CourierCreds courierCredentials = CourierCreds.from(courier);
        courierClient.loginCourier(courierCredentials)
                .assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Авторизация с незаполненным полем логин")
    @Description("Проверяем, что возвращается код 400 и уведомление об ошибке")
    public void authorizationCourierWithoutLoginTest() {
        CourierCreds courierCredentials = new CourierCreds("", password);
        courierClient.loginCourier(courierCredentials)
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }


    @Test
    @DisplayName("Авторизация с незаполненным полем пароль")
    @Description("Проверяем, что возвращается статус код 400 и уведомление об ошибке")
    public void authorizationCourierWithoutPasswordTest() {
        CourierCreds courierCredentials = new CourierCreds(login, "");
        courierClient.loginCourier(courierCredentials)
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    @Description("Проверяем, что возвращается статус код 404 и уведомление об ошибке")
    public void authorizationCourierWithInvalidLoginTest() {
        String invalidLogin = "Grozniy";
        CourierCreds courierCredentials = new CourierCreds(invalidLogin, password);
        courierClient.loginCourier(courierCredentials)
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверяем, что возвращается статус код 404 и уведомление об ошибке")
    public void authorizationCourierWithInvalidPasswordTest() {
        String invalidPassword = "qwerty";
        CourierCreds courierCredentials = new CourierCreds(login, invalidPassword);
        courierClient.loginCourier(courierCredentials)
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Успешный запрос возвращает id")
    @Description("Проверяем, что после авторизации возвращается Id")
    public void successfulQueryReturnsIdTest() {
        CourierCreds courierCredentials = CourierCreds.from(courier);
        String idReg = courierClient.loginCourier(courierCredentials)
                .assertThat().body("id", notNullValue())
                .and()
                .statusCode(200)
                .extract().response().jsonPath().getString("id");
        System.out.printf(String.valueOf(idReg));
    }

}

