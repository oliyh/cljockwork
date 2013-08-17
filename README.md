# cljockwork

A REST API for cron4j, written in Clojure.
Live demo at http://cljockwork.oliy.co.uk

### Getting started

    git clone https://github.com/oliyh/cljockwork.git
    lein run

Open a browser and visit http://localhost:8080 for a demo

### API

Basic scheduler control

    GET http://localhost:8080/status
    POST http://localhost:8080/stop
    POST http://localhost:8080/start

Viewing tasks

	GET http://localhost:8080/tasks/
	GET http://localhost:8080/tasks/:id

Adding, removing and validating tasks

	POST http://localhost:8080/tasks/validate {:desc "Task description" :schedule "* * * * *" :endpoint "http://path/to/endpoint"}
	PUT http://localhost:8080/tasks/add {:desc "Task description" :schedule "* * * * *" :endpoint "http://path/to/endpoint"}
	DELETE http://localhost:8080/tasks/:id/remove

### License

Distributed under the Eclipse Public License, the same as Clojure.
