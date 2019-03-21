package io.hydrosphere.serving.contract.utils

import io.hydrosphere.serving.contract.model_field.ModelField
import io.hydrosphere.serving.manager.data_profile_types.DataProfileType
import io.hydrosphere.serving.tensorflow.TensorShape
import io.hydrosphere.serving.tensorflow.tensor_shape.TensorShapeProto
import io.hydrosphere.serving.tensorflow.types.DataType

object ContractBuilders {
  def complexField(
    name: String,
    shape: Option[TensorShapeProto],
    subFields: Seq[ModelField],
    profile: DataProfileType = DataProfileType.NONE): ModelField = {
    ModelField(
      name,
      shape,
      profile,
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
    shape: Option[TensorShapeProto],
    profile: DataProfileType = DataProfileType.NONE): ModelField = {
    ModelField(name, shape, profile, ModelField.TypeOrSubfields.Dtype(dataType))
  }

  def simpleTensorModelField(
    name: String,
    dataType: DataType,
    shape: TensorShape,
    profile: DataProfileType = DataProfileType.NONE): ModelField = {
    ModelField(
      name,
      shape.toProto,
      profile,
      ModelField.TypeOrSubfields.Dtype(dataType)
    )
  }
}