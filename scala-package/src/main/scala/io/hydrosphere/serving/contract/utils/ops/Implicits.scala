package io.hydrosphere.serving.contract.utils.ops

trait Implicits
  extends ModelContractOps
  with ModelSignatureOps
  with ModelFieldOps
  with TensorShapeProtoOps {}

object Implicits extends Implicits
