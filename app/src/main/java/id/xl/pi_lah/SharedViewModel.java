package id.xl.pi_lah;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> headerResponse = new MutableLiveData<>();
    private MutableLiveData<String> bodyResponse = new MutableLiveData<>();
    private MutableLiveData<String> channelUrl = new MutableLiveData<>();

    public void setHeaderResponse(String input){
        headerResponse.setValue(input);
    }

    public LiveData<String> getHeaderResponse(){
        return headerResponse;
    }

    public void setBodyResponse(String input){
        bodyResponse.setValue(input);
    }

    public LiveData<String> getBodyResponse(){
        return bodyResponse;
    }

    public void setChannelUrl(String input){
        channelUrl.setValue(input);
    }

    public LiveData<String> getChannelUrl(){
        return channelUrl;
    }
}
