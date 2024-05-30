package com.tre3p.randomcatimgdownloader.config

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Bean
    fun httpClient(): HttpClient {
        return HttpClient(CIO) {
            engine {
                requestTimeout = 4000
            }
        }
    }
}