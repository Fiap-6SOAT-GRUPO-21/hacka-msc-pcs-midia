package br.com.fiap.pcsmidia.repository;

import br.com.fiap.pcsmidia.repository.entity.MediaMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MediaRepository extends JpaRepository<MediaMetadata, UUID> {


}
