package br.com.tabelaponto.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import br.com.tabelaponto.model.Horario;
import br.com.tabelaponto.model.Periodo;

public class TabelaPontoServiceImpl implements TabelaPontoService {

	@Override
	public Periodo calcularMarcacoes(List<Horario> horarios, List<Horario> horariosTrabalhados) {

		Periodo periodo = new Periodo();

		periodo.setAtraso(calcularAtrasos(horarios, horariosTrabalhados));
		periodo.setExtra(calcularExtras(horarios, horariosTrabalhados));
		return periodo;
	}

	private List<Horario> calcularAtrasos(List<Horario> horarios, List<Horario> horariosTrabalhados) {
		List<Horario> atrasos = new ArrayList<>();

		for (Horario horario : horarios) {
			boolean horarioTrabalhadoEncontrado = false;

			for (Horario horarioTrabalhado : horariosTrabalhados) {

				if (isAfterMeioDia(horario)) {
					if (isAfterMeioDia(horarioTrabalhado)) {
						if ((horarioTrabalhado.getInicio().isAfter(horario.getFim())
								|| horarioTrabalhado.getInicio().equals(horario.getFim()))
								&& (horario.getInicio().isAfter(horarioTrabalhado.getFim())
										|| horario.getInicio().equals(horarioTrabalhado.getFim()))) {

							if (horario.getInicio().isBefore(horarioTrabalhado.getInicio())) {
								atrasos.add(new Horario(horario.getInicio(), horarioTrabalhado.getInicio()));
							}
							if (horario.getFim().isAfter(horarioTrabalhado.getFim())) {
								atrasos.add(new Horario(horarioTrabalhado.getFim(), horario.getFim()));
							}
							horarioTrabalhadoEncontrado = true;

						} else if (horario.getInicio().equals(horarioTrabalhado.getInicio())
								&& horario.getFim().equals(horarioTrabalhado.getFim())) {

							horarioTrabalhadoEncontrado = true;

						}

					} else {
						if (horarioTrabalhado.getInicio().isBefore(horario.getFim())) {
							atrasos.add(new Horario(horario.getInicio(), horarioTrabalhado.getInicio()));
						}

					}

				} else if (isAfterMeioDia(horarioTrabalhado)) {

					if (horarioTrabalhado.getInicio().isAfter(horario.getFim())) {
						if(horarioTrabalhado.getFim().equals(horario.getInicio())) {
							atrasos.add(new Horario(horarioTrabalhado.getInicio(), horario.getInicio()));
						}
						atrasos.add(new Horario(horario.getInicio(), horarioTrabalhado.getFim()));
						horarioTrabalhadoEncontrado = true;
					}
					if (horarioTrabalhado.getFim().isAfter(horario.getInicio())) {
						if (horarioTrabalhado.getInicio().equals(horario.getFim())) {
							atrasos.add(new Horario(horarioTrabalhado.getFim(), horario.getFim()));
						}
						atrasos.add(new Horario(horario.getInicio(), horarioTrabalhado.getFim()));
						horarioTrabalhadoEncontrado = true;
					}
					if(horarioTrabalhado.getFim().equals(horario.getInicio()) && horarioTrabalhado.getInicio().equals(horario.getFim())) {
						horarioTrabalhadoEncontrado = false;
					}
					

				} else {
					if ((horarioTrabalhado.getFim().isAfter(horario.getInicio())
							|| horarioTrabalhado.getFim().equals(horario.getInicio()))
							&& (horario.getFim().isAfter(horarioTrabalhado.getInicio())
									|| horario.getFim().equals(horarioTrabalhado.getInicio()))) {

						if (horario.getInicio().isBefore(horarioTrabalhado.getInicio())) {
							atrasos.add(new Horario(horario.getInicio(), horarioTrabalhado.getInicio()));
						}
						if (horario.getFim().isAfter(horarioTrabalhado.getFim())) {
							atrasos.add(new Horario(horarioTrabalhado.getFim(), horario.getFim()));
						}
						horarioTrabalhadoEncontrado = true;

					} else if (horario.getInicio().equals(horarioTrabalhado.getInicio())
							&& horario.getFim().equals(horarioTrabalhado.getFim())) {
						horarioTrabalhadoEncontrado = true;
					}
				}

			}

			if (!horarioTrabalhadoEncontrado) {
				atrasos.add(horario);
			}
		}

		List<Horario> atrasosIteradores = new ArrayList<>(atrasos);
		List<Horario> atrasosCorrigidos = new ArrayList<>(atrasos);

		int i = 0;
		for (Horario atrasoIterado : atrasosIteradores) {
			for (Horario atraso : atrasos) {
				if (atraso.getFim().isBefore(atrasoIterado.getFim())
						&& atrasoIterado.getInicio().isBefore(atraso.getFim())) {
					try {
						atrasosCorrigidos.remove(i);
						atrasosCorrigidos.remove(i);
					} catch (IndexOutOfBoundsException ignored) {

					}
					if (!atrasosIteradores.get(atrasosIteradores.size() - 1).getFim().isAfter(atraso.getFim())) {
						atrasosCorrigidos.add(new Horario(atrasoIterado.getInicio(), atraso.getFim()));
					}
					i--;
				}
			}
			i++;
		}
		return atrasosCorrigidos;
	}

	private List<Horario> calcularExtras(List<Horario> horarios, List<Horario> horariosTrabalhados) {
		List<Horario> horasExtras = new ArrayList<>();

		for (Horario horarioTrabalhado : horariosTrabalhados) {
			boolean horarioEncontrado = false;

			for (Horario horario : horarios) {
				if (isAfterMeioDia(horario)) {
					if (isAfterMeioDia(horarioTrabalhado)) {

						if ((horario.getInicio().isAfter(horarioTrabalhado.getFim())
								|| horario.getInicio().equals(horarioTrabalhado.getFim()))
								&& (horarioTrabalhado.getInicio().isAfter(horario.getFim())
										|| horarioTrabalhado.getInicio().equals(horario.getFim()))) {

							if (horarioTrabalhado.getInicio().isAfter(horario.getInicio())) {
								horasExtras.add(new Horario(horario.getInicio(), horarioTrabalhado.getInicio()));
							}
							if (horarioTrabalhado.getFim().isAfter(horario.getFim())) {
								horasExtras.add(new Horario(horario.getFim(), horarioTrabalhado.getFim()));
							}
							horarioEncontrado = true;

						}

					} else if (horarioTrabalhado.getFim().isBefore(horario.getFim())) {

						horasExtras.add(new Horario(horario.getFim(), horarioTrabalhado.getFim()));
						horarioEncontrado = true;
					} else if (horarioTrabalhado.getInicio().isAfter(horario.getFim())) {
						if (horarioTrabalhado.getFim().isAfter(horario.getInicio())) {
							horasExtras.add(new Horario(horarioTrabalhado.getInicio(), horario.getInicio()));
							horarioEncontrado = true;
						}
					}

				} else if (isAfterMeioDia(horarioTrabalhado)) {

					if (horario.getFim().isBefore(horarioTrabalhado.getInicio())) {
						horasExtras.add(new Horario(horarioTrabalhado.getInicio(), horario.getFim()));
						horarioEncontrado = true;
					}
					if (horario.getInicio().isBefore(horarioTrabalhado.getFim())) {
						horasExtras.add(new Horario(horarioTrabalhado.getInicio(), horario.getInicio()));
						horarioEncontrado = true;
					}
					

				} else {

					if ((horario.getFim().isAfter(horarioTrabalhado.getInicio())
							|| horario.getFim().equals(horarioTrabalhado.getInicio()))
							&& (horarioTrabalhado.getFim().isAfter(horario.getInicio())
									|| horarioTrabalhado.getFim().equals(horario.getInicio()))) {

						if (horarioTrabalhado.getInicio().isAfter(horario.getInicio())) {
							horasExtras.add(new Horario(horario.getInicio(), horarioTrabalhado.getInicio()));
						}
						if (horarioTrabalhado.getFim().isAfter(horario.getFim())) {
							horasExtras.add(new Horario(horario.getFim(), horarioTrabalhado.getFim()));
						}
						horarioEncontrado = true;
					}

				}
				if (horarioTrabalhado.getInicio().equals(horario.getInicio())
						&& horarioTrabalhado.getFim().equals(horario.getFim())) {
					horarioEncontrado = true;
				}
			}

			if (!horarioEncontrado) {
				horasExtras.add(horarioTrabalhado);
			}
		}
		return horasExtras;

	}

	private Boolean isAfterMeioDia(Horario horario) {
		LocalTime marcaMeioDia = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
		return horario.getInicio().isAfter(marcaMeioDia) && horario.getFim().isBefore(marcaMeioDia);
	}
}
