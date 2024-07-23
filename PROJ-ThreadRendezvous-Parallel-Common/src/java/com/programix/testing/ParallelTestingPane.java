package com.programix.testing;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;

import com.programix.gui.*;
import com.programix.gui.layout.*;
import com.programix.testing.TestChunk.*;
import com.programix.thread.*;

public class ParallelTestingPane {
    private final Control control;
    private JComponent visualComponent;

    private JButton kickoffB;
    private JButton cancelB;
    private JButton resetB;
    private JTextField stateTF;
    private JTextField threadCountTF;
    private JTextField runningTimeTF;
    private JTextField passCountTF;
    private JTextField failCountTF;
    private JTextField totalCountTF;
    private JTextField totalPointsTF;

    private TestChunk testChunkContainer;
    private TestChunkModel testChunkContainerModel;
    private NanoTimer runningTimer;

    private JSplitPane mainSplitPane;
    private JTable table;
    private TestChunkTableModel tableModel;
    private final BooleanState everAutoSizedColumns = new BooleanState(false);
    private Deck deck;

    private boolean showPoints;

    private TestThreadFactory currentThreadFactory = null;

    public ParallelTestingPane(Control control) {
        this.control = control;
        showPoints = control.shouldShowPoints();
        runningTimer = NanoTimer.createStopped();
        createUi();
    }

    private void createUi() {
        confirmEventThread();

        kickoffB = new JButton("Kickoff Tests");
        kickoffB.setMnemonic(KeyEvent.VK_K);
        kickoffB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kickoffTests();
            }
        });

        cancelB = new JButton("Cancel Tests");
        cancelB.setMnemonic(KeyEvent.VK_C);
        cancelB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelTests();
            }
        });

        resetB = new JButton("Reset");
        resetB.setMnemonic(KeyEvent.VK_R);
        resetB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTests();
            }
        });

        JPanel buttonP = new JPanel(new ColumnButtonLayout());
        buttonP.add(kickoffB);
        buttonP.add(cancelB);
        buttonP.add(resetB);

        stateTF = createUneditableTextField(10);
        threadCountTF = createUneditableTextFieldAlignRight(5);
        runningTimeTF = createUneditableTextFieldAlignRight(5);
        passCountTF = createUneditableTextFieldAlignRight(5);
        failCountTF = createUneditableTextFieldAlignRight(5);
        totalCountTF = createUneditableTextFieldAlignRight(5);
        totalPointsTF = createUneditableTextFieldAlignRight(5);

        JPanel formP = new JPanel(new FormLayout());
        formP.add(new JLabel("State:"));
        formP.add(stateTF);
        formP.add(new JLabel("Thread Count:"));
        formP.add(threadCountTF);
        formP.add(new JLabel("Running Time:"));
        formP.add(runningTimeTF);
        formP.add(new JLabel("Passed:"));
        formP.add(passCountTF);
        formP.add(new JLabel("Failed:"));
        formP.add(failCountTF);
        formP.add(new JLabel("Total:"));
        formP.add(totalCountTF);

        if (showPoints) {
            formP.add(new JLabel("Total Points:"));
            formP.add(totalPointsTF);
        }

        JPanel controlP = new JPanel(new ShelfLayout());
        controlP.add(buttonP);
        controlP.add(formP);

        JLabel titleL = GuiTools.createScaledLabel(control.getTitle(), 1.5);

        JPanel topP = new JPanel(new StackLayout());
        topP.add(titleL);
        topP.add(controlP);

        tableModel = new TestChunkTableModel(showPoints);
        table = new JTable(tableModel);
        tableModel.setupRenderers(table);

        deck = new Deck();

        mainSplitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            new JScrollPane(table),
            deck);

        JPanel testAreaPanel = new JPanel(GuiTools.ONE_CELL_GRID);
        testAreaPanel.add(mainSplitPane);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainSplitPane.setDividerLocation(0.4);
            }
        });

        JPanel mainP = new JPanel(new BorderLayout(3, 3));
        mainP.add(topP, BorderLayout.NORTH);
        mainP.add(testAreaPanel, BorderLayout.CENTER);
        visualComponent = mainP;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                resetTests();
                kickoffTests();
            }
        });
    }

    public JComponent getVisualComponent() {
        return visualComponent;
    }

    public static void confirmEventThread() throws IllegalStateException {
        if ( SwingUtilities.isEventDispatchThread() == false ) {
            throw new IllegalStateException(
                "only the event handling thread can call this method");
        }
    }

    public static void createFramedInstance(final Control control) {
        if ( SwingUtilities.isEventDispatchThread() == false ) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    createFramedInstance(control);
                }
            });
            return;
        }

        ParallelTestingPane ptp = new ParallelTestingPane(control);

        JComponent visualComponent = ptp.getVisualComponent();
        JPanel contentPane = new JPanel(new GridLayout());
        contentPane.add(visualComponent);

        JFrame f = new JFrame(control.getTitle());
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.setContentPane(contentPane);

        //f.setSize(1500, 800);
        //GuiTools.setSizeAndCenterOnScreen(f, 1400, 900);
        GuiTools.setSizeToMax(f);
        f.setVisible(true);

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private static JTextField createUneditableTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setEditable(false);
        return tf;
    }

    private static JTextField createUneditableTextFieldAlignRight(int columns) {
        JTextField tf = createUneditableTextField(columns);
        tf.setHorizontalAlignment(SwingConstants.RIGHT);
        return tf;
    }

    private void kickoffTests() {
        runningTimer.resetAndStart();
        testChunkContainer.kickoffTestsAsync();
        drawFocusAndSelectionToTable();
    }

    private static class ThreadFactoryShudownHelper {
        private final TestThreadFactory threadFactory;

        public ThreadFactoryShudownHelper(TestThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            if (threadFactory != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runWork();
                    }
                }, "ThreadFactoryShudownHelper").start();
            }
        }

        private void runWork() {
            try {
                threadFactory.interruptAllLiveThreads();
                Thread.sleep(1000);
                threadFactory.interruptAllLiveThreads();
                Thread.sleep(1000);
            } catch ( InterruptedException x ) {
                // ignore
            } finally {
                threadFactory.forciblyStopAllLiveThreads();
            }
        }
    } // type ThreadFactoryShutdownHelper

    @SuppressWarnings("unused")
    private void cancelTests() {
        confirmEventThread();
        testChunkContainer.cancelAllTests();
        new ThreadFactoryShudownHelper(currentThreadFactory);
    }

    @SuppressWarnings("unused")
    private void resetTests() {
        confirmEventThread();

        new ThreadFactoryShudownHelper(currentThreadFactory);
        currentThreadFactory = new BasicTestThreadFactory();

        TestChunk[] testChunks = control.createNewTestChunks(currentThreadFactory);
        TestChunkModel[] testChunkModels = TestChunkModel.createModels(testChunks);
        TestChunkDetailPane[] testPanes =
            TestChunkDetailPane.createPanes(testChunkModels);

        testChunkContainer = new TestChunkContainer(
            testChunks, "<container for " + testChunks.length + " TestChunks>");
        testChunkContainerModel = new TestChunkModel(testChunkContainer);
        testChunkContainerModel.addModelListener(new TestChunkModel.ModelListener() {
            @Override
            public void modelChanged(TestChunkModel model) {
                updateUi();
            }
        });

        tableModel.setRows(testChunkModels);
        if ( everAutoSizedColumns.isFalse() ) {
            everAutoSizedColumns.setState(true);
            GuiTools.setPreferredColumnSizes(table, 10, 800, 15, 100);
        }

        deck.removeAllCards();

        final Deck.Card[] chunkCards = new Deck.Card[testPanes.length];
        for ( int i = 0; i < chunkCards.length; i++ ) {
            Deck.Card card = deck.createCard();
            chunkCards[i] = card;
            card.add(testPanes[i].getVisualComponent());
        }
        final Deck.Card noneSelectedCard = chunkCards[0];

        final ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if ( e.getValueIsAdjusting() ) return;

                int selectedRow = table.getSelectedRow();
                if ( selectedRow == -1 ) {
                    noneSelectedCard.toFront();
                } else {
                    int modelIdx = table.convertRowIndexToModel(selectedRow);
                    chunkCards[modelIdx].toFront();
                }

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        table.requestFocusInWindow();
                    }
                });
            }
        });

        drawFocusAndSelectionToTable();
        updateUi();
    }

    private void drawFocusAndSelectionToTable() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ListSelectionModel selectionModel = table.getSelectionModel();
                if ( selectionModel.getMinSelectionIndex() == -1 ) {
                    selectionModel.setSelectionInterval(0, 0);
                }
                table.requestFocusInWindow();
            }
        });
    }

    private void updateUi() {
        if ( SwingUtilities.isEventDispatchThread() == false ) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateUi();
                }
            });
            return;
        }

        TestState state = testChunkContainerModel.getState();
        stateTF.setText(state.getDisplayName());
        stateTF.setBackground(state.getPreferredBackgroundColor());
        threadCountTF.setText(String.format("%,d", testChunkContainerModel.getActiveThreadCount()));
        runningTimeTF.setText(String.format("%.1fs", runningTimer.getElapsedSeconds()));
        passCountTF.setText(String.format("%,d", testChunkContainerModel.getPassCount()));
        failCountTF.setText(String.format("%,d", testChunkContainerModel.getFailCount()));
        totalCountTF.setText(String.format("%,d", testChunkContainerModel.getTotalCount()));

        if (showPoints) {
            int totalPoints = 0;
            int rowCount = tableModel.getRowCount();
            for (int row = 0; row < rowCount; row++) {
                totalPoints += (Integer) tableModel.getValueAt(row, 4);
            }
            totalPointsTF.setText(String.format("%,d", totalPoints));
        }

        switch ( state ) {
            case NEVER_STARTED:
                kickoffB.setEnabled(true);
                cancelB.setEnabled(false);
                resetB.setEnabled(false);
                break;

            case RUNNING:
                kickoffB.setEnabled(false);
                cancelB.setEnabled(true);
                resetB.setEnabled(false);
                break;

            case CANCELLED:
            case FAILED:
            case SUCCEEDED:
                kickoffB.setEnabled(false);
                cancelB.setEnabled(false);
                resetB.setEnabled(true);
                break;
        }
    }

    private static class TestChunkTableModel extends AbstractTableModel {
        private final TableModelColumn<TestChunkModel, ?>[] columns;
        private TestChunkModel[] testChunkModels = TestChunkModel.ZERO_LEN_ARRAY;

        @SuppressWarnings("unchecked")
        public TestChunkTableModel(boolean showPoints) {
            List<TableModelColumn<TestChunkModel, ?>> colList = new ArrayList<>();
            colList.add(
                new TableModelColumn<>(
                    TestState.class,
                    "State",
                    new CellExtractor<TestChunkModel, TestState>() {
                        @Override
                        public TestState getValueForRow(TestChunkModel row) {
                            return row.getState();
                        }
                    },
                    new StateCellRenderer()));

            colList.add(
                new TableModelColumn<>(
                    Integer.class,
                    "Passed",
                    new CellExtractor<TestChunkModel, Integer>() {
                        @Override
                        public Integer getValueForRow(TestChunkModel row) {
                            return row.getPassCount();
                        }
                    }));

            colList.add(
                new TableModelColumn<>(
                    Integer.class,
                    "Failed",
                    new CellExtractor<TestChunkModel, Integer>() {
                        @Override
                        public Integer getValueForRow(TestChunkModel row) {
                            return row.getFailCount();
                        }
                    }));

            colList.add(
                new TableModelColumn<>(
                    Integer.class,
                    "Total",
                    new CellExtractor<TestChunkModel, Integer>() {
                        @Override
                        public Integer getValueForRow(TestChunkModel row) {
                            return row.getTotalCount();
                        }
                    }));

            if (showPoints) {
                colList.add(
                    new TableModelColumn<>(
                        Integer.class,
                        "Points",
                        new CellExtractor<TestChunkModel, Integer>() {
                            @Override
                            public Integer getValueForRow(TestChunkModel row) {
                                return row.getPoints();
                            }
                        }));

                colList.add(
                    new TableModelColumn<>(
                        String.class,
                        "Valid",
                        new CellExtractor<TestChunkModel, String>() {
                            @Override
                            public String getValueForRow(TestChunkModel row) {
                                boolean valid = !row.isPassCountExceedsExpectedPassCount();
                                return valid ? "yes" : "NO!";
                            }
                        }));
            }

            colList.add(
                new TableModelColumn<>(
                    String.class,
                    "Title",
                    new CellExtractor<TestChunkModel, String>() {
                        @Override
                        public String getValueForRow(TestChunkModel row) {
                            return row.getTitle();
                        }
                    }));
            columns = colList.toArray(new TableModelColumn[0]);
        }

        public void setupRenderers(JTable table) {
            for ( int i = 0; i < columns.length; i++ ) {
                if ( columns[i].cellRenderer != null ) {
                    table.getColumnModel().getColumn(i).setCellRenderer(columns[i].cellRenderer);
                }
            }
        }

        public void setRows(TestChunkModel[] testChunkModels) {
            this.testChunkModels = testChunkModels;

            for ( int i = 0; i < testChunkModels.length; i++ ) {
                final int rowIdx = i;
                final TestChunkModel row = testChunkModels[rowIdx];
                row.addModelListener(new TestChunkModel.ModelListener() {
                    @Override
                    public void modelChanged(TestChunkModel targetModel) {
                        fireTableRowsUpdated(rowIdx, rowIdx);
                    }
                });
            }

            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return testChunkModels.length;
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public Class<?> getColumnClass(int colIdx) {
            return columns[colIdx].columnClass;
        }

        @Override
        public String getColumnName(int colIdx) {
            return columns[colIdx].displayName;
        }

        @Override
        public Object getValueAt(int rowIdx, int colIdx) {
            return columns[colIdx].cellExtractor
                .getValueForRow(testChunkModels[rowIdx]);
        }

        public static class TableModelColumn<R, C> {
            public Class<C> columnClass;
            public String displayName;
            public CellExtractor<R, C> cellExtractor;
            public CellRenderer<C> cellRenderer;

            public TableModelColumn(Class<C> columnClass,
                                    String displayName,
                                    CellExtractor<R, C> cellExtractor,
                                    CellRenderer<C> cellRenderer) {

                this.columnClass = columnClass;
                this.displayName = displayName;
                this.cellExtractor = cellExtractor;
                this.cellRenderer = cellRenderer;
            }

            public TableModelColumn(Class<C> columnClass,
                                    String displayName,
                                    CellExtractor<R, C> cellExtractor) {
                this(columnClass, displayName, cellExtractor, null);
            }

        } // type TableModelColumn

        public static interface CellExtractor<R, C> {
            C getValueForRow(R row);
        } // type CellExtractor

        public static abstract class CellRenderer<C>
                extends DefaultTableCellRenderer.UIResource {

        }

        private static class StateCellRenderer extends CellRenderer<TestState> {
            @Override
            protected void setValue(Object value) {
                TestState state = (TestState) (value != null ? value : TestState.NEVER_STARTED);
                setText(state.getDisplayName());
                setBackground(state.getPreferredBackgroundColor());
            }
        } // type StateCellRenderer
    } // type TestChunkTableModel

    public static interface Control {
        String getTitle();
        TestChunk[] createNewTestChunks(TestThreadFactory threadFactory);
        boolean shouldShowPoints();
    } // type Control

    private static class TestChunkModel {
        public static final TestChunkModel[] ZERO_LEN_ARRAY = new TestChunkModel[0];

        private final TestChunk testChunk;
        private final ListenerManager<ModelListener> listenerManager;

        private final String title;
        private TestState state;
        private final Counter passCount;
        private final Counter failCount;
        private final Counter totalCount;
        private boolean passCountExceedsExpectedPassCount;

        public TestChunkModel(TestChunk chunk) {
            this.testChunk = chunk;
            listenerManager = new ListenerManager<>(ModelListener.class);

            state = chunk.getTestState();
            passCount = new Counter(0);
            failCount = new Counter(0, passCount.getLockObject());
            totalCount = new Counter(0, passCount.getLockObject());

            title = chunk.getDisplayName();

            final ListenerManager.NotifyAction<ModelListener> notifyAction =
                new ListenerManager.NotifyAction<>() {

                @Override
                public void performAction(ModelListener listener) {
                    listener.modelChanged(TestChunkModel.this);
                }
            };

            chunk.addListener(new TestChunk.ListenerAdapter() {
                @Override
                public void testStateChanged(ListenerEventMeta meta,
                                             TestState oldState,
                                             TestState newState) {

                    state = newState;
                    listenerManager.notifyListeners(notifyAction);
                }

                @Override
                public void incrementPassedCount(ListenerEventMeta meta) {
                    synchronized ( totalCount.getLockObject() ) {
                        passCount.increment();
                        totalCount.increment();
                        passCountExceedsExpectedPassCount =
                            passCount.getCount() > testChunk.getScoringInfo().getExpectedPassCount();
                        listenerManager.notifyListeners(notifyAction);
                    }
                }

                @Override
                public void incrementFailedCount(ListenerEventMeta meta) {
                    synchronized ( totalCount.getLockObject() ) {
                        failCount.increment();
                        totalCount.increment();
                        listenerManager.notifyListeners(notifyAction);
                    }
                }
            });
        }

        public TestChunk getTestChunk() {
            return testChunk;
        }

        public boolean addModelListener(ModelListener listener) {
            return listenerManager.addListener(listener);
        }

        @SuppressWarnings("unused")
        public boolean removeModelListener(ModelListener listener) {
            return listenerManager.removeListener(listener);
        }

        public String getTitle() {
            return title;
        }

        public TestState getState() {
            return state;
        }

        public int getActiveThreadCount() {
            return testChunk.getActiveThreadCount();
        }

        public int getPassCount() {
            return passCount.getCount();
        }

        public int getFailCount() {
            return failCount.getCount();
        }

        public int getTotalCount() {
            return totalCount.getCount();
        }

        public boolean isPassCountExceedsExpectedPassCount() {
            return passCountExceedsExpectedPassCount;
        }

        public int getPoints() {
            ScoringInfo si = testChunk.getScoringInfo();
            if (si == null || si.getFullPointValue() < 1) {
                return 0;
            }

            //int qualifiedPassCount = Math.min(getTotalCount() - getFailCount(), si.getExpectedPassCount());
            //int qualifiedPassCount = Math.max(0, si.getExpectedPassCount() - getFailCount());
            //int qualifiedPassCount = Math.max(0, Math.min(si.getExpectedPassCount() - getFailCount(), si.getExpectedPassCount()));
            int qualifiedPassCount = Math.max(0, Math.min(getPassCount() - getFailCount(), si.getExpectedPassCount()));
            int points = (int) Math.floor(si.getFullPointValue() * (double) qualifiedPassCount / si.getExpectedPassCount());

            // Make sure that if there are any failures, we always take at least one point off
            if (getFailCount() > 0) points = Math.min(points, si.getFullPointValue() - 1);

            // Be really, really sure we're always in a valid range
            return Math.max(0, Math.min(points, si.getFullPointValue()));
        }

        public static TestChunkModel[] createModels(TestChunk[] chunks) {
            TestChunkModel[] models = new TestChunkModel[chunks.length];
            for ( int i = 0; i < models.length; i++ ) {
                models[i] = new TestChunkModel(chunks[i]);
            }
            return models;
        }

        public static interface ModelListener {
            void modelChanged(TestChunkModel model);
        } // type ModelListener
    } // type TestChunkModel

    private static class TestChunkDetailPane {
        @SuppressWarnings("unused")
        private static final Color FAIL_COUNT_BACKGROUND_COLOR =
            new Color(255, 192, 192);

        private final TestChunkModel testChunkModel;
        private JComponent visualComponent;

        private JTextField titleTF;
        private JTextField statusTF;

        private JTextPane logTP;
        private StyledDocument styledDocument;
        private Style baseStyle;
        private Style errorStyle;

        public TestChunkDetailPane(TestChunkModel testChunkModel) {
            this.testChunkModel = testChunkModel;
            createUi();

            testChunkModel.addModelListener(new TestChunkModel.ModelListener() {
                @Override
                public void modelChanged(TestChunkModel model) {
                    updateUi(model);
                }
            });

            testChunkModel.getTestChunk().addListener(new TestChunk.StandardBaseListener() {
                @Override
                protected void println(final boolean isError, final String line) {
                    appendLine(isError, line);
                }
            });
        }

        public JComponent getVisualComponent() {
            return visualComponent;
        }

        private void createUi() {
            confirmEventThread();

            titleTF = createUneditableTextField(15);
            statusTF = createUneditableTextField(15);

            titleTF.setText(testChunkModel.getTitle());
            titleTF.setCaretPosition(0);

            logTP = new JTextPane();
            logTP.setEditable(false);
            styledDocument = logTP.getStyledDocument();
            baseStyle = logTP.addStyle("style001", null);
            StyleConstants.setFontFamily(baseStyle, Font.MONOSPACED);
            errorStyle = logTP.addStyle("style002", baseStyle);
            StyleConstants.setBackground(errorStyle, new Color(255, 192, 192));

            JPanel topP = new JPanel(new GridLayout(0, 1, 3, 3));
            topP.add(titleTF);
            topP.add(statusTF);

            JScrollPane sp = new JScrollPane(logTP);
            JPanel p = new JPanel(new BorderLayout(3, 3));
            p.add(topP, BorderLayout.NORTH);
            p.add(sp, BorderLayout.CENTER);

            visualComponent = p;
        }

       private void updateUi(final TestChunkModel model) {
           if ( SwingUtilities.isEventDispatchThread() == false ) {
               SwingUtilities.invokeLater(new Runnable() {
                   @Override
                   public void run() {
                       updateUi(model);
                   }
               });
               return;
           }

           String text = String.format(
               "%s - %,d passed, %,d failed, %,d total",
               model.getState().getDisplayName(),
               model.getPassCount(),
               model.getFailCount(),
               model.getTotalCount()
               );
           if (model.isPassCountExceedsExpectedPassCount()) {
               text += " !!! Pass Count has exceeded the Expected Pass Count !!!";
           }
           statusTF.setText(text);
       }

       private void appendLine(final boolean isError, final String line) {
           if ( SwingUtilities.isEventDispatchThread() == false ) {
               SwingUtilities.invokeLater(new Runnable() {
                   @Override
                   public void run() {
                       appendLine(isError, line);
                   }
               });
               return;
           }
           try {
               Style style = isError ? errorStyle : baseStyle;
               styledDocument.insertString(
                   styledDocument.getLength(), line + "\n", style);
           } catch ( BadLocationException x ) {
               x.printStackTrace();
           }
       }

       public static TestChunkDetailPane[] createPanes(TestChunkModel[] models) {
           TestChunkDetailPane[] panes = new TestChunkDetailPane[models.length];
           for ( int i = 0; i < panes.length; i++ ) {
               panes[i] = new TestChunkDetailPane(models[i]);
           }
           return panes;
       }
    } // type TestChunkDetailPane
}
