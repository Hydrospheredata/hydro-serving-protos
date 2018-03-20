package io.hydrosphere.serving.contract.utils.description

import io.hydrosphere.serving.tensorflow.types.DataType

case class FieldDescription(
  fieldName: String,
  dataType: DataType,
  shape: Option[Seq[Long]]
)
