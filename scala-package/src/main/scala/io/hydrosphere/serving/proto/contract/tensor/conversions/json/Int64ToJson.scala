package io.hydrosphere.serving.proto.contract.tensor.conversions.json

import io.circe.Json
import io.hydrosphere.serving.proto.contract.tensor.definitions.Int64Tensor

object Int64ToJson extends TensorJsonLens[Int64Tensor] {
  override def convert = Json.fromLong
}
