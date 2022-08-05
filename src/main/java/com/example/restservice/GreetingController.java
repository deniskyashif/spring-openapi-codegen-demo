package com.example.restservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {
	private static final String template = "Hello, %s!";
	private final AtomicInteger counter = new AtomicInteger();
	private final List<Greeting> greetings = new ArrayList<>();

	@GetMapping("api/greeting")
	@Operation(summary = "Gets all the greetings",
		responses = {
			@ApiResponse(
			responseCode = "200",
			description = "OK",
			content = {
				@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					array = @ArraySchema(schema = @Schema(implementation = Greeting.class)))
			})
		})
	public ResponseEntity<List<Greeting>> get() {
		return ResponseEntity.ok(greetings);
	}

	@GetMapping("api/greeting/{id}")
	@Operation(summary = "Gets a greeting by id",
		responses = {
			@ApiResponse(responseCode = "200",
				description = "OK",
				content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Greeting.class))
				}),
				@ApiResponse(responseCode = "400", description = "Invalid Id parameter"),
				@ApiResponse(responseCode = "404", description = "The greeting is not found.")
			})
	public ResponseEntity<Object> getById(@PathVariable Integer id) {
		if (id <= 0) {
			return ResponseEntity.badRequest().body("The id must be a positive integer.");
		}

		Optional<Greeting> greeting = greetings.stream()
				.filter(g -> Objects.equals(g.getId(), id))
				.findFirst();

		return greeting
				.<ResponseEntity<Object>>map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("api/greeting")
	@Operation(summary = "Creates a greeting",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Create greeting request body",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GreetingInput.class)), required = true),
		responses = {
			@ApiResponse(responseCode = "201",
			description = "OK",
			content = { @Content(/* mediaType = MediaType.TEXT_PLAIN_VALUE ,*/ schema = @Schema(implementation = String.class))})
	})
	public ResponseEntity<Object> create(@RequestBody GreetingInput input) {
		Greeting greeting = new Greeting(counter.incrementAndGet(), String.format(template, input.getContent()));
		greetings.add(greeting);

		return new ResponseEntity<>(greeting.getId(), HttpStatus.CREATED);
	}
}
