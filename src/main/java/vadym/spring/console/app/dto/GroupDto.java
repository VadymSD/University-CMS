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
public class GroupDto {
    private Long id;

    private String name;

    private Set<Long> coursesIds;

    private Set<Long> studentsIds;
}
