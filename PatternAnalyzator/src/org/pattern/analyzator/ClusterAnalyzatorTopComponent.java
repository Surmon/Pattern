package org.pattern.analyzator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.*;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.*;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.surmon.pattern.api.PatternData;
import org.surmon.pattern.api.PatternImage;
import org.surmon.pattern.api.analysis.Analysis;

/**
 * Top component which displays list of analyisis on image which currently has
 * the focus. It allows addition of new analysis such as deletion and rerun of
 * old ones.
 */
@ConvertAsProperties(
        dtd = "-//org.pattern.analyzator//ClusterAnalyzator//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ClusterAnalyzatorTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "properties", openAtStartup = true)
@ActionID(category = "Window", id = "org.pattern.analyzator.ClusterAnalyzatorTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ClusterAnalyzatorAction",
        preferredID = "ClusterAnalyzatorTopComponent"
)
@Messages({
    "CTL_ClusterAnalyzatorAction=ClusterAnalyzator",
    "CTL_ClusterAnalyzatorTopComponent=ClusterAnalyzator Window",
    "HINT_ClusterAnalyzatorTopComponent=This is a ClusterAnalyzator window"
})
public final class ClusterAnalyzatorTopComponent extends TopComponent implements LookupListener {

    private InstanceContent content = new InstanceContent();
    private Lookup lookup = new AbstractLookup(content);

    private JToolBar bottomToolbar = new JToolBar(JToolBar.HORIZONTAL);
    private JButton addAnalysisBtn;
    private JButton deleteAnalysisBtn;
    private AnalysisListView analysisListView;

    private Lookup.Result<PatternData> result = null;
    private PatternData data;

    public ClusterAnalyzatorTopComponent() {
        initComponents();
        setName(Bundle.CTL_ClusterAnalyzatorTopComponent());
        setToolTipText(Bundle.HINT_ClusterAnalyzatorTopComponent());

        associateLookup(lookup);
        initComponent();
    }

    private void initComponent() {
        setBackground(Color.WHITE);
        bottomToolbar.setFloatable(false);
        addAnalysisBtn = new JButton("Add"/*new ImageIcon("plus_16.png")*/);
        addAnalysisBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showAddDialog();
            }

        });
        addAnalysisBtn.setEnabled(false); // not enabled if no data
        bottomToolbar.add(addAnalysisBtn);

        deleteAnalysisBtn = new JButton("Delete");
        deleteAnalysisBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        bottomToolbar.add(deleteAnalysisBtn);
        add(bottomToolbar, BorderLayout.SOUTH);

        analysisListView = new AnalysisListView();
        add(analysisListView, BorderLayout.CENTER);
    }

    private void showAddDialog() {
        if (data != null) {
            final AddAnalysisPanel panel = new AddAnalysisPanel();
            final DialogDescriptor d = new DialogDescriptor(panel, "Add analysis", true, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == DialogDescriptor.OK_OPTION) {
                        if (panel.getAnalysis() != null) {
                            data.getSelectedImage().addAnalysis(panel.getAnalysis());
                            analysisListView.update(data.getSelectedImage().getAnalysisList());

                            SwingWorker worker = new SwingWorker<Void, Void>() {

                                @Override
                                protected Void doInBackground() throws Exception {
                                    panel.getAnalysis().execute(data.getSelectedImage());
                                    return null;
                                }
                            };
                            worker.execute();
                        }
                    }
                }
            });
            DialogDisplayer.getDefault().notifyLater(d);
        } else {
            throw new IllegalStateException("PatternData null");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        result = Utilities.actionsGlobalContext().lookupResult(PatternData.class);
        result.addLookupListener(this);
        // check for openedTopComponent of file, if found get's it's content
        // else display empty content and wait for some data to be opened

    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        result = null;
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    public void update() {
        content.set(Collections.singleton(data), null);
        analysisListView.update(data.getSelectedImage().getAnalysisList());
        revalidate();
        repaint();
    }

    public void setData(PatternData data) {
        this.data = data;
        if (data != null) {
            addAnalysisBtn.setEnabled(true);
            content.set(Collections.singleton(data), null); // TODO: ??
            analysisListView.update(data.getSelectedImage().getAnalysisList());
        } else {
            addAnalysisBtn.setEnabled(false);
        }

        revalidate();
        repaint();
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        Collection<? extends PatternData> datas = result.allInstances();
        if (!datas.isEmpty()) {
            setData(datas.iterator().next());
        }
    }

}
