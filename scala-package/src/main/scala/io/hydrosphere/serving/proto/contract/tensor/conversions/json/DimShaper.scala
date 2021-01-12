package io.hydrosphere.serving.proto.contract.tensor.conversions.json

import io.circe.Json
import io.hydrosphere.serving.proto.contract.tensor.definitions.Shape.{AnyShape, LocalShape}
import io.hydrosphere.serving.proto.contract.tensor.definitions.Shape

sealed trait ColumnShaper {
  def shape(data: Seq[Json]): Json
}

case object AnyShaper extends ColumnShaper {
  override def shape(data: Seq[Json]): Json = Json.fromValues(data)
}

case object ScalarShaper extends ColumnShaper {
  override def shape(data: Seq[Json]): Json = {
    data.headOption.getOrElse(Json.Null)
  }
}

case class DimShaper(dims: Seq[Long]) extends ColumnShaper {
  println(dims)
  val strides: Seq[Long] = {
    val res = Array.fill(dims.length)(1L)
    val stLen = dims.length - 1
    for (i <- 0.until(stLen).reverse) {
      res(i) = res(i + 1) * dims(i + 1)
    }
    res.toSeq
  }

  def shape(data: Seq[Json]): Json = {
    def shapeGrouped(dataId: Int, shapeId: Int): Json = {
      if (shapeId >= dims.length) {
        data(dataId)
      } else {
        val n = dims(shapeId).toInt
        val stride = strides(shapeId).toInt
        var mDataId = dataId
        val res = new Array[Json](n)

        for (i <- 0.until(n)) {
          val item = shapeGrouped(mDataId, shapeId + 1)
          res(i) = item
          mDataId += stride
        }
        Json.fromValues(res)
      }
    } // def shapeGrouped

    if (data.isEmpty) {
      Json.Null
    } else {
      shapeGrouped(0, 0)
    }
  }
}

object ColumnShaper {
  def apply(tensorShape: Shape): ColumnShaper = {
    tensorShape match {
      case AnyShape => AnyShaper
      case LocalShape(dims) if dims.isEmpty => ScalarShaper
      case LocalShape(dims) => DimShaper(dims)
    }
  }
}