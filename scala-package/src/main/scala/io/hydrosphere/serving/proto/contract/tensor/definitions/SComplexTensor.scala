package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.types.DataType

case class SComplexTensor(shape: Shape, data: Seq[Float]) extends TypedTensor[DataType.DT_COMPLEX64.type] {
  override type Self = SComplexTensor

  override type DataT = Float

  override def dtype = DataType.DT_COMPLEX64

  override def factory = SComplexTensor
}

object SComplexTensor extends TypedTensorFactory[SComplexTensor] {
  override implicit def lens: TensorProtoLens[SComplexTensor] = new TensorProtoLens[SComplexTensor] {
    override def getter = _.scomplexVal

    override def setter = _.withScomplexVal(_)
  }

  override def constructor = SComplexTensor.apply
}