package com.zhevzhyk.connector;

import com.zhevzhyk.model.ExampleMessage;
import lombok.experimental.Delegate;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.schema.SchemaDefinition;

public class AmazingSchema implements Schema<ExampleMessage> {
  @Delegate
  private final Schema<ExampleMessage> schema = Schema.AVRO(SchemaDefinition.<ExampleMessage>builder()
      .withJsonDef(ExampleMessage.getClassSchema().toString()).build());
}
