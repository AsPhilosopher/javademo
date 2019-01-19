package com.plain.prometheus.custom;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import org.springframework.stereotype.Component;

@Component
public class CustomMetric {
    static final Counter counter = Counter.build().name("my_request_total").help("Total request.")
            .labelNames("method").register();

    static final Gauge gauga = Gauge.build().name("my_gauga").help("Test gauge.").labelNames("method").register();

    static Histogram histogram = Histogram.build().name("my_histogram").help("Test histogram.").labelNames("method").register();

    static final Summary summary = Summary.build().name("my_summary")
            .quantile(0.3, 0.05)
            .quantile(0.5, 0.01)
            .quantile(0.8, 0.05)
            .help("Test summary.")
            .labelNames("method").register();

    public void processRequest(String method) {
        counter.labels(method.toUpperCase()).inc();
        gauga.labels(method.toUpperCase()).dec();
        histogram.labels(method.toUpperCase()).observe(counter.labels(method.toUpperCase()).get());
        summary.labels(method.toUpperCase()).observe(counter.labels(method.toUpperCase()).get());
    }
}
