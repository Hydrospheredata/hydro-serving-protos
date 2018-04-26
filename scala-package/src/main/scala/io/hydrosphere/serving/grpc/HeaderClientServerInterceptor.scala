package io.hydrosphere.serving.grpc

import io.grpc._

class HeaderClientServerInterceptor(header: String) extends ServerInterceptor with ClientInterceptor {

  lazy val metadataKey: Metadata.Key[String] = Metadata.Key.of(header, Metadata.ASCII_STRING_MARSHALLER)

  lazy val contextKey: Context.Key[String] = Context.key(header)

  lazy val callOptionsKey: CallOptions.Key[String] = CallOptions.Key.of(header, null)


  override def interceptCall[ReqT, RespT](call: ServerCall[ReqT, RespT], requestHeaders: Metadata, next: ServerCallHandler[ReqT, RespT]): ServerCall.Listener[ReqT] = {
    val ctx = Context.current
      .withValue(contextKey, requestHeaders.get(metadataKey))

    Contexts.interceptCall(ctx, call, requestHeaders, next)
  }

  override def interceptCall[ReqT, RespT](method: MethodDescriptor[ReqT, RespT], callOptions: CallOptions, next: Channel): ClientCall[ReqT, RespT] = {
    val destination = callOptions.getOption(callOptionsKey)
    new ForwardingClientCall.SimpleForwardingClientCall[ReqT, RespT](next.newCall(method, callOptions)) {
      override def start(responseListener: ClientCall.Listener[RespT], headers: Metadata): Unit = {
        if (destination != null) headers.put(metadataKey, destination)
        super.start(responseListener, headers)
      }
    }
  }
}

object HeaderClientServerInterceptor {
  def apply(header: String): HeaderClientServerInterceptor = new HeaderClientServerInterceptor(header)
}
