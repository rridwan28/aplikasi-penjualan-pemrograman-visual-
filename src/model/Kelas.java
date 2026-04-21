
package model;

public class Kelas {
    private int kelasId;
    private String namaKelas;
    private String tahunAjaran;
    private String semester;
    private int waliKelasId;
    private String namaWaliKelas;
 
    public Kelas() {}
 
    public int getKelasId()            { return kelasId; }
    public void setKelasId(int v)      { this.kelasId = v; }
    public String getNamaKelas()       { return namaKelas; }
    public void setNamaKelas(String v) { this.namaKelas = v; }
    public String getTahunAjaran()     { return tahunAjaran; }
    public void setTahunAjaran(String v){ this.tahunAjaran = v; }
    public String getSemester()        { return semester; }
    public void setSemester(String v)  { this.semester = v; }
    public int getWaliKelasId()        { return waliKelasId; }
    public void setWaliKelasId(int v)  { this.waliKelasId = v; }
    public String getNamaWaliKelas()   { return namaWaliKelas; }
    public void setNamaWaliKelas(String v){ this.namaWaliKelas = v; }
 
    @Override
    public String toString() { return namaKelas + " (" + tahunAjaran + " Sem." + semester + ")"; }
    
}
