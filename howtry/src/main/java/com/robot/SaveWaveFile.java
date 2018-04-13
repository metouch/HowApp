package com.robot;

import android.media.AudioRecord;
import android.os.Message;
import android.util.Log;

import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIMessage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by me_touch on 17-11-20.
 *
 */

public class SaveWaveFile implements Runnable{

    private volatile boolean recording;
    private String fileName;

    private AudioRecord recorder;
    private RecordHandler handler;
    private AIUIAgent agent;


    public SaveWaveFile(AIUIAgent agent, AudioRecord record, RecordHandler handler, String fileName){
        this.recorder = record;
        this.handler = handler;
        this.fileName = fileName;
        this.agent = agent;
    }

    @Override
    public void run() {
        save2wav(agent, fileName);
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    private void save2wav(AIUIAgent agent, String waveName){
        System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
        File file = new File(waveName);
        if(file.exists()){
            file.delete();
        }
        recording = true;
        try {
            file.createNewFile();
            RandomAccessFile rf = new RandomAccessFile(file, "rw");
            rf.write(waveFileHeader(0, 0, RecordHelper.AUDIO_RATE, 1, 16));
            byte[] arr = new byte[RecordHelper.BUFFER_SIZE];
            int length = 0;
            while (recording){
                int size = recorder.read(arr, 0, RecordHelper.BUFFER_SIZE);
                if(size > 0){
                    try{
                        long v = 0;
                        // 将 buffer 内容取出。进行平方和运算
                        for (int i = 0; i < size; i++) {
                            v += arr[i] * arr[i];
                        }
                        // 平方和除以数据总长度，得到音量大小。
                        double mean = v / (double) size;
                        int volume = (int)Math.log10(mean);
                        Log.e("SaveWaveFile", "volume = " + volume);
                        if(volume < 1) volume = 1;
                        if(volume > 7) volume = 7;
                        Message message = new Message();
                        message.what = RecordHandler.MSG_VOLUME;
                        message.arg1 = volume;
                        handler.sendMessage(message);
                        rf.write(arr);
                        length += size;
                        writeAudio(agent, arr);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            }
            System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
            writeAudio(agent, null);
            closeWaveFile(rf, length);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void writeAudio(AIUIAgent agent, byte[] audio){
        String params = "data_type=audio,sample_rate=16000";
        AIUIMessage msg;
        if(audio == null)
            msg = new AIUIMessage(AIUIConstant.CMD_STOP_WRITE, 0, 0, params, audio);
        else
            msg = new AIUIMessage(AIUIConstant.CMD_WRITE, 0, 0, params, audio);
        agent.sendMessage(msg);
    }

    private void closeWaveFile(RandomAccessFile rf, int length) throws IOException{
        Log.e("RecordHelper", "length = " + length);
        rf.seek(4);
        rf.writeInt(Integer.reverseBytes(length + 36));
        rf.seek(40);
        rf.writeInt(Integer.reverseBytes(length));
        rf.close();
    }

    private byte[] waveFileHeader(int totalAudioLen, int totalDataLen, int longSampleRate,
                                  int channels, long bit) throws IOException {
        int byteRate = (int)(longSampleRate * channels * bit / 8);
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) ( channels * bit / 8); // block align
        header[33] = 0;
        header[34] = (byte) bit; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        return header;
    }
}
