package br.com.alura.carteira.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import br.com.alura.carteira.modelo.TipoTransacao;
import br.com.alura.carteira.modelo.Transacao;

@Service
public class CalculadoraDeImpostoService {
	
	//15% de imposto para transacoes do tipo venda com valor superior a R$20.000,00
	public BigDecimal calcular(Transacao transacao) {
		if (transacao.getTipo() == TipoTransacao.COMPRA) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal valorTransacao = transacao
				.getPreco()
				.multiply(new BigDecimal(transacao.getQuantidade()));
		if (valorTransacao.compareTo(new BigDecimal(20000)) < 0) {
			return BigDecimal.ZERO;
		}
		
		return valorTransacao
				.multiply(new BigDecimal("0.15"))
				.setScale(2, RoundingMode.HALF_UP);
	}

}
