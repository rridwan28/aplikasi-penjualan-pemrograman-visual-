package model;

public class MataPelajaran {
    private int mapelId;
    private String kodeMapel;
    private String namaMapel;
    private double kkm;
    private String kategori;
 
    public MataPelajaran() {}
 
    public int getMapelId()            { return mapelId; }
    public void setMapelId(int v)      { this.mapelId = v; }
    public String getKodeMapel()       { return kodeMapel; }
    public void setKodeMapel(String v) { this.kodeMapel = v; }
    public String getNamaMapel()       { return namaMapel; }
    public void setNamaMapel(String v) { this.namaMapel = v; }
    public double getKkm()             { return kkm; }
    public void setKkm(double v)       { this.kkm = v; }
    public String getKategori()        { return kategori; }
    public void setKategori(String v)  { this.kategori = v; }
 
    @Override
    public String toString() { return namaMapel; }
    
}
