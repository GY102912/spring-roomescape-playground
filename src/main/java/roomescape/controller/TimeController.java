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
import roomescape.entity.Time;

@Controller
public class TimeController {

  @Autowired
  JdbcTemplate jdbcTemplate;

  private final RowMapper<Time> rowMapper = (rs, rowNum) ->
    Time.create(rs.getLong("id"), rs.getString("value"));

  @GetMapping("/times")
  @ResponseBody
  public ResponseEntity<List<Time>> getAllTimes() {
    String selectSql = "select * from time";
    List<Time> times = jdbcTemplate.query(selectSql, rowMapper);
    return new ResponseEntity<>(times, HttpStatus.OK);
  }

  @PostMapping("/times")
  @ResponseBody
  public ResponseEntity<Time> createTime(@RequestBody @Valid Time request) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    String insertSql = "INSERT INTO time (time) VALUES (?)";
    jdbcTemplate.update((connection) -> {
      PreparedStatement ps = connection.prepareStatement(insertSql, new String[]{"id"});
      ps.setString(1, request.getTime());
      return ps;
    }, keyHolder);

    Long generatedId = keyHolder.getKey().longValue();
    Time response = Time.create(generatedId, request.getTime());
    URI location = URI.create("/times/" + generatedId);
    return ResponseEntity.created(location).body(response);
  }

  @DeleteMapping("/times/{timeId}")
  @ResponseBody
  public ResponseEntity<Void> deleteTime(@PathVariable Long timeId) {
    String deleteSql = "DELETE FROM time WHERE id = ?";

    int rowsAffected = jdbcTemplate.update(deleteSql, timeId);
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
