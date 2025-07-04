/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package projectuas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import static projectuas.dbkoneksi.koneksi;

/**
 *
 * @author SILVIA DIAN LESTARI
 * PROJECT UAS OOP
 */
public class frmTugas extends javax.swing.JFrame {
    String edNamaTugas="";
    DefaultTableModel TM = new DefaultTableModel();

public frmTugas() throws SQLException {
    initComponents();

    // Set model dan kolom tabel
    TABELTUGAS.setModel(TM);
    TM.addColumn("No");
    TM.addColumn("Nama Tugas");
    TM.addColumn("MataKuliah");
    TM.addColumn("Deskripsi");
    TM.addColumn("Deadline");
    TM.addColumn("Status");
    TM.addColumn("Id");
    hideColumn(6);

    // Atur lebar kolom
    TABELTUGAS.getColumnModel().getColumn(0).setPreferredWidth(40);
    TABELTUGAS.getColumnModel().getColumn(1).setPreferredWidth(150);
    TABELTUGAS.getColumnModel().getColumn(2).setPreferredWidth(120);
    TABELTUGAS.getColumnModel().getColumn(3).setPreferredWidth(200);
    TABELTUGAS.getColumnModel().getColumn(4).setPreferredWidth(100);
    TABELTUGAS.getColumnModel().getColumn(5).setPreferredWidth(80);
    
    
    TABELTUGAS.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt){
            formMouseClicked(evt);
        }
        
        
    
    });
    

    // Nonaktifkan field dan tombol di awal
    fieldisEnabled(false);
    tombolisEnabled(false);

    // Tampilkan data
    this.dtTugasList();
}

// ✅ Method: Aktif/nonaktifkan input form
private void fieldisEnabled(boolean opsi) {
    txNAMATUGAS.setEnabled(opsi);
    txMATAKULIAH.setEnabled(opsi);
    txDESKRIPSI.setEnabled(opsi);
    dcDEADLINE.setEnabled(opsi);
    rdBELUMSELESAI.setEnabled(opsi);
    rdSELESAI.setEnabled(opsi);
}

// ✅ Method: Aktif/nonaktifkan tombol
private void tombolisEnabled(boolean opsi) {
    cmdTambah.setEnabled(opsi);
    cmdEdit.setEnabled(opsi);
    cmdHapus.setEnabled(opsi);
    cmdTutup.setEnabled(opsi);
}

// ✅ Method: Reset form input
private void resetForm() {
    txNAMATUGAS.setText("");
    txMATAKULIAH.setText("");
    txDESKRIPSI.setText("");
    dcDEADLINE.setDate(null);
    buttonGroup1.clearSelection();
}
private void storeData() throws SQLException {
    // Ambil nilai dari input
    String namatugas = txNAMATUGAS.getText();
    String matakuliah = txMATAKULIAH.getText();
    String deskripsi = txDESKRIPSI.getText();

    // Ambil nilai tanggal dari JDateChooser (dcDEADLINE)
    java.util.Date deadlineDate = dcDEADLINE.getDate();
    java.sql.Date sqlDeadline = new java.sql.Date(deadlineDate.getTime());

    // Ambil status dari JRadioButton
    String status = "";
    if (rdSELESAI.isSelected()) {
        status = "Selesai";
    } else if (rdBELUMSELESAI.isSelected()) {
        status = "Belum Selesai";
    }

    // Koneksi dan simpan data
    Connection cnn = koneksi();
    PreparedStatement PS = cnn.prepareStatement(
        "INSERT INTO datatugas(namatugas, matakuliah, deskripsi, deadline, status) VALUES (?, ?, ?, ?, ?);"
    );
    PS.setString(1, namatugas);
    PS.setString(2, matakuliah);
    PS.setString(3, deskripsi);
    PS.setDate(4, sqlDeadline);
    PS.setString(5, status);
    PS.executeUpdate();
    
}
private void hideColumn(int columnIndex) {
    TableColumnModel columnModel = TABELTUGAS.getColumnModel();
    TableColumn column = columnModel.getColumn(columnIndex);
    
    // Menyembunyikan kolom dengan indeks yang diberikan
    column.setMaxWidth(0);
    column.setMinWidth(0);
    column.setPreferredWidth(0);
    column.setResizable(false);
}

private void updateData() throws SQLException {
    
    Connection cnn = dbkoneksi.koneksi();
    
    String namatugas = txNAMATUGAS.getText();
    String matakuliah = txMATAKULIAH.getText();
    String deskripsi = txDESKRIPSI.getText();
    
    // Ambil nilai tanggal dari JDateChooser (dcDEADLINE)
    java.util.Date deadlineDate = dcDEADLINE.getDate();
    
    // Periksa apakah deadlineDate null
    if (deadlineDate == null) {
        JOptionPane.showMessageDialog(this, "Tanggal deadline harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
        return;  // Hentikan proses jika tanggal null
    }
    
    java.sql.Date sqlDeadline = new java.sql.Date(deadlineDate.getTime());

    // Ambil status dari JRadioButton
    String status = "";
    if (rdSELESAI.isSelected()) {
        status = "Selesai";
    } else if (rdBELUMSELESAI.isSelected()) {
        status = "Belum Selesai";
    }

    // Ambil idtugas berdasarkan baris yang dipilih
    int idtugas = Integer.parseInt(TABELTUGAS.getValueAt(TABELTUGAS.getSelectedRow(), 6).toString());

    PreparedStatement PS = cnn.prepareStatement(
        "UPDATE datatugas SET namatugas=?, matakuliah=?, deskripsi=?, deadline=?, status=? WHERE idtugas=?;"
    );

    PS.setString(1, namatugas);
    PS.setString(2, matakuliah);
    PS.setString(3, deskripsi);
    PS.setDate(4, sqlDeadline);
    PS.setString(5, status);
    PS.setInt(6, idtugas);  // Mengisi parameter idtugas
    PS.executeUpdate();
}

private void destroyData()throws SQLException{
    int idtugas = Integer.parseInt(TABELTUGAS.getValueAt(TABELTUGAS.getSelectedRow(), 6).toString());

    Connection cnn = koneksi();
    PreparedStatement PS = cnn.prepareStatement("DELETE FROM datatugas WHERE idtugas=?;");
    PS.setInt(1, idtugas);
    PS.executeUpdate();
} 

private void tandaiSelesai() throws SQLException {
    int selectedRow = TABELTUGAS.getSelectedRow();
    if (selectedRow == -1) {
        return; // Pastikan ada baris yang dipilih
    }
    
    int idtugas = Integer.parseInt(TABELTUGAS.getValueAt(selectedRow, 6).toString());
    
    Connection cnn = dbkoneksi.koneksi();
    String query = "UPDATE datatugas SET status='Selesai' WHERE idtugas=?";
    PreparedStatement PS = cnn.prepareStatement(query);
    PS.setInt(1, idtugas);  // Mengisi parameter idtugas
    PS.executeUpdate();

    // Memperbarui tampilan tabel setelah update status
    dtTugasList();
}

// ✅ Method: Ambil data dari database dan isi ke tabel
private void dtTugasList() throws SQLException {
    String selectedFilter = jfilter.getSelectedItem().toString();
    Connection cnn = dbkoneksi.koneksi();  // Mendapatkan koneksi
    PreparedStatement PS;
    
    if (cnn == null) {
        JOptionPane.showMessageDialog(this, "Koneksi ke database gagal!", "Error", JOptionPane.ERROR_MESSAGE);
        return;  // Hentikan eksekusi jika koneksi gagal
    }
        

    if (selectedFilter.equalsIgnoreCase("Semua")) {
        PS = cnn.prepareStatement("SELECT * FROM datatugas");
    } else {
        PS = cnn.prepareStatement("SELECT * FROM datatugas WHERE status = ?");
        PS.setString(1, selectedFilter);
    }
    
    ResultSet RS = PS.executeQuery();


    TM.getDataVector().removeAllElements();
    TM.fireTableDataChanged();

    int no = 1;
    while (RS.next()) {
        Object[] dta = new Object[7];
        dta[0] = no++;
        dta[1] = RS.getString("namatugas");
        dta[2] = RS.getString("matakuliah");
        dta[3] = RS.getString("deskripsi");
        dta[4] = RS.getString("deadline");
        dta[5] = RS.getString("status");        
        dta[6] = RS.getString("idtugas");


        TM.addRow(dta);
        
        cmdTambah.setEnabled(true);
        cmdEdit.setEnabled(true);
        cmdHapus.setEnabled(true);
        cmdSelesai.setEnabled(true);
        cmdTutup.setEnabled(true);
    }
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txNAMATUGAS = new javax.swing.JTextField();
        txMATAKULIAH = new javax.swing.JTextField();
        txDESKRIPSI = new javax.swing.JTextField();
        rdBELUMSELESAI = new javax.swing.JRadioButton();
        rdSELESAI = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TABELTUGAS = new javax.swing.JTable();
        dcDEADLINE = new com.toedter.calendar.JDateChooser();
        cmdTambah = new javax.swing.JButton();
        cmdEdit = new javax.swing.JButton();
        cmdHapus = new javax.swing.JButton();
        cmdSelesai = new javax.swing.JButton();
        cmdTutup = new javax.swing.JButton();
        jfilter = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel1.setText("TO DO LIST MAHASISWA");

        jLabel3.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jLabel3.setText("NAMA TUGAS :");

        jLabel4.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jLabel4.setText("MATAKULIAH :");

        jLabel5.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jLabel5.setText("DESKRIPSI :");

        jLabel6.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jLabel6.setText("DEADLINE : ");

        jLabel7.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jLabel7.setText("STATUS :");

        txMATAKULIAH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txMATAKULIAHActionPerformed(evt);
            }
        });

        txDESKRIPSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txDESKRIPSIActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdBELUMSELESAI);
        rdBELUMSELESAI.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        rdBELUMSELESAI.setText("Belum Selesai");

        buttonGroup1.add(rdSELESAI);
        rdSELESAI.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        rdSELESAI.setText("Selesai");

        jLabel8.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jLabel8.setText("TABEL TUGAS");

        TABELTUGAS.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        TABELTUGAS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Nama Tugas", "Mata Kuliah", "Deskripsi", "Deadline", "Status"
            }
        ));
        jScrollPane1.setViewportView(TABELTUGAS);
        if (TABELTUGAS.getColumnModel().getColumnCount() > 0) {
            TABELTUGAS.getColumnModel().getColumn(0).setResizable(false);
            TABELTUGAS.getColumnModel().getColumn(0).setPreferredWidth(30);
        }

        cmdTambah.setBackground(new java.awt.Color(51, 153, 0));
        cmdTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmdTambah.setForeground(new java.awt.Color(255, 255, 255));
        cmdTambah.setText("Tambah");
        cmdTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTambahActionPerformed(evt);
            }
        });

        cmdEdit.setBackground(new java.awt.Color(0, 153, 255));
        cmdEdit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmdEdit.setForeground(new java.awt.Color(255, 255, 255));
        cmdEdit.setText("Edit");
        cmdEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditActionPerformed(evt);
            }
        });

        cmdHapus.setBackground(new java.awt.Color(255, 0, 0));
        cmdHapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmdHapus.setForeground(new java.awt.Color(255, 255, 255));
        cmdHapus.setText("Hapus");
        cmdHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdHapusActionPerformed(evt);
            }
        });

        cmdSelesai.setBackground(new java.awt.Color(255, 153, 0));
        cmdSelesai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmdSelesai.setForeground(new java.awt.Color(255, 255, 255));
        cmdSelesai.setText("Tandai Selesai");
        cmdSelesai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelesaiActionPerformed(evt);
            }
        });

        cmdTutup.setBackground(new java.awt.Color(153, 153, 153));
        cmdTutup.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmdTutup.setForeground(new java.awt.Color(255, 255, 255));
        cmdTutup.setText("Tutup");
        cmdTutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTutupActionPerformed(evt);
            }
        });

        jfilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua", "Belum Selesai", "Selesai"}));
        jfilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jfilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 788, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(364, 364, 364)
                        .addComponent(jLabel8))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(299, 299, 299)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jfilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 519, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txMATAKULIAH, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txNAMATUGAS, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(dcDEADLINE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txDESKRIPSI, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(rdBELUMSELESAI, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(rdSELESAI, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(30, 30, 30)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmdEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmdTambah, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmdSelesai, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmdHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmdTutup, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(65, 65, 65))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdTambah)
                            .addComponent(txNAMATUGAS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(cmdEdit)
                        .addGap(18, 18, 18)
                        .addComponent(cmdHapus)
                        .addGap(18, 18, 18)
                        .addComponent(cmdSelesai)
                        .addGap(18, 18, 18)
                        .addComponent(cmdTutup))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txMATAKULIAH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txDESKRIPSI, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(dcDEADLINE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(rdBELUMSELESAI)
                                    .addComponent(rdSELESAI))))))
                .addGap(22, 22, 22)
                .addComponent(jfilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txMATAKULIAHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txMATAKULIAHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txMATAKULIAHActionPerformed

    private void cmdTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTambahActionPerformed
        if (cmdTambah.getText().equals("Tambah")) {
                // Jika tombol Tambah ditekan, tombol berubah menjadi Simpan
                cmdTambah.setText("Simpan");
                cmdTutup.setText("Batal");
                resetForm();  // Reset form untuk menambah data baru
                fieldisEnabled(true);  // Aktifkan field untuk input data baru
                tombolisEnabled(true); 
                
                rdBELUMSELESAI.setSelected(true);  // Pilih status Belum Selesai
                rdSELESAI.setEnabled(false);
                
        } else {
        // Jika tombol Simpan ditekan, simpan data ke database
            try {
                storeData();  // Simpan data baru ke database
                dtTugasList();  // Perbarui tabel dengan data baru
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                Logger.getLogger(frmTugas.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        cmdTambah.setText("Tambah");  // Tombol kembali menjadi 'Tambah'
        cmdTutup.setText("Tutup");  // Tombol kembali menjadi 'Tutup'
        tombolisEnabled(false);  // Nonaktifkan tombol Simpan dan Batal setelah data disimpan
        fieldisEnabled(false);  // Nonaktifkan form setelah menyimpan data
        
        cmdTambah.setEnabled(true);
        cmdTutup.setEnabled(true);
        cmdEdit.setEnabled(true);
        cmdHapus.setEnabled(true);
        }
        
    }//GEN-LAST:event_cmdTambahActionPerformed
    
    
    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        int selectedRow = TABELTUGAS.getSelectedRow();
        
        if (selectedRow == -1) {  
        JOptionPane.showMessageDialog(this, "Pilih baris dari tabel terlebih dahulu.", "Error", JOptionPane.WARNING_MESSAGE);
        return;  // Hentikan jika tidak ada baris yang dipilih
        }
        
        if (cmdEdit.getText().equals("Edit")) {
           
            fieldisEnabled(true); // Enables the fields for editing

            cmdEdit.setText("Simpan");
            cmdTutup.setText("Batal");

            tombolisEnabled(true); 
            cmdTutup.setEnabled(true);

            
            txNAMATUGAS.setText(TABELTUGAS.getValueAt(TABELTUGAS.getSelectedRow(), 1).toString());
            txMATAKULIAH.setText(TABELTUGAS.getValueAt(TABELTUGAS.getSelectedRow(), 2).toString());
            txDESKRIPSI.setText(TABELTUGAS.getValueAt(TABELTUGAS.getSelectedRow(), 3).toString());

            try {
                String dateString = TABELTUGAS.getValueAt(selectedRow, 4).toString();
                java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                dcDEADLINE.setDate(date);  
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
            String status = TABELTUGAS.getValueAt(selectedRow, 5).toString();
            if (status.equalsIgnoreCase("Selesai")) {
                rdSELESAI.setSelected(true);
            } else {
                rdBELUMSELESAI.setSelected(true);
            }
            
        } else { 
            try {
                updateData();  
                dtTugasList();  
                JOptionPane.showMessageDialog(this, "Data berhasil diubah!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                Logger.getLogger(frmTugas.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            cmdEdit.setText("Edit");
            cmdTutup.setText("Tutup");  
            fieldisEnabled(false);
            
            cmdTutup.setEnabled(true);
            cmdEdit.setEnabled(true); // Pastikan Edit juga aktif
            cmdHapus.setEnabled(true);
            cmdSelesai.setEnabled(true);
            resetForm();
        }
    }//GEN-LAST:event_cmdEditActionPerformed

    private void cmdTutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTutupActionPerformed
        if(cmdTutup.getText().equals("Tutup")){
        int jopsi = JOptionPane.showOptionDialog(this, 
            "Yakin akan menutup aplikasi?", 
            "Konfirmasi Tutup Aplikasi", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, null, null);
        if(jopsi== JOptionPane.YES_NO_OPTION){
        System.exit(0);
        }
       }else{
            resetForm();
            fieldisEnabled(false);
            cmdTambah.setText("Tambah");
            cmdTutup.setText("Tutup");
            cmdEdit.setText("Edit");
            cmdTambah.setEnabled(true);
            cmdEdit.setEnabled(true);
        }
    }//GEN-LAST:event_cmdTutupActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
// Ambil data dari tabel dan tampilkan ke input
        txNAMATUGAS.setText(TABELTUGAS.getValueAt(TABELTUGAS.getSelectedRow(), 1).toString());
        txMATAKULIAH.setText(TABELTUGAS.getValueAt(TABELTUGAS.getSelectedRow(), 2).toString());
        txDESKRIPSI.setText(TABELTUGAS.getValueAt(TABELTUGAS.getSelectedRow(), 3).toString());

// Ambil dan atur tanggal ke JDateChooser
        try {
        String dateString = TABELTUGAS.getValueAt(TABELTUGAS.getSelectedRow(), 4).toString();
        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString); // sesuaikan format sesuai database kamu
        dcDEADLINE.setDate(date);
        }catch (ParseException e){
            e.printStackTrace();
        }

// Ambil dan atur status ke radio button
        String status = TABELTUGAS.getValueAt(TABELTUGAS.getSelectedRow(), 5).toString();
        if (status.equalsIgnoreCase("Selesai")) {
            rdSELESAI.setSelected(true);
        } else {
            rdBELUMSELESAI.setSelected(true);
        }

        // Aktifkan tombol Ubah dan Hapus
        cmdEdit.setEnabled(true);
        cmdHapus.setEnabled(true);
    }//GEN-LAST:event_formMouseClicked

    private void cmdHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdHapusActionPerformed
        String isNAMA = txNAMATUGAS.getText();
        int jopsi = JOptionPane.showOptionDialog(this, 
                "Yakin ingin menghapus data "+isNAMA+"?", 
                "Konfirmasi Hapus Data", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, null, null);
        
        if(jopsi == JOptionPane.YES_OPTION){
            try {
                destroyData();
                dtTugasList();
            } catch (SQLException ex) {
                Logger.getLogger(frmTugas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cmdHapusActionPerformed

    private void cmdSelesaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelesaiActionPerformed
        int selectedRow = TABELTUGAS.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu!", "Error", JOptionPane.WARNING_MESSAGE);
            return; // Menghentikan eksekusi jika tidak ada baris yang dipilih
        }
        
        try {
        tandaiSelesai();  // Memanggil metode untuk memperbarui status ke "Selesai"
        JOptionPane.showMessageDialog(this, "Tugas telah ditandai selesai!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
         Logger.getLogger(frmTugas.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }//GEN-LAST:event_cmdSelesaiActionPerformed

    private void txDESKRIPSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txDESKRIPSIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txDESKRIPSIActionPerformed

    private void jfilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jfilterActionPerformed
        try {
            dtTugasList();
        } catch (SQLException ex) {
            Logger.getLogger(frmTugas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jfilterActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new frmTugas().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(frmTugas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TABELTUGAS;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdHapus;
    private javax.swing.JButton cmdSelesai;
    private javax.swing.JButton cmdTambah;
    private javax.swing.JButton cmdTutup;
    private com.toedter.calendar.JDateChooser dcDEADLINE;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> jfilter;
    private javax.swing.JRadioButton rdBELUMSELESAI;
    private javax.swing.JRadioButton rdSELESAI;
    private javax.swing.JTextField txDESKRIPSI;
    private javax.swing.JTextField txMATAKULIAH;
    private javax.swing.JTextField txNAMATUGAS;
    // End of variables declaration//GEN-END:variables

}

