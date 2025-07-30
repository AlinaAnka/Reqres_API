package tests;

import io.qameta.allure.*;
import models.RegisterBodyModel;
import models.RegisterErrorModel;
import models.RegisterResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

import static org.junit.jupiter.api.Assertions.*;
import static specs.Specs.requestSpec;
import static specs.Specs.responseSpec;

@Epic("Reqres API")
@Feature("Регистрация")
@Owner("Алина")
@Tag("api")
public class RegisterTests extends TestBase {

    @Test
    @Story("Регистрация пользователя")
    @DisplayName("Успешная регистрация")
    @Severity(SeverityLevel.BLOCKER)
    void successfulRegisterTest() {
        RegisterBodyModel registerData = new RegisterBodyModel(config.login(), config.password());

        RegisterResponseModel response = step("Выполнение регистрации", () ->
                given(requestSpec)
                        .body(registerData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseSpec(200))
                        .extract().as(RegisterResponseModel.class));

        step("Проверка ответа", () -> {
            assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
            assertEquals("4", response.getId());
        });
    }

    @Test
    @Story("Регистрация пользователя")
    @DisplayName("Неудачная регистрация")
    @Severity(SeverityLevel.NORMAL)
    void unsuccessfulRegisterTest() {
        RegisterBodyModel registerData = new RegisterBodyModel("", "");

        RegisterErrorModel response = step("Попытка регистрации без пароля и email", () ->
                given(requestSpec)
                        .body(registerData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseSpec(400))
                        .extract().as(RegisterErrorModel.class));

        step("Проверка ответа", () -> {
            assertEquals("Missing email or username", response.getError());
        });
    }

    @Test
    @Story("Регистрация пользователя")
    @DisplayName("Неудачная регистрация без пароля")
    @Severity(SeverityLevel.NORMAL)
    void missingPasswordRegisterTest() {
        RegisterBodyModel registerData = new RegisterBodyModel(config.login(), "");

        RegisterErrorModel response = step("Попытка регистрации без пароля", () ->
                given(requestSpec)
                        .body(registerData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseSpec(400))
                        .extract().as(RegisterErrorModel.class));

        step("Проверка ответа", () -> {
            assertEquals("Missing password", response.getError());
        });
    }

    @Test
    @Story("Регистрация пользователя")
    @DisplayName("Неудачная регистрация без email")
    @Severity(SeverityLevel.NORMAL)
    void missingEmailRegisterTest() {
        RegisterBodyModel registerData = new RegisterBodyModel("", config.password());

        RegisterErrorModel response = step("Попытка регистрации без email", () ->
                given(requestSpec)
                        .body(registerData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseSpec(400))
                        .extract().as(RegisterErrorModel.class));

        step("Проверка ответа", () -> {
            assertEquals("Missing email or username", response.getError());
        });
    }
}
