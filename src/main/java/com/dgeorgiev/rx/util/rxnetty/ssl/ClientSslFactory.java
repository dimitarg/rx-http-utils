package com.dgeorgiev.rx.util.rxnetty.ssl;

import io.netty.buffer.ByteBufAllocator;
import io.reactivex.netty.pipeline.ssl.DefaultFactories;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * An SSL factory which runs in client mode.
 */
public class ClientSslFactory extends DefaultFactories.SSLContextBasedFactory {

    public ClientSslFactory(SSLContext sslContext) {
        super(sslContext);
    }

    @Override
    public SSLEngine createSSLEngine(ByteBufAllocator allocator) {
        SSLEngine result = super.createSSLEngine(allocator);
        result.setUseClientMode(true);
        return result;
    }
}