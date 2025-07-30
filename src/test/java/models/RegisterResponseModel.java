package models;

import lombok.Data;

@Data
public class RegisterResponseModel {
    String token, id, email, password, createdAt;
}
