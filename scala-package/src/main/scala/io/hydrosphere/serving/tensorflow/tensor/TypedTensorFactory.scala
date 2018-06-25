package io.hydrosphere.serving.tensorflow.tensor

import io.hydrosphere.serving.tensorflow.TensorShape
import io.hydrosphere.serving.tensorflow.types.DataType
import io.hydrosphere.serving.tensorflow.types.DataType._

trait TypedTensorFactory[TensorT <: TypedTensor[_]] {

  implicit def lens: TensorProtoLens[TensorT]

  def constructor: (TensorShape, Seq[TensorT#DataT]) => TensorT

  /**
    * Tries to convert `data` to tensor-specific type.
    *
    * @param data target data
    * @return converted data or error
    */
  final def castData(data: Seq[Any]): Either[Exception, Seq[TensorT#DataT]] = {
    try {
      Right(data.map(_.asInstanceOf[TensorT#DataT]))
    } catch {
      case ex: ClassCastException => Left(new IllegalArgumentException(ex.getClass.toString))
    }
  }

  final def empty: TensorT = {
    constructor(TensorShape.scalar, Seq.empty)
  }

  final def create(
    data: Seq[TensorT#DataT],
    shape: TensorShape
  ): TensorT = {
    constructor(shape, data)
  }

  final def fromProto(proto: TensorProto): TensorT = {
    constructor(TensorShape(proto.tensorShape), lens.lens.get(proto))
  }

  /**
    * Creates tensor with `data` and `tensorInfo`
    *
    * @param data  contents to be put in tensor
    * @param shape data shape
    * @return tensor with data or error
    */
  final def createFromAny(
    data: Seq[Any],
    shape: TensorShape
  ): Option[TensorT] = {
    val tensorProto = TensorProto(dtype = empty.dtype, tensorShape = shape.toProto)
    castData(data).right.toOption.map { converted =>
      val newTensorProto = tensorProto.update(_ => lens.lens := converted)
      fromProto(newTensorProto)
    }
  }
}

object TypedTensorFactory {

  def apply(dataType: DataType): TypedTensorFactory[_ <: TypedTensor[_]] = {
    dataType match {
      case DT_FLOAT => FloatTensor
      case DT_DOUBLE => DoubleTensor

      case DT_INT8 | DT_QINT8 => Int8Tensor
      case DT_INT16 | DT_QINT16 => Int16Tensor
      case DT_INT32 => Int32Tensor

      case DT_UINT8 | DT_QUINT8 => Uint8Tensor
      case DT_UINT16 | DT_QUINT16 => Uint16Tensor
      case DT_UINT32 => Uint32Tensor

      case DT_INT64 => Int64Tensor
      case DT_UINT64 => Uint64Tensor

      case DT_COMPLEX64 => SComplexTensor
      case DT_COMPLEX128 => DComplexTensor

      case DT_STRING => StringTensor
      case DT_BOOL => BoolTensor

      case DT_MAP => MapTensor

      case x => throw new IllegalArgumentException(s"Unsupported DataType $x")
    }
  }

  def create(tensorProto: TensorProto): TypedTensor[_] = {
    TypedTensorFactory(tensorProto.dtype).fromProto(tensorProto)
  }
}