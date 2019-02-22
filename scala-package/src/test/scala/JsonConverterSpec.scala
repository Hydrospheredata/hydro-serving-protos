import io.hydrosphere.serving.model.api.json.TensorJsonLens
import io.hydrosphere.serving.tensorflow.TensorShape
import io.hydrosphere.serving.tensorflow.tensor.DoubleTensor
import org.scalatest.FunSpec
import spray.json.{JsArray, JsNumber}

class JsonConverterSpec extends FunSpec {
  describe("Json Tensor converters") {
    it("should convert tensor without dims") {
      val t1 = DoubleTensor(TensorShape.any, Seq(1, 2, 3, 4))
      val t2 = DoubleTensor(TensorShape.any, Seq(1))

      val res1 = TensorJsonLens.toJson(t1)
      val res2 = TensorJsonLens.toJson(t2)
      assert(res1 === JsArray(Vector(JsNumber(1), JsNumber(2), JsNumber(3), JsNumber(4))))
      assert(res2 === JsArray(Vector(JsNumber(1))))
    }
    it("should convert scalar tensor") {
      val t1 = DoubleTensor(TensorShape.scalar, Seq(1))

      val res1 = TensorJsonLens.toJson(t1)
      assert(res1 === JsNumber(1))
    }
    it("should convert tensor with dims") {
      val t1 = DoubleTensor(TensorShape.vector(-1), Seq(1, 2, 3, 4))

      val res1 = TensorJsonLens.toJson(t1)
      assert(res1 === JsArray(Vector(JsNumber(1), JsNumber(2), JsNumber(3), JsNumber(4))))
    }
  }
}