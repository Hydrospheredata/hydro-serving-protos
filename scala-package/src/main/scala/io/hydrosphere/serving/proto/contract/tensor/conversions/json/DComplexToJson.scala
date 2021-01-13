package io.hydrosphere.serving.proto.contract.tensor.conversions.json

import io.circe.Json
import io.hydrosphere.serving.proto.contract.tensor.definitions.DComplexTensor

object DComplexToJson extends TensorJsonLens[DComplexTensor] {
  override def convert = Json.fromDoubleOrNull
}
