package io.hydrosphere.serving.tensorflow.utils.ops

import io.hydrosphere.serving.tensorflow.tensor_shape.TensorShapeProto

trait TensorShapeProtoOps {
  type Shape = Option[TensorShapeProto]

  implicit class TensorShapeProtoPumped(tensorShapeProto: TensorShapeProto) {
    def toDimList: List[Long] = {
      tensorShapeProto.dim.map(_.size).toList
    }
  }

  def merge(firstShape: Shape, secondShape: Shape): Option[Shape] = {
    firstShape -> secondShape match {
      case (em, re) if em == re => Some(firstShape)
      case (Some(em), Some(re)) if re.unknownRank == em.unknownRank && re.unknownRank =>
        Some(firstShape)
      case (Some(em), Some(re)) =>
        TensorShapeProtoOps.merge(em, re).map(Some.apply)
      case _ => None
    }
  }

  def merge(first: TensorShapeProto, second: TensorShapeProto): Option[TensorShapeProto] = {
    if (first.dim.lengthCompare(second.dim.length) != 0) {
      None
    } else {
      val dims = first.dim.zip(second.dim).map {
        case (fDim, sDim) if fDim.size == sDim.size => Some(fDim)
        case (fDim, sDim) if fDim.size == -1 => Some(sDim)
        case (fDim, sDim) if sDim.size == -1 => Some(fDim)
        case _ => None
      }
      if (dims.forall(_.isDefined)) {
        Some(TensorShapeProto(dims.map(_.get)))
      } else {
        None
      }
    }
  }
}

object TensorShapeProtoOps extends TensorShapeProtoOps