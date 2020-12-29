package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.types.DataType

case class Int64Tensor(shape: Shape, data: Seq[Long]) extends TypedTensor[DataType.DT_INT64.type] {
  override type Self = Int64Tensor

  override type DataT = Long

  override def dtype = DataType.DT_INT64

  override def factory = Int64Tensor
}

object Int64Tensor extends TypedTensorFactory[Int64Tensor] {
  override implicit def lens: TensorProtoLens[Int64Tensor] = new TensorProtoLens[Int64Tensor] {
    override def getter = _.int64Val

    override def setter = _.withInt64Val(_)
  }

  override def constructor = Int64Tensor.apply
}