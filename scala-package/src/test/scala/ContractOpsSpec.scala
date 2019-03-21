import io.hydrosphere.serving.contract.model_signature.ModelSignature
import io.hydrosphere.serving.contract.utils.ContractBuilders
import io.hydrosphere.serving.model.api.ops.ModelSignatureOps
import io.hydrosphere.serving.tensorflow.TensorShape
import io.hydrosphere.serving.tensorflow.types.DataType
import org.scalatest.WordSpec


class ContractOpsSpec extends WordSpec {

  "ContractOps" can {
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
              ContractBuilders.simpleTensorModelField("in2", DataType.DT_INT32, TensorShape.scalar)
            ),
            List(
              ContractBuilders.simpleTensorModelField("out2", DataType.DT_INT32, TensorShape.vector(3))
            )
          )

          val expectedSig = ModelSignature(
            "sig1&sig2",
            List(
              ContractBuilders.simpleTensorModelField("in2", DataType.DT_INT32, TensorShape.scalar)
            ),
            List(
              ContractBuilders.simpleTensorModelField("out2", DataType.DT_INT32, TensorShape.vector(3))
            )
          )
          val res = ModelSignatureOps.merge(sig1, sig2)
          assert(res.right.get === expectedSig, res)
        }

        "signatures don't overlap" in {
          val sig1 = ModelSignature(
            "sig1",
            List(
              ContractBuilders.simpleTensorModelField("in1", DataType.DT_STRING, TensorShape.scalar)
            ),
            List(
              ContractBuilders.simpleTensorModelField("out1", DataType.DT_DOUBLE, TensorShape.vector(-1))
            )
          )
          val sig2 = ModelSignature(
            "sig2",
            List(
              ContractBuilders.simpleTensorModelField("in2", DataType.DT_INT32, TensorShape.scalar)
            ),
            List(
              ContractBuilders.simpleTensorModelField("out2", DataType.DT_INT32, TensorShape.vector(3))
            )
          )

          val expectedSig = ModelSignature(
            "sig1&sig2",
            List(
              ContractBuilders.simpleTensorModelField("in1", DataType.DT_STRING, TensorShape.scalar),
              ContractBuilders.simpleTensorModelField("in2", DataType.DT_INT32, TensorShape.scalar)
            ),
            List(
              ContractBuilders.simpleTensorModelField("out1", DataType.DT_DOUBLE, TensorShape.vector(-1)),
              ContractBuilders.simpleTensorModelField("out2", DataType.DT_INT32, TensorShape.vector(3))
            )
          )
          val res = ModelSignatureOps.merge(sig1, sig2)
          assert(res.right.get === expectedSig, res)
        }

        "inputs are overlapping and compatible" in {
          val sig1 = ModelSignature(
            "sig1",
            List(
              ContractBuilders.simpleTensorModelField("in1", DataType.DT_INT32, TensorShape.vector(-1))
            ),
            List(
              ContractBuilders.simpleTensorModelField("out1", DataType.DT_DOUBLE, TensorShape.vector(-1))
            )
          )
          val sig2 = ModelSignature(
            "sig2",
            List(
              ContractBuilders.simpleTensorModelField("in1", DataType.DT_INT32, TensorShape.vector(3))
            ),
            List(
              ContractBuilders.simpleTensorModelField("out2", DataType.DT_INT32, TensorShape.vector(3))
            )
          )

          val expectedSig = ModelSignature(
            "sig1&sig2",
            List(
              ContractBuilders.simpleTensorModelField("in1", DataType.DT_INT32, TensorShape.vector(3))
            ),
            List(
              ContractBuilders.simpleTensorModelField("out1", DataType.DT_DOUBLE, TensorShape.vector(-1)),
              ContractBuilders.simpleTensorModelField("out2", DataType.DT_INT32, TensorShape.vector(3))
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
              ContractBuilders.simpleTensorModelField("in1", DataType.DT_STRING, TensorShape.scalar)
            ),
            List(
              ContractBuilders.simpleTensorModelField("out1", DataType.DT_DOUBLE, TensorShape.vector(-1))
            )
          )
          val sig2 = ModelSignature(
            "sig2",
            List(
              ContractBuilders.simpleTensorModelField("in1", DataType.DT_INT32, TensorShape.scalar)
            ),
            List(
              ContractBuilders.simpleTensorModelField("out2", DataType.DT_INT32, TensorShape.vector(3))
            )
          )

          assert(ModelSignatureOps.merge(sig1, sig2).isLeft)
        }

        "outputs overlap is conflicting" in {
          val sig1 = ModelSignature(
            "sig1",
            List(
              ContractBuilders.simpleTensorModelField("in1", DataType.DT_STRING, TensorShape.scalar)
            ),
            List(
              ContractBuilders.simpleTensorModelField("out1", DataType.DT_DOUBLE, TensorShape.vector(-1))
            )
          )
          val sig2 = ModelSignature(
            "sig2",
            List(
              ContractBuilders.simpleTensorModelField("in2", DataType.DT_INT32, TensorShape.scalar)
            ),
            List(
              ContractBuilders.simpleTensorModelField("out1", DataType.DT_INT32, TensorShape.vector(3))
            )
          )

          assert(ModelSignatureOps.merge(sig1, sig2).isLeft)
        }
      }
    }
  }
}