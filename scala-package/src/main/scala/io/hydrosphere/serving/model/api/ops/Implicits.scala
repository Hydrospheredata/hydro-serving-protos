package io.hydrosphere.serving.model.api.ops

trait Implicits
  extends ModelContractOps
  with ModelSignatureOps
  with ModelFieldOps {}

object Implicits extends Implicits
