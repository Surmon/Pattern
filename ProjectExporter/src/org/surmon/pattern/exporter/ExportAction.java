/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.exporter;

import com.sun.media.jai.codec.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import org.openide.awt.*;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.surmon.pattern.api.PatternData;
import org.surmon.pattern.api.PatternImage;
import org.surmon.pattern.api.utils.ImageConverter;
import org.surmon.pattern.project.api.Project;
import org.surmon.pattern.project.api.ProjectInfo;

@ActionID(
        category = "File",
        id = "org.surmon.pattern.exporter.ExportAction"
)
@ActionRegistration(
        iconBase = "org/surmon/pattern/exporter/resources/extract_16.png",
        displayName = "#CTL_ExportAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1300),
    @ActionReference(path = "Toolbars/File", position = 200),
    @ActionReference(path = "Shortcuts", name = "D-E")
})
@Messages("CTL_ExportAction=Export ...")
public final class ExportAction implements ActionListener {

    private JFileChooser fc;

    @Override
    public void actionPerformed(ActionEvent e) {

        // get project to export
        Project project = Utilities.actionsGlobalContext().lookup(Project.class);

        if (project == null) {
            return;
        }

        // initiate and show dialog
        fc = new JFileChooser();
        fc.setDialogTitle("Save project");

        ProjectInfo info = project.getLookup().lookup(ProjectInfo.class);
        if (info != null) {
            fc.setSelectedFile(new File(info.getDisplayName()));
        }

        int code = fc.showSaveDialog(null);

        // handle 
        if (code == JFileChooser.APPROVE_OPTION) {
            final File file = fc.getSelectedFile();
            //if (file.exists() && file.isFile()) {
                TIFFEncodeParam params = new TIFFEncodeParam();
                OutputStream out = null;
                try {
                    out = new FileOutputStream(file+ ".tif");
                } catch (FileNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                }
                ImageEncoder encoder = ImageCodec.createImageEncoder("tiff", out, params);

                PatternData data = project.getLookup().lookup(PatternData.class);
                List<BufferedImage> images = new ArrayList<>();
                
                for (PatternImage image : data.getImages()) {
                    images.add(ImageConverter.toBufferedImage(image.getPixels()));
                }
                
                params.setExtraImages(images.iterator());
                try {
                    encoder.encode(images.get(0));
                    out.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }

            //}
        }
    }
}
