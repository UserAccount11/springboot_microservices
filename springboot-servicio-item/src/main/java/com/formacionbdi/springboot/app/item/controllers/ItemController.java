package com.formacionbdi.springboot.app.item.controllers;

import com.formacionbdi.springboot.app.commons.models.entity.Producto;
import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.service.ItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RefreshScope
@RestController
public class ItemController {

	private final Logger logger = LoggerFactory.getLogger(ItemController.class);
	private final ItemService itemService;
	private final CircuitBreakerFactory circuitBreakerFactory;
	private final Environment env;

	@Value("${configuracion.texto}")
	private String text;

	public ItemController(@Qualifier("serviceFeign") ItemService itemService,
						  CircuitBreakerFactory circuitBreakerFactory, Environment env) {
		this.itemService = itemService;
		this.circuitBreakerFactory = circuitBreakerFactory;
		this.env = env;
	}

	@GetMapping("/listar")
	public List<Item> listar(@RequestParam(name = "nombre", required = false) String nombre,
							 @RequestHeader(name = "token-request", required = false) String token) {
		logger.info(nombre);
		logger.info(token);
		return itemService.findAll();
	}

	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		return circuitBreakerFactory.create("items")
				.run(() -> itemService.findById(id, cantidad), e -> metodoAlternativo(id, cantidad, e));
	}

	@CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo")
	@GetMapping("/ver2/{id}/cantidad/{cantidad}")
	public Item detalle2(@PathVariable Long id, @PathVariable Integer cantidad) {
		return itemService.findById(id, cantidad);
	}

	@CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo2") // Solo manejar método alternativo en @CircuitBraker
	@TimeLimiter(name = "items")
	@GetMapping("/ver3/{id}/cantidad/{cantidad}")
	public CompletableFuture<Item> detalle3(@PathVariable Long id, @PathVariable Integer cantidad) {
		return CompletableFuture.supplyAsync(() -> itemService.findById(id, cantidad));
	}
	
	public Item metodoAlternativo(Long id, Integer cantidad, Throwable e) {
		logger.error("ERROR: " + e.getMessage());

		Item item = new Item();
		Producto producto = new Producto();
		
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("Camara Sony");
		producto.setPrecio(500.00);
		item.setProducto(producto);
		return item;
	}

	public CompletableFuture<Item> metodoAlternativo2(Long id, Integer cantidad, Throwable e) {
		logger.error("ERROR: " + e.getMessage());

		Item item = new Item();
		Producto producto = new Producto();

		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("Camara Sony");
		producto.setPrecio(500.00);
		item.setProducto(producto);

		return CompletableFuture.supplyAsync(() -> item);
	}

	@GetMapping("/get-config")
	public ResponseEntity<?> getConfig(@Value("${server.port}") String port) {
		Map<String, String> json = new HashMap<>();
		json.put("text", text);
		json.put("port", port);

		logger.info("text: " + text);

		if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].contentEquals("dev")) {
			json.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
			json.put("autor.email", env.getProperty("configuracion.autor.email"));
		}

		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Producto create(@RequestBody Producto producto) {
		return itemService.save(producto);
	}

	@PutMapping("/{id}")
	public Producto update(@PathVariable Long id, @RequestBody Producto producto) {
		return itemService.update(id, producto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		itemService.delete(id);
	}

}
