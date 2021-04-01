package com.foxminded.android.task2.dto;

public class OperationItem {

    private String mName;
    private String mTime;

    private Integer mNumber;
    private boolean isOperationOn;

    public OperationItem (String name, String time, boolean operationFlag, Integer number){
        mName = name;
        mTime = time;
        mNumber = number;
        isOperationOn = operationFlag;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public Integer getNumber() {
        return mNumber;
    }

    public void setNumber(Integer number) {
        mNumber = number;
    }
    public boolean isOperationOn() {
        return isOperationOn;
    }

    public void setOperationOn(boolean operationOn) {
        isOperationOn = operationOn;
    }

}
