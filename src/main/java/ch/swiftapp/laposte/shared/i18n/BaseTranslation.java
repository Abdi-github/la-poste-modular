package ch.swiftapp.laposte.shared.i18n;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter @Setter @SuperBuilder @NoArgsConstructor
public abstract class BaseTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "locale", nullable = false, length = 5)
    private String locale;
}

