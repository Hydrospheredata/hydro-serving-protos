package io.hydrosphere.serving.tensorflow.tensor

import io.hydrosphere.serving.contract.utils.validation.{InvalidFieldData, UnsupportedFieldTypeError, ValidationError}
import io.hydrosphere.serving.tensorflow.tensor_shape.TensorShapeProto
import io.hydrosphere.serving.tensorflow.types.DataType
import io.hydrosphere.serving.tensorflow.types.DataType._
import io.hydrosphere.serving.tensorflow.utils.ops.TensorShapeProtoOps

trait TypedTensorFactory[TensorT <: TypedTensor[_]] {

  implicit def lens: TensorProtoLens[TensorT]

  def constructor: (Option[Seq[Long]], Seq[TensorT#DataT]) => TensorT

  /**
    * Tries to convert `data` to tensor-specific type.
    *
    * @param data target data
    * @return converted data or error
    */
  final def castData(data: Seq[Any]): Either[ValidationError, Seq[TensorT#DataT]] = {
    try {
      Right(data.map(_.asInstanceOf[TensorT#DataT]))
    } catch {
      case ex: ClassCastException => Left(new InvalidFieldData(ex.getClass))
    }
  }

  final def empty: TensorT = {
    constructor(None, Seq.empty)
  }

  final def create(
    data: Seq[TensorT#DataT],
    shape: Option[Seq[Long]]
  ): TensorT = {
    constructor(shape, data)
  }

  final def fromProto(proto: TensorProto): TensorT = {
    constructor(TensorShapeProtoOps.shapeToList(proto.tensorShape), lens.lens.get(proto))
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
    shape: Option[TensorShapeProto]
  ): Either[ValidationError, TensorT] = {
    val tensorProto = TensorProto(dtype = empty.dtype.asInstanceOf[DataType], tensorShape = shape)
    castData(data).right.map { converted =>
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

      case x => throw new UnsupportedFieldTypeError(x)
    }
  }

  def create(tensorProto: TensorProto): TypedTensor[_] = {
    TypedTensorFactory(tensorProto.dtype).fromProto(tensorProto)
  }
}