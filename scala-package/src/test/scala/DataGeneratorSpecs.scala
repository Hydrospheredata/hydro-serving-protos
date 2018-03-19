import com.google.protobuf.ByteString
import io.hydrosphere.serving.contract.model_signature.ModelSignature
import io.hydrosphere.serving.contract.utils.{ContractBuilders, DataGenerator}
import io.hydrosphere.serving.tensorflow.tensor.{MapTensorData, TensorProto}
import io.hydrosphere.serving.tensorflow.types.DataType
import io.hydrosphere.serving.tensorflow.utils.ops.TensorShapeProtoOps
import org.scalatest.WordSpec

class DataGeneratorSpecs extends WordSpec {
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
          "in1" -> TensorProto(dtype = DataType.DT_STRING, tensorShape = None, stringVal = List(ByteString.copyFromUtf8("foo")))
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
          "in1" -> TensorProto(dtype = DataType.DT_STRING, tensorShape = Some(TensorShapeProtoOps.seqToShape(List(-1))), stringVal = List(ByteString.copyFromUtf8("foo"))),
          "in2" -> TensorProto(dtype = DataType.DT_INT32, tensorShape = Some(TensorShapeProtoOps.seqToShape(List(3))), intVal = List(1, 1, 1))
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
          "in1" -> TensorProto(
            dtype = DataType.DT_MAP,
            tensorShape = None,
            mapVal = Seq(
              MapTensorData(
                Map(
                  "a" -> TensorProto(DataType.DT_STRING, None, stringVal = List(ByteString.copyFromUtf8("foo"))),
                  "b" -> TensorProto(DataType.DT_STRING, None, stringVal = List(ByteString.copyFromUtf8("foo")))
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
              TensorShapeProtoOps.maybeSeqToShape(Some(Seq(3))),
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
          "in1" -> TensorProto(
            dtype = DataType.DT_MAP,
            tensorShape = None,
            mapVal = Seq(
              MapTensorData(
                Map(
                  "a" -> TensorProto(DataType.DT_STRING, None, stringVal = List(ByteString.copyFromUtf8("foo"))),
                  "b" -> TensorProto(DataType.DT_STRING, None, stringVal = List(ByteString.copyFromUtf8("foo")))
                )
              ),
              MapTensorData(
                Map(
                  "a" -> TensorProto(DataType.DT_STRING, None, stringVal = List(ByteString.copyFromUtf8("foo"))),
                  "b" -> TensorProto(DataType.DT_STRING, None, stringVal = List(ByteString.copyFromUtf8("foo")))
                )
              ),
              MapTensorData(
                Map(
                  "a" -> TensorProto(DataType.DT_STRING, None, stringVal = List(ByteString.copyFromUtf8("foo"))),
                  "b" -> TensorProto(DataType.DT_STRING, None, stringVal = List(ByteString.copyFromUtf8("foo")))
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