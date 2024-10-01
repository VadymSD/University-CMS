package vadym.spring.console.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UniversityDto {
    private Long id;

    private String name;

    private String location;
}
