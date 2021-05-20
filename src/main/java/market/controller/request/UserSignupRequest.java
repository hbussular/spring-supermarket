package market.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
public class UserSignupRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "This field is required")
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "This field is required")
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "This field is required")
    private String fullName;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "This field is required")
    private String username;
}
