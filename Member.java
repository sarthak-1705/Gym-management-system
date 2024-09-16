import javax.swing.JOptionPane;

public class Member {

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private String mid, name, nic, address, birthday, contact, gender, other;

    public Member() {
        initComponents();
        conn = DBConnect.connect();
        generateMemberId();
        loadTableData();
    }

    // Generate a new Member ID automatically
    private void generateMemberId() {
        try {
            String sql = "SELECT mid FROM member ORDER BY mid DESC LIMIT 1";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("mid");
                String prefix = lastId.substring(0, 2);
                int idNumber = Integer.parseInt(lastId.substring(2)) + 1;
                txtmid.setText(prefix + idNumber);
            } else {
                txtmid.setText("MI1000");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Error: " + e.getMessage());
        }
    }

    // Load the members' data into the table
    private void loadTableData() {
        try {
            String sql = "SELECT `mid` AS 'Member ID', `nic` AS 'NIC', `name` AS 'Name', `adress` AS 'Address', `contact` AS 'Contact Number', `birthday` AS 'Birthday', `gender` AS 'Gender', `other` AS 'Other', date AS 'Date' FROM `member`";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            tblmember.setModel(net.proteanit.sql.DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Error: " + e.getMessage());
        }
    }

    // Collect data from the form fields
    private void collectFormData() {
        mid = txtmid.getText();
        nic = txtnic.getText();
        name = txtname.getText();
        address = txtaddress.getText();
        contact = txtcontact.getText();
        birthday = ((JTextField) bdate.getDateEditor().getUiComponent()).getText();
        gender = cmbgender.getSelectedItem().toString();
        other = txtother.getText();
    }

    // Clear the form fields
    private void clearFormFields() {
        txtmid.setText("");
        txtnic.setText("");
        txtname.setText("");
        txtaddress.setText("");
        txtcontact.setText("");
        ((JTextField) bdate.getDateEditor().getUiComponent()).setText("");
        cmbgender.setSelectedIndex(0);
        txtother.setText("");
    }

    // Handle Add Member button click
    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        collectFormData();
        try {
            String query = "INSERT INTO `member`(`mid`, `nic`, `name`, `adress`, `contact`, `birthday`, `gender`, `other`, `date`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(query);
            pst.setString(1, mid);
            pst.setString(2, nic);
            pst.setString(3, name);
            pst.setString(4, address);
            pst.setString(5, contact);
            pst.setString(6, birthday);
            pst.setString(7, gender);
            pst.setString(8, other);
            pst.setString(9, Home.getDate());
            pst.execute();
            JOptionPane.showMessageDialog(rootPane, "Member added successfully.");
            loadTableData();
            clearFormFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Error: " + e.getMessage());
        }
    }

    // Handle Update Member button click
    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        collectFormData();
        try {
            String query = "UPDATE `member` SET `nic` = ?, `name` = ?, `adress` = ?, `contact` = ?, `birthday` = ?, `gender` = ?, `other` = ? WHERE `mid` = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, nic);
            pst.setString(2, name);
            pst.setString(3, address);
            pst.setString(4, contact);
            pst.setString(5, birthday);
            pst.setString(6, gender);
            pst.setString(7, other);
            pst.setString(8, mid);
            pst.execute();
            JOptionPane.showMessageDialog(rootPane, "Member updated successfully.");
            loadTableData();
            clearFormFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Error: " + e.getMessage());
        }
    }

    // Handle Delete Member button click
    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        int confirmation = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to delete this member?");
        if (confirmation == 0) {
            try {
                String query = "DELETE FROM `member` WHERE `mid` = ?";
                pst = conn.prepareStatement(query);
                pst.setString(1, txtmid.getText());
                pst.execute();
                JOptionPane.showMessageDialog(rootPane, "Member deleted successfully.");
                loadTableData();
                clearFormFields();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(rootPane, "Error: " + e.getMessage());
            }
        }
    }

    // Clear form and reload table on Reset button click
    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        clearFormFields();
        loadTableData();
    }

    // Load selected data into form when a table row is clicked
    private void tblmemberMouseClicked(java.awt.event.MouseEvent evt) {
        DefaultTableModel model = (DefaultTableModel) tblmember.getModel();
        int selectedRow = tblmember.getSelectedRow();
        txtmid.setText(model.getValueAt(selectedRow, 0).toString());
        txtnic.setText(model.getValueAt(selectedRow, 1).toString());
        txtname.setText(model.getValueAt(selectedRow, 2).toString());
        txtaddress.setText(model.getValueAt(selectedRow, 3).toString());
        txtcontact.setText(model.getValueAt(selectedRow, 4).toString());
        ((JTextField) bdate.getDateEditor().getUiComponent()).setText(model.getValueAt(selectedRow, 5).toString());
        cmbgender.setSelectedItem(model.getValueAt(selectedRow, 6).toString());
        txtother.setText(model.getValueAt(selectedRow, 7).toString());
    }
}
