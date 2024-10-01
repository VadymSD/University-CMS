package vadym.spring.console.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private Long id;

    private String firstName;

    private String surname;

    private Long groupId;

    private Long userId;
}
