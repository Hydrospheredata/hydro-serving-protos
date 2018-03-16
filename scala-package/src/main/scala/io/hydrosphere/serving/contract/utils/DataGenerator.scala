package io.hydrosphere.serving.contract.utils

import io.hydrosphere.serving.contract.model_contract.ModelContract
import io.hydrosphere.serving.contract.model_field.ModelField
import io.hydrosphere.serving.contract.model_field.ModelField.TypeOrSubfields.{Dtype, Empty, Subfields}
import io.hydrosphere.serving.contract.model_signature.ModelSignature
import io.hydrosphere.serving.tensorflow.tensor.{MapTensorData, TensorProto, TypedTensor}
import io.hydrosphere.serving.tensorflow.tensor_shape.TensorShapeProto
import io.hydrosphere.serving.tensorflow.types.DataType
import io.hydrosphere.serving.tensorflow.types.DataType._

class DataGenerator(val modelApi: ModelSignature) {
  def generateInputs: Map[String, TensorProto] = {
    modelApi.inputs.flatMap(DataGenerator.generateField).toMap
  }

  def generateOutputs: Map[String, TensorProto] = {
    modelApi.outputs.flatMap(DataGenerator.generateField).toMap
  }
}

object DataGenerator {
  def apply(modelApi: ModelSignature): DataGenerator = new DataGenerator(modelApi)

  def forContract(modelContract: ModelContract, signature: String): Option[DataGenerator] = {
    modelContract.signatures.find(_.signatureName == signature).map(DataGenerator.apply)
  }

  def generateScalarData(dataType: DataType): Any = {
    dataType match {
      case DT_FLOAT | DT_COMPLEX64   => 1.0F
      case DT_DOUBLE | DT_COMPLEX128 => 1.0D
      case DT_INT8 | DT_INT16 | DT_INT32 | DT_UINT8 | DT_UINT16 | DT_UINT32 | DT_QINT8 | DT_QINT16 |
          DT_QINT32 | DT_QUINT8 | DT_QUINT16 =>
        1
      case DT_INT64 | DT_UINT64 => 1L
      case DT_STRING            => "foo"
      case DT_BOOL              => true

      case DT_INVALID =>
        throw new IllegalArgumentException(
          s"Can't convert data to DT_INVALID  has an invalid dtype"
        )
      case x => throw new IllegalArgumentException(s"Cannot process Tensor with $x dtype") // refs
    }
  }

  def generateScalarCollection(dataType: DataType, shape: Option[TensorShapeProto]): Seq[Any] = {
    createFlatTensor(shape, generateScalarData(dataType))
  }

  def createFlatTensor[T](shape: Option[TensorShapeProto], generator: => T): Seq[T] = {
    shape match {
      case Some(sh) =>
        val flatLen = sh.dim.map(_.size.max(1)).product
        (0L to flatLen).map(_ => generator)
      case None => List(generator)
    }
  }

  def generateTensor(dtype: DataType, shape: Option[TensorShapeProto]): TensorProto = {
    val tensor = TensorProto(dtype = dtype, tensorShape = shape)
    val data = generateScalarCollection(dtype, shape)
    val typedTensor = TypedTensor(tensor)
    typedTensor.putAny(data).right.get
  }

  def generateField(field: ModelField): Map[String, TensorProto] = {
    val tensor = field.typeOrSubfields match {
      case Empty       => TensorProto()
      case Dtype(value) => generateTensor(value, field.shape)
      case Subfields(value) => generateNestedTensor(field.shape, value)
    }
    Map(field.name -> tensor)
  }

  private def generateNestedTensor(shape: Option[TensorShapeProto], value: ModelField.Subfield) = {
    val tensor = TensorProto(dtype = DataType.DT_MAP)
    val map = generateMap(value)
    val tensorData = createFlatTensor(shape, map)
    tensor.withMapVal(tensorData)
  }

  private def generateMap(value: ModelField.Subfield): MapTensorData = {
    MapTensorData(value.data.flatMap(generateField).toMap)
  }
}
