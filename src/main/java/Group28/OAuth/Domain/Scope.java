package Group28.OAuth.Domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Scopes")
public class Scope {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(unique = true, name = "scope_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany
    @JoinColumn(name = "permission_id")
    private Set<Permission> permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
