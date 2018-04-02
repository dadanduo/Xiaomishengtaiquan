package com.xiaomishengtaiquan.Model;

/**
 * Created by chenda on 2018/3/27.
 */

public class Register {

    /**
     * mobilePhone : 18011009889
     * smsCode : 123456
     * inviteCode : 654321
     * channel : Matilda
     * product : Matilda
     */

    private String mobilePhone;
    private String smsCode;
    private String inviteCode;
    private String channel;
    private String product;

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
