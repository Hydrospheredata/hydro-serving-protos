package io.hydrosphere.serving.model.api

import io.hydrosphere.serving.contract.model_field.ModelField

sealed trait MergeError extends Throwable with Serializable with Product

object MergeError {
  case class FieldNotFound(fieldName: String) extends MergeError

  def fieldNotFound(fieldName: String): MergeError = FieldNotFound(fieldName)

  case class NamesAreDifferent(m1: ModelField, m2: ModelField) extends MergeError

  def namesAreDifferent(m1: ModelField, m2: ModelField): MergeError = NamesAreDifferent(m1, m2)

  case class IncompatibleTypes(m1: ModelField, m2: ModelField) extends MergeError

  def incompatibleTypes(m1: ModelField, m2: ModelField): MergeError = IncompatibleTypes(m1, m2)

  case class IncompatibleShapes(m1: ModelField, m2: ModelField) extends MergeError

  def incompatibleShapes(m1: ModelField, m2: ModelField): MergeError = IncompatibleShapes(m1, m2)
}