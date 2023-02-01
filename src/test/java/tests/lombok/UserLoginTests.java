package tests.lombok;

import models.FailedAuthResponse;
import models.SuccessAuthResponse;
import org.junit.jupiter.api.*;
import models.Auth;
import tests.BaseTest;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.Specs.*;

public class UserLoginTests extends BaseTest {

    @Test
    @DisplayName("Логин пользователя с валидными данными")
    @Tags({@Tag(POSITIVE), @Tag(LOGIN), @Tag(POST_REQUEST)})
    public void loginTest() {
        step("Логин пользователя с валидными данными", () -> {
            useSpecs(requestSpec(URL), responseSpec200());
            Auth authData = new Auth("eve.holt@reqres.in", "cityslicka");
            SuccessAuthResponse authResponse = given()
                    .when()
                    .body(authData)
                    .post(LOGIN_ENDPOINT)
                    .then().log().all()
                    .extract().as(SuccessAuthResponse.class);
            Assertions.assertNotNull(authResponse.getToken());
        });
    }

    @Test
    @DisplayName("Логин пользователя без пароля")
    @Tags({@Tag(NEGATIVE), @Tag(LOGIN), @Tag(POST_REQUEST)})
    public void loginNegativeTest() {
        step("Логин пользователя без пароля", () -> {
            useSpecs(requestSpec(URL), responseSpec400());
            Auth authData = new Auth("peter@klaven", null);
            FailedAuthResponse authResponse = given()
                    .when()
                    .body(authData)
                    .post(LOGIN_ENDPOINT)
                    .then().log().all()
                    .extract().as(FailedAuthResponse.class);
            Assertions.assertEquals("Missing password", authResponse.getError());
        });
    }
}
