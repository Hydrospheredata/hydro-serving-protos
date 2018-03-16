package io.hydrosphere.serving.tensorflow.tensor

case class SComplexTensor(tensorProto: TensorProto) extends TypedTensor[Float] {
  override def get: Seq[Float] = tensorProto.scomplexVal

  override def put(data: Seq[Float]): TensorProto =
    tensorProto.addAllScomplexVal(data)
}
