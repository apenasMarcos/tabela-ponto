package br.com.tabelaponto.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.tabelaponto.model.Horario;
import br.com.tabelaponto.model.Periodo;
import br.com.tabelaponto.service.TabelaPontoService;
import br.com.tabelaponto.service.TabelaPontoServiceImpl;

@WebServlet("/url-da-sua-api")
public class TabelaPontoController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private TabelaPontoService tabelaPontoService = new TabelaPontoServiceImpl();

	public TabelaPontoController() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		String horarioJson = request.getParameter("horarios");
		String horariosTrabalhadosJson = request.getParameter("horariosTrabalhados");

		List<Horario> horarios = mapper.readValue(horarioJson, new TypeReference<List<Horario>>() {
		});
		List<Horario> horariosTrabalhados = mapper.readValue(horariosTrabalhadosJson,
				new TypeReference<List<Horario>>() {
				});

		Periodo periodo = tabelaPontoService.calcularMarcacoes(horarios, horariosTrabalhados);

		String atrasoJson = mapper.writeValueAsString(periodo.getAtrasoFormatado());
		String extraJson = mapper.writeValueAsString(periodo.getExtraFormatado());

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write("{\"atraso\": " + atrasoJson + ", \"extra\": " + extraJson + "}");

	}

}
