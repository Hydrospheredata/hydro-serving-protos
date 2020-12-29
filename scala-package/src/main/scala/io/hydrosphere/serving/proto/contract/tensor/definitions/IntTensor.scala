package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.tensor.Tensor
import io.hydrosphere.serving.proto.contract.types.DataType

trait IntTensor[T <: DataType] extends TypedTensor[T] {
  final override type DataT = Int
}

object IntTensor {
  def protoLens[T <: IntTensor[_]] = new TensorProtoLens[T] {
    override def getter: Tensor => Seq[Int] = _.intVal

    override def setter: (Tensor, Seq[Int]) => Tensor = _.withIntVal(_)
  }
}
