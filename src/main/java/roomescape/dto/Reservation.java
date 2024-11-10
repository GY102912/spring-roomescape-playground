package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
  private long id;

  @NotBlank
  private String name;

  @NotBlank
  private String date;

  @NotBlank
  private String time;

  public Reservation(String name, String date, String time) {
    this.name = name;
    this.date = date;
    this.time = time;
  }
}
