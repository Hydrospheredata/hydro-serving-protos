import io.circe.Json
import io.circe.parser
import io.circe.optics.JsonOptics._
import io.hydrosphere.serving.proto.contract.tensor.conversions.json.ColumnShaper
import io.hydrosphere.serving.proto.contract.tensor.definitions.Shape
import org.scalatest.funspec.AnyFunSpec

class ColumnShaperSpec extends AnyFunSpec{
  describe("ColumnShaper") {
    it("should shape any-shape tensor") {
      val data: Seq[Json] = Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).map(jsonInt(_))
      val shaper = ColumnShaper(Shape.any)
      assert(shaper.shape(data) === parser.parse("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]").getOrElse(Json.Null))
    }
    it("should shape scalar-shape tensor") {
      val data = Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).map(jsonInt(_))
      val shaper = ColumnShaper(Shape.scalar)
      assert(shaper.shape(data) === jsonInt(1))
    }
    it("should shape full-shaped vector") {
      val data = Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).map(jsonInt(_))
      val shaper = ColumnShaper(Shape.vector(10))
      assert(shaper.shape(data) === parser.parse("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]").getOrElse(Json.Null))
    }
    it("should shape full-shaped matrix") {
      val data = Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).map(jsonInt(_))
      val shaper = ColumnShaper(Shape.mat(2,5))
      assert(shaper.shape(data) === jsonArray(
        Vector(
          jsonArray(Vector(1, 2, 3, 4, 5).map(jsonInt(_))),
          jsonArray(Vector(6, 7, 8, 9, 10).map(jsonInt(_)))
        )
      ))
    }
//    it("should shape partially shaped tensor") {
//      val data = Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).map(jsonInt(_))
//      val shaper = ColumnShaper(Shape.mat(-1,5))
//      assert(shaper.shape(data) === jsonArray(
//        Vector(
//          jsonArray(Vector(1, 2, 3, 4, 5).map(jsonInt(_))),
//          jsonArray(Vector(6, 7, 8, 9, 10).map(jsonInt(_)))
//        )
//      ))
//    }
  }
}
