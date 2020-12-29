import io.hydrosphere.serving.proto.contract.ops.ModelSignatureOps
import io.hydrosphere.serving.proto.contract.signature.{ModelSignature, SignatureBuilder}
import io.hydrosphere.serving.proto.contract.tensor.definitions.Shape
import io.hydrosphere.serving.proto.contract.types.DataType
import org.scalatest.wordspec.AnyWordSpec

class SignatureCheckerSpec extends AnyWordSpec {
  "SignatureChecker" should {
    "accept connection" when {
      "two identical signatures (String,String -> String,String)" in {
        val sig1 = ModelSignature(
          "sig1",
          List(
            SignatureBuilder.simpleTensorModelField("in1", DataType.DT_STRING, Shape.scalar)
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_STRING, Shape.scalar)
          )
        )
        val sig2 = ModelSignature(
          "sig2",
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_STRING, Shape.scalar)
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out2", DataType.DT_STRING, Shape.scalar)
          )
        )
        assert(ModelSignatureOps.append(sig1, sig2).isRight)
      }

      "two identical signatures (Double[5],Double[5] -> Double[5],Double[5])" in {
        val sig1 = ModelSignature(
          "sig1",
          List(
            SignatureBuilder.simpleTensorModelField("in1", DataType.DT_DOUBLE, Shape.vector(5))
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_DOUBLE, Shape.vector(5))
          )
        )
        val sig2 = ModelSignature(
          "sig2",
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_DOUBLE, Shape.vector(5))
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out2", DataType.DT_DOUBLE, Shape.vector(5))

          )
        )
        assert(ModelSignatureOps.append(sig1, sig2).isRight)
      }

      "two compatible signatures (Int32[3] -> Int32[-1])" in {
        val sig1 = ModelSignature(
          "sig1",
          List(
            SignatureBuilder.simpleTensorModelField("in1", DataType.DT_INT32, Shape.vector(3))
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_INT32, Shape.vector(3))
          )
        )
        val sig2 = ModelSignature(
          "sig2",
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_INT32, Shape.vector(-1))
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out2", DataType.DT_INT32, Shape.vector(-1))

          )
        )
        assert(ModelSignatureOps.append(sig1, sig2).isRight)
      }

      "two identical signatures (Double[5, 2] -> Double[5, 2])" in {
        val sig1 = ModelSignature(
          "sig1",
          List(
            SignatureBuilder.simpleTensorModelField("in1", DataType.DT_DOUBLE, Shape.mat(5, 2))
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_DOUBLE, Shape.mat(5, 2))
          )
        )
        val sig2 = ModelSignature(
          "sig2",
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_DOUBLE, Shape.mat(5, 2))
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out2", DataType.DT_DOUBLE, Shape.mat(5, 2))

          )
        )
        assert(ModelSignatureOps.append(sig1, sig2).isRight)
      }

      "two compatible signatures (Double[5, 2] -> Double[5, -1])" in {
        val sig1 = ModelSignature(
          "sig1",
          List(
            SignatureBuilder.simpleTensorModelField("in1", DataType.DT_DOUBLE, Shape.mat(5, 2))
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_DOUBLE, Shape.mat(5, 2))
          )
        )
        val sig2 = ModelSignature(
          "sig2",
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_DOUBLE, Shape.mat(5, -1))
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out2", DataType.DT_DOUBLE, Shape.mat(5, -1))

          )
        )
        assert(ModelSignatureOps.append(sig1, sig2).isRight)
      }

    }

    "decline connection" when {
      "two completely different signatures (String -> Int32)" in {
        val sig1 = ModelSignature(
          "sig1",
          List(
            SignatureBuilder.simpleTensorModelField("in1", DataType.DT_STRING, Shape.scalar)
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_STRING, Shape.scalar)
          )
        )
        val sig2 = ModelSignature(
          "sig2",
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_INT32, Shape.scalar)
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out2", DataType.DT_INT32, Shape.scalar)
          )
        )
        assert(ModelSignatureOps.append(sig1, sig2).isLeft)
      }

      "two completely different signatures (String[3] -> String[4])" in {
        val sig1 = ModelSignature(
          "sig1",
          List(
            SignatureBuilder.simpleTensorModelField("in1", DataType.DT_STRING, Shape.vector(3))
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_STRING, Shape.vector(3))
          )
        )
        val sig2 = ModelSignature(
          "sig2",
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_INT32, Shape.vector(4))
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out2", DataType.DT_INT32, Shape.vector(4))
          )
        )
        assert(ModelSignatureOps.append(sig1, sig2).isLeft)
      }

      "two completely different signatures (Double[4] -> Double[3])" in {
        val sig1 = ModelSignature(
          "sig1",
          List(
            SignatureBuilder.simpleTensorModelField("in1", DataType.DT_DOUBLE, Shape.vector(4))
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_DOUBLE, Shape.vector(4))
          )
        )
        val sig2 = ModelSignature(
          "sig2",
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_DOUBLE, Shape.vector(3))
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out2", DataType.DT_DOUBLE, Shape.vector(3))
          )
        )
        assert(ModelSignatureOps.append(sig1, sig2).isLeft)
      }

      "two signatures when receiver has empty input signature" in {
        val sig1 = ModelSignature(
          "sig1",
          List(
            SignatureBuilder.simpleTensorModelField("in1", DataType.DT_DOUBLE, Shape.vector(4))
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out1", DataType.DT_DOUBLE, Shape.vector(4))
          )
        )
        val sig2 = ModelSignature(
          "sig2",
          List(),
          List(
            SignatureBuilder.simpleTensorModelField("out2", DataType.DT_DOUBLE, Shape.vector(4))
          )
        )
        assert(ModelSignatureOps.append(sig1, sig2).isLeft)
      }

      "two signatures when emitter has empty output signature" in {
        val sig1 = ModelSignature(
          "sig1",
          List(
            SignatureBuilder.simpleTensorModelField("in1", DataType.DT_DOUBLE, Shape.vector(4))
          ),
          List()
        )
        val sig2 = ModelSignature(
          "sig2",
          List(
            SignatureBuilder.simpleTensorModelField("in1", DataType.DT_DOUBLE, Shape.vector(4))
          ),
          List(
            SignatureBuilder.simpleTensorModelField("out2", DataType.DT_DOUBLE, Shape.vector(4))
          )
        )
        assert(ModelSignatureOps.append(sig1, sig2).isLeft)
      }
    }
  }
}