package com.example.appfoododer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.example.appfoododer.model.Restaurant;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class OrderSucceessActivity extends AppCompatActivity {

    BillingClient billingClient;
    List<ProductDetails> productDetails;
    ProductDetails prD;
    PurchasesUpdatedListener purchasesUpdatedListener;
    Purchase purchase;
    BillingResult billingResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_succeess);

        Restaurant restaurantModel = getIntent().getParcelableExtra("RestaurantModel");
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle(restaurantModel.getName());
//        actionBar.setSubtitle(restaurantModel.getAddress());
//        actionBar.setDisplayHomeAsUpEnabled(false);


        TextView buttonDone = findViewById(R.id.buttonDone);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null){
                    for (Purchase purchase : list){
                        if(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()){
                            handlePurchase(purchase);
                        }
                    }
                }
            }
        };

        billingClient = BillingClient.newBuilder(OrderSucceessActivity.this)
                        .setListener(purchasesUpdatedListener)
                                .enablePendingPurchases()
                                        .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected(){ connectGoogle();} {

            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                    getProductDetails();
                }
            }
        });

        connectGoogle();
        getProductDetails();
    }

    private void connectGoogle(){

    }

    private  void getProductDetails() {
        QueryProductDetailsParams queryProductDetailsParams = QueryProductDetailsParams.newBuilder().setProductList(
                ImmutableList.of(QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("id_product_inapp5")
                        .setProductType(BillingClient.ProductType.INAPP).build())
        ).build();

        billingClient.queryProductDetailsAsync(
                queryProductDetailsParams, new ProductDetailsResponseListener() {
                    @Override
                    public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> list) {
                        Log.d("xxx", "yyy");
                        if (!list.isEmpty()) {
                            TextView title = findViewById(R.id.tv1);
                            TextView price = findViewById(R.id.tv2);
                            Button btn = findViewById(R.id.btn_pay);
                            prD = list.get(0);
                            title.setText(prD.getName());
                            price.setText((prD.getOneTimePurchaseOfferDetails().getFormattedPrice()));
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                                            ImmutableList.of(
                                                    BillingFlowParams.ProductDetailsParams.newBuilder()
                                                            .setProductDetails(prD)
                                                            .build()
                                            );
                                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                            .setProductDetailsParamsList(productDetailsParamsList)
                                            .build();

                                    billingClient.launchBillingFlow(OrderSucceessActivity.this, billingFlowParams);
                                }
                            });
                        }
                    }
                }
        );
    }

    void handlePurchase(Purchase purchase){
        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        ConsumeResponseListener listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String purchaseToken) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){

                }
            }
        };
        billingClient.consumeAsync(consumeParams, listener);

    }



}