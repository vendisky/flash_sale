package com.imooc.flashsale.vo;

import com.imooc.flashsale.validator.IsMobile;
import com.sun.istack.internal.NotNull;
import org.hibernate.validator.constraints.Length;

public class LoginVo {

    @NotNull @IsMobile private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVo [mobile=" + mobile + ", password=" + password + "]";
    }
}
