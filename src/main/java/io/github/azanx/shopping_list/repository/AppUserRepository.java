package io.github.azanx.shopping_list.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.github.azanx.shopping_list.domain.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
	Optional<AppUser> findByUserName(String userName);
}
