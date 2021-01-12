package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.tensor.TensorShape

sealed trait Shape {
  def toProto: Option[TensorShape]
}

object Shape {
  def apply(tensorShape: Option[TensorShape]): Shape = tensorShape match {
    case Some(x) => LocalShape(x.dims)
    case None => LocalShape(Seq.empty)
  }

  case object AnyShape extends Shape {
    override def toProto: Option[TensorShape] = None
  }

  case class LocalShape(dims: Seq[Long]) extends Shape {
    override def toProto: Option[TensorShape] = dims match {
      case Nil => None
      case _ => Some(TensorShape(dims=dims))
    }
  }

  def any: Shape = AnyShape

  def scalar: Shape = LocalShape(Seq.empty)

  def vector(size: Long): Shape = LocalShape(Seq(size))

  def mat(dims: Long*): Shape = LocalShape(dims)
}
