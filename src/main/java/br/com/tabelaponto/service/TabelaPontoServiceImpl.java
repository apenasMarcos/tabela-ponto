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

					} else if (horarioTrabalhado.getInicio().isBefore(horario.getFim())) {
							atrasos.add(new Horario(horario.getInicio(), horarioTrabalhado.getInicio()));
						} else if (horario.getInicio().equals(horarioTrabalhado.getInicio())
								&& horario.getFim().equals(horarioTrabalhado.getFim())) {
							horarioTrabalhadoEncontrado = true;
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

					}
				}

			}

			if (!horarioTrabalhadoEncontrado) {
				atrasos.add(horario);
			}
		}
		Set<Horario> atrasosIterados = new HashSet<>(atrasos);
		for (Horario atrasoIterado : atrasos) {
			for (Horario atraso : atrasos) {
				if (atraso.getFim().isBefore(atrasoIterado.getFim())
						&& atrasoIterado.getInicio().isBefore(atraso.getFim())) {
					atrasosIterados.add(new Horario(atrasoIterado.getInicio(), atraso.getFim()));
				}
			}
		}
		
		
		List<Horario> atrasosCorrigidos = new ArrayList<>();
		for (Horario horario : atrasosIterados) {
            boolean isDuplicate = false;
            for (Horario uniqueHorario : atrasosCorrigidos) {
                if (horario.getInicio().equals(uniqueHorario.getInicio())) {
                	if (horario.getFim().isBefore(uniqueHorario.getFim())) {
                		uniqueHorario.setFim(horario.getFim());
            		}
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
            	atrasosCorrigidos.add(horario);
            }
        }
		
		Collections.sort(atrasosCorrigidos, Comparator.comparing(Horario::getInicio));
		
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
		
		
		List<Horario> extrasIterados = new ArrayList<>(horasExtras);
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
		
		List<Horario> extrasCorrigidos = new ArrayList<>();
		
		for (Horario horario : horarios) {
			for(Horario extra : extrasIterados) {
				if (!(extra.getInicio().equals(horario.getInicio()) || extra.getInicio().isAfter(horario.getInicio())) ||
				        !(extra.getFim().equals(horario.getFim()) || extra.getFim().isBefore(horario.getFim()))) {
				    extrasCorrigidos.add(extra);
				}
			}
		}
		
        Set<Horario> conjunto = new HashSet<>(extrasCorrigidos);
        extrasCorrigidos.clear();
        extrasCorrigidos.addAll(conjunto);
        
		Collections.sort(extrasCorrigidos, Comparator.comparing(Horario::getInicio));

		return extrasCorrigidos;

	}

	private Boolean isAfterMeioDia(Horario horario) {
		LocalTime marcaMeioDia = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
		return horario.getInicio().isAfter(marcaMeioDia) && horario.getFim().isBefore(marcaMeioDia);
	}
}
