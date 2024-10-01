package vadym.spring.console.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto {
    private Long id;

    private String firstName;

    private String surname;

    private Long userId;

    private Long facultyId;

    private Set<Long> coursesIds;
}
