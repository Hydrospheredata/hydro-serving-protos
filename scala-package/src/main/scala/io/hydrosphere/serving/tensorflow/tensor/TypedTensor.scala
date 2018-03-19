package io.hydrosphere.serving.tensorflow.tensor

import io.hydrosphere.serving.tensorflow.types.DataType
import io.hydrosphere.serving.tensorflow.utils.ops.TensorShapeProtoOps


trait TypedTensor[DTypeT <: DataType] {
  type Self <: TypedTensor[DTypeT]
  type DataT

  def data: Seq[Self#DataT]

  def shape: Option[Seq[Long]]

  def dtype: DTypeT

  def factory: TypedTensorFactory[Self]

  final def toProto(): TensorProto = {
    val pretensor = TensorProto(dtype, TensorShapeProtoOps.maybeSeqToShape(shape))
    pretensor.update { _ =>
      factory.lens.lens := data
    }
  }
}
