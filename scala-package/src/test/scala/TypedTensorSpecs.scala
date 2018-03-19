import com.google.protobuf.ByteString
import io.hydrosphere.serving.contract.utils.DataGenerator
import io.hydrosphere.serving.tensorflow.TensorShape
import io.hydrosphere.serving.tensorflow.tensor._
import io.hydrosphere.serving.tensorflow.types.DataType
import org.scalatest.WordSpec

class TypedTensorSpecs extends WordSpec {
  val shape = TensorShape.mat(2, 2)

  def test[TFactory <: TypedTensorFactory[_ <: TypedTensor[_ <: DataType]]]
  (factory: TFactory) = {
    factory.getClass.getSimpleName in {
      val tensor = DataGenerator.generateTensor(shape, factory.empty.dtype)
      assert(tensor.get === factory.fromProto(tensor.get.toProto))
    }
  }

  "Scalar tensors" should {
    "convert to TensorProto" when {
      test(BoolTensor)

      test(DoubleTensor)
      test(FloatTensor)

      test(StringTensor)

      test(SComplexTensor)
      test(DComplexTensor)

      test(Int8Tensor)
      test(Int16Tensor)
      test(Int32Tensor)
      test(Int64Tensor)

      test(Uint8Tensor)
      test(Uint16Tensor)
      test(Uint32Tensor)
      test(Uint64Tensor)
    }
  }

  "Map tensor" should {
    "convert to TensorProto" in {
      val expected = TensorProto(
        dtype = DataType.DT_MAP,
        tensorShape = None,
        mapVal = Seq(
          MapTensorData(
            Map(
              "name" -> TensorProto(
                dtype = DataType.DT_STRING,
                tensorShape = None,
                stringVal = Seq(ByteString.copyFromUtf8("John"))
              ),
              "surname" -> TensorProto(
                dtype = DataType.DT_STRING,
                tensorShape = None,
                stringVal = Seq(ByteString.copyFromUtf8("Doe"))
              )
            )
          )
        )
      )

      assert(TypedTensorFactory.create(expected).toProto === expected)
    }
  }
}
