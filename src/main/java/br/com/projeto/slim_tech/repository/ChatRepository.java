package br.com.projeto.slim_tech.repository;

import br.com.projeto.slim_tech.domain.ChatTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatTable, Long> {
}
