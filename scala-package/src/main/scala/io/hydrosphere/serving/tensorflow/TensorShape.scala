package io.hydrosphere.serving.tensorflow

import io.hydrosphere.serving.tensorflow.tensor_shape.TensorShapeProto

case class TensorShape(dims: Option[Seq[Long]], unknownRank: Boolean = false) {
  def toProto: Option[TensorShapeProto] = {
    dims.map { shapeDims =>
      TensorShapeProto(
        dim = shapeDims.map(TensorShapeProto.Dim.apply(_)),
        unknownRank = unknownRank
      )
    }
  }
}

object TensorShape {
  def scalar: TensorShape = TensorShape(None)

  def vector(size: Long) = TensorShape(Some(Seq(size)))

  def mat(dims: Long*) = TensorShape(Some(dims))

  def fromProto(protoShape: Option[TensorShapeProto]): TensorShape = {
    TensorShape(
      dims = protoShape.map { shape =>
        shape.dim.map(_.size)
      },
      unknownRank = protoShape.exists(_.unknownRank)
    )
  }

  def fromSeq(dims: Option[Seq[Long]]): TensorShape = {
    TensorShape(
      dims = dims
    )
  }
}
