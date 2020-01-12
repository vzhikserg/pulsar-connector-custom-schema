# Description 

This project shows a problem when trying to use a custom schema in the Pulsar source connector.

You can reproduce the issue following the instructions below.

## Generate nar file

The connector is packaged as nar file using the following command:

```
mvn clean package
```

## Start container

Run a standalone instance of Pulsar:

```
docker run -d -it -p 6650:6650 -p 8080:8080 --name pulsar apachepulsar/pulsar:2.4.2 bin/pulsar standalone
```

## Copy nar

The created nar file is copied to the running container:

```
docker cp target/pulsar-connector-localrun-vs-create-1.0.0-SNAPSHOT.nar pulsar:/pulsar/
```

## Connect to the running container

```
docker exec -it pulsar /bin/bash
```

## Start the connector with a custom schema

```
./bin/pulsar-admin sources create --archive pulsar-connector-localrun-vs-create-1.0.0-SNAPSHOT.nar --tenant public --namespace default --destination-topic-name example1 --name pulsar-source-connector1 -st avro
```