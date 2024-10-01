package vadym.spring.console.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="timetables")
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="timetable_id")
    private Long id;

    @Column(name = "timetable_name")
    private String timetableName;

    @Column(name="type")
    private String type;

    @Column(name="start_date")
    private LocalDate startDate;

    @OneToMany(mappedBy = "timetable", cascade = CascadeType.ALL)
    private List<Lecture> lectureList;
}