package com.medi.medipass;

/**
 * Created by Elizabeth on 2016-05-31.
 */
public class WaitListItem {
    //waithos, waitnum
    private String hosname;
    private String hoswait;

    public String getHosname(){return hosname;}

    public void setHosname(String hosname){this.hosname = hosname;}

    public String getHoswait(){return hoswait;}

    public void setHoswait(String hoswait){this.hoswait = hoswait;}

    public WaitListItem(String hosname, String hoswait){
        this.hosname=hosname;
        this.hoswait=hoswait;
    }

}
