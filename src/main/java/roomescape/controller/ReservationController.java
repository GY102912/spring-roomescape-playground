package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
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
import roomescape.entity.Reservation;
import roomescape.entity.Time;

@Controller
public class ReservationController {

  @Autowired
  JdbcTemplate jdbcTemplate;

  private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) ->
      Reservation.create(
          resultSet.getLong("id"),
          resultSet.getString("name"),
          resultSet.getString("date"),
          Time.create(
              resultSet.getLong("time_id"),
              resultSet.getString("time_value"))
  );

  @GetMapping("/")
  public String home() {
    return "home";
  }

  @GetMapping("/reservation")
  public String reservation() {
    return "new-reservation";
  }

  @GetMapping("/reservations")
  @ResponseBody
  public List<Reservation> findAll() {
    String selectSql = """
        SELECT
            r.id as reservation_id,
            r.name,
            r.date,
            t.id as time_id,
            t.time as time_value
        FROM reservation as r inner join time as t on r.time_id = t.id
        """;
    return jdbcTemplate.query(selectSql, rowMapper);
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
      ps.setString(3, String.valueOf(reservation.getTime().getId()));
      return ps;
    }, keyHolder);

    Long generatedId = keyHolder.getKey().longValue();
    Reservation response = Reservation.create(
        generatedId,
        reservation.getName(),
        reservation.getDate(),
        reservation.getTime()
        );

    URI location = URI.create("/reservations/" + generatedId);

    return ResponseEntity.created(location).body(response);
  }

  @DeleteMapping("/reservations/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    int rowsAffected = jdbcTemplate.update("delete from reservation where id = ?", id);

    if (rowsAffected == 0) {
      throw new NoSuchElementException();
    }

    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler({NoSuchElementException.class, MethodArgumentNotValidException.class})
  public ResponseEntity<Void> handleException(Exception e) {
    return ResponseEntity.badRequest().build();
  }
}
