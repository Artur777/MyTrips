package drawable.androidespoteldemo.HTTP;

/**
 * Created by AChojeck on 10/03/2015.
 */
public class MDemoData {

    private String deviceid;
    private String securekey;
    private String type;
    private String displayname;
    private String conntype;
    private String conndata ;
    private String measures ;
    private String location ;
    private String parent;


    public void setConntype(String conntype) {
        this.conntype = conntype;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSecurekey(String securekey) {
        this.securekey = securekey;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public void setConndata(String conndata) {
        this.conndata = conndata;
    }

    public void setMeasures(String measures) {
        this.measures = measures;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getSecurekey() {
        return securekey;
    }

    public String getType() {
        return type;
    }


    public String getDisplayname() {
        return displayname;
    }


    public String getConntype() {
        return conntype;
    }


    public String getConndata() {
        return conndata;
    }


    public String getMeasures() {
        return measures;
    }

    public String getLocation() {
        return location;
    }


    public String getParent() {
        return parent;
    }


}
