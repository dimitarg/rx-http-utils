package com.dgeorgiev.rx.util.data;

import fj.F;
import fj.data.Either;
import rx.Observable;

public class MonadConversions {

    public static <A,B,E> Observable<Either<E,B>> mapSuccess(Observable<Either<E, A>> source, F<A,B> transformer) {
        return source.map(either -> {

            return mapSuccess(either, transformer);
        });
    }

    public static <A, B, E> Either<E, B> mapSuccess(Either<E, A> either, F<A, B> transformer) {
        if (either.isLeft()) {
            E error = either.left().value();
            return Either.left(error);
        } else {
            A input = either.right().value();
            B result = transformer.f(input);

            return Either.right(result);
        }
    }



}
