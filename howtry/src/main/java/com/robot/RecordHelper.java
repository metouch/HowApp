package com.robot;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIMessage;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by me_touch on 17-11-8.
 *
 */

public class RecordHelper {

    private final static String BASE_PATH = "/gzrb/record";
    private final static String STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public final static String AUDIO_STORAGE_PATH = STORAGE_PATH + BASE_PATH;
    private final static int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    public final static int AUDIO_RATE = 16 * 1000;
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private final static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    public final static int BUFFER_SIZE = 4 * AudioRecord.getMinBufferSize(AUDIO_RATE,AUDIO_CHANNEL,AUDIO_FORMAT);

    private volatile static RecordHelper instance;

    private AudioRecord recorder;
    private ExecutorService executor;
    private SaveWaveFile saveAction;

    private volatile boolean isRecording = false;


    public static RecordHelper getInstance(){
        if(null == instance){
            synchronized (RecordHelper.class){
                if(null == instance)
                    instance = new RecordHelper();
            }
        }
        return instance;
    }
    private RecordHelper(){
        executor = Executors.newSingleThreadExecutor();
        try{
            validDirectory();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

//    public String startRecord(final AIUIAgent agent,String fileName){
//        final String wavePath = AUDIO_STORAGE_PATH + "/" + fileName + ".wav";
//        if(null == recorder)
//            recorder = new AudioRecord(AUDIO_SOURCE, AUDIO_RATE, AUDIO_CHANNEL, AUDIO_FORMAT, BUFFER_SIZE);
//        recorder.startRecording();
//        isRecording = true;
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                //save2pcm(agent,filePath, wavePath);
//                save2wav(agent, wavePath);
//            }
//        });
//        return wavePath;
//    }
//
//    public void stopRecording(){
//        recorder.stop();
//        recorder.release();
//        recorder = null;
//        isRecording = false;
//    }

    public void startRecord(final AIUIAgent agent, RecordHandler handler, String fileName){
        final String wavePath = AUDIO_STORAGE_PATH + "/" + fileName + ".wav";
        if(null == recorder)
            recorder = new AudioRecord(AUDIO_SOURCE, AUDIO_RATE, AUDIO_CHANNEL, AUDIO_FORMAT, BUFFER_SIZE);
        saveAction = new SaveWaveFile(agent, recorder, handler, wavePath);
        recorder.startRecording();
        executor.execute(saveAction);
    }

    public void stopRecording(){
        recorder.stop();
        recorder.release();
        recorder = null;
        if(saveAction != null)
            saveAction.setRecording(false);
    }

    private void validDirectory() throws FileNotFoundException{
        File file = new File(AUDIO_STORAGE_PATH);
        System.out.println(AUDIO_STORAGE_PATH);
        if(!file.exists() || !file.isDirectory()){
            boolean succeed = file.mkdirs();
            if(!succeed) throw new FileNotFoundException("directory creates failed");
        }
    }



    @SuppressWarnings("")
    private void save2pcm(AIUIAgent agent, String fileName, String waveName){
        File file = new File(fileName);
        if(file.exists())
            file.delete();
        try{
            file.createNewFile();
        }catch(Exception e){
            e.printStackTrace();
        }
        byte[] arr = new byte[BUFFER_SIZE];
        BufferedOutputStream os = null;
        try{
            os = new BufferedOutputStream(new FileOutputStream(file));
        }catch(Exception e){
            e.printStackTrace();
        }
        while (isRecording){
            int size = recorder.read(arr, 0, BUFFER_SIZE);
            if(size >= 0){
                try{
                    os.write(arr);
                    writeAudio(agent, arr);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
            System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        }
        System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTT");
        try{
            os.flush();
            os.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        writeAudio(agent, null);
        copyWaveFile(fileName, waveName);
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

    private void save2wav(AIUIAgent agent, String waveName){
        File file = new File(waveName);
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            RandomAccessFile rf = new RandomAccessFile(file, "rw");
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//            DataOutputStream dos = new DataOutputStream(bos);
            //addWaveHeader(dos, AUDIO_RATE, (short) 1, (short) 16);
//            ByteBuffer byteBuffer = ByteBuffer.allocate(44);
//            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
//            byteBuffer.putInt(0x46464952);
//            byteBuffer.putInt(0);
//            byteBuffer.putInt(0x45564157);
//            byteBuffer.putInt(0x20746d66);
//            byteBuffer.putInt(16);
//            byteBuffer.putShort((short)1);
//            byteBuffer.putShort((short) 1);
//            byteBuffer.putInt(AUDIO_RATE);
//            byteBuffer.putInt((AUDIO_RATE* 1 * 2));
//            byteBuffer.putShort((short) (2));
//            byteBuffer.putShort((short) (16));
//            byteBuffer.putInt(0x61746164);
//            byteBuffer.putInt(0);
//            dos.write(byteBuffer.array());
            //dos.write(waveFileHeader(0, 0, AUDIO_RATE, 1, 16));
            rf.write(waveFileHeader(0, 0, AUDIO_RATE, 1, 16));
            byte[] arr = new byte[BUFFER_SIZE];
            int length = 0;
            while (isRecording){
                int size = recorder.read(arr, 0, BUFFER_SIZE);
                if(size > 0){
                    try{
                        rf.write(arr);
                        length += size;
                        writeAudio(agent, arr);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            }
            writeAudio(agent, null);
            closeWaveFile(rf, length);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void addWaveHeader(DataOutputStream rf, int sampleRate, short channels, short byteRate) throws Exception{
        rf.writeBytes("RIFF");
        rf.writeInt(0);
        rf.writeBytes("WAVE");
        rf.writeBytes("fmt ");
        rf.writeInt(Integer.reverseBytes(16));
        rf.writeShort(Short.reverseBytes((short) 1));
        rf.writeShort(Short.reverseBytes(channels));
        rf.writeInt(Integer.reverseBytes(sampleRate));
        rf.writeInt(Integer.reverseBytes(sampleRate * channels * byteRate / 8));
        rf.writeShort(Short.reverseBytes((short)(channels * byteRate / 8)));
        rf.writeShort(Short.reverseBytes(byteRate));
        rf.writeBytes("data");
        rf.write(0);
    }

    private void closeWaveFile(RandomAccessFile rf, int length) throws IOException{
        Log.e("RecordHelper", "length = " + length);
        rf.seek(4);
        rf.writeInt(Integer.reverseBytes(length + 36));
        rf.seek(40);
        rf.writeInt(Integer.reverseBytes(length));
        rf.close();
    }

    // 这里得到可播放的音频文件
    private void copyWaveFile(String inFilename, String outFilename) {
        System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTT");
        File file = new File(outFilename);
        try{
            if(!file.exists())
                file.createNewFile();
        }catch(Exception e){
            e.printStackTrace();
        }

        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = 0;
        long longSampleRate = AUDIO_RATE;

        int channels = 1;

        byte[] data = new byte[BUFFER_SIZE];
        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;
            WriteWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, 16);
            while (in.read(data) != -1) {
                out.write(data);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try{
//            RandomAccessFile rf = new RandomAccessFile(outFilename, "rw");
//            long length = rf.getChannel().size();
//            rf.seek(0);
//            addWaveHeader(rf, AUDIO_RATE, (short) 1, (short) 16);
//            rf.seek(4);
//            rf.writeInt(Integer.reverseBytes( (int)(length - 8) ));
//            rf.seek(40);
//            rf.writeInt(Integer.reverseBytes((int)(length - 44) ));
//            rf.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }

    /**
     * 这里提供一个头信息。插入这些信息就可以得到可以播放的文件。
     * 为我为啥插入这44个字节，这个还真没深入研究，不过你随便打开一个wav
     * 音频的文件，可以发现前面的头文件可以说基本一样哦。每种格式的文件都有自己特有的头文件。
     */
    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate, int channels, int bit) throws IOException {
        int byteRate = (int)longSampleRate * channels * bit / 8;
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
        header[32] = (byte) ( bit * channels / 8); // block align
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
        out.write(header, 0, 44);
    }

    private byte[] waveFileHeader(int totalAudioLen, int totalDataLen, int longSampleRate, int channels, long bit) throws IOException {
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