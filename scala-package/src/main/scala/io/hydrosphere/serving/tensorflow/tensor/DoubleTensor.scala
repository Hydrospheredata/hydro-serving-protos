package io.hydrosphere.serving.tensorflow.tensor

case class DoubleTensor(tensorProto: TensorProto) extends TypedTensor[Double] {
  override def get: Seq[Double] = tensorProto.doubleVal

  override def put(data: Seq[Double]): TensorProto =
    tensorProto.addAllDoubleVal(data)
}
