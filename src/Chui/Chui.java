import java.io.*;
import okhttp3.*;
public class Chui {
    public static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");
    //OkHttpClient client = new OkHttpClient();

    public boolean spoofDetection(String picture) throws Exception {
        RequestBody formBody = new FormBody.Builder()
                .add(picture)

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .header("x-api-key", "vOjf0XRyf72QJzFOVxff7aKYtUeRBtgR6MXAMzPe")
                .addHeader("Content-Type", "image/jpeg")
                .url("https://api.chui.ai/v1/spdetect")
                .post(formBody)
                .build();



       return true;
   }

}
