package br.com.fiap.pcsmidia.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FFmpegConfig {

    @Bean
    public FFprobe ffprobe() throws IOException {
        return new FFprobe();
    }

    @Bean
    public FFmpeg ffmpeg() throws IOException {
        return new FFmpeg();
    }
}