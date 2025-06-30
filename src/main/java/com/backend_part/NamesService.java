package com.backend_part;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
class NamesService {
    private final static List<String> names = new ArrayList<>();

    Mono<List<String>> getNames() {
        return Mono.just(names);
    }

    Mono<String> getName(String name) {
        names.add(name);
        return Mono.just(name);
    }
}
