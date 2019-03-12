package io.hydrosphere.serving.tensorflow

import io.hydrosphere.serving.tensorflow.tensor_shape.TensorShapeProto

/**
  * Generic Tensor shape.
  */
sealed trait TensorShape {
  /**
    * Convert TensorShape to the field value in TensorProto
    * @return
    */
  def toProto: Option[TensorShapeProto]
}

object TensorShape {
  def apply(tensorShapeProto: Option[TensorShapeProto]): TensorShape = {
    tensorShapeProto match {
      case Some(shape) => Dims(shape.dim.map(_.size))
      case None => AnyDims
    }
  }

  /**
    * Missing Tensor shape. Could be anything.
    */
  case object AnyDims extends TensorShape {
    override def toProto: Option[TensorShapeProto] = None
  }

  /**
    * Specified Tensor shape.
    *
    * @param dims        if empty - then it's scalar value, if 1 element - vector, if 2 - matrix and so on.
    * @param unknownRank field for compatibility with TF
    */
  case class Dims(dims: Seq[Long], unknownRank: Boolean = false) extends TensorShape {
    override def toProto: Option[TensorShapeProto] = Some(
      TensorShapeProto(
        dim = dims.map(TensorShapeProto.Dim.apply(_)),
        unknownRank = unknownRank
      )
    )
  }

  def any: TensorShape = AnyDims

  def scalar: TensorShape = Dims(Seq.empty)

  def vector(size: Long): TensorShape = Dims(Seq(size))

  def mat(dims: Long*): TensorShape = Dims(dims)
}
