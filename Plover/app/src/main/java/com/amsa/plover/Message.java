package com.amsa.plover;

public class Message {
    private String Msg;
    private String MsgId;
    private String MsgType;

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public Message(){

    }

    public Message(String msg, String msgId, String msgType) {
        Msg = msg;
        MsgId = msgId;
        MsgType = msgType;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String msgId) {
        MsgId = msgId;
    }
}
