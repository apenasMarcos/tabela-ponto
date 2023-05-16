package br.com.tabelaponto.service;

import java.util.List;

import br.com.tabelaponto.model.Horario;
import br.com.tabelaponto.model.Periodo;

public interface TabelaPontoService {
	
	Periodo calcularMarcacoes(List<Horario> horarioTable, List<Horario> marcacoesTable);
	
	List<Horario> calcularExtras(List<Horario> horarios, List<Horario> horariosTrabalhados);

	List<Horario> calcularAtrasos(List<Horario> horarios, List<Horario> horariosTrabalhados);
}
