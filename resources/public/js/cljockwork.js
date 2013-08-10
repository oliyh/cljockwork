function updateStatus() {
    $.get('/status', function (data) {
	$('#status').html(JSON.stringify(data));
    });
}

function stopScheduler() {
    $.post('/stop', updateStatus);
}

function startScheduler() {
    $.post('/start', updateStatus);
}

function formToJson() {
    return {desc: $('#desc').val(),
	    endpoint: $('#endpoint').val(),
	    schedule: $('#schedule').val()}
}

function removeTask(id) {
    $.ajax({
	url: '/tasks/remove/' + id,
	type: 'DELETE',
	contentType: 'application/json',
	success: populateTasks
    });
}

function populateTasks() {

    $.get('/tasks/')
	.done(function (data) {
	    $('#tasks').empty();
	    $.each(data, function(i, e) {
		var deleteButton = $('<button class="btn btn-danger btn-xs">DELETE</button>');
		var row = $('<tr/>')
		    .append($('<td/>').html(e.id))
		    .append($('<td/>').html(e.desc))
		    .append($('<td/>').html(e.endpoint))
		    .append($('<td/>').html(e.schedule))
		    .append($('<td/>').html(e.interval + 's'))
		    .append($('<td/>').append(deleteButton));

		deleteButton.click(function() {removeTask(e.id)});

		$('#tasks').append(row);
	    });
	})
	.always(function() {
	    setTimeout(populateTasks, 5000);
	});
}

function addTask() {
    $.ajax({
	url: '/tasks/add',
	type: 'PUT',
	contentType: 'application/json',
	data: JSON.stringify(formToJson()),
	success: populateTasks
    });
}

function validateTask() {
    $.ajax({
	url: '/tasks/validate',
	type: 'POST',
	contentType: 'application/json',
	data: JSON.stringify(formToJson()),
	success: function(data) {
	    for (var key in data) {
		if (data.hasOwnProperty(key)) {
		    $('#' + key + '-group').attr('class', data[key] ? 'has-success' : 'has-error');
		}
	    }
	}
    });
}

$(document).ready(function () {
    updateStatus();
    populateTasks();
});
