package co.ppk.service.impl;

import co.ppk.data.BillboardRepository;
import co.ppk.data.RateRepository;
import co.ppk.data.TemporalTransactionRepository;
import co.ppk.domain.Billboard;
import co.ppk.domain.Transaction;
import co.ppk.domain.TemporalTransaction;
import co.ppk.domain.Rate;
import co.ppk.dto.BillboardDto;
import co.ppk.dto.RateDto;
import co.ppk.dto.TemporalTransactionDto;
import co.ppk.dto.TransactionDto;
import co.ppk.service.BusinessManager;
import co.ppk.data.TransactionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;
import java.util.Optional;

import static co.ppk.utilities.Constants.END_TRANSACTION_ALREADY_EXISTS;
import static co.ppk.utilities.Constants.INIT_TRANSACTION_ALREADY_EXISTS;


@Component
public class BussinessManagerImpl implements BusinessManager{

    private static TransactionRepository transactionRepository;
    private static TemporalTransactionRepository temporalTransactionRepository;
    private static BillboardRepository billboardRepository;
    private static RateRepository rateRepository;

    private final static Logger LOGGER = LogManager.getLogger(BussinessManagerImpl.class);



    public BussinessManagerImpl() {
        temporalTransactionRepository = new TemporalTransactionRepository();
        transactionRepository = new TransactionRepository();
        billboardRepository = new BillboardRepository();
        rateRepository = new RateRepository();
    }

    @Override
    public TransactionDto findTransactionByFacePlate(String facePlate) {
        Optional<Transaction> transaction = transactionRepository.getTransactionByFacePlate(facePlate);
        if (!transaction.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        TransactionDto response = new TransactionDto();
        response.setPhone(transaction.get().getPhone());
        response.setBillboards_code(transaction.get().getBillboards_code());
        response.setLicense_plate(transaction.get().getLicense_plate());
        return response;
    }

    @Override
    public TemporalTransactionDto getInitTransactionByFacePlate(String facePlate) {
        Optional<TemporalTransaction> temporalTransaction = temporalTransactionRepository.getInitTransactionByFacePlate(facePlate);
        TemporalTransactionDto response = new TemporalTransactionDto();
        if (!temporalTransaction.isPresent()) {
            return response;
        }else{
            response.setId(temporalTransaction.get().getId());
            response.setPhone(temporalTransaction.get().getPhone());
            response.setBillboards_code(temporalTransaction.get().getBillboards_code());
            response.setLicense_plate(temporalTransaction.get().getLicense_plate());
            response.setDate(temporalTransaction.get().getDate());
            response.setHour(temporalTransaction.get().getHour());
            response.setTime(temporalTransaction.get().getTime());
            response.setPrice(temporalTransaction.get().getPrice());
            response.setAction(temporalTransaction.get().getAction());
            return response;
        }
    }


    @Override
    public BillboardDto getBillboardByCode(String code) {
        Optional<Billboard> billboard = billboardRepository.getBillboardByCode(code);
        BillboardDto response = new BillboardDto();
        if (!billboard.isPresent()) {
            return response;
        }
        response.setId(billboard.get().getId());
        response.setCode(billboard.get().getCode());
        response.setAddress(billboard.get().getAddress());

        return response;
    }

    @Override
    public BillboardDto getBillboardById(String id) {
        Optional<Billboard> billboard = billboardRepository.getBillboardById(id);
        BillboardDto response = new BillboardDto();
        if (!billboard.isPresent()) {
            return response;
        }
        response.setId(billboard.get().getId());
        response.setCode(billboard.get().getCode());
        response.setAddress(billboard.get().getAddress());

        return response;
    }

    @Override
    public String setTemporalTransaction(TemporalTransactionDto temporalTransaction) {
        if(temporalTransactionRepository.getInitTransactionByFacePlate(temporalTransaction.getLicense_plate()).isPresent()) {
            return INIT_TRANSACTION_ALREADY_EXISTS;
        }
        if(temporalTransactionRepository.getEndTransactionByFacePlate(temporalTransaction.getLicense_plate()).isPresent()) {
            return END_TRANSACTION_ALREADY_EXISTS;
        }
        temporalTransactionRepository.setTemporalTransaction(temporalTransaction);
        return "S";
    }

    @Override
    public String setConfirmedInitTransactionByFacePlate(TransactionDto transaction) {
        if(transactionRepository.getConfirmedTransactionByFacePlate(transaction.getLicense_plate()).isPresent()) {
            return "No Existe";
        }
        return transactionRepository.setConfirmedInitTransactionByFacePlate(transaction);
    }

    @Override
    public String setAutorizationInitTransactionByFacePlate(TransactionDto transaction) {
        if(transactionRepository.getTransactionByFacePlate(transaction.getLicense_plate()).isPresent()) {
            return "Ya la transaccion de inicio EXISTE";
        }
        return transactionRepository.setConfirmedInitTransactionByFacePlate(transaction);
    }

    @Override
    public TransactionDto getConfirmedTransactionByFacePlate(String facePlate) {
        Optional<Transaction> transaction = transactionRepository.getConfirmedTransactionByFacePlate(facePlate);
        TransactionDto response = new TransactionDto();
        if (!transaction.isPresent())
        {
            return response;
        }
        //TODO: Replace this code for mapper approach
        response.setId(transaction.get().getId());
        response.setPhone(transaction.get().getPhone());
        response.setLicense_plate(transaction.get().getLicense_plate());
        response.setBillboards_code(transaction.get().getBillboards_code());
        response.setStart_date(transaction.get().getStart_date());
        response.setStart_time(transaction.get().getStart_time());
        response.setEnd_date(transaction.get().getEnd_date());
        response.setEnd_time(transaction.get().getEnd_time());
        response.setTime(transaction.get().getTime());
        response.setPrice(transaction.get().getPrice());
        response.setClosed(transaction.get().getClosed());
        return response;
    }

    @Override
    public TemporalTransactionDto getEndTransactionByFacePlate(String facePlate) {
        Optional<TemporalTransaction> temporalTransaction = temporalTransactionRepository.getEndTransactionByFacePlate(facePlate);
        TemporalTransactionDto response = new TemporalTransactionDto();
        if (!temporalTransaction.isPresent()) {
            return response;
        }else{
            response.setId(temporalTransaction.get().getId());
            response.setPhone(temporalTransaction.get().getPhone());
            response.setBillboards_code(temporalTransaction.get().getBillboards_code());
            response.setLicense_plate(temporalTransaction.get().getLicense_plate());
            response.setDate(temporalTransaction.get().getDate());
            response.setHour(temporalTransaction.get().getHour());
            response.setTime(temporalTransaction.get().getTime());
            response.setPrice(temporalTransaction.get().getPrice());
            response.setAction(temporalTransaction.get().getAction());
            return response;
        }
    }

    @Override
    public String createBillboard(BillboardDto billboard) {
        if(billboardRepository.getBillboardByCode(billboard.getCode()).isPresent()) {
            return "Ya Existe";
        }
        return billboardRepository.createBillboard(billboard);
    }

    @Override
    public String createRate(RateDto rate) {
        if(rateRepository.getRate().isPresent()) {
            return "Ya Existe";
        }
        return rateRepository.createRate(rate);
    }

    @Override
    public RateDto getRate() {
        Optional<Rate> rate = rateRepository.getRate();
        RateDto response = new RateDto();
        if (!rate.isPresent()) {
            return response;
        }
        response.setId(rate.get().getId());
        response.setDate(rate.get().getDate());
        response.setStatus(rate.get().getStatus());
        response.setValue(rate.get().getValue());
        return response;
    }

    @Override
    public String putEndTransactionById(String id) {
        return transactionRepository.putEndTransactionById(id);
    }

    @Override
    public void updateTransaction(TransactionDto transaction) {
        Optional<Transaction> currentTransaction = transactionRepository.getTransaction(transaction.getId());
        if(!currentTransaction.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        transactionRepository.updateTransaction(new Transaction.Builder()
                .setId(transaction.getId())
                .setPhone((Objects.isNull(transaction.getPhone()) || transaction.getPhone().isEmpty()) ? currentTransaction.get().getPhone() : transaction.getPhone())
                .setLicense_plate((Objects.isNull(transaction.getLicense_plate()) || transaction.getLicense_plate().isEmpty()) ? currentTransaction.get().getLicense_plate() : transaction.getLicense_plate())
                .setBillboards_code((Objects.isNull(transaction.getBillboards_code()) || transaction.getBillboards_code().isEmpty()) ? currentTransaction.get().getBillboards_code() : transaction.getBillboards_code())
                .setStart_date((Objects.isNull(transaction.getStart_date()) || transaction.getStart_date().isEmpty()) ? currentTransaction.get().getStart_date() : transaction.getStart_date())
                .setStart_time((Objects.isNull(transaction.getStart_time()) || transaction.getStart_time().isEmpty()) ? currentTransaction.get().getStart_time() : transaction.getStart_time())
                .setEnd_date((Objects.isNull(transaction.getEnd_date()) || transaction.getEnd_date().isEmpty()) ? currentTransaction.get().getEnd_date() : transaction.getEnd_date())
                .setEnd_time((Objects.isNull(transaction.getEnd_time()) || transaction.getEnd_time().isEmpty()) ? currentTransaction.get().getEnd_time() : transaction.getEnd_time())
                .setTime((Objects.isNull(transaction.getTime()) || transaction.getTime().isEmpty()) ? currentTransaction.get().getTime() : transaction.getTime())
                .setPrice((Objects.isNull(transaction.getPrice()) || transaction.getPrice().isEmpty()) ? currentTransaction.get().getPrice() : transaction.getPrice())
                .setClosed((Objects.isNull(transaction.getClosed()) || transaction.getClosed().isEmpty()) ? currentTransaction.get().getClosed() : transaction.getClosed())
                .build());
    }


    @Override
    public void updateBillboard(BillboardDto billboard) {
        Optional<Billboard> currentBillboard = billboardRepository.getBillboardById(billboard.getId());
        if(!currentBillboard.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        billboardRepository.updateBillboard(new Billboard.Builder()
                .setId(billboard.getId())
                .setCode((Objects.isNull(billboard.getCode()) || billboard.getCode().isEmpty()) ? currentBillboard.get().getCode() : billboard.getCode())
                .setAddress((Objects.isNull(billboard.getAddress()) || billboard.getAddress().isEmpty()) ? currentBillboard.get().getAddress() : billboard.getAddress())
                .build());
    }

    @Override
    public void deleteBillboard(String billboardId) {
        billboardRepository.deleteBillboard(billboardId);
    }

    @Override
    public void deleteTemporalTransaction(String temporalTransactionId) {
        temporalTransactionRepository.deleteTemporalTransaction(temporalTransactionId);
    }

}
