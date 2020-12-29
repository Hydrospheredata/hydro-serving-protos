package io.hydrosphere.serving.proto.contract.tensor.generators

import io.circe.Json
import io.hydrosphere.serving.proto.contract.field.ModelField
import io.hydrosphere.serving.proto.contract.field.ModelField.TypeOrSubfields._
import io.hydrosphere.serving.proto.contract.signature.ModelSignature
import io.hydrosphere.serving.proto.contract.tensor.TensorShape
import io.hydrosphere.serving.proto.contract.tensor.conversions.json.TensorJsonLens
import io.hydrosphere.serving.proto.contract.tensor.definitions.Shape.{AnyShape, LocalShape}
import io.hydrosphere.serving.proto.contract.tensor.definitions.{MapTensor, Shape, TypedTensor, TypedTensorFactory}
import io.hydrosphere.serving.proto.contract.types.DataType
import io.hydrosphere.serving.proto.contract.types.DataType._

case class TensorExampleGenerator(signature: ModelSignature) {
  def inputs: Map[String, TypedTensor[_]] = {
    signature.inputs.flatMap(TensorExampleGenerator.generateField).toMap
  }

  def outputs: Map[String, TypedTensor[_]] = {
    signature.outputs.flatMap(TensorExampleGenerator.generateField).toMap
  }
}

object TensorExampleGenerator {
  def generatePayload(signature: ModelSignature): Json =
    TensorJsonLens.mapToJson(TensorExampleGenerator(signature).inputs)

  def generateScalarData[T <: DataType](dataType: T): Any = {
    dataType match {
      case DT_FLOAT | DT_COMPLEX64 => 1.0F
      case DT_DOUBLE | DT_COMPLEX128 => 1.0D
      case DT_INT8 | DT_INT16 | DT_INT32 | DT_UINT8 | DT_UINT16 | DT_UINT32 | DT_QINT8 | DT_QINT16 |
           DT_QINT32 | DT_QUINT8 | DT_QUINT16 => 1
      case DT_INT64 | DT_UINT64 => 1L
      case DT_STRING => "foo"
      case DT_BOOL => true
      case DT_INVALID =>
        throw new IllegalArgumentException(
          s"Can't convert data to DT_INVALID"
        )
      case x => throw new IllegalArgumentException(s"Cannot process Tensor with $x dtype") // refs
    }
  }

  def createFlatTensor[T](shape: Shape, generator: => T): Seq[T] = {
    shape match {
      case AnyShape => List(generator)
      case LocalShape(dims) if dims.isEmpty => List(generator)
      case LocalShape(dims) =>
        val flatLen = dims.map(_.max(1)).product
        (1L to flatLen).map(_ => generator) // mat
    }
  }

  def generateTensor(shape: Shape, dtype: DataType): Option[TypedTensor[_]] = {
    val factory = TypedTensorFactory(dtype)
    val data = createFlatTensor(shape, generateScalarData(dtype))
    factory.createFromAny(data, shape)
  }

  def generateField(field: ModelField): Map[String, TypedTensor[_]] = {
    val shape = Shape(field.shape)
    val fieldValue = field.typeOrSubfields match {
      case Empty => None
      case Dtype(value) => generateTensor(shape, value)
      case Subfields(value) => generateNestedTensor(shape, value)
    }
    fieldValue.map(x => field.name -> x).toMap
  }

  private def generateNestedTensor(shape: Shape, value: ModelField.Subfield): Option[MapTensor] = {
    Some(MapTensor(shape, createFlatTensor(shape, generateMap(value))))
  }

  private def generateMap(value: ModelField.Subfield): Map[String, TypedTensor[_]] = {
    value.data.flatMap(generateField).toMap
  }
}