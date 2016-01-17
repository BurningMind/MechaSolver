import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class Engine implements ActionListener, Serializable {

    public Joint m_joint;
    public double m_speed;
    public Timer m_timer;
    public MainWindow m_mainWindow;
    public double m_currentAngle;
    public int m_freeSolid;

    private MySlider m_slider;

    public static final int FPS = 30;

    //Constructor
    public Engine (Joint joint, int freeSolid, double speed, MainWindow mainWindow) {
        m_joint = joint;
        m_speed = speed;
        m_freeSolid = freeSolid;

        for (int i =0; i< mainWindow.m_sliderJoints.size(); i++ ) {
            Pair<Joint, Integer> pair = mainWindow.m_sliderJoints.get(i);
            if (pair.a == m_joint && pair.b == m_freeSolid) {
                m_slider = mainWindow.m_sliders.get(i);
            }
        }

        if (m_joint.m_anchor.m_isGround) {
            m_currentAngle = (m_joint.m_freeSolids.get(freeSolid).m_angle - m_joint.m_anchor.m_angle) % (Math.toRadians(m_slider.getMaximum()));
        } else {
            m_currentAngle = (m_joint.m_freeSolids.get(freeSolid).m_angle - (m_joint.m_anchor.m_angle - Math.PI))% (Math.toRadians(m_slider.getMaximum()));
        }

        m_mainWindow = mainWindow;
        m_timer = new Timer(1000 / FPS, this);
        m_timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        m_currentAngle += Math.toRadians(m_speed / FPS);

        double modulo = Math.toRadians(m_slider.getMaximum());
        m_currentAngle %= modulo;

        m_slider.setValue((int)Math.toDegrees(m_currentAngle));
        m_mainWindow.setJointAngle(m_joint, m_freeSolid, m_currentAngle);

        m_mainWindow.repaint();
    }

}
