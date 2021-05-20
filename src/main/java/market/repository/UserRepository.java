package market.repository;

import javax.transaction.Transactional;

import market.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByUsername(String username);

  User findByEmail(String email);

  @Transactional
  void deleteByUsername(String username);
}
