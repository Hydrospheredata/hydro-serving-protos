import io.hydrosphere.serving.proto.contract.ops.ModelSignatureOps
import io.hydrosphere.serving.proto.contract.signature.{ModelSignature, SignatureBuilder}
import io.hydrosphere.serving.proto.contract.tensor.TensorShape
import io.hydrosphere.serving.proto.contract.tensor.definitions.Shape
import io.hydrosphere.serving.proto.contract.types.DataType
import org.scalatest.wordspec.AnyWordSpec


class SignatureOpsSpec extends AnyWordSpec {

  "SignatureOps" can {
    "merge" should {
      "success" when {
        "empty and non-empty" in {
          val sig1 = ModelSignature(
            "sig1",
            List.empty,
            List.empty
          )
          val sig2 = ModelSignature(
            "sig2",
            List(
              SignatureBuilder.simpleTensorModelField("in2", DataType.DT_INT32, Shape.scalar)
            ),
            List(
              SignatureBuilder.simpleTensorModelField("out2", DataType.DT_INT32, Shape.vector(3))
            )
          )

          val expectedSig = ModelSignature(
            "sig1&sig2",
            List(
              SignatureBuilder.simpleTensorModelField("in2", DataType.DT_INT32, Shape.scalar)
            ),
            List(
              SignatureBuilder.simpleTensorModelField("out2", DataType.DT_INT32, Shape.vector(3))
            )
          )
          val res = ModelSignatureOps.merge(sig1, sig2)
          assert(res.right.get === expectedSig, res)
        }

        "signatures don't overlap" in {
          val sig1 = ModelSignature(
            "sig1",
            List(
              SignatureBuilder.simpleTensorModelField("in1", DataType.DT_STRING, Shape.scalar)
            ),
            List(
              SignatureBuilder.simpleTensorModelField("out1", DataType.DT_DOUBLE, Shape.vector(-1))
            )
          )
          val sig2 = ModelSignature(
            "sig2",
            List(
              SignatureBuilder.simpleTensorModelField("in2", DataType.DT_INT32, Shape.scalar)
            ),
            List(
              SignatureBuilder.simpleTensorModelField("out2", DataType.DT_INT32, Shape.vector(3))
            )
          )

          val expectedSig = ModelSignature(
            "sig1&sig2",
            List(
              SignatureBuilder.simpleTensorModelField("in1", DataType.DT_STRING, Shape.scalar),
              SignatureBuilder.simpleTensorModelField("in2", DataType.DT_INT32, Shape.scalar)
            ),
            List(
              SignatureBuilder.simpleTensorModelField("out1", DataType.DT_DOUBLE, Shape.vector(-1)),
              SignatureBuilder.simpleTensorModelField("out2", DataType.DT_INT32, Shape.vector(3))
            )
          )
          val res = ModelSignatureOps.merge(sig1, sig2)
          assert(res.right.get === expectedSig, res)
        }

        "inputs are overlapping and compatible" in {
          val sig1 = ModelSignature(
            "sig1",
            List(
              SignatureBuilder.simpleTensorModelField("in1", DataType.DT_INT32, Shape.vector(-1))
            ),
            List(
              SignatureBuilder.simpleTensorModelField("out1", DataType.DT_DOUBLE, Shape.vector(-1))
            )
          )
          val sig2 = ModelSignature(
            "sig2",
            List(
              SignatureBuilder.simpleTensorModelField("in1", DataType.DT_INT32, Shape.vector(3))
            ),
            List(
              SignatureBuilder.simpleTensorModelField("out2", DataType.DT_INT32, Shape.vector(3))
            )
          )

          val expectedSig = ModelSignature(
            "sig1&sig2",
            List(
              SignatureBuilder.simpleTensorModelField("in1", DataType.DT_INT32, Shape.vector(3))
            ),
            List(
              SignatureBuilder.simpleTensorModelField("out1", DataType.DT_DOUBLE, Shape.vector(-1)),
              SignatureBuilder.simpleTensorModelField("out2", DataType.DT_INT32, Shape.vector(3))
            )
          )
          val res = ModelSignatureOps.merge(sig1, sig2)
          assert(res.isRight, res)
          assert(res.right.get === expectedSig, res)
        }
      }

      "fail" when {
        "inputs overlap is conflicting" in {
          val sig1 = ModelSignature(
            "sig1",
            List(
              SignatureBuilder.simpleTensorModelField("in1", DataType.DT_STRING, Shape.scalar)
            ),
            List(
              SignatureBuilder.simpleTensorModelField("out1", DataType.DT_DOUBLE, Shape.vector(-1))
            )
          )
          val sig2 = ModelSignature(
            "sig2",
            List(
              SignatureBuilder.simpleTensorModelField("in1", DataType.DT_INT32, Shape.scalar)
            ),
            List(
              SignatureBuilder.simpleTensorModelField("out2", DataType.DT_INT32, Shape.vector(3))
            )
          )

          assert(ModelSignatureOps.merge(sig1, sig2).isLeft)
        }

        "outputs overlap is conflicting" in {
          val sig1 = ModelSignature(
            "sig1",
            List(
              SignatureBuilder.simpleTensorModelField("in1", DataType.DT_STRING, Shape.scalar)
            ),
            List(
              SignatureBuilder.simpleTensorModelField("out1", DataType.DT_DOUBLE, Shape.vector(-1))
            )
          )
          val sig2 = ModelSignature(
            "sig2",
            List(
              SignatureBuilder.simpleTensorModelField("in2", DataType.DT_INT32, Shape.scalar)
            ),
            List(
              SignatureBuilder.simpleTensorModelField("out1", DataType.DT_INT32, Shape.vector(3))
            )
          )

          assert(ModelSignatureOps.merge(sig1, sig2).isLeft)
        }
      }
    }
  }
}