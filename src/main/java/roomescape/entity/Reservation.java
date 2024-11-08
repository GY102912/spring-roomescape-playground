package roomescape.entity;

public class Reservation {
  private long id;
  private String name;
  private String date;
  private String time;


  public Reservation(String name, String date, String time) {
    this.name = name;
    this.date = date;
    this.time = time;
  }

  public long getId() { return id; }
  public String getName() { return name; }
  public String getDate() { return date; }
  public String getTime() { return time; }

  private void setId(long id) { this.id = id; }

  public Reservation toEntity(Reservation reservation, Long id) {
    reservation.setId(id);
    return reservation;
  }
}

