package io.hydrosphere.serving.contract.utils.ops

import io.hydrosphere.serving.contract.utils.description.{ContractDescription, SignatureDescription}
import io.hydrosphere.serving.contract.model_contract.ModelContract

trait ModelContractOps {
  implicit class ModelContractPumped(modelContract: ModelContract) {
    def flatten: ContractDescription = {
      ContractDescription(
        ModelContractOps.flatten(modelContract)
      )
    }
  }
}

object ModelContractOps {
  def flatten(modelContract: ModelContract): List[SignatureDescription] = {
    modelContract.signatures.map(ModelSignatureOps.flatten).toList
  }
}
