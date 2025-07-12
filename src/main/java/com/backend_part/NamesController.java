package com.backend_part;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

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
        log.info("----------------------Received name: {}-----------------------", name);
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

    // Request DTO - what we receive from frontend
    private record RequestDTO(String name) {
    }

    // Response DTO - what we send back to frontend
    private record ResponseDTO(String processedName) {
    }
}