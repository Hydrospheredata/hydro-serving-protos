import io.hydrosphere.serving.model.api.json.ColumnShaper
import io.hydrosphere.serving.tensorflow.TensorShape
import org.scalatest.funspec.AnyFunSpec
import spray.json.{JsArray, JsNumber}

class ColumnShaperSpec extends AnyFunSpec{
  describe("ColumnShaper") {
    it("should shape any-shape tensor") {
      val data = Seq(1,2,3,4,5,6,7,8,9,10).map(JsNumber.apply)
      val shaper = ColumnShaper(TensorShape.any)
      assert(shaper.shape(data) === JsArray(data.toVector))
    }
    it("should shape scalar-shape tensor") {
      val data = Seq(1,2,3,4,5,6,7,8,9,10).map(JsNumber.apply)
      val shaper = ColumnShaper(TensorShape.scalar)
      assert(shaper.shape(data) === JsNumber(1))
    }
    it("should shape full-shaped vector") {
      val data = Seq(1,2,3,4,5,6,7,8,9,10).map(JsNumber.apply)
      val shaper = ColumnShaper(TensorShape.vector(10))
      assert(shaper.shape(data) === JsArray(data.toVector))
    }
    it("should shape full-shaped matrix") {
      val data = Seq(1,2,3,4,5,6,7,8,9,10).map(JsNumber.apply)
      val shaper = ColumnShaper(TensorShape.mat(2,5))
      assert(shaper.shape(data) === JsArray(
        JsArray(JsNumber(1),JsNumber(2),JsNumber(3),JsNumber(4),JsNumber(5)),
        JsArray(JsNumber(6),JsNumber(7),JsNumber(8),JsNumber(9),JsNumber(10))
      ))
    }
//    it("should shape partially shaped tensor") {
//      val data = Seq(1,2,3,4,5,6,7,8,9,10).map(JsNumber.apply)
//      val shaper = ColumnShaper(TensorShape.mat(-1,5))
//      assert(shaper.shape(data) === JsArray(
//        JsArray(JsNumber(1),JsNumber(2),JsNumber(3),JsNumber(4),JsNumber(5)),
//        JsArray(JsNumber(6),JsNumber(7),JsNumber(8),JsNumber(9),JsNumber(10))
//      ))
//    }
  }
}
