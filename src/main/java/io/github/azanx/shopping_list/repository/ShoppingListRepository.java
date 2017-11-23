package io.github.azanx.shopping_list.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.github.azanx.shopping_list.domain.ShoppingList;

public interface ShoppingListRepository extends CrudRepository<ShoppingList, Long> {
	List<ShoppingList> findById(Long Id);
}
