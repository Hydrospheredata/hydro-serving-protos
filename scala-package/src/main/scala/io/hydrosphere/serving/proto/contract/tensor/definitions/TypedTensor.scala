package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.tensor.Tensor
import io.hydrosphere.serving.proto.contract.types.DataType

trait TypedTensor[DTypeT] {
  type Self <: TypedTensor[DTypeT]
  type DataT

  def data: Seq[Self#DataT]

  def shape: Shape

  def dtype: DataType

  def factory: TypedTensorFactory[Self]

  final def toProto: Tensor = {
    val pretensor = Tensor(dtype, shape.toProto)
    pretensor.update { _ =>
      factory.lens.lens := data
    }
  }
}
