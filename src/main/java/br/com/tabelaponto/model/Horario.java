package br.com.tabelaponto.model;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.tabelaponto.util.LocalTimeDeserializer;

public class Horario {

    @JsonProperty("entrada")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime inicio;

    @JsonProperty("saida")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime fim;
    
    
    public Horario() {
    	
    }
    
    public Horario(LocalTime inicio, LocalTime fim) {
		super();
		this.inicio = inicio;
		this.fim = fim;
	}
    
    public LocalTime getInicio() {
        return inicio;
    }

    public LocalTime getFim() {
        return fim;
    }

	public void setInicio(LocalTime inicio) {
		this.inicio = inicio;
	}

	public void setFim(LocalTime fim) {
		this.fim = fim;
	}
    
    

}

