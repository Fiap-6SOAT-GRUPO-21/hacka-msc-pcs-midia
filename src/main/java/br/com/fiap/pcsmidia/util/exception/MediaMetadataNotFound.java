package br.com.fiap.pcsmidia.util.exception;

public class MediaMetadataNotFound extends RuntimeException {

    public MediaMetadataNotFound() {
        super("Media Meta Data not found in repository.");
    }
}
