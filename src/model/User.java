package model;

public class User {
    private int     userId;
    private String  username;
    private String  password;
    private String  namaLengkap;
    private String  email;
    private String  role;
    private boolean statusAktif;
    
    public User(){}
    
    public User (int userId, String username, String password, String namaLengkap, 
    String email, String role, boolean statusAktif) {
        this.userId      = userId;
        this.username    = username;
        this.password    = password;
        this.namaLengkap = namaLengkap;
        this.email       = email;
        this.role        = role;
        this.statusAktif = statusAktif;                
    }
    
    public int getUserId()                  {return userId; }
    public void SetUserId(int v )           {this.userId = v; }
    public String getUsername()             {return username; }
    public void setUsername (String v)      {this.username = v; }
    public String getPassword()             {return password; }
    public void setPassword(String v)       {this.password = v; }
    public String getNamaLengkap()          {return namaLengkap; }
    public void setNamaLengkap(String v)    {this.namaLengkap = v; }
    public String getEmail()                {return email; }
    public void setEmail(String v)          {this.email = v; }
    public String getRole()                 {return role; }
    public void setRole (String v)          {this.role = v; }
    public boolean isStatusAktif()          {return statusAktif; }
    public void isStatusAktif(boolean v)    {this.statusAktif = v; }    
    
    @Override
    public String toString() {return namaLengkap; }
    
}
