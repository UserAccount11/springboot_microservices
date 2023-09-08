package com.formacionbdi.springboot.app.item.models.service;

import com.formacionbdi.springboot.app.commons.models.entity.Producto;
import com.formacionbdi.springboot.app.item.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("serviceRestTemplate")
public class ItemServiceImpl implements ItemService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public List<Item> findAll() {
		List<Producto> productos = Arrays.asList(Objects
				.requireNonNull(restTemplate.getForObject("http://servicio-productos/listar", Producto[].class)));
		
		return productos.stream()
				.map(p -> new Item(p, 1))
				.collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put("id", id.toString());

		Producto producto = restTemplate.getForObject("http://servicio-productos/ver/{id}",
				Producto.class, pathVariables);

		return new Item(producto, cantidad);
	}

	@Override
	public Producto save(Producto producto) {
		HttpEntity<Producto> body = new HttpEntity<>(producto);
		ResponseEntity<Producto> response = restTemplate.exchange("http://servicio-productos",
				HttpMethod.POST, body, Producto.class);

		return response.getBody();
	}

	@Override
	public Producto update(Long id, Producto producto) {
		Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put("id", id.toString());

		HttpEntity<Producto> body = new HttpEntity<>(producto);
		ResponseEntity<Producto> response = restTemplate.exchange("http://servicio-productos/{id}",
				HttpMethod.PUT, body, Producto.class, pathVariables);

		return response.getBody();
	}

	@Override
	public void delete(Long id) {
		Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put("id", id.toString());

		restTemplate.delete("http://servicio-productos/{id}", pathVariables);
	}

}
