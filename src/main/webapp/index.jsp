<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tabela de Ponto</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.16/jquery.mask.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>

</head>
<body>
	<div class="container">

		<div class="modal fade" id="modal-erro" tabindex="-1" role="dialog"
			aria-labelledby="modal-erro-titulo" aria-hidden="true">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="modal-erro-titulo">Horário
							inválido!</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Fechar">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">Digite um horário válido.</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-dismiss="modal">Fechar</button>
					</div>
				</div>
			</div>
		</div>

		<h2>Horário de Trabalho</h2>
		<table id="horario-table" class="table">
			<thead>
				<tr>
					<th>Entrada</th>
					<th>Saída</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><input type="text" class="form-control input-horario"
						placeholder="HH:mm"></td>
					<td><input type="text" class="form-control input-horario"
						placeholder="HH:mm"></td>
				</tr>
				<tr>
					<td><input type="text" class="form-control input-horario"
						placeholder="HH:mm"></td>
					<td><input type="text" class="form-control input-horario"
						placeholder="HH:mm"></td>
				</tr>
				<tr>
					<td><input type="text" class="form-control input-horario"
						placeholder="HH:mm"></td>
					<td><input type="text" class="form-control input-horario"
						placeholder="HH:mm"></td>
				</tr>
			</tbody>
		</table>

		<h2>Marcações Feitas</h2>
		<table id="marcacoes-table" class="table">
			<thead>
				<tr>
					<th>Entrada</th>
					<th>Saída</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><input type="text" class="form-control input-horario"
						placeholder="HH:mm"></td>
					<td><input type="text" class="form-control input-horario"
						placeholder="HH:mm"></td>
					<td><button class="btn btn-outline-primary btn-sm btn-add-horario">Adicionar</button>
				</tr>
			</tbody>
		</table>

		<button id="enviar" class="btn btn-outline-primary">Enviar Horários</button>
		
		<button type="button" class="btn btn-outline-secondary" data-toggle="modal" data-target="#meuModal">Resultado atrasos e extras</button>
		
		<button id="recarregar" type="button" class="btn btn-outline-danger" data-toggle="modal" data-target="" onclick="location.reload()">Limpar Dados</button>
				
		<div class="modal fade" id="meuModal" tabindex="-1" role="dialog" aria-labelledby="meuModalLabel" aria-hidden="true">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="meuModalLabel">Tabela Atrasos e Extras</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Fechar">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        <p id="atrasos"></p>
		        <p id="extras"></p>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
		      </div>
		    </div>
		  </div>
		</div>
	</div>
	
	

	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.16/jquery.mask.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js"></script>
	<script src="script.js"></script>
</body>
</html>