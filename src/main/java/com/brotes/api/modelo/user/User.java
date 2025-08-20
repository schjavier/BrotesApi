package com.brotes.api.modelo.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario no debe estar vacío")
    @Size(min = 3, max = 16, message = "El nombre de usuario debe tener entre 3 y 16 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$",
            message = "El nombre de usuario solo puede tener letras, números, y guiones bajos")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
    private String password;

    @NotBlank(message = "El mail no puede estar vacío")
    @Email
    private String email;

    public User(UserRegistrationData userRegistrationData){
        this.username = userRegistrationData.username();
        this.password = userRegistrationData.password();
        this.email = userRegistrationData.email();

    }

}
