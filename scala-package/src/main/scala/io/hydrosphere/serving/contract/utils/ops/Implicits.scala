package io.hydrosphere.serving.contract.utils.ops

import io.hydrosphere.serving.tensorflow.utils.ops.TensorShapeProtoOps

trait Implicits
  extends ModelContractOps
  with ModelSignatureOps
  with ModelFieldOps
  with TensorShapeProtoOps {}

object Implicits extends Implicits
