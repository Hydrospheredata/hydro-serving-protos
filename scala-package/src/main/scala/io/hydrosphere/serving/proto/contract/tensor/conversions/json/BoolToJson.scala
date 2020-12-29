package io.hydrosphere.serving.proto.contract.tensor.conversions.json

import io.circe.Json
import io.hydrosphere.serving.proto.contract.tensor.definitions.BoolTensor

object BoolToJson extends TensorJsonLens[BoolTensor] {
  override def convert = Json.fromBoolean
}
