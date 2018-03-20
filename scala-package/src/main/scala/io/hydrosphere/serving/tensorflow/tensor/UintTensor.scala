package io.hydrosphere.serving.tensorflow.tensor

object UintTensor {
  def protoLens[T <: IntTensor[_]] = new TensorProtoLens[T] {
    override def getter: TensorProto => Seq[Int] = _.uint32Val

    override def setter: (TensorProto, Seq[Int]) => TensorProto = _.withUint32Val(_)
  }
}
