package personal.bookerav2.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(initialValue = 100)
    @Column(name = "category_id")
    Integer categoryId;

    @Column(name = "category_name", nullable = false, length = 20, unique = true)
    String categoryName;
}
