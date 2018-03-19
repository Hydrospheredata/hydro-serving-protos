package io.hydrosphere.serving.tensorflow.tensor

import io.hydrosphere.serving.tensorflow.types.DataType

case class Int32Tensor(shape: Option[Seq[Long]], data: Seq[Int]) extends IntTensor[DataType.DT_INT32.type] {
  override type Self = Int32Tensor

  override def dtype = DataType.DT_INT32

  override def factory = Int32Tensor
}

object Int32Tensor extends TypedTensorFactory[Int32Tensor] {
  override implicit def lens: TensorProtoLens[Int32Tensor] = IntTensor.protoLens[Int32Tensor]

  override def constructor = Int32Tensor.apply
}