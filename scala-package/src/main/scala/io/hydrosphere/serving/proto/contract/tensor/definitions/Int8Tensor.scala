package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.types.DataType

case class Int8Tensor(shape: Shape, data: Seq[Int]) extends IntTensor[DataType.DT_INT8.type] {
  override type Self = Int8Tensor

  override def dtype = DataType.DT_INT8

  override def factory = Int8Tensor
}

object Int8Tensor extends TypedTensorFactory[Int8Tensor] {
  override implicit def lens = IntTensor.protoLens[Int8Tensor]

  override def constructor = Int8Tensor.apply
}