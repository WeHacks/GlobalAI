package thebrewerytech.paymentconnectorsdk;

import android.accounts.Account;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.Toast;

import com.clover.connector.sdk.v3.PaymentConnector;
import com.clover.connector.sdk.v3.PaymentV3Connector;
import com.clover.sdk.internal.util.BitmapUtils;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.tender.TenderConnector;
import com.clover.sdk.v3.order.VoidReason;
import com.clover.sdk.v3.payments.Payment;
import com.clover.sdk.v3.remotepay.AuthResponse;
import com.clover.sdk.v3.remotepay.CapturePreAuthResponse;
import com.clover.sdk.v3.remotepay.ConfirmPaymentRequest;
import com.clover.sdk.v3.remotepay.ManualRefundResponse;
import com.clover.sdk.v3.remotepay.PreAuthResponse;
import com.clover.sdk.v3.remotepay.ReadCardDataResponse;
import com.clover.sdk.v3.remotepay.RefundPaymentResponse;
import com.clover.sdk.v3.remotepay.RetrievePendingPaymentsResponse;
import com.clover.sdk.v3.remotepay.SaleRequest;
import com.clover.sdk.v3.remotepay.SaleResponse;
import com.clover.sdk.v3.remotepay.TipAdded;
import com.clover.sdk.v3.remotepay.TipAdjustAuthResponse;
import com.clover.sdk.v3.remotepay.VaultCardResponse;
import com.clover.sdk.v3.remotepay.VerifySignatureRequest;
import com.clover.sdk.v3.remotepay.VoidPaymentRequest;
import com.clover.sdk.v3.remotepay.VoidPaymentResponse;

import java.io.ByteArrayOutputStream;
import java.util.UUID;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {
    private PaymentConnector paymentServiceConnector;
    private String TAG = "MainActivity";
    private AsyncTask waitingTask;
    private Account mAccount;
    private final int CUSTOMER_DATA = 0;
    private long l;
    private Button create_tend, charge;
    private String temp;
    private Payment p;
    private String cardnum, name;
    private OkHttpClient client;
    public void create_tender(View v){new AsyncTask<Void, Void, Void>(){
        private TenderConnector tenderConnector;
//        private Tender tender;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                tenderConnector = new TenderConnector(getBaseContext(), CloverAccount.getAccount(getBaseContext()), null);
                tenderConnector.checkAndCreateTender("PicPay", getPackageName(), true, false);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

    }.execute();
    };
    private void startPaymentConnector_voidPayment(Payment payment) {
        if (payment != null) {
            try {
                final VoidPaymentRequest request = new VoidPaymentRequest();
                request.setPaymentId(payment.getId());
                request.setOrderId(payment.getOrder().getId());
                request.setVoidReason(VoidReason.USER_CANCEL.toString());

                request.validate();
                Log.i(this.getClass().getSimpleName(), request.toString());
                if (this.paymentServiceConnector != null) {
                    if (this.paymentServiceConnector.isConnected()) {
                        this.paymentServiceConnector.getService().voidPayment(request);
                    } else {
                        Toast.makeText(getApplicationContext(), "not connected", Toast.LENGTH_LONG).show();
                        this.paymentServiceConnector.connect();
                        waitingTask = new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] params) {
                                try {
                                    MainActivity.this.paymentServiceConnector.getService().voidPayment(request);
                                } catch (RemoteException e) {
                                    Log.e(this.getClass().getSimpleName(), " void", e);
                                }
                                return null;
                            }
                        };
                    }
                }
            } catch (IllegalArgumentException e) {
                Log.e(this.getClass().getSimpleName(), " void", e);
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (RemoteException e) {
                Log.e(this.getClass().getSimpleName(), " void", e);
            }
        } else {
            Toast.makeText(getApplicationContext(), "All Good", Toast.LENGTH_LONG).show();
        }
    }

    final PaymentV3Connector.PaymentServiceListener paymentConnectorListener = new PaymentV3Connector.PaymentServiceListener() {
        @Override
        public void onPreAuthResponse(PreAuthResponse response) {

        }

        @Override
        public void onAuthResponse(AuthResponse response) {

        }

        @Override
        public void onTipAdjustAuthResponse(TipAdjustAuthResponse response) {

        }

        @Override
        public void onCapturePreAuthResponse(CapturePreAuthResponse response) {

        }

        @Override
        public void onVerifySignatureRequest(VerifySignatureRequest request) {

        }

        @Override
        public void onConfirmPaymentRequest(ConfirmPaymentRequest request) {

        }

        @Override
        public void onSaleResponse(SaleResponse response) {
            Log.d(TAG, "onSaleResponse: ");

        }

        @Override
        public void onManualRefundResponse(ManualRefundResponse response) {
            Log.d(TAG, "onManualRefundResponse: ");
        }

        @Override
        public void onRefundPaymentResponse(RefundPaymentResponse response) {

        }

        @Override
        public void onTipAdded(TipAdded tipadded) {

        }

        @Override
        public void onVoidPaymentResponse(VoidPaymentResponse response) {

        }

        @Override
        public void onVaultCardResponse(VaultCardResponse response) {
            Log.d(TAG, "onVaultCardResponse:");

            // insert your logic here about whether or not you'd want to actually authorize a payment
            SaleRequest saleRequest = new SaleRequest();
            saleRequest.setAmount(1000L);
            // you'll need to generate a random uuid here and set externalID

            saleRequest.setExternalId(UUID.randomUUID().toString());
            saleRequest.setVaultedCard(response.getCard());
            try {
                paymentServiceConnector.getService().sale(saleRequest);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onRetrievePendingPaymentsResponse(RetrievePendingPaymentsResponse retrievePendingPaymentResponse) {

        }

        @Override
        public void onReadCardDataResponse(ReadCardDataResponse response) {
            Log.d(TAG, "onReadCardDataResponse: ");

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        create_tend = (Button)findViewById(R.id.create_tend);
        charge = (Button)findViewById(R.id.charge);
        if(getIntent().hasExtra(Intents.EXTRA_AMOUNT)){
            charge.setVisibility(View.VISIBLE);
            create_tend.setVisibility(View.GONE);
            l = getIntent().getLongExtra(Intents.EXTRA_AMOUNT, 0);
        }
        else{
            charge.setVisibility(View.GONE);
            create_tend.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Retrieve the Clover account
        if (mAccount == null) {
            mAccount = CloverAccount.getAccount(this);

            if (mAccount == null) {
                return;
            }
        }

        connectToPaymentService();

    }

    private void connectToPaymentService() {
        if (this.paymentServiceConnector == null) {
            this.paymentServiceConnector = new PaymentConnector(MainActivity.this, mAccount,
                    new ServiceConnector.OnServiceConnectedListener() {
                        @Override
                        public void onServiceConnected(ServiceConnector connector) {
                            Log.d(this.getClass().getSimpleName(), "onServiceConnected " + connector);
                            MainActivity.this.paymentServiceConnector.addPaymentServiceListener(MainActivity.this.paymentConnectorListener);

                            AsyncTask tempWaitingTask = waitingTask;
                            waitingTask = null;

                            if (tempWaitingTask != null) {
                                tempWaitingTask.execute();
                            }
                        }

                        @Override
                        public void onServiceDisconnected(ServiceConnector connector) {
                            Log.d(this.getClass().getSimpleName(), "onServiceDisconnected " + connector);
                        }
                    }
            );
            this.paymentServiceConnector.connect();
        } else if (!this.paymentServiceConnector.isConnected()) {
            this.paymentServiceConnector.connect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && data != null){
            if(requestCode == CUSTOMER_DATA){

                p = (Payment) data.getParcelableExtra(Intents.EXTRA_PAYMENT);
                cardnum = p.getCardTransaction().getLast4();
                name = p.getCardTransaction().getCardholderName();
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 1);
            }
            if(requestCode == 1){
                Bitmap btmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                btmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                temp = Base64.encodeToString(byteArray, Base64.DEFAULT);
                //send to server
                PostTask task = new PostTask();
                task.execute();
            }
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }


    public class PostTask extends AsyncTask {
        private Exception exception;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String getResponse = post("https://api.chui.ai/v1/enroll", temp);
                return getResponse;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String getResponse) {
            System.out.println(getResponse);
        }

        private String post(String url, String bits) throws IOException {
            RequestBody body = new FormBody.Builder()
                    .add("img0", "123")
                    .add("img1", "123")
                    .add("img2", "123")
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .header("x-api-key", "vOjf0XRyf72QJzFOVxff7aKYtUeRBtgR6MXAMzPe")
                    .addHeader("Content-Type", "img/jpeg")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

    }

    public void executeVaultedCard(View view) {
        try {

            // vaultCard takes in a static int
            // number that represents the credit card
            //this intent pays
            Intent a = new Intent(Intents.ACTION_SECURE_PAY);
            a.putExtra(Intents.EXTRA_AMOUNT, l);

            // code of intent with intent
            startActivityForResult(a, CUSTOMER_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        this.paymentServiceConnector.disconnect();
    }
}
