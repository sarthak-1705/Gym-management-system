import javax.swing.JOptionPane;

public class Payment {

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private String mid, month, pack, amount;

    public Payment() {
        initComponents();
        conn = DBConnect.connect();
        loadTableData();
        populatePackageCombo();
    }

    // Load data into table
    private void loadTableData() {
        try {
            String sql = "SELECT `mid` AS 'Member ID', `month` AS 'Month', `package` AS 'Package', `amount` AS 'Amount', `date` AS 'Date' FROM `payment`";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            tblpayment.setModel(net.proteanit.sql.DbUtils.resultSetToTableModel(rs));
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Error loading data: " + e.getMessage());
        }
    }

    // Fetch data from input fields
    private void collectFormData() {
        mid = txtmid.getText();
        month = cmbmonth.getSelectedItem().toString();
        pack = cmbpackage.getSelectedItem().toString();
        amount = txtamount.getText();
    }

    // Clear input fields
    private void clearFields() {
        txtmid.setText("");
        txtamount.setText("");
        cmbmonth.setSelectedIndex(0);
        cmbpackage.setSelectedIndex(0);
    }

    // Handle Add Payment button click
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        collectFormData();
        try {
            String query = "INSERT INTO `payment`(`mid`, `month`, `package`, `amount`, `date`) VALUES (?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(query);
            pst.setString(1, mid);
            pst.setString(2, month);
            pst.setString(3, pack);
            pst.setString(4, amount);
            pst.setString(5, Home.getDate); // Assuming Home.getDate is a method that provides current date
            pst.execute();
            JOptionPane.showMessageDialog(rootPane, "Payment successfully recorded.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Error: " + e.getMessage());
        }
        loadTableData();
    }

    // Handle Search button click
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String sql = "SELECT `mid` AS 'Member ID', `month` AS 'Month', `package` AS 'Package', `amount` AS 'Amount', `date` AS 'Date' FROM `payment` WHERE `mid` = ? AND `month` = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtmidsearch.getText());
            pst.setString(2, cmbmonthsearch.getSelectedItem().toString());
            rs = pst.executeQuery();
            tblpayment.setModel(net.proteanit.sql.DbUtils.resultSetToTableModel(rs));
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Error searching records: " + e.getMessage());
        }
    }

    // Load packages into combo box
    private void populatePackageCombo() {
        try {
            String sql = "SELECT `name` FROM `package`";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                cmbpackage.addItem(rs.getString("name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Error loading packages: " + e.getMessage());
        }
    }
}
