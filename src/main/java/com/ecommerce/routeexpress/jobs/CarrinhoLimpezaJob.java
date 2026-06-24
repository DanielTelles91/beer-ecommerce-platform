package com.ecommerce.routeexpress.jobs;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ecommerce.routeexpress.models.Carrinho;
import com.ecommerce.routeexpress.services.CarrinhoRepositorio;

/**
 *
 * @author Daniel Arantes Telles
 */

@Component
public class CarrinhoLimpezaJob {

	@Autowired
	private CarrinhoRepositorio carrinhoRepo;

	private static final int DIAS_EXPIRACAO = 1; // ajuste conforme necessário

	@Scheduled(cron = "0 0 1 * * *") // roda todo dia à 1h da manhã
	// @Scheduled(cron = "*/10 * * * * *") // A cada 10 segundos
	public void limparCarrinhosAbandonados() {
		LocalDateTime limite = LocalDateTime.now().minusMinutes(DIAS_EXPIRACAO);
		List<Carrinho> abandonados = carrinhoRepo.findByDataAtualizacaoBefore(limite);
		carrinhoRepo.deleteAll(abandonados);

	}
}
