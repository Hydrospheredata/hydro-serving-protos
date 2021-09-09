package io.hydrosphere.serving.proto.contract.tensor.conversions.json

import io.circe.Json
import io.hydrosphere.serving.proto.contract.tensor.definitions.DoubleTensor

object DoubleToJson extends TensorJsonLens[DoubleTensor] {
  override def convert = Json.fromDoubleOrNull
}
