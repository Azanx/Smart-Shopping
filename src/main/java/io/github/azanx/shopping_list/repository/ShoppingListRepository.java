package io.github.azanx.shopping_list.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import io.github.azanx.shopping_list.domain.ShoppingList;

public interface ShoppingListRepository extends CrudRepository<ShoppingList, Long> {
	ShoppingList findById(Long Id);
	ShoppingList findByIdAndOwnerUserName(Long id, String userName);
	Collection<ShoppingList> findByOwnerUserName(String ownerUserName);
}
