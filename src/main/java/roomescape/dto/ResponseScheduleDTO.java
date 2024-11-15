package roomescape.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseScheduleDTO {

  private long id;

  private String time;

  public static ResponseScheduleDTO createResponseTimeDTO(final long id, final String time) {
    return new ResponseScheduleDTO(id, time);
  }
}
