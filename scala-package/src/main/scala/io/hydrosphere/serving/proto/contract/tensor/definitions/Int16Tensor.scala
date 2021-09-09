package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.types.DataType

case class Int16Tensor(shape: Shape, data: Seq[Int]) extends IntTensor[DataType.DT_INT16.type] {
  override type Self = Int16Tensor

  override def dtype = DataType.DT_INT16

  override def factory = Int16Tensor
}

object Int16Tensor extends TypedTensorFactory[Int16Tensor] {
  override implicit def lens: TensorProtoLens[Int16Tensor] = IntTensor.protoLens[Int16Tensor]

  override def constructor = Int16Tensor.apply
}
