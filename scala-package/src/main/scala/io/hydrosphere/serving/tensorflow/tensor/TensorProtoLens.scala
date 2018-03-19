package io.hydrosphere.serving.tensorflow.tensor

import com.trueaccord.lenses.Lens

trait TensorProtoLens[T <: TypedTensor[_]] {
  def getter: TensorProto => Seq[T#DataT]

  def setter: (TensorProto, Seq[T#DataT]) => TensorProto

  final def lens: Lens[TensorProto, Seq[T#DataT]] = {
    Lens[TensorProto, Seq[T#DataT]](getter)(setter)
  }
}
