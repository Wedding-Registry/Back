package com.wedding.serviceapi.util.webclient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WebClientUtil {

    private final WebClient webClient;

    public GoodsRegisterResponseDto getGoodsInfo(String url) {

        Mono<GoodsRegisterResponseDto> response = webClient.post()
                .uri("/search")
                .body(Mono.just(new GoodsRegisterRequestDto(url)), GoodsRegisterRequestDto.class)
                .retrieve()
                .bodyToMono(GoodsRegisterResponseDto.class);

        return response.block();
    }

}
