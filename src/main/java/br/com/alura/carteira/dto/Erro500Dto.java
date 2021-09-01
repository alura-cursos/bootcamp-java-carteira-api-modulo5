package br.com.alura.carteira.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Erro500Dto {

	private LocalDateTime timestamp;
	private String erro;
	private String mensagem;
	private String path;

}
