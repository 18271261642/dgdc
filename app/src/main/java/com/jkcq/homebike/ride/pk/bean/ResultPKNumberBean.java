package com.jkcq.homebike.ride.pk.bean;

public class ResultPKNumberBean {
    /**
     * "minimum": 2,
     * "maximum": 6
     */
    int minimum;
    int maximum;

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    @Override
    public String toString() {
        return "ResultPKNumberBean{" +
                "minimum=" + minimum +
                ", maximum=" + maximum +
                '}';
    }
}
