package io.hydrosphere.serving.tensorflow.tensor

import com.google.protobuf.ByteString

case class StringTensor(tensorProto: TensorProto) extends TypedTensor[String] {
  override def get: Seq[String] = tensorProto.stringVal.map(_.toStringUtf8)

  override def put(data: Seq[String]): TensorProto =
    tensorProto.addAllStringVal(data.map(ByteString.copyFromUtf8))
}
