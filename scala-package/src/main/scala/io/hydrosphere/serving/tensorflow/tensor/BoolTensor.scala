package io.hydrosphere.serving.tensorflow.tensor

import io.hydrosphere.serving.tensorflow.types.DataType

case class BoolTensor(shape: Option[Seq[Long]], data: Seq[Boolean]) extends TypedTensor[DataType.DT_BOOL.type] {
  override type Self = BoolTensor

  override type DataT = Boolean

  override def dtype = DataType.DT_BOOL

  override def factory = BoolTensor
}

object BoolTensor extends TypedTensorFactory[BoolTensor] {

  override implicit def lens: TensorProtoLens[BoolTensor] = new TensorProtoLens[BoolTensor] {
    override def getter: TensorProto => Seq[Boolean] = _.boolVal

    override def setter: (TensorProto, Seq[Boolean]) => TensorProto = _.withBoolVal(_)
  }

  override def constructor = BoolTensor.apply
}