package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.tensor.Tensor
import io.hydrosphere.serving.proto.contract.tensor.definitions.Shape.{AnyShape, LocalShape}

object TensorUtil {

  def verifyShape[T](tensor: TypedTensor[T]): Option[TypedTensor[T]] = {
    tensor.shape match {
      case AnyShape => Some(tensor)
      case LocalShape(dims) if dims.isEmpty => Some(tensor)
      case LocalShape(dims) =>
        val reverseTensorDimIter = dims.reverseIterator

        val actualDims = Array.fill(dims.length)(0L)
        var actualDimId = actualDims.indices.last
        var dimLen = tensor.data.length

        var isShapeOk = true

        while (isShapeOk && reverseTensorDimIter.hasNext) {
          val currentDim = reverseTensorDimIter.next()
          val subCount = dimLen.toDouble / currentDim.toDouble
          if (subCount.isWhole) { // ok
            dimLen = subCount.toInt
            if (subCount < 0) {
              actualDims(actualDimId) = dimLen.abs
            } else {
              actualDims(actualDimId) = currentDim
            }
            actualDimId -= 1
          } else { // not ok
            isShapeOk = false
          }
        }

        if (isShapeOk) {
          val rawTensor = tensor.toProto.copy(tensorShape = LocalShape(actualDims).toProto)
          val result = tensor.factory.fromProto(rawTensor)
          Some(result)
        } else {
          None
        }
    }
  }

  def verifyShape(tensor: Tensor): Option[Tensor] = {
    verifyShape(TypedTensorFactory.create(tensor)).map(_.toProto)
  }
}