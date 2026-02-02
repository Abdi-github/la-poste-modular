package ch.swiftapp.laposte.branch.model;
import ch.swiftapp.laposte.shared.i18n.BaseTranslation;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
@Entity @Table(name = "branch_translation", uniqueConstraints = @UniqueConstraint(columnNames = {"branch_id", "locale"}))
@Getter @Setter @SuperBuilder @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class BranchTranslation extends BaseTranslation {
    @Column(name = "name", nullable = false) private String name;
    @Column(name = "description") private String description;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;
}
