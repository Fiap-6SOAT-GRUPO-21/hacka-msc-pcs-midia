package br.com.fiap.pcsmidia.storage.service;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

public interface S3MediaService {

    File downloadMedia(String path);

    String uploadFrames(String userReference, UUID mediaId, Path framesDir);

}
