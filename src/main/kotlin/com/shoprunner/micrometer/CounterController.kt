package com.shoprunner.micrometer

import com.google.common.collect.Maps
import com.google.common.util.concurrent.AtomicDouble
import io.micrometer.core.annotation.Timed
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.LongTaskTimer
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class CounterController {
    @Autowired
    private lateinit var registry: MeterRegistry

    private val gmsGauges: MutableMap<String, AtomicDouble> = Maps.newHashMap()

    @PostMapping("/order")
    @ResponseStatus(value = HttpStatus.OK)
    @Timed("orderProcessing5")
    fun postOrder(@RequestBody payload: String): Unit {
        System.out.println("received '$payload'")
        val vs = payload.split('|')
        if (vs.size != 2) throw IllegalArgumentException("Invalid data: $payload")
        val retailer = vs[0]
        val orderTotal = vs[1].toLong()
        val count = registry.counter("orderCount5", "retailer", retailer)
        count.increment()
        val gmsGauge = gmsGauges.getOrPut(retailer, {
            val ad = AtomicDouble()
            Gauge.builder("orderSum5", ad, AtomicDouble::get)
                .description("accumulated GMS YTD")
                .tags("retailer", retailer)
                .register(registry)
            ad
        })

        gmsGauge.addAndGet(orderTotal.toDouble())
    }
}