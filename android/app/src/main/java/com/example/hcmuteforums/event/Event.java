package com.example.hcmuteforums.event;

public class Event<T>{
    private boolean hasBeenHandled = false;
    private T content;
    public Event(T object){
        this.content = object;
    }

    public T getContent() {
        if (hasBeenHandled)
            return null;
        hasBeenHandled = true;
        return content;
    }

    public T peakContent(){
        return content;
    }
}
