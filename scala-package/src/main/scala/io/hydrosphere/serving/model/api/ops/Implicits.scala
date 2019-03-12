package io.hydrosphere.serving.model.api.ops

trait Implicits
  extends ModelSignatureOps
  with ModelFieldOps {}

object Implicits extends Implicits
