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
@Table(name="universities")
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="university_id")
    private Long id;

    @Column(name="university_name")
    private String universityName;

    @Column(name="location")
    private String location;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<Faculty> facultyList;
}