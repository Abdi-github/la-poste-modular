package ch.swiftapp.laposte.shared;

import ch.swiftapp.laposte.shared.model.AuditLog;
import ch.swiftapp.laposte.shared.repository.AuditLogRepository;
import ch.swiftapp.laposte.shared.service.AuditService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock private AuditLogRepository auditLogRepository;
    @InjectMocks private AuditService auditService;

    @Test
    void log_shouldSaveAuditEntry() {
        auditService.log("Parcel", 1L, "CREATE", "New parcel created");

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository).save(captor.capture());

        AuditLog saved = captor.getValue();
        assertThat(saved.getEntityType()).isEqualTo("Parcel");
        assertThat(saved.getEntityId()).isEqualTo(1L);
        assertThat(saved.getAction()).isEqualTo("CREATE");
        assertThat(saved.getDetails()).isEqualTo("New parcel created");
    }
}

