package br.com.alura.carteira.service;

import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.alura.carteira.dto.UsuarioDto;
import br.com.alura.carteira.dto.UsuarioFormDto;
import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;

	public Page<UsuarioDto> listar(Pageable paginacao) {
		return repository.findAll(paginacao).map(t -> modelMapper.map(t, UsuarioDto.class));
	}

	@Transactional
	public UsuarioDto cadastrar(UsuarioFormDto dto) {
		Usuario usuario = modelMapper.map(dto, Usuario.class);

		String senha = new Random().nextInt(999999) + "";
		usuario.setSenha(passwordEncoder.encode(senha));

		repository.save(usuario);
		return modelMapper.map(usuario, UsuarioDto.class);
	}

}
