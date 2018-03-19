package io.hydrosphere.serving.tensorflow.tensor

import io.hydrosphere.serving.tensorflow.TensorShape
import io.hydrosphere.serving.tensorflow.types.DataType

case class FloatTensor(shape: TensorShape, data: Seq[Float]) extends TypedTensor[DataType.DT_FLOAT.type] {
  override type Self = FloatTensor

  override type DataT = Float

  override def dtype = DataType.DT_FLOAT

  override def factory = FloatTensor
}

object FloatTensor extends TypedTensorFactory[FloatTensor] {
  override implicit def lens: TensorProtoLens[FloatTensor] = new TensorProtoLens[FloatTensor] {
    override def getter: TensorProto => Seq[Float] = _.floatVal

    override def setter: (TensorProto, Seq[Float]) => TensorProto = _.withFloatVal(_)
  }

  override def constructor = FloatTensor.apply
}