package io.hydrosphere.serving.tensorflow.tensor

case class UintTensor(tensorProto: TensorProto) extends TypedTensor[Int] {
  override def get: Seq[Int] = tensorProto.uint32Val

  override def put(data: Seq[Int]): TensorProto =
    tensorProto.addAllUint32Val(data)
}
