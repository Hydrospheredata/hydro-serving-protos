package io.hydrosphere.serving.proto.contract.tensor.builders

import io.circe.Json
import io.hydrosphere.serving.proto.contract.errors.ValidationError
import io.hydrosphere.serving.proto.contract.errors.ValidationError.SignatureValidationError
import io.hydrosphere.serving.proto.contract.field.ModelField
import io.hydrosphere.serving.proto.contract.signature.ModelSignature
import io.hydrosphere.serving.proto.contract.tensor.definitions.TypedTensor
import io.hydrosphere.serving.proto.contract.types.DataProfileType

class SignatureBuilder(val signature: ModelSignature) {
  def convert(data: Json): Either[ValidationError, Map[String, TypedTensor[_]]] = {
    // rootField is a virtual field that aggregates all request inputs
    val rootField = ModelField(
      "root",
      None,
      ModelField.TypeOrSubfields.Subfields(
        ModelField.Subfield(
          signature.inputs
        )
      ),
      DataProfileType.NONE
    )

    val nestedFieldBuilder = new NestedFieldBuilder(rootField, signature.inputs)
    nestedFieldBuilder.convert(data) match {
      case Left(errors) =>
        Left(SignatureValidationError(errors, signature))
      case Right(tensor) =>
        Right(tensor.data.head)
    }
  }
}