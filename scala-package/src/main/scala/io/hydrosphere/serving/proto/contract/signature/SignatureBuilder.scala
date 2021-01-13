package io.hydrosphere.serving.proto.contract.signature

import io.hydrosphere.serving.proto.contract.field.ModelField
import io.hydrosphere.serving.proto.contract.tensor.TensorShape
import io.hydrosphere.serving.proto.contract.tensor.definitions.Shape
import io.hydrosphere.serving.proto.contract.types.{DataProfileType, DataType}

object SignatureBuilder {

  def nestedField(
    name: String,
    shape: Option[TensorShape],
    subFields: Seq[ModelField],
    profile: DataProfileType = DataProfileType.NONE): ModelField = {
    ModelField(
      name,
      shape,
      ModelField.TypeOrSubfields.Subfields(
        ModelField.Subfield(
          subFields
        )
      ),
      profile
    )
  }

  def rawTensorModelField(
    name: String,
    dataType: DataType,
    shape: Option[TensorShape],
    profile: DataProfileType = DataProfileType.NONE): ModelField = {
    ModelField(
      name,
      shape,
      ModelField.TypeOrSubfields.Dtype(dataType),
      profile
    )
  }

  def simpleTensorModelField(
    name: String,
    dataType: DataType,
    shape: Shape,
    profile: DataProfileType = DataProfileType.NONE): ModelField = {
    ModelField(
      name,
      shape.toProto,
      ModelField.TypeOrSubfields.Dtype(dataType),
      profile
    )
  }
}