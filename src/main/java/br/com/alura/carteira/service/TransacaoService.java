package br.com.alura.carteira.service;

import java.math.BigDecimal;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.alura.carteira.dto.AtualizacaoTransacaoFormDto;
import br.com.alura.carteira.dto.TransacaoDetalhadaDto;
import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.repository.TransacaoRepository;
import br.com.alura.carteira.repository.UsuarioRepository;

@Service
public class TransacaoService {

	@Autowired
	private TransacaoRepository repository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CalculadoraDeImpostoService calculadoraDeImpostoService;

	public Page<TransacaoDto> listar(Pageable paginacao, Usuario logado) {
		return repository
				.findAllByUsuario(logado, paginacao)
				.map(t -> modelMapper.map(t, TransacaoDto.class));
		
//		List<TransacaoDto> transacoesDto = new ArrayList<>();
//		
//		transacoes.forEach(t -> {
//			BigDecimal imposto = calculadoraDeImpostoService.calcular(t);
//			TransacaoDto dto = modelMapper.map(t, TransacaoDto.class);
//			dto.setImposto(imposto);
//			
//			transacoesDto.add(dto);
//		});
//		
//		return new PageImpl<TransacaoDto>(transacoesDto, transacoes.getPageable(), transacoes.getTotalElements());
	}

	@Transactional
	public TransacaoDto cadastrar(TransacaoFormDto dto, Usuario logado) {
		Long idUsuario = dto.getUsuarioId();
		
		try {
			Usuario usuario = usuarioRepository.getById(idUsuario);
			
			if (!logado.equals(usuario)) {
				lancarErro403();
			}
			
			Transacao transacao = modelMapper.map(dto, Transacao.class);
			transacao.setId(null);
			transacao.setUsuario(usuario);
			
			BigDecimal imposto = calculadoraDeImpostoService.calcular(transacao);
			transacao.setImposto(imposto);

			repository.save(transacao);

			return modelMapper.map(transacao, TransacaoDto.class);
		} catch (EntityNotFoundException e) {
			throw new IllegalArgumentException("usuario inexistente!");
		}
	}

	@Transactional
	public TransacaoDto atualizar(AtualizacaoTransacaoFormDto dto, Usuario logado) {
		Transacao transacao = repository.getById(dto.getId());
		
		if (!transacao.pertenceAo(logado)) {
			lancarErro403();
		}
		
		transacao.atualizarInformacoes(dto.getTicker(), dto.getData(), dto.getPreco(), dto.getQuantidade(), dto.getTipo());
		
		return modelMapper.map(transacao, TransacaoDto.class);
	}

	@Transactional
	public void remover(Long id, Usuario logado) {
		Transacao transacao = repository
				.findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		
		if (transacao.pertenceAo(logado)) {
			repository.deleteById(id);
		} else {
			lancarErro403();
		}
	}

	public TransacaoDetalhadaDto detalhar(Long id, Usuario logado) {
		Transacao transacao = repository
				.findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		
		if (!transacao.pertenceAo(logado)) {
			lancarErro403();
		}
		return modelMapper.map(transacao, TransacaoDetalhadaDto.class);
	}

	private void lancarErro403() {
		throw new AccessDeniedException("Acesso negado!");
	}

}














