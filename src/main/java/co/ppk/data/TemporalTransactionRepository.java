package co.ppk.data;

import co.ppk.domain.TemporalTransaction;
import co.ppk.dto.TemporalTransactionDto;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TemporalTransactionRepository {

    private final DataSource ds;

    public TemporalTransactionRepository() {

        this.ds = DataSourceSingleton.getInstance();
    }

    public Optional<TemporalTransaction> getTransactionT(String TransactionId) {
        QueryRunner run = new QueryRunner(ds);
        try {
            String query = "SELECT * FROM ppk_transactions.temporal_transaction WHERE id = '" + TransactionId + "';";
            Optional<TemporalTransaction> transactionT = run.query(query,
                rs -> {
                    if (!rs.next()) {
                        Optional<Object> empty = Optional.empty();
                        return Optional.empty();
                    }
                    rs.last();




                    return Optional.ofNullable(new TemporalTransaction.Builder()
                            .setId(rs.getString(1))
                            .setPhone(rs.getString(2))
                            .setLicense_plate(rs.getString(3))
                            .setBillboards_code(rs.getString(4))
                            .setDate(rs.getString(5))
                            .setHour(rs.getString(6))
                            .setTime(rs.getString(7))
                            .setPrice(rs.getString(8))
                            .setAction(rs.getString(9))
                            .setCreateDate(rs.getString(10))
                            .setUpdateDate(rs.getString(11))
                            .build());
                });
            return transactionT;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TemporalTransaction> getTransactionT() {
        QueryRunner run = new QueryRunner(ds);
        List<TemporalTransaction> transactionsT = new LinkedList<>();
        try {
            String query = "SELECT * FROM transactions.temporal_transactions;";
            List<TemporalTransaction> temporalTransactionList = run.query(query,
                    rs -> {
                        while (rs.next()) {
                            transactionsT.add(new TemporalTransaction.Builder()
                                    .setId(rs.getString(1))
                                    .setPhone(rs.getString(2))
                                    .setLicense_plate(rs.getString(3))
                                    .setBillboards_code(rs.getString(4))
                                    .setDate(rs.getString(5))
                                    .setHour(rs.getString(6))
                                    .setTime(rs.getString(7))
                                    .setPrice(rs.getString(8))
                                    .setAction(rs.getString(9))
                                    .setCreateDate(rs.getString(10))
                                    .setUpdateDate(rs.getString(11))
                                    .build());
                        }
                        return transactionsT;
                    });
            return temporalTransactionList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String setTemporalTransaction(TemporalTransactionDto temporalTransaction) {
        QueryRunner run = new QueryRunner(ds);
//        Timestamp now = Timestamp.from(Instant.now());

        String transactionId = UUID.randomUUID().toString();
        try {
            Connection conn = ds.getConnection();
            conn.setAutoCommit(false);
            try {

                String insert = "INSERT INTO ppk_transactions.temporal_transactions " +
                        "(id, " +
                        "phone, " +
                        "license_plate, " +
                        "billboard_code, " +
                        "date, " +
                        "hour, " +
                        "time, " +
                        "price, " +
                        "action) " +
                        "VALUES " +
                        "('" + transactionId + "', " +
                        "'" + temporalTransaction.getPhone() + "', " +
                        "'" + temporalTransaction.getLicense_plate() + "', " +
                        "'" + temporalTransaction.getBillboard_code() + "', " +
                        "'" + temporalTransaction.getDate() + "', " +
                        "'" + temporalTransaction.getHour() + "', " +
                        "'" + temporalTransaction.getTime() + "', " +
                        "'" + temporalTransaction.getPrice() + "', " +
                        "'" + temporalTransaction.getAction() + "');";
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



    public Optional<TemporalTransaction> getInitTransactionByFacePlate(String facePlate) {
        QueryRunner run = new QueryRunner(ds);
        try {
            String query = "SELECT * FROM ppk_transactions.temporal_transactions WHERE license_plate = '" + facePlate +  "' AND " +
                    "action = 'I';";
            Optional<TemporalTransaction> temporalTransaction = run.query(query,
                    rs -> {
                        if (!rs.next()) {
                            Optional<Object> empty = Optional.empty();
                            return Optional.empty();
                        }
                        rs.last();

                        return Optional.ofNullable(new TemporalTransaction.Builder()
                                .setId(rs.getString(1))
                                .setPhone(rs.getString(2))
                                .setLicense_plate(rs.getString(3))
                                .setBillboards_code(rs.getString(4))
                                .setDate(rs.getString(5))
                                .setHour(rs.getString(6))
                                .setTime(rs.getString(7))
                                .setPrice(rs.getString(8))
                                .setAction(rs.getString(9))
                                .setCreateDate(rs.getString(10))
                                .setUpdateDate(rs.getString(11))
                                .build());
                    });

            return temporalTransaction;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<TemporalTransaction> getEndTransactionByFacePlate(String facePlate) {
        QueryRunner run = new QueryRunner(ds);
        try {
            String query = "SELECT * FROM ppk_transactions.temporal_transactions WHERE license_plate = '" + facePlate +  "' AND " +
                    "action = 'F';";
            Optional<TemporalTransaction> temporalTransaction = run.query(query,
                    rs -> {
                        if (!rs.next()) {
                            Optional<Object> empty = Optional.empty();
                            return Optional.empty();
                        }
                        rs.last();

                        return Optional.ofNullable(new TemporalTransaction.Builder()
                                .setId(rs.getString(1))
                                .setPhone(rs.getString(2))
                                .setLicense_plate(rs.getString(3))
                                .setBillboards_code(rs.getString(4))
                                .setDate(rs.getString(5))
                                .setHour(rs.getString(6))
                                .setTime(rs.getString(7))
                                .setPrice(rs.getString(8))
                                .setAction(rs.getString(9))
                                .setCreateDate(rs.getString(10))
                                .setUpdateDate(rs.getString(11))
                                .build());
                    });

            return temporalTransaction;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteTemporalTransaction(String temporalTransactionId) {
        try {
            Connection conn = ds.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            try {
                String delete = "DELETE FROM ppk_transactions.temporal_transactions " +
                        "WHERE " +
                        "id = '" + temporalTransactionId + "';";
                stmt.execute(delete);
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

}
