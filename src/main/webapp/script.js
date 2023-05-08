$(document).ready(function() {
  // Aplica a máscara nos campos de entrada de horário
  applyHorarioMask();

  // Valida os campos de entrada de horário ao perder o foco
  validateHorarioInput();

  // Adiciona uma nova linha na tabela de marcações
  addMarcacoesTableRow();

  // Remove uma linha da tabela de marcações
  removeMarcacoesTableRow();

  // Envia os dados da tabela de horários e marcações para a API
  sendHorariosData();
});

function applyHorarioMask() {
  $('.input-horario').mask('00:00', {reverse: true});
}

function validateHorarioInput() {
  $(".input-horario").blur(function() {
    var value = $(this).val();
    if (value !== '' && !/^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/.test(value)) {
      $('#modal-erro').modal('show');
      $(this).val("");
    }
  });
}

function addMarcacoesTableRow() {
  $("#marcacoes-table").on("click", ".btn-add-horario", function() {
    var newRow = $("<tr>");
    var cols = "";
    cols += '<td><input type="text" class="form-control input-horario" placeholder="HH:mm"></td>';
    cols += '<td><input type="text" class="form-control input-horario" placeholder="HH:mm"></td>';
    cols += '<td><button class="btn btn-primary btn-sm btn-add-horario">Adicionar</button>';
    cols += '<button class="btn btn btn-outline-danger btn-sm btn-remover-horario">Remover</button></td>';
    newRow.append(cols);

	$(".input-horario", newRow).mask('00:00', {reverse: true});
	
	$(".input-horario", newRow).blur(function() {
	  var value = $(this).val();
	  if (value !== '' && !/^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/.test(value)) {
	    $('#modal-erro').modal('show');
	    $(this).val("");
	  }
	});
	

    validateHorarioInput.call($(".input-horario", newRow));

    $("#marcacoes-table tbody").append(newRow);
    $(".btn-add-horario:last").hide();
  });
}

function removeMarcacoesTableRow() {
  $("#marcacoes-table").on("click", ".btn-remover-horario", function() {
    $(this).closest('tr').remove();
    if ($("#marcacoes-table tbody tr").length == 1) {
      $(".btn-remover-horario").remove();
      $(".btn-add-horario").show();
    }
  });
}
    
function sendHorariosData() {
	$('#enviar').on('click', function() {
		var horarioTable = [];
		$('#horario-table tbody tr').each(function() {
		    var entrada = $(this).find('td:eq(0) input').val();
		    var saida = $(this).find('td:eq(1) input').val();
		    if (entrada !== '' && saida !== '') {
		        horarioTable.push({
		            entrada: entrada,
		            saida: saida
		        });
		    }
		});
		var marcacoesTable = [];
		$('#marcacoes-table tbody tr').each(function() {
			var entrada = $(this).find('td:eq(0) input').val();
		    var saida = $(this).find('td:eq(1) input').val();
		    if (entrada !== '' && saida !== '') {
		        marcacoesTable.push({
		            entrada: entrada,
		            saida: saida
		        });
		    }
		});
    	$.ajax({
      	url: 'url-da-sua-api',
      	type: 'POST',
      	data: {
			horarios: JSON.stringify(horarioTable),
			horariosTrabalhados: JSON.stringify(marcacoesTable)
		},
		success: function(response) {
		  var atrasos = response.atraso;
		  var extras = response.extra;
		  $('#atrasos').text('Atrasos: ' + atrasos);
		  $('#extras').text('Extras: ' + extras);
		  $('#meuModal').modal('show');
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert('Ocorreu um erro ao enviar os dados.');
		}
	});
  });
}