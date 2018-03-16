package io.hydrosphere.serving.tensorflow.tensor

case class FloatTensor(tensorProto: TensorProto) extends TypedTensor[Float] {
  override def get: Seq[Float] = tensorProto.floatVal

  override def put(data: Seq[Float]): TensorProto =
    tensorProto.addAllFloatVal(data)
}
