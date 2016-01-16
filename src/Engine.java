import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Engine implements ActionListener{

    public Joint m_joint;
    public double m_speed;
    public Timer monTimer;
    public MainWindow m_mainWindow;
    public double m_currentAngle;

    public static final int FPS = 30;

    //Constructor
    public Engine (Joint joint, double speed, MainWindow mainWindow) {
        m_joint = joint;
        m_speed = speed;

        if (m_joint.m_anchor.m_isGround) {
            m_currentAngle = ((m_joint.m_freeSolids.get(0).m_angle - m_joint.m_anchor.m_angle) + Math.PI*2) % (Math.PI*2);
        } else {
            m_currentAngle = ((m_joint.m_freeSolids.get(0).m_angle - (m_joint.m_anchor.m_angle - Math.PI)+Math.PI*2))% (Math.PI*2);
        }

        m_mainWindow = mainWindow;
        monTimer = new Timer(1000 / FPS, this);
        monTimer.start();
    }

    public void actionPerformed(ActionEvent e) {
        m_currentAngle += Math.toRadians(m_speed / FPS);
        m_currentAngle %= Math.PI * 2;

        m_mainWindow.m_sliders.get(m_joint.m_id).setValue((int)Math.toDegrees(m_currentAngle));
        m_mainWindow.setJointAngle(m_joint, 0, m_currentAngle);

        m_mainWindow.repaint();
    }

}
