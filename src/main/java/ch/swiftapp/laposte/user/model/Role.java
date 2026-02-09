package ch.swiftapp.laposte.user.model;

import ch.swiftapp.laposte.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "role")
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class Role extends BaseEntity {
    @Column(name = "name", nullable = false, unique = true, length = 50) private String name;
    @Column(name = "description") private String description;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_permission", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    @Builder.Default private Set<Permission> permissions = new HashSet<>();
}

