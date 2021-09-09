package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.types.DataType

case class FloatTensor(shape: Shape, data: Seq[Float]) extends TypedTensor[DataType.DT_FLOAT.type] {
  override type Self = FloatTensor

  override type DataT = Float

  override def dtype = DataType.DT_FLOAT

  override def factory = FloatTensor
}

object FloatTensor extends TypedTensorFactory[FloatTensor] {
  override implicit def lens: TensorProtoLens[FloatTensor] = new TensorProtoLens[FloatTensor] {
    override def getter = _.floatVal

    override def setter = _.withFloatVal(_)
  }

  override def constructor = FloatTensor.apply
}