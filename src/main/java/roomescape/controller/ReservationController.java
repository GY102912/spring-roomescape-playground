package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
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
import roomescape.dto.ReservationDTO;
import roomescape.exception.NotFoundReservationException;

@Controller
public class ReservationController {

  @Autowired
  JdbcTemplate jdbcTemplate;

  private final RowMapper<ReservationDTO> rowMapper = (resultSet, rowNum) ->
      new ReservationDTO(
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
  public List<ReservationDTO> findAll() {
    return jdbcTemplate.query("select id, name, date, time from reservation", rowMapper);
  }

  @PostMapping("/reservations")
  @ResponseBody
  public ResponseEntity<ReservationDTO> create(@RequestBody @Valid ReservationDTO reservationDTO) {

    KeyHolder keyHolder = new GeneratedKeyHolder();

    String insertSql = "INSERT INTO reservation (name, date, time) VALUES (?, ?, ?)";
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(insertSql, new String[]{"id"});
      ps.setString(1, reservationDTO.getName());
      ps.setString(2, reservationDTO.getDate());
      ps.setString(3, reservationDTO.getTime());
      return ps;
    }, keyHolder);

    Long generatedId = keyHolder.getKey().longValue();
    reservationDTO.setId(generatedId);

    URI location = URI.create("/reservations/" + generatedId);

    return ResponseEntity.created(location).body(reservationDTO);
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
