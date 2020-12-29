package io.hydrosphere.serving.proto.contract.tensor.conversions.json

import io.circe._
import io.hydrosphere.serving.proto.contract.tensor.definitions.{TensorUtil, _}

trait TensorJsonLens[T <: TypedTensor[_]] {
  def convert: T#Self#DataT => Json

  final def get(tensor: T): Seq[Json] = tensor.data.map(convert)

  final def toJson(tensor: T): Json = {
    val vTensor: T = TensorUtil.verifyShape(tensor.asInstanceOf[TypedTensor[_]]).get.asInstanceOf[T]
    val shaper = ColumnShaper(vTensor.shape)
    shaper.shape(get(vTensor))
  }
}

object TensorJsonLens {
  def toJson(t: TypedTensor[_]): Json = {
    t match {
      case x: MapTensor => MapToJson.toJson(x)
      case x: DoubleTensor => DoubleToJson.toJson(x)
      case x: Int64Tensor => Int64ToJson.toJson(x)
      case x: FloatTensor => FloatToJson.toJson(x)
      case x: Uint64Tensor => Uint64ToJson.toJson(x)
      case x: BoolTensor => BoolToJson.toJson(x)
      case x: SComplexTensor => SComplexToJson.toJson(x)
      case x: DComplexTensor => DComplexToJson.toJson(x)
      case x: StringTensor => StringToJson.toJson(x)
      case x: IntTensor[_] => IntToJson.toJson(x)
      case x => throw new IllegalArgumentException(s"Cant convert unknown tensor $x to json")
    }
  }

  def mapToJson(tensors: Map[String, TypedTensor[_]]): Json = {
    Json.fromFields(tensors.view.mapValues(toJson))
  }
}