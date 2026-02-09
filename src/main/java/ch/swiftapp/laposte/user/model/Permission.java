package ch.swiftapp.laposte.user.model;

import ch.swiftapp.laposte.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity @Table(name = "permission")
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class Permission extends BaseEntity {
    @Column(name = "name", nullable = false, unique = true, length = 100) private String name;
    @Column(name = "description") private String description;
}

