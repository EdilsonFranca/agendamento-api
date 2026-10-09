package br.com.agendamentoapi.repository;

import br.com.agendamentoapi.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {}