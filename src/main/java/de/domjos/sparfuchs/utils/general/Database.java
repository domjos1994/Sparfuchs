package de.domjos.sparfuchs.utils.general;

import de.domjos.sparfuchs.model.account.Account;
import de.domjos.sparfuchs.model.account.BankDetail;
import de.domjos.sparfuchs.model.account.StandingOrder;
import de.domjos.sparfuchs.model.account.Transaction;
import de.domjos.sparfuchs.model.general.Category;
import de.domjos.sparfuchs.model.general.DatabaseObject;
import de.domjos.sparfuchs.model.general.Tag;
import de.domjos.sparfuchs.model.person.Person;
import de.domjos.sparfuchs.utils.helper.Helper;
import javafx.scene.image.Image;

import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class Database {
    private final Connection connection;
    private Statement statement;

    public Database() throws Exception {
        // init driver
        Class.forName("org.sqlite.JDBC");

        // get connection
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + Helper.initializePath() + "/sparfuchs.db");
    }

    public void init() throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Database.class.getResourceAsStream("/sql/init.sql")));

        String line;
        StringBuilder query = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            query.append(line);
            if(line.contains(";")) {
                this.executeUpdate(query.toString());
                query = new StringBuilder();
            }
        }
    }

    public int insertOrUpdateCategory(Category category) throws Exception {
        PreparedStatement preparedStatement = this.getPreparedStatement(Arrays.asList("title", "description"), category);
        preparedStatement.setString(1, category.title.get());
        preparedStatement.setString(2, category.description.get());
        return this.execute(preparedStatement, category);
    }

    public List<Category> getCategories(String where) throws Exception {
        List<Category> categories = new LinkedList<>();
        ResultSet resultSet = this.getQuery(Category.class, where);
        while (resultSet.next()) {
            Category category = new Category();
            category.ID.set(resultSet.getInt("ID"));
            category.title.set(resultSet.getString("title"));
            category.description.set(resultSet.getString("description"));
            categories.add(category);
        }
        this.closeStatement(resultSet);
        return categories;
    }

    public int insertOrUpdateTag(Tag tag) throws Exception {
        List<Tag> tags = this.getTags("title='" + tag.title.get() + "'");
        if(!tags.isEmpty()) {
            tag.ID.set(tags.get(0).ID.get());
        }
        PreparedStatement preparedStatement = this.getPreparedStatement(Collections.singletonList("title"), tag);
        preparedStatement.setString(1, tag.title.get());
        return this.execute(preparedStatement, tag);
    }

    public List<Tag> getTags(String where) throws Exception {
        List<Tag> tags = new LinkedList<>();
        ResultSet resultSet = this.getQuery(Tag.class, where);
        while (resultSet.next()) {
            Tag tag = new Tag();
            tag.ID.set(resultSet.getInt("ID"));
            tag.title.set(resultSet.getString("title"));
            tags.add(tag);
        }
        this.closeStatement(resultSet);
        return tags;
    }

    public int insertOrUpdateBankDetails(BankDetail bankDetail) throws Exception {
        if(!bankDetail.bic.get().trim().isEmpty() || !bankDetail.iBan.get().trim().isEmpty()) {
            PreparedStatement preparedStatement = this.getPreparedStatement(Arrays.asList("title", "bic", "iban"), bankDetail);
            preparedStatement.setString(1, bankDetail.title.get());
            preparedStatement.setString(2, bankDetail.bic.get());
            preparedStatement.setString(3, bankDetail.iBan.get());
            return this.execute(preparedStatement, bankDetail);
        }
        return 0;
    }

    public List<BankDetail> getBankDetails(String where) throws Exception {
        List<BankDetail> bankDetails = new LinkedList<>();
        ResultSet resultSet = this.getQuery(BankDetail.class, where);
        while (resultSet.next()) {
            BankDetail bankDetail = new BankDetail();
            bankDetail.ID.set(resultSet.getInt("ID"));
            bankDetail.title.set(resultSet.getString("title"));
            bankDetail.bic.set(resultSet.getString("bic"));
            bankDetail.iBan.set(resultSet.getString("iban"));
            bankDetails.add(bankDetail);
        }
        this.closeStatement(resultSet);
        return bankDetails;
    }

    public int insertOrUpdatePerson(Person person) throws Exception {
        PreparedStatement preparedStatement = this.getPreparedStatement(Arrays.asList("title", "firstName", "lastName", "birthDate", "profilePic"), person);
        preparedStatement.setString(1, person.title.get());
        preparedStatement.setString(2, person.firstName.get());
        preparedStatement.setString(3, person.lastName.get());
        if(person.birthDate.get()!=null) {
            preparedStatement.setDate(4, Date.valueOf(person.birthDate.get()));
        } else {
            preparedStatement.setNull(4, Types.DATE);
        }
        if(person.profileImage.get()!=null) {
            preparedStatement.setBytes(5, Converter.convertImageToByteArray(person.profileImage.get()));
        } else {
            preparedStatement.setNull(5, Types.BLOB);
        }
        return this.execute(preparedStatement, person);
    }

    public List<Person> getPersons(String where) throws Exception {
        List<Person> persons = new LinkedList<>();
        ResultSet resultSet = this.getQuery(Person.class, where);
        while (resultSet.next()) {
            Person person = new Person();
            person.ID.set(resultSet.getInt("ID"));
            person.title.set(resultSet.getString("title"));
            person.firstName.set(resultSet.getString("firstName"));
            person.lastName.set(resultSet.getString("lastName"));
            Date dt = resultSet.getDate("birthDate");
            if(dt!=null) {
                person.birthDate.set(dt.toLocalDate());
            }
            byte[] image = resultSet.getBytes("profilePic");
            if(image!=null) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image);
                person.profileImage.set(new Image(byteArrayInputStream));
                byteArrayInputStream.close();
            }
            person.accounts.addAll(this.getAccounts("person=" + person.ID.get()));
            persons.add(person);
        }
        this.closeStatement(resultSet);
        return persons;
    }

    public int insertOrUpdateAccount(Account account, Person person) throws Exception {
        PreparedStatement preparedStatement = this.getPreparedStatement(Arrays.asList("title", "description", "isCash", "start", "bankDetail", "person"), account);
        preparedStatement.setString(1, account.title.get());
        preparedStatement.setString(2, account.description.get());
        preparedStatement.setBoolean(3, account.isCash.get());
        preparedStatement.setDouble(4, account.startAmount.get());
        if(account.bankDetail.get()!=null) {
            preparedStatement.setInt(5, this.insertOrUpdateBankDetails(account.bankDetail.get()));
        } else {
            preparedStatement.setNull(5, Types.INTEGER);
        }
        preparedStatement.setInt(6, person.ID.get());
        return this.execute(preparedStatement, account);
    }

    public List<Account> getAccounts(String where) throws Exception {
        List<Account> accounts = new LinkedList<>();
        ResultSet resultSet = this.getQuery(Account.class, where);
        while (resultSet.next()) {
            Account account = new Account();
            account.ID.set(resultSet.getInt("ID"));
            account.title.set(resultSet.getString("title"));
            account.description.set(resultSet.getString("description"));
            account.isCash.set(resultSet.getBoolean("isCash"));
            account.startAmount.set(resultSet.getDouble("start"));
            int bankDetail = resultSet.getInt("bankDetail");
            if(bankDetail!=0) {
                account.bankDetail.set(this.getBankDetails("ID=" + bankDetail).get(0));
            }
            account.transactions.addAll(this.getTransactions("account=" + account.ID.get()));
            account.standingOrders.addAll(this.getStandingOrders("account=" + account.ID.get()));
            accounts.add(account);
        }
        this.closeStatement(resultSet);
        return accounts;
    }

    public int insertOrUpdateTransaction(Transaction transaction, Account account) throws Exception {
        PreparedStatement preparedStatement = this.getPreparedStatement(Arrays.asList("title", "description", "value", "date", "system", "category", "bankDetail", "account"), transaction);
        preparedStatement.setString(1, transaction.title.get());
        preparedStatement.setString(2, transaction.description.get());
        preparedStatement.setDouble(3, transaction.amount.get());
        if(transaction.date.get()!=null) {
            preparedStatement.setString(4, Converter.convertDateToString(transaction.date.get()));
        } else {
            preparedStatement.setNull(4, Types.DATE);
        }
        preparedStatement.setBoolean(5, transaction.system.get());
        this.setForeignFields(preparedStatement, transaction.category.get(), transaction.bankDetail.get(), account);
        return this.execute(preparedStatement, transaction);
    }

    public List<Transaction> getTransactions(String where) throws Exception {
        List<Transaction> transactions = new LinkedList<>();
        ResultSet resultSet = this.getQuery(Transaction.class, where);
        while (resultSet.next()) {
            Transaction transaction = new Transaction();
            transaction.ID.set(resultSet.getInt("ID"));
            transaction.title.set(resultSet.getString("title"));
            transaction.description.set(resultSet.getString("description"));
            transaction.amount.set(resultSet.getDouble("value"));
            String dt = resultSet.getString("date").trim();
            if(!dt.isEmpty()) {
                transaction.date.set(Converter.convertStringToDate(dt));
            }
            transaction.system.set(resultSet.getBoolean("system"));
            this.getForeignFields(transaction, resultSet);
            transactions.add(transaction);
        }
        this.closeStatement(resultSet);
        return transactions;
    }

    public void insertOrUpdateStandingOrder(StandingOrder standingOrder, Account account) throws Exception {
        PreparedStatement preparedStatement = this.getPreparedStatement(Arrays.asList("title", "start", "days", "months", "amount", "category", "bankDetail", "account"), standingOrder);
        preparedStatement.setString(1, standingOrder.title.get());
        if(standingOrder.start.get() != null) {
            preparedStatement.setString(2, Converter.convertDateToString(standingOrder.start.get()));
        } else {
            preparedStatement.setNull(2, Types.DATE);
        }
        preparedStatement.setInt(3, standingOrder.days.get());
        preparedStatement.setInt(4, standingOrder.months.get());
        preparedStatement.setDouble(5, standingOrder.amount.get());
        this.setForeignFields(preparedStatement, standingOrder.category.get(), standingOrder.bankDetail.get(), account);
        int id = this.execute(preparedStatement, standingOrder);
        this.setTags(id, standingOrder);
    }

    private void setTags(int id, StandingOrder standingOrder) throws Exception {
        this.delete("standingOrders_tags", "standingOrder=" + id);
        for(String strTag : standingOrder.tags.get().split(",")) {
            if(!strTag.trim().isEmpty()) {
                Tag tag = new Tag();
                tag.title.set(strTag.trim());
                int tagId = this.insertOrUpdateTag(tag);
                Statement stmt = this.connection.createStatement();
                stmt.execute(String.format("INSERT INTO standingOrders_tags(standingOrder, tag) VALUES(%s, %s)", id, tagId));
                stmt.close();
            }
        }
    }

    public List<StandingOrder> getStandingOrders(String where) throws Exception {
        List<StandingOrder> standingOrders = new LinkedList<>();
        ResultSet resultSet = this.getQuery(StandingOrder.class, where);
        while (resultSet.next()) {
            StandingOrder standingOrder = new StandingOrder();
            standingOrder.ID.set(resultSet.getInt("ID"));
            standingOrder.title.set(resultSet.getString("title"));
            String dt = resultSet.getString("start").trim();
            if(!dt.isEmpty()) {
                standingOrder.start.set(Converter.convertStringToDate(dt));
            }
            standingOrder.days.set(resultSet.getInt("days"));
            standingOrder.months.set(resultSet.getInt("months"));
            this.getForeignFields(standingOrder, resultSet);
            standingOrder.amount.set(resultSet.getDouble("amount"));
            List<String> tags = new LinkedList<>();
            for(Tag tag : this.getTags(standingOrder.ID.get())) {
                tags.add(tag.title.get());
            }
            standingOrder.tags.set(this.join(tags, ", "));
            standingOrders.add(standingOrder);
        }
        this.closeStatement(resultSet);
        return standingOrders;
    }

    private List<Tag> getTags(int id) throws Exception {
        List<String> ids = new LinkedList<>();

        Statement stmt = this.connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT tag FROM standingOrders_tags WHERE standingOrder=" + id);
        while (resultSet.next()) {
            ids.add(String.valueOf(resultSet.getInt("tag")));
        }
        resultSet.close();
        stmt.close();

        return this.getTags("id IN(" + this.join(ids, ", ") + ")");
    }

    private void setForeignFields(PreparedStatement preparedStatement, Category category, BankDetail bankDetail, Account account) throws Exception {
        if(category!=null) {
            preparedStatement.setInt(6, this.insertOrUpdateCategory(category));
        } else {
            preparedStatement.setNull(6, Types.INTEGER);
        }
        if(bankDetail!=null) {
            preparedStatement.setInt(7, this.insertOrUpdateBankDetails(bankDetail));
        } else {
            preparedStatement.setNull(7, Types.INTEGER);
        }
        preparedStatement.setInt(8, account.ID.get());
    }

    private void getForeignFields(StandingOrder standingOrder, ResultSet resultSet) throws Exception {
        int category = resultSet.getInt("category");
        if(category!=0) {
            standingOrder.category.set(this.getCategories("ID=" + category).get(0));
        }
        int bankDetail = resultSet.getInt("bankDetail");
        if(bankDetail!=0) {
            standingOrder.bankDetail.set(this.getBankDetails("ID=" + bankDetail).get(0));
        }
    }

    private void getForeignFields(Transaction transaction, ResultSet resultSet) throws Exception {
        int bankDetail = resultSet.getInt("bankDetail");
        if(bankDetail!=0) {
            transaction.bankDetail.set(this.getBankDetails("ID=" + bankDetail).get(0));
        }
        int category = resultSet.getInt("category");
        if(category!=0) {
            transaction.category.set(this.getCategories("ID=" + category).get(0));
        }
    }

    public void delete(DatabaseObject databaseObject) throws Exception {
        this.executeUpdate(String.format("DELETE FROM %s WHERE ID=%s", databaseObject.getTable(), databaseObject.getID()));
    }

    public void delete(String table, String where) throws Exception {
        this.executeUpdate(String.format("DELETE FROM %s WHERE %s", table, where));
    }

    private ResultSet getQuery(Class<? extends DatabaseObject> cls, String where) throws Exception {
        DatabaseObject obj = (DatabaseObject) cls.getConstructors()[0].newInstance();
        this.statement = this.connection.createStatement();
        return this.statement.executeQuery(String.format("SELECT * FROM %s %s", obj.getTable(), !where.trim().equals("") ? "WHERE " + where : ""));
    }

    private void closeStatement(ResultSet resultSet) throws Exception {
        resultSet.close();
        if(this.statement!=null) {
            if(!this.statement.isClosed()) {
                this.statement.close();
            }
        }
    }

    private int execute(PreparedStatement preparedStatement, DatabaseObject databaseObject) throws Exception {
        preparedStatement.executeUpdate();
        if(databaseObject.getID() == 0) {
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()) {
                return (int) rs.getLong(1);
            }
        }
        preparedStatement.close();
        return databaseObject.getID();
    }

    private PreparedStatement getPreparedStatement(List<String> columns, DatabaseObject databaseObject) throws Exception {
        if(databaseObject.getID()==0) {
            String columnString = this.join(columns, ",");
            List<String> values = new LinkedList<>();
            columns.forEach(column -> values.add("?"));
            String valueString = this.join(values, ",");
            return this.connection.prepareStatement(String.format("INSERT INTO %s(%s) VALUES(%s)", databaseObject.getTable(), columnString, valueString), Statement.RETURN_GENERATED_KEYS);
        } else {
            for(int i = 0; i<=columns.size() - 1; i++) {
                columns.set(i, columns.get(i) + "=?");
            }
            String columnString = this.join(columns, ",");
            return this.connection.prepareStatement(String.format("UPDATE %S SET %S WHERE ID=%s", databaseObject.getTable(), columnString, databaseObject.getID()));
        }
    }

    private void executeUpdate(String query) throws Exception {
        Statement statement = this.connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
    }

    private String join(List<String> items, String separator) {
        StringBuilder result = new StringBuilder();
        for(String item : items) {
            result.append(item).append(separator);
        }
        return (result.toString() + "))").replace(separator + "))", "");
    }
}
