package io.hydrosphere.serving.proto.contract.tensor.builders

import io.circe.Decoder.Result
import io.circe.{DecodingFailure, Json}
import io.circe.optics.JsonOptics._
import io.hydrosphere.serving.proto.contract.errors.ValidationError
import io.hydrosphere.serving.proto.contract.errors.ValidationError.InvalidFieldValuesConversion
import io.hydrosphere.serving.proto.contract.field.ModelField
import io.hydrosphere.serving.proto.contract.tensor.definitions.{TypedTensor, TypedTensorFactory}
import io.hydrosphere.serving.proto.contract.types.DataType
import io.hydrosphere.serving.proto.contract.tensor.definitions._



class InfoFieldBuilder(val field: ModelField, val dataType: DataType) {

  def convert(data: Seq[Json]): Either[ValidationError, TypedTensor[_]] = {
    val flattened: Seq[Json] = field.shape match {
      case Some(_) => flatten(data)
      case None => data
    }
    val factory = TypedTensorFactory(dataType)
    val convertedData: Seq[Result[Any]] = factory match {
      case FloatTensor | SComplexTensor => flattened.map(_.as[Float])
      case DoubleTensor | DComplexTensor => flattened.map(_.as[Double])
      case Uint64Tensor | Int64Tensor => flattened.map(_.as[Long])
      case Int8Tensor | Uint8Tensor |
           Int16Tensor | Uint16Tensor |
           Int32Tensor | Uint32Tensor => flattened.map(_.as[Int])
      case StringTensor => flattened.map(_.as[String])
      case BoolTensor => flattened.map(_.as[Boolean])
    }
    val (errors, tensors) = convertedData partition {
      case e: Either[DecodingFailure, Any] => true
      case _ => false
    }
    if (errors.nonEmpty) {
      Left(InvalidFieldValuesConversion(convertedData.collect { case Left(error) => error.message }))
    } else {
      toTensor(factory, tensors)
    }
  }

  def toTensor[T <: TypedTensor[_]](factory: TypedTensorFactory[T], flatData: Seq[Any]): Either[ValidationError, T] = {
    factory.createFromAny(flatData, Shape(field.shape)) match {
      case Some(tensor) => Right(tensor)
      case None => Left(ValidationError.IncompatibleFieldTypeError(field.name, dataType))
    }
  }

  private def flatten(arr: Seq[Json]): Seq[Json] = {
    arr.flatMap {
      case jsonArray(x) => flatten(x)
      case value => Seq(value)
    }
  }
}