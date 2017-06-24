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

import com.clover.connector.sdk.v3.PaymentConnector;
import com.clover.connector.sdk.v3.PaymentV3Connector;
import com.clover.sdk.internal.util.BitmapUtils;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v1.ServiceConnector;
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
import com.clover.sdk.v3.remotepay.VoidPaymentResponse;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private PaymentConnector paymentServiceConnector;
    private String TAG = "MainActivity";
    private AsyncTask waitingTask;
    private Account mAccount;
    ImageView imgview;

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
        //EDMT Dev video
        Button btnCamera = (Button) findViewById(R.id.camerabtn);
        imgview = (ImageView) findViewById(R.id.imageview);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap btmp = (Bitmap) data.getExtras().get("data");

        imgview.setImageBitmap(btmp);
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

    public void executeVaultedCard(View view) {
        try {
            this.paymentServiceConnector.getService().vaultCard(Intents.CARD_ENTRY_METHOD_ALL);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
