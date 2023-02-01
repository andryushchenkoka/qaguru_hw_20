package tests.groovy;

import org.junit.jupiter.api.*;
import tests.BaseTest;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static specs.Specs.*;
import static helpers.tags.Tags.*;

public class GroovyTests extends BaseTest {

    @Test
    @DisplayName("Количество элементов на странице взаимоизменяется (c groovy)")
    @Tags({@Tag(POSITIVE), @Tag(GET_REQUEST)})
    public void getUsersListGroovyTest() {
        step("Получить список пользователей на 2 странице", () -> {
            useSpecs(requestSpec(URL), responseSpec200());
            given()
                    .when()
                    .param("page", 2)
                    .param("per_page", 8)
                    .get(USERS_ENDPOINT)
                    .then().log().all()
                    .body("data.findAll{it.id}.size()", is(4))
                    .extract().response();
        });
    }

    @Test
    @DisplayName("Email содержит имя пользователя (c groovy)")
    @Tags({@Tag(POSITIVE), @Tag(GET_REQUEST)})
    public void getUserGroovyTest() {
        step("Email содержит имя пользователя", () -> {
            useSpecs(requestSpec(URL), responseSpec200());
            given()
                    .param("id", 3)
                    .get(USERS_ENDPOINT)
                    .then().log().all()
                    .body("data.email", is("emma.wong@reqres.in"))
                    .extract().response();
        });
    }
}
