package ch.swiftapp.laposte.parcel;

import ch.swiftapp.laposte.parcel.dto.request.CreateParcelRequest;
import ch.swiftapp.laposte.parcel.dto.response.ParcelResponse;
import ch.swiftapp.laposte.parcel.enums.ParcelStatus;
import ch.swiftapp.laposte.parcel.enums.ParcelType;
import ch.swiftapp.laposte.parcel.mapper.ParcelMapper;
import ch.swiftapp.laposte.parcel.model.Parcel;
import ch.swiftapp.laposte.parcel.repository.ParcelRepository;
import ch.swiftapp.laposte.parcel.service.ParcelServiceImpl;
import ch.swiftapp.laposte.shared.exception.BusinessRuleException;
import ch.swiftapp.laposte.shared.exception.ResourceNotFoundException;
import ch.swiftapp.laposte.shared.event.ParcelCreatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParcelServiceTest {

    @Mock private ParcelRepository parcelRepository;
    @Mock private ParcelMapper parcelMapper;
    @Mock private ApplicationEventPublisher events;
    @InjectMocks private ParcelServiceImpl parcelService;

    @Test
    void create_shouldSetTrackingNumberAndCreatedStatus() {
        // Arrange
        CreateParcelRequest request = CreateParcelRequest.builder()
                .type(ParcelType.STANDARD)
                .weightKg(2.5)
                .recipientName("Max Muster")
                .build();

        Parcel entity = Parcel.builder()
                .type(ParcelType.STANDARD)
                .weightKg(2.5)
                .recipientName("Max Muster")
                .build();

        Parcel saved = Parcel.builder()
                .trackingNumber("LP-ABC123DEF-CH")
                .status(ParcelStatus.CREATED)
                .type(ParcelType.STANDARD)
                .weightKg(2.5)
                .recipientName("Max Muster")
                .price(BigDecimal.valueOf(10.00))
                .build();

        ParcelResponse response = ParcelResponse.builder()
                .trackingNumber("LP-ABC123DEF-CH")
                .status(ParcelStatus.CREATED)
                .type(ParcelType.STANDARD)
                .price(BigDecimal.valueOf(10.00))
                .recipientName("Max Muster")
                .build();

        when(parcelMapper.toEntity(request)).thenReturn(entity);
        when(parcelRepository.existsByTrackingNumber(any())).thenReturn(false);
        when(parcelRepository.save(any())).thenReturn(saved);
        when(parcelMapper.toResponse(saved)).thenReturn(response);

        // Act
        ParcelResponse result = parcelService.create(request);

        // Assert
        assertThat(result.getStatus()).isEqualTo(ParcelStatus.CREATED);
        assertThat(result.getTrackingNumber()).isNotNull();
        verify(parcelRepository).save(any());
        verify(events).publishEvent(any(ParcelCreatedEvent.class));
    }

    @Test
    void getById_shouldThrowWhenNotFound() {
        when(parcelRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parcelService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void cancel_shouldNotAllowCancelAfterPickup() {
        Parcel parcel = Parcel.builder()
                .status(ParcelStatus.PICKED_UP)
                .trackingNumber("LP-TEST12345-CH")
                .build();
        when(parcelRepository.findById(1L)).thenReturn(Optional.of(parcel));

        assertThatThrownBy(() -> parcelService.cancel(1L))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Cannot cancel");
    }
}



