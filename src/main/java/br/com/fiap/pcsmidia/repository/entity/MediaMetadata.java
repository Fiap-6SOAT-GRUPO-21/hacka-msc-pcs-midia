package br.com.fiap.pcsmidia.repository.entity;

import br.com.fiap.pcsmidia.util.enumerated.MediaStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MediaMetadata extends AuditData {

    @Id
    @UuidGenerator
    private UUID mediaId;

    private String storagePath;

    @Enumerated(EnumType.STRING)
    private MediaStatus status;

    private String userReference;

    private String zippedFolderPath;

}
