package personal.bookerav2.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "roles")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private short roleId;

    @Column(name= "role_name", nullable = false)
    private String roleName;
}
