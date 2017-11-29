package io.github.azanx.shopping_list.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import io.github.azanx.shopping_list.domain.ListItem;

/**
 * @author Kamil Piwowarski
 *
 */
public interface ListItemRepository extends CrudRepository<ListItem, Long> {
	Collection<ListItem> findByParentListId(Long parentListId);
	Collection<ListItem> findByParentListAndParentListOwnerUserName(Long parentListId, String parentListOwnerUserName);
	Collection<ListItem> findByParentListOwnerUserNameAndParentListListNo(String parentListOwnerUserName, Short parentListListNo);
}
