package com.formacionbdi.springboot.app.productos.controllers;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.commons.models.entity.Producto;
import com.formacionbdi.springboot.app.productos.models.service.ProductoService;

@RestController
@RequiredArgsConstructor
public class ProductoController {


	private final ProductoService productoService;
	private final Environment env;

	@Value("${server.port}")
	private Integer port;

	@GetMapping("/listar")
	public List<Producto> listar(){
		return productoService.findAll().stream()
				.map(producto ->{
					producto.setPort(Integer.parseInt(Objects.requireNonNull(env.getProperty("local.server.port"))));
					// producto.setPort(port);
					return producto;
				}).collect(Collectors.toList());
	}
	
	@GetMapping("/ver/{id}")
	public Producto detalle(@PathVariable Long id) throws InterruptedException {
		if (id.equals(10L)) {
			throw new IllegalStateException("Product not found!");
		}

		if (id.equals(7L)) {
			TimeUnit.SECONDS.sleep(5L);
		}

		Producto producto = productoService.findById(id);
		producto.setPort(Integer.parseInt(Objects.requireNonNull(env.getProperty("local.server.port"))));
		//producto.setPort(port);
		
		return producto;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Producto save(@RequestBody Producto producto) {
		return productoService.save(producto);
	}

	@PutMapping("/{id}")
	public Producto update(@PathVariable Long id, @RequestBody Producto producto) {
		Producto productoDb = productoService.findById(id);

		if (producto == null) {
			throw new RuntimeException(String.format("Producto con id %d no encontrado.", id));
		} else {
			productoDb.setNombre(producto.getNombre());
			productoDb.setPrecio(producto.getPrecio());

			return productoService.save(productoDb);
		}
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		productoService.deleteById(id);
	}

}
