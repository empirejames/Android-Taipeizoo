package com.james.zoo;

/**
 * Created by 101716 on 2017/9/12.
 */

public class Animals {
    String TAG = Animals.class.getSimpleName();
    String Location;
    String Geo;
    String Name_Ch;
    String Phylum;
    String Classes;
    String Order;
    String Family;
    String Distribution;
    String Habitat;
    String Interpretation;
    String Pic1_ALT, Pic1_URL;
    String Pic2_ALT,Pic2_URL;
    String Pic3_ALT,Pic3_URL;
    String Pic4_ALT,Pic4_URL;
    String Voice1_ALT, Voice1_URL;
    String Voice2_ALT, Voice2_URL;
    String Voice3_ALT, Voice3_URL;
    String Video;
    public Animals(String Name_Ch,String Location, String Geo , String Phylum, String Classes, String Order
            , String Family, String Distribution, String Habitat, String Interpretation
            , String Pic1_ALT, String Pic1_URL, String Pic2_ALT, String Pic2_URL, String Pic3_ALT
            , String Pic3_URL, String Pic4_ALT, String Pic4_URL, String Voice1_ALT, String Voice1_URL
            , String Voice2_ALT, String Voice2_URL, String Voice3_ALT, String Voice3_URL, String Video) {
        this.Location = Location;
        this.Geo = Geo;
        this.Name_Ch = Name_Ch;
        this.Phylum = Phylum;
        this.Classes = Classes;
        this.Order = Order;
        this.Family = Family;
        this.Distribution = Distribution;
        this.Habitat = Habitat;
        this.Interpretation = Interpretation;
        this.Pic1_ALT = Pic1_ALT;
        this.Pic1_URL = Pic1_URL;
        this.Pic2_ALT = Pic2_ALT;
        this.Pic2_URL = Pic2_URL;

        this.Pic3_ALT = Pic3_ALT;
        this.Pic3_URL = Pic3_URL;
        this.Pic4_ALT = Pic4_ALT;
        this.Pic4_URL = Pic4_URL;
        this.Voice1_ALT = Voice1_ALT;
        this.Voice1_URL = Voice1_URL;
        this.Voice2_ALT = Voice2_ALT;
        this.Voice2_URL = Voice2_URL;
        this.Voice3_ALT = Voice3_ALT;
        this.Voice3_URL = Voice3_URL;
        this.Video = Video;

    }
    public String getLocation() {
        return Location;
    }
    public String getGeo() {
        return Geo;
    }
    public String getName_Ch() {
        return Name_Ch;
    }
    public String getPhylum() {
        return Phylum;
    }
    public String getClasses() {
        return Classes;
    }
    public String getOrder() {
        return Order;
    }
    public String getFamily() {
        return Family;
    }
    public String getDistribution() {
        return Distribution;
    }
    public String getHabitat() {
        return Habitat;
    }
    public String getInterpretation() {
        return Interpretation;
    }
    public String getPic1_ALT() {
        return Pic1_ALT;
    }
    public String getPic1_URL() {
        return Pic1_URL;
    }
    public String getPic2_ALT() {
        return Pic2_ALT;
    }
    public String getPic2_URL() {
        return Pic2_URL;
    }
    public String getPic3_ALT() {
        return Pic3_ALT;
    }
    public String getPic3_URL() {
        return Pic3_URL;
    }
    public String getPic4_ALT() {
        return Pic4_ALT;
    }
    public String getPic4_URL() {
        return Pic4_URL;
    }
    public String getVoice1_ALT() {
        return Voice1_ALT;
    }
    public String getVoice1_URL() {
        return Voice1_URL;
    }
    public String getVoice2_ALT() {
        return Voice2_ALT;
    }
    public String getVoice2_URL() {
        return Voice2_URL;
    }
    public String getVoice3_ALT() {
        return Voice3_ALT;
    }
    public String getVoice3_URL() {
        return Voice3_URL;
    }
    public String getVideo() {
        return Video;
    }


}
