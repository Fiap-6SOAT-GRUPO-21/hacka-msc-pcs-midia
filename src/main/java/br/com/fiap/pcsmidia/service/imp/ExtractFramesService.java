package br.com.fiap.pcsmidia.service.imp;

import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ExtractFramesService {

    private final FFmpeg ffmpeg = new FFmpeg();
    private final FFprobe ffprobe = new FFprobe();

    public ExtractFramesService() throws IOException {
    }

    public Path extractFramesFromFile(File videoFile) throws Exception {

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

    public void  extractFramesFromStream(InputStream inputStream, Path outputDir) throws IOException, InterruptedException {

        Files.createDirectories(outputDir);

        // Cria o comando ffmpeg com os parâmetros para extrair frames
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg",
                "-i", "pipe:0",
                "-vf", "fps=1",
                outputDir.resolve("frame-%03d.png").toString()
        );

        System.out.println("Comando executado: " + String.join(" ", processBuilder.command()));

        // Configura o processo para usar o InputStream como entrada
        Process process = processBuilder.start();

        try (OutputStream os = process.getOutputStream()) {
            // Copia os dados do InputStream do vídeo para o OutputStream do ffmpeg
            byte[] buffer = new byte[4096];
            int bytesRead;
            int totalBytes = 0;

            // Lê o InputStream enquanto não chega ao final
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }

            System.out.println("Total de bytes enviados ao ffmpeg: " + totalBytes);
            os.close();
            os.flush();
        } finally {
            inputStream.close();
        }

        // Espera o processo do ffmpeg terminar
        if (!process.waitFor(60, TimeUnit.SECONDS)) {
            process.destroy();
            throw new RuntimeException("ffmpeg não terminou em 60 segundos.");
        }

        log.info("Frames extraídos com sucesso em: {}", outputDir);
    }
}
