package io.hydrosphere.serving.grpc

import io.grpc._

class KafkaTopicServerInterceptor extends ServerInterceptor with ClientInterceptor {

  override def interceptCall[ReqT, RespT](call: ServerCall[ReqT, RespT], requestHeaders: Metadata, next: ServerCallHandler[ReqT, RespT]): ServerCall.Listener[ReqT] = {
    val ctx = Context.current
      .withValue(KafkaTopicServerInterceptor.CTX_KAFKA_TOPIC, requestHeaders.get(KafkaTopicServerInterceptor.KAFKA_TOPIC_HEADER_METADATA))

    Contexts.interceptCall(ctx, call, requestHeaders, next)
  }

  override def interceptCall[ReqT, RespT](method: MethodDescriptor[ReqT, RespT], callOptions: CallOptions, next: Channel): ClientCall[ReqT, RespT] = {
    val destination = callOptions.getOption(KafkaTopicServerInterceptor.KAFKA_TOPIC_KEY)
    new ForwardingClientCall.SimpleForwardingClientCall[ReqT, RespT](next.newCall(method, callOptions)) {
      override def start(responseListener: ClientCall.Listener[RespT], headers: Metadata): Unit = {
        if (destination != null) headers.put(KafkaTopicServerInterceptor.KAFKA_TOPIC_HEADER_METADATA, destination)
        super.start(responseListener, headers)
      }
    }
  }
}

object KafkaTopicServerInterceptor {
  private val KAFKA_TOPIC_HEADER = "kafka_produce_topic"

  private val KAFKA_TOPIC_HEADER_METADATA = Metadata.Key.of(KAFKA_TOPIC_HEADER, Metadata.ASCII_STRING_MARSHALLER)

  val CTX_KAFKA_TOPIC: Context.Key[String] = Context.key(KAFKA_TOPIC_HEADER)

  val KAFKA_TOPIC_KEY: CallOptions.Key[String] = CallOptions.Key.of(KAFKA_TOPIC_HEADER, null)

}