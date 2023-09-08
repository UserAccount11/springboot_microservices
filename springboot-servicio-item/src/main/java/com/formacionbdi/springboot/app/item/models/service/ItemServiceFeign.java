package com.formacionbdi.springboot.app.item.models.service;

import java.util.List;
import java.util.stream.Collectors;

import com.formacionbdi.springboot.app.commons.models.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formacionbdi.springboot.app.item.clientes.ProductoFeignClient;
import com.formacionbdi.springboot.app.item.models.Item;

@Service("serviceFeign")
public class ItemServiceFeign implements ItemService {
	
	@Autowired
	private ProductoFeignClient feignClient;

	@Override
	public List<Item> findAll() {
		return feignClient.listar().stream()
				.map(p -> new Item(p, 1))
				.collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		return new Item(feignClient.detalle(id), cantidad);
	}

	@Override
	public Producto save(Producto producto) {
		return feignClient.create(producto);
	}

	@Override
	public Producto update(Long id, Producto producto) {
		return feignClient.update(id, producto);
	}

	@Override
	public void delete(Long id) {
		feignClient.delete(id);
	}

}
