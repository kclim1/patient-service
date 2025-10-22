package com.patientmanagement.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(BillingRequest billingRequest , StreamObserver<BillingResponse> responseObserver){
        log.info("create billing account request received {} " , billingRequest.toString());
        //define business logic here - e.g save to database , perform calculations etc
        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId(billingRequest.getPatientId())
                .setStatus("ACTIVE")
                .build();
//        used to send a response to the client from the grpc service
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
