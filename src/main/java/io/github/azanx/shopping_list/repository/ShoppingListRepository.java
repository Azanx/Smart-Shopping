package io.github.azanx.shopping_list.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import io.github.azanx.shopping_list.domain.ShoppingList;

public interface ShoppingListRepository extends CrudRepository<ShoppingList, Long> {
	Collection<ShoppingList> findById(Long Id);
	Collection<ShoppingList> findByOwnerUserName(String ownerUserName);
}
