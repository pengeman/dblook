package org.peng.dblook;

import dblook.dbmain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ConDB extends javax.swing.JDialog {

    java.awt.Frame frame ;
    /**
     * Creates new form ConDB
     */
    public ConDB(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        frame = parent;

    }




    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // 退出对话框
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed


    private List getAllProject(){
        String projects = dbhelpx.DBHelp.getInstanc().getProperties().getProperty("project");
        String[] pros = projects.split(",");
        List prol = new ArrayList();
        for(String pro : pros){
            prol.add(pro);
        }
        return prol;
    }
    
    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // 选中下拉列表项
        String item ;
       // dblook.Properties pre = new dblook.Properties();
        
        
        //if (evt.getStateChange() == java.awt.event.ItemEvent.DESELECTED) System.out.println("deselected" + item);
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            item = (String) evt.getItem();
            
            java.util.Properties pro = dbhelpx.DBHelp.getInstanc().getProperties();
            
            
            //java.util.Properties pro = pre.getPropertiesByProject(item);
            String driver = pro.getProperty(item+".driver");
            String url = pro.getProperty(item+".url");
            String username = pro.getProperty(item+".username");
            String pwd = pro.getProperty(item+".password");
            String characterEncoding = pro.getProperty(item+".characterEncoding");
            String InitialSize = pro.getProperty(item+".InitialSize");
            String MaxActive = pro.getProperty(item+".MaxActive");
            String MaxWait = pro.getProperty(item+".MaxWait");
            String MinIdle = pro.getProperty(item + ".MinIdle");
            

            
        }
            //System.out.println("selected" + item);
    }//GEN-LAST:event_jComboBox1ItemStateChanged


    /**
     * 链接数据库
     */
    private void connectionDB(String project){
        dbhelpx.DBHelp.destroy();
        dbhelpx.DBHelp dbh = dbhelpx.DBHelp.getInstanc(project);
        javax.swing.JOptionPane.showMessageDialog(this, project + "数据库链接成功");
        ((dbmain)frame).setJtextField1(project + " 链接 ...");
        ((dbmain)frame).setDhb(dbh);
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ConDB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConDB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConDB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConDB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ConDB dialog = new ConDB(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

}
