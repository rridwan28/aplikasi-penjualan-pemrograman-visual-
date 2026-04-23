package model;

public class Siswa {
    private int siswaId;
    private String nis;
    private String namaLengkap;
    private String jenisKelamin;
    private String tanggalLahir;
    private String alamat;
    private String namaOrtu;
    private String noTelp;
    private boolean statusAktif;
    
    public Siswa(){}
    
    public int getSiswaId()                 {return siswaId; }
    public void setSiswaId(int v)           {this.siswaId = v; }
    public String getNis()                  {return nis; }
    public void setNis(String v)            {this.nis = v; }
    public String getNamaLengkap()          {return namaLengkap; }
    public void setNamaLengkap(String v)    {this.namaLengkap = v; }
    public String getJenisKelamin()         {return jenisKelamin; }
    public void setJenisKelamin(String v)   {this.jenisKelamin = v; }
    public String getTanggalLahir()         {return tanggalLahir; }
    public void setTanggalLahir(String v)   {this.tanggalLahir = v; }
    public String getAlamat()               {return alamat; }
    public void setAlamat(String v)         {this.alamat = v; }
    public String getNamaOrtu()             {return namaOrtu; }
    public void setNamaOrtu(String v)       {this.namaOrtu = v; }
    public String getNoTelp()               {return noTelp; }
    public void setNoTelp(String v)         {this.noTelp = v; }
    public boolean isStatusAktif()          {return statusAktif; }
    public void setStatusAktif(boolean v)   {this.statusAktif = v; }
    
    @Override
    public String toString() {return namaLengkap + "(" + nis + ")"; }
     
}
