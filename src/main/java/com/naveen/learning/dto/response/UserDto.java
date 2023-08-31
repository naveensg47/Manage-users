package com.naveen.learning.dto.response;

import com.naveen.learning.model.User;
import com.naveen.learning.validation.annotation.NullOrNotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "Username can not be null or blank")
    @Size(max = 100, message = "Username can not have more than 100 characters")
    private String username;

    @NotBlank(message = "Email can not be Null or Blank")
    @Size(max = 100,message = "Email length should not exceed 100 characters")
    private String email;

    @NullOrNotBlank(message = "Password can not be Null or Blank")
    @Size(min = 8, max = 50,message = "Password should have min length of 10 and max of 50")
    private String password;

    private boolean isActive;

    private Set<String> roles;

    private List<Long> roleIds;

    private Timestamp lastLogin;

    private String mobileNumber;

    private String status;

    public UserDto mapUserDto(User user){
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setEmail(user.getEmail());
        this.setMobileNumber(user.getMobileNumber());
        this.setRoles(user.getRoles().stream().map(role -> role.getRoleName().name()).collect(Collectors.toSet()));
        this.setLastLogin(user.getLastLogin());
        this.setActive(user.isActive());
        return this;
    }

}
