package io.hydrosphere.serving.proto.contract.ops

import io.hydrosphere.serving.proto.contract.errors.MergeError
import io.hydrosphere.serving.proto.contract.errors.MergeError.{IncompatibleShapes, NamesAreDifferent}
import io.hydrosphere.serving.proto.contract.field.ModelField
import io.hydrosphere.serving.proto.contract.field.ModelField.TypeOrSubfields
import io.hydrosphere.serving.proto.contract.field.ModelField.TypeOrSubfields.{Dtype, Subfields}
import io.hydrosphere.serving.proto.contract.signature.SignatureBuilder
import io.hydrosphere.serving.proto.contract.tensor.definitions.Shape
import io.hydrosphere.serving.proto.contract.tensor.definitions.Shape.{AnyShape, LocalShape}

trait ModelFieldOps {

  implicit class ModelFieldPumped(modelField: ModelField) {

    def insert(name: String, fieldInfo: ModelField): Option[ModelField] = {
      modelField.typeOrSubfields match {
        case Subfields(fields) =>
          fields.data.find(_.name == name) match {
            case Some(_) =>
              None
            case None =>
              val newData = fields.data :+ fieldInfo
              Some(SignatureBuilder.nestedField(modelField.name, fieldInfo.shape, newData))
          }
        case _ => None
      }
    }

    def child(name: String): Option[ModelField] = {
      modelField.typeOrSubfields match {
        case Subfields(value) =>
          value.data.find(_.name == name)
        case _ => None
      }
    }

    def search(name: String): Option[ModelField] = {
      modelField.typeOrSubfields match {
        case Subfields(value) =>
          value.data.find(_.name == name).orElse {
            value.data.flatMap(_.search(name)).headOption
          }
        case _ => None
      }
    }
  }

  def mergeAll(inputsA: Seq[ModelField], inputsB: Seq[ModelField]): Either[Seq[MergeError], Seq[ModelField]] = {
    (inputsA, inputsB) match {
      case (Nil, x) => Right(x)
      case (x, Nil) => Right(x)
      case (x, y) if x == y => Right(x)
      case _ =>
        val res = inputsA.zip(inputsB).map {
          case (in1, in2) =>
            if (in1.name == in2.name) {
              merge(in1, in2).right.map(Seq(_))
            } else {
              Right.apply[Seq[MergeError], Seq[ModelField]](Seq(in1, in2))
            }
        }
        val errors = res.filter(_.isLeft)
        if (errors.nonEmpty) {
          Left(errors.flatMap(_.left.get))
        } else {
          Right(res.flatMap(_.right.get))
        }
    }
  }

  def merge(first: ModelField, second: ModelField): Either[Seq[MergeError], ModelField] = {
    if (first == second) {
      Right(first)
    } else if (first.name == second.name) {
      for {
        mergedShape <- mergeShapes(Shape(first.shape), Shape(second.shape))
          .map(Right(_))
          .getOrElse(Left(Seq(IncompatibleShapes(first, second))))
          .right
        mergedType <- mergeTypeOrSubfields(first, second).right
      } yield ModelField(first.name, mergedShape.toProto, mergedType, first.profile)
    } else {
      Left(Seq(NamesAreDifferent(first, second)))
    }
  }

  def mergeShapes(first: Shape, second: Shape): Option[Shape] = {
    first -> second match {
      case (AnyShape, AnyShape) => Some(first)
      case (AnyShape, LocalShape(_)) => Some(second) // todo maybe reconsider any with dim?
      case (LocalShape(_), AnyShape) => Some(first)  // todo maybe reconsider any with dim?
      case (LocalShape(fDims), LocalShape(sDims)) if fDims == sDims => Some(first)
      case (LocalShape(fDims), LocalShape(sDims)) if fDims.length == sDims.length =>
        val res = fDims.zip(sDims).map {
          case (fDim, sDim) if fDim == sDim => Some(fDim)
          case (fDim, sDim) if fDim == -1 => Some(sDim)
          case (fDim, sDim) if sDim == -1 => Some(fDim)
          case (_, _) => None
        }
        if (res.exists(_.isEmpty)) {
          None
        } else {
          val dims = res.map(_.get)
          Some(LocalShape(dims))
        }
      case _ => None
    }
  }

  def mergeTypeOrSubfields(first: ModelField, second: ModelField): Either[Seq[MergeError], TypeOrSubfields] = {
    first.typeOrSubfields -> second.typeOrSubfields match {
      case (fDict: Subfields, sDict: Subfields) =>
        mergeSubfields(fDict, sDict)
      case (fInfo: Dtype, sInfo: Dtype) =>
        mergeTypes(fInfo, sInfo)
          .map(Right(_))
          .getOrElse(Left(Seq(MergeError.incompatibleTypes(first, second))))
      case _ => Left(Seq(MergeError.incompatibleTypes(first, second)))
    }
  }

  def mergeTypes(first: Dtype, second: Dtype): Option[Dtype] = {
    first -> second match {
      case (Dtype(fInfo), Dtype(sInfo)) if fInfo == sInfo => Some(Dtype(fInfo))
      case (Dtype(_), Dtype(_)) => None
    }
  }

  def mergeSubfields(
    first: ModelField.TypeOrSubfields.Subfields,
    second: ModelField.TypeOrSubfields.Subfields
  ): Either[Seq[MergeError], ModelField.TypeOrSubfields.Subfields] = {
    val fields = second.value.data.map { field =>
      val emitterField = first.value.data.find(_.name == field.name)
        .map(Right(_))
        .getOrElse(Left(Seq(MergeError.fieldNotFound(field.name))))
      emitterField.right.flatMap(merge(_, field))
    }
    val errors = fields.filter(_.isLeft)
    if (errors.nonEmpty) {
      Left(errors.flatMap(_.left.get))
    } else {
      val succ = fields.map(_.right.get)
      Right(TypeOrSubfields.Subfields(ModelField.Subfield(succ)))
    }
  }

  def appendAll(outputs: Seq[ModelField], inputs: Seq[ModelField]): Either[Seq[MergeError], Seq[ModelField]] = {
    val fields = inputs.map { input =>
      outputs.find(_.name == input.name)
        .map(Right(_))
        .getOrElse(Left(Seq(MergeError.fieldNotFound(input.name))))
        .right
        .flatMap { output => merge(output, input)}
    }

    val errors = fields.filter(_.isLeft)

    if (errors.nonEmpty) {
      Left(errors.flatMap(_.left.get))
    } else {
      Right(fields.map(_.right.get))
    }
  }

}

object ModelFieldOps extends ModelFieldOps