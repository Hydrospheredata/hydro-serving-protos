package io.hydrosphere.serving.tensorflow.tensor

case class DComplexTensor(tensorProto: TensorProto) extends TypedTensor[Double] {
  override def get: Seq[Double] = tensorProto.dcomplexVal

  override def put(data: Seq[Double]): TensorProto =
    tensorProto.addAllDcomplexVal(data)
}
