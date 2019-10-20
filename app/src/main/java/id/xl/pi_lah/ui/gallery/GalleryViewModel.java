package id.xl.pi_lah.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<String> mText = new MutableLiveData<>();

    public GalleryViewModel() {

    }

    public LiveData<String> getText() {
        return mText;
    }

}