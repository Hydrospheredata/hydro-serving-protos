package io.hydrosphere.serving.proto.contract.ops

trait Implicits
  extends ModelSignatureOps
  with ModelFieldOps {}

object Implicits extends Implicits
