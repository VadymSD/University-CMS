package vadym.spring.console.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;

    private String name;

    private String description;

    private Set<Long> groupsIds;

    private Set<Long> teachersIds;
}
