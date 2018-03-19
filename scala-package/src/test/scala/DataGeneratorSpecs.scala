import com.google.protobuf.ByteString
import io.hydrosphere.serving.contract.model_signature.ModelSignature
import io.hydrosphere.serving.contract.utils.{ContractBuilders, DataGenerator}
import io.hydrosphere.serving.tensorflow.TensorShape
import io.hydrosphere.serving.tensorflow.tensor.{MapTensorData, TensorProto, TypedTensorFactory}
import io.hydrosphere.serving.tensorflow.types.DataType
import org.scalatest.WordSpec

class DataGeneratorSpecs extends WordSpec {
  val fooString = ByteString.copyFromUtf8("foo")

  "DataGenerator" should {
    "generate correct example" when {
      "scalar flat signature" in {
        val sig1 = ModelSignature(
          "sig1",
          List(
            ContractBuilders.simpleTensorModelField("in1", DataType.DT_STRING, None)
          ),
          List(
            ContractBuilders.simpleTensorModelField("out1", DataType.DT_DOUBLE, Some(List(-1)))
          )
        )

        val expected = Map(
          "in1" -> TypedTensorFactory.create(
            TensorProto(
              dtype = DataType.DT_STRING,
              tensorShape = None,
              stringVal = List(fooString)
            )
          )
        )

        val generated = DataGenerator(sig1).generateInputs
        assert(generated === expected)
      }

      "vector flat signature" in {
        val sig1 = ModelSignature(
          "sig1",
          List(
            ContractBuilders.simpleTensorModelField("in1", DataType.DT_STRING, Some(List(-1))),
            ContractBuilders.simpleTensorModelField("in2", DataType.DT_INT32, Some(List(3)))
          ),
          List(
            ContractBuilders.simpleTensorModelField("out1", DataType.DT_DOUBLE, Some(List(-1)))
          )
        )

        val expected = Map(
          "in1" -> TypedTensorFactory.create(
            TensorProto(
              dtype = DataType.DT_STRING,
              tensorShape = TensorShape.vector(-1).toProto,
              stringVal = List(fooString)
            )
          ),
          "in2" -> TypedTensorFactory.create(
            TensorProto(
              dtype = DataType.DT_INT32,
              tensorShape = TensorShape.vector(3).toProto,
              intVal = List(1, 1, 1)
            )
          )
        )

        val generated = DataGenerator(sig1).generateInputs
        assert(generated === expected)
      }

      "nested singular signature" in {
        val sig1 = ModelSignature(
          "sig1",
          List(
            ContractBuilders.complexField(
              "in1",
              None,
              Seq(
                ContractBuilders.simpleTensorModelField("a", DataType.DT_STRING, None),
                ContractBuilders.simpleTensorModelField("b", DataType.DT_STRING, None)
              )
            )
          ),
          List(
            ContractBuilders.simpleTensorModelField("out1", DataType.DT_DOUBLE, Some(List(-1)))
          )
        )

        val expected = Map(
          "in1" ->
            TypedTensorFactory.create(
              TensorProto(
                dtype = DataType.DT_MAP,
                tensorShape = None,
                mapVal = Seq(
                  MapTensorData(
                    Map(
                      "a" -> TensorProto(DataType.DT_STRING, None, stringVal = List(fooString)),
                      "b" -> TensorProto(DataType.DT_STRING, None, stringVal = List(fooString))
                    )
                  )
                )
              )
            )
        )

        val generated = DataGenerator(sig1).generateInputs
        assert(generated === expected)
      }

      "nested vector signature" in {
        val sig1 = ModelSignature(
          "sig1",
          List(
            ContractBuilders.complexField(
              "in1",
              TensorShape.vector(3).toProto,
              Seq(
                ContractBuilders.simpleTensorModelField("a", DataType.DT_STRING, None),
                ContractBuilders.simpleTensorModelField("b", DataType.DT_STRING, None)
              )
            )
          ),
          List(
            ContractBuilders.simpleTensorModelField("out1", DataType.DT_DOUBLE, Some(List(-1)))
          )
        )

        val expected = Map(
          "in1" -> TypedTensorFactory.create(
            TensorProto(
              dtype = DataType.DT_MAP,
              tensorShape = TensorShape.vector(3).toProto,
              mapVal = Seq(
                MapTensorData(
                  Map(
                    "a" -> TensorProto(DataType.DT_STRING, None, stringVal = List(fooString)),
                    "b" -> TensorProto(DataType.DT_STRING, None, stringVal = List(fooString))
                  )
                ),
                MapTensorData(
                  Map(
                    "a" -> TensorProto(DataType.DT_STRING, None, stringVal = List(fooString)),
                    "b" -> TensorProto(DataType.DT_STRING, None, stringVal = List(fooString))
                  )
                ),
                MapTensorData(
                  Map(
                    "a" -> TensorProto(DataType.DT_STRING, None, stringVal = List(fooString)),
                    "b" -> TensorProto(DataType.DT_STRING, None, stringVal = List(fooString))
                  )
                )
              )
            )
          )
        )

        val generated = DataGenerator(sig1).generateInputs
        assert(generated === expected)
      }
    }
  }
}