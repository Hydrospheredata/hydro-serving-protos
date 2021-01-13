package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.types.DataType

case class Uint32Tensor(shape: Shape, data: Seq[Int]) extends IntTensor[DataType.DT_UINT32.type] {
  override type Self = Uint32Tensor

  override def dtype = DataType.DT_UINT32

  override def factory = Uint32Tensor
}

object Uint32Tensor extends TypedTensorFactory[Uint32Tensor] {
  override implicit def lens: TensorProtoLens[Uint32Tensor] = new TensorProtoLens[Uint32Tensor] {
    override def getter = _.uint32Val

    override def setter = _.withUint32Val(_)
  }

  override def constructor = Uint32Tensor.apply
}