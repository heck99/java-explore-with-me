package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Integer id;

    @NotBlank
    @Size(max = 30)
    private String name;

    @Email
    private String email;

}
