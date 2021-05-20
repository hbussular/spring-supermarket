package market.model.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_CLIENT, ROLE_CASHIER;

  public String getAuthority() {
    return name();
  }

}
