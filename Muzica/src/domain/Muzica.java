package domain;

import repository.HasId;

import java.util.Objects;

public class Muzica implements HasId<String> {
    private String id;
    private String artist;
    private String album;
    private String verdict;
    private String melodii;
    private String recomandare;

    public Muzica(){}

    public Muzica (String artist,String album, String verdict, String melodii, String recomandare){
        this.artist=artist;
        this.album=album;
        this.verdict=verdict;
        this.melodii=melodii;
        this.recomandare=recomandare;
        this.id=(artist+"&"+ album);
    }

    public Muzica buildObject(String line){
        if(line.equals("")){
            return null;
        }else{
            String[] args = line.split("~~~");
            final Muzica m = new Muzica(args[0],args[1],args[2],args[3],args[4]);
            return m;
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getVerdict() {
        return verdict;
    }

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    public String getMelodii() {
        return melodii;
    }

    public void setMelodii(String melodii) {
        this.melodii = melodii;
    }

    public String getRecomandare() {
        return recomandare;
    }

    public void setRecomandare(String recomandare) {
        this.recomandare = recomandare;
    }

    public String toString() {return  artist+"~~~"+album+"~~~"+verdict+"~~~"+melodii+"~~~"+recomandare;}

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Muzica m = (Muzica) o;
        return Objects.equals(id, m.id);
    }

    public int hashCode() {return Objects.hash(id,verdict,melodii,recomandare);}
}
