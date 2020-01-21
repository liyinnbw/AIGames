package com.projectcs2103t.chinesechess;

/**
 * Created by yoyo on 2015/7/25.
 */
public class Move{
    public int fromR;
    public int fromC;
    public int toR;
    public int toC;
    public int rmPiec;
    public Move(int fr, int fc, int tr, int tc, int rm){
        fromR = fr;
        fromC = fc;
        toR   = tr;
        toC   = tc;
        rmPiec= rm;
    }
    @Override
    public String toString(){
        return "("+fromC+","+fromR+")->("+toC+","+toR+")kill="+rmPiec;
    }
}
