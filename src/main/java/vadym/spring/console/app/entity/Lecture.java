package vadym.spring.console.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="lectures")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long id;

    @Column(name = "lecture_name")
    private String lectureName;

    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;

    @ManyToMany(mappedBy = "lectures")
    private Set<Group> groups;

    @ManyToOne
    @JoinColumn(name="timetable_id")
    private Timetable timetable;

    @ManyToOne
    @JoinColumn(name="teacher_id")
    private Teacher teacher;

    @Column(name="date")
    private LocalDate date;

    @Column(name="start_time")
    private LocalTime startTime;

    @Column(name="end_time")
    private LocalTime endTime;

    @Column(name="room")
    private int room;
}
