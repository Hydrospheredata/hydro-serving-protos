package io.hydrosphere.serving.tensorflow.tensor

case class BoolTensor(tensorProto: TensorProto) extends TypedTensor[Boolean] {
  override def get: Seq[Boolean] = tensorProto.boolVal

  override def put(data: Seq[Boolean]): TensorProto =
    tensorProto.addAllBoolVal(data)
}
