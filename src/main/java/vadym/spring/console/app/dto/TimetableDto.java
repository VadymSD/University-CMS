package vadym.spring.console.app.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TimetableDto {
    private Long id;

    private String name;

    private String type;

    private LocalDate startDate;
}
