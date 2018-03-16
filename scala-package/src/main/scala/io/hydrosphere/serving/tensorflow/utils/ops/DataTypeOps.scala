package io.hydrosphere.serving.tensorflow.utils.ops

import io.hydrosphere.serving.tensorflow.types.DataType

trait DataTypeOps {
  def merge(d1: DataType, d2: DataType): Option[DataType] = {
    if (d1 == d2) {
      Some(d1)
    } else {
      None
    }
  }
}

object DataTypeOps extends DataTypeOps
