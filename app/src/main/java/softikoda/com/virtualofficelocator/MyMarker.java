package softikoda.com.virtualofficelocator;

/**
 * Created by Geofrey on 2/15/2016.
 */
public class MyMarker {

    double Mlatitude;
    double Mlongitudes;
    String Mid;
    String Mprovider;
    String Maddress1;
    String Maddress2;
    String Maddress3;
    String Mavailable_services;
    String Mmissing_services;
    int Msum_weight;
    int MdistanceMetres;

    public MyMarker(double latitude,double longitudes,final String id,final String provider,final String address1,final String address2,final String address3,String available_services,String missing_services,int sum_weight,int distanceMetres)
    {
        this.Mlatitude=latitude;
       this.Mlongitudes=longitudes;
        this.Mid = id;
        this.Mprovider = provider;
        this.Maddress1 = address1;
        this.Maddress2 = address2;
        this.Maddress3 = address3;
        this.Mavailable_services = available_services;
        this.Mmissing_services = missing_services;
        this.Msum_weight = sum_weight;
        this.MdistanceMetres = distanceMetres;
    }

    public double getMlatitude() {
        return Mlatitude;
    }

    public void setMlatitude(double mlatitude) {
        Mlatitude = mlatitude;
    }

    public double getMlongitudes() {
        return Mlongitudes;
    }

    public void setMlongitudes(double mlongitudes) {
        Mlongitudes = mlongitudes;
    }

    public String getMid() {
        return Mid;
    }

    public void setMid(String mid) {
        Mid = mid;
    }

    public String getMprovider() {
        return Mprovider;
    }

    public void setMprovider(String mprovider) {
        Mprovider = mprovider;
    }

    public String getMaddress1() {
        return Maddress1;
    }

    public void setMaddress1(String maddress1) {
        Maddress1 = maddress1;
    }

    public String getMaddress2() {
        return Maddress2;
    }

    public void setMaddress2(String maddress2) {
        Maddress2 = maddress2;
    }

    public String getMaddress3() {
        return Maddress3;
    }

    public void setMaddress3(String maddress3) {
        Maddress3 = maddress3;
    }

    public String getMavailable_services() {
        return Mavailable_services;
    }

    public void setMavailable_services(String mavailable_services) {
        Mavailable_services = mavailable_services;
    }

    public String getMmissing_services() {
        return Mmissing_services;
    }

    public void setMmissing_services(String mmissing_services) {
        Mmissing_services = mmissing_services;
    }

    public int getMsum_weight() {
        return Msum_weight;
    }

    public void setMsum_weight(int msum_weight) {
        Msum_weight = msum_weight;
    }

    public int getMdistanceMetres() {
        return MdistanceMetres;
    }

    public void setMdistanceMetres(int mdistanceMetres) {
        MdistanceMetres = mdistanceMetres;
    }
}
