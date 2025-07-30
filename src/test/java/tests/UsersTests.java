package tests;

import io.qameta.allure.*;
import models.UserBodyModel;
import models.UserResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specs.requestSpec;
import static specs.Specs.responseSpec;

@Epic("Reqres API")
@Feature("Управление пользователями")
@Owner("Алина")
@Tag("api")
public class UsersTests extends TestBase {

    @Test
    @Story("Получение данных")
    @DisplayName("GET - получение конкретного пользователя")
    @Severity(SeverityLevel.CRITICAL)
    void getSingleUserTest() {
        UserResponseModel response = step("Запрос существующего пользователя", () ->
                given(requestSpec)
                        .when()
                        .get("/users/2")
                        .then()
                        .spec(responseSpec(200))
                        .extract().as(UserResponseModel.class)
        );

        step("Проверка ответа", () -> {
            assertEquals("Janet", response.getData().getFirst_name());
            assertEquals("Weaver", response.getData().getLast_name());
        });

        step("Запрос несуществующего пользователя", () -> {
            given(requestSpec)
                    .when()
                    .get("/users/23")
                    .then()
                    .spec(responseSpec(404));
        });
    }

    @Test
    @Story("Создание и удаление")
    @DisplayName("POST/DELETE - полный цикл работы с пользователем")
    @Severity(SeverityLevel.CRITICAL)
    void deleteUserTest() {

        UserBodyModel requestBody = new UserBodyModel();
        requestBody.setName("morpheus");
        requestBody.setJob("leader");

        UserResponseModel response = step("Создание тестового пользователя", () ->
                given(requestSpec)
                        .body(requestBody)
                        .when()
                        .post("/users")
                        .then()
                        .statusCode(201)
                        .extract().as(UserResponseModel.class));

        step("Проверить ответ на создание нового пользователя", () -> {
            assertEquals("morpheus", response.getName());
            assertEquals("leader", response.getJob());
        });

        step("Удаление пользователя", () -> {
            given(requestSpec)
                    .when()
                    .delete("/users/" + response.getId())
                    .then()
                    .statusCode(204);
        });

        step("Проверка отсутствия пользователя", () -> {
            given(requestSpec)
                    .when()
                    .get("/users/" + response.getId())
                    .then()
                    .statusCode(404);
        });
    }
}

