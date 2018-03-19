package io.hydrosphere.serving.tensorflow.tensor

import io.hydrosphere.serving.tensorflow.TensorShape
import io.hydrosphere.serving.tensorflow.types.DataType

case class DComplexTensor(shape: TensorShape, data: Seq[Double]) extends TypedTensor[DataType.DT_COMPLEX128.type] {
  override type Self = DComplexTensor

  override type DataT = Double

  override def dtype = DataType.DT_COMPLEX128

  override def factory = DComplexTensor
}

object DComplexTensor extends TypedTensorFactory[DComplexTensor] {
  override implicit def lens: TensorProtoLens[DComplexTensor] = new TensorProtoLens[DComplexTensor] {
    override def getter: TensorProto => Seq[Double] = _.dcomplexVal

    override def setter: (TensorProto, Seq[Double]) => TensorProto = _.withDcomplexVal(_)
  }

  override def constructor = DComplexTensor.apply
}