package io.hydrosphere.serving.grpc

import java.util.concurrent.atomic.AtomicReference

import io.grpc._

class HeaderClientServerInterceptor(header: String) extends ServerInterceptor with ClientInterceptor {

  lazy val metadataKey: Metadata.Key[String] = Metadata.Key.of(header, Metadata.ASCII_STRING_MARSHALLER)

  lazy val contextKey: Context.Key[String] = Context.key(header)

  lazy val callOptionsKey: CallOptions.Key[String] = CallOptions.Key.of(header, null)
  lazy val callOptionsClientResponseWrapperKey: CallOptions.Key[AtomicReference[String]] = CallOptions.Key.of(header, null)


  override def interceptCall[ReqT, RespT](call: ServerCall[ReqT, RespT], requestHeaders: Metadata, next: ServerCallHandler[ReqT, RespT]): ServerCall.Listener[ReqT] = {
    val ctx = Context.current
      .withValue(contextKey, requestHeaders.get(metadataKey))

    Contexts.interceptCall(ctx, call, requestHeaders, next)
  }

  override def interceptCall[ReqT, RespT](method: MethodDescriptor[ReqT, RespT], callOptions: CallOptions, next: Channel): ClientCall[ReqT, RespT] = {
    val metadataValue = callOptions.getOption(callOptionsKey)
    val responseHeaderWrapper = callOptions.getOption(callOptionsClientResponseWrapperKey)

    new ForwardingClientCall.SimpleForwardingClientCall[ReqT, RespT](next.newCall(method, callOptions)) {
      override def start(responseListener: ClientCall.Listener[RespT], headers: Metadata): Unit = {
        if (metadataValue != null) headers.put(metadataKey, metadataValue)

        val listener = if (responseHeaderWrapper != null) {
          new ContextualizedClientCallListener[RespT](responseListener, responseHeaderWrapper)
        } else {
          responseListener
        }

        super.start(listener, headers)
      }
    }
  }

  private class ContextualizedClientCallListener[RespT](val delegate: ClientCall.Listener[RespT], headerWrapper: AtomicReference[String]) extends ClientCall.Listener[RespT] {

    override def onHeaders(headers: Metadata): Unit = {
      if (headers.containsKey(metadataKey)) {
        headerWrapper.set(headers.get(metadataKey))
      }
      delegate.onHeaders(headers)
    }

    override def onMessage(message: RespT): Unit = {
      delegate.onMessage(message)
    }

    override def onClose(status: Status, trailers: Metadata): Unit = {
      delegate.onClose(status, trailers)
    }

    override def onReady(): Unit = {
      delegate.onReady()
    }
  }

}

object HeaderClientServerInterceptor {
  def apply(header: String): HeaderClientServerInterceptor = new HeaderClientServerInterceptor(header)
}
