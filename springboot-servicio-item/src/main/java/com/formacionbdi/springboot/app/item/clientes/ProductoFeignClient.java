package com.formacionbdi.springboot.app.item.clientes;

import java.util.List;

import com.formacionbdi.springboot.app.commons.models.entity.Producto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "servicio-productos")
public interface ProductoFeignClient {
	
	@GetMapping("/listar")
    List<Producto> listar();
	
	@GetMapping("/ver/{id}")
    Producto detalle(@PathVariable Long id);

    @PostMapping
    Producto create(@RequestBody Producto producto);

    @PutMapping("/{id}")
    Producto update(@PathVariable Long id, @RequestBody Producto producto);

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id);

}
