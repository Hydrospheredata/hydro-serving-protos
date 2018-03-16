package io.hydrosphere.serving.tensorflow.tensor

import io.hydrosphere.serving.contract.utils.validation.{InvalidFieldData, UnsupportedFieldTypeError, ValidationError}
import io.hydrosphere.serving.tensorflow.tensor_shape.TensorShapeProto
import io.hydrosphere.serving.tensorflow.types.DataType
import io.hydrosphere.serving.tensorflow.types.DataType._

import scala.reflect.ClassTag

/**
  * At one time, `TensorProto` can only have one type of data.
  * `TypedTensor` is a wrapper that ensures it, and provides simple access.
  * @tparam T type of data field
  */
trait TypedTensor[T] {
  def tensorProto: TensorProto

  /**
    * Returns tensor contents from a field as flat `Seq`
    * @return flat data `Seq`
    */
  def get: Seq[T]

  /**
    * Puts data to a field
    * @param data data
    * @return tensor with new data
    */
  def put(data: Seq[T]): TensorProto

  /**
    * Tries to convert data to tensor-specific type and puts it to a field
    * @param data data
    * @param ct class tag to retrieve class info
    * @return tensor with new data or error
    */
  def putAny(data: Seq[Any])(implicit ct: ClassTag[T]): Either[ValidationError, TensorProto] = {
    castData(data).right.map { converted =>
      put(converted)
    }
  }

  /**
    * Tries to convert `data` to tensor-specific type.
    * @param data target data
    * @param ct class tag to retrieve class info
    * @return converted data or error
    */
  def castData(data: Seq[Any])(implicit ct: ClassTag[T]): Either[ValidationError, Seq[T]] = {
    try {
      Right(data.map(_.asInstanceOf[T]))
    } catch {
      case _: ClassCastException => Left(new InvalidFieldData(ct.runtimeClass))
    }
  }
}

object TypedTensor {
  def apply(dataType: DataType): TypedTensor[_] = {
    TypedTensor(TensorProto.defaultInstance.withDtype(dataType))
  }

  def apply(tensorProto: TensorProto): TypedTensor[_] = {
    tensorProto.dtype match {
      case DT_FLOAT => FloatTensor(tensorProto)

      case DT_DOUBLE => DoubleTensor(tensorProto)

      case DT_INT8 | DT_INT16 | DT_INT32 | DT_QINT8 | DT_QINT16 | DT_QINT32 =>
        IntTensor(tensorProto)

      case DT_UINT8 | DT_UINT16 | DT_UINT32 | DT_QUINT8 | DT_QUINT16 =>
        UintTensor(tensorProto)

      case DT_INT64 => Int64Tensor(tensorProto)

      case DT_UINT64 => Uint64Tensor(tensorProto)

      case DT_COMPLEX64 => SComplexTensor(tensorProto)
      case DT_COMPLEX128 => DComplexTensor(tensorProto)

      case DT_STRING => StringTensor(tensorProto)
      case DT_BOOL => BoolTensor(tensorProto)

      case x => throw new UnsupportedFieldTypeError(x)
    }
  }

  /**
    * Creates tensor with `data` and `tensorInfo`
    *
    * @param data  contents to be put in tensor
    * @param shape data shape
    * @param dtype data type
    * @return tensor with data or error
    */
  def constructTensor(
    data: Seq[Any],
    shape: Option[TensorShapeProto],
    dtype: DataType
  ): Either[ValidationError, TensorProto] = {
    val tensor = TensorProto(dtype = dtype, tensorShape = shape)
    val typedTensor = TypedTensor(tensor)
    typedTensor.putAny(data)
  }
}
