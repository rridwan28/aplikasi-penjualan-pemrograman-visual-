package model;

public class Guru {
    
    private int     guruId;       
    private String  nip;
    private String  namaLengkap;
    private String  mataPelajaran;
    private String  noTelp;
    private String  email;
    private boolean statusAktif;
 
    public Guru(){}
    
    public int    getGuruId()                {return guruId; }
    public void   setGuruId(int v)           {this.guruId = v; }
    public String getNip()                   {return nip; }
    public void   setNip(String v)           {this.nip = v; }
    public String getNamaLengkap()           {return namaLengkap; }
    public void   setNamaLengkap(String v)   {this.namaLengkap = v; }
    public String getMataPelajaran()         {return mataPelajaran; }
    public void   setMataPelajaran(String v) {this.mataPelajaran = v; }
    public String getNoTelp()                {return noTelp; }
    public void   setNoTelp(String v)        {this.noTelp = v; }
    public String getEmail()                 {return email; }
    public void   setEmail(String v)         {this.email = v; }
    public boolean isStatusAktif()           {return statusAktif; }
    public void   setStatusAktif(boolean v)  {this.statusAktif = v; }
 
    @Override
    public String toString() { return namaLengkap + (nip != null ? " (" + nip + ")" : ""); }

    
}
