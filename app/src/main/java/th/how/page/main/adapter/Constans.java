package th.how.page.main.adapter;

/**
 * Created by Administrator on 2017-11-06.
 */

public class Constans {
    public static final int RESULT_OK = 0;//接口返回请求总标识，顶层code字段等于0未成功，非0为失败

    private int currect_action = REFRESH_ACTION;
    private static final int REFRESH_ACTION = 10;
    private static final int LOADMORE_ACTION = 11;
    private static final int PAGE_COUNT = 20;
    private int PAGE = 1;

    public interface MessageStatus {
        int FROM = 1;//发送方
        int TO = 2;//接收方
    }

}
