package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.types.DataType


case class DoubleTensor(shape: Shape, data: Seq[Double]) extends TypedTensor[DataType.DT_DOUBLE.type] {
  override type Self = DoubleTensor

  override type DataT = Double

  override def dtype: DataType.DT_DOUBLE.type = DataType.DT_DOUBLE

  override def factory: DoubleTensor.type = DoubleTensor
}

object DoubleTensor extends TypedTensorFactory[DoubleTensor] {
  override implicit def lens: TensorProtoLens[DoubleTensor] = new TensorProtoLens[DoubleTensor] {
    override def getter = _.doubleVal

    override def setter = _.withDoubleVal(_)
  }

  override def constructor = DoubleTensor.apply
}
