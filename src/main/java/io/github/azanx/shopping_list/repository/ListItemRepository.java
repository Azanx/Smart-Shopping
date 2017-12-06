package io.github.azanx.shopping_list.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.github.azanx.shopping_list.domain.ListItem;

/**
 * @author Kamil Piwowarski
 *
 */
public interface ListItemRepository extends CrudRepository<ListItem, Long> {
	List<ListItem> findByParentListIdOrderByItemNo(Long parentListId);
	List<ListItem> findByParentList_OwnerNameAndParentList_ListNo(String parentListOwner, Short parentListNo);
	Short countByParentListId(Long parentListId);
	Optional<ListItem> findById(Long itemId);
	Optional<ListItem> findByIdAndParentListId(Long itemId, Long parentListId);
}
//getItemsForUsersListNo(String userName, Short listNo) {