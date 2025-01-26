package br.com.fiap.pcsmidia.storage.service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

public interface S3MediaService {

    File downloadMedia(String path);

    InputStream downloadMediaAsStream(String path);

    void uploadFrames(String path, Path framesDir);

}
