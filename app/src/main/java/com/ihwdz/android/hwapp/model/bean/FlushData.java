package com.ihwdz.android.hwapp.model.bean;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/04
 * desc : 首页－每日讯－小红点
 * version: 1.0
 * </pre>
 */
public class FlushData {

    public String code;
    public String msg;
    public FlushModel data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FlushModel getData() {
        return data;
    }

    public void setData(FlushModel data) {
        this.data = data;
    }

    public static class FlushModel{
        private String fastNewsTag = "";
        private String systemNewsTag = "";

        public String getFastNewsTag() {
            return fastNewsTag;
        }

        public void setFastNewsTag(String fastNewsTag) {
            this.fastNewsTag = fastNewsTag;
        }

        public String getSystemNewsTag() {
            return systemNewsTag;
        }

        public void setSystemNewsTag(String systemNewsTag) {
            this.systemNewsTag = systemNewsTag;
        }
    }
}
