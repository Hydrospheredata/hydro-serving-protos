package io.hydrosphere.serving.tensorflow.tensor

import io.hydrosphere.serving.tensorflow.types.DataType

case class MapTensor(shape: Option[Seq[Long]], data: Seq[MapTensorData]) extends TypedTensor[DataType.DT_MAP.type] {
  override type Self = MapTensor

  override type DataT = MapTensorData

  override def dtype = DataType.DT_MAP

  override def factory = MapTensor
}

object MapTensor extends TypedTensorFactory[MapTensor] {
  override implicit def lens: TensorProtoLens[MapTensor] = new TensorProtoLens[MapTensor] {
    override def getter: TensorProto => Seq[MapTensorData] = _.mapVal

    override def setter: (TensorProto, Seq[MapTensorData]) => TensorProto = _.withMapVal(_)
  }

  override def constructor = MapTensor.apply
}