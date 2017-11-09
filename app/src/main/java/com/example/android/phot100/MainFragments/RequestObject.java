package com.example.android.phot100.MainFragments;

/**
 * Created by Robert on 03/11/2017.
 */

public class RequestObject {

    public String received;


    public RequestObject(){}

    public RequestObject(String received){
        this.received = received;
    }

    public String getReceived(){return received;}

    public void setReceived(String received){this.received = received;}

}
