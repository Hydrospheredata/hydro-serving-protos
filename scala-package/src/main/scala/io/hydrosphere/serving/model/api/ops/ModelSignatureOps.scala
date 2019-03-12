package io.hydrosphere.serving.model.api.ops

import io.hydrosphere.serving.contract.model_signature.ModelSignature
import io.hydrosphere.serving.model.api.MergeError

trait ModelSignatureOps {
  def merge(signature1: ModelSignature, signature2: ModelSignature) = {
    for {
      mergedIns <- ModelFieldOps.mergeAll(signature1.inputs, signature2.inputs).right
      mergedOuts <- ModelFieldOps.mergeAll(signature1.outputs, signature2.outputs).right
    } yield {
      ModelSignature(
        s"${signature1.signatureName}&${signature2.signatureName}",
        mergedIns,
        mergedOuts
      )
    }
  }

  def append(head: ModelSignature, tail: ModelSignature): Either[Seq[MergeError], ModelSignature] = {
    if (tail.inputs.isEmpty) {
      Left(Seq(MergeError.fieldNotFound("No fields provided")))
    } else {
      ModelFieldOps.appendAll(head.outputs, tail.inputs)
        .right
        .map { _ =>
          ModelSignature(
            s"${head.signatureName}>${tail.signatureName}",
            head.inputs,
            tail.outputs
          )
        }
    }
  }
}

object ModelSignatureOps extends ModelSignatureOps