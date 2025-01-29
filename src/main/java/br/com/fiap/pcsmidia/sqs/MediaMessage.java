package br.com.fiap.pcsmidia.sqs;

import lombok.Data;

import java.util.UUID;

@Data
public class MediaMessage {

    private UUID mediaId;
    private String storagePath;
    private String userReference;
    private String phoneNumber;
    private String status;
    private String zippedPath;
}
