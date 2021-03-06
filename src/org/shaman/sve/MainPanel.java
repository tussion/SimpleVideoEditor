/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shaman.sve;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.undo.UndoableEditSupport;
import org.shaman.sve.model.Project;
import org.shaman.sve.player.Player;

/**
 *
 * @author Sebastian
 */
public class MainPanel extends javax.swing.JPanel implements PropertyChangeListener {
	private static final Logger LOG = Logger.getLogger(MainPanel.class.getName());
	
	private Project project;
	private Player player;
	private UndoableEditSupport undoSupport;
	private Selections selections;
	private Timer timer;
	
	private FrameTime currentTime;
	
	/**
	 * Creates new form MainPanel
	 */
	public MainPanel() {
		initComponents();
		timer = new Timer(1000 / 25, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contentPanel.repaint();
			}
		});
		timer.setRepeats(true);
		timer.start();
	}

	public void setProject(Project project) {
		this.project = project;
		project.addPropertyChangeListener(this);
		currentTime = new FrameTime(project.getFramerate());
		timer.setDelay(1000 / project.getFramerate());
	}
	
	public void setUndoSupport(UndoableEditSupport undoSupport) {
		this.undoSupport = undoSupport;
	}

	public void setSelections(Selections selections) {
		this.selections = selections;
	}

	public void setPlayer(Player player) {
		this.player = player;
		player.addPropertyChangeListener(this);
		setTotalTime(project.getLength());
		setCurrentTime(project.getTime());
	}

	private void setPlaying(boolean playing) {
		playButton.setEnabled(!playing);
		timeSpinner.setEnabled(!playing);
		timeSlider.setEnabled(!playing);
		aSpinner.setEnabled(!playing);
		gotoAButton.setEnabled(!playing);
//		setAButton.setEnabled(!playing);
		bSpinner.setEnabled(!playing);
		gotoBButton.setEnabled(!playing);
//		setBButton.setEnabled(!playing);
	}
	
	/**
	 * Sets the total time in the UI
	 */
	private void setTotalTime(FrameTime msec) {
		LOG.log(Level.INFO, "set total time to {0} ms", msec);
		//check current time
		boolean fireTimeChange = false;
		if (currentTime.compareTo(msec) > 0) {
			currentTime.fromMillis(0);
			fireTimeChange = true;
		}
		
		timeLabel.setText("/ "+msec+" s");
		timeSpinner.setModel(currentTime.getSpinnerModel(new FrameTime(project.getFramerate()).fromMillis(0), msec));
		aSpinner.setModel(project.getMarkerA().getSpinnerModel(new FrameTime(project.getFramerate()).fromMillis(0), msec));
		bSpinner.setModel(project.getMarkerB().getSpinnerModel(new FrameTime(project.getFramerate()).fromMillis(0), msec));
		timeSlider.setMinimum(0);
		timeSlider.setMaximum(msec.toMillis());
		timeSlider.setValue(currentTime.toMillis());
		
		if (fireTimeChange) {
			changeCurrentTime(currentTime);
		}
	}
	
	/**
	 * Sets the current time in the UI
	 */
	private void setCurrentTime(FrameTime msec) {
		if (msec == currentTime) return;
		currentTime = msec;
		timeSpinner.setValue(msec);
		timeSlider.setValue(msec.toMillis());
//		LOG.log(Level.INFO, "set current time to {0} ms", msec);
	}
	
	/**
	 * Sends the current time to the player
	 * @param time 
	 */
	private void changeCurrentTime(FrameTime time) {
		project.setTime(time);
		if (player != null) {
			player.setTime(time);
		}
		LOG.log(Level.INFO, "change current time to {0} ms", time);
	}
	
	private void paintContent(Graphics2D g) {
		if (player == null || project == null) {
			return;
		}
		if (player.isRecording()) {
			return;
		}
		//transform graphics to view the whole screen
		int sw = contentPanel.getWidth();
		int sh = contentPanel.getHeight();
		int pw = project.getWidth();
		int ph = project.getHeight();
		float scale = Math.min(sw / (float) pw, sh / (float) ph);
		pw *= scale;
		ph *= scale;
		int ox = (sw - pw) / 2;
		int oy = (sh - ph) / 2;
		AffineTransform at = g.getTransform();
		g.translate(ox, oy);
		g.scale(scale, scale);
		
		//send to player
		player.draw(g);
		
		//reset transform
		g.setTransform(at);
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contentPanel = new ContentPanel();
        playButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        timeSpinner = new javax.swing.JSpinner();
        timeLabel = new javax.swing.JLabel();
        timeSlider = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        aSpinner = new javax.swing.JSpinner();
        setAButton = new javax.swing.JButton();
        gotoAButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        bSpinner = new javax.swing.JSpinner();
        setBButton = new javax.swing.JButton();
        gotoBButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setMinimumSize(new java.awt.Dimension(400, 300));

        javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setHorizontalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        contentPanelLayout.setVerticalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 336, Short.MAX_VALUE)
        );

        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/shaman/sve/icons/play16.png"))); // NOI18N
        playButton.setToolTipText("Play");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playEvent(evt);
            }
        });

        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/shaman/sve/icons/stop16.png"))); // NOI18N
        stopButton.setToolTipText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopEvent(evt);
            }
        });

        jLabel1.setText(" time:");

        timeSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(10)));
        timeSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                timeSpinnerChanged(evt);
            }
        });

        timeLabel.setText("/ 0 s");

        timeSlider.setMajorTickSpacing(1000);
        timeSlider.setMinorTickSpacing(10);
        timeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                timeSliderChanged(evt);
            }
        });

        jLabel2.setText("A:");

        aSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(10)));
        aSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                aSpinnerChanged(evt);
            }
        });

        setAButton.setText("Set");
        setAButton.setToolTipText("Sets the first marker");
        setAButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setAEvent(evt);
            }
        });

        gotoAButton.setText("GoTo");
        gotoAButton.setToolTipText("go to marker A");
        gotoAButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gotoAEvent(evt);
            }
        });

        jLabel3.setText("B:");

        bSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(10)));
        bSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bSpinnerChanged(evt);
            }
        });

        setBButton.setText("Set");
        setBButton.setToolTipText("Sets the first marker");
        setBButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setBButtonsetAEvent(evt);
            }
        });

        gotoBButton.setText("GoTo");
        gotoBButton.setToolTipText("go to marker A");
        gotoBButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gotoBButtongotoAEvent(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setAButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gotoAButton)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setBButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gotoBButton)
                        .addGap(0, 91, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(playButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopButton)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(playButton)
                    .addComponent(stopButton)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(aSpinner)
                    .addComponent(setAButton)
                    .addComponent(gotoAButton)
                    .addComponent(jLabel3)
                    .addComponent(bSpinner)
                    .addComponent(setBButton)
                    .addComponent(gotoBButton)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void playEvent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playEvent
		player.setTime(project.getTime());
		player.start(false);
    }//GEN-LAST:event_playEvent

    private void stopEvent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopEvent
        if (player.isPlaying()) {
			player.stop();
		} else if (currentTime.toMillis() != 0) {
			FrameTime ft = new FrameTime(project.getFramerate());
			setCurrentTime(ft);
			changeCurrentTime(ft);
		}
    }//GEN-LAST:event_stopEvent

    private void timeSpinnerChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_timeSpinnerChanged
        FrameTime ft = (FrameTime) timeSpinner.getValue();
		if (ft.equals(currentTime)) {
			return;
		}
		currentTime = ft;
		timeSlider.setValue(ft.toMillis());
		changeCurrentTime(ft);
    }//GEN-LAST:event_timeSpinnerChanged

    private void timeSliderChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_timeSliderChanged
        int msec = timeSlider.getValue();
		FrameTime ft = new FrameTime(project.getFramerate()).fromMillis(msec);
		if (ft.equals(currentTime)) return;
		currentTime = ft;
		timeSpinner.setValue(ft);
		changeCurrentTime(ft);
    }//GEN-LAST:event_timeSliderChanged

    private void aSpinnerChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_aSpinnerChanged
        FrameTime ft = (FrameTime) aSpinner.getValue();
		if (ft.equals(project.getMarkerA())) {
			return;
		}
		project.setMarkerA(ft);
    }//GEN-LAST:event_aSpinnerChanged

    private void setAEvent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setAEvent
        project.setMarkerA(project.getTime().clone());
    }//GEN-LAST:event_setAEvent

    private void gotoAEvent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gotoAEvent
        changeCurrentTime(project.getMarkerA().clone());
    }//GEN-LAST:event_gotoAEvent

    private void bSpinnerChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bSpinnerChanged
        FrameTime ft = (FrameTime) bSpinner.getValue();
		if (ft.equals(project.getMarkerB())) {
			return;
		}
		project.setMarkerB(ft);
    }//GEN-LAST:event_bSpinnerChanged

    private void setBButtonsetAEvent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setBButtonsetAEvent
        project.setMarkerB(project.getTime().clone());
    }//GEN-LAST:event_setBButtonsetAEvent

    private void gotoBButtongotoAEvent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gotoBButtongotoAEvent
        changeCurrentTime(project.getMarkerB().clone());
    }//GEN-LAST:event_gotoBButtongotoAEvent


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner aSpinner;
    private javax.swing.JSpinner bSpinner;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JButton gotoAButton;
    private javax.swing.JButton gotoBButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton playButton;
    private javax.swing.JButton setAButton;
    private javax.swing.JButton setBButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JSlider timeSlider;
    private javax.swing.JSpinner timeSpinner;
    // End of variables declaration//GEN-END:variables

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == project) {
			if (player.isRecording()) {
				return;
			}
			switch (evt.getPropertyName()) {
				case Project.PROP_LENGTH:
					setTotalTime(project.getLength());
					break;
				case Project.PROP_TIME:
					setCurrentTime(project.getTime());
					break;
				case Project.PROP_MARKER_A:
					aSpinner.setValue(project.getMarkerA().clone());
					break;
				case Project.PROP_MARKER_B:
					bSpinner.setValue(project.getMarkerB().clone());
			}
		} else if (evt.getSource() == player) {
			switch (evt.getPropertyName()) {
				case Player.PROP_PLAYING:
					setPlaying((boolean) evt.getNewValue());
					break;
			}
		}
	}
	
	private class ContentPanel extends JPanel {

		@Override
		public void paint(Graphics g) {
			paintComponent(g);
			paintBorder(g);
			paintContent((Graphics2D) g);
		}
		
	}
}
