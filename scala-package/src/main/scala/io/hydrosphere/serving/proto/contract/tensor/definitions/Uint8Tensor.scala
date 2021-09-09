package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.types.DataType

case class Uint8Tensor(shape: Shape, data: Seq[Int]) extends IntTensor[DataType.DT_UINT8.type] {
  override type Self = Uint8Tensor

  override def dtype = DataType.DT_UINT8

  override def factory = Uint8Tensor
}

object Uint8Tensor extends TypedTensorFactory[Uint8Tensor] {
  override implicit def lens: TensorProtoLens[Uint8Tensor] = IntTensor.protoLens[Uint8Tensor]

  override def constructor = Uint8Tensor.apply
}