package ag.shop.manager.repository;

import ag.shop.manager.entity.ShopUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ShopUserRepository extends CrudRepository<ShopUser, Integer> {

    Optional<ShopUser> findByUsername(String username);
}
