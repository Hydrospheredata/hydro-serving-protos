package io.hydrosphere.serving.tensorflow.tensor

case class MapTensor(tensorProto: TensorProto) extends TypedTensor[MapTensorData] {
  /**
    * Returns tensor contents from a field as flat `Seq`
    *
    * @return flat data `Seq`
    */
  override def get: Seq[MapTensorData] = tensorProto.mapVal

  /**
    * Puts data to a field
    *
    * @param data data
    * @return tensor with new data
    */
  override def put(data: Seq[MapTensorData]): TensorProto = tensorProto.addAllMapVal(data)
}
