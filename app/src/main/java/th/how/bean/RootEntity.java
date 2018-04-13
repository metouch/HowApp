package th.how.bean;

/**
 * Created by me_touch on 2017/8/16.
 *
 */

public class RootEntity {
    private boolean breturn;
    private String errorinfo;
    private int iretrun ;

    public void setIretrun(int iretrun) {
        this.iretrun = iretrun;
    }

    public int getIretrun() {
        return iretrun;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    public String getErrorinfo() {
        return errorinfo;
    }

    public void setBreturn(boolean breturn) {
        this.breturn = breturn;
    }

    public boolean getBreturn(){
        return breturn;
    }


}
