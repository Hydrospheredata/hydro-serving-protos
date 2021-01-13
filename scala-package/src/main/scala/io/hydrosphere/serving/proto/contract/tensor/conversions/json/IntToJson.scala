package io.hydrosphere.serving.proto.contract.tensor.conversions.json

import io.circe.Json.JNumber
import io.circe.{Json, JsonNumber}
import io.hydrosphere.serving.proto.contract.tensor.definitions.IntTensor

object IntToJson extends TensorJsonLens[IntTensor[_]] {
  override def convert = value =>
    Json.fromString(value.toString)
}
