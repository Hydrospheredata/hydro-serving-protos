package io.hydrosphere.serving.tensorflow.tensor

import io.hydrosphere.serving.tensorflow.types.DataType

case class Uint32Tensor(shape: Option[Seq[Long]], data: Seq[Int]) extends IntTensor[DataType.DT_UINT32.type] {
  override type Self = Uint32Tensor

  override def dtype = DataType.DT_UINT32

  override def factory = Uint32Tensor
}

object Uint32Tensor extends TypedTensorFactory[Uint32Tensor] {
  override implicit def lens: TensorProtoLens[Uint32Tensor] = UintTensor.protoLens[Uint32Tensor]

  override def constructor = Uint32Tensor.apply
}