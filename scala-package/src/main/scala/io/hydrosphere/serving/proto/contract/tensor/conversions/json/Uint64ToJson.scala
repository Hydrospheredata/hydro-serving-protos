package io.hydrosphere.serving.proto.contract.tensor.conversions.json

import io.circe.Json.JNumber
import io.circe.{Json, JsonNumber}
import io.hydrosphere.serving.proto.contract.tensor.definitions.Uint64Tensor

object Uint64ToJson extends TensorJsonLens[Uint64Tensor] {
  override def convert = (value: Long) =>
    Json.fromLong(value)
}
