package io.hydrosphere.serving.proto.contract.tensor.conversions.json

import io.circe.Json.JObject
import io.circe.{Json, JsonObject}
import io.hydrosphere.serving.proto.contract.tensor.definitions.MapTensor

object MapToJson extends TensorJsonLens[MapTensor] {
  override def convert = x =>
    Json.fromFields(x.mapValues(TensorJsonLens.toJson))
}
