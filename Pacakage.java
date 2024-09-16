import javax.swing.JOptionPane;

public class Package {

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private String pid, name, hour, milk, tablet, expert, body;

    public Package() {
        initComponents();
        conn = DBConnect.connect();
        loadTableData();
    }

    // Handle Add Package button click
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        collectData();
        try {
            String query = "INSERT INTO `package`(`name`, `hours`, `milk`, `tablet`, `expert`, `body`) VALUES (?, ?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, hour);
            pst.setString(3, milk);
            pst.setString(4, tablet);
            pst.setString(5, expert);
            pst.setString(6, body);
            pst.execute();
            JOptionPane.showMessageDialog(rootPane, "Package added successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Error: " + e.getMessage());
        }
        loadTableData();
    }

    // Handle Delete Package button click
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to delete this package?");
        if (confirm == 0) {
            try {
                String query = "DELETE FROM `package` WHERE name = ?";
                pst = conn.prepareStatement(query);
                pst.setString(1, txtpackagename.getText());
                pst.execute();
                loadTableData();
                clearFields();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(rootPane, "Error: " + e.getMessage());
            }
        }
    }

    // Handle Update Package button click
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        collectData();
        try {
            String query = "UPDATE `package` SET `hours` = ?, `milk` = ?, `tablet` = ?, `expert` = ?, `body` = ? WHERE name = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, hour);
            pst.setString(2, milk);
            pst.setString(3, tablet);
            pst.setString(4, expert);
            pst.setString(5, body);
            pst.setString(6, txtpackagename.getText());
            pst.execute();
            loadTableData();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Error: " + e.getMessage());
        }
    }

    // When a row is clicked in the table, load the selected data into the form fields
    private void tblpackageMouseClicked(java.awt.event.MouseEvent evt) {
        DefaultTableModel tmodel = (DefaultTableModel) tblpackage.getModel();
        int selectedRowIndex = tblpackage.getSelectedRow();
        txtpackagename.setText(tmodel.getValueAt(selectedRowIndex, 0).toString());
        txthour.setText(tmodel.getValueAt(selectedRowIndex, 1).toString());
        cmbmilk.setSelectedItem(tmodel.getValueAt(selectedRowIndex, 2).toString());
        cmbtablet.setSelectedItem(tmodel.getValueAt(selectedRowIndex, 3).toString());
        cmbadvice.setSelectedItem(tmodel.getValueAt(selectedRowIndex, 4).toString());
        cmbbodycheckup.setSelectedItem(tmodel.getValueAt(selectedRowIndex, 5).toString());
    }

    // Collect data from the form fields
    private void collectData() {
        name = txtpackagename.getText();
        hour = txthour.getText();
        milk = cmbmilk.getSelectedItem().toString();
        tablet = cmbtablet.getSelectedItem().toString();
        expert = cmbadvice.getSelectedItem().toString();
        body = cmbbodycheckup.getSelectedItem().toString();
    }

    // Clear the form fields
    private void clearFields() {
        txtpackagename.setText("");
        txthour.setText("");
        cmbmilk.setSelectedIndex(0);
        cmbtablet.setSelectedIndex(0);
        cmbadvice.setSelectedIndex(0);
        cmbbodycheckup.setSelectedIndex(0);
    }

    // Load data into the table
    private void loadTableData() {
        try {
            String query = "SELECT `name` AS 'Package Name', `hours` AS 'Hours', `milk` AS 'Milk', `tablet` AS 'Tablet', `expert` AS 'Expert Advice', `body` AS 'Body Checkup' FROM `package`";
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            tblpackage.setModel(net.proteanit.sql.DbUtils.resultSetToTableModel(rs));
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Error loading data: " + e.getMessage());
        }
    }
}
