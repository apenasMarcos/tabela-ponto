package br.com.tabelaponto.model;

import java.util.List;
import java.util.ArrayList;

public class Periodo {

	private List<Horario> atrasos = new ArrayList<>();;
	private List<Horario> extras = new ArrayList<>();;

	public List<Horario> getAtraso() {
		return atrasos;
	}

	public void setAtraso(List<Horario> atrasos) {
		this.atrasos = atrasos;
	}

	public List<Horario> getExtra() {
		return extras;
	}

	public void setExtra(List<Horario> extras) {
		this.extras = extras;
	}

	public String getExtraFormatado() {
		return formataHorarios(extras);
	}

	public String getAtrasoFormatado() {
		return formataHorarios(atrasos);
	}

	private String formataHorarios(List<Horario> horarios) {
		StringBuilder horarioStringBuilder = new StringBuilder();
		horarios.forEach(horario -> {
			horarioStringBuilder.append("[" + horario.getInicio()).append(" / ").append(horario.getFim()).append("] ");
		});
		return horarioStringBuilder.toString();
	}

}
