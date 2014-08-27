/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.importer;

import org.surmon.pattern.api.PDataImporter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Collection;
import javax.swing.JFileChooser;
import org.apache.commons.io.FilenameUtils;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.*;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.surmon.pattern.api.PatternInfo;
import org.surmon.pattern.project.api.*;

@ActionID(
        category = "File",
        id = "org.surmon.pattern.api.ImportAction"
)
@ActionRegistration(
        iconBase = "org/surmon/pattern/importer/resources/import_16.png",
        displayName = "#CTL_ImportAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1450),
    @ActionReference(path = "Toolbars/File", position = 300),
    @ActionReference(path = "Shortcuts", name = "D-I")
})
@Messages("CTL_ImportAction=Import ...")
/**
 * This is the import action for different file types. Import finds the propper
 * data loader and also loads reference to data. Data aren't automatically opened. 
 */
public final class ImportAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        createAndShowDialog();
    }

    /**
     * Creates and shows import dialog. Also handles user interaction with the
     * dialog.
     */
    public static void createAndShowDialog() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Import data");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(true);

        Collection<? extends PDataImporter> importers = Lookup.getDefault().lookupAll(PDataImporter.class);

        // find all importers and set the filters for open dialog
        for (PDataImporter importer : importers) {
            chooser.addChoosableFileFilter(importer.getExtensionFilter());
        }

        // open and wait for result
        int returnVal = chooser.showOpenDialog(null);

        // process the result from open dialog
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File file = chooser.getSelectedFile();

            if (file.exists()) {
                final String extension = FilenameUtils.getExtension(file.getName());
                final PDataImporter importer = selectImporter(importers, extension);
                if (importer != null) {
                    executeImport(importer, file);
                } else {
                    NotifyDescriptor nd = new NotifyDescriptor.Message("File " + extension + " doesn't contain image data or is not supported.");
                    DialogDisplayer.getDefault().notify(nd);
                }
            }
        }
    }

    /**
     * Selects from list of importers the first one that supports this
     * extension.
     *
     * @param importers
     * @param extension file extension (eq. mrc, jpg, ...)
     * @return selected importer or null if no support found
     */
    public static PDataImporter selectImporter(Collection<? extends PDataImporter> importers, String extension) {
        for (PDataImporter i : importers) {
            if (i.isSupporting(extension)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Process the import with selected importer and with selected file.
     *
     * @param importer
     * @param file
     */
    private static void executeImport(final PDataImporter importer, final File file) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // display progress bar of this task
                ProgressHandle p = ProgressHandleFactory.createHandle("Importing ...");
                //StatusDisplayer.getDefault().
                p.start();

                // get controller and create new project
                final WorkspaceController controller = Lookup.getDefault().lookup(WorkspaceController.class);
                final Project project = controller.newProject();

                // assign selected importer for further work eg. opening
                project.add(importer);

                // get project info and if there is some set temporary display name
                final ProjectInfo info = project.getLookup().lookup(ProjectInfo.class);
                if (info != null) {
                    info.setDisplayName("Loading ...");
                }

                // load image info
                final PatternInfo dataInfo = PatternInfoImporter.importInfo(file);
                project.add(dataInfo);

                // switch progress bar to indeterminate task
                p.switchToIndeterminate();

                // finally set proper name
                if (info != null) {
                    info.setDisplayName(file.getName());
                }

                // dismiss the progress bar
                p.finish();
            }
        }).start();
    }

}
