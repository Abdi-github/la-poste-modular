package ch.swiftapp.laposte.branch.model;
import ch.swiftapp.laposte.branch.enums.*;
import ch.swiftapp.laposte.shared.model.BaseEntity;
import ch.swiftapp.laposte.shared.i18n.TranslatableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.HashSet;
import java.util.Set;
@Entity @Table(name = "branch")
@Getter @Setter @SuperBuilder @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class Branch extends BaseEntity implements TranslatableEntity<BranchTranslation> {
    @Column(name = "branch_code", nullable = false, unique = true, length = 20) private String branchCode;
    @Enumerated(EnumType.STRING) @Column(name = "type", nullable = false, length = 30) private BranchType type;
    @Enumerated(EnumType.STRING) @Column(name = "status", nullable = false, length = 30) private BranchStatus status;
    @Column(name = "street") private String street;
    @Column(name = "zip_code", length = 10) private String zipCode;
    @Column(name = "city", length = 100) private String city;
    @Column(name = "canton", length = 5) private String canton;
    @Column(name = "phone", length = 30) private String phone;
    @Column(name = "email", length = 150) private String email;
    @Column(name = "latitude") private Double latitude;
    @Column(name = "longitude") private Double longitude;
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default private Set<BranchTranslation> translations = new HashSet<>();
    public void addTranslation(BranchTranslation t) { t.setBranch(this); translations.add(t); }
}
