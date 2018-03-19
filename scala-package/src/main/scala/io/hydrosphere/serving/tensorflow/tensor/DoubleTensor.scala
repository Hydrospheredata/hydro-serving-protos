package io.hydrosphere.serving.tensorflow.tensor

import io.hydrosphere.serving.tensorflow.types.DataType

case class DoubleTensor(shape: Option[Seq[Long]], data: Seq[Double]) extends TypedTensor[DataType.DT_DOUBLE.type] {
  override type Self = DoubleTensor

  override type DataT = Double

  override def dtype = DataType.DT_DOUBLE

  override def factory = DoubleTensor
}

object DoubleTensor extends TypedTensorFactory[DoubleTensor] {
  override implicit def lens: TensorProtoLens[DoubleTensor] = new TensorProtoLens[DoubleTensor] {
    override def getter: TensorProto => Seq[Double] = _.doubleVal

    override def setter: (TensorProto, Seq[Double]) => TensorProto = _.withDoubleVal(_)
  }

  override def constructor = DoubleTensor.apply
}
