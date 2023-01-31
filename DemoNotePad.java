/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.avalon.javapp.devj120;

import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.SystemColor.text;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author kateromanova
 */
public class DemoNotePad extends JFrame {
    
    private final JTextArea content;
    private static JScrollPane scrollPane;
    private final JFileChooser chooser;
    File f = new File("");
    boolean isChanged = false;
    private final JMenuBar menu;
    
    public DemoNotePad(){
        
        content = new JTextArea();
        
        content.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                isChanged = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                isChanged = false;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                isChanged = true;
            }
        });
        
        menu = new JMenuBar();
        
        JMenu file = new JMenu("File");
        
        chooser = new JFileChooser();
        
        
        
        JMenuItem newFile = new JMenuItem("New file");
        newFile.setAccelerator(KeyStroke.getKeyStroke(
                java.awt.event.KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        
        newFile.addActionListener(e -> {
            if(isChanged){
                if(JOptionPane.showConfirmDialog(this, "Are you sure you want to create new file?", 
                            "Creating new file", 
                            JOptionPane.YES_NO_OPTION, 
                            JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION)
                        return;
                
            }
            
              content.setText("");
              setTitle("NotePad");
              f = new File("");
            
        });
        
        
        
        
        JMenuItem openFile = new JMenuItem("Open file");
        openFile.addActionListener(e -> createFile());
        
        JMenuItem saveFile = new JMenuItem("Save file");
        saveFile.setAccelerator(KeyStroke.getKeyStroke(
                java.awt.event.KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        saveFile.addActionListener(e -> saveFile());
        
        
        JMenuItem saveAsFile = new JMenuItem("Save as ...");
        saveAsFile.setAccelerator(KeyStroke.getKeyStroke(
            java.awt.event.KeyEvent.VK_S, ((Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | java.awt.event.InputEvent.SHIFT_MASK))));
        saveAsFile.addActionListener(e -> saveAs());
        
        JMenuItem closeFile = new JMenuItem("Close file");
        closeFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              content.setText("");
              setTitle("NotePad");
              f = new File("");
              
            }
        });
        
        
        JMenuItem exit = new JMenuItem("Exit");
        exit.setAccelerator(KeyStroke.getKeyStroke(
                java.awt.event.KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        exit.addActionListener(e -> exit());
        
        
        
        file.add(newFile);
        file.add(openFile);
        file.add(saveFile);
        file.add(saveAsFile);
        file.add(closeFile);
        file.add(exit);
                
    
        
        menu.add(file);
        
        setJMenuBar(menu);
        //newFile.addActionListener(this);
        
        
        
        
        scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
         
        
        
        setBounds(800, 400, 600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("NotePad");
        
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getViewport().add(content);
        
        
        add(scrollPane);
        chooser.setMultiSelectionEnabled(false);
        
    }
    
    

    

    
    private void saveFile(File filename){
        
        try(FileWriter writer = new FileWriter(filename)){
                    
                    writer.write(content.getText());
                    writer.flush();
                setTitle(filename.getName());
            }   catch (IOException e2) {
                  JOptionPane.showMessageDialog(content,"Error writing file: " + e2.getMessage() + ".");  
                }
    }
    
    private void createFile(){
            if(isChanged){
                if(JOptionPane.showConfirmDialog(this, "Are you sure you want to create new file?", 
                            "Creating new file", 
                            JOptionPane.YES_NO_OPTION, 
                            JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION)
                        return;
                
            }
            
               JFileChooser chooser = new JFileChooser();
               chooser.showOpenDialog(content);
               f = chooser.getSelectedFile();
               //нужно прочесть файл
               try(FileReader reader = new FileReader (f)){
                   
                   char[] buf = new char [(int) f.length()];
                   
                   reader.read(buf);
                   content.setText(new String(buf));
                   content.setCaretPosition(0);
                   setTitle(f.getName());
                   
                   
               } catch (FileNotFoundException ex) {
                    
                } catch (IOException ex) {
                    content.setText("Error reading file: " + ex.getMessage() + ".");
                }
            
    }
    
    private void saveFile(){
            if (!f.canWrite()){
                int res = chooser.showDialog(menu, "Save file");
                if(res == JFileChooser.APPROVE_OPTION){
                    File newf = chooser.getSelectedFile();
                    if(newf.exists()){
                        if(JOptionPane.showConfirmDialog(this, "Are you sure you want to overwrite the file?", 
                                "File overwriting confirmation", 
                                JOptionPane.YES_NO_OPTION, 
                                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                            return;
                    }
                    //if(!chooser.getName().isEmpty())
                        saveFile(newf);
                }
            }
                else{
                    saveFile(f);
            }
            isChanged = false;
            
        }
    
    private void saveAs(){
            int res = chooser.showDialog(menu, "Save file");
            if(res == JFileChooser.APPROVE_OPTION){
                File newf = chooser.getSelectedFile();
                if(newf.exists()){
                    if(JOptionPane.showConfirmDialog(this, "Are you sure you want to overwrite the file?", 
                            "File overwriting confirmation", 
                            JOptionPane.YES_NO_OPTION, 
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                        return;
                }
                //if(!chooser.getName().isEmpty())
                    saveFile(newf);
                        
                
            }
            isChanged = false;
            
        }
    private void exit() {
            if(isChanged){
                if(JOptionPane.showConfirmDialog(this, "Are you sure you want to exit without saving?", 
                            "File save confirmation", 
                            JOptionPane.YES_NO_OPTION, 
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                        return;
                
            }
            System.exit(0);
            
        }
    
    
    
    public static void main(String[] args) {
        new DemoNotePad().setVisible(true);
    }
    
}