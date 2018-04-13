package th.how.bean;
import th.how.bean.NewsEntity;
import th.how.bean.IClientInterface;

interface IMyAidlInterface {
    NewsEntity getData(int id, String title);
    void register(IClientInterface callback);
    void unRegister(IClientInterface callback);
}
