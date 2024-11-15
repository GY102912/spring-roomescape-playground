package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestScheduleDTO {

  @NotBlank
  private String time;

  public static RequestScheduleDTO createRequestTimeDTO(final String time) {
    return new RequestScheduleDTO(time);
  }
}
