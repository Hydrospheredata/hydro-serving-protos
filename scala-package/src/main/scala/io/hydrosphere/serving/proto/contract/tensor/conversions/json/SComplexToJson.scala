package io.hydrosphere.serving.proto.contract.tensor.conversions.json

import io.circe.Json
import io.hydrosphere.serving.proto.contract.tensor.definitions.SComplexTensor

object SComplexToJson extends TensorJsonLens[SComplexTensor] {
  override def convert = Json.fromFloatOrNull
}
