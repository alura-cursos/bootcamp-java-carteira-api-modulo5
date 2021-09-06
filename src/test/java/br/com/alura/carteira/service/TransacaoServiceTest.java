package br.com.alura.carteira.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.modelo.TipoTransacao;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.repository.TransacaoRepository;
import br.com.alura.carteira.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {
	
	@Mock
	private TransacaoRepository repository;

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private TransacaoService service;

	private Usuario logado;
	
	@BeforeEach
	void beforeEach() {
		this.logado = new Usuario("Rafaela", "rafa", "123456");
	}
	
	private TransacaoFormDto criarTransacaoFormDto() {
		TransacaoFormDto formDto = new TransacaoFormDto(
				"ITSA4",
				new BigDecimal("10.45"),
				LocalDate.now(),
				120,
				TipoTransacao.COMPRA,
				1l
			);
		return formDto;
	}

	@Test
	void deveriaCadastrarUmaTransacao() {
		TransacaoFormDto formDto = criarTransacaoFormDto();
		Transacao transacao = new Transacao(
				formDto.getTicker(), 
				formDto.getData(), 
				formDto.getPreco(), 
				formDto.getQuantidade(), 
				formDto.getTipo(), 
				logado);
		
		Mockito.when(usuarioRepository.getById(formDto.getUsuarioId())).thenReturn(logado);
		Mockito.when(modelMapper.map(formDto, Transacao.class)).thenReturn(transacao);
		Mockito.when(modelMapper.map(transacao, TransacaoDto.class)).thenReturn(new TransacaoDto(
				null, formDto.getTicker(), formDto.getPreco(), formDto.getQuantidade(), formDto.getTipo()
				));
		
		TransacaoDto dto = service.cadastrar(formDto, logado);
		
		Mockito.verify(repository).save(Mockito.any());
		
		assertEquals(formDto.getTicker(), dto.getTicker());
		assertEquals(formDto.getPreco(), dto.getPreco());
		assertEquals(formDto.getQuantidade(), dto.getQuantidade());
		assertEquals(formDto.getTipo(), dto.getTipo());
	}

	@Test
	void naoDeveriaCadastrarUmaTransacaoComUsuarioInexistente() {
		TransacaoFormDto formDto = criarTransacaoFormDto();
		
		Mockito
		.when(usuarioRepository.getById(formDto.getUsuarioId()))
		.thenThrow(EntityNotFoundException.class);
		
		assertThrows(IllegalArgumentException.class, () -> service.cadastrar(formDto, logado));
	}

}
