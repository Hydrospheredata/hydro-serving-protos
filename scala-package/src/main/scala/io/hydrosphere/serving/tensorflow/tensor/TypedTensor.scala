package io.hydrosphere.serving.tensorflow.tensor

import io.hydrosphere.serving.tensorflow.TensorShape
import io.hydrosphere.serving.tensorflow.types.DataType

trait TypedTensor[DTypeT] {
  type Self <: TypedTensor[DTypeT]
  type DataT

  def data: Seq[Self#DataT]

  def shape: TensorShape

  def dtype: DataType

  def factory: TypedTensorFactory[Self]

  final def toProto: TensorProto = {
    val pretensor = TensorProto(dtype, shape.toProto)
    pretensor.update { _ =>
      factory.lens.lens := data
    }
  }
}
