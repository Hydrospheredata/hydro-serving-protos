package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.tensor.Tensor
import scalapb.lenses.Lens

trait TensorProtoLens[T <: TypedTensor[_]] {
  def getter: Tensor => Seq[T#DataT]

  def setter: (Tensor, Seq[T#DataT]) => Tensor

  final def lens: Lens[Tensor, Seq[T#DataT]] = {
    Lens[Tensor, Seq[T#DataT]](getter)(setter)
  }
}
