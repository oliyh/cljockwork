function updateStatus() {
    $.get('/status', function (data) {
	$('#status').html(JSON.stringify(data));
    });
}

function stopScheduler() {
    $.post('/stop', updateStatus);
    $('#stopButton').addClass('hidden');
    $('#startButton').removeClass('hidden');
}

function startScheduler() {
    $.post('/start', updateStatus);
    $('#startButton').addClass('hidden');
    $('#stopButton').removeClass('hidden');
}

function formToJson() {
    return {desc: $('#desc').val(),
	    endpoint: $('#endpoint').val(),
	    schedule: $('#schedule').val()}
}

function removeTask(id) {
    $.ajax({
	url: '/tasks/' + id + '/remove',
	type: 'DELETE',
	contentType: 'application/json',
	success: function() { populateTasks(false); }
    });
}

function changeTaskState(id, state) {
    $.ajax({
	url: '/tasks/' + id + '/' + state,
	type: 'POST',
	contentType: 'application/json',
	success: function() { populateTasks(false); }
    });
}

function populateEvents() {
    $.get('/events/')
	.done(function (data) {
	    $('#events').empty();
	    $.each(data, function(i, e) {
		var row = $('<tr/>')
		    .append($('<td/>').html(e.id))
		    .append($('<td/>').html(e.time));
		$('#events').append(row);
	    });
	})
	.always(function() {
	    setTimeout(populateEvents, 5000);
	});
}

function populateTasks(continuous) {
    $.get('/tasks/')
	.done(function (data) {
	    $('#tasks').empty();
	    $.each(data, function(i, e) {
		var deleteButton = $('<button class="btn btn-danger btn-xs">DELETE</button>')
		    .click(function() {removeTask(e.id)});

		var pauseButton = $('<button class="btn btn-xs"></button>');
		if (e.state == 'paused') {
		    pauseButton.text('UNPAUSE')
			.addClass('btn-warning')
			.click(function() {changeTaskState(e.id, 'activate')});;
		} else {
		    pauseButton.text('PAUSE')
			.click(function() {changeTaskState(e.id, 'pause')});
		}

		var row = $('<tr/>')
		    .append($('<td/>').html(e.id))
		    .append($('<td/>').html(e.desc))
		    .append($('<td/>').html(e.endpoint))
		    .append($('<td/>').html(e.schedule))
		    .append($('<td/>').html(e.interval + 's'))
		    .append($('<td/>').append(pauseButton))
		    .append($('<td/>').append(deleteButton));

		$('#tasks').append(row);
	    });
	})
	.always(function() {
	    if (continuous) {
		setTimeout(function() { populateTasks(true) }, 5000);
	    }
	});
}

function addTask() {
    $.ajax({
	url: '/tasks/add',
	type: 'PUT',
	contentType: 'application/json',
	data: JSON.stringify(formToJson()),
	success: function() { populateTasks(false); }
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
    populateTasks(true);
    populateEvents();
});
