package com.backend_part;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
class NamesService {
    private final static List<String> names = new ArrayList<>(
            Arrays.asList("Vasia", "Petia", "Valia")
    );

    Mono<List<String>> getNames() {
        return Mono.just(names);
    }

    Mono<String> addName(String name) {
        names.add(name);
        return Mono.just(name);
    }

    Mono<String> updateName(String newName, int index) {
        return getNames()
                .map(all -> Optional.ofNullable(all.set(index, newName))
                        .orElseThrow(() -> new RuntimeException("Name not found"))
                );
    }

    Mono<Void> deleteName(int index) {
        return getNames()
                .handle((list, sink) -> {
                    try {
                        list.remove(index);
                        sink.complete(); //the same as Mono.empty()
                    } catch (Exception e) {
                        sink.error(new RuntimeException(e.getMessage(), e));
                    }
                });
    }
}
