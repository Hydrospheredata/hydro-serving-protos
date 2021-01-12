package io.hydrosphere.serving.proto.contract.tensor.conversions.json

import io.circe.Json
import io.hydrosphere.serving.proto.contract.tensor.definitions.StringTensor

object StringToJson extends TensorJsonLens[StringTensor] {
  override def convert = Json.fromString
}
