package com.formacionbdi.springboot.app.item.models.service;

import java.util.List;

import com.formacionbdi.springboot.app.commons.models.entity.Producto;
import com.formacionbdi.springboot.app.item.models.Item;

public interface ItemService {

	List<Item> findAll();
	Item findById(Long id, Integer cantidad);
	Producto save(Producto producto);
	Producto update(Long id, Producto producto);
	void delete(Long id);

}
