package io.hydrosphere.serving.tensorflow.tensor

case class Int64Tensor(tensorProto: TensorProto) extends TypedTensor[Long] {
  override def get: Seq[Long] = tensorProto.int64Val

  override def put(data: Seq[Long]): TensorProto =
    tensorProto.addAllInt64Val(data)
}
