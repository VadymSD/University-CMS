package vadym.spring.console.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="faculties")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="faculty_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="university_id")
    private University university;

    @Column(name="faculty_name")
    private String facultyName;

    @Column(name="head")
    private String head;

    @OneToMany(mappedBy = "faculty")
    private List<Group> groupList;

    @OneToMany(mappedBy = "faculty")
    private List<Teacher> teacherList;

    @OneToMany(mappedBy = "faculty")
    private List<Course> courseList;
}
