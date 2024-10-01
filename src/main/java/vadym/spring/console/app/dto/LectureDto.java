package vadym.spring.console.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureDto {
    private Long id;

    private String name;

    private Set<Long> groupIds;

    private Long courseId;

    private Long teacherId;

    private Long timetableId;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private int room;
}
