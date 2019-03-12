package io.hydrosphere.serving.model.api

import io.hydrosphere.serving.contract.model_contract.ModelContract
import io.hydrosphere.serving.contract.model_field.ModelField
import io.hydrosphere.serving.contract.model_signature.ModelSignature
import io.hydrosphere.serving.tensorflow.tensor_shape.TensorShapeProto
import io.hydrosphere.serving.tensorflow.types.DataType

sealed trait ValidationError extends Throwable

object ValidationError {

  final case class SignatureMissingError(expectedSignature: String, modelContract: ModelContract) extends ValidationError

  final case class SignatureValidationError(
    suberrors: Seq[ValidationError],
    modelSignature: ModelSignature
  ) extends ValidationError

  final case class FieldMissingError(expectedField: String)
    extends ValidationError

  final case class ComplexFieldValidationError(suberrors: Seq[ValidationError], field: ModelField)
    extends ValidationError

  final case class IncompatibleFieldTypeError(field: String, expectedType: DataType)
    extends ValidationError

  final case class UnsupportedFieldTypeError(expectedType: DataType)
    extends ValidationError

  final case class IncompatibleFieldShapeError(field: String, expectedShape: Option[TensorShapeProto])
    extends ValidationError

  final case class InvalidFieldData[T](actualClass: Class[T])
    extends ValidationError

}