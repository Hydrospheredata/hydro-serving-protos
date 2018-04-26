package io.hydrosphere.serving.grpc

import io.grpc.{CallOptions, Context, Metadata}

sealed trait Header {
  val name: String
  
  lazy val interceptor: HeaderClientServerInterceptor = HeaderClientServerInterceptor(name)
  
  def metadataKey: Metadata.Key[String] = interceptor.metadataKey
  def contextKey: Context.Key[String] = interceptor.contextKey
  def callOptionsKey: CallOptions.Key[String] = interceptor.callOptionsKey
}

object Headers {
  
  object KafkaTopic extends Header {
    override val name: String = "kafka_produce_topic"
  }
  
  object ApplicationId extends Header {
    override val name: String = "application_id"
  }
  
  object TraceId extends Header {
    override val name: String = "trace_id"
  }
  
  object StageId extends Header {
    override val name: String = "stage_id"
  }
  
  object StageName extends Header {
    override val name: String = "stage_name"
  }
  
  val all: Seq[Header] = Seq(
    KafkaTopic,
    ApplicationId,
    TraceId,
    StageId,
    StageName
  )

  def interceptors: Seq[HeaderClientServerInterceptor] = all.map(_.interceptor)
}
