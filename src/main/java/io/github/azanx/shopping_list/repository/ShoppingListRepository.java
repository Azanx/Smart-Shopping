package io.github.azanx.shopping_list.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.github.azanx.shopping_list.domain.ShoppingList;

/**
 * @author Kamil Piwowarski
 *
 */
public interface ShoppingListRepository extends CrudRepository<ShoppingList, Long> {
	Optional<ShoppingList> findById(Long Id);
	Optional<ShoppingList> findByIdAndOwnerName(Long id, String OwnerName);
	List<ShoppingList> findByOwnerName(String ownerName);
	Short countByOwnerName(String ownerName);
}
