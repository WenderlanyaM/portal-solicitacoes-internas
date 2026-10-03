package info.bitsolucoes.portal.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tbl_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "role", nullable = false, length = 50, unique = true)
    private String role;

    public Role() {}

    public Role(String role) {
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role1)) return false;
        return Objects.equals(id, role1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}