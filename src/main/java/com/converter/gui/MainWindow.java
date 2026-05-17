package com.converter.gui;

import com.converter.core.PdfConverter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame {

    // === Color Palette ===
    private static final Color BG_DARK = new Color(18, 18, 24);
    private static final Color BG_CARD = new Color(28, 28, 40);
    private static final Color BG_LIST = new Color(22, 22, 32);
    private static final Color ACCENT = new Color(99, 102, 241); // Indigo
    private static final Color ACCENT_HOVER = new Color(129, 132, 255);
    private static final Color DANGER = new Color(239, 68, 68);
    private static final Color DANGER_HOVER = new Color(255, 100, 100);
    private static final Color SUCCESS = new Color(16, 185, 129);
    private static final Color SUCCESS_HOVER = new Color(52, 211, 153);
    private static final Color TEXT_PRIMARY = new Color(237, 237, 245);
    private static final Color TEXT_SECONDARY = new Color(148, 148, 168);
    private static final Color BORDER_COLOR = new Color(55, 55, 75);
    private static final Color GRADIENT_START = new Color(99, 102, 241);
    private static final Color GRADIENT_END = new Color(168, 85, 247);

    private DefaultListModel<File> listModel;
    private JList<File> imageList;
    private List<File> selectedFiles = new ArrayList<>();
    private JLabel statusLabel;
    private JLabel countLabel;
    private JLabel previewImage;
    private JLabel previewInfo;

    public MainWindow() {
        setTitle("Paper Morph");
        setSize(820, 580);
        setMinimumSize(new Dimension(700, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_DARK);

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createCenterPanel(), BorderLayout.CENTER);
        root.add(createFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, GRADIENT_START, getWidth(), 0, GRADIENT_END);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JLabel title = new JLabel("Paper Morph");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Convert Images to PDF without any Ads!");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(new Color(255, 255, 255, 200));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);
        textPanel.add(title);
        textPanel.add(subtitle);

        header.add(textPanel, BorderLayout.WEST);
        return header;
    }

    private JPanel createCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setBackground(BG_DARK);
        center.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        countLabel = new JLabel("0 images selected");
        countLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        countLabel.setForeground(TEXT_SECONDARY);
        topRow.add(countLabel, BorderLayout.WEST);
        center.add(topRow, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setBackground(BG_DARK);
        split.setBorder(null);
        split.setDividerLocation(440);
        split.setResizeWeight(0.6);

        listModel = new DefaultListModel<>();
        imageList = new JList<>(listModel);
        imageList.setBackground(BG_LIST);
        imageList.setForeground(TEXT_PRIMARY);
        imageList.setSelectionBackground(ACCENT.darker());
        imageList.setSelectionForeground(Color.WHITE);
        imageList.setFont(new Font("Monospaced", Font.PLAIN, 13));
        imageList.setFixedCellHeight(36);
        imageList.setCellRenderer(new FileListRenderer());
        imageList.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        imageList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                updatePreview();
        });

        JScrollPane scroll = new JScrollPane(imageList);
        scroll.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scroll.getViewport().setBackground(BG_LIST);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        split.setLeftComponent(scroll);

        JPanel previewPanel = new JPanel(new BorderLayout(0, 8));
        previewPanel.setBackground(BG_CARD);
        previewPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel previewTitle = new JLabel("Preview");
        previewTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        previewTitle.setForeground(TEXT_PRIMARY);
        previewTitle.setHorizontalAlignment(SwingConstants.CENTER);
        previewPanel.add(previewTitle, BorderLayout.NORTH);

        previewImage = new JLabel("No image selected", SwingConstants.CENTER);
        previewImage.setForeground(TEXT_SECONDARY);
        previewImage.setFont(new Font("SansSerif", Font.ITALIC, 12));
        previewPanel.add(previewImage, BorderLayout.CENTER);

        previewInfo = new JLabel(" ");
        previewInfo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        previewInfo.setForeground(TEXT_SECONDARY);
        previewInfo.setHorizontalAlignment(SwingConstants.CENTER);
        previewPanel.add(previewInfo, BorderLayout.SOUTH);

        split.setRightComponent(previewPanel);
        center.add(split, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnPanel.setOpaque(false);

        JButton addBtn = createStyledButton("＋  Add Images", ACCENT, ACCENT_HOVER);
        JButton removeBtn = createStyledButton("✕  Remove", DANGER, DANGER_HOVER);
        JButton convertBtn = createStyledButton("⬇  Convert to PDF", SUCCESS, SUCCESS_HOVER);

        addBtn.addActionListener(e -> addImages());
        removeBtn.addActionListener(e -> removeImages());
        convertBtn.addActionListener(e -> convertToPdf());

        btnPanel.add(addBtn);
        btnPanel.add(removeBtn);
        btnPanel.add(convertBtn);
        center.add(btnPanel, BorderLayout.SOUTH);

        return center;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_CARD);
        footer.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 0, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)));

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setForeground(TEXT_SECONDARY);
        footer.add(statusLabel, BorderLayout.WEST);

        JLabel version = new JLabel("v1.0");
        version.setFont(new Font("SansSerif", Font.PLAIN, 11));
        version.setForeground(new Color(80, 80, 100));
        footer.add(version, BorderLayout.EAST);

        return footer;
    }

    private JButton createStyledButton(String text, Color bg, Color hoverBg) {
        JButton btn = new JButton(text) {
            private boolean hovering = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovering = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovering = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = hovering ? hoverBg : bg;
                g2.setColor(c);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));

                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(170, 38));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private static class FileListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            File f = (File) value;
            String ext = getExtension(f.getName()).toUpperCase();
            long kb = f.length() / 1024;
            label.setText("  🖼  " + f.getName() + "    (" + ext + " · " + kb + " KB)");
            label.setFont(new Font("SansSerif", Font.PLAIN, 13));
            label.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

            if (isSelected) {
                label.setBackground(new Color(99, 102, 241, 60));
                label.setForeground(Color.WHITE);
            } else {
                label.setBackground(index % 2 == 0 ? new Color(22, 22, 32) : new Color(26, 26, 38));
                label.setForeground(new Color(210, 210, 225));
            }
            return label;
        }

        private static String getExtension(String name) {
            int dot = name.lastIndexOf('.');
            return dot >= 0 ? name.substring(dot + 1) : "";
        }
    }

    private void updatePreview() {
        File sel = imageList.getSelectedValue();
        if (sel == null) {
            previewImage.setIcon(null);
            previewImage.setText("No image selected");
            previewInfo.setText(" ");
            return;
        }
        try {
            BufferedImage img = ImageIO.read(sel);
            if (img != null) {
                int pw = Math.max(previewImage.getWidth() - 20, 200);
                int ph = Math.max(previewImage.getHeight() - 20, 200);
                double ratio = Math.min((double) pw / img.getWidth(), (double) ph / img.getHeight());
                int w = Math.max(1, (int) (img.getWidth() * ratio));
                int h = Math.max(1, (int) (img.getHeight() * ratio));
                Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                previewImage.setIcon(new ImageIcon(scaled));
                previewImage.setText(null);
                previewInfo
                        .setText(img.getWidth() + " × " + img.getHeight() + " px  ·  " + (sel.length() / 1024) + " KB");
            } else {
                previewImage.setIcon(null);
                previewImage.setText("Cannot read image");
                previewInfo.setText(" ");
            }
        } catch (Exception ex) {
            previewImage.setIcon(null);
            previewImage.setText("Error loading preview");
            previewInfo.setText(" ");
        }
    }

    private void updateCount() {
        int n = selectedFiles.size();
        countLabel.setText(n + " image" + (n != 1 ? "s" : "") + " selected");
    }

    private void addImages() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg", "bmp", "gif", "tiff"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            int added = 0;
            for (File file : chooser.getSelectedFiles()) {
                if (!selectedFiles.contains(file)) {
                    selectedFiles.add(file);
                    listModel.addElement(file);
                    added++;
                }
            }
            updateCount();
            statusLabel.setText(added + " image" + (added != 1 ? "s" : "") + " added");
        }
    }

    private void removeImages() {
        List<File> toRemove = imageList.getSelectedValuesList();
        if (toRemove.isEmpty()) {
            statusLabel.setText("Select items to remove");
            return;
        }
        for (File file : toRemove) {
            selectedFiles.remove(file);
            listModel.removeElement(file);
        }
        updateCount();
        statusLabel.setText(toRemove.size() + " image(s) removed");
    }

    private void convertToPdf() {
        if (selectedFiles.isEmpty()) {
            statusLabel.setText("⚠ No images to convert");
            JOptionPane.showMessageDialog(this, "Please add images first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser saveChooser = new JFileChooser();
        saveChooser.setDialogTitle("Save PDF");
        saveChooser.setFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));

        if (saveChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File outputFile = saveChooser.getSelectedFile();
            if (!outputFile.getName().toLowerCase().endsWith(".pdf")) {
                outputFile = new File(outputFile.getAbsolutePath() + ".pdf");
            }
            statusLabel.setText("Converting…");
            try {
                PdfConverter.convertImagesToPdf(selectedFiles, outputFile);
                statusLabel.setText("✓ PDF saved: " + outputFile.getName());
                JOptionPane.showMessageDialog(this, "PDF created successfully!\n" + outputFile.getAbsolutePath(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                statusLabel.setText("✕ Conversion failed");
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
