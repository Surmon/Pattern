package org.surmon.pattern.editor.table;

import java.awt.Component;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.table.*;
import org.openide.util.Exceptions;
import org.surmon.pattern.api.Particle;

/**
 * This class is now JDK 1.6 Dependent, with the addition of the use of the
 * Desktop class instead of the default application launch procedure, and the
 * use of the new FileNameExtensionFilter class introduced under
 * javax.swing.filechooser.
 *
 * This class can be used in one line of code by passing the appropriate JTable
 * to the desired constructor.
 */
public class ExcelExporter extends Object {

    public JFileChooser chooser;
    public File csvFile;
    private boolean cancelOp = false, isDefault = true;
    private String topText = "";
    private final List<Particle> particles;

    public ExcelExporter(final List<Particle> particles) {
        this.particles = particles;
        obtainFileName();
    }

    private void obtainFileName() {
        cancelOp = false;

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Format (CSV)", "csv");
        if (chooser == null) {
            chooser = new JFileChooser();
            chooser.setDialogTitle("Saving Database");
            chooser.setFileFilter(filter);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setSelectedFile(new File("data.csv"));
            chooser.setAcceptAllFileFilterUsed(false);
        }

        int val = chooser.showSaveDialog((Component) null);

        if (val == JFileChooser.APPROVE_OPTION) {
            csvFile = chooser.getSelectedFile();
            boolean fixed = fixExtension(csvFile, "csv");

            if (!fixed && !cancelOp) {
                JOptionPane.showMessageDialog(null, "File Name Specified Not Supported", 
                        "File Name Error", JOptionPane.ERROR_MESSAGE);
                obtainFileName();
                return;
            }

            if (!cancelOp) {
                try {
                    storeParticlesAsCSV(csvFile, particles);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    public boolean fixExtension(File file, String prefExt) {
        String fileName = file.getName();
        String dir = file.getParentFile().getAbsolutePath();

        String ext = null;

        try {
            ext = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            System.out.println("Original File Extension: " + ext);
        } catch (StringIndexOutOfBoundsException e) {
            ext = null;
        }

        if (ext != null && !ext.equalsIgnoreCase("." + prefExt)) {
            return false;
        }

        String csvName = null;

        if (ext == null || ext.length() == 0) {
            csvName = fileName + "." + prefExt;
        } else {
            csvName = fileName.substring(0, fileName.lastIndexOf(".") + 1) + prefExt;
        }

        System.out.println("Corrected File Name: " + csvName);

        File csvCert = new File(dir, csvName);

        if (csvCert.exists()) {
            int val = JOptionPane.showConfirmDialog(null, "Replace Existing File?", "File Exists",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

            if (val == JOptionPane.NO_OPTION) {
                obtainFileName();
                cancelOp = true;
                return false;
            } else if (val == JOptionPane.CANCEL_OPTION) {
                cancelOp = true;
                return false;
            }
        }

        if (!file.renameTo(csvCert)) {
            file = new File(dir, csvName);
            try {
                file.createNewFile();
            } catch (IOException ioe) {
            }
        }

        System.out.println("Exporting as: " + file.getAbsolutePath());

        return true;
    }
    
    /**
     * 
     * @param target
     * @param particles
     */
    public void storeParticlesAsCSV(File target, List<Particle> particles) throws IOException {
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(target));
        
        for (Particle particle : particles) {
            writer.write(String.valueOf(particle.getId()));
            
            double[] pos = particle.getPosition().toArray();
            for (double d : pos) {
                writer.write(",");
                writer.write(String.valueOf(d));
            }
            writer.newLine();
        }
        
        writer.flush();
        writer.close();
        writer = null;
    }

    public String removeAnyCommas(String src) {
        if (src == null) {
            return "";
        }

        for (int i = 0; i < src.length(); i++) {
            if (src.charAt(i) == ',') {
                src = src.substring(0, i) + src.substring(i + 1, src.length());
            }
        }

        return src;
    }
}
