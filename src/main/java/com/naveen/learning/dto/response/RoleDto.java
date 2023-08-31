package com.naveen.learning.dto.response;

import com.naveen.learning.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private Long id;
    private String roleCode;
    private String roleName;

    public RoleDto mapRoleDto(Role role){
        this.setId(role.getId());
        this.setRoleCode(role.getRoleName().name());
        this.setRoleName(role.getRoleName().name());
        return this;
    }
}
