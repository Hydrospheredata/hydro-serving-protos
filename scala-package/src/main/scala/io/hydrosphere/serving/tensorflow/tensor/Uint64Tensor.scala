package io.hydrosphere.serving.tensorflow.tensor

case class Uint64Tensor(tensorProto: TensorProto) extends TypedTensor[Long] {
  override def get: Seq[Long] = tensorProto.uint64Val

  override def put(data: Seq[Long]): TensorProto =
    tensorProto.addAllUint64Val(data)
}
