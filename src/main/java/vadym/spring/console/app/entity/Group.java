package vadym.spring.console.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name="group_name")
    private String groupName;

    @OneToMany(mappedBy = "group")
    private List<Student> students;

    @ManyToMany
    @JoinTable(name = "group_lectures",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "lecture_id"))
    private Set<Lecture> lectures;

    @ManyToMany(mappedBy = "groups")
    private List<Course> courses;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;
}
