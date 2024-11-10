package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import roomescape.dto.Reservation;
import roomescape.exception.NotFoundReservationException;

@Controller
public class ReservationController {

  @Autowired
  JdbcTemplate jdbcTemplate;

  private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) ->
      new Reservation(
          resultSet.getLong("id"),
          resultSet.getString("name"),
          resultSet.getString("date"),
          resultSet.getString("time")
  );

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
  public List<Reservation> findAll() {
    return jdbcTemplate.query("select id, name, date, time from reservation", rowMapper);
  }

  @PostMapping("/reservations")
  @ResponseBody
  public ResponseEntity<Reservation> create(@RequestBody @Valid Reservation reservation) {

    KeyHolder keyHolder = new GeneratedKeyHolder();

    String insertSql = "INSERT INTO reservation (name, date, time) VALUES (?, ?, ?)";
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(insertSql, new String[]{"id"});
      ps.setString(1, reservation.getName());
      ps.setString(2, reservation.getDate());
      ps.setString(3, reservation.getTime());
      return ps;
    }, keyHolder);

    Long generatedId = keyHolder.getKey().longValue();
    reservation.setId(generatedId);

    URI location = URI.create("/reservations/" + generatedId);

    return ResponseEntity.created(location).body(reservation);
  }

  @DeleteMapping("/reservations/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    int rowsAffected = jdbcTemplate.update("delete from reservation where id = ?", id);

    if (rowsAffected == 0) {
      throw new NotFoundReservationException(id);
    }

    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler({NotFoundReservationException.class, MethodArgumentNotValidException.class})
  public ResponseEntity handleException(Exception e) {
    return ResponseEntity.badRequest().build();
  }
}
