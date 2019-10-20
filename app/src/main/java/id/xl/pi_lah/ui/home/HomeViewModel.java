package id.xl.pi_lah.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> capacity = new MutableLiveData<>();
    private MutableLiveData<String> count = new MutableLiveData<>();
    private MutableLiveData<String> battery = new MutableLiveData<>();


    public HomeViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setCapacity(String input){
        capacity.setValue(input);
    }

    public LiveData<String> getCapacity(){
        return capacity;
    }

    public void setCount(String input){
        count.setValue(input);
    }

    public LiveData<String> getCount(){
        return count;
    }

    public void setBattery(String input){
        battery.setValue(input);
    }

    public LiveData<String> getBattery(){
        return battery;
    }

}