package roomescape.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.entity.Reservation;

@RestController
public class ReservationController {

  private List<Reservation> reservations = new ArrayList<>();

  @GetMapping("/reservation")
  public String reservation() {
    return "reservation";
  }

  @GetMapping("/reservations")
  public List<Reservation> reservations() {
    reservations = List.of(
        new Reservation(
            1,
            "브라운",
            "2023-01-01",
            "10:00"
        ),
        new Reservation(
            2,
            "브라운",
            "2023-01-02",
            "11:00"
        )
    );
    return reservations;
  }



}
