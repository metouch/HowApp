package th.how.page.main;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialogListener;


import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import th.how.app.R;
import th.how.bean.MessageBean;
import th.how.bean.RobotBean;
import th.how.page.main.adapter.Constans;
import th.how.page.main.adapter.RobotAdapter;


/**
 * 机器人聊天页面
 */
public class RobotChatActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener,
        View.OnClickListener, View.OnTouchListener {

    private static final String TAG = "RobotChatActivity";

    private Vibrator mVibrator;

    private ListView listView;
    private EditText ed;
    private Button btnSend;
    private RobotAdapter robotAdapter;
    private List<MessageBean> list;

    private AIUIAgent mAIUIAgent;
    private GestureDetector mGestureDetector;

//    private RecognizerDialog mIatDialog;//听写框


    private int mAIUIState;

    private ImageView ivAudio;

    private boolean isAudio;//是否是语音输入
    private TextView tvAudio;
    private Gson gson;

    private boolean recording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_chat);
        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        findView();
        initData();
    }

    private void initData() {

        gson = new Gson();
        //创建AIUIAgent
        mAIUIAgent = AIUIAgent.createAgent(this, getAIUIParams(), mAIUIListener);
        //发送`CMD_START`消息，使AIUI处于工作状态
        AIUIMessage startMsg = new AIUIMessage(AIUIConstant.CMD_START, 0, 0, null, null);
        mAIUIAgent.sendMessage(startMsg);
        mGestureDetector = new GestureDetector(this, new MyGestureListener());

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
//        mIatDialog = new RecognizerDialog(RobotChatActivity.this, mInitListener);


        list = new ArrayList<>();
//        listView.setRefreshTime("从未刷新");
        robotAdapter = new RobotAdapter(this, list);
//        listView.setXListViewListener(this);
        listView.setAdapter(robotAdapter);
        listView.setOnItemClickListener(this);
        loadData();
    }

    private void loadData() {
        MessageBean bean = new MessageBean();
        bean.setContent("hi,我是机器人小广");
        bean.setId(list.size());
        bean.setStatus(Constans.MessageStatus.TO);
        list.add(bean);
        robotAdapter.notifyDataSetChanged();
    }

    private void findView() {
        listView = (ListView) findViewById(R.id.listView);
        ed = (EditText) findViewById(R.id.ed_content);
        tvAudio = (TextView) findViewById(R.id.tv_audio);
        ivAudio = (ImageView) findViewById(R.id.iv_audio);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        //tvAudio.setOnClickListener(this);
        tvAudio.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvAudio.setOnTouchListener(this);
        ivAudio.setOnClickListener(this);
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    btnSend.setBackgroundResource(R.drawable.shape_btn_pres);
                } else {
                    btnSend.setBackgroundResource(R.drawable.btn_shape);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    boolean isPress;//是否是按住

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public void onLongPress(MotionEvent e) {
            controlRecord(true);
            mVibrator.vibrate(1000);//振动
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(recording)
                    controlRecord(false);
                break;
        }
        return false;
    }

    /**
     * 控制录音
     * @param flag true 启动录音, false 结束录音
     */
    private void controlRecord(boolean flag){
        recording = flag;
        Log.e(TAG, "flag = " + flag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String content = ed.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast("请输入内容");
                    return;
                }

                if (AIUIConstant.STATE_WORKING != this.mAIUIState) {
                    AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
                    mAIUIAgent.sendMessage(wakeupMsg);
                }

//                sendMessage(content, Constans.MessageStatus.FROM);

                AIUIMessage msg = new AIUIMessage(AIUIConstant.CMD_WRITE, 0, 0, "data_type=text", content.getBytes());
                mAIUIAgent.sendMessage(msg);
                ed.setText("");
                break;
            case R.id.iv_audio:
                isAudio = !isAudio;
                if (isAudio) {
                    ed.setVisibility(View.GONE);
                    btnSend.setVisibility(View.INVISIBLE);
                    tvAudio.setVisibility(View.VISIBLE);
                } else {
                    ed.setVisibility(View.VISIBLE);
                    btnSend.setVisibility(View.VISIBLE);
                    tvAudio.setVisibility(View.GONE);
                }

                break;
            case R.id.tv_audio:

                ///语音客服这里还有问题。。
                //体验也不好。应该在在按住说话的时候。弹出个类似于微信的听筒框，然后放开的时候，语音输入结束，对话框消失。。

                // 先发送唤醒消息，改变AIUI内部状态，只有唤醒状态才能接收语音输入
                isPress = !isPress;
                String params = "sample_rate=16000,data_type=audio";
                if (isPress) {
                    tvAudio.setText("点击停止录音");
                    if (AIUIConstant.STATE_WORKING != this.mAIUIState) {
                        AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
                        mAIUIAgent.sendMessage(wakeupMsg);
                    }
                    // 打开AIUI内部录音机，开始录音 这个时候，也会在监听器里收到相应事件的监听

                    AIUIMessage writeMsg = new AIUIMessage(AIUIConstant.CMD_START_RECORD, 0, 0, params, null);
//                    String audio = new String(writeMsg.data);
//                    Log.e("zmm", "发送语音---》" + audio);
                    mAIUIAgent.sendMessage(writeMsg);
                } else {
                    tvAudio.setText("开始录音");
//                    mIatDialog.dismiss();
                    AIUIMessage writeMsg = new AIUIMessage(AIUIConstant.CMD_STOP_RECORD, 0, 0, params, null);
//                    String audio = new String(writeMsg.data);
//                    Log.e("zmm", "结束语音---》" + audio);
                    mAIUIAgent.sendMessage(writeMsg);
                }
//                String audio = new String(writeMsg.data);
//                Log.e("zmm", "发送语音---》" + audio);
//                sendMessage(audio, Constans.MessageStatus.FROM);
                break;
        }

//        bean.setTime(System.currentTimeMillis());

    }


    public void sendMessage(String content, int status) {
        MessageBean bean = new MessageBean();
        bean.setContent(content);
        bean.setId(list.size());
        bean.setStatus(status);
        list.add(bean);
        robotAdapter.notifyDataSetChanged();
    }

    private String getAIUIParams() {
        String params = "";
        AssetManager assetManager = getResources().getAssets();
        try {
            InputStream ins = assetManager.open("cfg/aiui_phone.cfg");
            byte[] buffer = new byte[ins.available()];

            ins.read(buffer);
            ins.close();

            params = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return params;
    }

    private AIUIListener mAIUIListener = new AIUIListener() {
        StringBuilder mNlpText = new StringBuilder();

        @Override
        public void onEvent(AIUIEvent event) {
            switch (event.eventType) {
                case AIUIConstant.EVENT_WAKEUP:
                    Log.i(TAG, "on event: " + event.eventType);
                    Toast("进入识别状态");
                    break;

                case AIUIConstant.EVENT_RESULT: {
//                    mNlpText.
                    //解析结果
//                    Log.e("zmm", "解析杰锅锅--->" + event.info);
                    try {
                        JSONObject bizParamJson = new JSONObject(event.info);
                        JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
                        JSONObject params = data.getJSONObject("params");
                        JSONObject content = data.getJSONArray("content").getJSONObject(0);

                        if (content.has("cnt_id")) {
                            String cnt_id = content.getString("cnt_id");
                            JSONObject cntJson = new JSONObject(new String(event.data.getByteArray(cnt_id), "utf-8"));

                            mNlpText.append("\n");
                            mNlpText.append(cntJson.toString());

                            String sub = params.optString("sub");
                            if ("nlp".equals(sub)) {
                                // 解析得到语义结果
                                String resultStr = cntJson.optString("intent");
                                try {
                                    RobotBean robotBean = gson.fromJson(resultStr, RobotBean.class);
                                    Log.e("zmm", "解析--->" + resultStr);
                                    int rc = robotBean.getRc();
                                    String text1 = robotBean.getText();
                                    sendMessage(text1, Constans.MessageStatus.FROM);//
//                                    Log.e("zmm","--->")
                                    if (rc == 1) {//输入异常
                                        sendMessage("输入异常", Constans.MessageStatus.TO);
                                    } else if (rc == 2) {//系统内部异常
                                        sendMessage("系统内部异常", Constans.MessageStatus.TO);
                                    } else if (rc == 3) {//业务操作失败，错误信息在error字段描述

                                        RobotBean.AnswerBean answer = robotBean.getAnswer();
                                        String text = null;
                                        if (answer != null) {
                                            text = answer.getText();
                                        }
                                        sendMessage("系统内部异常" + text, Constans.MessageStatus.TO);
                                    } else if (rc == 4) {//文本没有匹配的技能场景，技能不理解或不能处理该文本
                                        sendMessage("抱歉，这个问题太难了，我暂时还不知道。。。", Constans.MessageStatus.TO);
                                    } else if (rc == 0) {// 	操作成功
                                        RobotBean.AnswerBean answer = robotBean.getAnswer();
                                        if (answer != null) {
                                            String text = answer.getText();
                                            sendMessage(text, Constans.MessageStatus.TO);
                                        } else {
                                            sendMessage("抱歉，这个问题还没有答案", Constans.MessageStatus.TO);
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e("zmm", "解析失败-->" + e.getMessage());
                                    sendMessage("小广没有找到相对应的答案..", Constans.MessageStatus.TO);
                                }


//                                sendMessage(resultStr, Constans.MessageStatus.TO);
//                                Log.i(TAG, resultStr);
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        mNlpText.append("\n");
                        mNlpText.append(e.getLocalizedMessage());
                    }

//                    mNlpText.append("\n");
//                    Log.e("zmm", "解析结果-->" + mNlpText);
                }
                break;

                case AIUIConstant.EVENT_ERROR: {
                    Log.i(TAG, "on error: " + event.eventType + event.arg1 + "-->" + event.info);
                    mNlpText.append("\n");
                    mNlpText.append("错误: " + event.arg1 + "\n" + event.info);
                }
                break;

                case AIUIConstant.EVENT_VAD: {
                    if (AIUIConstant.VAD_BOS == event.arg1) {
                        Toast("找到vad_bos");
                    } else if (AIUIConstant.VAD_EOS == event.arg1) {
                        Toast("找到vad_eos");
                    } else {
//                        Toast("EVENT_VAD" + event.arg2);
                    }
                }
                break;

                case AIUIConstant.EVENT_START_RECORD: {
                    Log.i(TAG, "on event: " + event.eventType);
//                    Toast("开始录音");

//                    mIatDialog.setListener(mRecognizerDialogListener);
//                    mIatDialog.show();
                }
                break;

                case AIUIConstant.EVENT_STOP_RECORD: {
                    Log.i(TAG, "on event: " + event.eventType);
                    tvAudio.setText("开始录音");
                    isPress = !isPress;
                    Toast("录音停止");
//                    mIatDialog.dismiss();
                }
                break;

                case AIUIConstant.EVENT_STATE: {    // 状态事件
                    mAIUIState = event.arg1;

                    if (AIUIConstant.STATE_IDLE == mAIUIState) {
                        // 闲置状态，AIUI未开启
//                        Toast("STATE_IDLE");
                        Log.e("zmm", "STATE_IDLE----->");
                    } else if (AIUIConstant.STATE_READY == mAIUIState) {
                        // AIUI已就绪，等待唤醒
//                        Toast("STATE_READY");
                        Log.e("zmm", "STATE_READY----->");
                    } else if (AIUIConstant.STATE_WORKING == mAIUIState) {
                        // AIUI工作中，可进行交互
//                        Toast("STATE_WORKING");
                        Log.e("zmm", "STATE_WORKING----->");
                    }
                }
                break;

                case AIUIConstant.EVENT_CMD_RETURN: {
                    if (AIUIConstant.CMD_UPLOAD_LEXICON == event.arg1) {
//                        Toast("上传" + (0 == event.arg2 ? "成功" : "失败"));
                    }
                }
                break;

                default:
                    break;
            }
        }
    };


    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("zmm", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast("初始化失败，错误码：" + code);
            }
        }
    };


    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
//            printResult(results);

            Log.e("zmm", "--->" + results.getResultString() + "-->" + results.describeContents() + "-->" + isLast);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
//            showTip(error.getPlainDescription(true));
        }

    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAIUIAgent.destroy();
    }

    private void Toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
