package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import static helpers.report.CustomAllureListener.*;

public class BaseTest {

    protected String URL = "https://reqres.in/",
            USERS_ENDPOINT = "api/users/",
            REGISTER_ENDPOINT = "/api/register/",
            LOGIN_ENDPOINT = "/api/login";

    @BeforeAll
    public static void beforeAll() {
        RestAssured.filters(withCustomTemplate());
    }
}
