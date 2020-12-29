package io.hydrosphere.serving.proto.contract.tensor.builders

import io.circe.{ACursor, HCursor, Json}
import io.hydrosphere.serving.proto.contract.errors.ValidationError
import io.hydrosphere.serving.proto.contract.errors.ValidationError.{FieldMissingError, IncompatibleFieldTypeError}
import io.hydrosphere.serving.proto.contract.field.ModelField
import io.hydrosphere.serving.proto.contract.tensor.definitions.{MapTensor, Shape, TypedTensor}
import io.hydrosphere.serving.proto.contract.types.DataType

import scala.annotation.tailrec

class NestedFieldBuilder(val modelField: ModelField, val subfields: Seq[ModelField]) {
  type ConvertResult = Either[Seq[ValidationError], MapTensor]

  def convert(data: Json): ConvertResult = {
    val cursor: HCursor = data.hcursor
    if (data.isObject) {
      val convertions: Seq[Either[ValidationError, (String, TypedTensor[_])]] =
        subfields.map { field =>
          cursor.downField(field.name).success match {
            case Some(hcursor: HCursor) =>
              new ModelFieldBuilder(field).convert(hcursor) match {
                case Right(tensor: TypedTensor[_]) => Right(field.name -> tensor)
                case Left(x) => Left(x)
              }
            case None => Left(FieldMissingError(field.name))
          }
        }
      val (errors, tensors) = convertions.partitionMap(identity)
      if (errors.nonEmpty) {
        Left(errors)
      } else {
        Right(MapTensor(Shape(modelField.shape), Seq(tensors.toMap)))
      }
    } else if (data.isArray) {
      @tailrec
      def loop(cursor: ACursor, acc: List[ConvertResult]): List[ConvertResult] = cursor.focus match {
        case Some(json) => loop(cursor.right, acc :+ convert(json))
        case None => Nil
      }
      val (errors, tensors) = loop(cursor.downArray, List()).partitionMap(identity)
      if (errors.nonEmpty) {
        Left(errors.flatten)
      } else {
        Right(MapTensor(Shape(modelField.shape), tensors.map(_.data.head)))
      }
    } else if (data.isNull) {
      Left(Seq(FieldMissingError(modelField.name)))
    } else {
      Left(Seq(IncompatibleFieldTypeError(modelField.name, DataType.DT_MAP)))
    }
  }
}
