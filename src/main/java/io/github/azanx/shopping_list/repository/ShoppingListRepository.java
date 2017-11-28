package io.github.azanx.shopping_list.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.github.azanx.shopping_list.domain.ShoppingList;

/**
 * @author Kamil Piwowarski
 *
 */
public interface ShoppingListRepository extends CrudRepository<ShoppingList, Long> {
	Optional<ShoppingList> findById(Long Id);
	Optional<ShoppingList> findByIdAndOwnerUserName(Long id, String userName);
	Collection<ShoppingList> findByOwnerUserName(String ownerUserName);
}
