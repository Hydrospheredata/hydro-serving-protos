package io.hydrosphere.serving.proto.contract.errors

import io.hydrosphere.serving.proto.contract.field.ModelField
import io.hydrosphere.serving.proto.contract.signature.ModelSignature
import io.hydrosphere.serving.proto.contract.tensor.TensorShape
import io.hydrosphere.serving.proto.contract.types.DataType

sealed trait ValidationError extends Throwable

object ValidationError {

  final case class SignatureValidationError(
    suberrors: Seq[ValidationError],
    modelSignature: ModelSignature
  ) extends ValidationError

  final case class FieldMissingError(expectedField: String)
    extends ValidationError

  final case class NestedFieldValidationError(suberrors: Seq[ValidationError], field: ModelField)
    extends ValidationError

  final case class IncompatibleFieldTypeError(field: String, expectedType: DataType)
    extends ValidationError

  final case class UnsupportedFieldTypeError(expectedType: DataType)
    extends ValidationError

  final case class IncompatibleFieldShapeError(field: String, expectedShape: Option[TensorShape])
    extends ValidationError

  final case class InvalidFieldData[T](actualClass: Class[T])
    extends ValidationError

  final case class InvalidFieldValuesConversion(messages: Seq[String])
    extends ValidationError
}