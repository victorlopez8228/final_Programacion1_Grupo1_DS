public class User {

  private int id;
  private String username;
  private String password;
  private String email;
  private boolean isActive;

  public User() {
  }

  public User(int id, String username, String password, String email, boolean isActive) {
    this.id          = id;
    this.username    = username;
    this.password    = password;
    this.email       = email;
    this.isActive    = isActive;
  }
