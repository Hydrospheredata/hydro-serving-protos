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

  object XRequestId extends Header {
    override val name: String = "x-request-id"
  }

  object XOtSpanContext extends Header {
    override val name: String = "x-ot-span-context"
  }

  object XB3TraceId extends Header {
    override val name: String = "x-b3-traceid"
  }

  object XB3SpanId extends Header {
    override val name: String = "x-b3-spanid"
  }

  object XB3ParentSpanId extends Header {
    override val name: String = "x-b3-parentspanid"
  }

  object XB3Sampled extends Header {
    override val name: String = "x-b3-sampled"
  }

  object XB3Flags extends Header {
    override val name: String = "x-b3-flags"
  }

  val all: Seq[Header] = Seq(
    XServingKafkaProduceTopic,
    XEnvoyUpstreamServiceTime,
    XServingModelVersionId,
    XRequestId,
    XOtSpanContext,
    XB3TraceId,
    XB3SpanId,
    XB3ParentSpanId,
    XB3Sampled,
    XB3Flags
  )

  def interceptors: Seq[HeaderClientServerInterceptor] = all.map(_.interceptor)
}
