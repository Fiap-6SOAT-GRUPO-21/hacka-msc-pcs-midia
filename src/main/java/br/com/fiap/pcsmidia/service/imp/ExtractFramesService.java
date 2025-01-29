package br.com.fiap.pcsmidia.service.imp;

import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Service
public class ExtractFramesService {

    private final FFmpeg ffmpeg = new FFmpeg();
    private final FFprobe ffprobe = new FFprobe();

    public ExtractFramesService() throws IOException {
    }

    public Path extractFramesFromFile(File videoFile) {

        log.info("Starting process of converting media file to frames");

        File localFile = new File("frames/" + videoFile.getName());
        localFile.mkdirs();
        String outputPattern = localFile.getAbsolutePath() + "/frame_%04d.jpg";

        // Configurando o comando do FFmpeg para extrair frames
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(videoFile.getAbsolutePath())
                .addOutput(outputPattern)
                .setFormat("image2")
                .setVideoFrameRate(1) // 1 frame por segundo
                .done();

        // Executa o comando do FFmpeg
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();

        log.info("Frames extracted in: {}", outputPattern);

        return localFile.toPath();
    }
}
