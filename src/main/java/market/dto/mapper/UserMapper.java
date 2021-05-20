package market.dto.mapper;


import market.dto.model.user.UserReadDto;
import market.model.user.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {
    public static UserReadDto toUserDto(User user) {
        return new UserReadDto()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setFullName(user.getFullName())
                .setUsername(user.getUsername());
    }

}
