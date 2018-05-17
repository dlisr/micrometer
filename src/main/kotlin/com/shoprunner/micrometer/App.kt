package com.shoprunner.micrometer

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import java.util.*

@SpringBootApplication
@EnableAutoConfiguration
open class App {
    @Bean
    open fun commandLineRunner(ctx: ApplicationContext): CommandLineRunner = CommandLineRunner { args ->

        println("the beans provided by Spring Boot:")

        val beanNames = ctx.beanDefinitionNames
        Arrays.sort(beanNames)
        for (beanName in beanNames) {
            val bean = ctx.getType(beanName)
            println("$beanName - ${bean?.name}")
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(App::class.java, *args)
}