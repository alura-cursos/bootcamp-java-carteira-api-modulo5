package br.com.alura.carteira.service;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	public Page<TransacaoDto> listar(Pageable paginacao) {
		return repository
				.findAll(paginacao)
				.map(t -> modelMapper.map(t, TransacaoDto.class));
	}

	@Transactional
	public TransacaoDto cadastrar(TransacaoFormDto dto) {
		Long idUsuario = dto.getUsuarioId();
		
		try {
			Usuario usuario = usuarioRepository.getById(idUsuario);
			Transacao transacao = modelMapper.map(dto, Transacao.class);
			transacao.setId(null);
			transacao.setUsuario(usuario);

			repository.save(transacao);

			return modelMapper.map(transacao, TransacaoDto.class);
		} catch (EntityNotFoundException e) {
			throw new IllegalArgumentException("usuario inexistente!");
		}
	}

	@Transactional
	public TransacaoDto atualizar(AtualizacaoTransacaoFormDto dto) {
		Transacao transacao = repository.getById(dto.getId());
		transacao.atualizarInformacoes(dto.getTicker(), dto.getData(), dto.getPreco(), dto.getQuantidade(), dto.getTipo());
		
		return modelMapper.map(transacao, TransacaoDto.class);
	}

	@Transactional
	public void remover(Long id) {
		repository.deleteById(id);
	}

	public TransacaoDetalhadaDto detalhar(Long id) {
		Transacao transacao = repository
				.findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		
		return modelMapper.map(transacao, TransacaoDetalhadaDto.class);
	}

}














