package vadym.spring.console.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDto {
    private Long id;

    private String roleName;
}
