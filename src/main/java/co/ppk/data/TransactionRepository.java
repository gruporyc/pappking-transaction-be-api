package co.ppk.data;

import co.ppk.domain.Billboard;
import co.ppk.domain.Transaction;
import co.ppk.dto.TransactionDto;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Clock;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TransactionRepository {

    private final DataSource ds;

    public TransactionRepository() {

        this.ds = DataSourceSingleton.getInstance();
    }

    public Optional<Transaction> getTransaction(String TransactionId) {
        QueryRunner run = new QueryRunner(ds);
        try {
            String query = "SELECT * FROM ppk_transactions.transactions WHERE id = '" + TransactionId + "';";
            Optional<Transaction> transaction = run.query(query,
                rs -> {
                    if (!rs.next()) {
                        Optional<Object> empty = Optional.empty();
                        return Optional.empty();
                    }
                    rs.last();




                    return Optional.ofNullable(new Transaction.Builder()
                            .setId(rs.getString(1))
                            .setPhone(rs.getString(2))
                            .setLicense_plate(rs.getString(3))
                            .setBillboards_code(rs.getString(4))
                            .setStart_date(rs.getString(5))
                            .setStart_time(rs.getString(6))
                            .setEnd_date(rs.getString(7))
                            .setEnd_time(rs.getString(8))
                            .setTime(rs.getString(9))
                            .setPrice(rs.getString(10))
                            .setClosed(rs.getString(11))
                            .setCreateDate(rs.getString(12))
                            .setUpdateDate(rs.getString(13))
                            .build());
                });
            return transaction;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //BUSCAR TODAS LAS TRANSACCIONES -->NO USES ESTO JEJE
    public List<Transaction> getTransaction() {
        QueryRunner run = new QueryRunner(ds);
        List<Transaction> transactions = new LinkedList<>();
        try {
            String query = "SELECT * FROM ppk_transactions.transactions;";
            List<Transaction> transactionList = run.query(query,
                    rs -> {
                        while (rs.next()) {
                            transactions.add(new Transaction.Builder()
                                    .setId(rs.getString(1))
                                    .setPhone(rs.getString(2))
                                    .setLicense_plate(rs.getString(3))
                                    .setBillboards_code(rs.getString(4))
                                    .setStart_date(rs.getString(5))
                                    .setStart_time(rs.getString(6))
                                    .setEnd_date(rs.getString(7))
                                    .setEnd_time(rs.getString(8))
                                    .setTime(rs.getString(9))
                                    .setPrice(rs.getString(10))
                                    .setClosed(rs.getString(11))
                                    .setCreateDate(rs.getString(12))
                                    .setUpdateDate(rs.getString(13))
                                    .build());
                        }
                        return transactions;
                    });
            return transactionList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //INSERTAR UNA TRANSACCION CONFIRMADA
    public String setConfirmedInitTransactionByFacePlate(TransactionDto transaction) {
        QueryRunner run = new QueryRunner(ds);
//        Timestamp now = Timestamp.from(Instant.now());

        String transactionId = UUID.randomUUID().toString();
        try {
            Connection conn = ds.getConnection();
            conn.setAutoCommit(false);
            try {

                String insert = "INSERT INTO ppk_transactions.transactions " +
                        "(id, " +
                        "phone, " +
                        "license_plate, " +
                        "billboard_code, " +
                        "start_date, " +
                        "start_time, " +
                        "end_date, " +
                        "end_time, " +
                        "time, " +
                        "price, " +
                        "closed) " +
                        "VALUES " +
                        "('" + transactionId + "', " +
                        "'" + transaction.getPhone() + "', " +
                        "'" + transaction.getLicense_plate() + "', " +
                        "'" + transaction.getBillboards_code() + "', " +
                        "'" + transaction.getStart_date() + "', " +
                        "'" + transaction.getStart_time() + "', " +
                        "'" + transaction.getEnd_date() + "', " +
                        "'" + transaction.getEnd_time() + "', " +
                        "'" + transaction.getTime() + "', " +
                        "'" + transaction.getPrice() + "', " +
                        "'" + transaction.getClosed() + "');";
                run.insert(conn, insert, new ScalarHandler<>());
                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            } finally {
                DbUtils.close(conn);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return transactionId;
    }

    //Verificar si una transaccion existe sin importar el status
    public Optional<Transaction> getTransactionByFacePlate(String facePlate) {
        QueryRunner run = new QueryRunner(ds);
        try {
            String query = "SELECT * FROM ppk_transactions.transactions WHERE license_plate = '" + facePlate + "';";
            Optional<Transaction> Transaction = run.query(query,
                    rs -> {
                        if (!rs.next()) {
                            Optional<Object> empty = Optional.empty();
                            return Optional.empty();
                        }
                        rs.last();
                        return Optional.ofNullable(new Transaction.Builder()
                                .setId(rs.getString(1))
                                .setPhone(rs.getString(2))
                                .setLicense_plate(rs.getString(3))
                                .setBillboards_code(rs.getString(4))
                                .setStart_date(rs.getString(5))
                                .setStart_time(rs.getString(6))
                                .setEnd_date(rs.getString(7))
                                .setEnd_time(rs.getString(8))
                                .setTime(rs.getString(9))
                                .setPrice(rs.getString(10))
                                .setClosed(rs.getString(11))
                                .setCreateDate(rs.getString(12))
                                .setUpdateDate(rs.getString(13))
                                .build());
                    });
            return Transaction;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //VERIFICAR SI LA TRANSACCION ESTA PENDIENTE POR CONFIRMACION DE FINALIZACION --> esto es despues del EndTransaction
    public Optional<Transaction> getConfirmedTransactionByFacePlate(String facePlate) {
        QueryRunner run = new QueryRunner(ds);
        try {
            String query = "SELECT * FROM ppk_transactions.transactions WHERE license_plate = '" + facePlate + "'" +
                    " AND closed in ('N','P');";
            Optional<Transaction> Transaction = run.query(query,
                    rs -> {
                        if (!rs.next()) {
                            Optional<Object> empty = Optional.empty();
                            return Optional.empty();
                        }
                        rs.last();
                        return Optional.ofNullable(new Transaction.Builder()
                                .setId(rs.getString(1))
                                .setPhone(rs.getString(2))
                                .setLicense_plate(rs.getString(3))
                                .setBillboards_code(rs.getString(4))
                                .setStart_date(rs.getString(5))
                                .setStart_time(rs.getString(6))
                                .setEnd_date(rs.getString(7))
                                .setEnd_time(rs.getString(8))
                                .setTime(rs.getString(9))
                                .setPrice(rs.getString(10))
                                .setClosed(rs.getString(11))
                                .setCreateDate(rs.getString(12))
                                .setUpdateDate(rs.getString(13))
                                .build());
                    });
            return Transaction;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //VERIFICAR SI UNA TRANSACCION YA ESTA COMPLETADA
    public Optional<Transaction> getCompleteTransactionByFacePlate(String facePlate) {
        QueryRunner run = new QueryRunner(ds);
        try {
            String query = "SELECT * FROM ppk_transactions.transactions WHERE license_plate = '" + facePlate + "'" +
                    " AND closed = 'S';";
            Optional<Transaction> Transaction = run.query(query,
                    rs -> {
                        if (!rs.next()) {
                            Optional<Object> empty = Optional.empty();
                            return Optional.empty();
                        }
                        rs.last();
                        return Optional.ofNullable(new Transaction.Builder()
                                .setId(rs.getString(1))
                                .setPhone(rs.getString(2))
                                .setLicense_plate(rs.getString(3))
                                .setBillboards_code(rs.getString(4))
                                .setStart_date(rs.getString(5))
                                .setStart_time(rs.getString(6))
                                .setEnd_date(rs.getString(7))
                                .setEnd_time(rs.getString(8))
                                .setTime(rs.getString(9))
                                .setPrice(rs.getString(10))
                                .setClosed(rs.getString(11))
                                .setCreateDate(rs.getString(12))
                                .setUpdateDate(rs.getString(13))
                                .build());
                    });
            return Transaction;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void  updateTransaction(Transaction transaction) {
        QueryRunner run = new QueryRunner(ds);
//        Timestamp now = Timestamp.from(Instant.now());
        try {
            Connection conn = ds.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            try {

                String update = "update ppk_transactions.transactions set " +
                        "id = '" +  transaction.getId() + "', " +
                        "phone = '" + transaction.getPhone() + "', " +
                        "license_plate = '" + transaction.getLicense_plate() + "', " +
                        "billboard_code = '" + transaction.getBillboards_code() + "', " +
                        "start_date = '" + transaction.getStart_date() + "', " +
                        "start_time = '" + transaction.getStart_time() + "', " +
                        "end_date = '" + transaction.getEnd_date() + "', " +
                        "end_time = '" + transaction.getEnd_time() + "', " +
                        "time = '" + transaction.getTime() + "', " +
                        "price = '" + transaction.getPrice() + "', " +
                        "closed = '" + transaction.getClosed() + "' " +
                        " WHERE  " +
                        " id = '" + transaction.getId() +"' ";

                stmt.executeUpdate(update);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            } finally {
                DbUtils.close(conn);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    public String putEndTransactionById(String id) {
        QueryRunner run = new QueryRunner(ds);
//        Timestamp now = Timestamp.from(Instant.now());
        try {
            Connection conn = ds.getConnection();
            conn.setAutoCommit(false);
            try {

                String update = "update ppk_transactions.transactions set " +
                        "closed = 'P' where id = '"+id+";";
                run.update(conn, update, new ScalarHandler<>());
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            } finally {
                DbUtils.close(conn);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return id;
    }




}
