package th.how.bean;
import th.how.bean.IClientInterface;

interface IBinderPool {

    IBinder getBinder(int code);
    void register(IClientInterface callback);
    void unRegister(IClientInterface callback);
}
