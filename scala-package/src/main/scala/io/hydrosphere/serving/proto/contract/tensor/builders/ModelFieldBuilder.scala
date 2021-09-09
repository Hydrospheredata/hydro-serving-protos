package io.hydrosphere.serving.proto.contract.tensor.builders

import io.circe.{HCursor, Json}
import io.hydrosphere.serving.proto.contract.errors.ValidationError
import io.hydrosphere.serving.proto.contract.errors.ValidationError.{FieldMissingError, NestedFieldValidationError}
import io.hydrosphere.serving.proto.contract.field.ModelField
import io.hydrosphere.serving.proto.contract.field.ModelField.TypeOrSubfields.{Dtype, Empty, Subfields}
import io.hydrosphere.serving.proto.contract.tensor.definitions.TypedTensor

class ModelFieldBuilder(val modelField: ModelField) {
  def convert(data: HCursor): Either[ValidationError, TypedTensor[_]] = {
    modelField.typeOrSubfields match {
      case Empty => Left(FieldMissingError(modelField.name))

      case Subfields(subfields) =>
        val nestedFieldBuilder = new NestedFieldBuilder(modelField, subfields.data)
        nestedFieldBuilder.convert(data.focus.getOrElse(Json.Null)).left.map { errors =>
          NestedFieldValidationError(errors, modelField)
        }

      case Dtype(value) =>
        val infoFieldValidator = new InfoFieldBuilder(modelField, value)
        infoFieldValidator.convert(Seq(data.focus.getOrElse(Json.Null)))
    }
  }
}
