package br.com.tabelaponto.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	@Override
	public List<Horario> calcularAtrasos(List<Horario> horarios, List<Horario> horariosTrabalhados) {
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
						if(horarioTrabalhado.getInicio().isBefore(horario.getFim())) {
							atrasos.add(new Horario(horario.getInicio(), horarioTrabalhado.getInicio()));
							horarioTrabalhadoEncontrado = true;
						} else if (horarioTrabalhado.getInicio().isAfter(horario.getInicio())) {
								atrasos.add(new Horario(horario.getInicio(), horarioTrabalhado.getInicio()));
								horarioTrabalhadoEncontrado = true;
							}
						if (horarioTrabalhado.getFim().isBefore(horario.getFim())) {
							atrasos.add(new Horario(horarioTrabalhado.getFim(), horario.getFim()));
							horarioTrabalhadoEncontrado = true;
						}
						
					}

				} else if (isAfterMeioDia(horarioTrabalhado)) {

					if (horarioTrabalhado.getInicio().isAfter(horario.getInicio())
							|| horarioTrabalhado.getInicio().equals(horario.getInicio())) {
						if (horario.getInicio().isAfter(horarioTrabalhado.getInicio())) {
							atrasos.add(new Horario(horarioTrabalhado.getInicio(), horario.getInicio()));
						}
						if (horario.getFim().isBefore(horarioTrabalhado.getFim())) {
							atrasos.add(new Horario(horario.getFim(), horarioTrabalhado.getFim()));
						}
						horarioTrabalhadoEncontrado = true;
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

					}
				}
				if (horarioTrabalhado.getInicio().equals(horario.getInicio())
						&& horarioTrabalhado.getFim().equals(horario.getFim())) {
					horarioTrabalhadoEncontrado = true;
				}
			}

			if (!horarioTrabalhadoEncontrado) {
				atrasos.add(horario);
			}
		}
		
		Set<Horario> atrasosNaoDuplicados = new HashSet<>(atrasos);
		List<Horario> elementosARemover = new ArrayList<>();
		
		for (Horario atrasoIterado : atrasos) {
			for (Horario atraso : atrasos) {
				if(isAfterMeioDia(atrasoIterado)) {
					if(!isAfterMeioDia(atraso)) {
						if (atrasoIterado.getFim().isAfter(atraso.getInicio())
								&& atraso.getInicio().isBefore(atrasoIterado.getFim())) {
							atrasosNaoDuplicados.add(new Horario(atraso.getInicio(), atrasoIterado.getFim()));
							elementosARemover.add(atrasoIterado);
							elementosARemover.add(atraso);
						}
					}
				} else if (!isAfterMeioDia(atraso) && atraso.getFim().isBefore(atrasoIterado.getFim())
						&& atrasoIterado.getInicio().isBefore(atraso.getFim())) {
					atrasosNaoDuplicados.add(new Horario(atrasoIterado.getInicio(), atraso.getFim()));
					elementosARemover.add(atrasoIterado);
					elementosARemover.add(atraso);
				}
			}
		}
		
		atrasosNaoDuplicados.removeAll(elementosARemover);

		List<Horario> atrasosIteradosFim = new ArrayList<>();
		for (Horario horario : atrasosNaoDuplicados) {
			boolean isDuplicate = false;
			for (Horario uniqueHorario : atrasosIteradosFim) {
				if (horario.getInicio().equals(uniqueHorario.getInicio())) {
					if (horario.getFim().isBefore(uniqueHorario.getFim())) {
						uniqueHorario.setFim(horario.getFim());
					}
					isDuplicate = true;
					break;
				}
			}
			if (!isDuplicate) {
				atrasosIteradosFim.add(horario);
			}
		}
		
		List<Horario> atrasosIteradosInicio = new ArrayList<>();

		for (Horario horario : atrasosIteradosFim) {
			boolean isDuplicate = false;
			for (Horario uniqueHorario : atrasosIteradosInicio) {
				if (horario.getFim().equals(uniqueHorario.getFim())) {
					if (horario.getInicio().isAfter(uniqueHorario.getInicio())) {
						uniqueHorario.setInicio(horario.getInicio());
					}
					isDuplicate = true;
					break;
				}
			}
			if (!isDuplicate) {
				atrasosIteradosInicio.add(horario);
			}
		}
	
		Collections.sort(atrasosIteradosInicio, Comparator.comparing(Horario::getInicio));

		return atrasosIteradosInicio;
	}

	@Override
	public List<Horario> calcularExtras(List<Horario> horarios, List<Horario> horariosTrabalhados) {
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

							if (horarioTrabalhado.getInicio().isBefore(horario.getInicio())) {
								horasExtras.add(new Horario(horarioTrabalhado.getInicio(), horario.getInicio()));
							}
							if (horarioTrabalhado.getFim().isAfter(horario.getFim())) {
								horasExtras.add(new Horario(horario.getFim(), horarioTrabalhado.getFim()));
							}
							horarioEncontrado = true;

						}

					} else {
						if (horarioTrabalhado.getInicio().isBefore(horario.getFim())) {
							horasExtras.add(new Horario(horario.getInicio(), horarioTrabalhado.getInicio()));
						}
						horarioEncontrado = true;
						if (horarioTrabalhado.getFim().isBefore(horario.getInicio())
								|| horarioTrabalhado.getFim().equals(horario.getInicio())) {
							horarioEncontrado = false;
						}
					}

				} else if (isAfterMeioDia(horarioTrabalhado)) {

					if ((horario.getInicio().isAfter(horarioTrabalhado.getInicio())
							|| horario.getInicio().equals(horarioTrabalhado.getInicio()))
							&& (horarioTrabalhado.getInicio().isAfter(horario.getInicio())
									|| horarioTrabalhado.getInicio().equals(horario.getInicio()))) {

						if (horarioTrabalhado.getInicio().isAfter(horario.getInicio())) {
							horasExtras.add(new Horario(horario.getInicio(), horarioTrabalhado.getInicio()));
						}
						if (horarioTrabalhado.getFim().isBefore(horario.getFim())) {
							horasExtras.add(new Horario(horarioTrabalhado.getFim(), horario.getFim()));
						}
						horarioEncontrado = true;
					}

				} else {

					if ((horario.getFim().isAfter(horarioTrabalhado.getInicio())
							|| horario.getFim().equals(horarioTrabalhado.getInicio()))
							&& (horarioTrabalhado.getFim().isAfter(horario.getInicio())
									|| horarioTrabalhado.getFim().equals(horario.getInicio()))) {

						if (horarioTrabalhado.getInicio().isBefore(horario.getInicio())) {
							horasExtras.add(new Horario(horarioTrabalhado.getInicio(), horario.getInicio()));
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

		List<Horario> extrasIterados = new ArrayList<>();

		for (Horario horario : horasExtras) {
			boolean isDuplicate = false;
			for (Horario uniqueHorario : extrasIterados) {
				if (horario.getInicio().equals(uniqueHorario.getInicio())) {
					if (horario.getFim().isBefore(uniqueHorario.getFim())) {
						uniqueHorario.setFim(horario.getFim());
					}
					isDuplicate = true;
					break;
				}
			}
			if (!isDuplicate) {
				extrasIterados.add(horario);
			}
		}

		List<Horario> extrasNaoDuplicados = new ArrayList<>();

		for (Horario horario : horarios) {
			for (Horario extra : extrasIterados) {
				if (isAfterMeioDia(extra)) {
					if (!(extra.getInicio().equals(horario.getInicio())
							|| extra.getInicio().isBefore(horario.getInicio()))
							|| !(extra.getFim().equals(horario.getFim()) || extra.getFim().isAfter(horario.getFim()))) {
						extrasNaoDuplicados.add(extra);
					}
				} else if (!(extra.getInicio().equals(horario.getInicio())
						|| extra.getInicio().isAfter(horario.getInicio()))
						|| !(extra.getFim().equals(horario.getFim()) || extra.getFim().isBefore(horario.getFim()))) {
					extrasNaoDuplicados.add(extra);
				}
			}
		}

		Set<Horario> limpaDuplicadas = new HashSet<>(extrasNaoDuplicados);
		extrasNaoDuplicados.clear();
		extrasNaoDuplicados.addAll(limpaDuplicadas);
		limpaDuplicadas.clear();

		List<Horario> extrasCorrigidos = new ArrayList<>(extrasNaoDuplicados);
		List<Horario> elementosARemover = new ArrayList<>();

		for (Horario extra : extrasNaoDuplicados) {
			for (Horario extraCorrigido : extrasNaoDuplicados) {
				if (extra.getFim().isBefore(extraCorrigido.getFim())
						&& extraCorrigido.getInicio().isBefore(extra.getFim())) {
					extrasCorrigidos.add(new Horario(extraCorrigido.getInicio(), extra.getFim()));
					elementosARemover.add(extra);
					elementosARemover.add(extraCorrigido);
				}
			}
		}

		extrasCorrigidos.removeAll(elementosARemover);
		elementosARemover.clear();

		for (Horario extra : extrasNaoDuplicados) {
			for (Horario extraCorrigido : extrasNaoDuplicados) {
				if (extra.getInicio().isBefore(extraCorrigido.getInicio())
						&& extra.getFim().equals(extraCorrigido.getFim())) {
					for (Horario horario : horarios) {
						if (extra.getInicio().equals(horario.getFim())) {
							int index = horarios.indexOf(horario);
							if (index < horarios.size() - 1) {
								Horario proximoHorario = horarios.get(index + 1);
								extrasCorrigidos.add(new Horario(horario.getFim(), proximoHorario.getInicio()));
								elementosARemover.add(extra);
							}
						}
					}
				}
			}
		}

		if (!elementosARemover.isEmpty()) {
			extrasCorrigidos.removeAll(elementosARemover);

			limpaDuplicadas.addAll(extrasCorrigidos);

			extrasCorrigidos.clear();
			extrasCorrigidos.addAll(limpaDuplicadas);
		}

		Collections.sort(extrasCorrigidos, Comparator.comparing(Horario::getInicio));

		return extrasCorrigidos;

	}

	private Boolean isAfterMeioDia(Horario horario) {
		LocalTime marcaMeioDia = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
		return horario.getInicio().isAfter(marcaMeioDia) && horario.getFim().isBefore(marcaMeioDia);
	}
}
