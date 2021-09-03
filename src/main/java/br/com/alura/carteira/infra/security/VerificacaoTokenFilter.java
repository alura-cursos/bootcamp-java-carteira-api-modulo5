package br.com.alura.carteira.infra.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.repository.UsuarioRepository;

public class VerificacaoTokenFilter extends OncePerRequestFilter {

	private TokenService tokenService;
	private UsuarioRepository usuarioRepository;

	public VerificacaoTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String token = request.getHeader("Authorization");
		
		// nao veio o header AUTHORIZATION
		if (token == null || token.isBlank()) {
			filterChain.doFilter(request, response);
			return;
		}

		// remove o prefixo BEARER do texto do token
		token = token.replace("Bearer ", "");
		
		// Token valido foi enviado na requisicao
		if (tokenService.isValido(token)) {
			// recupera o id do usuario que foi guardado dentro do token e carrega o usuario do banco de dados
			Long idUsuario = tokenService.extrairIdDoUsuario(token);
			Usuario usuario = usuarioRepository.findById(idUsuario).get();
			
			// indica para o Spring que o usuario esta logado para essa requisicao
			Authentication autenticacao = new UsernamePasswordAuthenticationToken(usuario, null, null);
			SecurityContextHolder.getContext().setAuthentication(autenticacao);
		}
		
		// indica para o Spring seguir o fluxo normal da requisicao
		filterChain.doFilter(request, response);
	}

}
