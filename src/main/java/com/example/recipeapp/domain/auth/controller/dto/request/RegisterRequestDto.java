package com.example.recipeapp.domain.auth.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterRequest {

    @NotNull
    @Size(min = 2, max = 50)
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;


}
