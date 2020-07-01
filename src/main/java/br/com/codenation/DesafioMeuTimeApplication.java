package br.com.codenation;
import br.com.codenation.exceptions.CapitaoNaoInformadoException;
import br.com.codenation.exceptions.IdentificadorUtilizadoException;
import br.com.codenation.exceptions.JogadorNaoEncontradoException;
import br.com.codenation.exceptions.TimeNaoEncontradoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DesafioMeuTimeApplication implements MeuTimeInterface {
	private List<Team> teams = new ArrayList<>();
	private List<Player> players = new ArrayList<>();
	//private Stream<Player> ;


	public void incluirTime(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal, String corUniformeSecundario) {
		if(teams.stream().anyMatch(team -> team.getId().equals(id))) {
			throw new IdentificadorUtilizadoException();
		}
		teams.add(new Team(id, nome, dataCriacao, corUniformePrincipal, corUniformeSecundario));
	}

	public void incluirJogador(Long id, Long idTime, String nome, LocalDate dataNascimento, Integer nivelHabilidade, BigDecimal salario) {
		if(players.stream().anyMatch(player -> player.getId().equals(id))) {
			throw new IdentificadorUtilizadoException();
		}
		teamExists(idTime);
		players.add(new Player(id, idTime, nome, dataNascimento, nivelHabilidade, salario));
	}

	public void definirCapitao(Long idJogador) {
		if(players.stream().noneMatch(player -> player.getId().equals(idJogador))) {
			throw new JogadorNaoEncontradoException();
		}
		//players.stream().filter(player -> player.getIdTime().equals(teamExists(idJogador))).findFirst().get().setIdCapitaoTime(idJogador);
		players.forEach(player -> player.setIdCapitaoTime(player.getId().equals(idJogador)));
	}

	public Long buscarCapitaoDoTime(Long idTime) {
		Team team = validateTeam(idTime);
		Player capitao = players.stream().filter(player -> player.getIdTime().equals(team.getId()) && player.getIdCapitaoTime()).findFirst().orElseThrow(CapitaoNaoInformadoException::new);
		return capitao.getId();
	}

	public String buscarNomeJogador(Long idJogador) {
		return validatePLayer(idJogador).getNome();
	}

	public String buscarNomeTime(Long idTime) {
		return validateTeam(idTime).getNome();
	}

	public List<Long> buscarJogadoresDoTime(Long idTime) {
		teamExists(idTime);
		return players.stream().filter(player -> player.getIdTime().equals(idTime)).map(Player::getId).sorted().collect(Collectors.toList());
	}

	public Long buscarMelhorJogadorDoTime(Long idTime) {
		teamExists(idTime);
		return players.stream().filter(player -> player.getIdTime().equals(idTime)).sorted(Comparator.comparingInt(Player::getNivelHabilidade).reversed().thenComparingLong(Player::getId)).map(Player::getId).findFirst().orElseThrow(JogadorNaoEncontradoException::new);
	}

	public Long buscarJogadorMaisVelho(Long idTime) {
		teamExists(idTime);
		return players.stream().filter(player -> player.getIdTime().equals(idTime)).sorted(Comparator.comparing(Player::getDataNascimento).thenComparingLong(Player::getId)).map(Player::getId).findFirst().orElseThrow(JogadorNaoEncontradoException::new);
	}

	public List<Long> buscarTimes() {
		return teams.stream().sorted(Comparator.comparingLong(Team::getId)).map(Team::getId).collect(Collectors.toList());
	}

	public Long buscarJogadorMaiorSalario(Long idTime) {
		teamExists(idTime);
		return players.stream().filter(player -> player.getIdTime().equals(idTime)).sorted(Comparator.comparing(Player::getSalario).reversed().thenComparingLong(Player::getId)).map(Player::getId).findFirst().orElseThrow(JogadorNaoEncontradoException::new);
	}

	public BigDecimal buscarSalarioDoJogador(Long idJogador) {
		return validatePLayer(idJogador).getSalario();
	}

	public List<Long> buscarTopJogadores(Integer top) {
		return players.stream().sorted(Comparator.comparingInt(Player::getNivelHabilidade).reversed().thenComparingLong(Player::getId)).limit(top).map(Player::getId).collect(Collectors.toList());
	}

	private void teamExists(Long idTime){
		if(teams.stream().noneMatch(team -> team.getId().equals(idTime))){
			throw new TimeNaoEncontradoException();
		}
	}

	//private void capitaoExists(Long idTime){
		//if (players.stream().filter(player -> player.getId().equals(idTime)).findFirst().get().getIdCapitaoTime() == null){
			//throw new CapitaoNaoInformadoException();
		//}

	private Team validateTeam(Long idTime){
			return teams.stream().filter(team -> team.getId().equals(idTime)).findFirst().orElseThrow(TimeNaoEncontradoException::new);

			//return players.stream().filter(player -> player.getId().equals(idTime)).findFirst().get().getIdTime();
		}

	private Player validatePLayer(Long idJogador){
		return players.stream().filter(player -> player.getId().equals(idJogador)).findFirst().orElseThrow(JogadorNaoEncontradoException::new);
	}

}
