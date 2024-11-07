package roomescape.entity;

public class Reservation {
  private long id;
  private String date;
  private String time;
  private String description;

  public Reservation(String date, String time, String description) {
    this.date = date;
    this.time = time;
    this.description = description;
  }
  public Reservation(long id, String date, String time, String description) {
    this.id = id;
  }

  public long getId() { return id; }
  public String getDate() { return date; }
  public String getTime() { return time; }
  public String getDescription() { return description; }
}

