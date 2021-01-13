package io.hydrosphere.serving.proto.contract.tensor.definitions

import io.hydrosphere.serving.proto.contract.tensor.{MapTensorData, Tensor}
import io.hydrosphere.serving.proto.contract.types.DataType

case class MapTensor(shape: Shape, data: Seq[Map[String, TypedTensor[_]]]) extends TypedTensor[DataType.DT_MAP.type] {
  override type Self = MapTensor

  override type DataT = Map[String, TypedTensor[_]]

  override def dtype = DataType.DT_MAP

  override def factory = MapTensor
}

object MapTensor extends TypedTensorFactory[MapTensor] {
  override implicit def lens: TensorProtoLens[MapTensor] = new TensorProtoLens[MapTensor] {
    override def getter: Tensor => Seq[Map[String, TypedTensor[_]]] = { tensor =>
      tensor.mapVal.map {
        _.subtensors.mapValues(TypedTensorFactory.create).toMap
      }
    }

    override def setter: (Tensor, Seq[Map[String, TypedTensor[_]]]) => Tensor = { (tensor, maps) =>
      val protoMaps = maps.map { tensorMap =>
        MapTensorData(tensorMap.mapValues(_.toProto).toMap)
      }
      tensor.withMapVal(protoMaps)
    }
  }

  override def constructor = MapTensor.apply
}