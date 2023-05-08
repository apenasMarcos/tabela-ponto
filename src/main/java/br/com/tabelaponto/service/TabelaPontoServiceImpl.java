package br.com.tabelaponto.service;

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

	    for (Horario intervaloHorario : horarios) {
	        boolean horarioTrabalhadoEncontrado = false;

	        for (Horario intervaloMarcacao : horariosTrabalhados) {
	        	
 	            if ((intervaloMarcacao.getFim().isAfter(intervaloHorario.getInicio()) ||
 	            		intervaloMarcacao.getFim().equals(intervaloHorario.getInicio())) &&
 	            		(intervaloHorario.getFim().isAfter(intervaloMarcacao.getInicio()) ||
 	            				intervaloHorario.getFim().equals(intervaloMarcacao.getInicio()))) {
	            	
	            	if (intervaloHorario.getInicio().isBefore(intervaloMarcacao.getInicio())) {

	                    atrasos.add(new Horario(intervaloHorario.getInicio(), intervaloMarcacao.getInicio()));
		                if (intervaloHorario.getFim().isAfter(intervaloMarcacao.getFim())) {

		                    atrasos.add(new Horario(intervaloMarcacao.getFim(), intervaloHorario.getFim()));
		                }
	                } else if (intervaloHorario.getFim().isAfter(intervaloMarcacao.getFim()) && 
	                		intervaloHorario.getInicio().isBefore(intervaloMarcacao.getFim()) &&
	                		!intervaloHorario.getInicio().equals(intervaloMarcacao.getFim())) {

		                    atrasos.add(new Horario(intervaloMarcacao.getFim(), intervaloHorario.getFim()));
		                }
	            	horarioTrabalhadoEncontrado = true;
	            } else if ((intervaloMarcacao.getInicio().isBefore(intervaloHorario.getInicio()) &&
 	            		intervaloMarcacao.getFim().equals(intervaloHorario.getFim())) &&
 	            		(intervaloHorario.getFim().isAfter(intervaloMarcacao.getInicio()) ||
 	            				intervaloHorario.getFim().equals(intervaloMarcacao.getInicio()))) {
	            	horarioTrabalhadoEncontrado = true;
	            }
	        }

	        if (!horarioTrabalhadoEncontrado) {
	            atrasos.add(intervaloHorario);
	        }
	        
	    }
	    
		List<Horario> atrasosIteradores = new ArrayList<>(atrasos);
		List<Horario> atrasosCorrigidos = new ArrayList<>(atrasos);
		
		int i = 0;
        for (Horario atrasoIterado : atrasosIteradores) {
	        for(Horario atraso : atrasos) {
	        	if(atraso.getFim().isBefore(atrasoIterado.getFim()) && atrasoIterado.getInicio().isBefore(atraso.getFim())) {
	        		try {
	        			atrasosCorrigidos.remove(i);
	        			atrasosCorrigidos.remove(i++);
	        		} catch (IndexOutOfBoundsException ignored) {
	        			
	        		}
	        		if(!atrasosIteradores.get(atrasosIteradores.size() - 1).getFim().isAfter(atraso.getFim())) {
	        			atrasosCorrigidos.add(new Horario(atrasoIterado.getInicio(), atraso.getFim()));
	        		}
	        		i--;
	        	} 
	        	if(atraso.getInicio().isBefore(atrasoIterado.getInicio()) && 
	        			atraso.getFim().isBefore(atrasoIterado.getFim()) &&
	        			atrasoIterado.getInicio().isBefore(atraso.getFim())) {
	        		atrasosCorrigidos.add(new Horario(atrasoIterado.getInicio(), atraso.getFim()));
	        	}
	        } i++;
        }
        return atrasosCorrigidos;
	}

	private List<Horario> calcularExtras(List<Horario> horarios, List<Horario> horariosTrabalhados) {
		List<Horario> horasExtras = new ArrayList<>();

	    for (Horario intervaloHorario : horarios) {
	        boolean horarioTrabalhadoEncontrado = false;

	        for (Horario intervaloMarcacao : horariosTrabalhados) {
	        	
	            if ((intervaloMarcacao.getInicio().isBefore(intervaloHorario.getFim()) || intervaloMarcacao.getInicio().equals(intervaloHorario.getFim())) && (intervaloHorario.getInicio().isBefore(intervaloMarcacao.getFim()) || intervaloHorario.getInicio().equals(intervaloMarcacao.getFim()))) {

	                if (intervaloHorario.getInicio().isAfter(intervaloMarcacao.getInicio())) {

	                	horasExtras.add(new Horario(intervaloMarcacao.getInicio(), intervaloHorario.getInicio()));
	                }

	                if (intervaloHorario.getFim().isBefore(intervaloMarcacao.getFim())) {

	                	horasExtras.add(new Horario(intervaloHorario.getFim(), intervaloMarcacao.getFim()));
	                }
	                horarioTrabalhadoEncontrado = true;
	            } else if(intervaloMarcacao.getInicio().isBefore(intervaloHorario.getInicio()) && intervaloMarcacao.getFim().isBefore(intervaloHorario.getInicio())) {
	            	horasExtras.add(new Horario(intervaloMarcacao.getInicio(), intervaloMarcacao.getFim()));
	            }
	        }

	        if (!horarioTrabalhadoEncontrado) {
	        	horasExtras.add(intervaloHorario);
	        }
	        
	    }
	    return horasExtras;
//	    List<Horario> extrasIteradores = new ArrayList<>(horasExtras);
//		List<Horario> extrasCorrigidos = new ArrayList<>(horasExtras);
//		
//		int i = 0;
//        for (Horario intervaloMarcacao : extrasIteradores) {
//	        for(Horario atraso : horasExtras) {
//	        	if(atraso.getFim().isBefore(intervaloMarcacao.getFim()) && intervaloMarcacao.getInicio().isBefore(atraso.getFim())) {
//	        		try {
//	        			extrasCorrigidos.remove(i);
//	        			extrasCorrigidos.remove(i++);
//	        		} catch (IndexOutOfBoundsException ignored) {
//	        			
//	        		}
//	        		if(!extrasIteradores.get(extrasIteradores.size() - 1).getFim().isAfter(atraso.getFim())) {
//	        			extrasCorrigidos.add(new Horario(intervaloMarcacao.getInicio(), atraso.getFim()));
//	        		}
//	        		i--;
//	        	}
//	        } i++;
//        }
//        return extrasCorrigidos;
    }
}
