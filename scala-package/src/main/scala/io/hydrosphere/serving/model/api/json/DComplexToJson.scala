package io.hydrosphere.serving.model.api.json

import io.hydrosphere.serving.tensorflow.tensor.DComplexTensor
import spray.json.{JsNumber, JsValue}

object DComplexToJson extends TensorJsonLens[DComplexTensor] {
  override def convert: Double => JsValue = JsNumber.apply
}
