package vadym.spring.console.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FacultyDto {
    private Long id;

    private String name;

    private String description;
}
