package com.timecat.login.data;

/**
 * @author 小星 QQ:753940262
 * @class describe
 * @time 2018/7/7 0007 16:43
 */
public class SmsSend {


    /**
     * code : 200
     * msg : 发送成功
     * data : {"Message":"OK","RequestId":"59F22A72-DD92-418B-B9D7-696B90BCF883","BizId":"773511232002742304^0","Code":"OK"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * Message : OK
         * RequestId : 59F22A72-DD92-418B-B9D7-696B90BCF883
         * BizId : 773511232002742304^0
         * Code : OK
         */

        private String Message;
        private String RequestId;
        private String BizId;
        private String Code;

        public String getMessage() {
            return Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }

        public String getRequestId() {
            return RequestId;
        }

        public void setRequestId(String RequestId) {
            this.RequestId = RequestId;
        }

        public String getBizId() {
            return BizId;
        }

        public void setBizId(String BizId) {
            this.BizId = BizId;
        }

        public String getCode() {
            return Code;
        }

        public void setCode(String Code) {
            this.Code = Code;
        }
    }
}
