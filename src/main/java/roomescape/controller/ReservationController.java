package roomescape.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import roomescape.entity.Reservation;

@Controller
public class ReservationController {

  private List<Reservation> reservations = new ArrayList<>();

  private AtomicLong index = new AtomicLong(1);

  @GetMapping("/")
  public String home() {
    return "home";
  }

  @GetMapping("/reservation")
  public String reservation() {
    return "reservation";
  }

  @GetMapping("/reservations")
  @ResponseBody
  public List<Reservation> read() {
    return reservations;
  }

  @PostMapping("/reservations")
  @ResponseBody
  public ResponseEntity<Reservation> create(@RequestBody Reservation reservation) {
    Reservation newReservation = reservation.toEntity(reservation, index.getAndIncrement());
    reservations.add(newReservation);

    return ResponseEntity
        .created(URI.create("/reservations/" + newReservation.getId()))
        .body(newReservation);
  }

  @DeleteMapping("/reservations/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") Long id) {
    reservations.removeIf(reservation -> reservation.getId() == id);
  }
}
