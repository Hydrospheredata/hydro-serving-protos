package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.types.DataType


case class DComplexTensor(shape: Shape, data: Seq[Double]) extends TypedTensor[DataType.DT_COMPLEX128.type] {
  override type Self = DComplexTensor

  override type DataT = Double

  override def dtype = DataType.DT_COMPLEX128

  override def factory = DComplexTensor
}

object DComplexTensor extends TypedTensorFactory[DComplexTensor] {
  override implicit def lens: TensorProtoLens[DComplexTensor] = new TensorProtoLens[DComplexTensor] {
    override def getter = _.dcomplexVal

    override def setter = _.withDcomplexVal(_)
  }

  override def constructor = DComplexTensor.apply
}