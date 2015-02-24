package com.dgeorgiev.rx.http.client;

import com.dgeorgiev.rx.util.json.Json;
import com.dgeorgiev.rx.util.rxnetty.ssl.ClientSslFactory;
import fj.data.Either;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringEncoder;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.pipeline.PipelineConfiguratorComposite;
import io.reactivex.netty.protocol.http.HttpObjectAggregationConfigurator;
import io.reactivex.netty.protocol.http.client.*;
import rx.Observable;

import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.util.Map;

import static com.dgeorgiev.rx.util.data.MonadConversions.mapSuccess;


/**
 * Dimitar Georgiev
 * 21.12.14.
 */
public class RestClient {

    /**
     * If there is an API hole, missing functionality you can revert to using the RxNetty internals, by extending the class:
     */
    protected final HttpClient<ByteBuf, ByteBuf> client;

    public RestClient(String host, int port, SSLContext ctx, int maxChunksizeBytes) {
        client = createHttpClient(host, port, ctx, maxChunksizeBytes);
    }

    public RestClient(String host, int port, int maxChunksizeBytes) {
        this(host,port,null,maxChunksizeBytes);
    }

    public RestClient(String host, int port) {
        this(host,port,null,HttpObjectAggregationConfigurator.DEFAULT_CHUNK_SIZE);
    }

    public RestClient(String host, int port, SSLContext ctx) {
        client = createHttpClient(host, port, ctx, HttpObjectAggregationConfigurator.DEFAULT_CHUNK_SIZE);
    }

    public <A> Observable<Either<HttpError,A>> execute(String path, Map<String, String> params, Method method, Class<A> resultClass) {

        Observable<Either<HttpError, String>> resultRaw = executeRaw(path, params, method);

        return mapSuccess(resultRaw, respString -> Json.read(respString, resultClass));

    }

    public Observable<Either<HttpError, String>> executeRaw(String path, Map<String, String> params, Method method) {
        HttpClientRequest<ByteBuf> req = createHttpRequest(path, method, params);

        Observable<HttpClientResponse<ByteBuf>> resp = client.submit(req);

        return resp.flatMap(r -> {

            HttpResponseStatus status = r.getStatus();
            if (status.code() == 200) {
                return r.getContent().map(c -> Either.right(c.toString(Charset.forName("UTF-8"))));
            } else {
                return r.getContent().map( byteByf -> Either.left(new HttpError(status.code(),status.reasonPhrase(), byteByf.toString(Charset.forName("UTF-8")))));
            }
        });
    }

    public void shutdown() {
        client.shutdown();
    }



    private HttpClient<ByteBuf,ByteBuf> createHttpClient(String host, int port, SSLContext sslCtx, int maxChunksizeBytes) {
        HttpClientBuilder<ByteBuf, ByteBuf> builder = RxNetty.<ByteBuf, ByteBuf>newHttpClientBuilder(host, port);
        if(sslCtx!=null) {
            builder = builder.withSslEngineFactory(new ClientSslFactory(sslCtx));
        }
        HttpClient<ByteBuf, ByteBuf> httpClient = builder.pipelineConfigurator(varChunksizeClientPipeline(maxChunksizeBytes)).build();
        return httpClient;
    }

    private HttpClientRequest<ByteBuf> createHttpRequest(String path, Method method, Map<String, String> params) {

        QueryStringEncoder query = new QueryStringEncoder(path);

        for(Map.Entry<String,String> e: params.entrySet()) {
            query.addParam(e.getKey(), e.getValue());
        }
        return HttpClientRequest.create(HttpMethod.GET, query.toString());
    }

    private PipelineConfiguratorComposite<HttpClientResponse<ByteBuf>, HttpClientRequest<ByteBuf>> varChunksizeClientPipeline(int maxChunkSizeBytes) {
        return new PipelineConfiguratorComposite<>(new HttpClientPipelineConfigurator<ByteBuf, ByteBuf>(), new HttpObjectAggregationConfigurator(maxChunkSizeBytes));
    }


    public static enum Method{GET,POST,PUT,DELETE}
}
