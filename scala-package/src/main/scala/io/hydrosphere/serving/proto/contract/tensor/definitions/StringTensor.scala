package io.hydrosphere.serving.proto.contract.tensor.definitions

import com.google.protobuf.ByteString
import io.hydrosphere.serving.proto.contract.types.DataType

case class StringTensor(shape: Shape, data: Seq[String]) extends TypedTensor[DataType.DT_STRING.type] {
  override type Self = StringTensor

  override type DataT = String

  override def dtype = DataType.DT_STRING

  override def factory = StringTensor
}

object StringTensor extends TypedTensorFactory[StringTensor] {
  override implicit def lens: TensorProtoLens[StringTensor] = new TensorProtoLens[StringTensor] {
    override def getter = _.stringVal.map(_.toStringUtf8)

    override def setter = (t, d) =>  t.withStringVal(d.map(ByteString.copyFromUtf8))
  }

  override def constructor = StringTensor.apply
}