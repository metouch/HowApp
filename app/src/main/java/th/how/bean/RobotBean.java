package th.how.bean;

/**
 * Created by Administrator on 2017-11-07.
 */

public class RobotBean {

    /**
     * sid : cid6f1cc82a@ch00b80d5c55f3040001
     * operation : ANSWER
     * text : 你好
     * status : 0
     * service : openQA
     * man_intv :
     * answer : {"answerType":"openQA","topicID":"32184199507836297","text":"你好，又见面了真开心。","question":{"question_ws":"你好/VI//","question":"你好"},"type":"T","emotion":"default"}
     * uuid : atn00182def@ch3d720d5c55f36f2601
     * rc : 0
     * no_nlu_result : 0
     */

    private String sid;
    private String operation;
    private String text;
    private int status;
    private String service;
    private String man_intv;
    private AnswerBean answer;
    private String uuid;
    private int rc;
    private int no_nlu_result;

    private ErrorBean error;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMan_intv() {
        return man_intv;
    }

    public void setMan_intv(String man_intv) {
        this.man_intv = man_intv;
    }

    public AnswerBean getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerBean answer) {
        this.answer = answer;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public int getNo_nlu_result() {
        return no_nlu_result;
    }

    public void setNo_nlu_result(int no_nlu_result) {
        this.no_nlu_result = no_nlu_result;
    }

    public static class AnswerBean {
        /**
         * answerType : openQA
         * topicID : 32184199507836297
         * text : 你好，又见面了真开心。
         * question : {"question_ws":"你好/VI//","question":"你好"}
         * type : T
         * emotion : default
         */

        private String answerType;
        private String topicID;
        private String text;
        private QuestionBean question;
        private String type;
        private String emotion;

        public String getAnswerType() {
            return answerType;
        }

        public void setAnswerType(String answerType) {
            this.answerType = answerType;
        }

        public String getTopicID() {
            return topicID;
        }

        public void setTopicID(String topicID) {
            this.topicID = topicID;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public QuestionBean getQuestion() {
            return question;
        }

        public void setQuestion(QuestionBean question) {
            this.question = question;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getEmotion() {
            return emotion;
        }

        public void setEmotion(String emotion) {
            this.emotion = emotion;
        }

        public static class QuestionBean {
            /**
             * question_ws : 你好/VI//
             * question : 你好
             */

            private String question_ws;
            private String question;

            public String getQuestion_ws() {
                return question_ws;
            }

            public void setQuestion_ws(String question_ws) {
                this.question_ws = question_ws;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }
        }
    }

    private  static class ErrorBean {

        /**
         * message : ERROR:No result from data source || No result from data source
         * code : 3001
         */

        private String message;
        private int code;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
