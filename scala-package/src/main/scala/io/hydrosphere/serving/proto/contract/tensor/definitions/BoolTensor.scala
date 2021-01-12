package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.types.DataType

case class BoolTensor(shape: Shape, data: Seq[Boolean]) extends TypedTensor[DataType.DT_BOOL.type] {
  override type Self = BoolTensor

  override type DataT = Boolean

  override def dtype = DataType.DT_BOOL

  override def factory = BoolTensor
}

object BoolTensor extends TypedTensorFactory[BoolTensor] {

  override implicit def lens: TensorProtoLens[BoolTensor] = new TensorProtoLens[BoolTensor] {
    override def getter = _.boolVal

    override def setter = _.withBoolVal(_)
  }

  override def constructor = BoolTensor.apply
}