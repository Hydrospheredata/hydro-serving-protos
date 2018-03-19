package io.hydrosphere.serving.tensorflow.tensor

import io.hydrosphere.serving.tensorflow.TensorShape
import io.hydrosphere.serving.tensorflow.types.DataType

case class Uint8Tensor(shape: TensorShape, data: Seq[Int]) extends IntTensor[DataType.DT_UINT8.type] {
  override type Self = Uint8Tensor

  override def dtype = DataType.DT_UINT8

  override def factory = Uint8Tensor
}

object Uint8Tensor extends TypedTensorFactory[Uint8Tensor] {
  override implicit def lens: TensorProtoLens[Uint8Tensor] = UintTensor.protoLens[Uint8Tensor]

  override def constructor = Uint8Tensor.apply
}