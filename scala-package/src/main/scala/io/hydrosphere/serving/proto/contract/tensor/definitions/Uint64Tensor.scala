package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.types.DataType

case class Uint64Tensor(shape: Shape, data: Seq[Long]) extends TypedTensor[DataType.DT_UINT64.type] {
  override type Self = Uint64Tensor
  override type DataT = Long

  override def dtype = DataType.DT_UINT64

  override def factory = Uint64Tensor
}

object Uint64Tensor extends TypedTensorFactory[Uint64Tensor] {
  override implicit def lens: TensorProtoLens[Uint64Tensor] = new TensorProtoLens[Uint64Tensor] {
    override def getter = _.uint64Val

    override def setter = _.withUint64Val(_)
  }

  override def constructor = Uint64Tensor.apply
}
