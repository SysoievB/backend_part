package com.backend_part;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * <h3>CrossOrigin</h3>
 * <ul>
 *     <li>Allow all origins (for development only) -> @CrossOrigin(origins = "*")</li>
 *     <li>Allow multiple specific origins -> @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})</li>
 *     <li>Allow all origins and methods (not recommended for production) -> @CrossOrigin</li>
 * </ul>
 * */

@Slf4j
@RestController
@RequestMapping("/api/names")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
class NamesController {
    private final NamesService service;

    @PostMapping
    Mono<ResponseDTO> processName(@RequestBody RequestDTO request) {
        log.info("----------------------Received name: {}----------------------", request.name());
        return service.addName(request.name())
                .log()
                .map(ResponseDTO::new)
                .doOnSuccess(responseDTO -> log.info("Response received on POST: {}", responseDTO));
    }

    @GetMapping("/all")
    Mono<List<String>> getAllNames() {
        log.info("----------------------Received all names----------------------");
        return service.getNames()
                .log()
                .doOnSuccess(names -> log.info("Returning {} names on GET ALL", names.size()));

    }

    @PutMapping("/{index}")
    Mono<ResponseDTO> updateName(@RequestParam String name, @PathVariable int index) {
        log.info("----------------------Received name for Update: {}-----------------------", name);
        return service.updateName(name, index)
                .log()
                .map(ResponseDTO::new)
                .doOnSuccess(responseDTO -> log.info("Response received on PUT: {}", responseDTO));
    }

    @DeleteMapping("/{index}")
    Mono<Void> deleteName(@PathVariable int index) {
        log.info("----------------------Name going to be deleted-----------------------");
        return service.deleteName(index);
    }

    @GetMapping("/status")
    public String updateStatus(@RequestParam boolean active) {
        log.info("----------------------Switch was set to {}-----------------------", String.valueOf(active).toUpperCase());
        return Optional.of(active)
                .filter(Boolean::booleanValue)
                .map(ignored -> "Status is now ACTIVE ✅")
                .orElse("Status is now INACTIVE ❌");
    }

    // both needed in order to avoid misconception on UI where what field is
    private record RequestDTO(String name) {
    }
    private record ResponseDTO(String processedName) {
    }
}