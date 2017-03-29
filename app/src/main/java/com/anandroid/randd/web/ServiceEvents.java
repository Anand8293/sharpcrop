package com.anandroid.randd.web;

public interface ServiceEvents {

    public void StartedRequest();
    public void Finished(String methodName, Object Data);
    public void FinishedWithException(Exception ex);
    public void EndedRequest();
}