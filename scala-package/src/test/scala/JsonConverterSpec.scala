import io.circe.{Json, parser}
import io.hydrosphere.serving.proto.contract.tensor.conversions.json.TensorJsonLens
import io.hydrosphere.serving.proto.contract.tensor.definitions.{DoubleTensor, Shape}
import org.scalatest.funspec.AnyFunSpec

class JsonConverterSpec extends AnyFunSpec {
  describe("Json Tensor converters") {
    it("should convert tensor without dims") {
      val t1 = DoubleTensor(Shape.any, Seq(1, 2, 3, 4))
      val t2 = DoubleTensor(Shape.any, Seq(1))

      val res1 = TensorJsonLens.toJson(t1)
      val res2 = TensorJsonLens.toJson(t2)

      assert(res1 === parser.parse("[1, 2, 3, 4]").getOrElse(Json.Null))
      assert(res2 === parser.parse("[1]").getOrElse(Json.Null))
    }
    it("should convert scalar tensor") {
      val t1 = DoubleTensor(Shape.scalar, Seq(1))

      val res1 = TensorJsonLens.toJson(t1)
      assert(res1 === parser.parse("1").getOrElse(Json.Null))
    }
    it("should convert tensor with dims") {
      val t1 = DoubleTensor(Shape.vector(-1), Seq(1, 2, 3, 4))

      val res1 = TensorJsonLens.toJson(t1)
      assert(res1 === parser.parse("[1, 2, 3, 4]").getOrElse(Json.Null))
    }
  }
}