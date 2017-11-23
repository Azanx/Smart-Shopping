package io.github.azanx.shopping_list.repository;

import org.springframework.data.repository.CrudRepository;

import io.github.azanx.shopping_list.domain.ListItem;

public interface ListItemRepository extends CrudRepository<ListItem, Long> {

}
