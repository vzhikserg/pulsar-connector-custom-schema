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
docker cp target/pulsar-connector-custom-schema-1.0.0-SNAPSHOT.nar pulsar:/pulsar/
```

## Connect to the running container

```
docker exec -it pulsar /bin/bash
```

## Start the connector with a custom schema

```
./bin/pulsar-admin sources create --archive pulsar-connector-custom-schema-1.0.0-SNAPSHOT.nar --tenant public --namespace default --destination-topic-name example1 --name pulsar-source-connector1 -st com.zhevzhyk.connector.AmazingSchema
```

Output

```
"Created successfully"
```

## Exception

The connector was not working and I found the following exception in the logs:

```
09:21:30.276 [public/default/pulsar-source-connector1-0] INFO  org.apache.pulsar.functions.sink.PulsarSink - Opening pulsar sink with config: PulsarSinkConfig(processingGuarantees=ATLEAST_ONCE, topic=example1, serdeClassName=null, schemaType=com.zhevzhyk.connector.
AmazingSchema, typeClassName=com.zhevzhyk.model.ExampleMessage)
09:21:30.287 [public/default/pulsar-source-connector1-0] ERROR org.apache.pulsar.functions.instance.JavaInstanceRunnable - Sink open produced uncaught exception:
java.lang.RuntimeException: User class must be in class path
        at org.apache.pulsar.functions.utils.Reflections.createInstance(Reflections.java:67) ~[org.apache.pulsar-pulsar-functions-utils-2.4.2.jar:2.4.2]
        at org.apache.pulsar.functions.instance.InstanceUtils.createInstance(InstanceUtils.java:84) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:?]
        at org.apache.pulsar.functions.instance.InstanceUtils.initializeSerDe(InstanceUtils.java:43) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:?]
        at org.apache.pulsar.functions.source.TopicSchema.newSchemaInstance(TopicSchema.java:202) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:2.4.2]
        at org.apache.pulsar.functions.source.TopicSchema.newSchemaInstance(TopicSchema.java:210) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:2.4.2]
        at org.apache.pulsar.functions.source.TopicSchema.lambda$getSchema$0(TopicSchema.java:66) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:2.4.2]
        at java.util.HashMap.computeIfAbsent(HashMap.java:1127) ~[?:1.8.0_232]
        at org.apache.pulsar.functions.source.TopicSchema.getSchema(TopicSchema.java:66) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:2.4.2]
        at org.apache.pulsar.functions.sink.PulsarSink.initializeSchema(PulsarSink.java:327) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:2.4.2]
        at org.apache.pulsar.functions.sink.PulsarSink.open(PulsarSink.java:255) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:2.4.2]
        at org.apache.pulsar.functions.instance.JavaInstanceRunnable.setupOutput(JavaInstanceRunnable.java:788) [org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:?]
        at org.apache.pulsar.functions.instance.JavaInstanceRunnable.setupJavaInstance(JavaInstanceRunnable.java:213) [org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:?]
        at org.apache.pulsar.functions.instance.JavaInstanceRunnable.run(JavaInstanceRunnable.java:244) [org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:?]
        at java.lang.Thread.run(Thread.java:748) [?:1.8.0_232]
Caused by: java.lang.ClassNotFoundException: com.zhevzhyk.connector.AmazingSchema
        at java.net.URLClassLoader.findClass(URLClassLoader.java:382) ~[?:1.8.0_232]
        at java.lang.ClassLoader.loadClass(ClassLoader.java:418) ~[?:1.8.0_232]
        at java.lang.ClassLoader.loadClass(ClassLoader.java:351) ~[?:1.8.0_232]
        at java.lang.Class.forName0(Native Method) ~[?:1.8.0_232]
        at java.lang.Class.forName(Class.java:348) ~[?:1.8.0_232]
        at org.apache.pulsar.functions.utils.Reflections.createInstance(Reflections.java:65) ~[org.apache.pulsar-pulsar-functions-utils-2.4.2.jar:2.4.2]
        ... 13 more
09:21:30.296 [public/default/pulsar-source-connector1-0] ERROR org.apache.pulsar.functions.instance.JavaInstanceRunnable - [public/default/pulsar-source-connector1:0] Uncaught exception in Java Instance
java.lang.RuntimeException: User class must be in class path
        at org.apache.pulsar.functions.utils.Reflections.createInstance(Reflections.java:67) ~[org.apache.pulsar-pulsar-functions-utils-2.4.2.jar:2.4.2]
        at org.apache.pulsar.functions.instance.InstanceUtils.createInstance(InstanceUtils.java:84) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:?]
        at org.apache.pulsar.functions.instance.InstanceUtils.initializeSerDe(InstanceUtils.java:43) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:?]
        at org.apache.pulsar.functions.source.TopicSchema.newSchemaInstance(TopicSchema.java:202) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:2.4.2]
        at org.apache.pulsar.functions.source.TopicSchema.newSchemaInstance(TopicSchema.java:210) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:2.4.2]
        at org.apache.pulsar.functions.source.TopicSchema.lambda$getSchema$0(TopicSchema.java:66) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:2.4.2]
        at java.util.HashMap.computeIfAbsent(HashMap.java:1127) ~[?:1.8.0_232]
        at org.apache.pulsar.functions.source.TopicSchema.getSchema(TopicSchema.java:66) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:2.4.2]
        at org.apache.pulsar.functions.sink.PulsarSink.initializeSchema(PulsarSink.java:327) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:2.4.2]
        at org.apache.pulsar.functions.sink.PulsarSink.open(PulsarSink.java:255) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:2.4.2]
        at org.apache.pulsar.functions.instance.JavaInstanceRunnable.setupOutput(JavaInstanceRunnable.java:788) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:?]
        at org.apache.pulsar.functions.instance.JavaInstanceRunnable.setupJavaInstance(JavaInstanceRunnable.java:213) ~[org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:?]
        at org.apache.pulsar.functions.instance.JavaInstanceRunnable.run(JavaInstanceRunnable.java:244) [org.apache.pulsar-pulsar-functions-instance-2.4.2.jar:?]
        at java.lang.Thread.run(Thread.java:748) [?:1.8.0_232]
Caused by: java.lang.ClassNotFoundException: com.zhevzhyk.connector.AmazingSchema
        at java.net.URLClassLoader.findClass(URLClassLoader.java:382) ~[?:1.8.0_232]
        at java.lang.ClassLoader.loadClass(ClassLoader.java:418) ~[?:1.8.0_232]
        at java.lang.ClassLoader.loadClass(ClassLoader.java:351) ~[?:1.8.0_232]
        at java.lang.Class.forName0(Native Method) ~[?:1.8.0_232]
        at java.lang.Class.forName(Class.java:348) ~[?:1.8.0_232]
        at org.apache.pulsar.functions.utils.Reflections.createInstance(Reflections.java:65) ~[org.apache.pulsar-pulsar-functions-utils-2.4.2.jar:2.4.2]
        ... 13 more
09:21:30.305 [public/default/pulsar-source-connector1-0] INFO  org.apache.pulsar.functions.instance.JavaInstanceRunnable - Closing instance
```
