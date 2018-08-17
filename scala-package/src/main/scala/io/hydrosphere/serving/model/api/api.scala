package io.hydrosphere.serving.model

import io.hydrosphere.serving.model.api.Result.HError

import scala.concurrent.Future

package object api {
  type HResult[T] = Either[HError, T]
  type HFResult[T] = Future[Either[HError, T]]

}
