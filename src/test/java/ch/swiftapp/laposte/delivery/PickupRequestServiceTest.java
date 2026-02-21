package ch.swiftapp.laposte.delivery;

import ch.swiftapp.laposte.delivery.dto.request.CreatePickupRequest;
import ch.swiftapp.laposte.delivery.dto.response.PickupResponse;
import ch.swiftapp.laposte.delivery.enums.PickupStatus;
import ch.swiftapp.laposte.delivery.model.PickupRequest;
import ch.swiftapp.laposte.delivery.repository.PickupRequestRepository;
import ch.swiftapp.laposte.delivery.service.PickupRequestServiceImpl;
import ch.swiftapp.laposte.shared.exception.BusinessRuleException;
import ch.swiftapp.laposte.shared.exception.ResourceNotFoundException;
import ch.swiftapp.laposte.shared.service.AuditService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PickupRequestServiceTest {

    @Mock private PickupRequestRepository pickupRequestRepository;
    @Mock private AuditService auditService;
    @InjectMocks private PickupRequestServiceImpl pickupRequestService;

    @Test
    void create_shouldSetPendingStatus() {
        CreatePickupRequest request = CreatePickupRequest.builder()
                .customerId(1L)
                .pickupStreet("Bahnhofstr. 1")
                .pickupZipCode("8001")
                .pickupCity("Zürich")
                .preferredDate(LocalDate.now().plusDays(1))
                .build();

        PickupRequest saved = PickupRequest.builder()
                .customerId(1L)
                .pickupStreet("Bahnhofstr. 1")
                .pickupZipCode("8001")
                .pickupCity("Zürich")
                .preferredDate(LocalDate.now().plusDays(1))
                .status(PickupStatus.PENDING)
                .build();

        when(pickupRequestRepository.save(any())).thenReturn(saved);

        PickupResponse result = pickupRequestService.create(request);

        assertThat(result.getStatus()).isEqualTo(PickupStatus.PENDING);
        assertThat(result.getPickupCity()).isEqualTo("Zürich");
        verify(pickupRequestRepository).save(any());
    }

    @Test
    void changeStatus_shouldRejectInvalidTransition() {
        PickupRequest entity = PickupRequest.builder()
                .status(PickupStatus.PICKED_UP)
                .customerId(1L)
                .pickupStreet("Test")
                .pickupZipCode("1000")
                .pickupCity("Test")
                .preferredDate(LocalDate.now())
                .build();
        when(pickupRequestRepository.findById(1L)).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> pickupRequestService.changeStatus(1L, PickupStatus.CANCELLED))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Invalid status transition");
    }

    @Test
    void changeStatus_shouldAllowPendingToRequested() {
        PickupRequest entity = PickupRequest.builder()
                .status(PickupStatus.PENDING)
                .customerId(1L)
                .pickupStreet("Test")
                .pickupZipCode("1000")
                .pickupCity("Test")
                .preferredDate(LocalDate.now())
                .build();
        when(pickupRequestRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(pickupRequestRepository.save(any())).thenReturn(entity);

        PickupResponse result = pickupRequestService.changeStatus(1L, PickupStatus.REQUESTED);

        assertThat(result).isNotNull();
        verify(pickupRequestRepository).save(any());
    }

    @Test
    void getById_shouldThrowWhenNotFound() {
        when(pickupRequestRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pickupRequestService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

