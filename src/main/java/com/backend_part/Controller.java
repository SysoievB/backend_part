package com.backend_part;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class Controller {

    @PostMapping("/process-name")
    public Mono<ResponseDTO> processName(@RequestBody RequestDTO request) {
        log.info("Received name: {}", request.name());
        val processedName = "Hello, " + request.name() + "! Welcome to Spring WebFlux!";
        log.info("Processed name: {}", processedName);

        return Mono.just(new ResponseDTO(processedName));
    }

    // Request DTO - what we receive from frontend
    private record RequestDTO(String name) {
    }

    // Response DTO - what we send back to frontend
    private record ResponseDTO(String processedName) {
    }
}