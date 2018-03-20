package io.hydrosphere.serving.contract.utils.ops

import io.hydrosphere.serving.contract.utils.description.FieldDescription
import io.hydrosphere.serving.contract.model_field.ModelField
import io.hydrosphere.serving.contract.model_field.ModelField.TypeOrSubfields.{Dtype, Empty, Subfields}
import io.hydrosphere.serving.contract.utils.ContractBuilders
import io.hydrosphere.serving.tensorflow.TensorShape
import io.hydrosphere.serving.tensorflow.utils.ops.{DataTypeOps, TensorShapeProtoOps}

trait ModelFieldOps {

  implicit class ModelFieldPumped(modelField: ModelField) {

    def flatten(rootName: String = ""): Seq[FieldDescription] = {
      ModelFieldOps.flatten(rootName, modelField)
    }

    def insert(name: String, fieldInfo: ModelField): Option[ModelField] = {
      modelField.typeOrSubfields match {
        case Subfields(fields) =>
          fields.data.find(_.name == name) match {
            case Some(_) =>
              None
            case None =>
              val newData = fields.data :+ fieldInfo
              Some(ContractBuilders.complexField(modelField.name, fieldInfo.shape, newData))
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

  def mergeAll(inputs: Seq[ModelField], inputs1: Seq[ModelField]): Seq[ModelField] = {
    inputs.zip(inputs1).flatMap {
      case (in1, in2) =>
        if (in1.name == in2.name) {
          val merged = merge(in1, in2)
            .getOrElse(throw new IllegalArgumentException(s"$in1 and $in2 aren't mergeable"))
          List(merged)
        } else {
          List(in1, in2)
        }
    }
  }

  def merge(first: ModelField, second: ModelField): Option[ModelField] = {
    if (first == second) {
      Some(first)
    } else if (first.name == second.name) {
      TensorShapeProtoOps.merge(first.shape, second.shape).flatMap { shape =>
        val fieldContents = first.typeOrSubfields -> second.typeOrSubfields match {
          case (Subfields(fDict), Subfields(sDict)) =>
            mergeSubfields(fDict, sDict).map(ModelField.TypeOrSubfields.Subfields.apply)
          case (Dtype(fInfo), Dtype(sInfo)) =>
            DataTypeOps.merge(fInfo, sInfo).map(ModelField.TypeOrSubfields.Dtype.apply)
          case _ => None
        }
        fieldContents.map(ModelField(first.name, shape, _))
      }
    } else {
      None
    }
  }

  def mergeSubfields(
    first: ModelField.Subfield,
    second: ModelField.Subfield
  ): Option[ModelField.Subfield] = {
    val fields = second.data.map { field =>
      val emitterField = first.data.find(_.name == field.name)
      emitterField.flatMap(merge(_, field))
    }
    if (fields.forall(_.isDefined)) {
      val exactFields = fields.flatten
      Some(ModelField.Subfield(exactFields))
    } else {
      None
    }
  }

  def flatten(fields: Seq[ModelField], rootName: String = ""): List[FieldDescription] = {
    fields.flatMap(flatten(rootName, _)).toList
  }

  def flatten(rootName: String, field: ModelField): Seq[FieldDescription] = {
    val name = s"$rootName/${field.name}"
    field.typeOrSubfields match {
      case Empty => List.empty
      case Subfields(value) =>
        value.data.flatMap { subfield =>
          flatten(name, subfield)
        }
      case Dtype(value) =>
        List(
          FieldDescription(
            name,
            value,
            TensorShape.fromProto(field.shape).dims
          )
        )
    }
  }

  def appendAll(outputs: Seq[ModelField], inputs: Seq[ModelField]): Option[Seq[ModelField]] = {
    val fields = inputs.map { input =>
      outputs.find(_.name == input.name).flatMap { output =>
        merge(output, input)
      }
    }

    if (fields.exists(_.isEmpty)) {
      None
    } else {
      Some(fields.flatten)
    }
  }

}

object ModelFieldOps extends ModelFieldOps