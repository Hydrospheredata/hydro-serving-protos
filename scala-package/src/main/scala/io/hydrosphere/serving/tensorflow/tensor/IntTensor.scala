package io.hydrosphere.serving.tensorflow.tensor

case class IntTensor(tensorProto: TensorProto) extends TypedTensor[Int] {
  override def get: Seq[Int] = tensorProto.intVal

  override def put(data: Seq[Int]): TensorProto =
    tensorProto.addAllIntVal(data)
}
