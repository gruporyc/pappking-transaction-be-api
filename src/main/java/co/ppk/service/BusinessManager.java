package co.ppk.service;

import co.ppk.dto.BillboardDto;
import co.ppk.dto.RateDto;
import co.ppk.dto.TemporalTransactionDto;
import co.ppk.dto.TransactionDto;
import co.ppk.utilities.PersonalExcepcion;

public interface BusinessManager {

    TransactionDto findTransactionByFacePlate(String facePlate);

    TemporalTransactionDto getInitTransactionByFacePlate(String facePlate);

    BillboardDto getBillboardByCode (String code);

    BillboardDto getBillboardById (String id);

    String setTemporalTransaction(TemporalTransactionDto transactionT) throws PersonalExcepcion;

    String setConfirmedInitTransactionByFacePlate(TransactionDto transaction);

    String setAutorizationInitTransactionByFacePlate(TransactionDto transaction);

    TransactionDto getConfirmedTransactionByFacePlate (String facePlate);

    TemporalTransactionDto getEndTransactionByFacePlate (String facePlate);

    String createBillboard(BillboardDto billboard);

    String createRate(RateDto rate);

    RateDto getRate();

    String putEndTransactionById(String id);

    void updateTransaction(TransactionDto transaction);

    void updateBillboard(BillboardDto billboard);

    void deleteTemporalTransaction (String temporalTransactionId);

    void deleteBillboard(String billboardId);
}
