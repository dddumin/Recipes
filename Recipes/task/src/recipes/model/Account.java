package recipes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Email(regexp = ".+[@].+[\\.].+")
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    @OneToMany(mappedBy = "account")
    private Set<Recipe> recipes;


    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean containsRecept(Long id) {
        return recipes.stream().anyMatch(o -> o.getId().equals(id));
    }
}
