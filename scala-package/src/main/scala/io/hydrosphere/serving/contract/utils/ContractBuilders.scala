package io.hydrosphere.serving.contract.utils

import io.hydrosphere.serving.contract.model_field.ModelField
import io.hydrosphere.serving.tensorflow.TensorShape
import io.hydrosphere.serving.tensorflow.tensor_shape.TensorShapeProto
import io.hydrosphere.serving.tensorflow.types.DataType

object ContractBuilders {
  def complexField(
    name: String,
    shape: Option[TensorShapeProto],
    subFields: Seq[ModelField]): ModelField = {
    ModelField(
      name,
      shape,
      ModelField.TypeOrSubfields.Subfields(
        ModelField.Subfield(
          subFields
        )
      )
    )
  }

  def rawTensorModelField(
    name: String,
    dataType: DataType,
    shape: Option[TensorShapeProto]): ModelField = {
    ModelField(name, shape, ModelField.TypeOrSubfields.Dtype(dataType))
  }

  def simpleTensorModelField(
    name: String,
    dataType: DataType,
    shape: TensorShape): ModelField = {
    ModelField(
      name,
      shape.toProto,
      ModelField.TypeOrSubfields.Dtype(dataType)
    )
  }
}