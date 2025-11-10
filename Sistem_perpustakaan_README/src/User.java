public abstract class User {
    protected int userId;
    protected String username;
    protected String password;
    protected String role;

    public User(int userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public abstract void menu();

    public boolean login(String inputUser, String inputPass) {
        return this.username.equals(inputUser) && this.password.equals(inputPass);
    }

    public void logout() {
        System.out.println(username + " telah keluar dari sistem.");
    }

    public String getRole() { return role; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
