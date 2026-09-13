package personal.bookerav2.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public int hashCode(){
        return Objects.hashCode(this.roleId);
    }
}
