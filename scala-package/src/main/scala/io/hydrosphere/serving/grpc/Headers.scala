package io.hydrosphere.serving.grpc

import java.util.concurrent.atomic.AtomicReference

import io.grpc.{CallOptions, Context, Metadata}

sealed trait Header {
  val name: String
  
  lazy val interceptor: HeaderClientServerInterceptor = HeaderClientServerInterceptor(name)
  
  def metadataKey: Metadata.Key[String] = interceptor.metadataKey
  def contextKey: Context.Key[String] = interceptor.contextKey
  def callOptionsKey: CallOptions.Key[String] = interceptor.callOptionsKey
  def callOptionsClientResponseWrapperKey: CallOptions.Key[AtomicReference[String]] = interceptor.callOptionsClientResponseWrapperKey
}

object Headers {

  object XEnvoyUpstreamServiceTime extends Header {
    override val name: String = "x-envoy-upstream-service-time"
  }

  object XServingModelVersionId extends Header {
    override val name: String = "x-serving-model-version-id"
  }

  object XServingKafkaProduceTopic extends Header {
    override val name: String = "x-serving-kafka-produce-topic"
  }

  val all: Seq[Header] = Seq(
    XServingKafkaProduceTopic,
    XEnvoyUpstreamServiceTime,
    XServingModelVersionId
  )

  def interceptors: Seq[HeaderClientServerInterceptor] = all.map(_.interceptor)
}
