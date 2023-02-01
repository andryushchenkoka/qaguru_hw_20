package tests.lombok;

import io.restassured.response.Response;
import models.SuccessUserCreate;
import models.SuccessUserUpdate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import models.User;
import models.UserWorker;
import tests.BaseTest;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.Specs.*;

public class UserTests extends BaseTest {

    @ValueSource(ints = {
            1, 2
    })
    @ParameterizedTest
    @DisplayName("Получить список пользователей")
    @Tags({@Tag(POSITIVE), @Tag(GET_REQUEST)})
    public void getUsersListTest(int page) {
        step("Получить список пользователей", () -> {
            useSpecs(requestSpec(URL), responseSpec200());
            List<User> users = given()
                    .when()
                    .param("page", page)
                    .get(USERS_ENDPOINT)
                    .then().log().all()
                    .extract().body().jsonPath().getList("data", User.class);
            Assertions.assertEquals(6, users.size());
        });
    }

    @CsvSource(value = {
            "1, George",
            "6, Tracey",
            "7, Michael",
            "12, Rachel"
    })
    @ParameterizedTest
    @DisplayName("Email содержит имя пользователя")
    @Tags({@Tag(POSITIVE), @Tag(GET_REQUEST)})
    public void getUserTest(int userId, String name) {
        step("Email содержит имя пользователя", () -> {
            useSpecs(requestSpec(URL), responseSpec200());
            User user = given()
                    .param("id", userId)
                    .get(USERS_ENDPOINT)
                    .then().log().all()
                    .extract().jsonPath().getObject("data", User.class);
            Assertions.assertEquals(name, user.getFirstName().toString());
            Assertions.assertTrue(user.getEmail().toString().toLowerCase().contains(name.toLowerCase()));
        });
    }

    @Test
    @DisplayName("Поиск пользователя с несуществующим id")
    @Tags({@Tag(NEGATIVE), @Tag(GET_REQUEST)})
    public void getUserNegativeTest() {
        step("Поиск пользователя с несуществующим id", () -> {
            useSpecs(requestSpec(URL), responseSpec404());
            Response response = given()
                    .param("id", 23)
                    .get(USERS_ENDPOINT)
                    .then().log().all()
                    .extract().response();
            Assertions.assertEquals("{}", response.path("").toString());
        });
    }

    @Test
    @DisplayName("Создание нового пользователя")
    @Tags({@Tag(POSITIVE), @Tag(POST_REQUEST)})
    public void createUserTest() {
        step("Создание нового пользователя", () -> {
            useSpecs(requestSpec(URL), responseSpec201());
            UserWorker userForCreate = new UserWorker("Steve Jobs", "Programmer");
            SuccessUserCreate createdUser = given()
                    .when()
                    .body(userForCreate)
                    .post(USERS_ENDPOINT)
                    .then().log().all()
                    .extract().as(SuccessUserCreate.class);
            Assertions.assertEquals(userForCreate.getName(), createdUser.getName());
            Assertions.assertEquals(userForCreate.getJob(), createdUser.getJob());
        });
    }

    @Test
    @DisplayName("Обновить данные пользователя")
    @Tags({@Tag(POSITIVE), @Tag(PUT_REQUEST)})
    public void updateUserTest() {
        step("Обновить данные пользователя", () -> {
            useSpecs(requestSpec(URL), responseSpec200());
            UserWorker userForUpdate = new UserWorker("Linus Torvalds", "God");
            SuccessUserUpdate updatedUser = given()
                    .when()
                    .body(userForUpdate)
                    .put(USERS_ENDPOINT)
                    .then().log().all()
                    .extract().as(SuccessUserUpdate.class);
            Assertions.assertEquals(userForUpdate.getName(), updatedUser.getName());
            Assertions.assertEquals(userForUpdate.getJob(), updatedUser.getJob());
        });
    }

    @Test
    @DisplayName("Удалить пользователя")
    @Tags({@Tag(POSITIVE), @Tag(DELETE_REQUEST)})
    public void deleteUserTest() {
        step("Удалить пользователя", () -> {
            useSpecs(requestSpec(URL), responseSpec204());
            given()
                    .when()
                    .param("id", 3)
                    .delete(USERS_ENDPOINT)
                    .then().log().all();
        });
    }
}
