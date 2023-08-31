package com.naveen.learning.dto.request;

import com.naveen.learning.validation.annotation.NullOrNotBlank;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Registration request", description = "The registration request payload")
public class RegistrationRequest {

    @NotBlank(message = "")
    private String fullName;

    @NotBlank
    private String email;

    @NullOrNotBlank(message = "Designation can be null but not blank")
    private String designation;

    @NotNull(message = "Registration password must not be null")
    private String password;

    @NullOrNotBlank(message = "Mobile can be null")
    private String mobileNumber;
}
