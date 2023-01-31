package tests;

import models.FailedAuthResponse;
import models.SuccessRegResponse;
import org.junit.jupiter.api.*;
import models.Auth;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.Specs.*;
import static tags.Tags.*;

public class UserRegisterTests extends BaseTest {

    @Test
    @DisplayName("Регистрация пользователя с валидными данными")
    @Tags({@Tag(POSITIVE), @Tag(REGISTER), @Tag(POST_REQUEST)})
    public void registerTest() {
        step("Регистрация пользователя с валидными данными", () -> {
            useSpecs(requestSpec(URL), responseSpec200());
            Auth authData = new Auth("eve.holt@reqres.in", "pistol");
            SuccessRegResponse regResponse = given()
                    .when()
                    .body(authData)
                    .post(REGISTER_ENDPOINT)
                    .then().log().all()
                    .extract().as(SuccessRegResponse.class);
            Assertions.assertEquals("QpwL5tke4Pnpja7X4", regResponse.getToken());
        });
    }

    @Test
    @DisplayName("Регистрация пользователя без пароля")
    @Tags({@Tag(NEGATIVE), @Tag(REGISTER), @Tag(POST_REQUEST)})
    public void registerNegativeTest() {
        step("Регистрация пользователя без пароля", () -> {
            useSpecs(requestSpec(URL), responseSpec400());
            Auth authData = new Auth("sydney@fife", null);
            FailedAuthResponse regResponse = given()
                    .when()
                    .body(authData)
                    .post(REGISTER_ENDPOINT)
                    .then().log().all()
                    .extract().as(FailedAuthResponse.class);
            Assertions.assertEquals("Missing password", regResponse.getError());
        });
    }
}
