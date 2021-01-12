package io.hydrosphere.serving.proto.contract.tensor.conversions.json

import io.circe.Json
import io.hydrosphere.serving.proto.contract.tensor.definitions.FloatTensor

object FloatToJson extends TensorJsonLens[FloatTensor] {
  override def convert = Json.fromFloatOrNull
}
