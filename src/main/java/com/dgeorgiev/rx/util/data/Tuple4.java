package com.dgeorgiev.rx.util.data;

import fj.P4;

public class Tuple4<A,B,C,D> extends P4<A,B,C,D> {

    public final A _1;
    public final B _2;
    public final C _3;
    public final D _4;

    public Tuple4(A _1, B _2, C _3, D _4) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this. _4 = _4;
    }

    @Override
    public A _1() {
        return _1;
    }

    @Override
    public B _2() {
        return _2;
    }

    @Override
    public C _3() {
        return _3;
    }

    @Override
    public D _4() {
        return _4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple4 tuple4 = (Tuple4) o;

        if (_1 != null ? !_1.equals(tuple4._1) : tuple4._1 != null) return false;
        if (_2 != null ? !_2.equals(tuple4._2) : tuple4._2 != null) return false;
        if (_3 != null ? !_3.equals(tuple4._3) : tuple4._3 != null) return false;
        if (_4 != null ? !_4.equals(tuple4._4) : tuple4._4 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _1 != null ? _1.hashCode() : 0;
        result = 31 * result + (_2 != null ? _2.hashCode() : 0);
        result = 31 * result + (_3 != null ? _3.hashCode() : 0);
        result = 31 * result + (_4 != null ? _4.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tuple4{" +
                "_1=" + _1 +
                ", _2=" + _2 +
                ", _3=" + _3 +
                ", _4=" + _4 +
                "} " + super.toString();
    }
}