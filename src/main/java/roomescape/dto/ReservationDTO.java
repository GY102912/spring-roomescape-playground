package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationDTO {
  private long id;

  @NotBlank
  private String name;

  @NotBlank
  private String date;

  @NotBlank
  private String time;

  public ReservationDTO(String name, String date, String time) {
    this.name = name;
    this.date = date;
    this.time = time;
  }
}
