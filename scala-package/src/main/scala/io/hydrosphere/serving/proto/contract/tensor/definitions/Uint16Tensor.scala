package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.types.DataType

case class Uint16Tensor(shape: Shape, data: Seq[Int]) extends IntTensor[DataType.DT_UINT16.type] {
  override type Self = Uint16Tensor

  override def dtype = DataType.DT_UINT16

  override def factory = Uint16Tensor
}

object Uint16Tensor extends TypedTensorFactory[Uint16Tensor] {
  override implicit def lens: TensorProtoLens[Uint16Tensor] = IntTensor.protoLens[Uint16Tensor]

  override def constructor = Uint16Tensor.apply
}