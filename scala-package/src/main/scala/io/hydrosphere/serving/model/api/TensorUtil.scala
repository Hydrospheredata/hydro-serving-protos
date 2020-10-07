package io.hydrosphere.serving.model.api

import io.hydrosphere.serving.tensorflow.TensorShape.{AnyDims, Dims}
import io.hydrosphere.serving.tensorflow.tensor.{TensorProto, TypedTensor, TypedTensorFactory}

object TensorUtil {
  def verifyShape[T](tensor: TypedTensor[T]): Option[TypedTensor[T]] = {
    tensor.shape match {
      case AnyDims => Some(tensor)
      case Dims(tensorDims, _) if tensorDims.isEmpty => Some(tensor)
      case Dims(tensorDims, _) =>
        if (tensorDims.isEmpty && tensor.data.length <= 1) {
          Some(tensor)
        } else {
          val reverseTensorDimIter = tensorDims.reverseIterator

          val actualDims = Array.fill(tensorDims.length)(0L)
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
            val rawTensor = tensor.toProto.copy(tensorShape = Dims(actualDims).toProto)
            val result = tensor.factory.fromProto(rawTensor)
            Some(result)
          } else {
            None
          }
        }
    }
  }

  def verifyShape(tensor: TensorProto): Option[TensorProto] = {
    verifyShape(TypedTensorFactory.create(tensor)).map(_.toProto)
  }
}